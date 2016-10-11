package com.feed_the_beast.ftbl.api_impl;

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
import com.feed_the_beast.ftbl.lib.INBTData;
import com.feed_the_beast.ftbl.lib.NBTDataStorage;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyEnum;
import com.feed_the_beast.ftbl.lib.config.PropertyEnumAbstract;
import com.feed_the_beast.ftbl.lib.config.PropertyString;
import com.feed_the_beast.ftbl.lib.config.PropertyStringList;
import com.feed_the_beast.ftbl.lib.io.Bits;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

/**
 * Created by LatvianModder on 26.05.2016.
 */
public final class ForgeTeam extends FinalIDObject implements IForgeTeam
{
    private static final EnumNameMap<EnumTeamColor> COLOR_NAME_MAP = new EnumNameMap<>(EnumTeamColor.values(), false);
    private static final IConfigKey KEY_COLOR = new ConfigKey("display.color", new PropertyEnum<>(COLOR_NAME_MAP, EnumTeamColor.BLUE));
    private static final IConfigKey KEY_TITLE = new ConfigKey("display.title", new PropertyString(""));
    private static final IConfigKey KEY_DESC = new ConfigKey("display.desc", new PropertyString(""));
    private static final IConfigKey KEY_FREE_TO_JOIN = new ConfigKey("team.free_to_join", new PropertyBool(false));
    private static final IConfigKey KEY_IS_HIDDEN = new ConfigKey("team.is_hidden", new PropertyBool(false));
    private static final IConfigKey KEY_ALLIES = new ConfigKey("team.allies", new PropertyStringList(Collections.emptyList()));

    private static final byte FLAG_FREE_TO_JOIN = 1;
    private static final byte FLAG_HIDDEN = 2;

    private final NBTDataStorage dataStorage;
    private final PropertyEnumAbstract<EnumTeamColor> color;
    private IForgePlayer owner;
    private Collection<String> allies;
    private Collection<UUID> enemies;
    private String title;
    private String desc;
    private byte flags;
    private Collection<UUID> invited;

    public ForgeTeam(String id)
    {
        super(id);
        color = new PropertyEnum<>(COLOR_NAME_MAP, EnumTeamColor.BLUE);

        title = "";
        desc = "";
        flags = 0;

        dataStorage = FTBLibRegistries.INSTANCE.createTeamDataStorage(this);
    }

