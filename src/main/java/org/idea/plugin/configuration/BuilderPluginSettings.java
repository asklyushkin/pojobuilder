package org.idea.plugin.configuration;

import java.util.Objects;

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

    private final Boolean isBuilderEnabled;

    private final Boolean isToStringEnabled;


    private BuilderPluginSettings(final BuilderDetails details,
                                  final Boolean isJacksonEnabled,
                                  final Boolean isRequireNonNullInConstructorEnabled,
                                  final Boolean isRequireNonNullBuilderEnabled,
                                  final Boolean isBuilderEnabled,
                                  final Boolean isToStringEnabled)
    {
        this.details = Objects.requireNonNull(details);
        this.isJacksonEnabled = Objects.requireNonNull(isJacksonEnabled);
        this.isRequireNonNullInConstructorEnabled = Objects.requireNonNull(isRequireNonNullInConstructorEnabled);
        this.isRequireNonNullBuilderEnabled = Objects.requireNonNull(isRequireNonNullBuilderEnabled);
        this.isBuilderEnabled = Objects.requireNonNull(isBuilderEnabled);
        this.isToStringEnabled = Objects.requireNonNull(isToStringEnabled);
    }


    public static Builder builder()
    {
        return new Builder();
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


    public BuilderDetails getDetails()
    {
        return details;
    }


    public Boolean isBuilderEnabled()
    {
        return isBuilderEnabled;
    }


    public Boolean isToStringEnabled()
    {
        return isToStringEnabled;
    }


    public static final class Builder
    {
        private BuilderDetails details;

        private Boolean isJacksonEnabled;

        private Boolean isRequireNonNullInConstructorEnabled;

        private Boolean isRequireNonNullBuilderEnabled;

        private Boolean isBuilderEnabled;

        private Boolean isToStringEnabled;


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


        public Builder isBuilderEnabled(final Boolean isBuilderEnabled)
        {
            this.isBuilderEnabled = isBuilderEnabled;
            return this;
        }


        public Builder isToStringEnabled(final Boolean isToStringEnabled)
        {
            this.isToStringEnabled = isToStringEnabled;
            return this;
        }


        public BuilderPluginSettings build()
        {
            return new BuilderPluginSettings(
                    details,
                    isJacksonEnabled,
                    isRequireNonNullInConstructorEnabled,
                    isRequireNonNullBuilderEnabled,
                    isBuilderEnabled,
                    isToStringEnabled);
        }
    }
}
