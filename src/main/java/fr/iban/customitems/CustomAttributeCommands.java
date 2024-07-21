package fr.iban.customitems;

import fr.iban.customitems.attribute.CustomAttribute;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.Map;

@Command({"customattribute"})
@CommandPermission("customattribute.admin")
public class CustomAttributeCommands {

    private final CustomItemsPlugin plugin;
    private final CustomAttributeManager attributeManager;

    public CustomAttributeCommands(CustomItemsPlugin plugin) {
        this.plugin = plugin;
        this.attributeManager = plugin.getAttributeManager();
    }

    @Subcommand("list")
    public void list(Player sender) {
        ItemStack item = sender.getInventory().getItemInMainHand();
        if (item.getType() != Material.AIR) {
            sender.sendMessage("§b§lAttributs de l'item :");
            Map<String, Map<String, String>> attributes = attributeManager.getAttributes(item);
            if (attributes.isEmpty()) {
                sender.sendMessage("Cet item n'a pas d'attributs.");
            } else {
                for (Map.Entry<String, Map<String, String>> attributeEntry : attributes.entrySet()) {
                    sender.sendMessage("§e- " + attributeEntry.getKey() + " :");
                    for (Map.Entry<String, String> valueEntry : attributeEntry.getValue().entrySet()) {
                        sender.sendMessage("  §6* " + valueEntry.getKey() + " : " + valueEntry.getValue());
                    }
                }
            }
        }
    }

    @Subcommand("add")
    public void addAttribute(Player sender, CustomAttribute customAttribute) {
        ItemStack item = sender.getInventory().getItemInMainHand();
        if (item.getType() != Material.AIR) {
            Map<String, Map<String, String>> attributes = attributeManager.getAttributes(item);
            if (attributes.containsKey(customAttribute.toString())) {
                sender.sendMessage("§cCet item a déjà cet attribut.");
            } else {
                sender.sendMessage("§aL'attribut a bien été ajouté.");
                attributeManager.addAttribute(item, customAttribute);
            }
        }
    }

    @Subcommand("addValue")
    public void addValue(Player sender, CustomAttribute customAttribute, String key, String value) {
        ItemStack item = sender.getInventory().getItemInMainHand();
        if (item.getType() != Material.AIR) {
            if (customAttribute.getValidatorError(key, value) != null) {
                sender.sendMessage("§c" + customAttribute.getValidatorError(key, value));
                return;
            }

            Map<String, Map<String, String>> attributes = attributeManager.getAttributes(item);
            if (attributes.containsKey(customAttribute.toString())) {
                attributes.get(customAttribute.toString()).put(key, value);
                attributeManager.setAttributes(item, attributes);
                sender.sendMessage("§aLa valeur a bien été ajoutée à l'attribut.");
            } else {
                sender.sendMessage("§cCet attribut n'existe pas. Ajoutez d'abord l'attribut.");
            }
        }
    }

    @Subcommand("remove")
    public void removeAttribute(Player sender, CustomAttribute customAttribute) {
        ItemStack item = sender.getInventory().getItemInMainHand();
        if (item.getType() != Material.AIR) {
            Map<String, Map<String, String>> attributes = attributeManager.getAttributes(item);
            if (attributes.containsKey(customAttribute.toString())) {
                attributeManager.removeAttribute(item, customAttribute);
                sender.sendMessage("§aL'attribut a bien été retiré.");
            } else {
                sender.sendMessage("§cCet item n'a pas cet attribut.");
            }
        }
    }

    @Subcommand("removeValue")
    public void removeAttributeValue(Player sender, CustomAttribute customAttribute, String key) {
        ItemStack item = sender.getInventory().getItemInMainHand();
        if (item.getType() != Material.AIR) {
            Map<String, Map<String, String>> attributes = attributeManager.getAttributes(item);
            if (attributes.containsKey(customAttribute.toString()) && attributes.get(customAttribute.toString()).containsKey(key)) {
                attributes.get(customAttribute.toString()).remove(key);
                attributeManager.setAttributes(item, attributes);
                sender.sendMessage("§aLa valeur a bien été retirée de l'attribut.");
            } else {
                sender.sendMessage("§cCet attribut ou cette valeur n'existe pas.");
            }
        }
    }
}
