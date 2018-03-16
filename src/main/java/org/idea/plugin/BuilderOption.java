package org.idea.plugin;

public enum BuilderOption
{

    IS_JACKSON_ENABLED("IS_JACKSON_ENABLED"),
    REQUIRE_NON_NULL_IN_CONSTRUCTOR("REQUIRE_NON_NULL_IN_CONSTRUCTOR"),
    REQUIRE_NON_NULL_IN_BUILDER("REQUIRE_NON_NULL_IN_BUILDER"),;

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
