package com.latmod.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api_impl.ConfigManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.latmod.lib.util.JsonElementIO;
import com.latmod.lib.util.LMJsonUtils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public class ConfigTree implements IConfigTree
{
    public static final String ID = "tree";

    private static final int HAS_DISPLAY_NAME = 1;
    private static final int HAS_INFO = 2;

    private final Map<IConfigKey, IConfigValue> tree = new HashMap<>();

    @Override
    public Map<IConfigKey, IConfigValue> getTree()
    {
        return tree;
    }

    @Override
    public void writeData(DataOutput data, boolean extended) throws IOException
    {
        if(!extended)
        {
            //FIXME
            JsonElementIO.write(data, getSerializableElement());
            return;
        }

        Map<IConfigKey, IConfigValue> map = getTree();

        data.writeShort(map.size());

        for(Map.Entry<IConfigKey, IConfigValue> entry : map.entrySet())
        {
            IConfigKey key = entry.getKey();
            IConfigValue value = key.getDefValue();

            data.writeUTF(key.getName());
            data.writeByte(key.getFlags());

            data.writeShort(ConfigManager.INSTANCE.configValues().getIntIDs().getIDFromKey(value.getID()));
            value.writeData(data, true);

            byte extraFlags = 0;

            String rawDN = key.getRawDisplayName();

            if(!rawDN.isEmpty())
            {
                extraFlags |= HAS_DISPLAY_NAME;
            }

            String info = key.getInfo();

            if(info.isEmpty())
            {
                extraFlags |= HAS_INFO;
            }

            data.writeByte(extraFlags);

            if(!rawDN.isEmpty())
            {
                data.writeUTF(rawDN);
            }

            if(info.isEmpty())
            {
                data.writeUTF(info);
            }

            value = entry.getValue();

            data.writeShort(ConfigManager.INSTANCE.configValues().getIntIDs().getIDFromKey(value.getID()));
            value.writeData(data, true);
        }
    }

    @Override
    public void readData(DataInput data, boolean extended) throws IOException
    {
        if(!extended)
        {
            //FIXME
            fromJson(JsonElementIO.read(data));
            return;
        }

        Map<IConfigKey, IConfigValue> map = getTree();
        map.clear();

        int s = data.readUnsignedShort();

        while(--s >= 0)
        {
            String id = data.readUTF();
            byte flags = data.readByte();

            IConfigValue value = ConfigManager.INSTANCE.configValues().getFromIntID(data.readUnsignedShort()).createConfigValue();
            value.readData(data, true);

            ConfigKey key = new ConfigKey(id, value);
            key.setFlags(flags);

            byte extraFlags = data.readByte();

            if((extraFlags & HAS_DISPLAY_NAME) != 0)
            {
                key.setDisplayName(data.readUTF());
            }

            if((extraFlags & HAS_INFO) != 0)
            {
                key.setInfo(data.readUTF());
            }

            value = ConfigManager.INSTANCE.configValues().getFromIntID(data.readUnsignedShort()).createConfigValue();
            value.readData(data, true);
            map.put(key, value);
        }
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