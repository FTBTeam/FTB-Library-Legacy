package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.util.misc.MouseButton;
import com.feed_the_beast.ftbl.lib.util.text_components.TextComponentCountdown;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ExtendedTextField extends TextField
{
	public ITextComponent textComponent;
	private List<GuiHelper.PositionedTextData> textData;
	private long lastUpdate = -1L;

	public ExtendedTextField(GuiBase gui, int x, int y, int width, int height, ITextComponent t)
	{
		super(gui, x, y, width, height, "");
		textComponent = t;
		setTitle("");
	}

	@Override
	public ExtendedTextField setTitle(String txt)
	{
		lastUpdate = -1L;

		if (textComponent != null)
		{
			for (ITextComponent component : textComponent)
			{
				if (component instanceof TextComponentCountdown)
				{
					lastUpdate = 0L;
				}
			}

			super.setTitle(textComponent.getFormattedText());
			textData = GuiHelper.createDataFrom(textComponent, ClientUtils.MC.fontRenderer, width);
		}

		return this;
	}

	@Nullable
	private GuiHelper.PositionedTextData getDataAtMouse()
	{
		int ax = getAX();
		int ay = getAY();

		for (GuiHelper.PositionedTextData data : textData)
		{
			if (gui.isMouseOver(data.posX + ax, data.posY + ay, data.width, data.height))
			{
				return data;
			}
		}

		return null;
	}

	@Override
	public void addMouseOverText(List<String> list)
	{
		GuiHelper.PositionedTextData data = getDataAtMouse();

		if (data != null && data.hoverEvent != null) //TODO: Special handling for each data.hoverEvent.getAction()
		{
			Collections.addAll(list, data.hoverEvent.getValue().getFormattedText().split("\n"));
		}
	}

	@Override
	public void onClicked(MouseButton button)
	{
		GuiHelper.PositionedTextData data = getDataAtMouse();

		if (data != null && data.clickEvent != null && GuiHelper.onClickEvent(data.clickEvent))
		{
			GuiHelper.playClickSound();
		}
	}

	@Override
	public void renderWidget()
	{
		if (lastUpdate != -1L)
		{
			long ms = System.currentTimeMillis();

			if (lastUpdate <= ms)
			{
				lastUpdate = ms + 500L;
				setTitle("");
				getParentPanel().refreshWidgets();
			}
		}

		super.renderWidget();
	}
}