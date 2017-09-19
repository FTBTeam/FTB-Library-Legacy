package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.EnumReloadType;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api_impl.SharedClientData;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.lib.io.DataIn;
import com.feed_the_beast.ftbl.lib.io.DataOut;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

/**
 * @author LatvianModder
 */
public class MessageReload extends MessageToClient<MessageReload>
{
	private int typeID;
	private NBTTagCompound syncData;
	private UUID universeId;
	private ResourceLocation reloadId;

	public MessageReload()
	{
	}

	public MessageReload(EnumReloadType t, NBTTagCompound sync, ResourceLocation id)
	{
		typeID = t.ordinal();
		syncData = sync;
		universeId = SharedServerData.INSTANCE.getUniverseId();
		reloadId = id;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.NET;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeByte(typeID);
		data.writeNBT(syncData);
		data.writeUUID(universeId);
		data.writeResourceLocation(reloadId);
	}

	@Override
	public void readData(DataIn data)
	{
		typeID = data.readUnsignedByte();
		syncData = data.readNBT();
		universeId = data.readUUID();
		reloadId = data.readResourceLocation();
	}

	@Override
	public void onMessage(MessageReload m, EntityPlayer player)
	{
		EnumReloadType type = m.typeID >= EnumReloadType.values().length ? EnumReloadType.MODE_CHANGED : EnumReloadType.values()[m.typeID];

		SharedClientData.INSTANCE.universeId = m.universeId;

		for (String key : m.syncData.getKeySet())
		{
			ISyncData nbt = FTBLibModCommon.SYNCED_DATA.get(key);

			if (nbt != null)
			{
				nbt.readSyncData(m.syncData.getCompoundTag(key));
			}
		}

		if (type != EnumReloadType.RELOAD_COMMAND)
		{
			FTBLibAPI.API.reload(Side.CLIENT, player, type, m.reloadId);
		}
	}
}