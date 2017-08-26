package com.feed_the_beast.ftbl.lib.util;

import com.feed_the_beast.ftbl.lib.NameMap;
import com.feed_the_beast.ftbl.lib.math.BlockDimPos;
import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
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
			entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationPitch, entity.rotationYaw);
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

	public static void teleportPlayer(EntityPlayerMP player, BlockDimPos pos)
	{
		teleportPlayer(player, pos.toVec(), pos.dim);
	}

	public static void teleportPlayer(EntityPlayerMP player, Vec3d pos, int dim)
	{
		if (dim != player.dimension)
		{
			WorldServer toDim = player.mcServer.getWorld(dim);
			player.mcServer.getPlayerList().transferPlayerToDimension(player, toDim.provider.getDimension(), new TeleporterBlank(toDim));
		}

		player.connection.setPlayerLocation(pos.x, pos.y, pos.z, player.rotationYaw, player.rotationPitch);
		player.motionX = player.motionY = player.motionZ = 0D;
		player.fallDistance = 0F;
		player.world.updateEntityWithOptionalForce(player, true);
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

	public static MinecraftServer getServer()
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		Preconditions.checkNotNull(server);
		return server;
	}

	public static boolean hasOnlinePlayers()
	{
		return !getServer().getPlayerList().getPlayers().isEmpty();
	}

	public static WorldServer getServerWorld()
	{
		return getServer().getWorld(0);
	}

	public static boolean isOP(GameProfile p)
	{
		return getServer().getPlayerList().canSendCommands(p);
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

	public static void addTickable(MinecraftServer server, ITickable tickable)
	{
		server.tickables.add(tickable);
	}

	public static Set<ICommand> getCommandSet(CommandHandler handler)
	{
		return handler.commandSet;
	}

	public static void notify(@Nullable EntityPlayer player, ITextComponent component)
	{
		if (player == null)
		{
			for (EntityPlayer player1 : getServer().getPlayerList().getPlayers())
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