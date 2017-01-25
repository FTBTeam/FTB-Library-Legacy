package com.feed_the_beast.ftbl.api;

import net.minecraft.util.IStringSerializable;

import java.io.File;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface IPackMode extends IStringSerializable
{
    File getFolder();
}