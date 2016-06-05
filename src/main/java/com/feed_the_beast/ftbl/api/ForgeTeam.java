package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.events.ForgeTeamEvent;
import gnu.trove.TIntCollection;
import gnu.trove.set.hash.TIntHashSet;
import latmod.lib.Bits;
import latmod.lib.LMUtils;
import latmod.lib.annotations.IFlagContainer;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.EnumDyeColor;
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
public final class ForgeTeam implements ICapabilitySerializable<NBTTagCompound>, IFlagContainer
{
    public static final byte FREE_TO_JOIN = 0;
    public static final byte HIDDEN = 1;

    public final ForgeWorld world;
    public final int teamID;
    CapabilityDispatcher capabilities;
    private ForgePlayer owner;
    private TIntCollection allies;
    private Collection<UUID> enemies;
    private EnumDyeColor color;
    private String title;
    private String desc;
    private byte flags;
    private Collection<ForgePlayerMP> invited;

    public ForgeTeam(ForgeWorld w, int id)
    {
        world = w;
        teamID = id;

        ForgeTeamEvent.AttachCapabilities event = new ForgeTeamEvent.AttachCapabilities(this);
        MinecraftForge.EVENT_BUS.post(event);
        capabilities = !event.getCapabilities().isEmpty() ? new CapabilityDispatcher(event.getCapabilities(), null) : null;
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

    @Override
    public final boolean equals(@Nullable Object o)
    {
        if(o == null)
        {
            return false;
        }
        else if(o == this)
        {
            return true;
        }
        else if(o instanceof ForgeTeam || o instanceof Integer)
        {
            return o.hashCode() == teamID;
        }

        return false;
    }

    @Override
    public final int hashCode()
    {
        return teamID;
    }

    @Override
    public String toString()
    {
        return Integer.toString(teamID);
    }

    @Override
    public void setFlag(byte flag, boolean b)
    {
        flags = Bits.setBit(flags, flag, b);
    }

    @Override
    public boolean getFlag(byte flag)
    {
        return Bits.getBit(flags, flag);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setString("Owner", LMUtils.fromUUID(owner.getProfile().getId()));
        tag.setByte("Flags", flags);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        owner = world.getPlayer(LMUtils.fromString(nbt.getString("Owner")));
        flags = nbt.getByte("Flags");
    }

    public NBTTagCompound serializeNBTForNet(ForgePlayerMP to)
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setLong("OM", owner.getProfile().getId().getMostSignificantBits());
        tag.setLong("OL", owner.getProfile().getId().getLeastSignificantBits());

        if(flags != 0)
        {
            tag.setByte("F", flags);
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

    public EnumDyeColor getColor()
    {
        return color == null ? EnumDyeColor.GREEN : color;
    }

    public void setColor(EnumDyeColor col)
    {
        color = col;
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

            if(allies != null && allies.contains(team.teamID))
            {
                return EnumTeamStatus.ALLY;
            }
        }

        return EnumTeamStatus.NONE;
    }

    public void addAllyTeam(int team)
    {
        if(allies == null || !allies.contains(team))
        {
            if(allies == null)
            {
                allies = new TIntHashSet();
            }

            allies.add(team);
        }
    }

    public void removeAllyTeam(int team)
    {
        if(allies != null && allies.contains(team))
        {
            allies.remove(team);

            if(allies.isEmpty())
            {
                allies = null;
            }
        }
    }

    public void addEnemy(UUID player)
    {
        if(enemies == null || !enemies.contains(player))
        {
            if(enemies == null)
            {
                enemies = new HashSet<>();
            }

            enemies.add(player);
        }
    }

    public void removeEnemy(UUID player)
    {
        if(enemies != null && enemies.contains(player))
        {
            enemies.remove(player);

            if(enemies.isEmpty())
            {
                enemies = null;
            }
        }
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

    public void addPlayer(ForgePlayerMP player)
    {
        if(player.getTeamID() != teamID)
        {
            player.setTeamID(teamID);
            MinecraftForge.EVENT_BUS.post(new ForgeTeamEvent.PlayerJoined(this, player));
        }
    }

    public void removePlayer(ForgePlayerMP player)
    {
        if(player.getTeamID() == teamID)
        {
            player.setTeamID(0);
            MinecraftForge.EVENT_BUS.post(new ForgeTeamEvent.PlayerLeft(this, player));
        }
    }

    public void inviteMember(ForgePlayerMP player)
    {
        if(player != null && (invited == null || !invited.contains(player)) && !player.hasTeam())
        {
            if(invited == null)
            {
                invited = new HashSet<>();
            }

            invited.add(player);
        }
    }

    public boolean isInvited(ForgePlayerMP player)
    {
        return player != null && invited != null && invited.contains(player);
    }

    public void changeOwner(ForgePlayerMP newOwner)
    {
        if(owner == null)
        {
            owner = newOwner;
            newOwner.setTeamID(teamID);
        }
        else
        {
            ForgePlayerMP oldOwner = owner.toMP();

            if(!oldOwner.equalsPlayer(newOwner) && newOwner.getTeamID() == teamID)
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
}