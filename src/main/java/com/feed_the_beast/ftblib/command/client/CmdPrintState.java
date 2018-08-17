package com.feed_the_beast.ftblib.command.client;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.util.BlockUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;

public class CmdPrintState extends CmdBase
{
	public CmdPrintState()
	{
		super("print_block_state", Level.ALL);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, final String[] args) throws CommandException
	{
		if (ClientUtils.MC.objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK)
		{
			return;
		}

		BlockPos pos = ClientUtils.MC.objectMouseOver.getBlockPos();
		IBlockState state = sender.getEntityWorld().getBlockState(pos);

		ITextComponent component = new TextComponentString(state.getBlock().getItem(sender.getEntityWorld(), pos, state).getDisplayName() + " :: " + BlockUtils.getNameFromState(state));
		component.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, BlockUtils.getNameFromState(state)));
		sender.sendMessage(component);
	}
}