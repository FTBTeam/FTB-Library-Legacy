package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.util.misc.Node;

/**
 * @author LatvianModder
 */
public final class RankConfigValueInfo implements Comparable<RankConfigValueInfo>
{
	public final Node node;
	public final ConfigValue defaultValue;
	public final ConfigValue defaultOPValue;

	public RankConfigValueInfo(Node s, ConfigValue def, ConfigValue defOP)
	{
		node = s;
		defaultValue = def.copy();
		defaultOPValue = def.copy();
		defaultOPValue.fromJson(defOP.getSerializableElement());
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
		return o == this || o instanceof ConfigValueInfo && node.equals(((ConfigValueInfo) o).id);
	}

	@Override
	public int compareTo(RankConfigValueInfo o)
	{
		return node.compareTo(o.node);
	}
}