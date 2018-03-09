package org.idea.plugin.builder.jackson;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import static org.idea.plugin.builder.jackson.JacksonAnnotation.JSON_PROPERTY_ANNOTATION;

/**
 * Помощник для работы с Jackson'ом
 *
 * @author Klyushkin A. <asklyushkin@gmail.com>.
 **/
public class JacksonHelper
{
    /**
     * Создает анотацию JsonProperty
     *
     * @param variableName имя переменной
     * @return анотация JsonProperty
     */
    public static String createJsonPropertyAnnotation(@NotNull final String variableName)
    {
        Objects.requireNonNull(variableName);
        return JSON_PROPERTY_ANNOTATION + "(\"" + variableName + "\")";
    }
}
