package fr.iban.customitems.attribute.handler;

import fr.iban.customitems.utils.BlockUtils;
import fr.iban.customitems.utils.MaterialUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class RangeHarvestHandler implements AttributeHandler {


    public void onCropBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();

        for (Block b : BlockUtils.getSurroundingBlocks(BlockFace.UP, block)) {
            if (MaterialUtils.isCrop(b.getType())) {
                BlockData bd = b.getBlockData();
                Ageable age = (Ageable) bd;

                if (age.getAge() == age.getMaximumAge()) {
                    player.breakBlock(b);
                }
            }
        }
    }

    public void onTilling(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        for (Block surroundingBloc : BlockUtils.getSurroundingBlocks(BlockFace.UP, block)) {
            Material material = surroundingBloc.getType();
            if (material == Material.DIRT || material == Material.GRASS_BLOCK) {

                surroundingBloc.setType(Material.FARMLAND);
                player.damageItemStack(event.getItemInHand(), 1);
            }
        }
    }
}
