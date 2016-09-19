package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.latmod.lib.io.IExtendedIOObject;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.IJsonSerializable;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 11.09.2016.
 */
public interface IConfigValue extends IExtendedIOObject, INBTSerializable<NBTBase>, IJsonSerializable
{
    String getID();

    @Nullable
    Object getValue();

    String getString();

    boolean getBoolean();

    int getInt();

    double getDouble();

    IConfigValue copy();

    boolean equalsValue(IConfigValue value);

    int getColor();

    @Nullable
    String getMinValueString();

    @Nullable
    String getMaxValueString();

    @Nullable
    List<String> getVariants();

    void onClicked(IGuiEditConfig gui, IConfigKey key, IMouseButton button);
}