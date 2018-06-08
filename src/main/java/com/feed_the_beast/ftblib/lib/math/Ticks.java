package com.feed_the_beast.ftblib.lib.math;

/**
 * @author LatvianModder
 */
public class Ticks
{
	public static final long SECOND = 20L;
	public static final long TICK_MS = 1000L / SECOND;
	public static final long MINUTE = SECOND * 60L;
	public static final long HOUR = MINUTE * 60L;
	public static final long DAY = HOUR * 24L;
	public static final long WEEK = DAY * 7L;

	public static long tms(long ticks)
	{
		return ticks * TICK_MS;
	}

	public static long mst(long millis)
	{
		return millis / TICK_MS;
	}

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

	public static long td(long ticks)
	{
		return ticks / DAY;
	}

	public static long dt(long days)
	{
		return days * DAY;
	}

	public static long tw(long ticks)
	{
		return ticks / WEEK;
	}

	public static long wt(long weeks)
	{
		return weeks * WEEK;
	}

	public static String toString(long timer)
	{
		if (timer <= 0L)
		{
			return "0s";
		}

		StringBuilder builder = new StringBuilder();

		if (timer < 20L)
		{
			builder.append(timer);
			builder.append('t');
			return builder.toString();
		}

		long weeks = tw(timer);
		boolean hasWeeks = weeks > 0L;

		if (hasWeeks)
		{
			builder.append(weeks);
			builder.append('w');
		}

		long days = td(timer) % 7L;
		boolean hasDays = hasWeeks || days != 0L;

		if (days != 0L)
		{
			if (hasWeeks)
			{
				builder.append(' ');
			}

			builder.append(days);
			builder.append('d');
		}

		long hours = th(timer) % 24L;
		boolean hasHours = hasDays || hours != 0L;

		if (hours != 0L)
		{
			if (hasDays)
			{
				builder.append(' ');
			}

			builder.append(hours);
			builder.append('h');
		}

		long minutes = tm(timer) % 60L;
		boolean hasMinutes = hasHours || minutes != 0L;

		if (minutes != 0L)
		{
			if (hasHours)
			{
				builder.append(' ');
			}

			builder.append(minutes);
			builder.append('m');
		}

		long seconds = ts(timer) % 60L;
		boolean hasSeconds = hasMinutes || seconds != 0L;

		if (seconds != 0L)
		{
			if (hasMinutes)
			{
				builder.append(' ');
			}

			builder.append(seconds);
			builder.append('s');
		}

		long ticks = timer % 20L;
		boolean hasTicks = ticks != 0L;

		if (ticks != 0L)
		{
			if (hasSeconds)
			{
				builder.append(' ');
			}

			builder.append(ticks);
			builder.append('t');
		}

		return builder.toString();
	}

	public static long fromString(String value) throws NumberFormatException
	{
		if (value.isEmpty() || value.equals("0s"))
		{
			return 0L;
		}

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
						timer += st(Long.parseLong(s.substring(0, s.length() - 1)));
						break;
					case 'm':
					case 'M':
						timer += mt(Long.parseLong(s.substring(0, s.length() - 1)));
						break;
					case 'h':
					case 'H':
						timer += ht(Long.parseLong(s.substring(0, s.length() - 1)));
						break;
					case 'd':
					case 'D':
						timer += dt(Long.parseLong(s.substring(0, s.length() - 1)));
						break;
					case 'w':
					case 'W':
						timer += wt(Long.parseLong(s.substring(0, s.length() - 1)));
						break;
					default:
						timer += Long.parseLong(s);
				}
			}
		}

		return timer;
	}
}