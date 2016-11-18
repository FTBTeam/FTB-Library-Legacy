package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public class SimpleConfigKey implements IConfigKey
{
    private String ID;

    public SimpleConfigKey(String id)
    {
        ID = id;
    }

    @Override
    public final String getID()
    {
        return ID;
    }

    @Override
    public String toString()
    {
        return ID;
    }

    @Override
    public final int hashCode()
    {
        return ID.hashCode();
    }

    @Override
    public final boolean equals(Object o)
    {
        return o == this || o == ID || (o != null && ID.equals(LMStringUtils.getID(o)));
    }

    @Override
    public byte getFlags()
    {
        return 0;
    }

    @Override
    public String getInfo()
    {
        return "";
    }

    @Override
    public IConfigValue getDefValue()
    {
        return PropertyNull.INSTANCE;
    }

    @Override
    @Nullable
    public ITextComponent getRawDisplayName()
    {
        return null;
    }
}
