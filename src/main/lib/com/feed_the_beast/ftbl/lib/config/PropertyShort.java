package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import io.netty.buffer.ByteBuf;

/**
 * @author LatvianModder
 */
public class PropertyShort extends PropertyInt
{
    public static final String ID = "short";

    private boolean unsigned = false;

    public PropertyShort()
    {
    }

    public PropertyShort(int v)
    {
        super(v);
    }

    public PropertyShort(int v, int min, int max)
    {
        super(v, min, max);
    }

    public PropertyShort(int v, int min, int max, boolean u)
    {
        this(v, min, max);
        unsigned = u;
    }

    public PropertyShort setUnsigned()
    {
        unsigned = true;
        return this;
    }

    @Override
    public void setInt(int v)
    {
        super.setInt(unsigned ? (v & 0xFFFF) : ((short) v));
    }

    @Override
    public String getName()
    {
        return ID;
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyShort(getInt(), getMin(), getMax(), unsigned);
    }

    @Override
    public void writeData(ByteBuf data)
    {
        data.writeBoolean(unsigned);
        data.writeShort(getInt());
        data.writeShort(getMin());
        data.writeShort(getMax());
    }

    @Override
    public void readData(ByteBuf data)
    {
        unsigned = data.readBoolean();
        setInt(unsigned ? data.readUnsignedShort() : data.readShort());
        setMin(unsigned ? data.readUnsignedShort() : data.readShort());
        setMax(unsigned ? data.readUnsignedShort() : data.readShort());
    }
}