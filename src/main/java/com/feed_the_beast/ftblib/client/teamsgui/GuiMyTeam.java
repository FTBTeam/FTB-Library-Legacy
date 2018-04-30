package com.feed_the_beast.ftblib.client.teamsgui;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.data.Action;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.WidgetType;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.feed_the_beast.ftblib.net.MessageMyTeamAction;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.Collection;

/**
 * @author LatvianModder
 */
public class GuiMyTeam extends GuiButtonListBase
{
	public static class ActionButton extends SimpleTextButton
	{
		private final Action.Inst action;

		public ActionButton(Panel panel, Action.Inst a)
		{
			super(panel, a.title.getFormattedText(), a.icon);
			action = a;
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();

			if (action.requiresConfirm)
			{
				ClientUtils.MC.displayGuiScreen(new GuiYesNo((result, id) ->
				{
					getGui().openGui();

					if (result)
					{
						sendAction(action.id);
					}
				}, action.title.getFormattedText() + "?", "", 0)); //LANG
			}
			else
			{
				sendAction(action.id);
			}
		}

		public void sendAction(ResourceLocation id)
		{
			new MessageMyTeamAction(id, new NBTTagCompound()).sendToServer();
		}

		@Override
		public boolean renderTitleInCenter()
		{
			return false;
		}

		@Override
		public WidgetType getWidgetType()
		{
			return action.enabled ? WidgetType.mouseOver(isMouseOver()) : WidgetType.DISABLED;
		}
	}

	private Collection<Action.Inst> actions;

	public GuiMyTeam(ITextComponent t, Collection<Action.Inst> l)
	{
		setTitle(t.getFormattedText());
		actions = l;
	}

	@Override
	public void addButtons(Panel panel)
	{
		for (Action.Inst action : actions)
		{
			panel.add(new ActionButton(panel, action));
		}
	}
}