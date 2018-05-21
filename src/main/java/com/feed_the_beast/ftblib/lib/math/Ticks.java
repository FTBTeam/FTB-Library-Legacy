package com.feed_the_beast.ftblib.lib.math;

/**
 * @author LatvianModder
 */
public class Ticks
{
	public static final long SECOND = 20L;
	public static final long MINUTE = SECOND * 60L;
	public static final long HOUR = MINUTE * 60L;

	public static long ts(long ticks)
	{
		return ticks / SECOND;
	}

	public static long st(long seconds)
	{
		return seconds * SECOND;
	}

	public static long tm(long ticks)
	{
		return ticks / MINUTE;
	}

	public static long mt(long mintes)
	{
		return mintes * MINUTE;
	}

	public static long th(long ticks)
	{
		return ticks / HOUR;
	}

	public static long ht(long hours)
	{
		return hours * HOUR;
	}

	public static String toString(long timer)
	{
		StringBuilder builder = new StringBuilder();

		if (timer < 20L)
		{
			builder.append(timer);
			builder.append('t');
			return builder.toString();
		}

		boolean hasHours = false;
		boolean hasMinutes = false;

		if (timer / Ticks.HOUR > 0L)
		{
			hasHours = true;

			builder.append(timer / HOUR);
			builder.append('h');
		}

		if ((timer / MINUTE) % 60L != 0L)
		{
			hasMinutes = true;

			if (hasHours)
			{
				builder.append(' ');
			}

			builder.append((timer / MINUTE) % 60L);
			builder.append('m');
		}

		if ((timer / SECOND) % 60L != 0L)
		{
			if (hasHours || hasMinutes)
			{
				builder.append(' ');
			}

			builder.append((timer / SECOND) % 60L);
			builder.append('s');
		}

		if (timer % 20L != 0L)
		{
			builder.append(' ');
			builder.append(timer % 20L);
			builder.append('t');
		}

		return builder.toString();
	}

	public static long fromString(String value) throws NumberFormatException
	{
		long timer = 0L;

		for (String s : value.split(" "))
		{
			if (!s.isEmpty())
			{
				switch (s.charAt(s.length() - 1))
				{
					case 't':
					case 'T':
						timer += Long.parseLong(s.substring(0, s.length() - 1));
						break;
					case 's':
					case 'S':
						timer += Long.parseLong(s.substring(0, s.length() - 1)) * SECOND;
						break;
					case 'm':
					case 'M':
						timer += Long.parseLong(s.substring(0, s.length() - 1)) * MINUTE;
						break;
					case 'h':
					case 'H':
						timer += Long.parseLong(s.substring(0, s.length() - 1)) * HOUR;
						break;
					default:
						timer += Long.parseLong(s);
				}
			}
		}

		return timer;
	}
}