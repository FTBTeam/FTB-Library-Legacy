package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.net.MessageLM;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.latmod.lib.annotations.AnnotationHelper;
import com.latmod.lib.annotations.Flags;
import com.latmod.lib.annotations.ID;
import com.latmod.lib.util.LMUtils;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigGroup extends ConfigEntry
{
    public final LinkedHashMap<String, ConfigEntry> entryMap;

    public ConfigGroup()
    {
        entryMap = createEntryMap();
    }

    private static boolean isValidChar(char c)
    {
        switch(c)
        {
            case '_':
                return true;
            case ':':
                return true;
            default:
            {
                return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9');
            }
        }
    }

    protected LinkedHashMap<String, ConfigEntry> createEntryMap()
    {
        return new LinkedHashMap<>();
    }

    @Override
    public ConfigEntryType getConfigType()
    {
        return ConfigEntryType.GROUP;
    }

    public ConfigEntry getEntryFromFullID(String id)
    {
        int idx = id.indexOf('.');

        if(idx == -1)
        {
            return entryMap.get(id);
        }

        ConfigEntry group = entryMap.get(id.substring(0, idx));

        if(group != null && group.getAsGroup() != null)
        {
            return group.getAsGroup().getEntryFromFullID(id.substring(idx + 1));
        }

        return null;
    }

    public ConfigFile asConfigFile()
    {
        return null;
    }

    public ConfigGroup add(String s, ConfigEntry e)
    {
        for(int i = 0; i < s.length(); i++)
        {
            if(!isValidChar(s.charAt(i)))
            {
                throw new IllegalArgumentException("Invalid ID: " + s + " - it can only contain '_', ':', 'a'-'z' and '0'-'9'!");
            }
        }

        if(e != null)
        {
            entryMap.put(s, e);
        }

        return this;
    }

    public ConfigGroup addAll(Class<?> c, Object parent)
    {
        try
        {
            Field f[] = c.getDeclaredFields();

            if(f != null && f.length > 0)
            {
                for(Field aF : f)
                {
                    try
                    {
                        aF.setAccessible(true);
                        if(ConfigEntry.class.isAssignableFrom(aF.getType()))
                        {
                            String id = aF.isAnnotationPresent(ID.class) ? aF.getDeclaredAnnotation(ID.class).value() : aF.getName();

                            ConfigEntry entry = (ConfigEntry) aF.get(parent);
                            if(entry != null && entry != this && !(entry instanceof ConfigFile))
                            {
                                AnnotationHelper.inject(aF, entry);
                                add(id, entry);
                            }
                        }
                    }
                    catch(Exception e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return this;
    }

    @Override
    public ConfigEntry copy()
    {
        ConfigGroup g = new ConfigGroup();
        for(Map.Entry<String, ConfigEntry> entry : entryMap.entrySet())
        {
            g.add(entry.getKey(), entry.getValue().copy());
        }
        return g;
    }

    @Override
    public final void fromJson(@Nonnull JsonElement o0)
    {
        entryMap.clear();

        if(o0.isJsonObject())
        {
            JsonObject o = o0.getAsJsonObject();

            for(Map.Entry<String, JsonElement> e : o.entrySet())
            {
                ConfigEntry entry = new ConfigEntryCustom();

                if(!e.getValue().isJsonNull())
                {
                    entry.fromJson(e.getValue());
                }

                add(e.getKey(), entry);
            }
        }
    }

    @Nonnull
    @Override
    public final JsonElement getSerializableElement()
    {
        JsonObject o = new JsonObject();

        for(Map.Entry<String, ConfigEntry> entry : entryMap.entrySet())
        {
            if((entry.getValue().getFlags() & Flags.EXCLUDED) == 0)
            {
                o.add(entry.getKey(), entry.getValue().getSerializableElement());
            }
        }

        return o;
    }

    @Override
    public String getAsString()
    {
        return getSerializableElement().toString();
    }

    @Override
    public List<String> getAsStringList()
    {
        List<String> list = new ArrayList<>(entryMap.size());
        for(ConfigEntry e : entryMap.values())
        {
            list.add(e.getAsString());
        }

        return list;
    }

    @Override
    public boolean getAsBoolean()
    {
        return !entryMap.isEmpty();
    }

    @Override
    public int getAsInt()
    {
        return entryMap.size();
    }

    @Override
    public void writeData(ByteBuf io, boolean extended)
    {
        super.writeData(io, extended);

        io.writeShort(entryMap.size());

        if(!entryMap.isEmpty())
        {
            for(Map.Entry<String, ConfigEntry> entry : entryMap.entrySet())
            {
                io.writeByte(entry.getValue().getConfigType().ID);
                MessageLM.writeString(io, entry.getKey());
                entry.getValue().writeData(io, extended);
            }
        }
    }

    @Override
    public void readData(ByteBuf io, boolean extended)
    {
        super.readData(io, extended);

        entryMap.clear();

        int s = io.readUnsignedShort();

        for(int i = 0; i < s; i++)
        {
            ConfigEntryType t = ConfigEntryType.getFromID(io.readByte());
            String id = MessageLM.readString(io);
            ConfigEntry entry = t.createNew();
            entry.readData(io, extended);
            entryMap.put(id, entry);
        }
    }

    private Map<String, JsonElement> getFullMapFromObject(Map<String, JsonElement> map, JsonObject obj, String parent)
    {
        for(Map.Entry<String, JsonElement> entry : obj.entrySet())
        {
            JsonElement e = entry.getValue();

            if(e != null && e.isJsonObject())
            {
                getFullMapFromObject(map, e.getAsJsonObject(), entry.getKey());
            }
            else
            {
                map.put((parent == null) ? entry.getKey() : (parent + '.' + entry.getKey()), e);
            }
        }

        return map;
    }

    public int loadFromGroup(JsonObject json)
    {
        int result = 0;

        if(json != null)
        {
            Map<String, JsonElement> map1 = getFullMapFromObject(new HashMap<>(), json, null);

            if(!map1.isEmpty())
            {
                for(Map.Entry<String, JsonElement> entry : map1.entrySet())
                {
                    ConfigEntry e0 = getEntryFromFullID(entry.getKey());

                    if(e0 != null)
                    {
                        try
                        {
                            e0.fromJson(entry.getValue());
                            result++;
                        }
                        catch(Exception ex)
                        {
                            System.err.println(ex);
                            System.err.println("Can't set value " + entry.getValue().getAsString() + " for '" + entry.getKey() + "' (type:" + e0.getConfigType() + ")");
                        }
                    }
                }
            }
        }

        onLoadedFromGroup(json, result);
        return result;
    }

    protected void onLoadedFromGroup(JsonObject json, int loaded)
    {
    }

    public boolean hasKey(Object key)
    {
        return entryMap.containsKey(LMUtils.getID(key));
    }

    public ConfigEntry getEntry(Object key)
    {
        return entryMap.get(LMUtils.getID(key));
    }

    public ConfigGroup getGroup(Object key)
    {
        ConfigEntry e = getEntry(key);
        return (e == null) ? null : e.getAsGroup();
    }

    public List<ConfigGroup> getGroups()
    {
        ArrayList<ConfigGroup> list = new ArrayList<>();
        for(ConfigEntry e : entryMap.values())
        {
            ConfigGroup g = e.getAsGroup();
            if(g != null)
            {
                list.add(g);
            }
        }
        return list;
    }

    @Override
    public ConfigGroup getAsGroup()
    {
        return this;
    }

    public Map<String, ConfigEntry> getFullEntryMap()
    {
        return getFullEntryMap0(new HashMap<>(), null);
    }

    private Map<String, ConfigEntry> getFullEntryMap0(Map<String, ConfigEntry> map, String prevID)
    {
        for(Map.Entry<String, ConfigEntry> e : entryMap.entrySet())
        {
            String id = e.getKey();
            ConfigGroup vg = e.getValue().getAsGroup();

            if(vg != null)
            {
                vg.getFullEntryMap0(map, prevID == null ? id : (prevID + '.' + id));
            }
            else
            {
                if(prevID == null)
                {
                    map.put(id, e.getValue());
                }
                else
                {
                    map.put(prevID + '.' + id, e.getValue());
                }
            }
        }

        return map;
    }
}