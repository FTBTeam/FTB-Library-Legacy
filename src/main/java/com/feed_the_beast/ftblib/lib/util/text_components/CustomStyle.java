package com.feed_the_beast.ftblib.lib.util.text_components;

import com.feed_the_beast.ftblib.lib.util.StringJoiner;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public class CustomStyle extends Style
{
	public static final CustomStyle CUSTOM_ROOT = new CustomStyle()
	{
		@Override
		@Nullable
		public TextFormatting getColor()
		{
			return null;
		}

		@Override
		@Nullable
		public TextFormatting getBackground()
		{
			return null;
		}

		@Override
		public boolean getBold()
		{
			return false;
		}

		@Override
		public boolean getItalic()
		{
			return false;
		}

		@Override
		public boolean getStrikethrough()
		{
			return false;
		}

		@Override
		public boolean getUnderlined()
		{
			return false;
		}

		@Override
		public boolean getObfuscated()
		{
			return false;
		}

		@Override
		public boolean getMonospaced()
		{
			return false;
		}

		@Override
		@Nullable
		public ClickEvent getClickEvent()
		{
			return null;
		}

		@Override
		@Nullable
		public HoverEvent getHoverEvent()
		{
			return null;
		}

		@Override
		@Nullable
		public String getInsertion()
		{
			return null;
		}

		@Override
		public Style setColor(TextFormatting color)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public CustomStyle setBackground(TextFormatting color)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public Style setBold(Boolean boldIn)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public Style setItalic(Boolean italic)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public Style setStrikethrough(Boolean strikethrough)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public Style setUnderlined(Boolean underlined)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public Style setObfuscated(Boolean obfuscated)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public CustomStyle setMonospaced(@Nullable Boolean _monospaced)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public Style setClickEvent(ClickEvent event)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public Style setHoverEvent(HoverEvent event)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public Style setParentStyle(Style parent)
		{
			throw new UnsupportedOperationException();
		}

		public String toString()
		{
			return "Style.ROOT";
		}

		@Override
		public CustomStyle createShallowCopy()
		{
			return this;
		}

		@Override
		public CustomStyle createDeepCopy()
		{
			return this;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public String getFormattingCode()
		{
			return "";
		}
	};

	public TextFormatting background;
	public Boolean monospaced;

	public CustomStyle()
	{
	}

	public CustomStyle(Style style)
	{
		shallowCopyFrom(style);
	}

	@Override
	public Style getParent()
	{
		return parentStyle == null ? CUSTOM_ROOT : parentStyle;
	}

	@Override
	public boolean isEmpty()
	{
		return monospaced == null && super.isEmpty();
	}

	@Nullable
	public TextFormatting getBackground()
	{
		Style style = getParent();
		return background == null ? (style instanceof CustomStyle ? ((CustomStyle) style).getBackground() : null) : background;
	}

	public CustomStyle setBackground(@Nullable TextFormatting _color)
	{
		background = _color;
		return this;
	}

	public boolean getMonospaced()
	{
		Style style = getParent();
		return monospaced == null ? (style instanceof CustomStyle && ((CustomStyle) style).getMonospaced()) : monospaced;
	}

	public CustomStyle setMonospaced(@Nullable Boolean _monospaced)
	{
		monospaced = _monospaced;
		return this;
	}

	public String toString()
	{
		return "Style{" + StringJoiner.properties().joinObjects(
				"hasParent", parentStyle != null,
				"color", color,
				"background", background,
				"bold", bold,
				"italic", italic,
				"underlined", underlined,
				"obfuscated", obfuscated,
				"monospaced", monospaced,
				"clickEvent", clickEvent,
				"hoverEvent", hoverEvent,
				"insertion", insertion
		) + "}";
	}

	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		else if (o instanceof CustomStyle)
		{
			CustomStyle c = (CustomStyle) o;
			return monospaced == c.monospaced && background == c.background && super.equals(c);
		}

		return false;
	}

	public int hashCode()
	{
		int i = Objects.hashCode(color);
		i = 31 * i + Objects.hashCode(background);
		i = 31 * i + Objects.hashCode(bold);
		i = 31 * i + Objects.hashCode(italic);
		i = 31 * i + Objects.hashCode(underlined);
		i = 31 * i + Objects.hashCode(strikethrough);
		i = 31 * i + Objects.hashCode(obfuscated);
		i = 31 * i + Objects.hashCode(monospaced);
		i = 31 * i + Objects.hashCode(clickEvent);
		i = 31 * i + Objects.hashCode(hoverEvent);
		i = 31 * i + Objects.hashCode(insertion);
		return i;
	}

	public void shallowCopyFrom(Style style)
	{
		bold = style.bold;
		italic = style.italic;
		strikethrough = style.strikethrough;
		underlined = style.underlined;
		obfuscated = style.obfuscated;
		monospaced = style instanceof CustomStyle ? ((CustomStyle) style).monospaced : null;
		color = style.color;
		background = style instanceof CustomStyle ? ((CustomStyle) style).background : null;
		clickEvent = style.clickEvent;
		hoverEvent = style.hoverEvent;
		parentStyle = style.parentStyle;
		insertion = style.insertion;
	}

	public void deepCopyFrom(Style style)
	{
		setBold(style.getBold());
		setItalic(style.getItalic());
		setStrikethrough(style.getStrikethrough());
		setUnderlined(style.getUnderlined());
		setObfuscated(style.getObfuscated());
		setMonospaced(style instanceof CustomStyle ? ((CustomStyle) style).getMonospaced() : null);
		setColor(style.getColor());
		setBackground(style instanceof CustomStyle ? ((CustomStyle) style).getBackground() : null);
		setClickEvent(style.getClickEvent());
		setHoverEvent(style.getHoverEvent());
		setInsertion(style.getInsertion());
	}

	@Override
	public CustomStyle createShallowCopy()
	{
		CustomStyle style = new CustomStyle();
		style.shallowCopyFrom(this);
		return style;
	}

	@Override
	public CustomStyle createDeepCopy()
	{
		CustomStyle style = new CustomStyle();
		style.deepCopyFrom(this);
		return style;
	}

	@Override
	public String getFormattingCode()
	{
		if (isEmpty())
		{
			return parentStyle != null ? parentStyle.getFormattingCode() : "";
		}
		else
		{
			StringBuilder sb = new StringBuilder();

			if (getColor() != null)
			{
				sb.append(getColor());
			}

			if (getBold())
			{
				sb.append(TextFormatting.BOLD);
			}

			if (getItalic())
			{
				sb.append(TextFormatting.ITALIC);
			}

			if (getUnderlined())
			{
				sb.append(TextFormatting.UNDERLINE);
			}

			if (getObfuscated())
			{
				sb.append(TextFormatting.OBFUSCATED);
			}

			if (getMonospaced())
			{
				sb.append(StringUtils.FORMATTING + '`');
			}

			if (getStrikethrough())
			{
				sb.append(TextFormatting.STRIKETHROUGH);
			}

			return sb.toString();
		}
	}
}