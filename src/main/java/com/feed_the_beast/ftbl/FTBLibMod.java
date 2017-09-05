package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.EnumReloadType;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.api_impl.TickHandler;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.cmd.CmdFTB;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibPerms;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.ServerUtils;
import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
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

	@Mod.EventHandler
	public void onServerAboutToStart(FMLServerAboutToStartEvent event)
	{
		FTBLibFinals.LOGGER.info("FTBLib Loaded");
		SharedServerData.INSTANCE.reset();
		TickHandler.INSTANCE = new TickHandler();
		CommonUtils.folderWorld = new File(FMLCommonHandler.instance().getSavesDirectory(), event.getServer().getFolderName());
		Universe.INSTANCE = new Universe();
		Universe.INSTANCE.load();
		PROXY.worldLoaded();
		ServerUtils.addTickable(event.getServer(), TickHandler.INSTANCE);
		FTBLibAPI.API.loadWorldData(event.getServer());
	}

	@Mod.EventHandler
	public void onServerStarted(FMLServerStartedEvent event)
	{
		FTBLibAPI.API.reload(Side.SERVER, ServerUtils.getServer(), EnumReloadType.CREATED, ReloadEvent.ALL);
	}

	@Mod.EventHandler
	public void onServerShutDown(FMLServerStoppedEvent event)
	{
		Universe.INSTANCE.onClosed();
		Universe.INSTANCE = null;
	}
}