package fr.iban.customitems.attribute.handler;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Vibration;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PowerBootsHandler implements AttributeHandler {

    private final Set<UUID> jumpers = new HashSet<>();

    public void onUserPowerBoots(Player player) {
        if(!player.isOnGround() && !jumpers.contains(player.getUniqueId())) {
            @NotNull Vector direction = player.getEyeLocation().getDirection();
            player.setVelocity(direction.multiply(1.3));
            jumpers.add(player.getUniqueId());

            player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 10, 0.3, 0.3, 0.3, 0.1);
        }
    }

    public void onReachGround(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();
        int x = Math.abs(from.getBlockX() - to.getBlockX());
        int y = Math.abs(from.getBlockY() - to.getBlockY());
        int z = Math.abs(from.getBlockZ() - to.getBlockZ());

        if (x == 0 && y == 0 && z == 0) return;

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if(!jumpers.contains(uuid)) return;

        Location belowPlayer = player.getLocation().subtract(0,0.1,0);
        Block block = belowPlayer.getBlock();

        // Player definitely not grounded so no refresh in sight
        if(block.isEmpty() || block.isLiquid())
            return;

        jumpers.remove(uuid);
    }
}
