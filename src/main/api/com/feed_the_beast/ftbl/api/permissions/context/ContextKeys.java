package com.feed_the_beast.ftbl.api.permissions.context;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

/**
 * Some default context keys, for easier compatibility
 */
public class ContextKeys
{
    /**
     * BlockPos for interacting, breaking and other permissions
     */
    public static final ContextKey<BlockPos> POS = ContextKey.create("pos", BlockPos.class);

    /**
     * The entity can be anything that gets interacted with - a sheep when you try to dye it, skeleton that you attack, etc.
     */
    public static final ContextKey<Entity> TARGET = ContextKey.create("target", Entity.class);

    public static final ContextKey<EnumFacing> FACING = ContextKey.create("facing", EnumFacing.class);
    public static final ContextKey<AxisAlignedBB> AREA = ContextKey.create("area", AxisAlignedBB.class);
    public static final ContextKey<IBlockState> BLOCK_STATE = ContextKey.create("blockstate", IBlockState.class);
}