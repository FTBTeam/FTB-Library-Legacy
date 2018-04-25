package com.feed_the_beast.ftblib.net;

import com.feed_the_beast.ftblib.client.teamsgui.GuiManageAllies;
import com.feed_the_beast.ftblib.client.teamsgui.GuiManageEnemies;
import com.feed_the_beast.ftblib.client.teamsgui.GuiManageMembers;
import com.feed_the_beast.ftblib.client.teamsgui.GuiManageModerators;
import com.feed_the_beast.ftblib.client.teamsgui.GuiTransferOwnership;
import com.feed_the_beast.ftblib.lib.EnumTeamStatus;
import com.feed_the_beast.ftblib.lib.data.FTBLibTeamGuiActions;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * @author LatvianModder
 */
public class MessageMyTeamPlayerList extends MessageToClient<MessageMyTeamPlayerList>
{
	public static class Entry implements Comparable<Entry>
	{
		private static final DataOut.Serializer<Entry> SERIALIZER = (data, object) -> object.writeData(data);
		private static final DataIn.Deserializer<Entry> DESERIALIZER = Entry::new;

		public final UUID uuid;
		public final String name;
		public EnumTeamStatus status;
		public boolean requestingInvite;

		private Entry(DataIn data)
		{
			uuid = data.readUUID();
			name = data.readString();
			status = data.read(EnumTeamStatus.NAME_MAP);
			requestingInvite = data.readBoolean();
		}

		public Entry(ForgePlayer player, EnumTeamStatus s, boolean i)
		{
			uuid = player.getId();
			name = player.getName();
			status = s;
			requestingInvite = i;
		}

		private void writeData(DataOut data)
		{
			data.writeUUID(uuid);
			data.writeString(name);
			data.write(status, EnumTeamStatus.NAME_MAP);
			data.writeBoolean(requestingInvite);
		}

		public int getSortIndex()
		{
			return requestingInvite ? 1000 : Math.max(status.getStatus(), status == EnumTeamStatus.ENEMY ? 1 : 0);
		}

		@Override
		public int compareTo(Entry o)
		{
			int o1s = getSortIndex();
			int o2s = o.getSortIndex();
			return o1s == o2s ? name.compareToIgnoreCase(o.name) : o2s - o1s;
		}
	}

	private ResourceLocation id;
	private Collection<Entry> entries;

	public MessageMyTeamPlayerList()
	{
	}

	public MessageMyTeamPlayerList(ResourceLocation _id, ForgePlayer player, Predicate<EnumTeamStatus> predicate)
	{
		id = _id;
		entries = new ArrayList<>();

		for (ForgePlayer p : player.team.universe.getPlayers())
		{
			if (p != player)
			{
				EnumTeamStatus status = player.team.getHighestStatus(p);

				if (status != EnumTeamStatus.OWNER && predicate.test(status))
				{
					entries.add(new Entry(p, status, player.team.isRequestingInvite(p)));
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
	@SideOnly(Side.CLIENT)
	public void onMessage()
	{
		if (id.equals(FTBLibTeamGuiActions.MEMBERS.getId()))
		{
			new GuiManageMembers(entries).openGui();
		}
		else if (id.equals(FTBLibTeamGuiActions.ALLIES.getId()))
		{
			new GuiManageAllies(entries).openGui();
		}
		else if (id.equals(FTBLibTeamGuiActions.MODERATORS.getId()))
		{
			new GuiManageModerators(entries).openGui();
		}
		else if (id.equals(FTBLibTeamGuiActions.ENEMIES.getId()))
		{
			new GuiManageEnemies(entries).openGui();
		}
		else if (id.equals(FTBLibTeamGuiActions.TRANSFER_OWNERSHIP.getId()))
		{
			new GuiTransferOwnership(entries).openGui();
		}
	}
}