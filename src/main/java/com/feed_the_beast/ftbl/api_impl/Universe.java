package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.api.events.universe.ForgeUniverseClosedEvent;
import com.feed_the_beast.ftbl.api.events.universe.ForgeUniverseLoadedBeforePlayersEvent;
import com.feed_the_beast.ftbl.api.events.universe.ForgeUniverseLoadedEvent;
import com.feed_the_beast.ftbl.api.events.universe.ForgeUniversePostLoadedEvent;
import com.feed_the_beast.ftbl.lib.INBTData;
import com.feed_the_beast.ftbl.lib.NBTDataStorage;
import com.feed_the_beast.ftbl.lib.reg.ResourceLocationIDRegistry;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.feed_the_beast.ftbl.lib.util.LMNBTUtils;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public class Universe implements IUniverse
{
    public static Universe INSTANCE = null;

    public final Map<UUID, ForgePlayer> playerMap = new HashMap<>();
    public final Map<String, ForgeTeam> teams = new HashMap<>();
    private NBTDataStorage dataStorage;
    public final ResourceLocationIDRegistry teamPlayerPermisssionIDs = new ResourceLocationIDRegistry();

    public void init()
    {
        dataStorage = FTBLibRegistries.INSTANCE.createUniverseDataStorage(this);
        MinecraftForge.EVENT_BUS.post(new ForgeUniverseLoadedEvent(this));

        try
        {
            JsonElement worldData = LMJsonUtils.fromJson(new File(LMUtils.folderWorld, "world_data.json"));

            if(worldData.isJsonObject())
            {
                SharedData.SERVER.fromJson(worldData.getAsJsonObject());
            }

            playerMap.clear();

            NBTTagCompound nbt = LMNBTUtils.readTag(new File(LMUtils.folderWorld, "data/FTBLib.dat"));

            if(nbt != null)
            {
                deserializeNBT(nbt);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    @Nullable
    public INBTData getData(ResourceLocation id)
    {
        return dataStorage == null ? null : dataStorage.get(id);
    }

    @Override
    public Collection<? extends IForgePlayer> getPlayers()
    {
        return playerMap.values();
    }

    @Override
    public ForgePlayer getPlayer(@Nullable Object o)
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

            return getPlayer(LMStringUtils.fromString(s));
        }

        return null;
    }

    @Override
    public Collection<? extends IForgeTeam> getTeams()
    {
        return teams.values();
    }

    @Override
    @Nullable
    public ForgeTeam getTeam(String id)
    {
        return teams.get(id);
    }

    public void onClosed()
    {
        MinecraftForge.EVENT_BUS.post(new ForgeUniverseClosedEvent(this));
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
        MinecraftForge.EVENT_BUS.post(new ForgeUniverseLoadedBeforePlayersEvent(this));

        NBTTagList list = nbt.getTagList("Players", Constants.NBT.TAG_COMPOUND);

        for(int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            UUID id = LMStringUtils.fromString(tag.getString("UUID"));

            if(id != null)
            {
                ForgePlayer p = new ForgePlayer(new GameProfile(id, tag.getString("Name")));
                p.deserializeNBT(tag);
                playerMap.put(id, p);
            }
        }

        teamPlayerPermisssionIDs.deserializeNBT(nbt.getCompoundTag("TeamPlayerPermissionIDs"));

        teams.clear();
        NBTTagList teamsTag = nbt.getTagList("Teams", Constants.NBT.TAG_COMPOUND);

        for(int i = 0; i < teamsTag.tagCount(); i++)
        {
            NBTTagCompound tag2 = teamsTag.getCompoundTagAt(i);
            ForgeTeam team = new ForgeTeam(tag2.getString("ID"));
            team.deserializeNBT(tag2);
            teams.put(team.getName(), team);
        }

        MinecraftForge.EVENT_BUS.post(new ForgeUniversePostLoadedEvent(this));

        if(dataStorage != null)
        {
            dataStorage.deserializeNBT(nbt.hasKey("ForgeCaps") ? nbt.getCompoundTag("ForgeCaps") : nbt.getCompoundTag("Data"));
        }
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagCompound nbt1;

        NBTTagList tagPlayers = new NBTTagList();

        for(ForgePlayer p : playerMap.values())
        {
            nbt1 = p.serializeNBT();
            nbt1.setString("Name", p.getProfile().getName());
            nbt1.setString("UUID", LMStringUtils.fromUUID(p.getProfile().getId()));
            tagPlayers.appendTag(nbt1);
        }

        nbt.setTag("Players", tagPlayers);

        NBTTagList teamsTag = new NBTTagList();

        for(ForgeTeam team : teams.values())
        {
            nbt1 = team.serializeNBT();
            nbt1.setString("ID", team.getName());
            teamsTag.appendTag(nbt1);
        }

        nbt.setTag("Teams", teamsTag);
        nbt.setTag("TeamPlayerPermissionIDs", teamPlayerPermisssionIDs.serializeNBT());

        if(dataStorage != null)
        {
            nbt.setTag("Data", dataStorage.serializeNBT());
        }

        return nbt;
    }
}
