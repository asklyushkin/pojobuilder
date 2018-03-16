package org.idea.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiElementFactory;
import org.idea.plugin.builder.BuilderManager;
import org.idea.plugin.configuration.BuilderPluginSettings;
import org.idea.plugin.implementation.DefaultBuilderPlugin;

/**
 * Фабрика имплементация плагинов.
 *
 * @author Klyushkin A. <asklyushkin@gmail.com>.
 **/
public class BuilderPluginFactory
{

    public static BuilderPlugin get(final BuilderPluginSettings settings, final Project project)
    {
        final PsiElementFactory psiElementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
        final BuilderManager builderManager = new BuilderManager(psiElementFactory);

        return new DefaultBuilderPlugin(settings, builderManager);
    }
}
