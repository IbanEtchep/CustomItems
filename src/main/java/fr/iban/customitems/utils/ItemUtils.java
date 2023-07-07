package fr.iban.customitems.utils;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class ItemUtils {

//    public static void handleBlockBreak(Player player, Block block, ItemStack item) {
//        BlockBreakEvent blockBreakEvent = new BlockBreakEvent(block, player);
//        blockBreakEvent.callEvent();
//        if (!blockBreakEvent.isCancelled()) {
//            if(blockBreakEvent.isDropItems()) {
//                block.breakNaturally(item);
//            }else {
//                block.breakNaturally(new ItemStack(Material.DIRT));
//            }
//            ItemUtils.damageItem(item, player);
//        }
//    }

    public static void damageItem(ItemStack itemStack, Player player) {
        if(itemStack.getItemMeta() instanceof Damageable damageable) {
            double rand = Math.random() * 100;
            double breakChance = (100.0 / (damageable.getEnchantLevel(Enchantment.DURABILITY) + 1));
            if (rand <= breakChance) {
                damageable.setDamage(damageable.getDamage() + 1);
                new PlayerItemDamageEvent(player, itemStack, 1, 1).callEvent();
                if (damageable.getDamage() > itemStack.getType().getMaxDurability()) {
                    itemStack.setAmount(0);
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                }
                itemStack.setItemMeta(damageable);
            }
        }
    }

}
