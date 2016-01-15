package ftb.lib.api.config;

import ftb.lib.FTBLib;
import ftb.lib.mod.client.FTBLibModClient;
import ftb.lib.notification.*;
import latmod.lib.LMFileUtils;
import latmod.lib.config.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.*;

import java.io.File;

public final class ClientConfigRegistry
{
	private static final ConfigGroup group = new ConfigGroup("client_config");
	
	public static final IConfigProvider provider = new IConfigProvider()
	{
		public String getGroupTitle(ConfigGroup g)
		{ return I18n.format(g.getFullID()); }
		
		public String getEntryTitle(ConfigEntry e)
		{ return I18n.format(e.getFullID()); }
		
		public ConfigGroup getGroup()
		{ return group; }
		
		public void save()
		{
			if(group.parentFile == null) init();
			group.parentFile.save();
			
			Notification n = new Notification("reload_client_config", new ChatComponentTranslation("ftbl:reload_client_config"), 3000);
			n.title.getChatStyle().setColor(EnumChatFormatting.WHITE);
			n.desc = new ChatComponentText("/" + FTBLibModClient.reload_client_cmd.get());
			n.setColor(0xFF33FF33);
			ClientNotifications.add(n);
		}
	};
	
	public static void init()
	{
		File file = LMFileUtils.newFile(new File(FTBLib.folderLocal, "client/config.json"));
		ConfigFile configFile = new ConfigFile(group, file);
		group.parentFile = configFile;
		configFile.load();
	}
	
	/**
	 * Do this before postInit()
	 */
	public static void add(ConfigGroup g)
	{ group.add(g, false); }
}