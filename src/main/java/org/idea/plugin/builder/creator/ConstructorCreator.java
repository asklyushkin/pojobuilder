package org.idea.plugin.builder.creator;

import java.util.List;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiStatement;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;
import static org.idea.plugin.builder.jackson.JacksonAnnotation.JSON_CREATOR_ANNOTATION;
import static org.idea.plugin.builder.jackson.JacksonHelper.createJsonPropertyAnnotation;

/**
 * Создает конструктор для основного класса.
 *
 * @author Klyushkin A. <asklyushkin@gmail.com>.
 **/
public class ConstructorCreator
{
    private final PsiElementFactory psiElementFactory;


    public ConstructorCreator(final PsiElementFactory psiElementFactory)
    {
        this.psiElementFactory = psiElementFactory;
    }


    public PsiMethod createConstructor(final PsiClass clazz,
                                       final List<PsiFieldMember> fields,
                                       final boolean isJacksonEnabled)
    {
        final PsiMethod constructor = createEmptyConstructor(clazz, isJacksonEnabled);

        final PsiCodeBlock constructorBody = requireNonNull(constructor.getBody());
        for (final PsiFieldMember member : fields)
        {
            final String fieldName = requireNonNull(member.getElement().getName());

            constructorBody.add(createConstructorFieldStatement(fieldName, isJacksonEnabled));

            final PsiParameter parameter = createConstructorFieldParameter(member, fieldName, isJacksonEnabled);
            constructor.getParameterList().add(parameter);
        }

        return constructor;
    }


    @NotNull
    private PsiParameter createConstructorFieldParameter(final PsiFieldMember member,
                                                         final String fieldName,
                                                         final boolean isJacksonEnabled)
    {
        final PsiParameter parameter = psiElementFactory.createParameter(fieldName, member.getElement().getType());
        requireNonNull(parameter.getModifierList()).setModifierProperty(PsiModifier.FINAL, true);

        if (isJacksonEnabled)
        {
            requireNonNull(parameter.getModifierList()).addAnnotation(createJsonPropertyAnnotation(fieldName));
        }

        return parameter;
    }


    @NotNull
    private PsiStatement createConstructorFieldStatement(final String fieldName, final boolean isJacksonEnabled)
    {
        if (isJacksonEnabled)
        {
            final String assignText = String.format("this.%1$s = %1$s;", fieldName);
            return psiElementFactory.createStatementFromText(assignText, null);
        }

        final String assignText = String.format("this.%1$s = Objects.requireNonNull(%1$s);", fieldName);
        return psiElementFactory.createStatementFromText(assignText, null);
    }


    @NotNull
    private PsiMethod createEmptyConstructor(final PsiClass clazz, final boolean isJacksonEnabled)
    {
        final PsiMethod constructor = psiElementFactory.createConstructor(requireNonNull(clazz.getName()));
        constructor.getModifierList().setModifierProperty(PsiModifier.PRIVATE, true);
        if (isJacksonEnabled)
        {
            constructor.getModifierList().addAnnotation(JSON_CREATOR_ANNOTATION);
        }
        return constructor;
    }
}
