package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.EnumTeamColor;
import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.events.team.AttachTeamCapabilitiesEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamOwnerChangedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamPlayerLeftEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamSettingsEvent;
import com.feed_the_beast.ftbl.api.security.EnumTeamPrivacyLevel;
import com.feed_the_beast.ftbl.lib.EnumNameMap;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyEnum;
import com.feed_the_beast.ftbl.lib.config.PropertyEnumAbstract;
import com.feed_the_beast.ftbl.lib.config.PropertyString;
import com.feed_the_beast.ftbl.lib.config.SimpleConfigKey;
import com.feed_the_beast.ftbl.lib.io.Bits;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

/**
 * Created by LatvianModder on 26.05.2016.
 */
public final class ForgeTeam extends FinalIDObject implements IForgeTeam, ICapabilitySerializable<NBTTagCompound>
{
    public static final EnumNameMap<EnumTeamColor> COLOR_NAME_MAP = new EnumNameMap<>(EnumTeamColor.values(), false);

    private final Universe world;
    private final CapabilityDispatcher capabilities;
    private final PropertyEnumAbstract<EnumTeamColor> color;
    private ForgePlayer owner;
    private Collection<String> allies;
    private Collection<UUID> enemies;
    private String title;
    private String desc;
    private byte flags;
    private Collection<IForgePlayer> invited;

