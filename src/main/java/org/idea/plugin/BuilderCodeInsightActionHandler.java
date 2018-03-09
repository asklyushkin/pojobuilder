package org.idea.plugin;

import java.util.List;
import java.util.stream.Collectors;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.lang.LanguageCodeInsightActionHandler;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiUtil;
import org.idea.plugin.builder.FieldsCollector;
import org.idea.plugin.configuration.BuilderDetails;
import org.idea.plugin.configuration.BuilderPluginSettings;
import org.jetbrains.annotations.NotNull;

import static org.idea.plugin.BuilderOptionSelector.selectFieldsAndOptions;

/**
 * Implementation {@link LanguageCodeInsightActionHandler} for BuilderPlugin
 *
 * @author Klyushkin A. <asklyushkin@gmail.com>.
 **/
public class BuilderCodeInsightActionHandler implements LanguageCodeInsightActionHandler
{

    private static final String BUILDER_CLASS_NAME = "Builder";

    private static final String BUILDER_SETTER_PREFIX = "with";

    private static final String BUILDER_METHOD_NAME = "builder";


    @Override
    public boolean startInWriteAction()
    {
        return false;
    }


    @Override
    public void invoke(@NotNull final Project project, @NotNull final Editor editor, @NotNull final PsiFile psiFile)
    {
        System.out.println("invoke");
        final List<PsiFieldMember> fields = FieldsCollector.collectFields(psiFile, editor);
        if (fields.isEmpty())
        {
            return;
        }

        final List<String> foundedFields = fields.stream()
                .map(psiFieldMember -> psiFieldMember.getElement().getName())
                .collect(Collectors.toList());
        System.out.println("Founded fields: " + foundedFields);

        final PsiClass targetClass = PsiUtil.getTopLevelClass(fields.get(0).getElement());
        if (targetClass == null)
        {
            return;
        }

        final List<PsiFieldMember> selectedFields = selectFieldsAndOptions(fields, project);
        if (selectedFields.isEmpty())
        {
            return;
        }

        WriteCommandAction.runWriteCommandAction(project, () ->
        {

            final BuilderDetails builderDetails = BuilderDetails.builder()
                    .withBuilderClassName(BUILDER_CLASS_NAME)
                    .withBuilderMethodName(BUILDER_METHOD_NAME)
                    .withBuilderSetterPrefix(BUILDER_SETTER_PREFIX)
                    .build();
            final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();

            final BuilderPluginSettings pluginSettings = BuilderPluginSettings.builder()
                    .withDetails(builderDetails)
                    .withIsJacksonEnabled(propertiesComponent.getBoolean(BuilderOption.IS_JACKSON_ENABLED.getProperty()))
                    .build();

            final BuilderPlugin builderPlugin = BuilderPluginFactory.get(pluginSettings, project);
            builderPlugin.process(targetClass, selectedFields);

            System.out.println("process ended");

            JavaCodeStyleManager.getInstance(project).shortenClassReferences(psiFile);
            CodeStyleManager.getInstance(project).reformat(targetClass);
        });
    }


    @Override
    public boolean isValidFor(final Editor editor, final PsiFile psiFile)
    {
        return true;
    }
}
