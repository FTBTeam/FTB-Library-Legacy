package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.api.events.universe.ForgeUniverseClosedEvent;
import com.feed_the_beast.ftbl.api.events.universe.ForgeUniverseLoadedBeforePlayersEvent;
import com.feed_the_beast.ftbl.api.events.universe.ForgeUniverseLoadedEvent;
import com.feed_the_beast.ftbl.api.events.universe.ForgeUniversePostLoadedEvent;
import com.feed_the_beast.ftbl.lib.NBTDataStorage;
import com.feed_the_beast.ftbl.lib.util.LMFileUtils;
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
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
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
    private static final Map<UUID, ForgePlayerFake> FAKE_PLAYER_MAP = new HashMap<>();

    public final Map<UUID, ForgePlayer> playerMap = new HashMap<>();
    public final Map<String, ForgeTeam> teams = new HashMap<>();
    private NBTDataStorage dataStorage;

    public void load()
    {
        dataStorage = FTBLibMod.PROXY.createDataStorage(this, FTBLibModCommon.DATA_PROVIDER_UNIVERSE);
        MinecraftForge.EVENT_BUS.post(new ForgeUniverseLoadedEvent(this));

        try
        {
            JsonElement worldData = LMJsonUtils.fromJson(new File(LMUtils.folderWorld, "world_data.json"));

            if(worldData.isJsonObject())
            {
                SharedServerData.INSTANCE.fromJson(worldData.getAsJsonObject());
            }

            playerMap.clear();
            teams.clear();

            File oldFile = new File(LMUtils.folderWorld, "data/FTBLib.dat");

            if(oldFile.exists())
            {
                NBTTagCompound nbt = LMNBTUtils.readTag(oldFile);
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

                oldFile.delete();
            }
            else
            {
                File folder = new File(LMUtils.folderWorld, "data/ftb_lib/");
                MinecraftForge.EVENT_BUS.post(new ForgeUniverseLoadedBeforePlayersEvent(this));

                Map<UUID, NBTTagCompound> playerNBT = new HashMap<>();
                Map<String, NBTTagCompound> teamNBT = new HashMap<>();

                File[] files = new File(folder, "players").listFiles();

                if(files != null && files.length > 0)
                {
                    for(File f : files)
                    {
                        if(f.getName().endsWith(".dat"))
                        {
                            UUID uuid = LMStringUtils.fromString(LMFileUtils.getRawFileName(f));

                            if(uuid != null)
                            {
                                NBTTagCompound nbt = LMNBTUtils.readTag(f);

                                if(nbt != null)
                                {
                                    playerNBT.put(uuid, nbt);
                                    playerMap.put(uuid, new ForgePlayer(new GameProfile(uuid, nbt.getString("Name"))));
                                }
                            }
                        }
                    }
                }

                files = new File(folder, "teams").listFiles();

                if(files != null && files.length > 0)
                {
                    for(File f : files)
                    {
                        if(f.getName().endsWith(".dat"))
                        {
                            NBTTagCompound nbt = LMNBTUtils.readTag(f);

                            if(nbt != null)
                            {
                                String s = LMFileUtils.getRawFileName(f);
                                teamNBT.put(s, nbt);
                                teams.put(s, new ForgeTeam(s));
                            }
                        }
                    }
                }

                for(IForgePlayer player : playerMap.values())
                {
                    player.deserializeNBT(playerNBT.get(player.getId()));
                }

                for(IForgeTeam team : teams.values())
                {
                    team.deserializeNBT(teamNBT.get(team.getName()));
                }

                MinecraftForge.EVENT_BUS.post(new ForgeUniversePostLoadedEvent(this));

                if(dataStorage != null)
                {
                    NBTTagCompound nbt = LMNBTUtils.readTag(new File(folder, "universe.dat"));

                    if(nbt != null)
                    {
                        dataStorage.deserializeNBT(nbt.getCompoundTag("Data"));
                    }
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    @Nullable
    public INBTSerializable<?> getData(ResourceLocation id)
    {
        return dataStorage == null ? null : dataStorage.get(id);
    }

    @Override
    public Collection<? extends IForgePlayer> getPlayers()
    {
        return playerMap.values();
    }

    @Override
    @Nullable
    public ForgePlayer getPlayer(@Nullable Object o)
    {
        if(o == null)
        {
            return null;
        }
        else if(o instanceof FakePlayer)
        {
            FakePlayer fp = (FakePlayer) o;
            ForgePlayerFake p = FAKE_PLAYER_MAP.get(fp.getGameProfile().getId());

            if(p == null)
            {
                p = new ForgePlayerFake(fp);
                p.onLoggedIn(fp, false);
                FAKE_PLAYER_MAP.put(p.getId(), p);
            }

            return p;
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
            return getPlayer(((IForgePlayer) o).getId());
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
                if(p.getName().equalsIgnoreCase(s))
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

    @Override
    public Collection<IForgePlayer> getOnlinePlayers()
    {
        Collection<IForgePlayer> l = Collections.emptySet();

        for(IForgePlayer p : playerMap.values())
        {
            if(p.isOnline())
            {
                if(l.isEmpty())
                {
                    l = new HashSet<>();
                }

                l.add(p);
            }
        }

        return l;
    }

    public void save(File folder) throws Exception
    {
        for(ForgePlayer p : playerMap.values())
        {
            NBTTagCompound nbt = p.serializeNBT();
            nbt.setString("Name", p.getName());
            LMNBTUtils.writeTag(new File(folder, "players/" + LMStringUtils.fromUUID(p.getId()) + ".dat"), nbt);
        }

        for(ForgeTeam team : teams.values())
        {
            LMNBTUtils.writeTag(new File(folder, "teams/" + team.getName() + ".dat"), team.serializeNBT());
        }

        NBTTagCompound nbt = new NBTTagCompound();

        if(dataStorage != null)
        {
            nbt.setTag("Data", dataStorage.serializeNBT());
        }

        LMNBTUtils.writeTag(new File(folder, "universe.dat"), nbt);
    }
}
