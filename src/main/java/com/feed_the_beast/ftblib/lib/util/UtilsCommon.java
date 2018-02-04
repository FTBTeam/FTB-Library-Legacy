package com.feed_the_beast.ftblib.lib.util;

import com.feed_the_beast.ftblib.FTBLibFinals;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.server.FMLServerHandler;

/**
 * @author LatvianModder
 */
public class UtilsCommon
{
	@SidedProxy(modId = FTBLibFinals.MOD_ID, serverSide = "com.feed_the_beast.ftblib.lib.util.UtilsCommon", clientSide = "com.feed_the_beast.ftblib.lib.util.UtilsClient")
	public static UtilsCommon INSTANCE;

	String translate(String key)
	{
		return I18n.translateToLocal(key);
	}

	String translate(String key, Object... objects)
	{
		return I18n.translateToLocalFormatted(key, objects);
	}

	boolean canTranslate(String key)
	{
		return I18n.canTranslate(key);
	}

	public void spawnDust(World world, double x, double y, double z, float r, float g, float b, float a)
	{
	}

	public void spawnDust(World world, double x, double y, double z, Color4I col)
	{
		spawnDust(world, x, y, z, col.redf(), col.greenf(), col.bluef(), col.alphaf());
	}

	long getWorldTime()
	{
		return FMLServerHandler.instance().getServer().getEntityWorld().getTotalWorldTime();
	}
}