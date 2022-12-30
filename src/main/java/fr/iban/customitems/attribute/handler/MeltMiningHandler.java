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
        meltAndDrop(e, newDrop, xp, loc, fortuneMultiply, 1);
    }

    private void meltAndDrop(BlockBreakEvent e, Material newDrop, double xp, Location loc, boolean fortuneMultiply, int amountToDrop) {
        Player player = e.getPlayer();
        ItemStack toDrop = new ItemStack(newDrop);
        int expToDrop = 0;

        //Centre du bloc :
        loc.add(0.5, 0.5, 0.5);

        if (fortuneMultiply) {
            int fortuneLevel = player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
            amountToDrop = getFortuneMultiplier(fortuneLevel);
            toDrop.setAmount(amountToDrop);
        }

        for (int i = 0; i < amountToDrop; i++) {
            if (xp >= 1) {
                expToDrop += xp;
            } else {
                if (Math.random() <= xp) {
                    expToDrop++;
                }
            }
        }

        e.setDropItems(false);
        e.setExpToDrop(expToDrop);
        player.getWorld().dropItem(loc, toDrop);
        player.getWorld().spawnParticle(Particle.FLAME, loc, 10, 0.3, 0.3, 0.3, 0.02);
    }

    private int getFortuneMultiplier(int fortunelevel) {
        Random rand = new Random();
        int alz2 = rand.nextInt(100 + 1);
        switch (fortunelevel) {

            case (3):
                if (alz2 <= 20 && alz2 >= 1) {
                    return 2;
                } else if (alz2 <= 40 && alz2 > 20) {
                    return 3;
                } else if (alz2 <= 60 && alz2 > 40) {
                    return 4;
                } else {
                    return 1;
                }
            case (2):
                if (alz2 <= 25 && alz2 >= 1) {
                    return 2;
                } else if (alz2 <= 50 && alz2 > 25) {
                    return 3;
                } else {
                    return 1;
                }
            case (1):
                if (alz2 <= 33 && alz2 >= 1) {
                    return 2;
                } else {
                    return 1;
                }
        }

        return 1;
    }
}
