package com.feed_the_beast.ftblib.lib.math;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.lib.util.ServerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class TeleporterDimPos implements ITeleporter
{
	public final double posX, posY, posZ;
	public final int dim;

	public static TeleporterDimPos of(double x, double y, double z, int dim)
	{
		return new TeleporterDimPos(x, y, z, dim);
	}

	public static TeleporterDimPos of(Entity entity)
	{
		return new TeleporterDimPos(entity.posX, entity.posY, entity.posZ, entity.dimension);
	}

	public static TeleporterDimPos of(BlockPos pos, int dim)
	{
		return new TeleporterDimPos(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, dim);
	}

	private TeleporterDimPos(double x, double y, double z, int d)
	{
		posX = x;
		posY = y;
		posZ = z;
		dim = d;
	}

	public BlockDimPos block()
	{
		return new BlockDimPos(posX, posY, posZ, dim);
	}

	@Override
	public void placeEntity(World world, Entity entity, float yaw)
	{
		entity.motionX = entity.motionY = entity.motionZ = 0D;
		entity.fallDistance = 0F;

		if (entity instanceof EntityPlayerMP && ((EntityPlayerMP) entity).connection != null)
		{
			((EntityPlayerMP) entity).connection.setPlayerLocation(posX, posY, posZ, yaw, entity.rotationPitch);
		}
		else
		{
			entity.setLocationAndAngles(posX, posY, posZ, yaw, entity.rotationPitch);
		}
	}

	@Nullable
	public Entity teleport(@Nullable Entity entity)
	{
		if (entity == null || entity.world.isRemote)
		{
			return entity;
		}

		if (FTBLibConfig.debugging.log_teleport)
		{
			FTBLib.LOGGER.info("Teleporting '" + entity.getName() + "' to [" + posX + ',' + posY + ',' + posZ + "] in " + ServerUtils.getDimensionName(dim).getUnformattedText());
		}

		if (dim != entity.dimension)
		{
			return entity.changeDimension(dim, this);
		}

		placeEntity(entity.world, entity, entity.rotationYaw);
		return entity;
	}
}