    public ForgeTeam(Universe w, String id)
    {
        super(id);
        world = w;

        color = new PropertyEnum<>(COLOR_NAME_MAP, EnumTeamColor.BLUE);

        AttachTeamCapabilitiesEvent event = new AttachTeamCapabilitiesEvent(this);
        MinecraftForge.EVENT_BUS.post(event);
        capabilities = !event.getCapabilities().isEmpty() ? new CapabilityDispatcher(event.getCapabilities(), null) : null;
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
    public boolean getFlag(byte flag)
    {
        return (flags & flag) != 0;
    }

    public void setFlag(byte flag, boolean v)
    {
        flags = (byte) Bits.setFlag(flags, flag, v);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setString("Owner", owner.getStringUUID());
        nbt.setByte("Flags", flags);
        nbt.setString("Color", color.getString());

        if(title != null)
        {
            nbt.setString("Title", title);
        }

        if(desc != null)
        {
            nbt.setString("Desc", desc);
        }

        if(capabilities != null)
        {
            nbt.setTag("Caps", capabilities.serializeNBT());
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        owner = world.getPlayer(LMStringUtils.fromString(nbt.getString("Owner")));
        flags = nbt.getByte("Flags");
        color.set(nbt.hasKey("Color") ? COLOR_NAME_MAP.get(nbt.getString("Color")) : EnumTeamColor.GRAY);
        title = nbt.hasKey("Title") ? nbt.getString("Title") : null;
        desc = nbt.hasKey("Desc") ? nbt.getString("Desc") : null;

        if(capabilities != null)
        {
            capabilities.deserializeNBT(nbt.getCompoundTag("Caps"));
        }
    }

    @Override
    public IUniverse getUniverse()
    {
        return world;
    }

    @Override
    public IForgePlayer getOwner()
    {
        return owner;
    }

    @Override
    public String getTitle()
    {
        return (title == null) ? (owner.getProfile().getName() + "'s Team") : title;
    }

    public void setTitle(@Nullable String s)
    {
        title = (s == null || s.isEmpty()) ? null : s;
    }

    @Override
    @Nullable
    public String getDesc()
    {
        return desc;
    }

    public void setDesc(@Nullable String s)
    {
        desc = (s == null || s.isEmpty()) ? null : s;
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
    public EnumTeamStatus getStatus(@Nullable IForgePlayer player)
    {
        if(player == null)
        {
            return EnumTeamStatus.NONE;
        }
        else if(owner.equalsPlayer(player))
        {
            return EnumTeamStatus.OWNER;
        }
        else if(enemies != null && enemies.contains(player.getProfile().getId()))
        {
            return EnumTeamStatus.ENEMY;
        }
        else
        {
            IForgeTeam team = player.getTeam();

            if(team != null)
            {
                if(team.equals(this))
                {
                    return EnumTeamStatus.MEMBER;
                }
                else if(allies != null && allies.contains(team.getName()) && team.isAllyTeam(getName()))
                {
                    return EnumTeamStatus.ALLY;
                }
            }
        }

        return EnumTeamStatus.NONE;
    }

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

    public boolean addEnemy(UUID player)
    {
        if(!isEnemy(player))
        {
            if(enemies == null)
            {
                enemies = new HashSet<>();
            }

            enemies.add(player);
            return true;
        }

        return false;
    }

    public boolean removeEnemy(UUID player)
    {
        if(isEnemy(player))
        {
            enemies.remove(player);

            if(enemies.isEmpty())
            {
                enemies = null;
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean isEnemy(UUID player)
    {
        return enemies != null && enemies.contains(player);
    }

    @Override
    public Collection<IForgePlayer> getMembers()
    {
        Collection<IForgePlayer> c = new HashSet<>();

        for(IForgePlayer p : world.getPlayers())
        {
            if(p.isMemberOf(this))
            {
                c.add(p);
            }
        }

        return c;
    }

    public boolean addPlayer(ForgePlayer player)
    {
        if(!player.isMemberOf(this) && isInvited(player))
        {
            player.setTeamID(getName());
            MinecraftForge.EVENT_BUS.post(new ForgeTeamPlayerJoinedEvent(this, player));
            invited.remove(player);
            return true;
        }

        return false;
    }

    public void removePlayer(ForgePlayer player)
    {
        if(player.isMemberOf(this))
        {
            player.setTeamID(null);
            MinecraftForge.EVENT_BUS.post(new ForgeTeamPlayerLeftEvent(this, player));
        }
    }

    public boolean inviteMember(@Nullable ForgePlayer player)
    {
        if(!isInvited(player))
        {
            if(invited == null)
            {
                invited = new HashSet<>();
            }

            invited.add(player);
            return true;
        }

        return false;
    }

    @Override
    public boolean isInvited(@Nullable IForgePlayer player)
    {
        return player != null && (getFlag(IForgeTeam.FREE_TO_JOIN) || invited != null && invited.contains(player) && player.getTeam() != null);
    }

    public void changeOwner(ForgePlayer newOwner)
    {
        if(owner == null)
        {
            owner = newOwner;
            newOwner.setTeamID(getName());
        }
        else
        {
            IForgePlayer oldOwner = owner;

            if(!oldOwner.equalsPlayer(newOwner) && newOwner.isMemberOf(this))
            {
                owner = newOwner;
                MinecraftForge.EVENT_BUS.post(new ForgeTeamOwnerChangedEvent(this, oldOwner, newOwner));
            }
        }
    }

    @Override
    public boolean canInteract(IForgePlayer player, EnumTeamPrivacyLevel level)
    {
        switch(level)
        {
            case EVERYONE:
                return true;
            case MEMBERS:
                return getStatus(player).isMember();
            case ALLIES:
                return getStatus(player).isAlly();
            default:
                return false;
        }
    }

    public void getSettings(IConfigTree tree)
    {
        MinecraftForge.EVENT_BUS.post(new ForgeTeamSettingsEvent(this, tree));

        tree.add(new SimpleConfigKey("color"), color);

        tree.add(new SimpleConfigKey("title"), new PropertyString(title == null ? "" : title)
        {
            @Override
            public void setString(String v)
            {
                setTitle(v.trim());
            }

            @Override
            public String getString()
            {
                return title == null ? "" : title;
            }
        });

        tree.add(new SimpleConfigKey("desc"), new PropertyString(desc == null ? "" : desc)
        {
            @Override
            public void setString(String v)
            {
                setDesc(v.trim());
            }

            @Override
            public String getString()
            {
                return desc == null ? "" : desc;
            }
        });

        tree.add(new SimpleConfigKey("free_to_join"), new PropertyBool(false)
        {
            @Override
            public void setBoolean(boolean v)
            {
                setFlag(IForgeTeam.FREE_TO_JOIN, v);
            }

            @Override
            public boolean getBoolean()
            {
                return getFlag(IForgeTeam.FREE_TO_JOIN);
            }
        });

        tree.add(new SimpleConfigKey("is_hidden"), new PropertyBool(false)
        {
            @Override
            public void setBoolean(boolean v)
            {
                setFlag(IForgeTeam.HIDDEN, v);
            }

            @Override
            public boolean getBoolean()
            {
                return getFlag(IForgeTeam.HIDDEN);
            }
        });
    }
}