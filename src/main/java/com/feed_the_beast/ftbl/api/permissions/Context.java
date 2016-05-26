package com.feed_the_beast.ftbl.api.permissions;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 26.05.2016.
 */
@ParametersAreNonnullByDefault
public class Context
{
    /**
     * Not recommended, only used when there really is no context available
     */
    public static final Context EMPTY = new Context();

    private IBlockAccess blockAccess;
    private Entity entity;
    private BlockPos blockPos;
    private Map<String, Object> custom;

    public Context()
    {
    }

    public Context(IBlockAccess w)
    {
        blockAccess = w;
    }

    public Context(Entity e)
    {
        blockAccess = e.worldObj;
        entity = e;
    }

    public Context(IBlockAccess w, BlockPos pos)
    {
        blockAccess = w;
        blockPos = pos;
    }

    public Context(Entity e, BlockPos pos)
    {
        blockAccess = e.worldObj;
        entity = e;
        blockPos = pos;
    }

    // IBlockAccess //

    public IBlockAccess getBlockAccess()
    {
        return blockAccess;
    }

    public Context setBlockAccess(IBlockAccess w)
    {
        blockAccess = w;
        return this;
    }

    public boolean hasBlockAccess()
    {
        return blockAccess != null;
    }

    // Entity //

    public Entity getEntity()
    {
        return entity;
    }

    public Context setEntity(Entity e)
    {
        blockAccess = e.worldObj;
        entity = e;
        return this;
    }

    public boolean hasEntity()
    {
        return entity != null;
    }

    // BlockPos //

    public BlockPos getBlockPos()
    {
        return blockPos;
    }

    public Context setBlockPos(BlockPos pos)
    {
        blockPos = pos;
        return this;
    }

    public boolean hasBlockPos()
    {
        return blockPos != null;
    }

    // Custom objects //

    public Object getCustomObject(String id)
    {
        return (custom == null || id.isEmpty()) ? null : custom.get(id);
    }

    public boolean hasCustomObject(String id)
    {
        return custom != null && !id.isEmpty() && custom.containsKey(id);
    }

    public Context setCustomObject(String id, Object obj)
    {
        if(custom == null)
        {
            custom = new HashMap<>();
        }

        custom.put(id, obj);
        return this;
    }
}