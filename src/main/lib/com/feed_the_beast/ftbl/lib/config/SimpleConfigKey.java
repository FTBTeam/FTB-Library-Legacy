package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import net.minecraft.util.text.ITextComponent;

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
