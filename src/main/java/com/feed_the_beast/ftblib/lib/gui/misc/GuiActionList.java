package com.feed_the_beast.ftblib.lib.gui.misc;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.data.Action;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.WidgetType;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class GuiActionList extends GuiButtonListBase
{
	private class ActionButton extends SimpleTextButton
	{
		private final Action.Inst action;

		private ActionButton(Panel panel, Action.Inst a)
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
						callback.accept(action.id);
					}
				}, action.title.getFormattedText() + "?", "", 0)); //LANG
			}
			else
			{
				callback.accept(action.id);
			}
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

	private final ArrayList<Action.Inst> actions;
	private final Consumer<ResourceLocation> callback;

	public GuiActionList(String title, Collection<Action.Inst> a, Consumer<ResourceLocation> c)
	{
		setTitle(title);
		actions = new ArrayList<>(a);
		actions.sort(null);
		callback = c;
	}

	@Override
	public void addButtons(Panel panel)
	{
		for (Action.Inst a : actions)
		{
			panel.add(new ActionButton(panel, a));
		}
	}
}
