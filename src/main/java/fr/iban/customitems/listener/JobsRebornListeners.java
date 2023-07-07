package fr.iban.customitems.listener;

import com.gamingmesh.jobs.api.JobsExpGainEvent;
import com.gamingmesh.jobs.api.JobsPaymentEvent;
import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import fr.iban.customitems.CustomAttributeManager;
import fr.iban.customitems.CustomItemsPlugin;
import fr.iban.customitems.attribute.CustomAttribute;
import fr.iban.customitems.utils.BlockUtils;
import fr.iban.customitems.utils.MaterialUtils;
import fr.iban.survivalcore.event.ItemRepairEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class JobsRebornListeners implements Listener {

    private final CustomItemsPlugin plugin;
    private final CustomAttributeManager attributeManager;

    private int rangeMiningMultiplier = 1;
    private int rangeHarvestMultiplier = 1;
    private int treeCutMultiplier = 1;

    public JobsRebornListeners(CustomItemsPlugin plugin) {
        this.plugin = plugin;
        this.attributeManager = plugin.getAttributeManager();
        this.rangeMiningMultiplier = plugin.getConfig().getInt("range-mining-jobs-multiplier", 1);
        this.rangeHarvestMultiplier = plugin.getConfig().getInt("range-harvest-jobs-multiplier", 1);
        this.treeCutMultiplier = plugin.getConfig().getInt("tree-cut-jobs-multiplier", 1);
    }

    @EventHandler
    public void onPayment(JobsPrePaymentEvent e) {
        Player player = e.getPlayer().getPlayer();

        if(player == null) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        Block block = e.getBlock();

        if(block != null) {
            if(MaterialUtils.isLog(block.getType()) && attributeManager.hasAttribute(item, CustomAttribute.TREE_CUT)) {
                e.setAmount(e.getAmount()*treeCutMultiplier);
            }

            if(attributeManager.hasAttribute(item, CustomAttribute.RANGE_MINING)) {
                e.setAmount(e.getAmount()*rangeMiningMultiplier);
            }

            if(MaterialUtils.isCrop(block.getType())
                    && attributeManager.hasAttribute(item, CustomAttribute.RANGE_HARVEST)) {
                e.setAmount(e.getAmount()*rangeHarvestMultiplier);
            }
        }

    }

    @EventHandler
    public void onExpGain(JobsExpGainEvent e) {
        Player player = e.getPlayer().getPlayer();

        if(player == null) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        Block block = e.getBlock();

        if(block != null) {
            if(MaterialUtils.isLog(block.getType()) && attributeManager.hasAttribute(item, CustomAttribute.TREE_CUT)) {
                e.setExp(e.getExp()*treeCutMultiplier);
            }

            if(attributeManager.hasAttribute(item, CustomAttribute.RANGE_MINING)) {
                e.setExp(e.getExp()*rangeMiningMultiplier);
            }

            if(MaterialUtils.isCrop(block.getType())
                    && attributeManager.hasAttribute(item, CustomAttribute.RANGE_HARVEST)) {
                e.setExp(e.getExp()*rangeHarvestMultiplier);
            }
        }
    }

}
