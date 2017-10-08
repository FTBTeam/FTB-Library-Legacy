package com.feed_the_beast.ftbl.lib.util;

import com.feed_the_beast.ftbl.lib.icon.Color4I;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.SidedProxy;

/**
 * @author LatvianModder
 */
public class UtilsCommon
{
	@SidedProxy(serverSide = "com.feed_the_beast.ftbl.lib.util.UtilsCommon", clientSide = "com.feed_the_beast.ftbl.lib.util.UtilsClient")
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
		return ServerUtils.getOverworld().getTotalWorldTime();
	}
}