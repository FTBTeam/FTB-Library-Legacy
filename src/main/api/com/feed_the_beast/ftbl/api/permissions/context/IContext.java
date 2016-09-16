package com.feed_the_beast.ftbl.api.permissions.context;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Use {@link BlockPosContext} or {@link PlayerContext} when possible
 */
public interface IContext
{
    /**
     * World from where permission is requested. Can be null
     */
    @Nullable
    World getWorld();

    /**
     * @return Player requesting permission. Can be null
     */
    @Nullable
    EntityPlayer getPlayer();

    /**
     * @param key Context key
     * @return Context object
     */
    @Nullable
    <T> T get(ContextKey<T> key);

    /**
     * @param key Context key
     * @return true if context contains this key
     */
    boolean has(ContextKey<?> key);
}