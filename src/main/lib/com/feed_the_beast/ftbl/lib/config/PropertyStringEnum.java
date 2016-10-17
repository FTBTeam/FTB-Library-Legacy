package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.RegistryObject;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyStringEnum extends PropertyBase
{
    @RegistryObject(PropertyEnumAbstract.ID)
    public static final IConfigValueProvider PROVIDER = () -> new PropertyStringEnum(Collections.emptyList(), "");

    private List<String> keys;
    private String value;

    public PropertyStringEnum(Collection<String> k, String v)
    {
        keys = new ArrayList<>(k);
        value = v;
    }

    @Override
    public String getID()
    {
        return PropertyEnumAbstract.ID;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getString();
    }

    public void setString(String v)
    {
        value = v;
    }

    @Override
    public String getString()
    {
        return value;
    }

    @Override
    public boolean getBoolean()
    {
        return !value.equals(EnumNameMap.NULL_VALUE);
    }

    @Override
    public int getInt()
    {
        return keys.indexOf(value);
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyStringEnum(keys, getString());
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
        return Collections.unmodifiableList(keys);
    }

    @Override
    public void onClicked(IGuiEditConfig gui, IConfigKey key, IMouseButton button)
    {
        setString(keys.get(MathHelperLM.wrap(getInt() + (button.isLeft() ? 1 : -1), keys.size())));
        gui.onChanged(key, getSerializableElement());
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
    public void writeToServer(ByteBuf data)
    {
        data.writeShort(keys.size());

        for(String s : keys)
        {
            LMNetUtils.writeString(data, s);
        }

        data.writeShort(getInt());
    }

    @Override
    public void readFromServer(ByteBuf data)
    {
        keys.clear();
        int s = data.readUnsignedShort();

        while(--s >= 0)
        {
            keys.add(LMNetUtils.readString(data));
        }

        setString(keys.get(data.readUnsignedShort()));
    }
}