package com.feed_the_beast.ftblib.command.client;

import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.util.NBTUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;

import java.util.Arrays;
import java.util.HashSet;

public class CmdPrintItem extends CmdBase
{
	public CmdPrintItem()
	{
		super("print_item", Level.ALL);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, final String[] args) throws CommandException
	{
		if (!(sender instanceof EntityPlayer))
		{
			return;
		}

		ItemStack stack = ((EntityPlayer) sender).getHeldItem(EnumHand.MAIN_HAND);

		if (stack.isEmpty())
		{
			return;
		}

		HashSet<String> argsSet = new HashSet<>(Arrays.asList(args));

		ITextComponent component = new TextComponentString(stack.getDisplayName() + " :: " + NBTUtils.getColoredNBTString(stack.serializeNBT()));
		component.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, stack.serializeNBT().toString()));
		sender.sendMessage(component);

		if (argsSet.contains("copy"))
		{
			GuiScreen.setClipboardString(stack.serializeNBT().toString());
		}
	}
}