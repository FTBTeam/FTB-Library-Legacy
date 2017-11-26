package com.feed_the_beast.ftbl.api;

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
public abstract class ATHelper
{
	public static ATHelper INSTANCE;

	public abstract Slot addSlot(Container container, Slot slot);

	public abstract boolean mergeItemStack(Container container, ItemStack stack, int startIndex, int endIndex, boolean reverseDirection);

	public abstract TextFormatting getTextFormattingFromDyeColor(EnumDyeColor color);

	public abstract char getTextFormattingChar(TextFormatting formatting);

	public abstract Set<ICommand> getCommandSet(CommandHandler handler);

	@Nullable
	public abstract Boolean getBold(Style style);

	@Nullable
	public abstract Boolean getItalic(Style style);

	@Nullable
	public abstract Boolean getStriketrough(Style style);

	@Nullable
	public abstract Boolean getUnderlined(Style style);

	@Nullable
	public abstract Boolean getObfuscated(Style style);

	@Nullable
	public abstract TextFormatting getColor(Style style);

	@Nullable
	public abstract ClickEvent getClickEvent(Style style);

	@Nullable
	public abstract HoverEvent getHoverEvent(Style style);

	@Nullable
	public abstract String getInsertion(Style style);

	@Nullable
	public abstract Style getParentStyle(Style style);
}