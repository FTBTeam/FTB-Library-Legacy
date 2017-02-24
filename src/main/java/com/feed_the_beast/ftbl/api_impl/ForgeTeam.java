package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.EnumTeamColor;
import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamOwnerChangedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamPlayerLeftEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamSettingsEvent;
import com.feed_the_beast.ftbl.lib.EnumNameMap;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.feed_the_beast.ftbl.lib.NBTDataStorage;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import com.feed_the_beast.ftbl.lib.config.PropertyEnum;
import com.feed_the_beast.ftbl.lib.config.PropertyString;
import com.feed_the_beast.ftbl.lib.internal.FTBLibPerms;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 26.05.2016.
 */
public final class ForgeTeam extends FinalIDObject implements IForgeTeam
{
    private static final IConfigKey KEY_COLOR = new ConfigKey("display.color", new PropertyEnum<>(EnumTeamColor.NAME_MAP, EnumTeamColor.BLUE), new TextComponentTranslation("ftbteam.config.display.color"));
    private static final IConfigKey KEY_TITLE = new ConfigKey("display.title", new PropertyString(""), new TextComponentTranslation("ftbteam.config.display.title"));
    private static final IConfigKey KEY_DESC = new ConfigKey("display.desc", new PropertyString(""), new TextComponentTranslation("ftbteam.config.display.desc"));

    public static class Message implements Comparable<Message>
    {
        public final UUID sender;
        public final long time;
        public final String text;

        public Message(UUID s, long t, String txt)
        {
            sender = s;
            time = t;
            text = txt;
        }

        public Message(NBTTagCompound nbt)
        {
            sender = nbt.getUniqueId("Sender");
            time = nbt.getLong("Time");
            text = nbt.getString("Text");
        }

        public Message(ByteBuf io)
        {
            sender = LMNetUtils.readUUID(io);
            time = io.readLong();
            text = ByteBufUtils.readUTF8String(io);
        }

        public NBTTagCompound toNBT()
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setUniqueId("Sender", sender);
            nbt.setLong("Time", time);
            nbt.setString("Text", text);
            return nbt;
        }

        public void write(ByteBuf io)
        {
            LMNetUtils.writeUUID(io, sender);
            io.writeLong(time);
            ByteBufUtils.writeUTF8String(io, text);
        }

