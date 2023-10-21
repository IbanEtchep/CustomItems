package fr.iban.customitems.attribute.handler;

import fr.iban.customitems.CustomItemsPlugin;
import fr.iban.customitems.utils.MaterialUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TreeCutHandler implements AttributeHandler {

    private int blockThreshold;

    public TreeCutHandler(CustomItemsPlugin plugin) {
        this.blockThreshold = plugin.getConfig().getInt("tree-cut.block-threshold", 2048);
    }

    public void cutTree(Player player, Block block) {
        List<Block> treeBlocks = getTreeBlocks(block);

        // Casser chaque bloc de l'arbre et ajouter un falling block
        for (Block treeBlock : treeBlocks) {
            FallingBlock fallingBlock = treeBlock.getWorld().spawnFallingBlock(treeBlock.getLocation().add(0.5, 0, 0.5), treeBlock.getBlockData());
            fallingBlock.setHurtEntities(false);
            fallingBlock.setDropItem(false);
            fallingBlock.setGravity(true);
            fallingBlock.shouldAutoExpire(true);
            player.breakBlock(treeBlock);
        }
    }

    private List<Block> getTreeBlocks(Block block) {
        List<Block> treeBlocks = new ArrayList<>();
        Queue<Block> queue = new LinkedList<>();
        queue.add(block);

        while (!queue.isEmpty()) {
            Block currentBlock = queue.remove();
            treeBlocks.add(currentBlock);

            if(treeBlocks.size() >= blockThreshold) {
                return treeBlocks;
            }

            int x = currentBlock.getX();
            int y = currentBlock.getY();
            int z = currentBlock.getZ();

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = 0; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) {
                            continue;
                        }

                        Block neighbor = currentBlock.getWorld().getBlockAt(x + dx, y + dy, z + dz);

                        if (MaterialUtils.isLog(neighbor.getType()) && !treeBlocks.contains(neighbor) && !queue.contains(neighbor)) {
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }

        return treeBlocks;
    }

}
