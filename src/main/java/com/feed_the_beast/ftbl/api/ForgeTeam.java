package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.config.ConfigEntryBool;
import com.feed_the_beast.ftbl.api.config.ConfigEntryEnum;
import com.feed_the_beast.ftbl.api.config.ConfigEntryString;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.feed_the_beast.ftbl.api.config.EnumNameMap;
import com.feed_the_beast.ftbl.api.events.ForgeTeamEvent;
import com.feed_the_beast.ftbl.api.security.EnumTeamPrivacyLevel;
import com.latmod.lib.FinalIDObject;
import com.latmod.lib.annotations.IFlagContainer;
import com.latmod.lib.util.LMUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

/**
 * Created by LatvianModder on 26.05.2016.
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class ForgeTeam extends FinalIDObject implements ICapabilitySerializable<NBTTagCompound>, IFlagContainer
{
    public static final byte FREE_TO_JOIN = 1;
    public static final byte HIDDEN = 2;

    public final ForgeWorld world;
    private final CapabilityDispatcher capabilities;
    private final ConfigEntryEnum<EnumTeamColor> color;
    private ForgePlayer owner;
    private Collection<String> allies;
    private Collection<UUID> enemies;
    private String title;
    private String desc;
    private int flags;
    private Collection<ForgePlayerMP> invited;

    public ForgeTeam(ForgeWorld w, String id)
    {
        super(id);
        world = w;

        color = new ConfigEntryEnum<>(EnumTeamColor.BLUE, EnumTeamColor.NAME_MAP);

        ForgeTeamEvent.AttachCapabilities event = new ForgeTeamEvent.AttachCapabilities(this);
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
    public int getFlags()
    {
        return flags;
    }

    @Override
    public void setFlags(int f)
    {
        flags = f;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setString("Owner", LMUtils.fromUUID(owner.getProfile().getId()));
        nbt.setByte("Flags", (byte) flags);
        nbt.setString("Color", EnumNameMap.getEnumName(color.get()));

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
        owner = world.getPlayer(LMUtils.fromString(nbt.getString("Owner")));
        flags = nbt.getByte("Flags");
        color.set(nbt.hasKey("Color") ? EnumTeamColor.NAME_MAP.get(nbt.getString("Color")) : EnumTeamColor.GRAY);
        title = nbt.hasKey("Title") ? nbt.getString("Title") : null;
        desc = nbt.hasKey("Desc") ? nbt.getString("Desc") : null;

        if(capabilities != null)
        {
            capabilities.deserializeNBT(nbt.getCompoundTag("Caps"));
        }
    }

    public NBTTagCompound serializeNBTForNet(ForgePlayerMP to)
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setLong("OM", owner.getProfile().getId().getMostSignificantBits());
        tag.setLong("OL", owner.getProfile().getId().getLeastSignificantBits());

        if(flags != 0)
        {
            tag.setByte("F", (byte) flags);
        }

        tag.setByte("C", (byte) color.getIndex());

        if(title != null)
        {
            tag.setString("T", title);
        }

        if(desc != null)
        {
            tag.setString("D", desc);
        }

        NBTTagCompound sync = new NBTTagCompound();
        MinecraftForge.EVENT_BUS.post(new ForgeTeamEvent.Sync(this, Side.SERVER, sync, to));

        if(!sync.hasNoTags())
        {
            tag.setTag("SY", sync);
        }

        return tag;
    }

    public void deserializeNBTFromNet(NBTTagCompound nbt)
    {
        owner = world.getPlayer(new UUID(nbt.getLong("OM"), nbt.getLong("OL")));
        flags = nbt.getByte("F");
        color.setIndex(nbt.getByte("C"));
        title = nbt.hasKey("T") ? nbt.getString("T") : null;
        desc = nbt.hasKey("D") ? nbt.getString("D") : null;

        MinecraftForge.EVENT_BUS.post(new ForgeTeamEvent.Sync(this, Side.CLIENT, nbt.getCompoundTag("SY"), ForgeWorldSP.inst.clientPlayer));
    }

    public ForgePlayer getOwner()
    {
        return owner;
    }

    public String getTitle()
    {
        return (title == null) ? (owner.getProfile().getName() + "'s Team") : title;
    }

    public void setTitle(@Nullable String s)
    {
        title = (s == null || s.isEmpty()) ? null : s;
    }

    @Nullable
    public String getDesc()
    {
        return desc;
    }

    public void setDesc(@Nullable String s)
    {
        desc = (s == null || s.isEmpty()) ? null : s;
    }

    public EnumTeamColor getColor()
    {
        return color.get();
    }

    public void setColor(EnumTeamColor col)
    {
        color.set(col);
    }

    public EnumTeamStatus getStatus(@Nullable ForgePlayer player)
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
        else if(player.hasTeam())
        {
            ForgeTeam team = player.getTeam();

            if(team.equals(this))
            {
                return EnumTeamStatus.MEMBER;
            }

            if(allies != null && allies.contains(team.getID()))
            {
                return EnumTeamStatus.ALLY;
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

    public boolean isEnemy(UUID player)
    {
        return enemies != null && enemies.contains(player);
    }

    public Collection<ForgePlayer> getMembers()
    {
        Collection<ForgePlayer> c = new HashSet<>();

        for(ForgePlayer p : world.playerMap.values())
        {
            if(getStatus(p).isMember())
            {
                c.add(p);
            }
        }

        return c;
    }

    public boolean addPlayer(ForgePlayerMP player)
    {
        if(!player.isMemberOf(this) && isInvited(player))
        {
            player.setTeamID(getID());
            MinecraftForge.EVENT_BUS.post(new ForgeTeamEvent.PlayerJoined(this, player));
            invited.remove(player);
            return true;
        }

        return false;
    }

    public void removePlayer(ForgePlayerMP player)
    {
        if(player.isMemberOf(this))
        {
            player.setTeamID(null);
            MinecraftForge.EVENT_BUS.post(new ForgeTeamEvent.PlayerLeft(this, player));
        }
    }

    public boolean inviteMember(@Nullable ForgePlayerMP player)
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

    public boolean isInvited(@Nullable ForgePlayerMP player)
    {
        return player != null && (((getFlags() & FREE_TO_JOIN) != 0) || invited != null && invited.contains(player) && !player.hasTeam());
    }

    public void changeOwner(ForgePlayerMP newOwner)
    {
        if(owner == null)
        {
            owner = newOwner;
            newOwner.setTeamID(getID());
        }
        else
        {
            ForgePlayerMP oldOwner = owner.toMP();

            if(!oldOwner.equalsPlayer(newOwner) && newOwner.isMemberOf(this))
            {
                owner = newOwner;
                MinecraftForge.EVENT_BUS.post(new ForgeTeamEvent.OwnerChanged(this, oldOwner, newOwner));
            }
        }
    }

    public boolean canInteract(ForgePlayer player, EnumTeamPrivacyLevel level)
    {
        switch(level)
        {
            case EVERYONE:
            {
                return true;
            }
            case MEMBERS:
            {
                return getStatus(player).isMember();
            }
            case ALLIES:
            {
                return getStatus(player).isAlly();
            }
            default:
            {
                return false;
            }
        }
    }

    public void getSettings(ConfigGroup group)
    {
        MinecraftForge.EVENT_BUS.post(new ForgeTeamEvent.GetSettings(this, group));

        group.add("color", color);

        group.add("title", new ConfigEntryString(title == null ? "" : title)
        {
            @Override
            public void set(String v)
            {
                setTitle(v.trim());
            }

            @Override
            public String getAsString()
            {
                return title == null ? "" : title;
            }
        });

        group.add("desc", new ConfigEntryString(desc == null ? "" : desc)
        {
            @Override
            public void set(String v)
            {
                setDesc(v.trim());
            }

            @Override
            public String getAsString()
            {
                return desc == null ? "" : desc;
            }
        });

        group.add("free_to_join", new ConfigEntryBool(false)
        {
            @Override
            public void set(boolean v)
            {
                setFlag(FREE_TO_JOIN, v);
            }

            @Override
            public boolean getAsBoolean()
            {
                return getFlag(FREE_TO_JOIN);
            }
        });

        group.add("is_hidden", new ConfigEntryBool(false)
        {
            @Override
            public void set(boolean v)
            {
                setFlag(HIDDEN, v);
            }

            @Override
            public boolean getAsBoolean()
            {
                return getFlag(HIDDEN);
            }
        });
    }
}