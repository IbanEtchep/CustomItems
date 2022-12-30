package fr.iban.customitems.listener;

import fr.iban.customitems.CustomAttributeManager;
import fr.iban.customitems.CustomItemsPlugin;
import fr.iban.customitems.attribute.CustomAttribute;
import fr.iban.survivalcore.event.ItemRepairEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SurvivalCoreListeners implements Listener {

    private final CustomItemsPlugin plugin;
    private final CustomAttributeManager attributeManager;

    public SurvivalCoreListeners(CustomItemsPlugin plugin) {
        this.plugin = plugin;
        this.attributeManager = plugin.getAttributeManager();
    }

    @EventHandler
    public void onRepair(ItemRepairEvent e) {
        if (attributeManager.hasAttribute(e.getItem(), CustomAttribute.UNREPAIRABLE_BY_COMMAND)) {
            e.setCancelled(true);
        }
    }

}
