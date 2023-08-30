package fr.iban.customitems.attribute.handler;

import fr.iban.customitems.CustomItemsPlugin;
import fr.iban.customitems.utils.BlockUtils;
import fr.iban.customitems.utils.MaterialUtils;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class RangeMiningHandler implements AttributeHandler {

    private final CustomItemsPlugin plugin;
    private final Set<Material> pickaxeBlacklist;
    private final Set<Material> shovelBlacklist;
    private final boolean onlyPickaxeMineableBlocks;
    private final boolean onlyShovelMineableBlocks;
    private final boolean isPickaxeBlacklistInversed;
    private final boolean isShovelBlacklistInversed;

    public RangeMiningHandler(CustomItemsPlugin plugin) {
        this.plugin = plugin;
        onlyPickaxeMineableBlocks = plugin.getConfig().getBoolean("range-mining.pickaxe.only-pickaxe-mineable-blocks");
        isPickaxeBlacklistInversed = plugin.getConfig().getBoolean("range-mining.pickaxe.blacklist-inversed");
        pickaxeBlacklist = plugin.getConfig().getStringList("range-mining.pickaxe.blacklist").stream()
                .map(this::toMaterial)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        onlyShovelMineableBlocks = plugin.getConfig().getBoolean("range-mining.shovel.only-pickaxe-mineable-blocks");
        isShovelBlacklistInversed = plugin.getConfig().getBoolean("range-mining.shovel.blacklist-inversed");
        shovelBlacklist = plugin.getConfig().getStringList("range-mining.shovel.blacklist").stream()
                .map(this::toMaterial)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    public void onBreak(BlockBreakEvent event, BlockFace blockface) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack tool = player.getInventory().getItemInMainHand();

        List<Block> surroundingBlocks = BlockUtils.getSurroundingBlocks(blockface, block);

        if (MaterialUtils.isShovel(tool.getType())) {
            surroundingBlocks = surroundingBlocks.stream()
                    .filter(b ->
                            (!onlyShovelMineableBlocks || Tag.MINEABLE_SHOVEL.isTagged(b.getType()))
                            && isMineable(b.getType(), shovelBlacklist, isShovelBlacklistInversed))
                    .collect(Collectors.toList());
        } else if (MaterialUtils.isPickaxe(tool.getType())) {
            surroundingBlocks = surroundingBlocks.stream()
                    .filter(b ->
                            (!onlyPickaxeMineableBlocks || Tag.MINEABLE_PICKAXE.isTagged(b.getType()))
                            && isMineable(b.getType(), pickaxeBlacklist, isPickaxeBlacklistInversed))
                    .collect(Collectors.toList());
        }

        for (Block b : surroundingBlocks) {
            player.breakBlock(b);
        }
    }

    private Optional<Material> toMaterial(String name) {
        try {
            return Optional.of(Material.valueOf(name));
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning(name + " is not a Material.");
            return Optional.empty();
        }
    }

    private boolean isMineable(Material material, Set<Material> list, boolean isInversed) {
        return isInversed == list.contains(material);
    }
}