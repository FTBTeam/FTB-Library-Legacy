package com.feed_the_beast.ftblib.lib;

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
public final class ATHelper
{
	public static Slot addSlot(Container container, Slot slot)
	{
		return container.addSlotToContainer(slot);
	}

	public static boolean mergeItemStack(Container container, ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
	{
		return container.mergeItemStack(stack, startIndex, endIndex, reverseDirection);
	}

	public static TextFormatting getTextFormattingFromDyeColor(EnumDyeColor color)
	{
		return color.chatColor;
	}

	public static char getTextFormattingChar(TextFormatting formatting)
	{
		return formatting.formattingCode;
	}

	public static Set<ICommand> getCommandSet(CommandHandler handler)
	{
		return handler.commandSet;
	}

	@Nullable
	public static Boolean getBold(Style style)
	{
		return style.bold;
	}

	@Nullable
	public static Boolean getItalic(Style style)
	{
		return style.italic;
	}

	@Nullable
	public static Boolean getStriketrough(Style style)
	{
		return style.strikethrough;
	}

	@Nullable
	public static Boolean getUnderlined(Style style)
	{
		return style.underlined;
	}

	@Nullable
	public static Boolean getObfuscated(Style style)
	{
		return style.obfuscated;
	}

	@Nullable
	public static TextFormatting getColor(Style style)
	{
		return style.color;
	}

	@Nullable
	public static ClickEvent getClickEvent(Style style)
	{
		return style.clickEvent;
	}

	@Nullable
	public static HoverEvent getHoverEvent(Style style)
	{
		return style.hoverEvent;
	}

	@Nullable
	public static String getInsertion(Style style)
	{
		return style.insertion;
	}

	@Nullable
	public static Style getParentStyle(Style style)
	{
		return style.parentStyle;
	}
}