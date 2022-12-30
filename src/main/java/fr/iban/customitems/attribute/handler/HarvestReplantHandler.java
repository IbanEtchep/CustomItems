package fr.iban.customitems.attribute.handler;

import fr.iban.customitems.CustomItemsPlugin;
import fr.iban.customitems.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class HarvestReplantHandler implements AttributeHandler {

    public void handleReplant(BlockBreakEvent e, CustomItemsPlugin plugin) {
        Block block = e.getBlock();
        Material type = block.getType();
        BlockData blockData = block.getBlockData();
        Ageable age = (Ageable) blockData;
        Player player = e.getPlayer();

        if (age.getAge() == age.getMaximumAge()) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                block.setType(type);
                ItemUtils.damageItem(player.getInventory().getItemInMainHand(), player);
            }, 1L);
        } else {
            e.setCancelled(true);
        }
    }

}
