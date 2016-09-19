package com.feed_the_beast.ftbl.api_impl.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.latmod.lib.FinalIDObject;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public class SimpleConfigKey extends FinalIDObject implements IConfigKey
{
    public SimpleConfigKey(String id)
    {
        super(id);
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
    public String getInfo()
    {
        return "";
    }

    @Override
    public void setInfo(String s)
    {
    }

    @Override
    public IConfigValue getDefValue()
    {
        return PropertyNull.INSTANCE;
    }

    @Nullable
    @Override
    public String getRawDisplayName()
    {
        return null;
    }
}
