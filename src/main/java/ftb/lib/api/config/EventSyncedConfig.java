package ftb.lib.api.config;

import ftb.lib.api.EventLM;
import latmod.lib.config.ConfigGroup;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class EventSyncedConfig extends EventLM
{
	public final ConfigGroup group;
	
	public EventSyncedConfig(ConfigGroup g)
	{ group = g; }
}