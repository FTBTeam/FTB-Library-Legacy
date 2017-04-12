package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 24.09.2016.
 */
public class PropertyTextComponent extends PropertyBase
{
    public static final String ID = "text_component";

    private ITextComponent value;

    public PropertyTextComponent()
    {
    }

    public PropertyTextComponent(@Nullable ITextComponent c)
    {
        value = c;
    }

    @Override
    public String getName()
    {
        return ID;
    }

    @Nullable
    public ITextComponent getText()
    {
        return value;
    }

    public void setText(@Nullable ITextComponent c)
    {
        value = c;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getText();
    }

    @Override
    public String getString()
    {
        ITextComponent c = getText();
        return c == null ? "" : c.getFormattedText();
    }

    @Override
    public String toString()
    {
        return getSerializableElement().toString();
    }

    @Override
    public boolean getBoolean()
    {
        return !getString().isEmpty();
    }

    @Override
    public int getInt()
    {
        return getString().length();
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyTextComponent(getText());
    }

    @Override
    public void fromJson(JsonElement e)
    {
        setText(JsonUtils.deserializeTextComponent(e));
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return JsonUtils.serializeTextComponent(getText());
    }

    @Override
    public void writeData(ByteBuf data)
    {
        NetUtils.writeTextComponent(data, getText());
    }

    @Override
    public void readData(ByteBuf data)
    {
        setText(NetUtils.readTextComponent(data));
    }
}