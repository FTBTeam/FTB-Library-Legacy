package ftb.lib.api.config;

import cpw.mods.fml.common.eventhandler.Cancelable;
import ftb.lib.api.EventLM;
import latmod.lib.config.ConfigGroup;

@Cancelable
public class EventSyncedConfig extends EventLM
{
	public final ConfigGroup group;
	
	public EventSyncedConfig(ConfigGroup g)
	{ group = g; }
}