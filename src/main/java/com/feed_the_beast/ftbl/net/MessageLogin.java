package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.EnumReloadType;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import com.feed_the_beast.ftbl.api_impl.SharedClientData;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.internal.FTBLibPerms;
import com.feed_the_beast.ftbl.lib.io.Bits;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.server.permission.PermissionAPI;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class MessageLogin extends MessageToClient<MessageLogin>
{
	private static final byte IS_OP = 1;
	private static final byte OPTIONAL_SERVER_MODS = 2;

	private byte flags;
	private UUID universeId;
	private NBTTagCompound syncData;
	private Collection<String> optionalServerMods;

	public MessageLogin()
	{
	}

	public MessageLogin(EntityPlayerMP player, IForgePlayer forgePlayer)
	{
		flags = 0;
		flags = Bits.setFlag(flags, IS_OP, PermissionAPI.hasPermission(player, FTBLibPerms.SHOW_OP_BUTTONS));
		universeId = SharedServerData.INSTANCE.getUniverseID();
		syncData = new NBTTagCompound();
		FTBLibModCommon.SYNCED_DATA.forEach((key, value) -> syncData.setTag(key, value.writeSyncData(player, forgePlayer)));
		optionalServerMods = SharedServerData.INSTANCE.optionalServerMods;
		flags = Bits.setFlag(flags, OPTIONAL_SERVER_MODS, !optionalServerMods.isEmpty());
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.NET;
	}

	@Override
	public void toBytes(ByteBuf io)
	{
		io.writeByte(flags);
		NetUtils.writeUUID(io, universeId);
		ByteBufUtils.writeTag(io, syncData);

		if (!optionalServerMods.isEmpty())
		{
			io.writeShort(optionalServerMods.size());

			for (String s : optionalServerMods)
			{
				ByteBufUtils.writeUTF8String(io, s);
			}
		}
	}

	@Override
	public void fromBytes(ByteBuf io)
	{
		flags = io.readByte();
		universeId = NetUtils.readUUID(io);
		syncData = ByteBufUtils.readTag(io);

		if (Bits.getFlag(flags, OPTIONAL_SERVER_MODS))
		{
			int s = io.readUnsignedShort();
			optionalServerMods = new HashSet<>(s);

			while (--s >= 0)
			{
				optionalServerMods.add(ByteBufUtils.readUTF8String(io));
			}
		}
	}

	@Override
	public void onMessage(MessageLogin m, EntityPlayer player)
	{
		SharedClientData.INSTANCE.reset();
		SharedClientData.INSTANCE.isClientPlayerOP = Bits.getFlag(m.flags, IS_OP);
		SharedClientData.INSTANCE.universeID = m.universeId;

		if (m.optionalServerMods != null && !m.optionalServerMods.isEmpty())
		{
			SharedClientData.INSTANCE.optionalServerMods.addAll(m.optionalServerMods);
		}

		for (String key : m.syncData.getKeySet())
		{
			ISyncData nbt = FTBLibModCommon.SYNCED_DATA.get(key);

			if (nbt != null)
			{
				nbt.readSyncData(m.syncData.getCompoundTag(key));
			}
		}

		ClientUtils.CACHED_SKINS.clear();
		FTBLibAPI.API.reload(Side.CLIENT, player, EnumReloadType.CREATED, ReloadEvent.ALL);
	}
}