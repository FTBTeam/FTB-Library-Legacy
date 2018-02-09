package com.feed_the_beast.ftblib.net;

import com.feed_the_beast.ftblib.FTBLibCommon;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.TeamGuiAction;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToServer;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class MessageMyTeamAction extends MessageToServer<MessageMyTeamAction>
{
	private ResourceLocation action;
	private NBTTagCompound nbt;

	public MessageMyTeamAction()
	{
	}

	public MessageMyTeamAction(ResourceLocation id, NBTTagCompound data)
	{
		action = id;
		nbt = data;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.MY_TEAM;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeResourceLocation(action);
		data.writeNBT(nbt);
	}

	@Override
	public void readData(DataIn data)
	{
		action = data.readResourceLocation();
		nbt = data.readNBT();
	}

	@Override
	public void onMessage(MessageMyTeamAction m, EntityPlayer player)
	{
		TeamGuiAction action = FTBLibCommon.TEAM_GUI_ACTIONS.get(m.action);

		if (action != null)
		{
			ForgePlayer p = Universe.get().getPlayer(player);

			if (p.getTeam() != null && action.isAvailable(p.getTeam(), p, m.nbt))
			{
				action.onAction(p.getTeam(), p, m.nbt);
			}
		}
	}
}