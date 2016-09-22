package com.latmod.lib.config;

import com.feed_the_beast.ftbl.api.config.ConfigValueProvider;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.gui.GuiSelectField;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyString extends PropertyBase
{
    public static final String ID = "string";

    @ConfigValueProvider(ID)
    public static final IConfigValueProvider PROVIDER = () -> new PropertyString("");

    private String value;

    public PropertyString(String v)
    {
        value = v;
    }

    @Override
    public String getID()
    {
        return ID;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getString();
    }

    public void set(String v)
    {
        value = v;
    }

    @Override
    public void writeData(DataOutput data, boolean extended) throws IOException
    {
        data.writeUTF(getString());
    }

    @Override
    public void readData(DataInput data, boolean extended) throws IOException
    {
        set(data.readUTF());
    }

    @Override
    public String getString()
    {
        return value;
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
        GuiSelectField.display(null, GuiSelectField.FieldType.STRING, getString(), (id, val) ->
        {
            set(val.toString());
            gui.onChanged(key, getSerializableElement());
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
        set(((NBTTagString) nbt).getString());
    }

    @Override
    public void fromJson(JsonElement json)
    {
        set(json.getAsString());
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getString());
    }
}