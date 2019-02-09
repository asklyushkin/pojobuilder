package org.idea.plugin.implementation;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.codeInsight.hint.HintManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import org.idea.plugin.BuilderPlugin;
import org.idea.plugin.builder.BuilderManager;
import org.idea.plugin.configuration.BuilderPluginSettings;
import org.jetbrains.java.generate.GenerateToStringActionHandlerImpl;
import org.jetbrains.java.generate.GenerateToStringWorker;
import org.jetbrains.java.generate.GenerationUtil;
import org.jetbrains.java.generate.config.ReplacePolicy;
import org.jetbrains.java.generate.template.TemplateResource;
import org.jetbrains.java.generate.template.toString.ToStringTemplatesManager;

import static org.idea.plugin.builder.utils.ClassAdder.addClass;
import static org.idea.plugin.builder.utils.ClassAdder.addMethod;
import static org.idea.plugin.builder.utils.ClassAdder.addMethods;

/**
 * Абстрактная (стандартная) имлпементация плагина билдера.
 *
 * @author Klyushkin A. <asklyushkin@gmail.com>.
 **/
public class DefaultBuilderPlugin implements BuilderPlugin
{
    private final BuilderPluginSettings settings;

    private final BuilderManager builderManager;


    public DefaultBuilderPlugin(final BuilderPluginSettings settings, final BuilderManager builderManager)
    {
        this.settings = settings;
        this.builderManager = builderManager;
    }


    @Override
    public void process(final PsiClass targetClass,
                        final List<PsiFieldMember> fields,
                        final Project project,
                        final Editor editor,
                        final PsiFile psiFile)
    {
        final Boolean isJacksonEnabled = settings.isJacksonEnabled();
        final Boolean isRequireNonNullInConstructorEnabled = settings.isRequireNonNullInConstructorEnabled();

        final PsiMethod constructor = builderManager.createConstructor(
                targetClass,
                fields,
                isJacksonEnabled,
                isRequireNonNullInConstructorEnabled,
                settings.isBuilderEnabled());

        addMethod(targetClass, constructor);

        final List<PsiMethod> getters = builderManager.createGetters(fields, isJacksonEnabled);
        addMethods(targetClass, getters);

        if (settings.isBuilderEnabled())
        {
            final String builderClassName = settings.getDetails().getBuilderClassName();
            final String builderMethodName = settings.getDetails().getBuilderMethodName();

            createBuilder(targetClass, fields, builderClassName, builderMethodName);
        }

        if (settings.isToStringEnabled())
        {
//            final GenerateToStringActionHandlerImpl generateToStringActionHandler = new GenerateToStringActionHandlerImpl();
//            generateToStringActionHandler.invoke(project, editor, psiFile);


            final Collection<PsiMember> selectedMembers = GenerationUtil.convertClassMembersToPsiMembers(
                    Arrays.stream(GenerateToStringActionHandlerImpl.buildMembersToShow(targetClass))
                            .collect(Collectors.toList()));

            final ToStringTemplatesManager toStringTemplatesManager = new ToStringTemplatesManager();
            TemplateResource template = toStringTemplatesManager.getDefaultTemplate();

            if (template.isValidTemplate())
            {
                final GenerateToStringWorker worker = new GenerateToStringWorker(targetClass, editor, true);

                try
                {
                    worker.execute(selectedMembers, template, ReplacePolicy.getInstance());
                }
                catch (Exception var11)
                {
                    GenerationUtil.handleException(project, var11);
                }
            }
            else
            {
                HintManager.getInstance().showErrorHint(
                        editor,
                        "toString() template '" + template.getFileName() + "' is invalid");
            }
        }
    }


    private void createBuilder(final PsiClass targetClass,
                               final List<PsiFieldMember> fields,
                               final String builderClassName, final String builderMethodName)
    {
        final PsiMethod builderMethod = builderManager.createBuilderMethod(builderClassName, builderMethodName);
        addMethod(targetClass, builderMethod);

        //generate builder
        final String builderSetterPrefix = settings.getDetails().getBuilderSetterPrefix();

        final PsiClass builderClass = builderManager.createBuilderClass(
                fields, builderClassName, builderSetterPrefix, targetClass, settings.isRequireNonNullBuilderEnabled());
        addClass(targetClass, builderClass);
    }


}
