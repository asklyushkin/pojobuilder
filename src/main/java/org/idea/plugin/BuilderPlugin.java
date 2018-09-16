package org.idea.plugin;

import java.util.List;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;

/**
 * Интерфейс билдера.
 *
 * @author Klyushkin A. <asklyushkin@gmail.com>.
 **/
public interface BuilderPlugin
{
    /**
     * выполняет работые по генерации билдера и украшательству основного класса.
     */
    void process(final PsiClass targetClass,
                 final List<PsiFieldMember> fields,
                 final Project project,
                 final Editor editor,
                 final PsiFile psiFile);


}
