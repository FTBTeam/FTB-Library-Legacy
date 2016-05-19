package com.feed_the_beast.ftbl.util;

import net.minecraft.util.EnumFacing;

public enum SidedDirection
{
    BOTTOM(EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.DOWN, EnumFacing.DOWN, EnumFacing.DOWN, EnumFacing.DOWN),
    TOP(EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.UP, EnumFacing.UP, EnumFacing.UP, EnumFacing.UP),
    BACK(EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST),
    FRONT(EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST),
    LEFT(EnumFacing.WEST, EnumFacing.EAST, EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH),
    RIGHT(EnumFacing.EAST, EnumFacing.WEST, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.NORTH);

    public static final SidedDirection[] VALUES = new SidedDirection[] {BOTTOM, TOP, BACK, FRONT, LEFT, RIGHT};
    public final EnumFacing[] directions;

    // Static //

    SidedDirection(EnumFacing a, EnumFacing b, EnumFacing c, EnumFacing d, EnumFacing e, EnumFacing f)
    {
        directions = new EnumFacing[] {a, b, c, d, e, f};
    }

    public static SidedDirection getSide(EnumFacing side, EnumFacing rot)
    {
        if(side == null || rot == null)
        {
            return null;
        }
        for(SidedDirection d : VALUES)
        {
            if(d.directions[rot.ordinal()] == side)
            {
                return d;
            }
        }
        return null;
    }

    public static SidedDirection get(EnumFacing side, EnumFacing rot3D, EnumFacing rot2D)
    {
        if(side == rot3D)
        {
            return FRONT;
        }
        if(side == rot3D.getOpposite())
        {
            return BACK;
        }

        if(rot3D == EnumFacing.DOWN)
        {
            if(side != EnumFacing.DOWN && side != EnumFacing.UP)
            {
                if(rot2D == side)
                {
                    return TOP;
                }
                else if(rot2D == side.getOpposite())
                {
                    return BOTTOM;
                }
            }

            return getSide(side, rot2D);
        }
        else if(rot3D == EnumFacing.UP)
        {
            if(side != EnumFacing.DOWN && side != EnumFacing.UP)
            {
                if(rot2D == side)
                {
                    return BOTTOM;
                }
                else if(rot2D == side.getOpposite())
                {
                    return TOP;
                }
            }

            return getSide(side, rot2D);
        }
        else
        {
            if(side == EnumFacing.DOWN)
            {
                return BOTTOM;
            }
            else if(side == EnumFacing.UP)
            {
                return TOP;
            }
            return getSide(side, rot3D);
        }
    }
}