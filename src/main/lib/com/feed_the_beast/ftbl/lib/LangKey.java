package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.FTBLibConfig;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.google.common.base.Preconditions;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public final class LangKey implements IStringSerializable
{
	private final String key;
	private final Class[] arguments;
	private Style defaultStyle;

	public static LangKey of(String key, Class... args)
	{
		return new LangKey(key, args);
	}

	private LangKey(String s, Class[] a)
	{
		key = s;
		arguments = a;
		defaultStyle = null;
	}

	private static boolean canAssign(@Nullable Object o, @Nullable Class c)
	{
		if (o == null && c == null)
		{
			return true;
		}
		else if (o == null || c == null)
		{
			return false;
		}

		Class c1 = o.getClass();

		if (c1 == c)
		{
			return true;
		}

		if (c == String.class)
		{
			return o instanceof ITextComponent;
		}
		else if (c == Integer.class)
		{
			return c1 == Short.class || c1 == Byte.class;
		}

		return c.isAssignableFrom(c1);
	}

	private void checkArguments(Object[] o)
	{
		Preconditions.checkArgument(arguments.length == o.length);

		for (int i = 0; i < arguments.length; i++)
		{
			Preconditions.checkArgument(canAssign(o[i], arguments[i]));
		}
	}

	@Override
	public String getName()
	{
		return key;
	}

	public LangKey setDefaultStyle(@Nullable Style style)
	{
		defaultStyle = style;
		return this;
	}

	public String translate()
	{
		checkArguments(CommonUtils.NO_OBJECTS);
		return StringUtils.translate(key);
	}

	public String translate(Object... o)
	{
		checkArguments(o);
		return StringUtils.translate(key, o);
	}

	public ITextComponent textComponent()
	{
		checkArguments(CommonUtils.NO_OBJECTS);
		ITextComponent component = new TextComponentTranslation(key, CommonUtils.NO_OBJECTS);

		if (FTBLibConfig.general.clientless_mode)
		{
			component = new TextComponentString(component.getFormattedText());
		}

		if (defaultStyle != null)
		{
			component.setStyle(defaultStyle);
		}

		return component;
	}

	public ITextComponent textComponent(Object... o)
	{
		checkArguments(o);
		ITextComponent component = new TextComponentTranslation(key, o);

		if (FTBLibConfig.general.clientless_mode)
		{
			component = new TextComponentString(component.getFormattedText());
		}

		if (defaultStyle != null)
		{
			component.setStyle(defaultStyle);
		}

		return component;
	}

	public void sendMessage(ICommandSender sender, Object... o)
	{
		checkArguments(o);
		sender.sendMessage(textComponent(o));
	}

	public CommandException commandError(Object... o)
	{
		checkArguments(o);
		return new CommandException(key, o);
	}
}