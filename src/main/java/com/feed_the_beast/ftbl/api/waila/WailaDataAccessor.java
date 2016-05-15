package com.feed_the_beast.ftbl.api.waila;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class WailaDataAccessor
{
	public EntityPlayer player;
	public World world;
	public RayTraceResult position;
	public TileEntity tile;
	public Block block;
	public int meta;
	public EnumFacing side;
}