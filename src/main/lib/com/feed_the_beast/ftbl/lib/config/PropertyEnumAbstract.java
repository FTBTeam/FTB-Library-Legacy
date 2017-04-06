package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.EnumNameMap;
import com.feed_the_beast.ftbl.lib.ImmutableColor4I;
import com.feed_the_beast.ftbl.lib.math.MathHelperLM;
import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public abstract class PropertyEnumAbstract<E extends Enum<E>> extends PropertyBase
{
    public static final String ID = "enum";
    public static final Color4I COLOR = new ImmutableColor4I(0xFF0094FF);

    @Override
    public String getName()
    {
        return ID;
    }

    public abstract EnumNameMap<E> getNameMap();

    @Nullable
    public abstract E get();

    public abstract void set(@Nullable E e);

    public E getNonnull()
    {
        E e = get();
        Preconditions.checkNotNull(e);
        return e;
    }

    @Override
    public Object getValue()
    {
        return get();
    }

    @Override
    public String getString()
    {
        return EnumNameMap.getName(getValue());
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
    public Color4I getColor()
    {
        return COLOR;
    }

    @Override
    public List<String> getVariants()
    {
        return getNameMap().getKeys();
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
    public void writeData(ByteBuf data)
    {
        data.writeShort(getNameMap().size);

        for(String s : getNameMap().getKeys())
        {
            ByteBufUtils.writeUTF8String(data, s);
        }

        data.writeShort(getNameMap().getIndex(getValue()));
    }

    @Override
    public void readData(ByteBuf data)
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