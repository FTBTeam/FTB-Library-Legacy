package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.EnumNameMap;
import com.feed_the_beast.ftbl.lib.math.MathHelperLM;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public abstract class PropertyEnumAbstract<E extends Enum<E>> extends PropertyBase
{
    public static final String ID = "enum";

    @Override
    public String getID()
    {
        return ID;
    }

    public abstract EnumNameMap<E> getNameMap();

    @Nullable
    public abstract E get();

    public abstract void set(@Nullable E e);

    @Override
    public Object getValue()
    {
        return get();
    }

    @Override
    public String getString()
    {
        return EnumNameMap.getEnumName((E) getValue());
    }

    @Override
    public boolean getBoolean()
    {
        return getValue() != null;
    }

    @Override
    public int getInt()
    {
        return getNameMap().getIndex(getValue());
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyEnum<>(getNameMap(), getNameMap().getFromIndex(getInt()));
    }

    @Override
    public int getColor()
    {
        return 0x0094FF;
    }

    @Override
    @Nullable
    public List<String> getVariants()
    {
        return new ArrayList<>(getNameMap().getKeys());
    }

    @Override
    public NBTBase serializeNBT()
    {
        return new NBTTagString(getString());
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        set(getNameMap().get(((NBTTagString) nbt).getString()));
    }

    @Override
    public void fromJson(JsonElement json)
    {
        set(getNameMap().get(json.getAsString()));
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getString());
    }

    @Override
    public void writeToServer(ByteBuf data)
    {
        data.writeShort(getNameMap().size);

        for(String s : getNameMap().getKeys())
        {
            LMNetUtils.writeString(data, s);
        }

        data.writeShort(getNameMap().getIndex(getValue()));
    }

    @Override
    public void readFromServer(ByteBuf data)
    {
        throw new IllegalStateException("Can't read Abstract Enum Property!");
    }

    @Override
    public void onClicked(IGuiEditConfig gui, IConfigKey key, IMouseButton button)
    {
        set(getNameMap().getFromIndex(MathHelperLM.wrap(getInt() + (button.isLeft() ? 1 : -1), getNameMap().size)));
        gui.onChanged(key, getSerializableElement());
    }
}