package com.feed_the_beast.ftbl.lib.util;

import com.feed_the_beast.ftbl.lib.CustomStyle;
import com.feed_the_beast.ftbl.lib.io.Bits;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class StringUtils
{
	public static final int DAY24 = 24 * 60 * 60;
	public static final Charset UTF_8 = Charset.forName("UTF-8");

	public static final String STRIP_SEP = ", ";
	public static final String ALLOWED_TEXT_CHARS = " .-_!@#$%^&*()+=\\/,<>?\'\"[]{}|;:`~";
	public static final char FORMATTING_CHAR = '\u00a7';
	public static final String FORMATTING = "\u00a7";

	public static final int FLAG_ID_ALLOW_EMPTY = 1;
	public static final int FLAG_ID_FIX = 2;
	public static final int FLAG_ID_ONLY_LOWERCASE = 4;
	public static final int FLAG_ID_ONLY_UNDERLINE = 8;
	public static final int FLAG_ID_ONLY_UNDERLINE_OR_PERIOD = FLAG_ID_ONLY_UNDERLINE | 16;
	public static final int FLAG_ID_DEFAULTS = FLAG_ID_FIX | FLAG_ID_ONLY_LOWERCASE | FLAG_ID_ONLY_UNDERLINE;

	public static final Comparator<Object> IGNORE_CASE_COMPARATOR = (o1, o2) -> String.valueOf(o1).compareToIgnoreCase(String.valueOf(o2));
	public static final Comparator<Object> ID_COMPARATOR = (o1, o2) -> getId(o1, FLAG_ID_FIX).compareToIgnoreCase(getId(o2, FLAG_ID_FIX));

	public static final Map<String, String> TEMP_MAP = new HashMap<>();
	public static final DecimalFormat SMALL_DOUBLE_FORMATTER = new DecimalFormat("#0.00");

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

	public static String toString(@Nullable Object object, @Nullable Function<Object, String> nameSupplier)
	{
		if (object == null)
		{
			return "null";
		}
		else if (object.getClass().isArray())
		{
			Object[] objects = (Object[]) object;

			if (objects.length == 0)
			{
				return "[]";
			}

			StringBuilder sb = new StringBuilder();
			sb.append('[');

			for (int i = 0; i < objects.length; i++)
			{
				sb.append(toString(objects[i], nameSupplier));

				if (i != objects.length - 1)
				{
					sb.append(',');
					sb.append(' ');
				}
			}

			sb.append(']');
			return sb.toString();
		}
		else if (object instanceof Map)
		{
			Map<?, ?> map = (Map<?, ?>) object;

			if (map.isEmpty())
			{
				return "{}";
			}

			StringBuilder sb = new StringBuilder();
			sb.append('{');

			int s = map.size();
			int i = 0;
			for (Map.Entry<?, ?> e : map.entrySet())
			{
				sb.append(toString(e.getKey(), nameSupplier));
				sb.append(':');
				sb.append(' ');
				sb.append(toString(e.getValue(), nameSupplier));

				i++;
				if (i != s)
				{
					sb.append(',');
					sb.append(' ');
				}
			}

			sb.append(' ');

			sb.append('}');
			return sb.toString();
		}
		else if (object instanceof Collection)
		{
			Collection<?> c = (Collection) object;

			if (c.isEmpty())
			{
				return "[]";
			}

			StringBuilder sb = new StringBuilder();
			sb.append('[');

			int s = c.size();
			int i = 0;

			for (Object o : c)
			{
				sb.append(toString(o, nameSupplier));

				i++;
				if (i != s)
				{
					sb.append(',');
					sb.append(' ');
				}
			}

			sb.append(']');
			return sb.toString();
		}

		String s = nameSupplier == null ? null : nameSupplier.apply(object);
		return s == null ? object.toString() : s;
	}

	public static String[] shiftArray(@Nullable String[] s)
	{
		if (s == null || s.length == 0)
		{
			return new String[0];
		}

		String[] s1 = new String[s.length - 1];
		System.arraycopy(s, 1, s1, 0, s1.length);
		return s1;
	}

	public static String readString(InputStream is) throws Exception
	{
		final char[] buffer = new char[0x10000];
		final StringBuilder out = new StringBuilder();
		try (Reader in = new InputStreamReader(is, UTF_8))
		{
			int read;
			do
			{
				read = in.read(buffer, 0, buffer.length);
				if (read > 0)
				{
					out.append(buffer, 0, read);
				}
			}
			while (read >= 0);
			in.close();
		}
		return out.toString();
	}

	public static List<String> readStringList(Reader r) throws Exception
	{
		List<String> l = new ArrayList<>();
		BufferedReader reader = new BufferedReader(r);
		String s;
		while ((s = reader.readLine()) != null)
		{
			l.add(s);
		}
		reader.close();
		return l;
	}

	public static List<String> readStringList(InputStream is) throws Exception
	{
		return readStringList(new InputStreamReader(is, UTF_8));
	}

	public static List<String> toStringList(String s, String regex)
	{
		List<String> al = new ArrayList<>();
		String[] s1 = s.split(regex);
		if (s1.length > 0)
		{
			for (String aS1 : s1)
			{
				al.add(aS1.trim());
			}
		}
		return al;
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

	public static <E> String[] toStrings(E[] o)
	{
		String[] s = new String[o.length];
		for (int i = 0; i < o.length; i++)
		{
			s[i] = String.valueOf(o[i]);
		}

		return s;
	}

	public static String strip(String... o)
	{
		if (o.length == 0)
		{
			return "";
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < o.length; i++)
		{
			sb.append(o[i]);
			if (i != o.length - 1)
			{
				sb.append(STRIP_SEP);
			}
		}

		return sb.toString();
	}

	public static String strip(Collection<?> c)
	{
		if (c.isEmpty())
		{
			return "";
		}

		StringBuilder sb = new StringBuilder();

		int idx = 0;
		int eidx = c.size() - 1;
		for (Object o : c)
		{
			sb.append(o);
			if (idx != eidx)
			{
				sb.append(STRIP_SEP);
			}
			idx++;
		}

		return sb.toString();
	}

	public static String stripD(double... o)
	{
		if (o.length == 0)
		{
			return "";
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < o.length; i++)
		{
			sb.append(formatDouble(o[i]));
			if (i != o.length - 1)
			{
				sb.append(STRIP_SEP);
			}
		}

		return sb.toString();
	}

	public static String stripDI(double... o)
	{
		if (o.length == 0)
		{
			return "";
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < o.length; i++)
		{
			sb.append((long) o[i]);
			if (i != o.length - 1)
			{
				sb.append(STRIP_SEP);
			}
		}

		return sb.toString();
	}

	public static String stripI(int... o)
	{
		if (o.length == 0)
		{
			return "";
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < o.length; i++)
		{
			sb.append(o[i]);
			if (i != o.length - 1)
			{
				sb.append(STRIP_SEP);
			}
		}

		return sb.toString();
	}

	public static String stripB(boolean... o)
	{
		if (o.length == 0)
		{
			return "";
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < o.length; i++)
		{
			sb.append(o[i] ? '1' : '0');
			if (i != o.length - 1)
			{
				sb.append(STRIP_SEP);
			}
		}

		return sb.toString();
	}

	public static String stripB(byte... o)
	{
		if (o.length == 0)
		{
			return "";
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < o.length; i++)
		{
			sb.append(o[i]);
			if (i != o.length - 1)
			{
				sb.append(STRIP_SEP);
			}
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

	public static String formatDouble(double d)
	{
		if (d == Double.POSITIVE_INFINITY)
		{
			return "+Inf";
		}
		else if (d == Double.NEGATIVE_INFINITY)
		{
			return "-Inf";
		}
		else if (d == Double.NaN)
		{
			return "NaN";
		}
		else if (d == 0D)
		{
			return "0.00";
		}

		return SMALL_DOUBLE_FORMATTER.format(d);
	}

	public static String getTimeStringTicks(long ticks)
	{
		return getTimeString(ticks * 1000L / CommonUtils.TICKS_SECOND);
	}

	public static String getTimeString(long millis)
	{
		return getTimeString(millis, true);
	}

	public static String getTimeString(long millis, boolean days)
	{
		long secs = millis / 1000L;
		StringBuilder sb = new StringBuilder();

		long h = (secs / 3600L) % 24;
		long m = (secs / 60L) % 60L;
		long s = secs % 60L;

		if (days && secs >= DAY24)
		{
			sb.append(secs / DAY24);
			//sb.append("d ");
			sb.append(':');
		}

		if (h < 10)
		{
			sb.append('0');
		}
		sb.append(h);
		//sb.append("h ");
		sb.append(':');
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

	public static byte[] toBytes(String s)
	{
		return s.isEmpty() ? new byte[0] : s.getBytes(UTF_8);
	}

	public static String fromBytes(byte[] b)
	{
		return (b.length == 0) ? "" : new String(b, UTF_8);
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

	public static boolean nodesMatch(String[] node, String[] with)
	{
		if (with.length > node.length)
		{
			return false;
		}

		for (int i = 0; i < with.length; i++)
		{
			if (with[i].equals("*"))
			{
				return true;
			}
			else if (!with[i].equals(node[i]))
			{
				return false;
			}
		}

		return false;
	}

	public static CustomStyle getStyle(ITextComponent component)
	{
		Style s = component.getStyle();

		if (!(s instanceof CustomStyle))
		{
			s = new CustomStyle(s);
			component.setStyle(s);
		}

		return (CustomStyle) s;
	}

	public static ITextComponent color(ITextComponent component, TextFormatting color)
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

	public static ITextComponent monospaced(ITextComponent component, boolean value)
	{
		getStyle(component).setMonospaced(value);
		return component;
	}

	public static ITextComponent background(ITextComponent component, TextFormatting background)
	{
		getStyle(component).setBackground(background);
		return component;
	}

	public static String translate(String key)
	{
		return UtilsCommon.INSTANCE.translate(key);
	}

	public static String translate(String key, Object... objects)
	{
		return UtilsCommon.INSTANCE.translate(key, objects);
	}

	public static boolean canTranslate(String key)
	{
		return UtilsCommon.INSTANCE.canTranslate(key);
	}
}