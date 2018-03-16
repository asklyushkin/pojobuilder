package org.idea.plugin.configuration;

import static java.util.Objects.requireNonNull;

/**
 * Настроки для биледра.
 *
 * @author Klyushkin A. <asklyushkin@gmail.com>.
 **/
public class BuilderPluginSettings
{
    private final BuilderDetails details;

    private final Boolean isJacksonEnabled;

    private final Boolean isRequireNonNullInConstructorEnabled;

    private final Boolean isRequireNonNullBuilderEnabled;


    private BuilderPluginSettings(final BuilderDetails details,
                                  final Boolean isJacksonEnabled,
                                  final Boolean isRequireNonNullInConstructorEnabled,
                                  final Boolean isRequireNonNullBuilderEnabled)
    {
        this.details = requireNonNull(details);
        this.isJacksonEnabled = requireNonNull(isJacksonEnabled);
        this.isRequireNonNullInConstructorEnabled = requireNonNull(isRequireNonNullInConstructorEnabled);
        this.isRequireNonNullBuilderEnabled = requireNonNull(isRequireNonNullBuilderEnabled);
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public BuilderDetails getDetails()
    {
        return details;
    }


    public Boolean isJacksonEnabled()
    {
        return isJacksonEnabled;
    }


    public Boolean isRequireNonNullInConstructorEnabled()
    {
        return isRequireNonNullInConstructorEnabled;
    }


    public Boolean isRequireNonNullBuilderEnabled()
    {
        return isRequireNonNullBuilderEnabled;
    }


    public static final class Builder
    {
        private BuilderDetails details;

        private Boolean isJacksonEnabled;

        private Boolean isRequireNonNullInConstructorEnabled;

        private Boolean isRequireNonNullBuilderEnabled;


        public Builder withDetails(final BuilderDetails details)
        {
            this.details = details;
            return this;
        }


        public Builder isJacksonEnabled(final Boolean isJacksonEnabled)
        {
            this.isJacksonEnabled = isJacksonEnabled;
            return this;
        }


        public Builder isRequireNonNullInConstructorEnabled(final Boolean isRequireNonNullInConstructorEnabled)
        {
            this.isRequireNonNullInConstructorEnabled = isRequireNonNullInConstructorEnabled;
            return this;
        }


        public Builder isRequireNonNullBuilderEnabled(final Boolean isRequireNonNullBuilderEnabled)
        {
            this.isRequireNonNullBuilderEnabled = isRequireNonNullBuilderEnabled;
            return this;
        }


        public BuilderPluginSettings build()
        {
            return new BuilderPluginSettings(details, isJacksonEnabled, isRequireNonNullInConstructorEnabled, isRequireNonNullBuilderEnabled);
        }
    }
}
