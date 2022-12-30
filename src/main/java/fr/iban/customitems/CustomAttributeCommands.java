package fr.iban.customitems;

import fr.iban.customitems.attribute.CustomAttribute;
import fr.iban.survivalcore.SurvivalCorePlugin;
import fr.iban.survivalcore.tools.SpecialTools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.List;

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
        if(item.getType() != Material.AIR) {
            sender.sendMessage("§b§lAttributs de l'item :");
            List<String> attributes = attributeManager.getAttributes(item);
            if(attributes.isEmpty()) {
                sender.sendMessage("Cet item n'a pas d'attributs.");
            }else {
                for (String attribute : attributes) {
                    sender.sendMessage("- " + attribute);
                }
            }
        }
    }

    @Subcommand("add")
    public void addAttribute(Player sender, CustomAttribute customAttribute) {
        ItemStack item = sender.getInventory().getItemInMainHand();
        if(item.getType() != Material.AIR) {
            List<String> attributes = attributeManager.getAttributes(item);
            if(attributes.contains(customAttribute.toString())) {
                sender.sendMessage("§cCet item a déjà cet attribut.");
            }else {
                sender.sendMessage("§aL'attribut a bien été ajouté.");
                attributeManager.addAttribute(item, customAttribute);
            }
        }
    }

    @Subcommand("remove")
    public void removeAttribute(Player sender, CustomAttribute customAttribute) {
        ItemStack item = sender.getInventory().getItemInMainHand();
        if(item.getType() != Material.AIR) {
            List<String> attributes = attributeManager.getAttributes(item);
            if(attributes.contains(customAttribute.toString())) {
                attributeManager.removeAttribute(item, customAttribute);
                sender.sendMessage("§aL'attribut a bien été retiré.");
            }else {
                sender.sendMessage("§cCet item n'a pas cet attribut.");
            }
        }
    }

}
