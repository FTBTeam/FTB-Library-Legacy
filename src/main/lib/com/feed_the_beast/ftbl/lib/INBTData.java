package com.feed_the_beast.ftbl.lib;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by LatvianModder on 10.10.2016.
 */
public interface INBTData extends IStringSerializable, INBTSerializable<NBTTagCompound>
{
}