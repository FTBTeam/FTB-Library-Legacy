package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.util.misc.Node;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public final class RankConfigValueInfo implements Comparable<RankConfigValueInfo>
{
	public final Node node;
	public final ConfigValue defaultValue;
	public final ConfigValue defaultOPValue;

	public RankConfigValueInfo(Node s, ConfigValue def, @Nullable ConfigValue defOP)
	{
		node = s;
		defaultValue = def.copy();
		defaultOPValue = def.copy();

		if (defOP != null)
		{
			defaultOPValue.setValueFromOtherValue(defOP);
		}
	}

	public String toString()
	{
		return node.toString();
	}

	public int hashCode()
	{
		return node.hashCode();
	}

	public boolean equals(Object o)
	{
		return o == this || o instanceof RankConfigValueInfo && node.equals(((RankConfigValueInfo) o).node);
	}

	@Override
	public int compareTo(RankConfigValueInfo o)
	{
		return node.compareTo(o.node);
	}
}