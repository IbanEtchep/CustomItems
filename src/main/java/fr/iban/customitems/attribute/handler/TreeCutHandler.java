package fr.iban.customitems.attribute.handler;

import fr.iban.customitems.CustomAttributeManager;
import fr.iban.customitems.CustomItemsPlugin;
import fr.iban.customitems.attribute.CustomAttribute;
import fr.iban.customitems.utils.ItemUtils;
import fr.iban.customitems.utils.MaterialUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TreeCutHandler implements AttributeHandler {

    public void cutTree(Player player, Block block, CustomAttributeManager manager) {
        List<Block> blocks = new ArrayList<>();

        for (Block b = block; !b.isEmpty(); b = b.getRelative(BlockFace.UP)) {

            for (int k = -1; k <= 1; k++) {
                for (int j = -1; j <= 1; j++) {
                    for (int l = -1; l <= 1; l++) {
                        final Block relativeBlock = b.getRelative(j, k, l);

                        if (MaterialUtils.isLog(relativeBlock.getType()))
                            blocks.add(relativeBlock);
                    }
                }
            }
        }

        int count = 0;
        for (final Block b : blocks) {
            Bukkit.getScheduler().runTaskLater(CustomItemsPlugin.getInstance(), () ->
            {
                if(manager.hasAttribute(player.getInventory().getItemInMainHand(), CustomAttribute.TREE_CUT)) {
                    player.breakBlock(b);
                }
            }, ++count);
        }
    }

}
