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

    public void cutTree(Player player, Block block) {
//        BlockFace[] faces = {BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
//        Queue<Block> blocks = new LinkedList<>();
//        blocks.add(block);
//
//        while (!blocks.isEmpty()) {
//            Block currentBlock = blocks.remove();
//            player.breakBlock(currentBlock);
//
//            for (BlockFace face : faces) {
//                Block relative = currentBlock.getRelative(face);
//                if (MaterialUtils.isLog(relative.getType())) {
//                    blocks.add(relative);
//                }
//            }
//        }
        List<Block> treeBlocks = getTreeBlocks(block);
        List<FallingBlock> fallingBlocks = new ArrayList<>();

        // Casser chaque bloc de l'arbre et ajouter un falling block
        for (Block treeBlock : treeBlocks) {
            FallingBlock fallingBlock = treeBlock.getWorld().spawnFallingBlock(treeBlock.getLocation().add(0.5, 0, 0.5), treeBlock.getBlockData());
            fallingBlock.setHurtEntities(false);
            fallingBlock.setDropItem(false);
            fallingBlock.setGravity(true);
            fallingBlock.shouldAutoExpire(true);
            //fallingBlock.setVelocity(new Vector(0, -0.3, 0));
            player.breakBlock(treeBlock);

        }

    }

    private void breakTree(Player player, Block block) {
        player.breakBlock(block);

        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) {
                        continue;
                    }

                    Block neighbor = block.getWorld().getBlockAt(x + dx, y + dy, z + dz);

                    if (MaterialUtils.isLog(neighbor.getType())) {
                        breakTree(player, neighbor);
                    }
                }
            }
        }
    }
    private List<Block> getTreeBlocks(Block block) {
        List<Block> treeBlocks = new ArrayList<>();
        Queue<Block> queue = new LinkedList<>();
        queue.add(block);

        while (!queue.isEmpty()) {
            Block currentBlock = queue.remove();
            treeBlocks.add(currentBlock);

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
