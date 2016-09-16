package com.feed_the_beast.ftbl.api.config;

import com.latmod.lib.annotations.IInfoContainer;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class ConfigKey extends SimpleConfigKey
{
    private final IConfigValue defValue;
    private ITextComponent displayName;
    private byte flags;
    private String[] info;

    public ConfigKey(String id, IConfigValue def, @Nullable ITextComponent dn)
    {
        super(id);
        defValue = def;
        displayName = dn;
    }

    @Override
    public IConfigValue getDefValue()
    {
        return defValue;
    }

    @Override
    public ITextComponent getRawDisplayName()
    {
        return displayName;
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

    public ConfigKey addFlag(int f)
    {
        setFlags(getFlags() | f);
        return this;
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

    public ConfigKey setKeyInfo(String... s)
    {
        setInfo(s);
        return this;
    }
}