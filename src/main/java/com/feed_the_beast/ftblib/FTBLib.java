package com.feed_the_beast.ftblib;

import com.feed_the_beast.ftblib.command.CmdAddFakePlayer;
import com.feed_the_beast.ftblib.command.CmdMySettings;
import com.feed_the_beast.ftblib.command.CmdReload;
import com.feed_the_beast.ftblib.command.team.CmdTeam;
import com.feed_the_beast.ftblib.events.RegisterPermissionsEvent;
import com.feed_the_beast.ftblib.lib.OtherMods;
import com.feed_the_beast.ftblib.lib.command.CommandUtils;
import com.feed_the_beast.ftblib.lib.util.SidedUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;

/**
 * @author LatvianModder
 */
@Mod(
		modid = FTBLib.MOD_ID,
		name = FTBLib.MOD_NAME,
		version = FTBLib.VERSION,
		acceptedMinecraftVersions = "[1.12,)",
		dependencies = "required-after:forge@[14.23.3.2697,);after:" + OtherMods.BAUBLES + ";after:" + OtherMods.JEI + ";after:" + OtherMods.NEI + ";after:" + OtherMods.MC_MULTIPART + ";after:" + OtherMods.CHISELS_AND_BITS + ";after:" + OtherMods.ICHUN_UTIL
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

	public static ITextComponent lang(@Nullable ICommandSender sender, String key, Object... args)
	{
		return SidedUtils.lang(sender, MOD_ID, key, args);
	}

	public static CommandException error(@Nullable ICommandSender sender, String key, Object... args)
	{
		return CommandUtils.error(lang(sender, key, args));
	}

	public static CommandException errorFeatureDisabledServer(@Nullable ICommandSender sender)
	{
		return error(sender, "feature_disabled_server");
	}

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
		event.registerServerCommand(new CmdReload());
		event.registerServerCommand(new CmdMySettings());
		event.registerServerCommand(new CmdTeam());

		if (FTBLibConfig.debugging.special_commands)
		{
			event.registerServerCommand(new CmdAddFakePlayer());
		}
	}

	@NetworkCheckHandler
	public boolean checkModLists(Map<String, String> map, Side side)
	{
		SidedUtils.checkModLists(side, map);
		return true;
	}
}