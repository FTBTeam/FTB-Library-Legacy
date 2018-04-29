package com.feed_the_beast.ftblib.lib.util.misc;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author LatvianModder
 */
public final class Node implements Comparable<Node>
{
	public static final Node ALL = new Node(new String[] {"*"});
	public static final Node COMMAND = new Node(new String[] {"command"});

	private static final LoadingCache<String, Node> CACHE = CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.MINUTES).build(new CacheLoader<String, Node>()
	{
		@Override
		public Node load(String string)
		{
			while (string.indexOf('.') == 0)
			{
				string = string.substring(1);
			}

			while (string.lastIndexOf('.') == string.length() - 1)
			{
				string = string.substring(0, string.length() - 1);
			}

			if (string.isEmpty() || string.charAt(0) == '*')
			{
				return ALL;
			}

			ArrayList<String> list = new ArrayList<>();

			for (String s : string.toLowerCase().split("\\."))
			{
				s = s.trim();

				if (!s.isEmpty())
				{
					list.add(s);
				}
			}

			while (!list.isEmpty() && list.get(list.size() - 1).equals("*"))
			{
				list.remove(list.size() - 1);
			}

			return new Node(list.toArray(new String[0]));
		}
	});

	public static Node get(String string)
	{
		try
		{
			return CACHE.get(string);
		}
		catch (Exception ex)
		{
			throw new IllegalArgumentException("Failed to create node from '" + string + "'!");
		}
	}

	private final String[] parts;
	private final String string;

	private Node(String[] p)
	{
		parts = p;
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < parts.length; i++)
		{
			if (i > 0)
			{
				builder.append('.');
			}

			builder.append(parts[i]);
		}

		string = builder.toString();
	}

	public String toString()
	{
		return string;
	}

	public Node append(String name)
	{
		return get(string + '.' + name);
	}

	public Node append(Node node)
	{
		if (node == ALL)
		{
			return this;
		}

		String[] nparts = new String[parts.length + node.parts.length];
		System.arraycopy(parts, 0, nparts, 0, parts.length);
		System.arraycopy(node.parts, 0, nparts, parts.length, node.parts.length);
		return new Node(nparts);
	}

	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		else if (o instanceof Node)
		{
			Node n = (Node) o;

			if (parts.length != n.parts.length)
			{
				return false;
			}

			for (int i = 0; i < parts.length; i++)
			{
				if (!parts[i].equals(n.parts[i]))
				{
					return false;
				}
			}

			return true;
		}

		return false;
	}

	public int hashCode()
	{
		return string.hashCode();
	}

	@Override
	public int compareTo(Node o)
	{
		return string.compareTo(o.string);
	}

	public int getPartCount()
	{
		return parts.length;
	}

	public String getPart(int index)
	{
		return parts[index];
	}

	public String[] createPartArray()
	{
		String[] array = new String[parts.length];
		System.arraycopy(parts, 0, array, 0, array.length);
		return array;
	}

	//a.b     - this
	//a.b.c.d - node
	//        = true

	//a.b.c.d - this
	//a.b     - node
	//        = false

	public boolean matches(Node node)
	{
		if (this == ALL)
		{
			return true;
		}
		else if (node.parts.length < parts.length)
		{
			return false;
		}

		for (int i = 0; i < parts.length; i++)
		{
			if (!parts[i].equals(node.parts[i]))
			{
				return false;
			}
		}

		return true;
	}
}