package com.feed_the_beast.ftblib.lib.util.text_components;

import com.feed_the_beast.ftblib.lib.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * @author LatvianModder
 */
public class TextComponentParser
{
	public static ITextComponent parse(String text, @Nullable Function<String, ITextComponent> substitutes)
	{
		return new TextComponentParser(text, substitutes).parse();
	}

	private static final Style ERROR_STYLE = new Style();

	static
	{
		ERROR_STYLE.setColor(TextFormatting.RED);
	}

	private String text;
	private Function<String, ITextComponent> substitutes;

	private ITextComponent component;
	private StringBuilder builder;
	private Style style;
	private boolean sub;

	private TextComponentParser(String txt, @Nullable Function<String, ITextComponent> sub)
	{
		text = txt;
		substitutes = sub;
	}

	private ITextComponent parse()
	{
		if (text.isEmpty())
		{
			return new TextComponentString("");
		}

		char[] c = text.toCharArray();
		boolean hasSpecialCodes = false;

		for (char c1 : c)
		{
			if (c1 == '$' || c1 == '&' || c1 == StringUtils.FORMATTING_CHAR)
			{
				hasSpecialCodes = true;
				break;
			}
		}

		if (!hasSpecialCodes)
		{
			return new TextComponentString(text);
		}

		component = new TextComponentString("");
		style = new Style();
		builder = new StringBuilder();
		sub = false;

		for (int i = 0; i < c.length; i++)
		{
			boolean escape = i > 0 && c[i - 1] == '\\';
			boolean end = i == c.length - 1;

			if (sub && (end || !StringUtils.isTextChar(c[i], true)))
			{
				finishPart();
				sub = false;
			}

			if (!escape)
			{
				if (c[i] == '&')
				{
					c[i] = StringUtils.FORMATTING_CHAR;
				}

				if (c[i] == StringUtils.FORMATTING_CHAR)
				{
					finishPart();

					if (end)
					{
						throw new IllegalArgumentException("Invalid formatting! Can't end string with & or " + StringUtils.FORMATTING_CHAR + "!");
					}

					i++;

					TextFormatting formatting = StringUtils.CODE_TO_FORMATTING.get(c[i]);

					if (formatting == null)
					{
						throw new IllegalArgumentException("Illegal formatting! Unknown color code character: " + c[i] + "!");
					}

					switch (formatting)
					{
						case OBFUSCATED:
							style.setObfuscated(!style.getObfuscated());
							break;
						case BOLD:
							style.setBold(!style.getBold());
							break;
						case STRIKETHROUGH:
							style.setStrikethrough(!style.getStrikethrough());
							break;
						case UNDERLINE:
							style.setUnderlined(!style.getUnderlined());
							break;
						case ITALIC:
							style.setItalic(!style.getItalic());
							break;
						case RESET:
							style = new Style();
							break;
						default:
							style.setColor(formatting);
					}

					continue;
				}
				else if (c[i] == '$')
				{
					finishPart();

					if (end)
					{
						throw new IllegalArgumentException("Invalid formatting! Can't end string with $!");
					}

					sub = true;
				}
			}
			else if (c[i] == ' ')
			{
				continue;
			}

			builder.append(c[i]);
		}

		finishPart();
		return component;
	}

	private void finishPart()
	{
		String string = builder.toString();
		builder.setLength(0);

		if (string.isEmpty())
		{
			return;
		}
		else if (string.length() < 2 || string.charAt(0) != '$')
		{
			ITextComponent component1 = new TextComponentString(string);
			component1.setStyle(style.createShallowCopy());
			component.appendSibling(component1);
			return;
		}

		ITextComponent component1 = substitutes.apply(string.substring(1));

		if (component1 != null)
		{
			Style style0 = component1.getStyle().createShallowCopy();
			Style style1 = style.createShallowCopy();
			style1.setHoverEvent(style0.getHoverEvent());
			style1.setClickEvent(style0.getClickEvent());
			style1.setInsertion(style0.getInsertion());
			component1.setStyle(style1);
		}
		else
		{
			component1 = new TextComponentString(string);
			component1.getStyle().setColor(TextFormatting.RED);
		}

		component.appendSibling(component1);
	}
}