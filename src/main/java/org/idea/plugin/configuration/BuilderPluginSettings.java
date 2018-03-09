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


    private BuilderPluginSettings(final Builder builder)
    {
        details = requireNonNull(builder.details);
        isJacksonEnabled = requireNonNull(builder.isJacksonEnabled);
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


    public static final class Builder
    {
        private BuilderDetails details;

        private Boolean isJacksonEnabled;


        private Builder()
        {
        }


        public Builder withDetails(final BuilderDetails val)
        {
            details = val;
            return this;
        }


        public Builder withIsJacksonEnabled(final Boolean val)
        {
            isJacksonEnabled = val;
            return this;
        }


        public BuilderPluginSettings build()
        {
            return new BuilderPluginSettings(this);
        }
    }
}
