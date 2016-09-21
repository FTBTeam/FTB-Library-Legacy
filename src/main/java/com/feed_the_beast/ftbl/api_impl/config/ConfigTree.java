package com.feed_the_beast.ftbl.api_impl.config;

import com.feed_the_beast.ftbl.api.config.ConfigValueProvider;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.latmod.lib.util.JsonElementIO;
import com.latmod.lib.util.LMJsonUtils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public class ConfigTree extends PropertyBase implements IConfigTree
{
    public static final String ID = "tree";

    @ConfigValueProvider(ID)
    public static final IConfigValueProvider PROVIDER = ConfigTree::new;

    private static final int HAS_DISPLAY_NAME = 1;
    private static final int HAS_INFO = 2;

    private final Map<IConfigKey, IConfigValue> tree = new HashMap<>();

    @Override
    public Map<IConfigKey, IConfigValue> getTree()
    {
        return tree;
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
        return getTree();
    }

    @Override
    public void writeData(DataOutput data, boolean extended) throws IOException
    {
        JsonElementIO.write(data, getSerializableElement());
    }

    @Override
    public void readData(DataInput data, boolean extended) throws IOException
    {
        fromJson(JsonElementIO.read(data));
    }

    @Override
    public String getString()
    {
        return getTree().toString();
    }

    @Override
    public boolean getBoolean()
    {
        return !getTree().isEmpty();
    }

    @Override
    public int getInt()
    {
        return getTree().size();
    }

    @Override
    public IConfigValue copy()
    {
        ConfigTree tree1 = new ConfigTree();

        for(Map.Entry<IConfigKey, IConfigValue> entry : getTree().entrySet())
        {
            tree1.getTree().put(entry.getKey(), entry.getValue().copy());
        }

        return tree1;
    }

    @Override
    public boolean equalsValue(IConfigValue value)
    {
        if(value instanceof IConfigTree)
        {
            return ((IConfigTree) value).getTree().equals(getTree());
        }

        return false;
    }

    @Override
    public void fromJson(JsonElement json)
    {
        JsonObject o = json.getAsJsonObject();

        for(Map.Entry<String, JsonElement> entry : o.entrySet())
        {
            get(new SimpleConfigKey(entry.getKey())).fromJson(entry.getValue());
        }
    }

    @Override
    public JsonElement getSerializableElement()
    {
        JsonObject o = new JsonObject();

        for(Map.Entry<IConfigKey, IConfigValue> entry : getTree().entrySet())
        {
            o.add(entry.getKey().getName(), entry.getValue().getSerializableElement());
        }

        return LMJsonUtils.fromJsonTree(o);
    }

    @Override
    public NBTBase serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        if(!isEmpty())
        {
            for(Map.Entry<IConfigKey, IConfigValue> entry : getTree().entrySet())
            {
                if(entry.getValue() != null)
                {
                    nbt.setTag(entry.getKey().getName(), entry.getValue().serializeNBT());
                }
            }
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        NBTTagCompound configTag = (NBTTagCompound) nbt;
        for(Map.Entry<IConfigKey, IConfigValue> entry : getTree().entrySet())
        {
            NBTBase base = configTag.getTag(entry.getKey().getName());

            if(base != null)
            {
                entry.getValue().deserializeNBT(base);
            }
        }
    }
}