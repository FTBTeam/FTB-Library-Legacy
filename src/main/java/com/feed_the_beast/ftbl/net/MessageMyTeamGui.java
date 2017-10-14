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
	public static class ActionContainer
	{
		private static final DataOut.Serializer<ActionContainer> SERIALIZER = (data, object) -> object.writeData(data);
		private static final DataIn.Deserializer<ActionContainer> DESERIALIZER = ActionContainer::new;
		private static final Comparator<ActionContainer> COMPARATOR = Comparator.comparingInt(o -> o.order);

		public final ResourceLocation id;
		public final ITextComponent title;
		public final boolean requiresConfirm;
		public final Icon icon;
		private int order;

		private ActionContainer(DataIn data)
		{
			id = data.readResourceLocation();
			title = data.readTextComponent();
			requiresConfirm = data.readBoolean();
			icon = data.readIcon();
		}

		public ActionContainer(ITeamGuiAction action)
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
	private Collection<ActionContainer> actions;

	public MessageMyTeamGui()
	{
	}

	public MessageMyTeamGui(IForgeTeam team, IForgePlayer player)
	{
		title = team.getColor().getTextFormatting() + team.getTitle();
		actions = new ArrayList<>();

		for (ITeamGuiAction action : FTBLibModCommon.TEAM_GUI_ACTIONS.values())
		{
			if (action.isAvailable(team, player))
			{
				actions.add(new ActionContainer(action));
			}
		}

		((List<ActionContainer>) actions).sort(ActionContainer.COMPARATOR);
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
		data.writeCollection(actions, ActionContainer.SERIALIZER);
	}

	@Override
	public void readData(DataIn data)
	{
		title = data.readString();
		actions = data.readCollection(ActionContainer.DESERIALIZER);
	}

	@Override
	public void onMessage(MessageMyTeamGui m, EntityPlayer player)
	{
		new GuiMyTeam(m.title, m.actions).openGuiLater();
	}
}