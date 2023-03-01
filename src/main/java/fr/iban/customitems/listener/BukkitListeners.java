package fr.iban.customitems.listener;

import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import fr.iban.customitems.CustomAttributeManager;
import fr.iban.customitems.CustomItemsPlugin;
import fr.iban.customitems.attribute.CustomAttribute;
import fr.iban.customitems.attribute.handler.*;
import fr.iban.customitems.utils.MaterialUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BukkitListeners implements Listener {

    private final CustomItemsPlugin plugin;
    private final CustomAttributeManager attributeManager;
    private final Map<UUID, BlockFace> faces = new HashMap<>();
    private final Set<UUID> clickedBlockPlayers = new HashSet<>();

    public BukkitListeners(CustomItemsPlugin plugin) {
        this.plugin = plugin;
        this.attributeManager = plugin.getAttributeManager();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (player.isSneaking() || e.isCancelled()) {
            return;
        }

        if (attributeManager.hasAttribute(item, CustomAttribute.HARVEST_REPLANT) && MaterialUtils.isCrop((block.getType()))) {
            if (CustomAttribute.HARVEST_REPLANT.getHandler() instanceof HarvestReplantHandler handler) {
                handler.handleReplant(e, plugin);
            }
        }

        if (attributeManager.hasAttribute(item, CustomAttribute.MELT_MINING)) {
            if (CustomAttribute.MELT_MINING.getHandler() instanceof MeltMiningHandler handler) {
                handler.meltBlock(e);
            }
        }

        //Pour éviter une boucle infinie avec les blocs autours qui font à leur tour du 3x3.
        if (!clickedBlockPlayers.contains(player.getUniqueId())) return;

        clickedBlockPlayers.remove(player.getUniqueId());

        if (attributeManager.hasAttribute(item, CustomAttribute.RANGE_MINING)) {
            if (CustomAttribute.RANGE_MINING.getHandler() instanceof RangeMiningHandler handler) {
                handler.onBreak(e, faces.get(player.getUniqueId()));
            }
        }

        if (attributeManager.hasAttribute(item, CustomAttribute.RANGE_HARVEST) && MaterialUtils.isCrop(block.getType())) {
            if (CustomAttribute.RANGE_HARVEST.getHandler() instanceof RangeHarvestHandler handler) {
                handler.onCropBreak(e);
            }
        }

        if (attributeManager.hasAttribute(item, CustomAttribute.TREE_CUT) && MaterialUtils.isLog(block.getType())) {
            if (CustomAttribute.TREE_CUT.getHandler() instanceof TreeCutHandler handler) {
                handler.cutTree(player, block);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        BlockFace bf = e.getBlockFace();
        if (bf == BlockFace.SELF) return;

        faces.put(player.getUniqueId(), bf);

        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            clickedBlockPlayers.add(player.getUniqueId());
        }

        if (e.getAction() == Action.LEFT_CLICK_AIR) {
            ItemStack boots = player.getEquipment().getBoots();
            if (boots == null) return;
            if (attributeManager.hasAttribute(boots, CustomAttribute.POWER_BOOTS)) {
                if (CustomAttribute.POWER_BOOTS.getHandler() instanceof PowerBootsHandler handler) {
                    handler.onUserPowerBoots(player);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        clickedBlockPlayers.remove(player.getUniqueId());
        faces.remove(player.getUniqueId());
    }

    @EventHandler
    public void onPrepare(PrepareResultEvent e) {
        ItemStack result = e.getResult();

        if (result == null) {
            return;
        }

        if (e.getInventory() instanceof AnvilInventory anvilInventory) {
            ItemStack firstItem = anvilInventory.getFirstItem();
            ItemStack secondItem = anvilInventory.getSecondItem();
            if (secondItem != null && secondItem.getType() == Material.ENCHANTED_BOOK) {

                if (secondItem.getItemMeta() instanceof EnchantmentStorageMeta enchantmentStorageMeta) {
                    if (enchantmentStorageMeta.hasStoredEnchant(Enchantment.MENDING) &&
                            attributeManager.hasAttribute(firstItem, CustomAttribute.UNREPAIRABLE_BY_MENDING)) {
                        e.setResult(new ItemStack(Material.AIR));
                    }
                }

                return;
            }
        }

        if (attributeManager.hasAttribute(result, CustomAttribute.UNREPAIRABLE_BY_ANVIL)) {
            e.setResult(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onMend(PlayerItemMendEvent e) {
        if (attributeManager.hasAttribute(e.getItem(), CustomAttribute.UNREPAIRABLE_BY_MENDING)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent e) {
        ItemStack item = e.getItem();
        if (e.getDamage() == 0) return;

        if (item.getItemMeta() instanceof Damageable damageable) {

            if (attributeManager.getAttributes(item).isEmpty()) {
                return;
            }

            int durability = item.getType().getMaxDurability();
            int fivePercent = durability / 20;
            int currentDamage = damageable.getDamage() + e.getDamage();
            int percentUsed = (int) ((currentDamage / (double) durability) * 100) + 1;
            if (percentUsed >= 74 && percentUsed < 100 && currentDamage % fivePercent == 0) {
                String name;
                if (item.getItemMeta().hasDisplayName()) {
                    name = e.getItem().getItemMeta().getDisplayName();
                } else {
                    name = item.getType().toString().toLowerCase();
                }
                e.getPlayer().sendMessage("§c⚠ Il reste moins de " + (100 - percentUsed) + "% de durabilité à votre " + name + "§4.");
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (CustomAttribute.POWER_BOOTS.getHandler() instanceof PowerBootsHandler handler) {
            handler.onReachGround(event);
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if(event.getEntity() instanceof FallingBlock fallingBlock) {
            if(MaterialUtils.isLog(fallingBlock.getBlockData().getMaterial())) {
                event.getEntity().remove();
                event.setCancelled(true);
            }
        }
    }

}
