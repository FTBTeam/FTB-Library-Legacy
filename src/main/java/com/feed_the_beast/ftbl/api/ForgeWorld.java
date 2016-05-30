package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.api.events.ForgeWorldEvent;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.mojang.authlib.GameProfile;
import latmod.lib.LMUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public abstract class ForgeWorld implements ICapabilityProvider
{
    public final Map<UUID, ForgePlayer> playerMap;
    public final Map<Integer, ForgeTeam> teams;
    final CapabilityDispatcher capabilities;
    protected UUID worldID;
    protected PackMode currentMode;

    ForgeWorld()
    {
        worldID = null;
        currentMode = new PackMode("default");
        playerMap = new HashMap<>();
        teams = new HashMap<>();

        ForgeWorldEvent.AttachCapabilities event = new ForgeWorldEvent.AttachCapabilities(this);
        MinecraftForge.EVENT_BUS.post(event);
        capabilities = !event.getCapabilities().isEmpty() ? new CapabilityDispatcher(event.getCapabilities(), null) : null;
        MinecraftForge.EVENT_BUS.post(new ForgeWorldEvent.Loaded(this));
    }

    public static ForgeWorld getFrom(Side side)
    {
        if(side == null)
        {
            return getFrom(FTBLib.getEffectiveSide());
        }

        return side.isServer() ? ForgeWorldMP.inst : FTBLibMod.proxy.getClientLMWorld();
    }

    public abstract Side getSide();

    public final UUID getID()
    {
        if(worldID == null || (worldID.getLeastSignificantBits() == 0L && worldID.getMostSignificantBits() == 0L))
        {
            worldID = UUID.randomUUID();
        }

        return worldID;
    }

    @Override
    public final boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capabilities != null && capabilities.hasCapability(capability, facing);
    }

    @Override
    public final <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        return capabilities == null ? null : capabilities.getCapability(capability, facing);
    }

    public abstract World getMCWorld();

    public abstract ForgeWorldMP toWorldMP();

    @SideOnly(Side.CLIENT)
    public abstract ForgeWorldSP toWorldSP();

    public PackMode getMode()
    {
        return currentMode;
    }

    public ForgePlayer getPlayer(Object o)
    {
        if(o == null || o instanceof FakePlayer)
        {
            return null;
        }
        else if(o instanceof UUID)
        {
            UUID id = (UUID) o;
            if(id.getLeastSignificantBits() == 0L && id.getMostSignificantBits() == 0L)
            {
                return null;
            }
            return playerMap.get(id);
        }
        else if(o instanceof ForgePlayer)
        {
            return getPlayer(((ForgePlayer) o).getProfile().getId());
        }
        else if(o instanceof EntityPlayer)
        {
            return getPlayer(((EntityPlayer) o).getGameProfile().getId());
        }
        else if(o instanceof GameProfile)
        {
            return getPlayer(((GameProfile) o).getId());
        }
        else if(o instanceof CharSequence)
        {
            String s = o.toString();

            if(s == null || s.isEmpty())
            {
                return null;
            }

            for(ForgePlayer p : playerMap.values())
            {
                if(p.getProfile().getName().equalsIgnoreCase(s))
                {
                    return p;
                }
            }

            return getPlayer(LMUtils.fromString(s));
        }

        return null;
    }

    public final List<ForgePlayer> getOnlinePlayers()
    {
        ArrayList<ForgePlayer> l = new ArrayList<>();

        for(ForgePlayer p : playerMap.values())
        {
            if(p.isOnline())
            {
                l.add(p);
            }
        }

        return l;
    }

    /**
     * 0 = OK, 1 - Mode is invalid, 2 - Mode already set (will be ignored and return 0, if forced == true)
     */
    public final int setMode(String mode)
    {
        PackMode m = PackModes.instance().getRawMode(mode);

        if(m == null)
        {
            return 1;
        }
        if(m.equals(currentMode))
        {
            return 2;
        }

        currentMode = m;

        return 0;
    }

    public void onClosed()
    {
        MinecraftForge.EVENT_BUS.post(new ForgeWorldEvent.Closed(this));
        playerMap.clear();
    }
}
