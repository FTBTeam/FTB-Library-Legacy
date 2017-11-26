package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.ATHelper;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * @author LatvianModder
 */
public class ATHelperImpl extends ATHelper
{
	@Override
	public Slot addSlot(Container container, Slot slot)
	{
		return container.addSlotToContainer(slot);
	}

	@Override
	public boolean mergeItemStack(Container container, ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
	{
		return container.mergeItemStack(stack, startIndex, endIndex, reverseDirection);
	}

	@Override
	public TextFormatting getTextFormattingFromDyeColor(EnumDyeColor color)
	{
		return color.chatColor;
	}

	@Override
	public char getTextFormattingChar(TextFormatting formatting)
	{
		return formatting.formattingCode;
	}

	@Override
	public Set<ICommand> getCommandSet(CommandHandler handler)
	{
		return handler.commandSet;
	}

	@Nullable
	@Override
	public Boolean getBold(Style style)
	{
		return style.bold;
	}

	@Nullable
	@Override
	public Boolean getItalic(Style style)
	{
		return style.italic;
	}

	@Nullable
	@Override
	public Boolean getStriketrough(Style style)
	{
		return style.strikethrough;
	}

	@Nullable
	@Override
	public Boolean getUnderlined(Style style)
	{
		return style.underlined;
	}

	@Nullable
	@Override
	public Boolean getObfuscated(Style style)
	{
		return style.obfuscated;
	}

	@Nullable
	@Override
	public TextFormatting getColor(Style style)
	{
		return style.color;
	}

	@Nullable
	@Override
	public ClickEvent getClickEvent(Style style)
	{
		return style.clickEvent;
	}

	@Nullable
	@Override
	public HoverEvent getHoverEvent(Style style)
	{
		return style.hoverEvent;
	}

	@Nullable
	@Override
	public String getInsertion(Style style)
	{
		return style.insertion;
	}

	@Nullable
	@Override
	public Style getParentStyle(Style style)
	{
		return style.parentStyle;
	}
}