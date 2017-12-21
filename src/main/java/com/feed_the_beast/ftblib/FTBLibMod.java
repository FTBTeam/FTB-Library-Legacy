package com.feed_the_beast.ftblib;

import com.feed_the_beast.ftblib.cmd.CmdFTB;
import com.feed_the_beast.ftblib.events.PermissionRegistryEvent;
import com.feed_the_beast.ftblib.lib.util.ServerUtils;
import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

import java.util.Locale;

/**
 * @author LatvianModder
 */
@Mod(modid = FTBLibFinals.MOD_ID, name = FTBLibFinals.MOD_NAME, version = FTBLibFinals.VERSION, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.12,)", dependencies = FTBLibFinals.DEPENDENCIES)
public class FTBLibMod
{
	@Mod.Instance(FTBLibFinals.MOD_ID)
	public static FTBLibMod INST;

	@SidedProxy(serverSide = "com.feed_the_beast.ftblib.FTBLibModCommon", clientSide = "com.feed_the_beast.ftblib.client.FTBLibModClient")
	public static FTBLibModCommon PROXY;

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{
		Locale.setDefault(Locale.US);
		FTBLibConfig.sync();
		PROXY.preInit(event);
	}

	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event)
	{
		new PermissionRegistryEvent().post();
	}

	@Mod.EventHandler
	public void onPostInit(FMLPostInitializationEvent event)
	{
		PROXY.postInit();
	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		ServerUtils.setServer(event.getServer());
		CmdFTB cmd = new CmdFTB(ServerUtils.getServer().isDedicatedServer());
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

	@Mod.EventHandler
	public void onServerStopped(FMLServerStoppedEvent event)
	{
		ServerUtils.setServer(null);
	}
}