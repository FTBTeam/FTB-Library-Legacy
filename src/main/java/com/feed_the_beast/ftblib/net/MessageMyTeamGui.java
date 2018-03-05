package com.feed_the_beast.ftblib.net;

import com.feed_the_beast.ftblib.FTBLibCommon;
import com.feed_the_beast.ftblib.client.teamsgui.GuiMyTeam;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.TeamGuiAction;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
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
		public boolean enabled;

		private Action(DataIn data)
		{
			id = data.readResourceLocation();
			title = data.readTextComponent();
			requiresConfirm = data.readBoolean();
			icon = data.readIcon();
			enabled = data.readBoolean();
		}

		public Action(TeamGuiAction action, TeamGuiAction.Type t)
		{
			id = action.getId();
			title = action.getTitle();
			requiresConfirm = action.getRequireConfirm();
			icon = action.getIcon();
			order = action.getOrder();
			enabled = t == TeamGuiAction.Type.ENABLED;
		}

		private void writeData(DataOut data)
		{
			data.writeResourceLocation(id);
			data.writeTextComponent(title);
			data.writeBoolean(requiresConfirm);
			data.writeIcon(icon);
			data.writeBoolean(enabled);
		}
	}

	private ITextComponent title;
	private Collection<Action> actions;

	public MessageMyTeamGui()
	{
	}

	public MessageMyTeamGui(ForgePlayer player)
	{
		title = player.team.getTitle();
		actions = new ArrayList<>();
		NBTTagCompound emptyData = new NBTTagCompound();

		for (TeamGuiAction action : FTBLibCommon.TEAM_GUI_ACTIONS.values())
		{
			TeamGuiAction.Type type = action.getType(player, emptyData);

			if (type != TeamGuiAction.Type.INVISIBLE)
			{
				actions.add(new Action(action, type));
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
		data.writeTextComponent(title);
		data.writeCollection(actions, Action.SERIALIZER);
	}

	@Override
	public void readData(DataIn data)
	{
		title = data.readTextComponent();
		actions = data.readCollection(Action.DESERIALIZER);
	}

	@Override
	public void onMessage(MessageMyTeamGui m, EntityPlayer player)
	{
		new GuiMyTeam(m.title, m.actions).openGuiLater();
	}
}