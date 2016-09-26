package com.latmod.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api_impl.ConfigManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.latmod.lib.util.LMJsonUtils;
import com.latmod.lib.util.LMNetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public class ConfigTree implements IConfigTree
{
    private static final int HAS_DISPLAY_NAME = 1;
    private static final int HAS_INFO = 2;

    private final Map<IConfigKey, IConfigValue> tree = new HashMap<>();

    @Override
    public Map<IConfigKey, IConfigValue> getTree()
    {
        return tree;
    }

    @Override
    public IConfigTree copy()
    {
        ConfigTree t = new ConfigTree();

        getTree().forEach((key, value) ->
        {
            t.add(key, value.copy());
        });

        return t;
    }

    @Override
    public void writeData(ByteBuf data, boolean extended)
    {
        Map<IConfigKey, IConfigValue> map = getTree();
        data.writeShort(map.size());

        if(!extended)
        {
            map.forEach((key, value) ->
            {
                LMNetUtils.writeString(data, key.getName());
                value.writeData(data, false);
            });

            return;
        }

        map.forEach((key, value) ->
        {
            LMNetUtils.writeString(data, key.getName());
            data.writeByte(key.getFlags());

            IConfigValue defValue = key.getDefValue();
            data.writeShort(ConfigManager.INSTANCE.CONFIG_VALUES.getIDs().getIDFromKey(defValue.getID()));
            defValue.writeData(data, true);

            byte extraFlags = 0;

            String rawDN = key.getRawDisplayName();

            if(!rawDN.isEmpty())
            {
                extraFlags |= HAS_DISPLAY_NAME;
            }

            String info = key.getInfo();

            if(!info.isEmpty())
            {
                extraFlags |= HAS_INFO;
            }

            data.writeByte(extraFlags);

            if(!rawDN.isEmpty())
            {
                LMNetUtils.writeString(data, rawDN);
            }

            if(!info.isEmpty())
            {
                LMNetUtils.writeString(data, info);
            }

            data.writeShort(ConfigManager.INSTANCE.CONFIG_VALUES.getIDs().getIDFromKey(value.getID()));
            value.writeData(data, true);
        });
    }

    @Override
    public void readData(ByteBuf data, boolean extended)
    {
        Map<IConfigKey, IConfigValue> map = getTree();
        int s = data.readUnsignedShort();

        if(!extended)
        {
            while(--s >= 0)
            {
                String id = LMNetUtils.readString(data);
                map.get(new SimpleConfigKey(id)).readData(data, false);
            }

            return;
        }

        map.clear();

        while(--s >= 0)
        {
            String id = LMNetUtils.readString(data);
            byte flags = data.readByte();

            IConfigValue value = ConfigManager.INSTANCE.CONFIG_VALUES.getFromIntID(data.readShort()).createConfigValue();
            value.readData(data, true);

            ConfigKey key = new ConfigKey(id, value);
            key.setFlags(flags);

            byte extraFlags = data.readByte();

            if((extraFlags & HAS_DISPLAY_NAME) != 0)
            {
                key.setDisplayName(LMNetUtils.readString(data));
            }

            if((extraFlags & HAS_INFO) != 0)
            {
                key.setInfo(LMNetUtils.readString(data));
            }

            value = ConfigManager.INSTANCE.CONFIG_VALUES.getFromIntID(data.readShort()).createConfigValue();
            value.readData(data, true);
            map.put(key, value);
        }
    }

    @Override
    public void fromJson(JsonElement json)
    {
        json.getAsJsonObject().entrySet().forEach(entry -> get(new SimpleConfigKey(entry.getKey())).fromJson(entry.getValue()));
    }

    @Override
    public JsonElement getSerializableElement()
    {
        JsonObject o = new JsonObject();
        getTree().forEach((key, value) -> o.add(key.getName(), value.getSerializableElement()));
        return LMJsonUtils.fromJsonTree(o);
    }

    @Override
    public NBTBase serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        getTree().forEach((key, value) -> nbt.setTag(key.getName(), value.serializeNBT()));
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