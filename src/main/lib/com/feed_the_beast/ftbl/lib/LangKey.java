package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.FTBLibConfig;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author LatvianModder
 */
public final class LangKey implements IStringSerializable
{
	private final String key;

	public static LangKey of(String key)
	{
		return new LangKey(key);
	}

	private LangKey(String s)
	{
		key = s;
	}

	@Override
	public String getName()
	{
		return key;
	}

	public String translate()
	{
		return StringUtils.translate(key);
	}

	public String translate(Object... o)
	{
		return StringUtils.translate(key, o);
	}

	public ITextComponent textComponent()
	{
		TextComponentTranslation component = new TextComponentTranslation(key, CommonUtils.NO_OBJECTS);

		if (FTBLibConfig.CLIENTLESS_MODE.getBoolean())
		{
			return new TextComponentString(component.getFormattedText());
		}

		return component;
	}

	public ITextComponent textComponent(Object... o)
	{
		TextComponentTranslation component = new TextComponentTranslation(key, o);

		if (FTBLibConfig.CLIENTLESS_MODE.getBoolean())
		{
			return new TextComponentString(component.getFormattedText());
		}

		return component;
	}

	public void printChat(ICommandSender sender, Object... o)
	{
		sender.sendMessage(textComponent(o));
	}

	public CommandException commandError(Object... o)
	{
		return new CommandException(key, o);
	}
}