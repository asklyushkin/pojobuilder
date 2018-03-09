package org.idea.plugin;

public enum BuilderOption
{

    IS_JACKSON_ENABLED("IS_JACKSON_ENABLED");

    private final String property;


    BuilderOption(final String property)
    {
        this.property = String.format("PojoBuilder.%s", property);
    }


    public String getProperty()
    {
        return property;
    }
}
