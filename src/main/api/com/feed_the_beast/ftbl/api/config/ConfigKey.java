package com.feed_the_beast.ftbl.api.config;

import com.latmod.lib.annotations.IInfoContainer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class ConfigKey implements IConfigKey
{
    private final String ID;
    private final IConfigValue defValue;
    private ITextComponent displayName;
    private byte flags;
    private String[] info;

    public ConfigKey(String id, IConfigValue def, @Nullable ITextComponent dn)
    {
        ID = id;
        defValue = def;
        displayName = dn;
    }

    @Override
    public String getName()
    {
        return ID;
    }

    @Override
    public IConfigValue getDefValue()
    {
        return defValue;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return displayName == null ? new TextComponentString(getName()) : displayName;
    }

    public void setDisplayName(@Nullable ITextComponent c)
    {
        displayName = c;
    }

    @Override
    public final int getFlags()
    {
        return flags;
    }

    @Override
    public final void setFlags(int f)
    {
        flags = (byte) f;
    }

    @Override
    public final String[] getInfo()
    {
        return info == null ? IInfoContainer.NO_INFO : info;
    }

    @Override
    public final void setInfo(String[] s)
    {
        info = s;
    }

    public int hashCode()
    {
        return ID.hashCode();
    }

    public String toString()
    {
        return ID;
    }

    public boolean equals(Object o)
    {
        return o == this || (o != null && o.toString().equals(ID));
    }
}