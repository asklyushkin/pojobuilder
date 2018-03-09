package org.idea.plugin.builder.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

/**
 * Добавлятель методов и классов в основной класс.
 *
 * @author Klyushkin A. <asklyushkin@gmail.com>.
 **/
public class ClassAdder
{
    public static void addMethods(final PsiClass clazz, final List<PsiMethod> methods)
    {
        methods.forEach(method ->
        {
            Arrays.stream(clazz.findMethodsByName(method.getName(), false)).forEach(
                    foundedMethod ->
                    {
                        System.out.println("founded method: " + foundedMethod.getName());
                        foundedMethod.delete();
                        System.out.println("deleted method: " + foundedMethod.getName());
                    }
            );
            System.out.println(String.format("add method: %s in class %s", method.getName(), clazz.getName()));
            clazz.add(method);
        });
    }


    public static void addMethod(final PsiClass clazz, final PsiMethod methods)
    {
        addMethods(clazz, Collections.singletonList(methods));
    }


    public static void addClass(final PsiClass clazz, final PsiClass psiClass)
    {
        final PsiClass innerClass = clazz.findInnerClassByName(psiClass.getName(), false);
        if (innerClass != null)
        {
            System.out.println("founded class: " + psiClass.getName());
            innerClass.delete();
            System.out.println("deleted class: " + psiClass.getName());
        }

        System.out.println("add class: " + psiClass.getName());
        clazz.add(psiClass);
    }
}