        @Override
        public int compareTo(Message o)
        {
            return Long.compare(time, o.time);
        }
    }

    private final NBTDataStorage dataStorage;
    private EnumTeamColor color;
    private IForgePlayer owner;
    private String title;
    private String desc;
    private int flags;
    private Map<UUID, Collection<String>> playerPermissions;
    private List<Message> chatHistory;

    public ForgeTeam(String id)
    {
        super(id);
        color = EnumTeamColor.BLUE;

        title = "";
        desc = "";
        flags = 0;

        dataStorage = FTBLibMod.PROXY.createDataStorage(this, FTBLibModCommon.DATA_PROVIDER_TEAM);
    }

    @Override
    @Nullable
    public INBTSerializable<?> getData(ResourceLocation id)
    {
        return dataStorage == null ? null : dataStorage.get(id);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("Owner", LMStringUtils.fromUUID(owner.getProfile().getId()));
        nbt.setByte("Flags", (byte) flags);
        nbt.setString("Color", EnumNameMap.getName(color));

        if(!title.isEmpty())
        {
            nbt.setString("Title", title);
        }

        if(!desc.isEmpty())
        {
            nbt.setString("Desc", desc);
        }

        if(playerPermissions != null && !playerPermissions.isEmpty())
        {
            NBTTagCompound nbt1 = new NBTTagCompound();

            for(Map.Entry<UUID, Collection<String>> entry : playerPermissions.entrySet())
            {
                if(!entry.getValue().isEmpty())
                {
                    NBTTagList list = new NBTTagList();

                    for(String s : entry.getValue())
                    {
                        list.appendTag(new NBTTagString(s));
                    }

                    nbt1.setTag(LMStringUtils.fromUUID(entry.getKey()), list);
                }
            }

            nbt.setTag("Perms", nbt1);
        }

        if(dataStorage != null)
        {
            nbt.setTag("Data", dataStorage.serializeNBT());
        }

        if(chatHistory != null && !chatHistory.isEmpty())
        {
            NBTTagList list = new NBTTagList();

            for(Message msg : chatHistory)
            {
                list.appendTag(msg.toNBT());
            }

            nbt.setTag("Chat", list);
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        owner = Universe.INSTANCE.getPlayer(LMStringUtils.fromString(nbt.getString("Owner")));
        flags = nbt.getInteger("Flags");
        color = EnumTeamColor.NAME_MAP.get(nbt.getString("Color"));
        title = nbt.getString("Title");
        desc = nbt.getString("Desc");

        if(playerPermissions != null)
        {
            playerPermissions.clear();
        }

        if(nbt.hasKey("Perms"))
        {
            if(playerPermissions == null)
            {
                playerPermissions = new HashMap<>();
            }

            NBTTagCompound nbt1 = nbt.getCompoundTag("Perms");

            for(String s : nbt1.getKeySet())
            {
                UUID id = LMStringUtils.fromString(s);

                if(id != null)
                {
                    NBTTagList list = nbt1.getTagList(s, Constants.NBT.TAG_STRING);
                    Collection<String> c = new HashSet<>(list.tagCount());

                    for(int i = 0; i < list.tagCount(); i++)
                    {
                        c.add(list.getStringTagAt(i));
                    }

                    playerPermissions.put(id, c);
                }
            }
        }

        if(nbt.hasKey("Invited"))
        {
            NBTTagList list = nbt.getTagList("Invited", Constants.NBT.TAG_STRING);

            for(int i = 0; i < list.tagCount(); i++)
            {
                UUID id = LMStringUtils.fromString(list.getStringTagAt(i));
                if(id != null)
                {
                    setHasPermission(id, FTBLibPerms.TEAM_CAN_JOIN, true);
                }
            }
        }

        if(dataStorage != null)
        {
            dataStorage.deserializeNBT(nbt.hasKey("Caps") ? nbt.getCompoundTag("Caps") : nbt.getCompoundTag("Data"));
        }

        if(chatHistory != null)
        {
            chatHistory.clear();
        }

        if(nbt.hasKey("Chat"))
        {
            NBTTagList list = nbt.getTagList("Chat", Constants.NBT.TAG_STRING);

            for(int i = 0; i < list.tagCount(); i++)
            {
                addMessage(new Message(list.getCompoundTagAt(i)));
            }
        }
    }

    public void addMessage(Message message)
    {
        if(chatHistory == null)
        {
            chatHistory = new ArrayList<>();
        }

        while(chatHistory.size() >= 1000)
        {
            chatHistory.remove(0);
        }

        chatHistory.add(message);
    }

    @Override
    public IForgePlayer getOwner()
    {
        return owner;
    }

    @Override
    public String getTitle()
    {
        return title.isEmpty() ? (owner.getProfile().getName() + (owner.getProfile().getName().endsWith("s") ? "' Team" : "'s Team")) : title;
    }

    @Override
    public String getDesc()
    {
        return desc;
    }

    @Override
    public EnumTeamColor getColor()
    {
        return color;
    }

    public void setColor(EnumTeamColor col)
    {
        color = col;
    }

    @Override
    public EnumTeamStatus getHighestStatus(IForgePlayer player)
    {
        if(owner.equalsPlayer(player))
        {
            return EnumTeamStatus.OWNER;
        }
        else if(hasPermission(player.getProfile().getId(), FTBLibPerms.TEAM_IS_ENEMY))
        {
            return EnumTeamStatus.ENEMY;
        }
        else if(player.getTeam() != null && player.getTeam().equals(this))
        {
            return EnumTeamStatus.MEMBER;
        }
        else if(hasPermission(player.getProfile().getId(), FTBLibPerms.TEAM_IS_ALLY))
        {
            return EnumTeamStatus.ALLY;
        }
        else if(hasPermission(player.getProfile().getId(), FTBLibPerms.TEAM_CAN_JOIN))
        {
            return EnumTeamStatus.INVITED;
        }

        return EnumTeamStatus.NONE;
    }

    @Override
    public boolean hasStatus(IForgePlayer player, EnumTeamStatus status)
    {
        IForgeTeam team = player.getTeam();

        switch(status)
        {
            case ENEMY:
                return hasPermission(player.getProfile().getId(), FTBLibPerms.TEAM_IS_ENEMY);
            case OWNER:
                return owner.equalsPlayer(player);
            case MEMBER:
                return owner.equalsPlayer(player) || (team != null && team.equals(this));
            case ALLY:
                return owner.equalsPlayer(player) || (team != null && (team.equals(this) || hasPermission(player.getProfile().getId(), FTBLibPerms.TEAM_IS_ALLY)));
            default:
                return false;
        }
    }

    @Override
    public Collection<IForgePlayer> getPlayersWithStatus(Collection<IForgePlayer> c, EnumTeamStatus status)
    {
        for(IForgePlayer p : Universe.INSTANCE.getPlayers())
        {
            if(hasStatus(p, status))
            {
                c.add(p);
            }
        }

        return c;
    }

    @Override
    public boolean addPlayer(IForgePlayer player)
    {
        if(!hasStatus(player, EnumTeamStatus.MEMBER) && hasPermission(player.getProfile().getId(), FTBLibPerms.TEAM_CAN_JOIN))
        {
            player.setTeamID(getName());
            MinecraftForge.EVENT_BUS.post(new ForgeTeamPlayerJoinedEvent(this, player));
            setHasPermission(player.getProfile().getId(), FTBLibPerms.TEAM_CAN_JOIN, false);
            return true;
        }

        return false;
    }

    @Override
    public void removePlayer(IForgePlayer player)
    {
        if(hasStatus(player, EnumTeamStatus.MEMBER))
        {
            player.setTeamID("");
            MinecraftForge.EVENT_BUS.post(new ForgeTeamPlayerLeftEvent(this, player));
        }
    }

    @Override
    public void changeOwner(IForgePlayer newOwner)
    {
        if(owner == null)
        {
            owner = newOwner;
            newOwner.setTeamID(getName());
        }
        else
        {
            IForgePlayer oldOwner = owner;

            if(!oldOwner.equalsPlayer(newOwner) && hasStatus(newOwner, EnumTeamStatus.MEMBER))
            {
                owner = newOwner;
                MinecraftForge.EVENT_BUS.post(new ForgeTeamOwnerChangedEvent(this, oldOwner, newOwner));
            }
        }
    }

    @Override
    public IConfigTree getSettings()
    {
        IConfigTree tree = new ConfigTree();
        MinecraftForge.EVENT_BUS.post(new ForgeTeamSettingsEvent(this, tree));

        tree.add(KEY_COLOR, new PropertyEnum<EnumTeamColor>(EnumTeamColor.NAME_MAP, EnumTeamColor.BLUE)
        {
            @Override
            public EnumTeamColor get()
            {
                return color;
            }

            @Override
            public void set(@Nullable EnumTeamColor e)
            {
                color = e == null ? EnumTeamColor.BLUE : e;
            }
        });

        tree.add(KEY_TITLE, new PropertyString(title)
        {
            @Override
            public void setString(String v)
            {
                title = v.trim();
            }

            @Override
            public String getString()
            {
                return title;
            }
        });

        tree.add(KEY_DESC, new PropertyString(desc)
        {
            @Override
            public void setString(String v)
            {
                desc = v.trim();
            }

            @Override
            public String getString()
            {
                return desc;
            }
        });

        return tree;
    }

    @Override
    public boolean hasPermission(UUID playerID, String permission)
    {
        if(playerID.equals(owner.getProfile().getId()))
        {
            return true;
        }
        else if(playerPermissions == null)
        {
            return false;
        }

        Collection<String> perms = playerPermissions.get(playerID);
        return perms != null && perms.contains(permission);
    }

    @Override
    public boolean setHasPermission(UUID playerID, String permission, boolean val)
    {
        if(val)
        {
            if(playerPermissions == null)
            {
                playerPermissions = new HashMap<>();
            }

            Collection<String> permissions = playerPermissions.get(playerID);

            if(permissions == null)
            {
                permissions = new HashSet<>();
                playerPermissions.put(playerID, permissions);
            }

            return permissions.add(permission);
        }
        else if(playerPermissions != null)
        {
            Collection<String> permissions = playerPermissions.get(playerID);

            if(permissions != null)
            {
                return permissions.remove(permission);
            }
        }

        return false;
    }

    @Override
    public Collection<String> getPermissions(UUID playerID, boolean onlyVisible)
    {
        Collection<String> c = new ArrayList<>();

        for(String permission : (onlyVisible ? FTBLibModCommon.VISIBLE_TEAM_PLAYER_PERMISSIONS : FTBLibModCommon.TEAM_PLAYER_PERMISSIONS))
        {
            if(hasPermission(playerID, permission))
            {
                c.add(permission);
            }
        }

        return c;
    }
}
