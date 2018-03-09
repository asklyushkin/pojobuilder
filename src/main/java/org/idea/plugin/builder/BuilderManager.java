package org.idea.plugin.builder;

import java.util.List;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiStatement;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiUtil;
import org.idea.plugin.builder.creator.BuilderCreator;
import org.idea.plugin.builder.creator.ConstructorCreator;
import org.idea.plugin.builder.creator.GettersCreator;

import static java.util.Objects.requireNonNull;

/**
 * Менеджер по взаимодействи с генераторами кода для основного класса и билдера.
 *
 * @author Klyushkin A. <asklyushkin@gmail.com>.
 **/
public class BuilderManager
{
    private final PsiElementFactory psiElementFactory;

    private final GettersCreator gettersCreator;

    private final ConstructorCreator constructorCreator;

    private final BuilderCreator builderCreator;


    public BuilderManager(final PsiElementFactory psiElementFactory)
    {
        this.psiElementFactory = psiElementFactory;
        this.gettersCreator = new GettersCreator(psiElementFactory);
        this.constructorCreator = new ConstructorCreator(psiElementFactory);
        this.builderCreator = new BuilderCreator(psiElementFactory);
    }


    /**
     * Генерирует список гетерров для филдов основного класса c анотациями для Jackson'а
     *
     * @param fields филды
     * @return Список геттеров.
     */
    public List<PsiMethod> createGettersWithJackson(final List<PsiFieldMember> fields)
    {
        return gettersCreator.createGetters(fields, true);
    }


    /**
     * Генерирует список гетерров для филдов основного класса
     *
     * @param fields филды
     * @return Список геттеров.
     */
    public List<PsiMethod> createGetters(final List<PsiFieldMember> fields)
    {
        return gettersCreator.createGetters(fields, false);
    }


    /**
     * Создает конструктор основного класса с Objects.requireNonNul над филдами конструктора.
     *
     * @param clazz  основной класс
     * @param fields филды
     * @return конструктор
     */
    public PsiMethod createConstructor(final PsiClass clazz, final List<PsiFieldMember> fields)
    {
        return constructorCreator.createConstructor(clazz, fields, false);
    }


    /**
     * Создает конструктор основного класса с JsonProperty над параметрами конструктора и JsonCreator на методом.
     *
     * @param clazz  основной класс
     * @param fields филды
     * @return конструктор
     */
    public PsiMethod createConstructorWithJackson(final PsiClass clazz, final List<PsiFieldMember> fields)
    {
        return constructorCreator.createConstructor(clazz, fields, true);
    }


    /**
     * Создает статический метод инициализации биллдера.
     *
     * @return Метод инициализации билдера.
     */
    public PsiMethod createBuilderMethod(final String builderClassName,
                                         final String builderMethodName)
    {
        final PsiType builderType = psiElementFactory.createTypeFromText(builderClassName, null);
        final PsiMethod builderMethod = psiElementFactory.createMethod(builderMethodName, builderType);

        PsiUtil.setModifierProperty(builderMethod, PsiModifier.STATIC, true);
        PsiUtil.setModifierProperty(builderMethod, PsiModifier.PUBLIC, true);

        final PsiStatement newStatement = psiElementFactory.createStatementFromText(String.format(
                "return new %s();", builderType.getPresentableText()), builderMethod);

        final PsiCodeBlock newBuilderMethodBody = requireNonNull(builderMethod.getBody());
        newBuilderMethodBody.add(newStatement);

        return builderMethod;
    }


    /**
     * Создает класс билдера без requireNonNull в методе build
     *
     * @param fields              филды
     * @param builderClassName    имя класса билдера
     * @param builderSetterPrefix префикс сеттеров
     * @param clazz               основной клас
     * @return класс билдера
     */
    public PsiClass createBuilderClass(final List<PsiFieldMember> fields,
                                       final String builderClassName,
                                       final String builderSetterPrefix,
                                       final PsiClass clazz)
    {

        return builderCreator.createBuilderClass(fields, builderClassName, builderSetterPrefix, clazz, false);
    }


    /**
     * Создает класс билдера с requireNonNull в методе build
     *
     * @param fields              филды
     * @param builderClassName    имя класса билдера
     * @param builderSetterPrefix префикс сеттеров
     * @param clazz               основной клас
     * @return класс билдера
     */
    public PsiClass createBuilderClassWithJackson(final List<PsiFieldMember> fields,
                                                  final String builderClassName,
                                                  final String builderSetterPrefix,
                                                  final PsiClass clazz)
    {

        return builderCreator.createBuilderClass(fields, builderClassName, builderSetterPrefix, clazz, true);
    }


}
