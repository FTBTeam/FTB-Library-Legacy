package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.util.misc.MouseButton;
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
	private long lastUpdate = 0L;

	public ExtendedTextField(int x, int y, int width, int height, ITextComponent t)
	{
		super(x, y, width, height, "");
		textComponent = t;
		setTitle("");
	}

	@Override
	public ExtendedTextField setTitle(String txt)
	{
		if (textComponent != null)
		{
			super.setTitle(textComponent.getFormattedText());
			textData = GuiHelper.createDataFrom(textComponent, ClientUtils.MC.fontRenderer, width);
		}

		return this;
	}

	@Nullable
	private GuiHelper.PositionedTextData getDataAtMouse(GuiBase gui)
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
	public void addMouseOverText(GuiBase gui, List<String> list)
	{
		GuiHelper.PositionedTextData data = getDataAtMouse(gui);

		if (data != null && data.hoverEvent != null) //TODO: Special handling for each data.hoverEvent.getAction()
		{
			Collections.addAll(list, data.hoverEvent.getValue().getFormattedText().split("\n"));
		}
	}

	@Override
	public void onClicked(GuiBase gui, MouseButton button)
	{
		GuiHelper.PositionedTextData data = getDataAtMouse(gui);

		if (data != null && data.clickEvent != null && GuiHelper.onClickEvent(data.clickEvent))
		{
			GuiHelper.playClickSound();
		}
	}

	@Override
	public void renderWidget(GuiBase gui)
	{
		long ms = System.currentTimeMillis();
		if (lastUpdate <= ms)
		{
			lastUpdate = ms + 500L;
			setTitle("");
		}

		super.renderWidget(gui);
	}
}