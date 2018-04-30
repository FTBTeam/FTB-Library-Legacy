package com.feed_the_beast.ftblib.net;

import com.feed_the_beast.ftblib.FTBLibCommon;
import com.feed_the_beast.ftblib.client.teamsgui.GuiMyTeam;
import com.feed_the_beast.ftblib.lib.data.Action;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author LatvianModder
 */
public class MessageMyTeamGuiResponse extends MessageToClient
{
	private ITextComponent title;
	private Collection<Action.Inst> actions;

	public MessageMyTeamGuiResponse()
	{
	}

	public MessageMyTeamGuiResponse(ForgePlayer player)
	{
		title = player.team.getTitle();
		actions = new ArrayList<>();
		NBTTagCompound emptyData = new NBTTagCompound();

		for (Action action : FTBLibCommon.TEAM_GUI_ACTIONS.values())
		{
			Action.Type type = action.getType(player, emptyData);

			if (type.isVisible())
			{
				actions.add(new Action.Inst(action, type));
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
		data.writeTextComponent(title);
		data.writeCollection(actions, Action.Inst.SERIALIZER);
	}

	@Override
	public void readData(DataIn data)
	{
		title = data.readTextComponent();
		actions = data.readCollection(Action.Inst.DESERIALIZER);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage()
	{
		new GuiMyTeam(title, actions).openGui();
	}
}