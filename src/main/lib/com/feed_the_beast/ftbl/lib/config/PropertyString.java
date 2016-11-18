package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiSelectors;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyString extends PropertyBase
{
    public static final String ID = "string";

    private String value;

    public PropertyString()
    {
        this("");
    }

    public PropertyString(String v)
    {
        value = v;
    }

    @Override
    public String getID()
    {
        return ID;
    }

    @Override
    public String getString()
    {
        return value;
    }

    public void setString(String v)
    {
        value = v;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getString();
    }

    @Override
    public boolean getBoolean()
    {
        return getString().equals("true");
    }

    @Override
    public int getInt()
    {
        return Integer.parseInt(getString());
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyString(getString());
    }

    @Override
    public int getColor()
    {
        return 0xFFAA49;
    }

    @Override
    public void onClicked(IGuiEditConfig gui, IConfigKey key, IMouseButton button)
    {
        GuiSelectors.selectJson(this, (val, set) ->
        {
            if(set)
            {
                setString(val.getString());
                gui.onChanged(key, getSerializableElement());
            }

            gui.openGui();
        });
    }

    @Override
    public NBTBase serializeNBT()
    {
        return new NBTTagString(getString());
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        setString(((NBTTagString) nbt).getString());
    }

    @Override
    public void fromJson(JsonElement json)
    {
        setString(json.getAsString());
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getString());
    }

    @Override
    public void writeData(ByteBuf data)
    {
        LMNetUtils.writeString(data, getString());
    }

    @Override
    public void readData(ByteBuf data)
    {
        setString(LMNetUtils.readString(data));
    }
}