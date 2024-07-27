package fr.iban.customitems.attribute;

import fr.iban.customitems.attribute.handler.AttributeHandler;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public enum CustomAttribute {

    HARVEST_REPLANT,
    FERTILIZE,
    RANGE_FERTILIZE,
    RANGE_HARVEST,
    RANGE_MINING,
    TREE_CUT,
    MELT_MINING,
    POWER_BOOTS,
    UNREPAIRABLE_BY_COMMAND,
    UNREPAIRABLE_BY_ANVIL,
    UNREPAIRABLE_BY_MENDING,
    ANIMAL_CATCHER,
    VILLAGER_CATCHER,
    ARMOR_EFFECT,
    REQUIRE_JOB_LEVEL;

    private AttributeHandler handler;
    private final Map<String, AttributeValueValidator> validators = new HashMap<>();

    public CustomAttribute registerHandler(AttributeHandler handler) {
        this.handler = handler;

        return this;
    }

    @Nullable
    public AttributeHandler getHandler() {
        return handler;
    }

    public CustomAttribute addAttributeValueValidator(String valueKey, String errorMessage, Predicate<String> predicate) {
        validators.put(valueKey, new AttributeValueValidator() {
            @Override
            public boolean validate(String value) {
                return predicate.test(value);
            }

            @Override
            public String errorMessage() {
                return errorMessage;
            }
        });

        return this;
    }

    @Nullable
    public String getValidatorError(String valueKey, String value) {
        AttributeValueValidator validator = validators.get(valueKey);
        if (validator == null) return null;
        return validator.validate(value) ? null : validator.errorMessage();
    }
}
