package com.feed_the_beast.ftbl.api;

import com.google.gson.JsonElement;

import java.util.Collection;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface IPackModes
{
    Collection<IPackMode> getModes();

    IPackMode getRawMode(String s);

    IPackMode getMode(String s);

    IPackMode getDefault();

    JsonElement getCustomData(String s);
}