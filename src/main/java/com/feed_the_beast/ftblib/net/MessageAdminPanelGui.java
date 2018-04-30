package com.feed_the_beast.ftblib.net;

import com.feed_the_beast.ftblib.FTBLibCommon;
import com.feed_the_beast.ftblib.lib.data.Action;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.net.MessageToServer;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class MessageAdminPanelGui extends MessageToServer
{
	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.GENERAL;
	}

	@Override
	public boolean hasData()
	{
		return false;
	}

	@Override
	public void onMessage(EntityPlayerMP player)
	{
		List<Action.Inst> actions = new ArrayList<>();
		ForgePlayer p = Universe.get().getPlayer(player);
		NBTTagCompound data = new NBTTagCompound();

		for (Action a : FTBLibCommon.ADMIN_PANEL_ACTIONS.values())
		{
			Action.Type type = a.getType(p, data);

			if (type.isVisible())
			{
				actions.add(new Action.Inst(a, type));
			}
		}

		new MessageAdminPanelGuiResponse(actions).sendTo(player);
	}
}