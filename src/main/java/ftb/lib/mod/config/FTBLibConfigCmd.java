package ftb.lib.mod.config;

import ftb.lib.api.config.*;
import latmod.lib.annotations.Info;

public class FTBLibConfigCmd
{
	@Info("A new layout for /list command")
	public static final ConfigEntryBool override_list = new ConfigEntryBool("override_list", true);
	
	@Info("Can fix some /help problems")
	public static final ConfigEntryBool override_help = new ConfigEntryBool("override_help", true);
	
	public static final ConfigEntryBool edit_config = new ConfigEntryBool("edit_config", true);
	public static final ConfigEntryBool set_item_name = new ConfigEntryBool("set_item_name", true);
	public static final ConfigEntryBool trash_can = new ConfigEntryBool("trash_can", true);
	public static final ConfigEntryBool heal = new ConfigEntryBool("heal", true);
	
	public static final ConfigEntryString reload_name = new ConfigEntryString("reload_name", "reload");
}