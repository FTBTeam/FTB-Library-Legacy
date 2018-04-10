package com.feed_the_beast.ftblib.lib.util;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.lib.math.BlockDimPos;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.server.FMLServerHandler;
import net.minecraftforge.server.command.TextComponentHelper;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class ServerUtils
{
	public enum SpawnType
	{
		CANT_SPAWN,
		ALWAYS_SPAWNS,
		ONLY_AT_NIGHT
	}

	private static class TeleporterBlank extends Teleporter
	{
		private TeleporterBlank(WorldServer world)
		{
			super(world);
		}

		@Override
		public boolean makePortal(Entity entity)
		{
			return true;
		}

		@Override
		public boolean placeInExistingPortal(Entity entity, float rotationYaw)
		{
			return true;
		}

		@Override
		public void placeInPortal(Entity entity, float rotationYaw)
		{
			int x = MathHelper.floor(entity.posX);
			int y = MathHelper.floor(entity.posY) - 1;
			int z = MathHelper.floor(entity.posZ);
			entity.setLocationAndAngles(x, y, z, entity.rotationPitch, entity.rotationYaw);
			entity.motionX = 0D;
			entity.motionY = 0D;
			entity.motionZ = 0D;
			entity.fallDistance = 0F;
		}

		@Override
		public void removeStalePortalLocations(long worldTime)
		{
		}
	}

	public static boolean teleportEntity(Entity entity, BlockDimPos pos)
	{
		return teleportEntity(entity, pos.getBlockPos(), pos.dim);
	}

	//Most of this code comes from EnderIO
	public static boolean teleportEntity(Entity entity, BlockPos pos, int targetDim)
	{
		if (entity.getEntityWorld().isRemote || entity.isRiding() || entity.isBeingRidden() || !entity.isEntityAlive())
		{
			return false;
		}

		EntityPlayerMP player = null;

		if (entity instanceof EntityPlayerMP)
		{
			player = (EntityPlayerMP) entity;
		}

		int from = entity.dimension;
		if (from != targetDim)
		{
			MinecraftServer server = player == null ? entity.getServer() : player.mcServer;
			WorldServer fromDim = server.getWorld(from);
			WorldServer toDim = server.getWorld(targetDim);
			Teleporter teleporter = new TeleporterBlank(toDim);

			if (player != null)
			{
				server.getPlayerList().transferPlayerToDimension(player, targetDim, teleporter);
				if (from == 1 && entity.isEntityAlive())
				{
					toDim.spawnEntity(entity);
					toDim.updateEntityWithOptionalForce(entity, false);
				}
			}
			else
			{
				NBTTagCompound tagCompound = entity.serializeNBT();
				float rotationYaw = entity.rotationYaw;
				float rotationPitch = entity.rotationPitch;
				fromDim.removeEntity(entity);
				Entity newEntity = EntityList.createEntityFromNBT(tagCompound, toDim);

				if (newEntity != null)
				{
					newEntity.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), rotationYaw, rotationPitch);
					newEntity.forceSpawn = true;
					toDim.spawnEntity(newEntity);
					newEntity.forceSpawn = false;
				}
				else
				{
					return false;
				}
			}
		}

		if (!entity.world.isBlockLoaded(pos))
		{
			entity.world.getChunkFromBlockCoords(pos);
		}

		if (player != null && player.connection != null)
		{
			player.connection.setPlayerLocation(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, player.rotationYaw, player.rotationPitch);
			player.addExperienceLevel(0);
		}
		else
		{
			entity.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D);
		}

		entity.fallDistance = 0;

		if (FTBLibConfig.debugging.log_teleport)
		{
			FTBLib.LOGGER.info("'" + entity.getName() + "' teleported to [" + pos.getX() + ',' + pos.getY() + ',' + pos.getZ() + "] in " + ServerUtils.getDimensionName(entity, targetDim).getUnformattedText());
		}

		return true;
	}

	public static double getMovementFactor(int dim)
	{
		switch (dim)
		{
			case 0:
			case 1:
				return 1D;
			case -1:
				return 8D;
			default:
			{
				WorldServer w = DimensionManager.getWorld(dim);
				return (w == null) ? 1D : w.provider.getMovementFactor();
			}
		}
	}

	public static ITextComponent getDimensionName(@Nullable ICommandSender sender, int dim)
	{
		switch (dim)
		{
			case 0:
				return TextComponentHelper.createComponentTranslation(sender, "dimension.overworld");
			case -1:
				return TextComponentHelper.createComponentTranslation(sender, "dimension.nether");
			case 1:
				return TextComponentHelper.createComponentTranslation(sender, "dimension.end");
			default:
				return new TextComponentString("DIM_" + dim);
		}
	}

	public static boolean isVanillaClient(ICommandSender sender)
	{
		if (sender instanceof EntityPlayerMP)
		{
			NetHandlerPlayServer connection = ((EntityPlayerMP) sender).connection;
			return connection != null && !connection.netManager.channel().attr(NetworkRegistry.FML_MARKER).get();
		}

		return false;
	}

	public static boolean isOP(@Nullable MinecraftServer server, GameProfile p)
	{
		if (server == null)
		{
			//Does this even work?
			server = FMLServerHandler.instance().getServer();
		}

		return server.getPlayerList().canSendCommands(p);
	}

	public static Collection<ICommand> getAllCommands(MinecraftServer server, ICommandSender sender)
	{
		Collection<ICommand> commands = new HashSet<>();

		for (ICommand c : server.getCommandManager().getCommands().values())
		{
			if (c.checkPermission(server, sender))
			{
				commands.add(c);
			}
		}

		return commands;
	}

	public static SpawnType canMobSpawn(World world, BlockPos pos)
	{
		if (pos.getY() < 0 || pos.getY() >= 256)
		{
			return SpawnType.CANT_SPAWN;
		}

		Chunk chunk = world.getChunkFromBlockCoords(pos);

		if (!WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, world, pos) || chunk.getLightFor(EnumSkyBlock.BLOCK, pos) >= 8)
		{
			return SpawnType.CANT_SPAWN;
		}

		AxisAlignedBB aabb = new AxisAlignedBB(pos.getX() + 0.2, pos.getY() + 0.01, pos.getZ() + 0.2, pos.getX() + 0.8, pos.getY() + 1.8, pos.getZ() + 0.8);
		if (!world.checkNoEntityCollision(aabb) || world.containsAnyLiquid(aabb))
		{
			return SpawnType.CANT_SPAWN;
		}

		return chunk.getLightFor(EnumSkyBlock.SKY, pos) >= 8 ? SpawnType.ONLY_AT_NIGHT : SpawnType.ALWAYS_SPAWNS;
	}

	@Nullable
	public static Entity getEntityByUUID(World worldObj, UUID uuid)
	{
		for (Entity e : worldObj.loadedEntityList)
		{
			if (e.getUniqueID().equals(uuid))
			{
				return e;
			}
		}

		return null;
	}

	public static void notify(MinecraftServer server, @Nullable EntityPlayer player, ITextComponent component)
	{
		if (player == null)
		{
			for (EntityPlayer player1 : server.getPlayerList().getPlayers())
			{
				player1.sendStatusMessage(component, true);
			}
		}
		else
		{
			player.sendStatusMessage(component, true);
		}
	}
}