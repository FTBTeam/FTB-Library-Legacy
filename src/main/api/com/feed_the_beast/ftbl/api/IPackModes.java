package com.feed_the_beast.ftbl.api;

import com.google.gson.JsonElement;

import java.util.Collection;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface IPackModes
{
    Collection<IPackMode> getModes();

    IPackMode getRawMode(String id);

    IPackMode getMode(String id);

    IPackMode getDefault();

    JsonElement getCustomData(String id);
}