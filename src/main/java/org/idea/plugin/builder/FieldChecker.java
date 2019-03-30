package org.idea.plugin.builder;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.psi.PsiType;

/**
 * Provider some checks for fields
 *
 * @author Klyushkin A. <asklyushkin@gmail.com>.
 **/
public class FieldChecker
{

    /**
     * Check that field has prefix 'is' in field name
     * <p>
     * Example: isEnabled, isTest
     *
     * @param fieldName field name
     * @return True - if the field has prefix
     */
    public static boolean hasIsPrefix(final String fieldName)
    {
        if (!fieldName.startsWith("is"))
        {
            return false;
        }

        final char[] chars = fieldName.substring(2).toCharArray();
        if (chars.length == 0)
        {
            return false;
        }
        return Character.isUpperCase(chars[0]);
    }


    /**
     * Check that field has {@link Boolean} type
     *
     * @param field field
     * @return True - if the field is {@link Boolean}
     */
    public static boolean isBooleanType(final PsiFieldMember field)
    {
        final PsiType fieldType = field.getElement().getType();

        return PsiType.BOOLEAN.equals(fieldType) || fieldType.equalsToText("java.lang.Boolean");
    }
}
