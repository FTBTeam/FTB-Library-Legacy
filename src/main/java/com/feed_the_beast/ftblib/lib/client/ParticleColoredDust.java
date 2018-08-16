package com.feed_the_beast.ftblib.lib.client;

import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public class ParticleColoredDust extends ParticleRedstone
{
	public ParticleColoredDust(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float red, float green, float blue, float alpha)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0F, 0F, 0F);
		setRBGColorF(red, green, blue);
		setAlphaF(alpha);
	}
}
