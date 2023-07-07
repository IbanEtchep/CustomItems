package fr.iban.customitems;

import fr.iban.customitems.attribute.*;
import fr.iban.customitems.attribute.handler.*;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class CustomAttributeManager {

    private final CustomItemsPlugin plugin;
    private final NamespacedKey customAttributeKey;

    public CustomAttributeManager(CustomItemsPlugin plugin) {
        this.plugin = plugin;
        customAttributeKey = new NamespacedKey(plugin, "custom_attribute");
        registerHandlers(plugin);
    }

    public void registerHandlers(CustomItemsPlugin plugin) {
        CustomAttribute.MELT_MINING.registerHandler(new MeltMiningHandler());
        CustomAttribute.RANGE_MINING.registerHandler(new RangeMiningHandler(plugin));
        CustomAttribute.RANGE_HARVEST.registerHandler(new RangeHarvestHandler());
        CustomAttribute.HARVEST_REPLANT.registerHandler(new HarvestReplantHandler());
        CustomAttribute.TREE_CUT.registerHandler(new TreeCutHandler());
        CustomAttribute.POWER_BOOTS.registerHandler(new PowerBootsHandler());
    }

    public List<String> getAttributes(ItemStack itemStack) {
        List<String> attributes = new ArrayList<>();
        if(itemStack.hasItemMeta()) {
            String attributesString = itemStack.getItemMeta().getPersistentDataContainer().get(customAttributeKey, PersistentDataType.STRING);
            if (attributesString != null) {
                attributes.addAll(Arrays.stream(attributesString.split(";")).filter(a -> !a.equals("")).toList());
            }
        }
        return attributes;
    }

    public void addAttribute(ItemStack itemStack, CustomAttribute attribute) {
        List<String> attributes = getAttributes(itemStack);
        attributes.add(attribute.toString());
        setAttributes(itemStack, attributes);
    }

    public void removeAttribute(ItemStack itemStack, CustomAttribute attribute) {
        List<String> attributes = getAttributes(itemStack);
        attributes.remove(attribute.toString());
        setAttributes(itemStack, attributes);
    }

    public void setAttributes(ItemStack itemStack, List<String> attributes) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (attributes.isEmpty()) {
            itemMeta.getPersistentDataContainer().remove(customAttributeKey);
        } else {
            itemMeta.getPersistentDataContainer().set(customAttributeKey, PersistentDataType.STRING, String.join(";", attributes));
        }
        itemStack.setItemMeta(itemMeta);
    }

    public boolean hasAttribute(ItemStack itemStack, CustomAttribute attribute) {
        return getAttributes(itemStack).contains(attribute.toString());
    }


}
