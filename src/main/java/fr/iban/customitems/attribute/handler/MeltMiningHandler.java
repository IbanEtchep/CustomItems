package fr.iban.customitems.attribute.handler;

import fr.iban.customitems.CustomItemsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;

import java.util.Iterator;
import java.util.Optional;

public class MeltMiningHandler implements AttributeHandler {

    private final boolean checksRecipes;

    public MeltMiningHandler(CustomItemsPlugin plugin) {
        checksRecipes = plugin.getConfig().getBoolean("melt-mining.recipe-checker", false);
    }

    public void meltBlock(BlockBreakEvent e) {
        Block block = e.getBlock();

        switch (block.getType()) {
            case GOLD_ORE, DEEPSLATE_GOLD_ORE -> meltAndDrop(e, Material.GOLD_INGOT, 1, true);
            case IRON_ORE, DEEPSLATE_IRON_ORE -> meltAndDrop(e, Material.IRON_INGOT, 0.7, true);
            case ANCIENT_DEBRIS -> meltAndDrop(e, Material.NETHERITE_SCRAP, 2, false);
            case NETHER_GOLD_ORE -> meltAndDrop(e, Material.GOLD_INGOT, 1, false);
            case COPPER_ORE, DEEPSLATE_COPPER_ORE -> meltAndDrop(e, Material.COPPER_INGOT, 0.7, true);
            default -> {
                if(checksRecipes && !block.getType().toString().endsWith("_ORE")) {
                    Optional<FurnaceRecipe> furnaceRecipe = getFurnaceRecipeFor(block.getType());

                    if (furnaceRecipe.isPresent()) {
                        Material smeltedResult = furnaceRecipe.get().getResult().getType();
                        meltAndDrop(e, smeltedResult, 0, false);
                    }
                }
            }
        }
    }

    private void meltAndDrop(BlockBreakEvent e, Material newDrop, double xp, boolean fortuneMultiply) {
        Player player = e.getPlayer();
        ItemStack toDrop = new ItemStack(newDrop);
        Location loc = e.getBlock().getLocation();
        int expToDrop = 0;

        //Centre du bloc :
        loc.add(0.5, 0.5, 0.5);

        if (fortuneMultiply) {
            for (ItemStack drop : e.getBlock().getDrops(e.getPlayer().getInventory().getItemInMainHand())) {
                toDrop.setAmount(drop.getAmount());
            }
        }

        if (xp >= 1) {
            expToDrop += xp;
        } else {
            if (Math.random() <= xp) {
                expToDrop++;
            }
        }

        e.setDropItems(false);
        e.setExpToDrop(expToDrop);
        player.getWorld().dropItem(loc, toDrop);
        player.getWorld().spawnParticle(Particle.FLAME, loc, 10, 0.3, 0.3, 0.3, 0.02);
    }

    private Optional<FurnaceRecipe> getFurnaceRecipeFor(Material blockType) {
        Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
        while (recipeIterator.hasNext()) {
            Recipe recipe = recipeIterator.next();
            if (recipe instanceof FurnaceRecipe furnaceRecipe) {
                RecipeChoice choice = furnaceRecipe.getInputChoice();
                if (choice instanceof RecipeChoice.MaterialChoice materialChoice) {
                    if (materialChoice.getChoices().contains(blockType)) {
                        return Optional.of(furnaceRecipe);
                    }
                }
            }
        }
        return Optional.empty();
    }


}
