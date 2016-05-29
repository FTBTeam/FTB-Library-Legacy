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

/**
 * Created by LatvianModder on 26.05.2016.
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class ForgeTeam implements INBTSerializable<NBTTagCompound>, IFlagContainer
{
    public static final byte ALLOW_INVITE_MEMBERS = 0;

    public final int teamID;
    public final ForgePlayer owner;
    private Map<Integer, EnumTeamStatus> specialStatus;
    private EnumDyeColor color;
    private String title;
    private String desc;
    private byte flags;

    public ForgeTeam(int id, ForgePlayer o)
    {
        teamID = id;
        owner = o;
    }

    @Override
    public final boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }
        else if(o == this)
        {
            return true;
        }
        else if(o instanceof ForgeTeam)
        {
            return o.hashCode() == teamID;
        }

        return owner.equals(o);
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

    public EnumTeamStatus getStatus(ForgePlayer player)
    {
        if(player == null)
        {
            return EnumTeamStatus.NONE;
        }
        else if(player.equalsPlayer(owner))
        {
            return EnumTeamStatus.OWNER;
        }
        else if(player.hasTeam())
        {
            ForgeTeam team = player.getTeam();

            if(team.equals(this))
            {
                return EnumTeamStatus.MEMBER;
            }

            if(specialStatus != null && specialStatus.containsKey(team.teamID))
            {
                return specialStatus.get(team.teamID);
            }
        }

        return EnumTeamStatus.NONE;
    }

    public void setSpecialStatus(int team, EnumTeamStatus status)
    {
        if(status.isSpecialStatus())
        {
            if(status == EnumTeamStatus.NONE)
            {
                if(specialStatus != null)
                {
                    specialStatus.remove(team);

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

                specialStatus.put(team, status);
            }
        }
    }

    public Collection<ForgePlayer> getMembers(boolean excludeOwner)
    {
        Collection<ForgePlayer> c = new HashSet<>();

        for(ForgePlayer p : owner.getWorld().playerMap.values())
        {
            if(p.equalsPlayer(owner))
            {
                if(!excludeOwner)
                {
                    c.add(p);
                }
            }
            else if(getStatus(p).isMember())
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
        tag.setInteger("ID", teamID);
        tag.setString("O", owner.getStringUUID());

        tag.setByte("F", flags);

        return tag;
    }

    public void deserializeNBTFromNet(NBTTagCompound nbt)
    {
        flags = nbt.getByte("F");
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