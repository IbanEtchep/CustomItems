package fr.iban.customitems.attribute.handler;

import fr.iban.customitems.utils.BlockUtils;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RangeFertilizeHandler extends FertilizeHandler {


    public void onFertilize(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        ItemStack item = e.getItem();

        if (item == null || block == null) return;

        fertilize(block, player, item);

        for (Block b : BlockUtils.getSurroundingBlocks(BlockFace.UP, block)) {
            fertilize(b, player, item);
        }
    }
}
