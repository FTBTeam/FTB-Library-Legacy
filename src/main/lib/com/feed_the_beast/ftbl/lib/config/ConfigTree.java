package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api_impl.FTBLibRegistries;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public class ConfigTree implements IConfigTree
{
    private static final int HAS_DISPLAY_NAME = 1;
    private static final int HAS_INFO = 2;

    private final Map<IConfigKey, IConfigValue> tree;

    public ConfigTree(boolean linked)
    {
        tree = linked ? new LinkedHashMap<>() : new HashMap<>();
    }

    public ConfigTree()
    {
        this(false);
    }

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
            data.writeShort(FTBLibRegistries.INSTANCE.CONFIG_VALUES.getIDs().getIDFromKey(defValue.getID()));
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

            data.writeShort(FTBLibRegistries.INSTANCE.CONFIG_VALUES.getIDs().getIDFromKey(value.getID()));
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

            IConfigValue value = FTBLibRegistries.INSTANCE.CONFIG_VALUES.getFromIntID(data.readShort()).createConfigValue();
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

            value = FTBLibRegistries.INSTANCE.CONFIG_VALUES.getFromIntID(data.readShort()).createConfigValue();
            value.readData(data, true);
            map.put(key, value);
        }
    }

    @Override
    public void fromJson(JsonElement json)
    {
        LMJsonUtils.fromJsonTree(json.getAsJsonObject()).entrySet().forEach(entry -> get(new SimpleConfigKey(entry.getKey())).fromJson(entry.getValue()));
    }

    @Override
    public JsonElement getSerializableElement()
    {
        List<Map.Entry<String, JsonElement>> list = new ArrayList<>();
        getTree().forEach((key, value) -> list.add(new AbstractMap.SimpleEntry<>(key.getName(), value.getSerializableElement())));
        return LMJsonUtils.toJsonTree(list);
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