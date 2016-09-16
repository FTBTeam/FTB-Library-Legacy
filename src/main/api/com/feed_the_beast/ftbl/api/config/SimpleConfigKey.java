package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.config.impl.PropertyNull;
import com.latmod.lib.annotations.IInfoContainer;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public class SimpleConfigKey implements IConfigKey
{
    private final String ID;

    public SimpleConfigKey(String id)
    {
        ID = id;
    }

    @Override
    public int getFlags()
    {
        return 0;
    }

    @Override
    public void setFlags(int flags)
    {
    }

    @Override
    public String[] getInfo()
    {
        return IInfoContainer.NO_INFO;
    }

    @Override
    public void setInfo(String[] s)
    {
    }

    @Override
    public IConfigValue getDefValue()
    {
        return PropertyNull.INSTANCE;
    }

    @Nullable
    @Override
    public ITextComponent getRawDisplayName()
    {
        return null;
    }

    @Override
    public String getName()
    {
        return ID;
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
