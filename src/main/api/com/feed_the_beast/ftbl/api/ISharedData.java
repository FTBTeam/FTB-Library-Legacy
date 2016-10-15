package com.feed_the_beast.ftbl.api;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IJsonSerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface ISharedData extends INBTSerializable<NBTTagCompound>, IJsonSerializable
{
    Side getSide();

    IPackMode getPackMode();

    UUID getUniverseID();

    boolean hasOptionalServerMod(@Nullable String id);

    boolean isOP(@Nullable GameProfile profile);

    boolean useFTBPrefix();
}