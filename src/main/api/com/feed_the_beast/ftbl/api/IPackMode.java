package com.feed_the_beast.ftbl.api;

import net.minecraft.util.IStringSerializable;

import java.io.File;

/**
 * @author LatvianModder
 */
public interface IPackMode extends IStringSerializable
{
    File getFolder();
}