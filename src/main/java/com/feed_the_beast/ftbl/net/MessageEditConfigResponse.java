package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.lib.io.DataIn;
import com.feed_the_beast.ftbl.lib.io.DataOut;
import com.feed_the_beast.ftbl.lib.net.MessageToServer;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author LatvianModder
 */
public class MessageEditConfigResponse extends MessageToServer<MessageEditConfigResponse>
{
	private JsonObject groupData;

	public MessageEditConfigResponse()
	{
	}

	public MessageEditConfigResponse(JsonObject json)
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
		data.writeJson(groupData);
	}

	@Override
	public void readData(DataIn data)
	{
		groupData = data.readJson().getAsJsonObject();
	}

	@Override
	public void onMessage(MessageEditConfigResponse m, EntityPlayer player)
	{
		FTBLibModCommon.EditingConfig c = FTBLibModCommon.TEMP_SERVER_CONFIG.get(player.getGameProfile().getId());

		if (c != null)
		{
			c.callback.saveConfig(c.group, player, m.groupData);
			FTBLibModCommon.TEMP_SERVER_CONFIG.remove(player.getGameProfile().getId());
		}
	}
}