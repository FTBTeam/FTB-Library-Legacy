package com.feed_the_beast.ftblib.lib.util;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.server.command.TextComponentHelper;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class SidedUtils
{
	private static final Map<String, String> SERVER_MODS_0 = new HashMap<>();
	public static final Map<String, String> SERVER_MODS = Collections.unmodifiableMap(SERVER_MODS_0);

	public static UUID UNIVERSE_UUID_CLIENT = null;

	public static ITextComponent lang(@Nullable ICommandSender sender, String mod, String key, Object... args)
	{
		return TextComponentHelper.createComponentTranslation(sender, key, args);
	}

	public static void checkModLists(Side side, Map<String, String> map)
	{
		if (side == Side.SERVER)
		{
			SERVER_MODS_0.clear();
			SERVER_MODS_0.putAll(map);
		}
		else if (side == Side.CLIENT)
		{
		}
	}

	/**
	 * Checks from client side if a mod exists on server side
	 */
	public static boolean isModLoadedOnServer(String modid)
	{
		return !modid.isEmpty() && SERVER_MODS_0.containsKey(modid);
	}

	/**
	 * Checks from client side if a set of mods exists on server side
	 */
	public static boolean areAllModsLoadedOnServer(Collection<String> modids)
	{
		if (!modids.isEmpty())
		{
			for (String modid : modids)
			{
				if (!isModLoadedOnServer(modid))
				{
					return false;
				}
			}
		}

		return true;
	}
}