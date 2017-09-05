package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.util.StringUtils;
import net.minecraft.util.IStringSerializable;

/**
 * @author LatvianModder
 */
public class FinalIDObject implements IStringSerializable
{
	private final String id;

	public FinalIDObject(String _id, int flags)
	{
		id = StringUtils.getId(_id, flags);
	}

	public FinalIDObject(String id)
	{
		this(id, StringUtils.FLAG_ID_DEFAULTS);
	}

	@Override
	public final String getName()
	{
		return id;
	}

	@Override
	public String toString()
	{
		return id;
	}

	@Override
	public final int hashCode()
	{
		return id.hashCode();
	}

	@Override
	public final boolean equals(Object o)
	{
		return o == this || o == id || (o != null && id.equals(StringUtils.getId(o, StringUtils.FLAG_ID_FIX)));
	}
}