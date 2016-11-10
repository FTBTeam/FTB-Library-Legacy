package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
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
    public void writeToServer(ByteBuf data)
    {
        Map<IConfigKey, IConfigValue> map = getTree();
        data.writeShort(map.size());

        map.forEach((key, value) ->
        {
            LMNetUtils.writeString(data, key.getName());
            data.writeByte(key.getFlags());

            IConfigValue defValue = key.getDefValue();
            data.writeShort(FTBLibIntegrationInternal.API.getClientData().getConfigIDs().getIDFromKey(defValue.getID()));
            defValue.writeToServer(data);

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

            if(LMUtils.DEV_ENV)
            {
                LMUtils.DEV_LOGGER.info(key + " -> " + value.getID());
            }

            data.writeShort(FTBLibIntegrationInternal.API.getClientData().getConfigIDs().getIDFromKey(value.getID()));
            value.writeToServer(data);
        });
    }

    @Override
    public void readFromServer(ByteBuf data)
    {
        Map<IConfigKey, IConfigValue> map = getTree();
        int s = data.readUnsignedShort();
        map.clear();

        while(--s >= 0)
        {
            String id = LMNetUtils.readString(data);
            byte flags = data.readByte();

            String sid = FTBLibIntegrationInternal.API.getClientData().getConfigIDs().getKeyFromID(data.readShort());
            IConfigValue value = FTBLibIntegrationInternal.API.getConfigValueProviders().get(sid).createConfigValue();
            value.readFromServer(data);

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

            sid = FTBLibIntegrationInternal.API.getClientData().getConfigIDs().getKeyFromID(data.readShort());

            if(LMUtils.DEV_ENV)
            {
                LMUtils.DEV_LOGGER.info(key + " -> " + sid);
            }

            value = FTBLibIntegrationInternal
                    .API
                    .getConfigValueProviders()
                    .get(sid)
                    .createConfigValue();
            value.readFromServer(data);
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