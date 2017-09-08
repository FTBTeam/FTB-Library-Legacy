package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.cmd.CmdFTB;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibPerms;
import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.Locale;

/**
 * @author LatvianModder
 */
@Mod(modid = FTBLibFinals.MOD_ID, name = FTBLibFinals.MOD_NAME, version = "0.0.0", useMetadata = true, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.12,)", dependencies = FTBLibFinals.DEPENDENCIES)
public class FTBLibMod
{
	@Mod.Instance(FTBLibFinals.MOD_ID)
	public static FTBLibMod INST;

	@SidedProxy(serverSide = "com.feed_the_beast.ftbl.FTBLibModCommon", clientSide = "com.feed_the_beast.ftbl.client.FTBLibModClient")
	public static FTBLibModCommon PROXY;

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{
		Locale.setDefault(Locale.US);
		FTBLibConfig.sync();
		PROXY.preInit(event);
		PROXY.reloadConfig(event.getModState());
	}

	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event)
	{
		FTBLibPerms.init();
		PROXY.reloadConfig(event.getModState());
	}

	@Mod.EventHandler
	public void onPostInit(FMLPostInitializationEvent event)
	{
		PROXY.postInit(event.getModState());
	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		CmdFTB cmd = new CmdFTB(event.getServer().isDedicatedServer());
		event.registerServerCommand(cmd);

		if (FTBLibConfig.general.mirror_ftb_commands)
		{
			for (ICommand command : cmd.getSubCommands())
			{
				if (!command.getName().equals("reload"))
				{
					event.registerServerCommand(command);
				}
			}
		}
	}
}