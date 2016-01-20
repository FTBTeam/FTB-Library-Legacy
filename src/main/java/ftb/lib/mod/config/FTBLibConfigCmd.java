package ftb.lib.mod.config;

import ftb.lib.api.cmd.CommandLevel;
import latmod.lib.config.*;

public class FTBLibConfigCmd
{
	public static final ConfigEntryBool override_list = new ConfigEntryBool("override_list", true).setInfo("A new layout for /list command");
	public static final ConfigEntryBool override_help = new ConfigEntryBool("override_help", true).setInfo("Can fix some /help problems");
	public static final ConfigEntryEnum<CommandLevel> level_set_item_name = new ConfigEntryEnum<>("set_item_name", CommandLevel.class, CommandLevel.VALUES, CommandLevel.OP, false);
	public static final ConfigEntryEnum<CommandLevel> level_trash_can = new ConfigEntryEnum<>("trash_can", CommandLevel.class, CommandLevel.VALUES, CommandLevel.ALL, false);
	
	public static final ConfigGroup name = new ConfigGroup("name");
	
	public static class Name
	{
		public static final ConfigEntryString reload = new ConfigEntryString("reload", "reload");
		public static final ConfigEntryString mode = new ConfigEntryString("mode", "ftb_mode");
		public static final ConfigEntryString edit_config = new ConfigEntryString("edit_config", "edit_config");
		public static final ConfigEntryString notify = new ConfigEntryString("notify", "ftb_notify");
		public static final ConfigEntryString set_item_name = new ConfigEntryString("set_item_name", "set_item_name");
		public static final ConfigEntryString trash_can = new ConfigEntryString("trash_can", "trash_can");
	}
}