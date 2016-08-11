package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.IForgeWorld;
import com.feed_the_beast.ftbl.api.events.world.AttachWorldCapabilitiesEvent;
import com.feed_the_beast.ftbl.api.events.world.ForgeWorldClosedEvent;
import com.feed_the_beast.ftbl.api.events.world.ForgeWorldLoadedBeforePlayersEvent;
import com.feed_the_beast.ftbl.api.events.world.ForgeWorldLoadedEvent;
import com.feed_the_beast.ftbl.api.events.world.ForgeWorldPostLoadedEvent;
import com.latmod.lib.util.LMUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public class ForgeWorld implements IForgeWorld
{
    public final Map<UUID, ForgePlayer> playerMap;
    public final Map<String, ForgeTeam> teams;
    final CapabilityDispatcher capabilities;
    private ForgePlayer currentPlayer;
    private ForgeTeam currentTeam;

    ForgeWorld()
    {
        playerMap = new HashMap<>();
        teams = new HashMap<>();

        AttachWorldCapabilitiesEvent event = new AttachWorldCapabilitiesEvent(this);
        MinecraftForge.EVENT_BUS.post(event);
        capabilities = !event.getCapabilities().isEmpty() ? new CapabilityDispatcher(event.getCapabilities(), null) : null;
        MinecraftForge.EVENT_BUS.post(new ForgeWorldLoadedEvent(this));
    }

    @Override
    public final boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capabilities != null && capabilities.hasCapability(capability, facing);
    }

    @Override
    public final <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capabilities == null ? null : capabilities.getCapability(capability, facing);
    }

    @Override
    public Collection<? extends IForgePlayer> getPlayers()
    {
        return playerMap.values();
    }

    public ForgePlayer getPlayer(Object o)
    {
        if(o == null)
        {
            return null;
        }
        else if(o instanceof FakePlayer)
        {
            return new ForgePlayerFake((FakePlayer) o);
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
        else if(o instanceof IForgePlayer)
        {
            return getPlayer(((IForgePlayer) o).getProfile().getId());
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

    @Override
    public IForgePlayer getCurrentPlayer()
    {
        return currentPlayer;
    }

    @Override
    public Collection<? extends IForgeTeam> getTeams()
    {
        return teams.values();
    }

    @Override
    public ForgeTeam getTeam(String id)
    {
        return teams.get(id);
    }

    @Override
    public IForgeTeam getCurrentTeam()
    {
        return currentTeam;
    }

    public void onClosed()
    {
        MinecraftForge.EVENT_BUS.post(new ForgeWorldClosedEvent(this));
        playerMap.clear();
    }

    public Collection<IForgePlayer> getOnlinePlayers()
    {
        Collection<IForgePlayer> l = new HashSet<>();

        for(IForgePlayer p : playerMap.values())
        {
            if(p.isOnline())
            {
                l.add(p);
            }
        }

        return l;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        if(capabilities != null)
        {
            capabilities.deserializeNBT(nbt.getCompoundTag("ForgeCaps"));
        }

        MinecraftForge.EVENT_BUS.post(new ForgeWorldLoadedBeforePlayersEvent(this));

        NBTTagList list = nbt.getTagList("Players", Constants.NBT.TAG_COMPOUND);

        for(int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            UUID id = LMUtils.fromString(tag.getString("UUID"));

            if(id != null)
            {
                ForgePlayer p = new ForgePlayer(new GameProfile(id, tag.getString("Name")));
                currentPlayer = p;
                p.deserializeNBT(tag);
                playerMap.put(id, p);
            }
        }

        currentPlayer = null;

        teams.clear();
        NBTTagList teamsTag = nbt.getTagList("Teams", Constants.NBT.TAG_COMPOUND);

        for(int i = 0; i < teamsTag.tagCount(); i++)
        {
            NBTTagCompound tag2 = teamsTag.getCompoundTagAt(i);
            ForgeTeam team = new ForgeTeam(this, tag2.getString("ID"));
            currentTeam = team;
            team.deserializeNBT(tag2);
            teams.put(team.getID(), team);
        }

        currentTeam = null;

        MinecraftForge.EVENT_BUS.post(new ForgeWorldPostLoadedEvent(this));
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound tag2;

        if(capabilities != null)
        {
            tag.setTag("ForgeCaps", capabilities.serializeNBT());
        }

        NBTTagList tagPlayers = new NBTTagList();

        for(ForgePlayer p : playerMap.values())
        {
            currentPlayer = p;
            tag2 = p.serializeNBT();
            tag2.setString("Name", p.getProfile().getName());
            tag2.setString("UUID", LMUtils.fromUUID(p.getProfile().getId()));
            tagPlayers.appendTag(tag2);
        }

        currentPlayer = null;

        tag.setTag("Players", tagPlayers);

        NBTTagList teamsTag = new NBTTagList();

        for(ForgeTeam team : teams.values())
        {
            currentTeam = team;
            tag2 = team.serializeNBT();
            tag2.setString("ID", team.getID());
            teamsTag.appendTag(tag2);
        }

        currentTeam = null;

        tag.setTag("Teams", teamsTag);

        return tag;
    }
}
