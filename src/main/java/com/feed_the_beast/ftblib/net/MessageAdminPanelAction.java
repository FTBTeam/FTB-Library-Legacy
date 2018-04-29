package com.feed_the_beast.ftblib.net;

import com.feed_the_beast.ftblib.FTBLibCommon;
import com.feed_the_beast.ftblib.lib.data.Action;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToServer;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class MessageAdminPanelAction extends MessageToServer<MessageAdminPanelAction>
{
	private ResourceLocation action;

	public MessageAdminPanelAction()
	{
	}

	public MessageAdminPanelAction(ResourceLocation id)
	{
		action = id;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.GENERAL;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeResourceLocation(action);
	}

	@Override
	public void readData(DataIn data)
	{
		action = data.readResourceLocation();
	}

	@Override
	public void onMessage(EntityPlayerMP player)
	{
		Action a = FTBLibCommon.ADMIN_PANEL_ACTIONS.get(action);

		if (a != null)
		{
			ForgePlayer p = Universe.get().getPlayer(player);
			NBTTagCompound data = new NBTTagCompound();

			if (p.hasTeam() && a.getType(p, data).isEnabled())
			{
				a.onAction(p, data);
			}
		}
	}
}