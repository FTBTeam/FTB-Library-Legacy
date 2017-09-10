package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.lib.net.MessageToServer;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
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
		return FTBLibNetHandler.NET;
	}

	@Override
	public void fromBytes(ByteBuf io)
	{
		groupData = NetUtils.readJsonElement(io).getAsJsonObject();
	}

	@Override
	public void toBytes(ByteBuf io)
	{
		NetUtils.writeJsonElement(io, groupData);
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