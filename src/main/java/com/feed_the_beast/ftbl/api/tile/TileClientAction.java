package com.feed_the_beast.ftbl.api.tile;

import com.feed_the_beast.ftbl.api.ResourceLocationObject;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 14.05.2016.
 */
public abstract class TileClientAction extends ResourceLocationObject
{
    public TileClientAction(ResourceLocation id)
    {
        super(id);
    }

    public abstract void onAction(TileEntity te, NBTTagCompound data, EntityPlayerMP player);
}