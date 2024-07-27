package fr.iban.customitems.attribute.handler;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import fr.iban.customitems.CustomItemsPlugin;
import fr.iban.customitems.attribute.CustomAttribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class ArmorEffectHandler implements AttributeHandler {

    private final CustomItemsPlugin plugin;

    public ArmorEffectHandler(CustomItemsPlugin plugin) {
        this.plugin = plugin;
    }

    public void handleArmorChange(PlayerArmorChangeEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = event.getNewItem();
        ItemStack oldItem = event.getOldItem();

        AttributeValues newValues = new AttributeValues(plugin.getAttributeManager().getAttributeValues(newItem, CustomAttribute.ARMOR_EFFECT));
        AttributeValues oldValues = new AttributeValues(plugin.getAttributeManager().getAttributeValues(oldItem, CustomAttribute.ARMOR_EFFECT));

        if (oldValues.isValid()) {
            removeEffect(player, oldValues.getEffect());
        }

        if (newValues.isValid()) {
            PotionEffectType effect = newValues.getEffect();
            if (effect != null) {
                if (newValues.requireFullSet()) {
                    String identifier = newValues.getIdentifier();
                    if (hasFullSet(player, identifier)) {
                        addEffect(player, effect, newValues.getLevel());
                    }
                } else {
                    addEffect(player, effect, newValues.getLevel());
                }
            }
        }
    }

    private boolean hasFullSet(Player player, String identifier) {
        for (ItemStack armorItem : player.getInventory().getArmorContents()) {
            AttributeValues armorValues = new AttributeValues(plugin.getAttributeManager().getAttributeValues(armorItem, CustomAttribute.ARMOR_EFFECT));
            if (!armorValues.isValid() || !armorValues.hasIdentifier(identifier)) {
                return false;
            }
        }
        return true;
    }

    private void addEffect(Player player, PotionEffectType effectType, int level) {
        PotionEffect effect = new PotionEffect(effectType, Integer.MAX_VALUE, level - 1, true, false);
        player.addPotionEffect(effect);
    }

    private void removeEffect(Player player, PotionEffectType effectType) {
        player.removePotionEffect(effectType);
    }

    private record AttributeValues(Map<String, String> values) {

        public boolean isValid() {
            return !values.isEmpty() && values.containsKey("effect");
        }

        public PotionEffectType getEffect() {
            return PotionEffectType.getByName(values.get("effect"));
        }

        public int getLevel() {
            return values.containsKey("level") ? Integer.parseInt(values.get("level")) : 1;
        }

        public boolean requireFullSet() {
            return values.containsKey("full_set") && Boolean.parseBoolean(values.get("full_set"));
        }

        public String getIdentifier() {
            return values.get("identifier");
        }

        public boolean hasIdentifier(String identifier) {
            return identifier != null && identifier.equals(this.getIdentifier());
        }
    }
}