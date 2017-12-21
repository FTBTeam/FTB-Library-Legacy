package com.feed_the_beast.ftblib.net;

import com.feed_the_beast.ftblib.FTBLibModCommon;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ISyncData;
import com.feed_the_beast.ftblib.lib.data.SharedClientData;
import com.feed_the_beast.ftblib.lib.data.SharedServerData;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class MessageSyncData extends MessageToClient<MessageSyncData>
{
	private int flags;
	private UUID universeId;
	private NBTTagCompound syncData;
	private Collection<String> optionalServerMods;

	public MessageSyncData()
	{
	}

	public MessageSyncData(EntityPlayerMP player, ForgePlayer forgePlayer)
	{
		flags = 0;//unused currently, 1 was used for OP
		universeId = SharedServerData.INSTANCE.getUniverseId();
		syncData = new NBTTagCompound();

		for (Map.Entry<String, ISyncData> entry : FTBLibModCommon.SYNCED_DATA.entrySet())
		{
			syncData.setTag(entry.getKey(), entry.getValue().writeSyncData(player, forgePlayer));
		}

		optionalServerMods = SharedServerData.INSTANCE.optionalServerMods;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.GENERAL;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeByte(flags);
		data.writeUUID(universeId);
		data.writeNBT(syncData);
		data.writeCollection(optionalServerMods, DataOut.STRING);
	}

	@Override
	public void readData(DataIn data)
	{
		flags = data.readByte();
		universeId = data.readUUID();
		syncData = data.readNBT();
		optionalServerMods = data.readCollection(DataIn.STRING);
	}

	@Override
	public void onMessage(MessageSyncData m, EntityPlayer player)
	{
		SharedClientData.INSTANCE.reset();
		SharedClientData.INSTANCE.universeId = m.universeId;
		SharedClientData.INSTANCE.optionalServerMods.addAll(m.optionalServerMods);

		for (String key : m.syncData.getKeySet())
		{
			ISyncData nbt = FTBLibModCommon.SYNCED_DATA.get(key);

			if (nbt != null)
			{
				nbt.readSyncData(m.syncData.getCompoundTag(key));
			}
		}

		ClientUtils.CACHED_SKINS.clear();
	}
}