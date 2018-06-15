package com.feed_the_beast.ftblib.lib.util.misc;

import com.feed_the_beast.ftblib.lib.util.StringJoiner;

import java.util.regex.Pattern;

/**
 * @author LatvianModder
 */
public final class Node implements Comparable<Node>
{
	private static final StringJoiner NODE_JOINER = StringJoiner.with('.');
	private static final Pattern SPLIT_PATTERN = Pattern.compile("\\.");

	public static final Node ALL = new Node(new String[] {"*"});
	public static final Node COMMAND = new Node(new String[] {"command"});

	public static Node get(String string)
	{
		if (string.isEmpty() || string.charAt(0) == '*')
		{
			return ALL;
		}

		String[] split = SPLIT_PATTERN.split(string);

		if (split.length == 1 && split[0].equals(split[0].trim().toLowerCase()))
		{
			return new Node(split);
		}

		String[] result = new String[split.length];
		int size = 0;

		for (String s : split)
		{
			s = s.trim();

			if (!s.isEmpty())
			{
				result[size] = s.toLowerCase();
				size++;
			}
		}

		while (size > 0 && result[size - 1].charAt(0) == '*')
		{
			size--;
		}

		if (size == 0)
		{
			return ALL;
		}

		if (size != result.length)
		{
			String[] result1 = new String[size];
			System.arraycopy(result, 0, result1, 0, size);
			result = result1;
		}

		return new Node(result);
	}

	private final String[] parts;
	private final String string;

	private Node(String[] p)
	{
		parts = p;
		string = NODE_JOINER.joinStrings(parts);
	}

	public String toString()
	{
		return string;
	}

	public Node append(String name)
	{
		return append(get(name));
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

	public Node removeLastPart()
	{
		if (this == ALL || parts.length == 1)
		{
			return ALL;
		}

		String[] nparts = new String[parts.length - 1];
		System.arraycopy(parts, 0, nparts, 0, nparts.length);
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