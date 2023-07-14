package fr.iban.customitems.attribute.handler;

import fr.iban.customitems.CustomItemsPlugin;
import fr.iban.customitems.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashSet;
import java.util.Set;

public class HarvestReplantHandler implements AttributeHandler {

    private final CustomItemsPlugin plugin;
    private final Set<String> disabledWorld;

    public HarvestReplantHandler(CustomItemsPlugin plugin) {
        this.plugin = plugin;
        this.disabledWorld = new HashSet<>(plugin.getConfig().getStringList("auto-replant.disabled-worlds"));
    }

    public void handleReplant(BlockBreakEvent e) {
        Block block = e.getBlock();
        Material type = block.getType();
        BlockData blockData = block.getBlockData();
        Ageable age = (Ageable) blockData;
        Player player = e.getPlayer();

        if(disabledWorld.contains(block.getWorld().getName())) {
            return;
        }

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
