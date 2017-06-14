package com.feed_the_beast.ftbl.lib;

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
		public boolean getCode()
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
		public CustomStyle setCode(Boolean obfuscated)
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

	public Boolean code;

	public CustomStyle()
	{
	}

	@Override
	public Style getParent()
	{
		return parentStyle == null ? CUSTOM_ROOT : parentStyle;
	}

	@Override
	public boolean isEmpty()
	{
		return code == null && super.isEmpty();
	}

	public boolean getCode()
	{
		return code == null ? false : code;
	}

	public CustomStyle setCode(@Nullable Boolean _code)
	{
		code = _code;
		return this;
	}

	public String toString()
	{
		return "Style{hasParent=" + (parentStyle != null) + ", color=" + color + ", bold=" + bold + ", italic=" + italic + ", underlined=" + underlined + ", obfuscated=" + obfuscated + ", code=" + code + ", clickEvent=" + getClickEvent() + ", hoverEvent=" + getHoverEvent() + ", insertion=" + getInsertion() + '}';
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
			return c.code == code && super.equals(c);
		}

		return false;
	}

	public int hashCode()
	{
		int i = Objects.hashCode(color);
		i = 31 * i + Objects.hashCode(bold);
		i = 31 * i + Objects.hashCode(italic);
		i = 31 * i + Objects.hashCode(underlined);
		i = 31 * i + Objects.hashCode(strikethrough);
		i = 31 * i + Objects.hashCode(obfuscated);
		i = 31 * i + Objects.hashCode(code);
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
		code = style instanceof CustomStyle ? ((CustomStyle) style).code : null;
		color = style.color;
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
		setCode(style instanceof CustomStyle ? ((CustomStyle) style).getCode() : null);
		setColor(style.getColor());
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
}