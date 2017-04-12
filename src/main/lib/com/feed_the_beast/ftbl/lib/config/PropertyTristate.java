package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyTristate extends PropertyBase
{
    public static final String ID = "tristate";

    public static PropertyTristate create(EnumTristate defValue, Supplier<EnumTristate> getter, Consumer<EnumTristate> setter)
    {
        return new PropertyTristate(defValue)
        {
            @Override
            public EnumTristate get()
            {
                return getter.get();
            }

            @Override
            public void set(EnumTristate v)
            {
                setter.accept(v);
            }
        };
    }

    private EnumTristate value;

    public PropertyTristate()
    {
        value = EnumTristate.DEFAULT;
    }

    public PropertyTristate(EnumTristate v)
    {
        value = v;
    }

    @Override
    public String getName()
    {
        return ID;
    }

    @Override
    public boolean getBoolean()
    {
        return value.isTrue();
    }

    public EnumTristate get()
    {
        return value;
    }

    public void set(EnumTristate v)
    {
        value = v;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return get();
    }

    @Override
    public String getString()
    {
        return get().getName();
    }

    @Override
    public int getInt()
    {
        return get().ordinal();
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyTristate(get());
    }

    @Override
    public boolean equalsValue(IConfigValue value)
    {
        return get() == value.getValue();
    }

    @Override
    public Color4I getColor()
    {
        return get().getColor();
    }

    @Override
    public List<String> getVariants()
    {
        return EnumTristate.NAME_MAP.getKeys();
    }

    @Override
    public void onClicked(IGuiEditConfig gui, IConfigKey key, IMouseButton button)
    {
        set(get().getNext());
        gui.onChanged(key, getSerializableElement());
    }

    @Override
    public void fromJson(JsonElement json)
    {
        if(json.getAsString().equals("toggle"))
        {
            set(get().getOpposite());
        }
        else
        {
            EnumTristate state = EnumTristate.NAME_MAP.get(json.getAsString());
            set(state == null ? EnumTristate.DEFAULT : state);
        }
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(get().getName());
    }

    @Override
    public void writeData(ByteBuf data)
    {
        data.writeByte(get().ordinal());
    }

    @Override
    public void readData(ByteBuf data)
    {
        set(EnumTristate.VALUES[data.readUnsignedByte()]);
    }
}