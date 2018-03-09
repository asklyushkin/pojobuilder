package org.idea.plugin.builder.utils;

/**
 * Общие утилиты
 *
 * @author Klyushkin A. <asklyushkin@gmail.com>.
 **/
public class BuilderUtils
{
    public static String capitalize(String str)
    {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
