package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.util.StringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;

/**
 * @author LatvianModder
 */
public final class LangKey implements IStringSerializable
{
	private final String key;

	public LangKey(String s)
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
		return StringUtils.translation(key);
	}

	public ITextComponent textComponent(Object... o)
	{
		return StringUtils.translation(key, o);
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