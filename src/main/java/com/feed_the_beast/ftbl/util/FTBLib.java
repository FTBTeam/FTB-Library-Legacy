package com.feed_the_beast.ftbl.util;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.api.block.IBlockWithItem;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemBlock;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.regex.Pattern;

public class FTBLib
{
    public static final boolean DEV_ENV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    public static final Logger DEV_LOGGER = LogManager.getLogger("FTBLibDev");
    public static final String FORMATTING = "\u00a7";
    public static final Pattern TEXT_FORMATTING_PATTERN = Pattern.compile("(?i)" + FORMATTING + "[0-9A-FK-OR]");

    public static boolean userIsLatvianModder = false;
    public static File folderConfig, folderMinecraft, folderModpack, folderLocal, folderWorld;

    public static void init(File configFolder)
    {
        folderConfig = configFolder;
        folderMinecraft = folderConfig.getParentFile();
        folderModpack = new File(folderMinecraft, "modpack/");
        folderLocal = new File(folderMinecraft, "local/");

        if(!folderModpack.exists())
        {
            folderModpack.mkdirs();
        }
        if(!folderLocal.exists())
        {
            folderLocal.mkdirs();
        }

        if(DEV_LOGGER instanceof org.apache.logging.log4j.core.Logger)
        {
            if(DEV_ENV)
            {
                ((org.apache.logging.log4j.core.Logger) DEV_LOGGER).setLevel(org.apache.logging.log4j.Level.ALL);
            }
            else
            {
                ((org.apache.logging.log4j.core.Logger) DEV_LOGGER).setLevel(org.apache.logging.log4j.Level.OFF);
            }
        }
        else
        {
            FTBLibMod.logger.info("DevLogger isn't org.apache.logging.log4j.core.Logger! It's " + DEV_LOGGER.getClass().getName());
        }
    }

    @Nonnull
    public static <K extends IForgeRegistryEntry<?>> K register(@Nonnull ResourceLocation id, @Nonnull K object)
    {
        object.setRegistryName(id);
        GameRegistry.register(object);

        if(object instanceof IBlockWithItem)
        {
            ItemBlock ib = ((IBlockWithItem) object).createItemBlock();
            ib.setRegistryName(id);
            GameRegistry.register(ib);
        }

        return object;
    }

    public static void addTile(Class<? extends TileEntity> c, ResourceLocation id)
    {
        if(c != null && id != null)
        {
            GameRegistry.registerTileEntity(c, id.toString().replace(':', '.'));
        }
    }

    public static ITextComponent getChatComponent(Object o)
    {
        return (o instanceof ITextComponent) ? (ITextComponent) o : new TextComponentString(String.valueOf(o));
    }

    /**
     * Prints message to chat (doesn't translate it)
     */
    public static void printChat(ICommandSender ep, Object o)
    {
        if(ep == null)
        {
            ep = FTBLibMod.proxy.getClientPlayer();
        }
        if(ep != null)
        {
            ep.addChatMessage(getChatComponent(o));
        }
        else
        {
            FTBLibMod.logger.info(o);
        }
    }

    public static MinecraftServer getServer()
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    public static boolean isDedicatedServer()
    {
        MinecraftServer mcs = getServer();
        return mcs != null && mcs.isDedicatedServer();
    }

    public static boolean hasOnlinePlayers()
    {
        return getServer() != null && !getServer().getPlayerList().getPlayerList().isEmpty();
    }

    public static String removeFormatting(String s)
    {
        if(s == null)
        {
            return null;
        }
        if(s.isEmpty())
        {
            return "";
        }
        return TEXT_FORMATTING_PATTERN.matcher(s).replaceAll("");
    }

    public static WorldServer getServerWorld()
    {
        MinecraftServer ms = getServer();
        if(ms == null || ms.worldServers.length < 1)
        {
            return null;
        }
        return ms.worldServers[0];
    }

    public static boolean isOP(GameProfile p)
    {
        return !isDedicatedServer() || (p != null && p.getId() != null && getServerWorld() != null && getServer().getPlayerList().getOppedPlayers().getPermissionLevel(p) > 0);
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
        if(world == null || pos == null || pos.getY() < 0 || pos.getY() >= 256)
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
        if(worldObj == null || uuid == null)
        {
            return null;
        }

        for(Entity e : worldObj.loadedEntityList)
        {
            if(e.getUniqueID().equals(uuid))
            {
                return e;
            }
        }

        return null;
    }
}