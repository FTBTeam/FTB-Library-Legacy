package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api_impl.FTBLibTeamGuiActions;
import com.feed_the_beast.ftbl.client.teamsgui.GuiManageAllies;
import com.feed_the_beast.ftbl.client.teamsgui.GuiManageMembers;
import com.feed_the_beast.ftbl.client.teamsgui.GuiManageModerators;
import com.feed_the_beast.ftbl.lib.io.DataIn;
import com.feed_the_beast.ftbl.lib.io.DataOut;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * @author LatvianModder
 */
public class MessageMyTeamPlayerList extends MessageToClient<MessageMyTeamPlayerList>
{
	public static class Entry
	{
		private static final DataOut.Serializer<Entry> SERIALIZER = (data, object) -> object.writeData(data);
		private static final DataIn.Deserializer<Entry> DESERIALIZER = Entry::new;

		public final UUID uuid;
		public final String name;
		public EnumTeamStatus status;

		private Entry(DataIn data)
		{
			uuid = data.readUUID();
			name = data.readString();
			status = data.read(EnumTeamStatus.NAME_MAP);
		}

		public Entry(IForgePlayer player, EnumTeamStatus s)
		{
			uuid = player.getId();
			name = player.getName();
			status = s;
		}

		private void writeData(DataOut data)
		{
			data.writeUUID(uuid);
			data.writeString(name);
			data.write(EnumTeamStatus.NAME_MAP, status);
		}
	}

	private ResourceLocation id;
	private Collection<Entry> entries;

	public MessageMyTeamPlayerList()
	{
	}

	public MessageMyTeamPlayerList(ResourceLocation _id, IForgeTeam team, IForgePlayer player, Predicate<EnumTeamStatus> predicate)
	{
		id = _id;
		entries = new ArrayList<>();

		for (IForgePlayer p : FTBLibAPI.API.getUniverse().getPlayers())
		{
			if (p != player && !p.isFake())
			{
				EnumTeamStatus status = team.getHighestStatus(p);

				if (status != EnumTeamStatus.OWNER && predicate.test(status))
				{
					entries.add(new Entry(p, status));
				}
			}
		}
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.MY_TEAM;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeResourceLocation(id);
		data.writeCollection(entries, Entry.SERIALIZER);
	}

	@Override
	public void readData(DataIn data)
	{
		id = data.readResourceLocation();
		entries = data.readCollection(Entry.DESERIALIZER);
	}

	@Override
	public void onMessage(MessageMyTeamPlayerList m, EntityPlayer player)
	{
		if (m.id.equals(FTBLibTeamGuiActions.MEMBERS.getId()))
		{
			new GuiManageMembers(m.entries).openGuiLater();
		}
		else if (m.id.equals(FTBLibTeamGuiActions.ALLIES.getId()))
		{
			new GuiManageAllies(m.entries).openGuiLater();
		}
		else if (m.id.equals(FTBLibTeamGuiActions.MODERATORS.getId()))
		{
			new GuiManageModerators(m.entries).openGuiLater();
		}
		else if (m.id.equals(FTBLibTeamGuiActions.ENEMIES.getId()))
		{
		}
		else if (m.id.equals(FTBLibTeamGuiActions.TRANSFER_OWNERSHIP.getId()))
		{
		}
	}
}