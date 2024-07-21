package fr.iban.customitems.attribute.handler;

import fr.iban.customitems.CustomItemsPlugin;
import fr.iban.lands.LandsPlugin;
import fr.iban.lands.enums.Action;
import fr.iban.lands.model.land.Land;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EntityCatcherHandler implements AttributeHandler {

    private final NamespacedKey catchedEntityKey;
    private final CustomItemsPlugin plugin;
    private boolean landsEnabled = false;
    private Map<UUID, Long> placeCooldowns;

    public EntityCatcherHandler(CustomItemsPlugin plugin) {
        this.plugin = plugin;
        this.catchedEntityKey = new NamespacedKey(plugin, "catched_entity");
        this.placeCooldowns = new HashMap<>();
        if (plugin.getServer().getPluginManager().isPluginEnabled("MSLands")) {
            landsEnabled = true;
        }
    }

    public void catchEntity(Player player, ItemStack item, Entity entity, PlayerInteractEntityEvent event) {
        if (landsEnabled) {
            Land land = LandsPlugin.getInstance().getLandRepository().getLandAt(entity.getLocation());
            if (!land.isBypassing(player, Action.BLOCK_BREAK)) {
                return;
            }
        }

        if (item.getAmount() > 1) {
            player.sendMessage("§cVeuillez utiliser un seul item !");
            event.setCancelled(true);
            return;
        }

        if (item.hasItemMeta()) {
            byte[] serializedEntity = item.getItemMeta().getPersistentDataContainer().get(catchedEntityKey, PersistentDataType.BYTE_ARRAY);
            if (serializedEntity != null) {
                player.sendMessage("§cL'item a déjà été utilsé !");
                event.setCancelled(true);
                return;
            }
        }


        byte[] serializedEntity = plugin.getServer().getUnsafe().serializeEntity(entity);
        item.editMeta(meta -> meta.getPersistentDataContainer().set(catchedEntityKey, PersistentDataType.BYTE_ARRAY, serializedEntity));
        entity.remove();
        player.sendMessage("§aVous avez capturé un mob !");
        this.placeCooldowns.put(player.getUniqueId(), System.currentTimeMillis() + 1000);
        item.editMeta(meta -> meta.setLore(List.of("§7Mob capturé: §e" + entity.getType().name())));
    }

    /**
     * Respawn the animal from the itemstack
     */
    public void respawnEntity(Player player, ItemStack item, PlayerInteractEvent event) {
        BlockFace blockFace = event.getBlockFace();
        Location clickedLocation = event.getInteractionPoint();
        Block block = event.getClickedBlock();

        if (block == null || clickedLocation == null) return;

        if (placeCooldowns.containsKey(player.getUniqueId()) && placeCooldowns.get(player.getUniqueId()) > System.currentTimeMillis()) {
            player.sendMessage("§cVous devez attendre 1 seconde avant de relâcher un mob !");
            return;
        }

        if (landsEnabled) {
            Land land = LandsPlugin.getInstance().getLandRepository().getLandAt(block.getLocation());
            if (!land.isBypassing(player, fr.iban.lands.enums.Action.BLOCK_PLACE)) {
                return;
            }
        }

        if (item.hasItemMeta()) {
            byte[] serializedEntity = item.getItemMeta().getPersistentDataContainer().get(catchedEntityKey, PersistentDataType.BYTE_ARRAY);
            if (serializedEntity != null) {
                if (item.getAmount() > 1) {
                    player.sendMessage("§cVous ne pouvez pas relâcher le mob avec plusieurs items dans la main !");
                    return;
                }

                if (blockFace != BlockFace.UP) {
                    player.sendMessage("§cVous ne pouvez pas relâcher le mob ici !");
                    return;
                }

                Entity entity = plugin.getServer().getUnsafe().deserializeEntity(serializedEntity, player.getWorld());
                entity.spawnAt(clickedLocation.add(0, 1, 0), CreatureSpawnEvent.SpawnReason.CUSTOM);

                entity.setInvulnerable(true);
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> entity.setInvulnerable(false), 200);
                player.sendMessage("§aVous avez relâché le mob ! Il est invulnérable pendant 10 secondes.");
                player.getInventory().removeItem(item);
            }
        }
    }
}
