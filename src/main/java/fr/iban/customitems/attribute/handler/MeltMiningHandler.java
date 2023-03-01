package fr.iban.customitems.attribute.handler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class MeltMiningHandler implements AttributeHandler {

    public void meltBlock(BlockBreakEvent e) {
        Block block = e.getBlock();
        Location loc = block.getLocation();
        switch (block.getType()) {
            case GOLD_ORE, DEEPSLATE_GOLD_ORE -> meltAndDrop(e, Material.GOLD_INGOT, 1, loc, true);
            case IRON_ORE, DEEPSLATE_IRON_ORE -> meltAndDrop(e, Material.IRON_INGOT, 0.7, loc, true);
            case ANCIENT_DEBRIS -> meltAndDrop(e, Material.NETHERITE_SCRAP, 2, loc, false);
            case NETHER_GOLD_ORE -> meltAndDrop(e, Material.GOLD_INGOT, 1, loc, false);
            case COPPER_ORE, DEEPSLATE_COPPER_ORE -> meltAndDrop(e, Material.COPPER_INGOT, 0.7, loc, true);
            default -> {
            }
        }
    }

    private void meltAndDrop(BlockBreakEvent e, Material newDrop, double xp, Location loc, boolean fortuneMultiply) {
        Player player = e.getPlayer();
        ItemStack toDrop = new ItemStack(newDrop);
        int expToDrop = 0;

        //Centre du bloc :
        loc.add(0.5, 0.5, 0.5);

        if (fortuneMultiply) {
            for (ItemStack drop : e.getBlock().getDrops(e.getPlayer().getInventory().getItemInMainHand())) {
                toDrop.setAmount(drop.getAmount());
            }
        }

        if (xp >= 1) {
            expToDrop += xp;
        } else {
            if (Math.random() <= xp) {
                expToDrop++;
            }
        }

        e.setDropItems(false);
        e.setExpToDrop(expToDrop);
        player.getWorld().dropItem(loc, toDrop);
        player.getWorld().spawnParticle(Particle.FLAME, loc, 10, 0.3, 0.3, 0.3, 0.02);
    }
}
