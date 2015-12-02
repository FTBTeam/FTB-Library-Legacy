package ftb.lib.mod.config;

import latmod.lib.config.*;

public class FTBLibConfigCmd
{
	public static final ConfigGroup group = new ConfigGroup("commands");
	
	public static final ConfigEntryBool override_list = new ConfigEntryBool("override_list", true).setInfo("A new layout for /list command");
	public static final ConfigEntryBool override_help = new ConfigEntryBool("override_help", true).setInfo("Can fix some /help problems");
	
	public static final ConfigEntryString name_reload = new ConfigEntryString("name_reload", "reload");
	public static final ConfigEntryString name_mode = new ConfigEntryString("name_mode", "ftb_mode");
	public static final ConfigEntryString name_edit_config = new ConfigEntryString("name_config", "edit_config");
	public static final ConfigEntryString name_world_id = new ConfigEntryString("name_world_id", "ftb_world_id");
}