package com.feed_the_beast.ftblib.net;

import com.feed_the_beast.ftblib.FTBLibCommon;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToServer;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author LatvianModder
 */
public class MessageEditConfigResponse extends MessageToServer
{
	private NBTTagCompound groupData;

	public MessageEditConfigResponse()
	{
	}

	public MessageEditConfigResponse(NBTTagCompound json)
	{
		groupData = json;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.EDIT_CONFIG;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeNBT(groupData);
	}

	@Override
	public void readData(DataIn data)
	{
		groupData = data.readNBT();
	}

	@Override
	public void onMessage(EntityPlayerMP player)
	{
		FTBLibCommon.EditingConfig c = FTBLibCommon.TEMP_SERVER_CONFIG.get(player.getGameProfile().getId());
		//TODO: Logger

		if (c != null)
		{
			c.group.deserializeEditedNBT(groupData);
			c.callback.onConfigSaved(c.group, player);
			FTBLibCommon.TEMP_SERVER_CONFIG.remove(player.getUniqueID());
		}
	}
}