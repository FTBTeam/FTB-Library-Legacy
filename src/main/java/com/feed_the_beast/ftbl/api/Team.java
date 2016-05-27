package com.feed_the_beast.ftbl.api;

import latmod.lib.Bits;
import latmod.lib.annotations.IFlagContainer;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 26.05.2016.
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class Team implements INBTSerializable<NBTTagCompound>, IFlagContainer
{
    public static final byte ALLOW_INVITE_MEMBERS = 0;

    public final ForgePlayer owner;
    private Map<UUID, EnumTeamStatus> specialStatus;
    private EnumDyeColor color;
    private String title;
    private String desc;
    private byte flags;

    public Team(ForgePlayer o)
    {
        owner = o;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }
        else if(o == this)
        {
            return true;
        }
        else if(o instanceof Team)
        {
            return ((Team) o).owner.equalsPlayer(owner);
        }

        return owner.equals(o);
    }

    @Override
    public int hashCode()
    {
        return owner.hashCode();
    }

    @Override
    public String toString()
    {
        return owner.toString();
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

    public EnumTeamStatus getStatusOf(ForgePlayer player)
    {
        if(player.equalsPlayer(owner))
        {
            return EnumTeamStatus.OWNER;
        }

        UUID id = player.getProfile().getId();

        if(specialStatus.containsKey(id))
        {
            return specialStatus.get(id);
        }

        if(player.getTeam().equals(this))
        {
            return EnumTeamStatus.MEMBER;
        }

        return EnumTeamStatus.NONE;
    }

    public EnumTeamStatus getSpecialStatus(UUID player)
    {
        EnumTeamStatus status = specialStatus.get(player);
        return (status == null) ? EnumTeamStatus.NONE : status;
    }

    public void setSpecialStatus(UUID player, EnumTeamStatus status)
    {
        if(status.isSpecialStatus())
        {
            if(status == EnumTeamStatus.NONE)
            {
                if(specialStatus != null)
                {
                    specialStatus.remove(player);

                    if(specialStatus.isEmpty())
                    {
                        specialStatus = null;
                    }
                }
            }
            else
            {
                if(specialStatus == null)
                {
                    specialStatus = new HashMap<>();
                }

                specialStatus.put(player, status);
            }
        }
    }

    public boolean isMember(ForgePlayer player)
    {
        if(owner.equals(player))
        {
            return true;
        }
        else
        {
            return player.getTeam().equals(this);
        }
    }

    public Collection<ForgePlayer> getMembers(boolean excludeOwner)
    {
        Collection<ForgePlayer> c = new HashSet<>();

        for(ForgePlayer p : owner.getWorld().playerMap.values())
        {
            if((!excludeOwner || !p.equalsPlayer(owner)) && isMember(p))
            {
                c.add(p);
            }
        }

        return c;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
    }

    public NBTTagCompound serializeNBTForNet()
    {
        NBTTagCompound tag = new NBTTagCompound();
        return tag;
    }

    public void deserializeNBTFromNet(NBTTagCompound nbt)
    {
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
}