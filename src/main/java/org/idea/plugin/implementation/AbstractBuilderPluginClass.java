package org.idea.plugin.implementation;

import java.util.List;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.idea.plugin.BuilderPlugin;
import org.idea.plugin.builder.BuilderManager;
import org.idea.plugin.configuration.BuilderPluginSettings;

import static org.idea.plugin.builder.utils.ClassAdder.addClass;
import static org.idea.plugin.builder.utils.ClassAdder.addMethod;
import static org.idea.plugin.builder.utils.ClassAdder.addMethods;

/**
 * Абстрактная (стандартная) имлпементация плагина билдера.
 *
 * @author Klyushkin A. <asklyushkin@gmail.com>.
 **/
public class AbstractBuilderPluginClass implements BuilderPlugin
{
    protected final BuilderPluginSettings settings;

    protected final BuilderManager builderManager;


    public AbstractBuilderPluginClass(final BuilderPluginSettings settings, final BuilderManager builderManager)
    {
        this.settings = settings;
        this.builderManager = builderManager;
    }


    @Override
    public void process(final PsiClass targetClass, final List<PsiFieldMember> fields)
    {
        final PsiMethod constructor = createConstructor(targetClass, fields);
        addMethod(targetClass, constructor);

        final List<PsiMethod> getters = createGetters(fields);
        addMethods(targetClass, getters);

        final PsiMethod builderMethod = createBuilderMethod();
        addMethod(targetClass, builderMethod);

        //generate builder
        final PsiClass builderClass = createBuilderClass(fields, targetClass);
        addClass(targetClass, builderClass);
    }


    protected PsiMethod createConstructor(final PsiClass clazz, final List<PsiFieldMember> fields)
    {
        return builderManager.createConstructor(clazz, fields);
    }


    protected List<PsiMethod> createGetters(final List<PsiFieldMember> fields)
    {
        return builderManager.createGetters(fields);
    }


    protected PsiMethod createBuilderMethod()
    {
        final String builderClassName = settings.getDetails().getBuilderClassName();
        final String builderMethodName = settings.getDetails().getBuilderMethodName();

        return builderManager.createBuilderMethod(builderClassName, builderMethodName);
    }


    protected PsiClass createBuilderClass(final List<PsiFieldMember> fields, final PsiClass clazz)
    {
        final String builderClassName = settings.getDetails().getBuilderClassName();
        final String builderSetterPrefix = settings.getDetails().getBuilderSetterPrefix();

        return builderManager.createBuilderClass(fields, builderClassName, builderSetterPrefix, clazz);
    }
}
