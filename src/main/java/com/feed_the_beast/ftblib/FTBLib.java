package com.feed_the_beast.ftblib;

import com.feed_the_beast.ftblib.commands.CmdFTB;
import com.feed_the_beast.ftblib.events.RegisterPermissionsEvent;
import com.feed_the_beast.ftblib.lib.OtherMods;
import com.feed_the_beast.ftblib.lib.cmd.CommandMirror;
import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

/**
 * @author LatvianModder
 */
@Mod(
		modid = FTBLib.MOD_ID,
		name = FTBLib.MOD_NAME,
		version = FTBLib.VERSION,
		acceptableRemoteVersions = "*",
		acceptedMinecraftVersions = "[1.12,)",
		dependencies = "required-after:forge@[14.23.0.2517,);after:" + OtherMods.BAUBLES + ";after:" + OtherMods.JEI + ";after:" + OtherMods.NEI + ";after:" + OtherMods.MC_MULTIPART + ";after:" + OtherMods.CHISELS_AND_BITS + ";after:" + OtherMods.ICHUN_UTIL
)
public class FTBLib
{
	public static final String MOD_ID = "ftblib";
	public static final String MOD_NAME = "FTBLib";
	public static final String VERSION = "@VERSION@";
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
	public static final String KEY_CATEGORY = "key.categories.ftbmods";

	@SidedProxy(serverSide = "com.feed_the_beast.ftblib.FTBLibCommon", clientSide = "com.feed_the_beast.ftblib.client.FTBLibClient")
	public static FTBLibCommon PROXY;

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
		new RegisterPermissionsEvent().post();
	}

	@Mod.EventHandler
	public void onPostInit(FMLPostInitializationEvent event)
	{
		PROXY.postInit();
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
					event.registerServerCommand(new CommandMirror(command));
				}
			}
		}
	}

	@Mod.EventHandler
	public void onServerStopped(FMLServerStoppedEvent event)
	{
	}
}