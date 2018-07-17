package com.feed_the_beast.ftblib.lib.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

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

	public static ITextComponent getDimensionName(int dim)
	{
		switch (dim)
		{
			case 0:
				return new TextComponentTranslation("createWorld.customize.preset.overworld");
			case -1:
				return new TextComponentTranslation("advancements.nether.root.title");
			case 1:
				return new TextComponentTranslation("advancements.end.root.title");
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

	public static boolean isFake(EntityPlayerMP player)
	{
		return player.connection == null || player instanceof FakePlayer;
	}

	public static boolean isOP(@Nullable MinecraftServer server, @Nullable GameProfile profile)
	{
		if (profile == null || profile.getId() == null || profile.getName() == null)
		{
			return false;
		}

		if (server == null)
		{
			server = FMLCommonHandler.instance().getMinecraftServerInstance();

			if (server == null)
			{
				return false;
			}
		}

		return server.getPlayerList().canSendCommands(profile);
	}

	public static boolean isOP(EntityPlayerMP player)
	{
		return isOP(player.server, player.getGameProfile());
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

		Chunk chunk = world.getChunk(pos);

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
	public static Entity getEntityByUUID(World world, UUID uuid)
	{
		for (Entity e : world.loadedEntityList)
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