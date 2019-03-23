package org.idea.plugin.builder.creator;

import java.util.List;
import java.util.stream.Collectors;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiStatement;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;
import static org.idea.plugin.builder.utils.BuilderUtils.capitalize;
import static org.idea.plugin.builder.utils.ClassAdder.addMethod;
import static org.idea.plugin.builder.utils.ClassAdder.addMethods;

/**
 * Создает билдер для основого класса.
 *
 * @author Klyushkin A. <asklyushkin@gmail.com>.
 **/
public class BuilderCreator
{
    private final PsiElementFactory psiElementFactory;


    public BuilderCreator(final PsiElementFactory psiElementFactory)
    {
        this.psiElementFactory = psiElementFactory;
    }


    public PsiClass createBuilderClass(final List<PsiFieldMember> fields,
                                       final String builderClassName,
                                       final String builderSetterPrefix,
                                       final PsiClass clazz,
                                       final boolean isJacksonEnable)
    {
        final PsiClass builderClass = psiElementFactory.createClass(builderClassName);
        PsiUtil.setModifierProperty(builderClass, PsiModifier.STATIC, true);
        PsiUtil.setModifierProperty(builderClass, PsiModifier.FINAL, true);
        PsiUtil.setModifierProperty(builderClass, PsiModifier.PUBLIC, true);

        for (final PsiFieldMember fieldMember : fields)
        {
            final PsiField fieldElement = fieldMember.getElement();
            final PsiField parameter =
                    psiElementFactory.createField(requireNonNull(fieldElement.getName()), fieldElement.getType());
            builderClass.add(parameter);
        }

        //generate builder `with` methods
        final List<PsiMethod> builderSetters = createBuilderSetters(fields, builderClassName, builderSetterPrefix);
        addMethods(builderClass, builderSetters);

        //generate build
        final PsiMethod buildMethod = createBuilderBuildMethod(clazz, fields, builderClass, isJacksonEnable);
        addMethod(builderClass, buildMethod);

        return builderClass;
    }


    private List<PsiMethod> createBuilderSetters(final List<PsiFieldMember> fields,
                                                 final String builderClassName,
                                                 final String builderSetterPrefix)
    {
        final PsiType builderType = psiElementFactory.createTypeFromText(builderClassName, null);
        return fields.stream()
                .map(field -> generateBuilderSetter(builderType, field, builderSetterPrefix))
                .collect(Collectors.toList());
    }


    private PsiMethod generateBuilderSetter(final PsiType builderType,
                                            final PsiFieldMember member,
                                            final String defaultBuilderSetterPrefix)
    {

        final String prefix = defaultBuilderSetterPrefix;
        final String fieldName = member.getElement().getName();
        final String methodName = getBuilderSetterMethodName(prefix, member);
        final PsiMethod setterMethod = psiElementFactory.createMethod(methodName, builderType);

        setterMethod.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);

        final PsiType fieldType = member.getElement().getType();
        final PsiParameter setterParameter = psiElementFactory.createParameter(fieldName, fieldType);
        requireNonNull(setterParameter.getModifierList()).setModifierProperty(PsiModifier.FINAL, true);
        setterMethod.getParameterList().add(setterParameter);

        final PsiCodeBlock setterMethodBody = getSetterBodyStatement(fieldName, setterMethod);
        setterMethodBody.add(psiElementFactory.createStatementFromText("return this;", setterMethod));
        return setterMethod;
    }


    private String getBuilderSetterMethodName(final String prefix, final PsiFieldMember fieldMember)
    {
        final String fieldName = fieldMember.getElement().getName();

        if (fieldName.startsWith("is"))
        {
            return fieldName;
        }
        final PsiType fieldType = fieldMember.getElement().getType();

        if (PsiType.BOOLEAN.equals(fieldType) || fieldType.equalsToText("Boolean"))
        {
            return String.format("%s%s", "is", capitalize(fieldName));
        }


        return String.format("%s%s", prefix, capitalize(fieldName));
    }


    @NotNull
    private PsiCodeBlock getSetterBodyStatement(final String fieldName, final PsiMethod setterMethod)
    {
        final PsiCodeBlock setterMethodBody = setterMethod.getBody();
        final String textStatement = String.format("this.%s = %s;", fieldName, fieldName);
        final PsiStatement assignStatement = psiElementFactory.createStatementFromText(textStatement, setterMethod);

        requireNonNull(setterMethodBody).add(assignStatement);

        return setterMethodBody;
    }


    private PsiMethod createBuilderBuildMethod(final PsiClass clazz,
                                               final List<PsiFieldMember> fields,
                                               final PsiClass builderClass,
                                               final boolean isRequireNonNullEnabled)
    {
        final PsiType clazzType = psiElementFactory.createTypeFromText(requireNonNull(clazz.getName()), null);
        final PsiMethod buildMethod = psiElementFactory.createMethod("build", clazzType);
        buildMethod.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);


        requireNonNull(buildMethod.getBody()).add(getBuildMethodStatement(fields, clazz, builderClass, isRequireNonNullEnabled));

        return buildMethod;
    }


    private PsiStatement getBuildMethodStatement(final List<PsiFieldMember> fields,
                                                 final PsiClass clazz,
                                                 final PsiClass builderClass,
                                                 final boolean isJacksonEnabled)
    {
        if (isJacksonEnabled)
        {
            return getBuildMethodStatementWithJackson(fields, clazz, builderClass);
        }

        return getBuildMethodStatement(fields, clazz, builderClass);
    }


    private PsiStatement getBuildMethodStatementWithJackson(final List<PsiFieldMember> fields,
                                                            final PsiClass clazz,
                                                            final PsiClass builderClass)
    {
        final StringBuilder builderMethodStatementText = new StringBuilder();
        builderMethodStatementText.append("return new ").append(clazz.getName()).append("(");

        fields.forEach(psiFieldMember ->
                builderMethodStatementText.append("Objects.requireNonNull(")
                        .append(psiFieldMember.getElement().getName())
                        .append("),"));

        builderMethodStatementText.deleteCharAt(builderMethodStatementText.length() - 1);
        builderMethodStatementText.append(");");

        return psiElementFactory.createStatementFromText(builderMethodStatementText.toString(), builderClass);
    }


    private PsiStatement getBuildMethodStatement(final List<PsiFieldMember> fields,
                                                 final PsiClass clazz,
                                                 final PsiClass builderClass)
    {
        final StringBuilder builderMethodStatementText = new StringBuilder();
        builderMethodStatementText.append("return new ").append(clazz.getName()).append("(");

        fields.forEach(psiFieldMember ->
                builderMethodStatementText
                        .append(psiFieldMember.getElement().getName())
                        .append(","));

        builderMethodStatementText.deleteCharAt(builderMethodStatementText.length() - 1);
        builderMethodStatementText.append(");");

        return psiElementFactory.createStatementFromText(builderMethodStatementText.toString(), builderClass);
    }
}
