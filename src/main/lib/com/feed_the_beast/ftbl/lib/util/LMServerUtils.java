package com.feed_the_beast.ftbl.lib.util;

import com.feed_the_beast.ftbl.lib.EnumNameMap;
import com.feed_the_beast.ftbl.lib.math.BlockDimPos;
import com.feed_the_beast.ftbl.lib.math.EntityDimPos;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by LatvianModder on 08.09.2016.
 */
public class LMServerUtils
{
    public static final EnumNameMap<TextFormatting> TEXT_FORMATTING_NAME_MAP = new EnumNameMap<>(TextFormatting.values(), false);

    private static class TeleporterBlank extends Teleporter
    {
        private TeleporterBlank(WorldServer w)
        {
            super(w);
        }

        @Override
        public boolean makePortal(Entity e)
        {
            return true;
        }

        @Override
        public boolean placeInExistingPortal(Entity e, float f)
        {
            return true;
        }

        @Override
        public void placeInPortal(Entity entity, float f)
        {
            entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationPitch, entity.rotationYaw);
            entity.motionX = 0D;
            entity.motionY = 0D;
            entity.motionZ = 0D;
            entity.fallDistance = 0F;
        }

        @Override
        public void removeStalePortalLocations(long l)
        {
        }
    }

    public static boolean teleportPlayer(Entity entity, EntityDimPos pos)
    {
        return teleportPlayer(entity, pos.pos, pos.dim);
    }

    public static boolean teleportPlayer(Entity entity, BlockDimPos pos)
    {
        return teleportPlayer(entity, pos.toVec(), pos.dim);
    }

    public static boolean teleportPlayer(Entity entity, Vec3d pos, int dim)
    {
        entity.fallDistance = 0F;
        EntityPlayerMP player = entity instanceof EntityPlayer ? (EntityPlayerMP) entity : null;

        if(dim == entity.dimension)
        {
            if(pos.xCoord == entity.posX && pos.yCoord == entity.posY && pos.zCoord == entity.posZ)
            {
                return true;
            }

            if(player != null)
            {
                player.connection.setPlayerLocation(pos.xCoord, pos.yCoord, pos.zCoord, player.rotationYaw, player.rotationPitch);
                return true;
            }
        }

        //FTBLib.dev_logger.info("Teleporting " + entity + " from " + new EntityPos(entity) + " to " + new EntityPos(x, y, z, dim));
        int from = entity.dimension;
        float rotationYaw = entity.rotationYaw;
        float rotationPitch = entity.rotationPitch;
        MinecraftServer server = LMServerUtils.getServer();
        WorldServer fromDim = server.worldServerForDimension(from);
        WorldServer toDim = server.worldServerForDimension(dim);

        if(player != null)
        {
            server.getPlayerList().transferPlayerToDimension(player, dim, new TeleporterBlank(toDim));
            if(from == 1 && entity.isEntityAlive())
            {
                // getMode around vanilla End hacks
                toDim.spawnEntityInWorld(entity);
                toDim.updateEntityWithOptionalForce(entity, false);
            }
        }
        else
        {
            NBTTagCompound tagCompound = new NBTTagCompound();
            entity.writeToNBT(tagCompound);
            Class<? extends Entity> entityClass = entity.getClass();
            fromDim.removeEntity(entity);

            try
            {
                Entity newEntity = entityClass.getConstructor(World.class).newInstance(toDim);
                newEntity.readFromNBT(tagCompound);
                newEntity.setLocationAndAngles(pos.xCoord, pos.yCoord, pos.zCoord, rotationYaw, rotationPitch);
                newEntity.forceSpawn = true;
                toDim.spawnEntityInWorld(newEntity);
                newEntity.forceSpawn = false;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        entity.fallDistance = 0;
        entity.rotationYaw = rotationYaw;
        entity.rotationPitch = rotationPitch;

        if(player != null)
        {
            player.setPositionAndUpdate(pos.xCoord, pos.yCoord, pos.zCoord);
        }
        else
        {
            entity.setPosition(pos.xCoord, pos.yCoord, pos.zCoord);
        }

        return true;
    }

    public static double getMovementFactor(int dim)
    {
        switch(dim)
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

    @Nullable
    public static BlockDimPos getSpawnPoint(int dim)
    {
        WorldServer w = DimensionManager.getWorld(dim);
        return (w == null) ? null : new BlockDimPos(w.getSpawnPoint(), dim);
    }

    public static ITextComponent getChatComponent(Object o)
    {
        return (o instanceof ITextComponent) ? (ITextComponent) o : new TextComponentString(String.valueOf(o));
    }

    public static MinecraftServer getServer()
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    public static boolean hasOnlinePlayers()
    {
        return !getServer().getPlayerList().getPlayerList().isEmpty();
    }

    public static WorldServer getServerWorld()
    {
        return getServer().worldServers[0];
    }

    public static boolean isOP(GameProfile p)
    {
        return getServer().getPlayerList().canSendCommands(p);
    }

    public static Collection<ICommand> getAllCommands(MinecraftServer server, ICommandSender sender)
    {
        Collection<ICommand> commands = new HashSet<>();

        for(ICommand c : server.getCommandManager().getCommands().values())
        {
            if(c.checkPermission(server, sender))
            {
                commands.add(c);
            }
        }

        return commands;
    }

    //null - can't, TRUE - always spawns, FALSE - only spawns at night
    public static Boolean canMobSpawn(World world, BlockPos pos)
    {
        if(pos.getY() < 0 || pos.getY() >= 256)
        {
            return null;
        }

        Chunk chunk = world.getChunkFromBlockCoords(pos);

        if(!WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, world, pos) || chunk.getLightFor(EnumSkyBlock.BLOCK, pos) >= 8)
        {
            return null;
        }

        AxisAlignedBB aabb = new AxisAlignedBB(pos.getX() + 0.2, pos.getY() + 0.01, pos.getZ() + 0.2, pos.getX() + 0.8, pos.getY() + 1.8, pos.getZ() + 0.8);
        if(!world.checkNoEntityCollision(aabb) || world.containsAnyLiquid(aabb))
        {
            return null;
        }

        if(chunk.getLightFor(EnumSkyBlock.SKY, pos) >= 8)
        {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static Entity getEntityByUUID(World worldObj, UUID uuid)
    {
        for(Entity e : worldObj.loadedEntityList)
        {
            if(e.getUniqueID().equals(uuid))
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
}