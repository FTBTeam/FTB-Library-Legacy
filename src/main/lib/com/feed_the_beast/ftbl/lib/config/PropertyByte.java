package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import io.netty.buffer.ByteBuf;

/**
 * @author LatvianModder
 */
public class PropertyByte extends PropertyInt
{
	public static final String ID = "byte";

	private boolean unsigned = false;

	public PropertyByte()
	{
	}

	public PropertyByte(int v)
	{
		super(v);
	}

	public PropertyByte(int v, int min, int max)
	{
		super(v, min, max);
	}

	public PropertyByte(int v, int min, int max, boolean u)
	{
		this(v, min, max);
		unsigned = u;
	}

	public PropertyByte setUnsigned()
	{
		unsigned = true;
		return this;
	}

	@Override
	public void setInt(int v)
	{
		super.setInt(unsigned ? (v & 0xFF) : ((byte) v));
	}

	@Override
	public String getName()
	{
		return ID;
	}

	@Override
	public IConfigValue copy()
	{
		return new PropertyByte(getInt(), getMin(), getMax(), unsigned);
	}

	@Override
	public void writeData(ByteBuf data)
	{
		data.writeBoolean(unsigned);
		data.writeByte(getInt());
		data.writeByte(getMin());
		data.writeByte(getMax());
	}

	@Override
	public void readData(ByteBuf data)
	{
		unsigned = data.readBoolean();
		setInt(unsigned ? data.readUnsignedByte() : data.readByte());
		setMin(unsigned ? data.readUnsignedByte() : data.readByte());
		setMax(unsigned ? data.readUnsignedByte() : data.readByte());
	}
}