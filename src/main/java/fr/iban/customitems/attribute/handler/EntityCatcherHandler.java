package fr.iban.customitems.attribute.handler;

import fr.iban.customitems.CustomItemsPlugin;
import fr.iban.lands.LandsPlugin;
import fr.iban.lands.enums.Action;
import fr.iban.lands.land.Land;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class EntityCatcherHandler implements AttributeHandler {

    private final NamespacedKey catchedEntityKey;
    private final CustomItemsPlugin plugin;
    private boolean landsEnabled = false;

    public EntityCatcherHandler(CustomItemsPlugin plugin) {
        this.plugin = plugin;
        this.catchedEntityKey = new NamespacedKey(plugin, "catched_entity");
        if (plugin.getServer().getPluginManager().isPluginEnabled("Lands")) {
            try {
                Class.forName("fr.iban.lands.LandsPlugin");
                landsEnabled = true;
            } catch (ClassNotFoundException ignored) {
            }
        }
    }

    public void catchEntity(Player player, ItemStack item, Entity entity) {
        if (landsEnabled) {
            Land land = LandsPlugin.getInstance().getLandManager().getLandAt(entity.getLocation());
            if (!land.isBypassing(player, Action.BLOCK_BREAK)) {
                return;
            }
        }

        if (item.getAmount() > 1) {
            player.sendMessage("§cVeuillez utiliser un seul item !");
            return;
        }

        if (item.hasItemMeta()) {
            byte[] serializedEntity = item.getItemMeta().getPersistentDataContainer().get(catchedEntityKey, PersistentDataType.BYTE_ARRAY);
            if (serializedEntity != null) {
                player.sendMessage("§cL'item a déjà été utilsé !");
                return;
            }
        }


        byte[] serializedEntity = plugin.getServer().getUnsafe().serializeEntity(entity);
        item.editMeta(meta -> meta.getPersistentDataContainer().set(catchedEntityKey, PersistentDataType.BYTE_ARRAY, serializedEntity));
        entity.remove();
        player.sendMessage("§aVous avez capturé un mob !");
        item.editMeta(meta -> meta.setLore(List.of("§7Mob capturé: §e" + entity.getType().name())));
    }

    /**
     * Respawn the animal from the itemstack
     */
    public void respawnEntity(Player player, ItemStack item, Block block) {
        if (landsEnabled) {
            Land land = LandsPlugin.getInstance().getLandManager().getLandAt(block.getLocation());
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

                Entity entity = plugin.getServer().getUnsafe().deserializeEntity(serializedEntity, player.getWorld());
                entity.spawnAt(block.getLocation().add(0, 1, 0), CreatureSpawnEvent.SpawnReason.CUSTOM);
                player.sendMessage("§aVous avez relâché le mob !");
                player.getInventory().removeItem(item);
            }
        }
    }
}
