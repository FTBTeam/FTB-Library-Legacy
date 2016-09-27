package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigValue;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public final class ConfigKey extends SimpleConfigKey
{
    private final IConfigValue defValue;
    private String displayName;
    private byte flags;
    private String info;

    public ConfigKey(String id, IConfigValue def, @Nullable String dn, boolean translate)
    {
        super(id);
        defValue = def;
        displayName = dn;

        if(translate)
        {
            flags |= TRANSLATE_DISPLAY_NAME;
        }
    }

    public ConfigKey(String id, IConfigValue def)
    {
        this(id, def, null, false);
    }

    @Override
    public IConfigValue getDefValue()
    {
        return defValue;
    }

    @Override
    public String getRawDisplayName()
    {
        return displayName == null ? "" : displayName;
    }

    public void setDisplayName(@Nullable String c)
    {
        displayName = c;
    }

    @Override
    public byte getFlags()
    {
        return flags;
    }

    public void setFlags(byte f)
    {
        flags = f;
    }

    public ConfigKey addFlag(int f)
    {
        flags |= f;
        return this;
    }

    @Override
    public String getInfo()
    {
        return info == null ? "" : info;
    }

    public void setInfo(String s)
    {
        info = s;
    }

    public ConfigKey setKeyInfo(String s)
    {
        setInfo(s);
        return this;
    }
}