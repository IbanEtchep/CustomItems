package fr.iban.customitems.utils;

import org.bukkit.Material;

import java.util.EnumSet;
import java.util.Set;

public class MaterialUtils {

    public static final Set<Material> shovels = EnumSet.of(
            Material.WOODEN_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL,
            Material.IRON_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL
    );

    public static final Set<Material> pickaxes = EnumSet.of(
            Material.WOODEN_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE,
            Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE
    );

    public static final Set<Material> crops = EnumSet.of(
            Material.WHEAT, Material.POTATOES, Material.CARROTS,
            Material.BEETROOTS, Material.NETHER_WART);

    public static boolean isShovel(Material material) {
        return shovels.contains(material);
    }

    public static boolean isPickaxe(Material material) {
        return pickaxes.contains(material);
    }

    public static boolean isCrop(Material material) {
        return crops.contains(material);
    }

    public static boolean isLog(Material material) {
        return switch (material) {
            case ACACIA_LOG, BIRCH_LOG, DARK_OAK_LOG, JUNGLE_LOG, OAK_LOG,
                    SPRUCE_LOG, WARPED_STEM, CRIMSON_STEM, STRIPPED_ACACIA_LOG,
                    STRIPPED_BIRCH_LOG, STRIPPED_CRIMSON_HYPHAE, STRIPPED_JUNGLE_LOG,
                    STRIPPED_OAK_LOG, STRIPPED_DARK_OAK_LOG, STRIPPED_SPRUCE_LOG, MANGROVE_LOG ->
                    true;
            default -> false;
        };
    }


}
