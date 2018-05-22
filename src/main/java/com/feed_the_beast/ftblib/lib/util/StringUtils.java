package com.feed_the_beast.ftblib.lib.util;

import com.feed_the_beast.ftblib.lib.io.Bits;
import com.feed_the_beast.ftblib.lib.math.Ticks;
import com.feed_the_beast.ftblib.lib.util.misc.NameMap;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StringUtils
{
	public static final int DAY24 = 24 * 60 * 60;

	public static final String ALLOWED_TEXT_CHARS = " .-_!@#$%^&*()+=\\/,<>?\'\"[]{}|;:`~";
	public static final char FORMATTING_CHAR = '\u00a7';
	public static final String FORMATTING = "" + FORMATTING_CHAR;
	public static final String[] EMPTY_ARRAY = { };

	public static final int FLAG_ID_ALLOW_EMPTY = 1;
	public static final int FLAG_ID_FIX = 2;
	public static final int FLAG_ID_ONLY_LOWERCASE = 4;
	public static final int FLAG_ID_ONLY_UNDERLINE = 8;
	public static final int FLAG_ID_ONLY_UNDERLINE_OR_PERIOD = FLAG_ID_ONLY_UNDERLINE | 16;
	public static final int FLAG_ID_DEFAULTS = FLAG_ID_FIX | FLAG_ID_ONLY_LOWERCASE | FLAG_ID_ONLY_UNDERLINE;

	public static final Comparator<Object> IGNORE_CASE_COMPARATOR = (o1, o2) -> String.valueOf(o1).compareToIgnoreCase(String.valueOf(o2));
	public static final Comparator<Object> ID_COMPARATOR = (o1, o2) -> getId(o1, FLAG_ID_FIX).compareToIgnoreCase(getId(o2, FLAG_ID_FIX));

	public static final Map<String, String> TEMP_MAP = new HashMap<>();
	public static final DecimalFormat DOUBLE_FORMATTER_00 = new DecimalFormat("#0.00");
	public static final DecimalFormat DOUBLE_FORMATTER_0 = new DecimalFormat("#0.0");
	public final static int[] INT_SIZE_TABLE = {9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE};

	public static final NameMap.ObjectProperties<TextFormatting> TEXT_FORMATTING_OBJECT_PROPERTIES = new NameMap.ObjectProperties<TextFormatting>()
	{
		@Override
		public String getName(TextFormatting value)
		{
			return value.getFriendlyName();
		}

		@Override
		public ITextComponent getDisplayName(@Nullable ICommandSender sender, TextFormatting value)
		{
			return StringUtils.color(new TextComponentString(getName(value)), value);
		}
	};

	public static final NameMap<TextFormatting> TEXT_FORMATTING_NAME_MAP = NameMap.create(TextFormatting.RESET, TEXT_FORMATTING_OBJECT_PROPERTIES, TextFormatting.values());
	public static final NameMap<TextFormatting> TEXT_FORMATTING_COLORS_NAME_MAP = NameMap.create(TextFormatting.WHITE, TEXT_FORMATTING_OBJECT_PROPERTIES,
			TextFormatting.BLACK,
			TextFormatting.DARK_BLUE,
			TextFormatting.DARK_GREEN,
			TextFormatting.DARK_AQUA,
			TextFormatting.DARK_RED,
			TextFormatting.DARK_PURPLE,
			TextFormatting.GOLD,
			TextFormatting.GRAY,
			TextFormatting.DARK_GRAY,
			TextFormatting.BLUE,
			TextFormatting.GREEN,
			TextFormatting.AQUA,
			TextFormatting.RED,
			TextFormatting.LIGHT_PURPLE,
			TextFormatting.YELLOW,
			TextFormatting.WHITE
	);

	public static String emptyIfNull(@Nullable Object o)
	{
		return o == null ? "" : o.toString();
	}

	public static String getRawId(Object o)
	{
		if (o instanceof IStringSerializable)
		{
			return ((IStringSerializable) o).getName();
		}
		else if (o instanceof Enum)
		{
			return ((Enum) o).name();
		}

		return String.valueOf(o);
	}

	public static String getId(Object o, int flags)
	{
		String id = getRawId(o);

		if (flags == 0)
		{
			return id;
		}

		boolean fix = Bits.getFlag(flags, FLAG_ID_FIX);

		if (!fix && id.isEmpty() && !Bits.getFlag(flags, FLAG_ID_ALLOW_EMPTY))
		{
			throw new NullPointerException("ID can't be empty!");
		}

		if (Bits.getFlag(flags, FLAG_ID_ONLY_LOWERCASE))
		{
			if (fix)
			{
				id = id.toLowerCase();
			}
			else if (!id.equals(id.toLowerCase()))
			{
				throw new IllegalArgumentException("ID can't contain uppercase characters!");
			}
		}

		if (Bits.getFlag(flags, FLAG_ID_ONLY_UNDERLINE))
		{
			if (fix)
			{
				id = id.toLowerCase();
			}
			else if (!id.equals(id.toLowerCase()))
			{
				throw new IllegalArgumentException("ID can't contain uppercase characters!");
			}
		}

		if (Bits.getFlag(flags, FLAG_ID_ONLY_UNDERLINE))
		{
			boolean allowPeriod = Bits.getFlag(flags, FLAG_ID_ONLY_UNDERLINE_OR_PERIOD);

			char[] chars = id.toCharArray();

			for (int i = 0; i < chars.length; i++)
			{
				if (!(chars[i] == '.' && allowPeriod || isTextChar(chars[i], true)))
				{
					if (fix)
					{
						chars[i] = '_';
					}
					else
					{
						throw new IllegalArgumentException("ID contains invalid character: '" + chars[i] + "'!");
					}
				}
			}

			id = new String(chars);
		}

		return id;
	}

	public static String[] shiftArray(@Nullable String[] s)
	{
		if (s == null || s.length <= 1)
		{
			return EMPTY_ARRAY;
		}

		String[] s1 = new String[s.length - 1];
		System.arraycopy(s, 1, s1, 0, s1.length);
		return s1;
	}

	public static boolean isASCIIChar(char c)
	{
		return c > 0 && c < 256;
	}

	public static boolean isTextChar(char c, boolean onlyAZ09)
	{
		return isASCIIChar(c) && (c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || !onlyAZ09 && (ALLOWED_TEXT_CHARS.indexOf(c) != -1));
	}

	public static void replace(List<String> txt, String s, String s1)
	{
		if (!txt.isEmpty())
		{
			String s2;
			for (int i = 0; i < txt.size(); i++)
			{
				s2 = txt.get(i);
				if (s2 != null && s2.length() > 0)
				{
					s2 = s2.replace(s, s1);
					txt.set(i, s2);
				}
			}
		}
	}

	public static String replace(String s, char c, char with)
	{
		if (s.isEmpty())
		{
			return s;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++)
		{
			char c1 = s.charAt(i);
			sb.append((c1 == c) ? with : c1);
		}

		return sb.toString();
	}

	@Nullable
	public static String joinSpaceUntilEnd(int startIndex, CharSequence[] o)
	{
		if (startIndex < 0 || o.length <= startIndex)
		{
			return null;
		}

		StringBuilder sb = new StringBuilder();

		for (int i = startIndex; i < o.length; i++)
		{
			sb.append(o[i]);
			if (i != o.length - 1)
			{
				sb.append(' ');
			}
		}

		return sb.toString();
	}

	public static String firstUppercase(String s)
	{
		if (s.length() == 0)
		{
			return s;
		}
		char c = Character.toUpperCase(s.charAt(0));
		if (s.length() == 1)
		{
			return Character.toString(c);
		}
		return String.valueOf(c) + s.substring(1);
	}

	public static String fillString(CharSequence s, char fill, int length)
	{
		int sl = s.length();

		char[] c = new char[Math.max(sl, length)];

		for (int i = 0; i < c.length; i++)
		{
			if (i >= sl)
			{
				c[i] = fill;
			}
			else
			{
				c[i] = s.charAt(i);
			}
		}

		return new String(c);
	}

	public static String removeAllWhitespace(String s)
	{
		return s.isEmpty() ? s : s.replaceAll("\\s+", "");
	}

	public static String trimAllWhitespace(String s)
	{
		return s.isEmpty() ? s : s.replace("^\\s*(.*?)\\s*$", "$1");
	}

	public static String formatDouble0(double value)
	{
		long lvalue = (long) value;
		return lvalue == value ? Long.toString(lvalue) : DOUBLE_FORMATTER_0.format(value);
	}

	public static String formatDouble00(double value)
	{
		long lvalue = (long) value;
		return lvalue == value ? Long.toString(lvalue) : DOUBLE_FORMATTER_00.format(value);
	}

	public static String formatDouble(double value, boolean fancy)
	{
		if (Double.isNaN(value))
		{
			return "NaN";
		}
		else if (value == Double.POSITIVE_INFINITY)
		{
			return "+Inf";
		}
		else if (value == Double.NEGATIVE_INFINITY)
		{
			return "-Inf";
		}
		else if (!fancy)
		{
			return formatDouble00(value);
		}
		else if (value == 0D)
		{
			return "0";
		}
		else if (value >= 100000000000D)
		{
			return value / 1000000000D + "B";
		}
		else if (value >= 10000000000D)
		{
			return formatDouble0(value / 1000000000D) + "B";
		}
		else if (value >= 1000000000D)
		{
			return formatDouble00(value / 1000000000D) + "B";
		}
		else if (value >= 100000000D)
		{
			return value / 1000000D + "M";
		}
		else if (value >= 10000000D)
		{
			return formatDouble0(value / 1000000D) + "M";
		}
		else if (value >= 1000000D)
		{
			return formatDouble00(value / 1000000D) + "M";
		}
		else if (value >= 100000D)
		{
			return value / 1000D + "K";
		}
		else if (value >= 10000D)
		{
			return formatDouble0(value / 1000D) + "K";
		}
		else if (value >= 1000D)
		{
			return formatDouble00(value / 1000D) + "K";
		}

		return formatDouble00(value);
	}

	public static String formatDouble(double value)
	{
		return formatDouble(value, false);
	}

	public static String getTimeStringTicks(long ticks)
	{
		return getTimeString(Ticks.ts(ticks) * 1000L);
	}

	public static String getTimeString(long millis)
	{
		boolean neg = false;
		if (millis <= 0L)
		{
			neg = true;
			millis = -millis;
		}

		if (millis < 1000L)
		{
			return (neg ? "-" : "") + millis + "ms";
		}

		if (neg)
		{
			millis += 1000L;
		}

		long secs = millis / 1000L;
		StringBuilder sb = new StringBuilder();

		if (neg)
		{
			sb.append('-');
		}

		long h = (secs / 3600L) % 24;
		long m = (secs / 60L) % 60L;
		long s = secs % 60L;

		if (secs >= DAY24)
		{
			sb.append(secs / DAY24);
			sb.append('d');
			sb.append(' ');
		}

		if (h > 0 || secs >= DAY24)
		{
			if (h < 10)
			{
				sb.append('0');
			}
			sb.append(h);
			//sb.append("h ");
			sb.append(':');
		}

		if (m < 10)
		{
			sb.append('0');
		}
		sb.append(m);
		//sb.append("m ");
		sb.append(':');
		if (s < 10)
		{
			sb.append('0');
		}
		sb.append(s);
		//sb.append('s');

		return sb.toString();
	}

	public static String fromUUID(@Nullable UUID id)
	{
		if (id != null)
		{
			long msb = id.getMostSignificantBits();
			long lsb = id.getLeastSignificantBits();
			StringBuilder sb = new StringBuilder(32);
			digitsUUID(sb, msb >> 32, 8);
			digitsUUID(sb, msb >> 16, 4);
			digitsUUID(sb, msb, 4);
			digitsUUID(sb, lsb >> 48, 4);
			digitsUUID(sb, lsb, 12);
			return sb.toString();
		}

		return "";
	}

	private static void digitsUUID(StringBuilder sb, long val, int digits)
	{
		long hi = 1L << (digits * 4);
		String s = Long.toHexString(hi | (val & (hi - 1)));
		sb.append(s, 1, s.length());
	}

	@Nullable
	public static UUID fromString(@Nullable String s)
	{
		if (s == null || !(s.length() == 32 || s.length() == 36))
		{
			return null;
		}

		try
		{
			if (s.indexOf('-') != -1)
			{
				return UUID.fromString(s);
			}

			int l = s.length();
			StringBuilder sb = new StringBuilder(36);
			for (int i = 0; i < l; i++)
			{
				sb.append(s.charAt(i));
				if (i == 7 || i == 11 || i == 15 || i == 19)
				{
					sb.append('-');
				}
			}

			return UUID.fromString(sb.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, String> parse(Map<String, String> map, String s)
	{
		if (map == TEMP_MAP)
		{
			map.clear();
		}

		for (String entry : s.split(","))
		{
			String[] val = entry.split("=");

			for (String key : val[0].split("&"))
			{
				map.put(key, val[1]);
			}
		}

		return map;
	}

	@SuppressWarnings("ConstantConditions")
	public static ITextComponent color(ITextComponent component, @Nullable TextFormatting color)
	{
		component.getStyle().setColor(color);
		return component;
	}

	public static ITextComponent bold(ITextComponent component, boolean value)
	{
		component.getStyle().setBold(value);
		return component;
	}

	public static ITextComponent italic(ITextComponent component, boolean value)
	{
		component.getStyle().setItalic(value);
		return component;
	}

	public static ITextComponent underlined(ITextComponent component, boolean value)
	{
		component.getStyle().setUnderlined(value);
		return component;
	}

	public static String fixTabs(String string, int tabSize) //FIXME
	{
		String with;

		if (tabSize == 2)
		{
			with = "  ";
		}
		else if (tabSize == 4)
		{
			with = "    ";
		}
		else
		{
			char c[] = new char[tabSize];
			Arrays.fill(c, ' ');
			with = new String(c);
		}

		return string.replace("\t", with);
	}

	public static int stringSize(int x)
	{
		for (int i = 0; ; i++)
		{
			if (x <= INT_SIZE_TABLE[i])
			{
				return i + 1;
			}
		}
	}

	public static String add0s(int number, int max)
	{
		int size = stringSize(max);
		int nsize = stringSize(number);
		StringBuilder builder = new StringBuilder(size);

		for (int i = 0; i < size - nsize; i++)
		{
			builder.append('0');
		}

		builder.append(number);
		return builder.toString();
	}

	public static String camelCaseToWords(String key)
	{
		StringBuilder builder = new StringBuilder();
		boolean pu = false;

		for (int i = 0; i < key.length(); i++)
		{
			char c = key.charAt(i);
			boolean u = Character.isUpperCase(c);

			if (!pu && u)
			{
				builder.append(' ');
			}

			pu = u;

			if (i == 0)
			{
				c = Character.toUpperCase(c);
			}

			builder.append(c);
		}

		return builder.toString();
	}
}