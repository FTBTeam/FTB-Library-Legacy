package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author LatvianModder
 */
public final class PropertyList extends PropertyBase implements Iterable<IConfigValue>
{
    public static final String ID = "list";
    public static final Color4I COLOR = new Color4I(false, 0xFFFFAA49);

    private final List<IConfigValue> list;
    private String valueId;

    public PropertyList(String id)
    {
        list = new ArrayList<>();
        valueId = id;
    }

    public PropertyList(Collection<IConfigValue> v)
    {
        this(PropertyNull.ID);
        addAll(v);
    }

    public PropertyList(IConfigValue v0, IConfigValue... v)
    {
        this(PropertyNull.ID);
        add(v0);
        addAll(v);
    }

    @Override
    public String getName()
    {
        return ID;
    }

    @Override
    public Object getValue()
    {
        return getList();
    }

    public void clear()
    {
        list.clear();
        valueId = PropertyNull.ID;
    }

    private boolean hasValidId()
    {
        return !valueId.equals(PropertyNull.ID);
    }

    public void add(IConfigValue v)
    {
        if(v.isNull())
        {
            return;
        }

        if(valueId.equals(PropertyNull.ID))
        {
            valueId = v.getName();
            list.add(v);
        }
        else if(v.getName().equals(valueId))
        {
            list.add(v);
        }
    }

    public void addAll(Collection<IConfigValue> v)
    {
        for(IConfigValue v1 : v)
        {
            add(v1);
        }
    }

    public void addAll(IConfigValue... v)
    {
        for(IConfigValue v1 : v)
        {
            add(v1);
        }
    }

    public Collection<IConfigValue> getList()
    {
        return list;
    }

    public boolean containsValue(@Nullable Object val)
    {
        if(list.isEmpty())
        {
            return false;
        }

        for(IConfigValue value : list)
        {
            if(value.getValue() == val)
            {
                return true;
            }
        }

        if(val == null)
        {
            return false;
        }

        for(IConfigValue value : list)
        {
            Object o = value.getValue();

            if(o != null && val.equals(o))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void writeData(ByteBuf data)
    {
        ByteBufUtils.writeUTF8String(data, valueId);

        if(valueId.equals(PropertyNull.ID))
        {
            return;
        }

        Collection<IConfigValue> list = getList();
        data.writeShort(list.size());

        for(IConfigValue s : list)
        {
            s.writeData(data);
        }
    }

    @Override
    public void readData(ByteBuf data)
    {
        clear();
        valueId = ByteBufUtils.readUTF8String(data);

        if(valueId.equals(PropertyNull.ID))
        {
            return;
        }

        int s = data.readUnsignedShort();
        IConfigValue blank = FTBLibIntegrationInternal.API.getConfigValueFromID(valueId);

        while(--s >= 0)
        {
            IConfigValue v = blank.copy();
            v.readData(data);
            add(v);
        }
    }

    @Override
    public String getString()
    {
        StringBuilder builder = new StringBuilder("[");

        for(int i = 0; i < list.size(); i++)
        {
            builder.append(list.get(i).getString());

            if(i != list.size() - 1)
            {
                builder.append(',');
                builder.append(' ');
            }
        }

        builder.append(']');
        return builder.toString();
    }

    @Override
    public boolean getBoolean()
    {
        return !list.isEmpty();
    }

    @Override
    public int getInt()
    {
        return list.size();
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyList(list);
    }

    @Override
    public Color4I getColor()
    {
        return COLOR;
    }

    @Override
    public void fromJson(JsonElement json)
    {
        if(!hasValidId())
        {
            return;
        }

        list.clear();
        JsonArray a = json.getAsJsonArray();

        if(a.size() == 0)
        {
            return;
        }

        IConfigValue blank = FTBLibIntegrationInternal.API.getConfigValueFromID(valueId);

        for(JsonElement e : a)
        {
            IConfigValue v = blank.copy();
            v.fromJson(e);
            add(v);
        }
    }

    @Override
    public JsonElement getSerializableElement()
    {
        JsonArray a = new JsonArray();

        if(hasValidId())
        {
            list.forEach(v -> a.add(v.getSerializableElement()));
        }

        return a;
    }

    @Override
    public Iterator<IConfigValue> iterator()
    {
        return list.iterator();
    }
}