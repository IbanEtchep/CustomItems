package fr.iban.customitems;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.iban.customitems.attribute.CustomAttribute;
import fr.iban.customitems.attribute.handler.*;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CustomAttributeManager {

    private final CustomItemsPlugin plugin;
    private final NamespacedKey customAttributeKey;
    private final Gson gson;
    private final Type attributeMapType = new TypeToken<Map<String, String>>() {
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

        CustomAttribute.ARMOR_EFFECT.registerHandler(new ArmorEffectHandler(plugin))
                .addAttributeValueValidator("effect", "L'effet n'existe pas.", s -> {
                    try {
                        PotionEffectType.getByName(s);
                        return true;
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                })
                .addAttributeValueValidator("level", "Le niveau doit être un nombre positif", s -> {
                    try {
                        int level = Integer.parseInt(s);
                        return level > 0;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .addAttributeValueValidator("full_set", "La valeur doit être 'true' ou 'false'", s -> s.equals("true") || s.equals("false"))
                .addAttributeValueValidator("identifier", "L'identifiant ne peut pas être vide", s -> !s.isEmpty());
    }

    public Map<String, Map<String, String>> getAttributes(ItemStack itemStack) {
        Map<String, Map<String, String>> attributes = new HashMap<>();

        if (itemStack != null && itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            String data = itemMeta.getPersistentDataContainer().get(customAttributeKey, PersistentDataType.STRING);
            if (data != null) {
                String[] attributeEntries = data.split(";");
                for (String entry : attributeEntries) {
                    if (entry.contains(":")) {
                        String[] parts = entry.split(":", 2);
                        String attributeName = parts[0];
                        Map<String, String> values = gson.fromJson(parts[1], attributeMapType);
                        attributes.put(attributeName, values);
                    } else {
                        attributes.put(entry, new HashMap<>());
                    }
                }
            }
        }

        return attributes;
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
        StringBuilder dataBuilder = new StringBuilder();
        for (Map.Entry<String, Map<String, String>> entry : attributes.entrySet()) {
            if (entry.getValue().isEmpty()) {
                dataBuilder.append(entry.getKey()).append(";");
            } else {
                String jsonValues = gson.toJson(entry.getValue());
                dataBuilder.append(entry.getKey()).append(":").append(jsonValues).append(";");
            }
        }
        String data = dataBuilder.toString();
        if (data.endsWith(";")) {
            data = data.substring(0, data.length() - 1);
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(customAttributeKey, PersistentDataType.STRING, data);
        itemStack.setItemMeta(itemMeta);
    }

    public boolean hasAttribute(ItemStack itemStack, CustomAttribute attribute) {
        return getAttributes(itemStack).containsKey(attribute.toString());
    }

    public Map<String, String> getAttributeValues(ItemStack itemStack, CustomAttribute attribute) {
        return getAttributes(itemStack).getOrDefault(attribute.toString(), Collections.emptyMap());
    }
}
