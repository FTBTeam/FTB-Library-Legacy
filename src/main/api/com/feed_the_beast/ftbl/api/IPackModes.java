package com.feed_the_beast.ftbl.api;

import com.google.gson.JsonElement;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author LatvianModder
 */
public interface IPackModes
{
    Collection<IPackMode> getModes();

    @Nullable
    IPackMode getRawMode(String id);

    IPackMode getMode(String id);

    IPackMode getDefault();

    JsonElement getCustomData(String id);
}