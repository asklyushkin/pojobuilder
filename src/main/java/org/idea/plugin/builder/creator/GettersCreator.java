package org.idea.plugin.builder.creator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiStatement;
import org.idea.plugin.builder.FieldChecker;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;
import static org.idea.plugin.builder.jackson.JacksonHelper.createJsonPropertyAnnotation;
import static org.idea.plugin.builder.utils.BuilderUtils.capitalize;

/**
 * Создает геттеры для основного класса.
 *
 * @author Klyushkin A. <asklyushkin@gmail.com>.
 **/
public class GettersCreator
{
    private static final Pattern IS_FIELD_PATTERN = Pattern.compile("is([A-Z])+");

    private final PsiElementFactory psiElementFactory;


    public GettersCreator(final PsiElementFactory psiElementFactory)
    {
        this.psiElementFactory = psiElementFactory;
    }


    public List<PsiMethod> createGetters(final List<PsiFieldMember> fields, boolean isJacksonEnabled)
    {
        final List<PsiMethod> getters = new ArrayList<>();

        fields.forEach(field ->
        {
            final String fieldName = requireNonNull(field.getElement().getName());
            System.out.println("Try generate getter for field: " + fieldName);

            final String getterName = getGetterName(field);
            final PsiMethod method = psiElementFactory.createMethod(getterName, field.getElement().getType());
            requireNonNull(method.getBody()).add(getGetterStatement(fieldName, method));

            if (isJacksonEnabled)
            {
                requireNonNull(method.getModifierList()).addAnnotation(createJsonPropertyAnnotation(fieldName));
            }

            System.out.println("generated getter: " + method.getName());
            getters.add(method);
        });

        return getters;
    }


    @NotNull
    private String getGetterName(final PsiFieldMember fieldMember)
    {
        final String fieldName = Objects.requireNonNull(fieldMember.getElement().getName());

        if (FieldChecker.hasIsPrefix(fieldName))
        {
            return fieldName;
        }

        if (FieldChecker.isBooleanType(fieldMember))
        {
            return "is" + capitalize(fieldName);
        }

        return "get" + capitalize(fieldName);
    }


    private PsiStatement getGetterStatement(final String fieldName, final PsiMethod method)
    {
        final String getterStatementText = String.format("return %s;", fieldName);
        return psiElementFactory.createStatementFromText(getterStatementText, method);
    }


}
