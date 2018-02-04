package com.feed_the_beast.ftblib.lib.util;

import com.feed_the_beast.ftblib.FTBLibFinals;
import com.feed_the_beast.ftblib.lib.math.BlockDimPos;
import com.feed_the_beast.ftblib.lib.util.misc.NameMap;
import com.mojang.authlib.GameProfile;
import io.netty.channel.Channel;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
	public static final NameMap<TextFormatting> TEXT_FORMATTING_NAME_MAP = NameMap.create(TextFormatting.RESET, TextFormatting.values());

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
		EntityPlayerMP player = null;

		if (entity instanceof EntityPlayerMP)
		{
			player = (EntityPlayerMP) entity;
		}

		int from = entity.dimension;
		if (from != targetDim)
		{
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
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
				NBTTagCompound tagCompound = new NBTTagCompound();
				float rotationYaw = entity.rotationYaw;
				float rotationPitch = entity.rotationPitch;
				entity.writeToNBT(tagCompound);
				Class<? extends Entity> entityClass = entity.getClass();
				fromDim.removeEntity(entity);

				try
				{
					Entity newEntity = entityClass.getConstructor(World.class).newInstance(toDim);
					newEntity.readFromNBT(tagCompound);
					newEntity.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), rotationYaw, rotationPitch);
					newEntity.forceSpawn = true;
					toDim.spawnEntity(newEntity);
					newEntity.forceSpawn = false;
				}
				catch (Exception ex)
				{
					FTBLibFinals.LOGGER.error("Error creating an entity to be created in new dimension." + ex);
					return false;
				}
			}
		}

		if (!entity.world.isBlockLoaded(pos))
		{
			entity.world.getChunkFromBlockCoords(pos);
		}

		if (player != null)
		{
			player.connection.setPlayerLocation(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, player.rotationYaw, player.rotationPitch);
			player.addExperienceLevel(0);
		}
		else
		{
			entity.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D);
		}

		entity.fallDistance = 0;
		return true;

		/*
		//Old teleporter code
		if (dim != player.dimension)
		{
			WorldServer toDim = player.mcServer.getWorld(dim);
			player.mcServer.getPlayerList().transferPlayerToDimension(player, toDim.provider.getDimension(), new TeleporterBlank(toDim));
		}

		player.connection.setPlayerLocation(pos.x, pos.y, pos.z, player.rotationYaw, player.rotationPitch);
		player.motionX = player.motionY = player.motionZ = 0D;
		player.fallDistance = 0F;
		player.addExperienceLevel(0);
		player.world.updateEntityWithOptionalForce(player, true);
		return true;
		*/
	}

	public static double getMovementFactor(int dim)
	{
		switch (dim)
		{
			case 0:
				return 1D;
			case -1:
				return 8D;
			case 1:
				return 1D;
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
			EntityPlayerMP playerMP = (EntityPlayerMP) sender;
			Channel channel = playerMP.connection.netManager.channel();
			return !channel.attr(NetworkRegistry.FML_MARKER).get();
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