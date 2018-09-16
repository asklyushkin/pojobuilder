package org.idea.plugin.implementation;

import java.util.List;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import org.idea.plugin.BuilderPlugin;
import org.idea.plugin.builder.BuilderManager;
import org.idea.plugin.configuration.BuilderPluginSettings;
import org.jetbrains.java.generate.GenerateToStringActionHandlerImpl;

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
                        final Editor editor, final PsiFile psiFile)
    {
        final Boolean isJacksonEnabled = settings.isJacksonEnabled();
        final Boolean isRequireNonNullInConstructorEnabled = settings.isRequireNonNullInConstructorEnabled();

        final PsiMethod constructor = builderManager.createConstructor(
                targetClass, fields, isJacksonEnabled, isRequireNonNullInConstructorEnabled);
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
            final GenerateToStringActionHandlerImpl generateToStringActionHandler = new GenerateToStringActionHandlerImpl();
            generateToStringActionHandler.invoke(project, editor, psiFile);
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
