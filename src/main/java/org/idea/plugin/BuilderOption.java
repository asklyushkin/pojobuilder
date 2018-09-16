package org.idea.plugin;

public enum BuilderOption
{

    IS_JACKSON_ENABLED("IS_JACKSON_ENABLED"),
    REQUIRE_NON_NULL_IN_CONSTRUCTOR("REQUIRE_NON_NULL_IN_CONSTRUCTOR"),
    REQUIRE_NON_NULL_IN_BUILDER("REQUIRE_NON_NULL_IN_BUILDER"),
    IS_BUILDER_ENABLED("IS_BUILDER_ENABLED"),
    IS_TO_STRING_ENABLED("IS_TO_STRING_ENABLED"),
    ;

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
