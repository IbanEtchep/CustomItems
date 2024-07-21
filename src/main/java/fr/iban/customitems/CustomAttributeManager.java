package fr.iban.customitems;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.iban.customitems.attribute.CustomAttribute;
import fr.iban.customitems.attribute.handler.*;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CustomAttributeManager {

    private final CustomItemsPlugin plugin;
    private final NamespacedKey customAttributeKey;
    private final Gson gson;
    private final Type attributeMapType = new TypeToken<Map<String, Map<String, String>>>() {
    }.getType();

    public CustomAttributeManager(CustomItemsPlugin plugin) {
        this.plugin = plugin;
        customAttributeKey = new NamespacedKey(plugin, "custom_attribute");
        gson = new Gson();
        registerHandlers(plugin);
    }

    public void registerHandlers(CustomItemsPlugin plugin) {
        CustomAttribute.MELT_MINING.registerHandler(new MeltMiningHandler(plugin));
        CustomAttribute.RANGE_MINING.registerHandler(new RangeMiningHandler(plugin));
        CustomAttribute.RANGE_HARVEST.registerHandler(new RangeHarvestHandler());
        CustomAttribute.HARVEST_REPLANT.registerHandler(new HarvestReplantHandler(plugin));
        CustomAttribute.TREE_CUT.registerHandler(new TreeCutHandler(plugin));
        CustomAttribute.POWER_BOOTS.registerHandler(new PowerBootsHandler());
        CustomAttribute.ANIMAL_CATCHER.registerHandler(new EntityCatcherHandler(plugin));
        CustomAttribute.VILLAGER_CATCHER.registerHandler(new EntityCatcherHandler(plugin));
        CustomAttribute.FERTILIZE.registerHandler(new FertilizeHandler());
        CustomAttribute.RANGE_FERTILIZE.registerHandler(new RangeFertilizeHandler());

        CustomAttribute.REQUIRE_JOB_LEVEL.registerHandler(new RequireJobLevelHandler(plugin))
                .addAttributeValueValidator("level", "Le niveau doit être un nombre.", s -> {
                    try {
                        Integer.parseInt(s);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                });
    }

    public Map<String, Map<String, String>> getAttributes(ItemStack itemStack) {
        if (itemStack.hasItemMeta()) {
            String json = itemStack.getItemMeta().getPersistentDataContainer().get(customAttributeKey, PersistentDataType.STRING);
            if (json != null) {
                return gson.fromJson(json, attributeMapType);
            }
        }
        return new HashMap<>();
    }

    public void setAttribute(ItemStack itemStack, CustomAttribute attribute, Map<String, String> values) {
        Map<String, Map<String, String>> attributes = getAttributes(itemStack);
        attributes.put(attribute.toString(), values);
        setAttributes(itemStack, attributes);
    }

    public void addAttribute(ItemStack itemStack, CustomAttribute attribute) {
        Map<String, Map<String, String>> attributes = getAttributes(itemStack);
        if (!attributes.containsKey(attribute.toString())) {
            attributes.put(attribute.toString(), new HashMap<>());
            setAttributes(itemStack, attributes);
        }
    }

    public void removeAttribute(ItemStack itemStack, CustomAttribute attribute) {
        Map<String, Map<String, String>> attributes = getAttributes(itemStack);
        attributes.remove(attribute.toString());
        setAttributes(itemStack, attributes);
    }

    public void setAttributes(ItemStack itemStack, Map<String, Map<String, String>> attributes) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (attributes.isEmpty()) {
            itemMeta.getPersistentDataContainer().remove(customAttributeKey);
        } else {
            String json = gson.toJson(attributes);
            itemMeta.getPersistentDataContainer().set(customAttributeKey, PersistentDataType.STRING, json);
        }
        itemStack.setItemMeta(itemMeta);
    }

    public boolean hasAttribute(ItemStack itemStack, CustomAttribute attribute) {
        return getAttributes(itemStack).containsKey(attribute.toString());
    }

    public Map<String, String> getAttributeValues(ItemStack itemStack, CustomAttribute attribute) {
        return getAttributes(itemStack).getOrDefault(attribute.toString(), Map.of());
    }
}
