package fr.iban.customitems.utils;

import fr.iban.customitems.CustomItemsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class BlockUtils {

    public static List<Block> getSurroundingBlocks(BlockFace blockFace, Block targetBlock, ItemStack tool) {
        List<Block> surroundingBlocks = getSurroundingBlocks(blockFace, targetBlock);
        if (MaterialUtils.isShovel(tool.getType())) {
            return surroundingBlocks.stream()
                    .filter(block -> shovelBlocks.contains(block.getType())).collect(Collectors.toList());
        } else if (MaterialUtils.isPickaxe(tool.getType())) {
            return surroundingBlocks.stream()
                    .filter(block -> pickaxeBlocks.contains(block.getType())).collect(Collectors.toList());
        }
        return surroundingBlocks;
    }

    public static List<Block> getSurroundingBlocks(BlockFace blockFace, Block targetBlock) {
        ArrayList<Block> blocks = new ArrayList<>();
        World world = targetBlock.getWorld();

        int x, y, z;
        x = targetBlock.getX();
        y = targetBlock.getY();
        z = targetBlock.getZ();

        switch (blockFace) {
            case UP, DOWN -> {
                blocks.add(world.getBlockAt(x + 1, y, z));
                blocks.add(world.getBlockAt(x - 1, y, z));
                blocks.add(world.getBlockAt(x, y, z + 1));
                blocks.add(world.getBlockAt(x, y, z - 1));
                blocks.add(world.getBlockAt(x + 1, y, z + 1));
                blocks.add(world.getBlockAt(x - 1, y, z - 1));
                blocks.add(world.getBlockAt(x + 1, y, z - 1));
                blocks.add(world.getBlockAt(x - 1, y, z + 1));
            }
            case EAST, WEST -> {
                blocks.add(world.getBlockAt(x, y, z + 1));
                blocks.add(world.getBlockAt(x, y, z - 1));
                blocks.add(world.getBlockAt(x, y + 1, z));
                blocks.add(world.getBlockAt(x, y - 1, z));
                blocks.add(world.getBlockAt(x, y + 1, z + 1));
                blocks.add(world.getBlockAt(x, y - 1, z - 1));
                blocks.add(world.getBlockAt(x, y - 1, z + 1));
                blocks.add(world.getBlockAt(x, y + 1, z - 1));
            }
            case NORTH, SOUTH -> {
                blocks.add(world.getBlockAt(x + 1, y, z));
                blocks.add(world.getBlockAt(x - 1, y, z));
                blocks.add(world.getBlockAt(x, y + 1, z));
                blocks.add(world.getBlockAt(x, y - 1, z));
                blocks.add(world.getBlockAt(x + 1, y + 1, z));
                blocks.add(world.getBlockAt(x - 1, y - 1, z));
                blocks.add(world.getBlockAt(x + 1, y - 1, z));
                blocks.add(world.getBlockAt(x - 1, y + 1, z));
            }
            default -> {
            }
        }

        blocks.removeAll(Collections.singleton(null));
        return blocks;
    }


    public static final Set<Material> pickaxeBlocks = EnumSet.of(
            Material.COBBLESTONE, Material.STONE, Material.DIRT, Material.NETHERRACK, Material.GRASS_BLOCK, Material.DIORITE,
            Material.ANDESITE, Material.GRANITE, Material.IRON_ORE, Material.COAL_ORE, Material.GOLD_ORE, Material.LAPIS_ORE,
            Material.REDSTONE_ORE, Material.GRAVEL, Material.BLACKSTONE, Material.END_STONE,
            Material.BASALT, Material.MAGMA_BLOCK, Material.DEEPSLATE, Material.DEEPSLATE_COAL_ORE, Material.COBBLED_DEEPSLATE,
            Material.DEEPSLATE_COPPER_ORE, Material.DEEPSLATE_GOLD_ORE, Material.DEEPSLATE_IRON_ORE, Material.DEEPSLATE_LAPIS_ORE,
            Material.DEEPSLATE_REDSTONE_ORE, Material.COPPER_ORE, Material.TUFF, Material.CRIMSON_NYLIUM, Material.WARPED_NYLIUM,
            Material.NETHER_GOLD_ORE, Material.NETHER_QUARTZ_ORE, Material.POLISHED_BLACKSTONE_BRICKS,
            Material.CRACKED_POLISHED_BLACKSTONE_BRICKS, Material.GILDED_BLACKSTONE, Material.DRIPSTONE_BLOCK,
            Material.NETHER_BRICK, Material.TERRACOTTA, Material.BLUE_TERRACOTTA, Material.BLACK_TERRACOTTA,
            Material.BROWN_TERRACOTTA, Material.GRAY_TERRACOTTA, Material.YELLOW_TERRACOTTA, Material.ORANGE_TERRACOTTA,
            Material.RED_TERRACOTTA, Material.LIGHT_GRAY_TERRACOTTA, Material.WHITE_TERRACOTTA,
            Material.SANDSTONE, Material.RED_SANDSTONE, Material.SMOOTH_SANDSTONE, Material.CHISELED_RED_SANDSTONE,
            Material.SMOOTH_RED_SANDSTONE, Material.CUT_SANDSTONE, Material.CUT_RED_SANDSTONE, Material.CHISELED_SANDSTONE
    );

    public static final Set<Material> shovelBlocks = EnumSet.of(
            Material.GRASS_BLOCK, Material.DIRT, Material.SAND, Material.RED_SAND,
            Material.GRAVEL, Material.SOUL_SAND, Material.SOUL_SOIL, Material.CLAY,
            Material.DIRT_PATH, Material.ROOTED_DIRT, Material.COARSE_DIRT,
            Material.MYCELIUM, Material.PODZOL
    );
}
