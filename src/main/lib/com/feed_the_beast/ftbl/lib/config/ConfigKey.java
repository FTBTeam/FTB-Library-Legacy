package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigValue;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class ConfigKey extends SimpleConfigKey
{
    private final IConfigValue defValue;
    private int flags;
    private String displayNameLangKey, infoLangKey = "", group = "";

    public ConfigKey(String id, IConfigValue def, String dn)
    {
        super(id);
        defValue = def;
        displayNameLangKey = dn;
    }

    public ConfigKey(String id, IConfigValue def, String group, String prefix)
    {
        this(group.isEmpty() ? id : (group + "." + id), def);
        setGroup(group);
        setNameLangKey(prefix + "." + getName() + ".name");
        setInfoLangKey(prefix + "." + getName() + ".info");
    }

    public ConfigKey(String id, IConfigValue def)
    {
        this(id, def, "");
    }

    @Override
    public ConfigKey setNameLangKey(String key)
    {
        displayNameLangKey = key;
        return this;
    }

    @Override
    public ConfigKey setInfoLangKey(String key)
    {
        infoLangKey = key;
        return this;
    }

    @Override
    public ConfigKey setGroup(String g)
    {
        group = g;
        return this;
    }

    @Override
    public IConfigValue getDefValue()
    {
        return defValue;
    }

    @Override
    public int getFlags()
    {
        return flags;
    }

    public ConfigKey setFlags(int f)
    {
        flags = f;
        return this;
    }

    @Override
    public ConfigKey addFlags(int f)
    {
        flags |= f;
        return this;
    }

    @Override
    public String getNameLangKey()
    {
        return displayNameLangKey;
    }

    @Override
    public String getInfoLangKey()
    {
        return infoLangKey;
    }

    @Override
    public String getGroup()
    {
        return group;
    }
}