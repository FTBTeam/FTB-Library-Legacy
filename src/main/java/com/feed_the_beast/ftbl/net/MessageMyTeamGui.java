package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.team.ITeamGuiAction;
import com.feed_the_beast.ftbl.client.teamsgui.GuiMyTeam;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.io.DataIn;
import com.feed_the_beast.ftbl.lib.io.DataOut;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * @author LatvianModder
 */
public class MessageMyTeamGui extends MessageToClient<MessageMyTeamGui>
{
	public static class Action
	{
		private static final DataOut.Serializer<Action> SERIALIZER = (data, object) -> object.writeData(data);
		private static final DataIn.Deserializer<Action> DESERIALIZER = Action::new;
		private static final Comparator<Action> COMPARATOR = (o1, o2) -> o1.order - o2.order;

		public final ResourceLocation id;
		public final ITextComponent title;
		public final boolean requiresConfirm;
		public final Icon icon;
		private int order;

		private Action(DataIn data)
		{
			id = data.readResourceLocation();
			title = data.readTextComponent();
			requiresConfirm = data.readBoolean();
			icon = data.readIcon();
		}

		public Action(ITeamGuiAction action)
		{
			id = action.getId();
			title = action.getTitle();
			requiresConfirm = action.getRequireConfirm();
			icon = action.getIcon();
			order = action.getOrder();
		}

		private void writeData(DataOut data)
		{
			data.writeResourceLocation(id);
			data.writeTextComponent(title);
			data.writeBoolean(requiresConfirm);
			data.writeIcon(icon);
		}
	}

	private String title;
	private Collection<Action> actions;

	public MessageMyTeamGui()
	{
	}

	public MessageMyTeamGui(IForgeTeam team, IForgePlayer player)
	{
		title = team.getColor().getTextFormatting() + team.getTitle();
		actions = new ArrayList<>();

		for (ITeamGuiAction action : FTBLibModCommon.TEAM_GUI_ACTIONS.values())
		{
			if (action.isAvailable(team, player, new NBTTagCompound()))
			{
				actions.add(new Action(action));
			}
		}

		((List<Action>) actions).sort(Action.COMPARATOR);
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.MY_TEAM;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeString(title);
		data.writeCollection(actions, Action.SERIALIZER);
	}

	@Override
	public void readData(DataIn data)
	{
		title = data.readString();
		actions = data.readCollection(Action.DESERIALIZER);
	}

	@Override
	public void onMessage(MessageMyTeamGui m, EntityPlayer player)
	{
		new GuiMyTeam(m.title, m.actions).openGuiLater();
	}
}