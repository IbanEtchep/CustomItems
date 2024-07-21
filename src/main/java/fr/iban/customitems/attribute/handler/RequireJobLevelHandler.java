package fr.iban.customitems.attribute.handler;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.JobsPlayer;
import fr.iban.customitems.CustomItemsPlugin;
import fr.iban.customitems.attribute.CustomAttribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class RequireJobLevelHandler implements AttributeHandler {

    private CustomItemsPlugin plugin;

    public RequireJobLevelHandler(CustomItemsPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean hasRequiredLevelForItem(Player player, ItemStack item) {
        Map<String, String> values = plugin.getAttributeManager().getAttributeValues(item, CustomAttribute.REQUIRE_JOB_LEVEL);

        if (values.isEmpty()) return false;

        int level = Integer.parseInt(values.get("level"));
        String job = values.get("job");

        JobsPlayer jobsPlayer = Jobs.getPlayerManager().getJobsPlayer(player);

        if (jobsPlayer == null) return false;

        if (jobsPlayer.isInJob(Jobs.getJob(job))) {
            return jobsPlayer.getJobProgression(Jobs.getJob(job)).getLevel() >= level;
        }

        return false;
    }

}
