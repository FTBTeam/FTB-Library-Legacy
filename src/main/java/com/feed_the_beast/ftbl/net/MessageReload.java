package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.EnumReloadType;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api_impl.SharedClientData;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

/**
 * @author LatvianModder
 */
public class MessageReload extends MessageToClient<MessageReload>
{
	private int typeID;
	private NBTTagCompound syncData;
	private UUID universeID;
	private ResourceLocation reloadId;

	public MessageReload()
	{
	}

	public MessageReload(EnumReloadType t, NBTTagCompound sync, ResourceLocation id)
	{
		typeID = t.ordinal();
		syncData = sync;
		universeID = SharedServerData.INSTANCE.getUniverseID();
		reloadId = id;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.NET;
	}

	@Override
	public void toBytes(ByteBuf io)
	{
		io.writeByte(typeID);
		ByteBufUtils.writeTag(io, syncData);
		NetUtils.writeUUID(io, universeID);
		NetUtils.writeResourceLocation(io, reloadId);
	}

	@Override
	public void fromBytes(ByteBuf io)
	{
		typeID = io.readUnsignedByte();
		syncData = ByteBufUtils.readTag(io);
		universeID = NetUtils.readUUID(io);
		reloadId = NetUtils.readResourceLocation(io);
	}

	@Override
	public void onMessage(MessageReload m, EntityPlayer player)
	{
		EnumReloadType type = m.typeID >= EnumReloadType.VALUES.length ? EnumReloadType.MODE_CHANGED : EnumReloadType.VALUES[m.typeID];

		SharedClientData.INSTANCE.universeID = m.universeID;

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