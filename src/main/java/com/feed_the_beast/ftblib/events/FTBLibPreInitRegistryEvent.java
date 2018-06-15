package com.feed_the_beast.ftblib.events;

import com.feed_the_beast.ftblib.lib.config.ConfigValueProvider;
import com.feed_the_beast.ftblib.lib.data.AdminPanelAction;
import com.feed_the_beast.ftblib.lib.data.ISyncData;
import com.feed_the_beast.ftblib.lib.data.TeamAction;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class FTBLibPreInitRegistryEvent extends FTBLibEvent
{
	public interface Registry
	{
		void registerConfigValueProvider(String id, ConfigValueProvider provider);

		void registerSyncData(String mod, ISyncData data);

		void registerServerReloadHandler(ResourceLocation id, IReloadHandler handler);

		void registerAdminPanelAction(AdminPanelAction action);

		void registerTeamAction(TeamAction action);
	}

	private final Registry registry;

	public FTBLibPreInitRegistryEvent(Registry r)
	{
		registry = r;
	}

	public Registry getRegistry()
	{
		return registry;
	}
}