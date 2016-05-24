package com.feed_the_beast.ftbl.api.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class TileInfoDataAccessor
{
    public static final TileInfoDataAccessor inst = new TileInfoDataAccessor();

    public EntityPlayer player;
    public World world;
    public RayTraceResult hit;
    public IBlockState state;
}