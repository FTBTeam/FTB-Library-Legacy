package com.feed_the_beast.ftbl.api.permissions.context;

import com.google.common.base.Preconditions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;

public class AreaContext extends PlayerContext
{
    private final AxisAlignedBB area;

    public AreaContext(EntityPlayer ep, AxisAlignedBB aabb)
    {
        super(ep);
        area = Preconditions.checkNotNull(aabb, "AxisAlignedBB can't be null in AreaContext!");
    }

    @Override
    @Nullable
    public <T> T get(ContextKey<T> key)
    {
        return key.equals(ContextKeys.AREA) ? (T) area : super.get(key);
    }

    @Override
    protected boolean covers(ContextKey<?> key)
    {
        return key.equals(ContextKeys.AREA);
    }
}