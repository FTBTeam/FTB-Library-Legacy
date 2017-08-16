package com.feed_the_beast.ftbl.lib.util;

import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.client.ParticleColoredDust;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public class UtilsClient extends UtilsCommon
{
	@Override
	String translate(String key)
	{
		return I18n.format(key, CommonUtils.NO_OBJECTS);
	}

	@Override
	String translate(String key, Object... objects)
	{
		return I18n.format(key, objects);
	}

	@Override
	boolean canTranslate(String key)
	{
		return I18n.hasKey(key);
	}

	@Override
	public void spawnDust(World world, double x, double y, double z, float r, float g, float b, float a)
	{
		ClientUtils.spawnParticle(new ParticleColoredDust(world, x, y, z, r, g, b, a));
	}
}