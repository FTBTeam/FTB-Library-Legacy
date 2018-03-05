package com.feed_the_beast.ftblib.net;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibCommon;
import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.client.FTBLibClient;
import com.feed_the_beast.ftblib.events.SyncGamerulesEvent;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ISyncData;
import com.feed_the_beast.ftblib.lib.io.Bits;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class MessageSyncData extends MessageToClient<MessageSyncData>
{
	private static final int LOGIN = 1;

	private int flags;
	private UUID universeId;
	private NBTTagCompound syncData;
	private Collection<String> optionalServerMods;
	private Map<String, String> gamerules;

	public MessageSyncData()
	{
	}

	public MessageSyncData(boolean login, EntityPlayerMP player, ForgePlayer forgePlayer)
	{
		flags = Bits.setFlag(0, LOGIN, login);
		universeId = forgePlayer.team.universe.getUUID();
		syncData = new NBTTagCompound();

		for (Map.Entry<String, ISyncData> entry : FTBLibCommon.SYNCED_DATA.entrySet())
		{
			syncData.setTag(entry.getKey(), entry.getValue().writeSyncData(player, forgePlayer));
		}

		gamerules = new HashMap<>();
		new SyncGamerulesEvent(gamerule -> gamerules.put(gamerule, player.world.getGameRules().getString(gamerule))).post();

		optionalServerMods = forgePlayer.team.universe.optionalServerMods;
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
		data.writeMap(gamerules, DataOut.STRING, DataOut.STRING);
	}

	@Override
	public void readData(DataIn data)
	{
		flags = data.readByte();
		universeId = data.readUUID();
		syncData = data.readNBT();
		optionalServerMods = data.readCollection(DataIn.STRING);
		gamerules = data.readMap(DataIn.STRING, DataIn.STRING);
	}

	@Override
	public void onMessage(MessageSyncData m, EntityPlayer player)
	{
		FTBLibClient.UNIVERSE_UUID = m.universeId;
		FTBLibClient.OPTIONAL_SERVER_MODS_CLIENT.clear();
		FTBLibClient.OPTIONAL_SERVER_MODS_CLIENT.addAll(m.optionalServerMods);

		for (String key : m.syncData.getKeySet())
		{
			ISyncData nbt = FTBLibCommon.SYNCED_DATA.get(key);

			if (nbt != null)
			{
				nbt.readSyncData(m.syncData.getCompoundTag(key));
			}
		}

		for (Map.Entry<String, String> entry : m.gamerules.entrySet())
		{
			player.world.getGameRules().setOrCreateGameRule(entry.getKey(), entry.getValue());
		}

		if (FTBLibConfig.debugging.print_more_info && Bits.getFlag(m.flags, LOGIN))
		{
			FTBLib.LOGGER.info("Synced data from universe " + StringUtils.fromUUID(FTBLibClient.UNIVERSE_UUID));
		}
	}
}