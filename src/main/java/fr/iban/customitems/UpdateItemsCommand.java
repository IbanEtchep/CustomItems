package fr.iban.customitems;

import fr.iban.customitems.attribute.CustomAttribute;
import fr.iban.survivalcore.tools.SpecialTools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.List;


public class UpdateItemsCommand {

    private final CustomAttributeManager attributeManager;

    public UpdateItemsCommand(CustomItemsPlugin plugin) {
        this.attributeManager = plugin.getAttributeManager();
    }

    @Command("updateitems")
    public void updateItems(Player sender) {
        int count = 0;
        for (ItemStack itemStack : sender.getInventory().getContents()) {
            if(itemStack == null) continue;
            if(SpecialTools.is3x3ReplantHoue(itemStack)) {
                if(!attributeManager.hasAttribute(itemStack, CustomAttribute.HARVEST_REPLANT) && !attributeManager.hasAttribute(itemStack, CustomAttribute.RANGE_HARVEST)) {
                    attributeManager.addAttribute(itemStack, CustomAttribute.HARVEST_REPLANT);
                    attributeManager.addAttribute(itemStack, CustomAttribute.RANGE_HARVEST);
                    sender.sendMessage("§aHoue 3x3 qui replante convertie.");
                    count++;
                }
            }
            if(SpecialTools.isReplantHoue(itemStack)) {
                if(!attributeManager.hasAttribute(itemStack, CustomAttribute.HARVEST_REPLANT)) {
                    attributeManager.addAttribute(itemStack, CustomAttribute.HARVEST_REPLANT);
                    sender.sendMessage("§aHoue qui replante convertie.");
                    count++;
                }
            }
            if(SpecialTools.is3x3Pickaxe(itemStack)) {
                if(!attributeManager.hasAttribute(itemStack, CustomAttribute.RANGE_MINING)) {
                    attributeManager.addAttribute(itemStack, CustomAttribute.RANGE_MINING);
                    sender.sendMessage("§aPioche 3x3 convertie.");
                    count++;
                }
            }
            if(SpecialTools.isLumberjackAxe(itemStack)) {
                if(!attributeManager.hasAttribute(itemStack, CustomAttribute.TREE_CUT)) {
                    attributeManager.addAttribute(itemStack, CustomAttribute.TREE_CUT);
                    sender.sendMessage("§aHache coupe arbre convertie.");
                    count++;
                }
            }
            if(SpecialTools.is3x3Shovel(itemStack)) {
                if(!attributeManager.hasAttribute(itemStack, CustomAttribute.RANGE_MINING)) {
                    attributeManager.addAttribute(itemStack, CustomAttribute.RANGE_MINING);
                    sender.sendMessage("§aPelle 3x3 convertie.");
                    count++;
                }
            }
            if(SpecialTools.isCutCleanPickaxe(itemStack)) {
                if(!attributeManager.hasAttribute(itemStack, CustomAttribute.MELT_MINING)) {
                    attributeManager.addAttribute(itemStack, CustomAttribute.MELT_MINING);
                    sender.sendMessage("§aPioche qui fait fondre convertie.");
                    count++;
                }
            }
        }
        if(count == 0) {
            sender.sendMessage("§cVous n'avez pas d'item à convertir dans votre inventaire.");
        }
    }

}