    @Override
    @Nullable
    public INBTData getData(ResourceLocation id)
    {
        return dataStorage == null ? null : dataStorage.get(id);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("Owner", LMStringUtils.fromUUID(owner.getProfile().getId()));
        nbt.setByte("Flags", flags);
        nbt.setString("Color", color.getString());

        if(!title.isEmpty())
        {
            nbt.setString("Title", title);
        }

        if(!desc.isEmpty())
        {
            nbt.setString("Desc", desc);
        }

        if(allies != null && !allies.isEmpty())
        {
            NBTTagList list = new NBTTagList();
            for(String s : allies)
            {
                list.appendTag(new NBTTagString(s));
            }

            nbt.setTag("Allies", list);
        }

        if(enemies != null && !enemies.isEmpty())
        {
            NBTTagList list = new NBTTagList();
            for(UUID id : enemies)
            {
                list.appendTag(new NBTTagString(LMStringUtils.fromUUID(id)));
            }

            nbt.setTag("Enemies", list);
        }

        if(invited != null && !invited.isEmpty())
        {
            NBTTagList list = new NBTTagList();
            for(UUID id : invited)
            {
                list.appendTag(new NBTTagString(LMStringUtils.fromUUID(id)));
            }

            nbt.setTag("Invited", list);
        }

        if(dataStorage != null)
        {
            nbt.setTag("Data", dataStorage.serializeNBT());
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        owner = Universe.INSTANCE.getPlayer(LMStringUtils.fromString(nbt.getString("Owner")));
        flags = nbt.getByte("Flags");
        color.set(COLOR_NAME_MAP.get(nbt.getString("Color")));
        title = nbt.getString("Title");
        desc = nbt.getString("Desc");

        if(nbt.hasKey("Allies"))
        {
            if(allies == null)
            {
                allies = new HashSet<>();
            }

            NBTTagList list = nbt.getTagList("Allies", Constants.NBT.TAG_STRING);

            for(int i = 0; i < list.tagCount(); i++)
            {
                allies.add(list.getStringTagAt(i));
            }
        }
        else if(allies != null)
        {
            allies.clear();
        }

        if(nbt.hasKey("Enemies"))
        {
            if(enemies == null)
            {
                enemies = new HashSet<>();
            }

            NBTTagList list = nbt.getTagList("Enemies", Constants.NBT.TAG_STRING);

            for(int i = 0; i < list.tagCount(); i++)
            {
                UUID id = LMStringUtils.fromString(list.getStringTagAt(i));
                if(id != null)
                {
                    enemies.add(id);
                }
            }
        }
        else if(enemies != null)
        {
            enemies.clear();
        }

        if(nbt.hasKey("Invited"))
        {
            if(invited == null)
            {
                invited = new HashSet<>();
            }

            NBTTagList list = nbt.getTagList("Invited", Constants.NBT.TAG_STRING);

            for(int i = 0; i < list.tagCount(); i++)
            {
                UUID id = LMStringUtils.fromString(list.getStringTagAt(i));
                if(id != null)
                {
                    invited.add(id);
                }
            }
        }
        else if(invited != null)
        {
            invited.clear();
        }

        if(dataStorage != null)
        {
            dataStorage.deserializeNBT(nbt.hasKey("Caps") ? nbt.getCompoundTag("Caps") : nbt.getCompoundTag("Data"));
        }
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
    public boolean isFreeToJoin()
    {
        return (flags & FLAG_FREE_TO_JOIN) != 0;
    }

    @Override
    public boolean isHidden()
    {
        return (flags & FLAG_HIDDEN) != 0;
    }

    private void setFreeToJoin(boolean v)
    {
        flags = Bits.setFlag(flags, FLAG_FREE_TO_JOIN, v);
    }

    private void setHidden(boolean v)
    {
        flags = Bits.setFlag(flags, FLAG_HIDDEN, v);
    }

    @Override
    public EnumTeamColor getColor()
    {
        return (EnumTeamColor) color.getValue();
    }

    public void setColor(EnumTeamColor col)
    {
        color.set(col);
    }

    @Override
    public boolean hasStatus(IForgePlayer player, EnumTeamStatus status)
    {
        IForgeTeam team = player.getTeam();

        switch(status)
        {
            case ENEMY:
                return enemies != null && enemies.contains(player.getProfile().getId());
            case OWNER:
                return owner.equalsPlayer(player);
            case MEMBER:
                return team != null && team.equals(this);
            case ALLY:
                return team != null && allies != null && allies.contains(team.getName()) && team.isAllyTeam(getName());
            default:
                return false;
        }
    }

    @Override
    public boolean addAllyTeam(String team)
    {
        if(!isAllyTeam(team))
        {
            if(allies == null)
            {
                allies = new HashSet<>();
            }

            allies.add(team);
            return true;
        }

        return false;
    }

    @Override
    public boolean removeAllyTeam(String team)
    {
        if(isAllyTeam(team))
        {
            allies.remove(team);

            if(allies.isEmpty())
            {
                allies = null;
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean isAllyTeam(String team)
    {
        return allies != null && allies.contains(team);
    }

    public boolean addEnemy(IForgePlayer player)
    {
        if(!hasStatus(player, EnumTeamStatus.ENEMY))
        {
            if(enemies == null)
            {
                enemies = new HashSet<>();
            }

            enemies.add(player.getProfile().getId());
            return true;
        }

        return false;
    }

    public boolean removeEnemy(IForgePlayer player)
    {
        if(hasStatus(player, EnumTeamStatus.ENEMY))
        {
            enemies.remove(player.getProfile().getId());

            if(enemies.isEmpty())
            {
                enemies = null;
            }

            return true;
        }

        return false;
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
        if(!hasStatus(player, EnumTeamStatus.MEMBER) && isInvited(player))
        {
            player.setTeamID(getName());
            MinecraftForge.EVENT_BUS.post(new ForgeTeamPlayerJoinedEvent(this, player));
            invited.remove(player.getProfile().getId());
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
    public boolean inviteMember(IForgePlayer player)
    {
        if(!isInvited(player))
        {
            if(invited == null)
            {
                invited = new HashSet<>();
            }

            invited.add(player.getProfile().getId());
            return true;
        }

        return false;
    }

    @Override
    public boolean isInvited(IForgePlayer player)
    {
        return isFreeToJoin() || (invited != null && invited.contains(player.getProfile().getId()));
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
    public void getSettings(IConfigTree tree)
    {
        MinecraftForge.EVENT_BUS.post(new ForgeTeamSettingsEvent(this, tree));

        tree.add(KEY_COLOR, color);

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

        tree.add(KEY_FREE_TO_JOIN, new PropertyBool(isFreeToJoin())
        {
            @Override
            public void setBoolean(boolean v)
            {
                setFreeToJoin(v);
            }

            @Override
            public boolean getBoolean()
            {
                return isFreeToJoin();
            }
        });

        tree.add(KEY_IS_HIDDEN, new PropertyBool(isHidden())
        {
            @Override
            public void setBoolean(boolean v)
            {
                setHidden(v);
            }

            @Override
            public boolean getBoolean()
            {
                return isHidden();
            }
        });

        tree.add(KEY_ALLIES, new PropertyStringList(allies)
        {
            @Override
            public void setStringList(Collection<String> v)
            {
                allies = v;
            }

            @Override
            public Collection<String> getStringList()
            {
                return allies;
            }
        });
    }
}