package org.idea.plugin.configuration;

import static java.util.Objects.requireNonNull;

/**
 * Основные детали билдера
 *
 * @author Klyushkin A. <asklyushkin@gmail.com>.
 **/
public class BuilderDetails
{
    private final String builderClassName;

    private final String builderMethodName;

    private final String builderSetterPrefix;


    private BuilderDetails(final Builder builder)
    {
        builderClassName = requireNonNull(builder.builderClassName);
        builderMethodName = requireNonNull(builder.builderMethodName);
        builderSetterPrefix = requireNonNull(builder.builderSetterPrefix);
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public String getBuilderClassName()
    {
        return builderClassName;
    }


    public String getBuilderMethodName()
    {
        return builderMethodName;
    }


    public String getBuilderSetterPrefix()
    {
        return builderSetterPrefix;
    }


    public static final class Builder
    {
        private String builderClassName;

        private String builderMethodName;

        private String builderSetterPrefix;


        private Builder()
        {
        }


        public Builder withBuilderClassName(final String val)
        {
            builderClassName = val;
            return this;
        }


        public Builder withBuilderMethodName(final String val)
        {
            builderMethodName = val;
            return this;
        }


        public Builder withBuilderSetterPrefix(final String val)
        {
            builderSetterPrefix = val;
            return this;
        }


        public BuilderDetails build()
        {
            return new BuilderDetails(this);
        }
    }
}
