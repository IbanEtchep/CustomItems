package fr.iban.customitems.attribute.handler;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class FertilizeHandler implements AttributeHandler {


    public void onFertilize(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        ItemStack item = e.getItem();

        if (item == null || block == null) return;

        fertilize(block, player, item);
    }

    protected void fertilize(Block block, Player player, ItemStack item) {
        if (block.getType().toString().startsWith("GRASS")) return;

        boolean result = block.applyBoneMeal(BlockFace.UP);

        if (result) {
            player.damageItemStack(item, 1);
        }
    }
}
