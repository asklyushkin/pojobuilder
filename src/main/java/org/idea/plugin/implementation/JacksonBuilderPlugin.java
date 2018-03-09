package org.idea.plugin.implementation;

import java.util.List;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.idea.plugin.builder.BuilderManager;
import org.idea.plugin.configuration.BuilderPluginSettings;

/**
 * Имплементация {@link org.idea.plugin.BuilderPlugin} для Jackson'а
 *
 * @author Klyushkin A. <asklyushkin@gmail.com>.
 **/
public class JacksonBuilderPlugin extends AbstractBuilderPluginClass
{
    public JacksonBuilderPlugin(final BuilderPluginSettings settings, final BuilderManager builderManager)
    {
        super(settings, builderManager);
    }


    @Override
    protected PsiMethod createConstructor(final PsiClass clazz, final List<PsiFieldMember> fields)
    {
        return builderManager.createConstructorWithJackson(clazz, fields);
    }


    @Override
    protected List<PsiMethod> createGetters(final List<PsiFieldMember> fields)
    {
        return builderManager.createGettersWithJackson(fields);
    }


    @Override
    protected PsiClass createBuilderClass(final List<PsiFieldMember> fields, final PsiClass clazz)
    {
        final String builderClassName = settings.getDetails().getBuilderClassName();
        final String builderSetterPrefix = settings.getDetails().getBuilderSetterPrefix();

        return builderManager.createBuilderClassWithJackson(fields, builderClassName, builderSetterPrefix, clazz);
    }
}
