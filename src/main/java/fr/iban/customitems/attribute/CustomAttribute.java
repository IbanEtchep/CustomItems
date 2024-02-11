package fr.iban.customitems.attribute;

import fr.iban.customitems.attribute.handler.AttributeHandler;
import org.jetbrains.annotations.Nullable;

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
    VILLAGER_CATCHER;

    private AttributeHandler handler;

    public void registerHandler(AttributeHandler handler) {
        this.handler = handler;
    }

    @Nullable
    public AttributeHandler getHandler() {
        return handler;
    }
}
