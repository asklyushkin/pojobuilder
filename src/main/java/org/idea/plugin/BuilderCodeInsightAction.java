package org.idea.plugin;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.BaseCodeInsightAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * BuilderCodeInsightAction Action Class.
 *
 * @author Klyushkin A. <asklyushkin@gmail.com>.
 **/
public class BuilderCodeInsightAction extends BaseCodeInsightAction
{
    private final BuilderCodeInsightActionHandler handler = new BuilderCodeInsightActionHandler();


    @Override
    public void actionPerformed(AnActionEvent e)
    {
        System.out.println("hello from builder");
        super.actionPerformed(e);
    }


    @NotNull
    @Override
    protected CodeInsightActionHandler getHandler()
    {
        return handler;
    }


    @Override
    protected boolean isValidForFile(@NotNull final Project project, @NotNull final Editor editor, @NotNull final PsiFile file)
    {
        return true;
    }
}
