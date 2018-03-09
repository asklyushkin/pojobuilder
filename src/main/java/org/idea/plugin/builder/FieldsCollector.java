package org.idea.plugin.builder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiVariable;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Collect fields for builder generating.
 *
 * @author Klyushkin A. <asklyushkin@gmail.com>.
 **/
public class FieldsCollector
{
    public static List<PsiFieldMember> collectFields(final PsiFile file, final Editor editor)
    {
        PsiElement[] variables = PsiTreeUtil.collectElements(file, isFinalFieldsVariable());

        return Arrays.stream(variables)
                .map(variable -> (PsiField) variable)
                .map(PsiFieldMember::new)
                .collect(Collectors.toList());
    }


    @NotNull
    private static PsiElementFilter isFinalFieldsVariable()
    {
        return e ->
        {
            if (!(e instanceof PsiField))
            {
                return false;
            }

            final PsiModifierList modifierList = ((PsiVariable) e).getModifierList();
            return modifierList != null &&
                    modifierList.hasModifierProperty(PsiModifier.FINAL) &&
                    !modifierList.hasModifierProperty(PsiModifier.STATIC);
        };
    }
}
