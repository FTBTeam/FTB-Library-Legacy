package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.util.LMNBTUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.latmod.lib.annotations.AnnotationHelper;
import com.latmod.lib.annotations.Flags;
import com.latmod.lib.annotations.ID;
import com.latmod.lib.util.LMUtils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
            char c = s.charAt(i);

            if(!(c == '-' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')))
            {
                throw new IllegalArgumentException("ConfigID can only contain '.' and 'a'-'z'!");
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
            if(!entry.getValue().getFlag(Flags.EXCLUDED))
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
    public void writeToNBT(NBTTagCompound tag, boolean extended)
    {
        super.writeToNBT(tag, extended);

        if(!entryMap.isEmpty())
        {
            NBTTagCompound tag1 = new NBTTagCompound();

            for(Map.Entry<String, ConfigEntry> entry : entryMap.entrySet())
            {
                NBTTagCompound tag2 = new NBTTagCompound();
                entry.getValue().writeToNBT(tag2, extended);
                tag2.setByte("T", entry.getValue().getConfigType().ID);
                tag1.setTag(entry.getKey(), tag2);
            }

            tag.setTag("V", tag1);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag, boolean extended)
    {
        super.readFromNBT(tag, extended);

        entryMap.clear();

        if(tag.hasKey("V"))
        {
            for(Map.Entry<String, NBTBase> entry : LMNBTUtils.entrySet((NBTTagCompound) tag.getTag("V")))
            {
                NBTTagCompound tag2 = (NBTTagCompound) entry.getValue();
                ConfigEntryType t = ConfigEntryType.getFromID(tag2.getByte("T"));

                if(t != null)
                {
                    ConfigEntry e = t.createNew();
                    e.readFromNBT(tag2, extended);
                    entryMap.put(entry.getKey(), e);
                }
            }
        }
    }

    public int loadFromGroup(ConfigGroup l)
    {
        if(l == null || l.entryMap.isEmpty())
        {
            return 0;
        }

        int result = 0;

        for(Map.Entry<String, ConfigEntry> entry : l.entryMap.entrySet())
        {
            ConfigEntry e0 = entryMap.get(entry.getKey());

            if(e0 != null)
            {
                if(e0.getAsGroup() != null)
                {
                    ConfigGroup g1 = new ConfigGroup();
                    g1.fromJson(entry.getValue().getSerializableElement());
                    result += e0.getAsGroup().loadFromGroup(g1);
                }
                else
                {
                    try
                    {
                        e0.fromJson(entry.getValue().getSerializableElement());

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

        if(result > 0)
        {
            onLoadedFromGroup(l);
        }

        return result;
    }

    protected void onLoadedFromGroup(ConfigGroup l)
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

    public Collection<String> getAllKeys()
    {
        Collection<String> c = new HashSet<>();
        getAllKeys0(c, null);
        return c;
    }

    private void getAllKeys0(Collection<String> keys, String prevID)
    {
        for(Map.Entry<String, ConfigEntry> e : entryMap.entrySet())
        {
            String id = e.getKey();
            ConfigGroup vg = e.getValue().getAsGroup();

            if(vg != null)
            {
                vg.getAllKeys0(keys, prevID == null ? id : (prevID + '.' + id));
            }
            else
            {
                if(prevID == null)
                {
                    keys.add(id);
                }
                else
                {
                    keys.add(prevID + '.' + id);
                }
            }
        }
    }

    @Override
    public boolean hasDiff(ConfigEntry entry)
    {
        ConfigGroup group = entry.getAsGroup();

        if(group == null || group.entryMap.size() != entryMap.size() || !group.entryMap.keySet().equals(entryMap.keySet()))
        {
            return true;
        }

        for(String k : entryMap.keySet())
        {
            if(entryMap.get(k).hasDiff(group.entryMap.get(k)))
            {
                return true;
            }
        }

        return true;
    }
}