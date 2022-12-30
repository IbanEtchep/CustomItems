package fr.iban.customitems.attribute.handler;

import fr.iban.customitems.utils.BlockUtils;
import fr.iban.customitems.utils.ItemUtils;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class RangeMiningHandler implements AttributeHandler {

    public void onBreak(BlockBreakEvent event, BlockFace blockface) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack item = player.getInventory().getItemInMainHand();

        for (Block b : BlockUtils.getSurroundingBlocks(blockface, block, item)) {
            player.breakBlock(b);
        }
    }
}
