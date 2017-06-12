package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ExtendedTextField extends TextField
{
	public final ITextComponent textComponent;
	private List<GuiHelper.PositionedTextData> textData;

	public ExtendedTextField(int x, int y, int width, int height, FontRenderer font, ITextComponent t)
	{
		super(x, y, width, height, font, t.getFormattedText());
		textComponent = t;
		textData = GuiHelper.createDataFrom(t, font, width);
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
	public void onClicked(GuiBase gui, IMouseButton button)
	{
		GuiHelper.PositionedTextData data = getDataAtMouse(gui);

		if (data != null && data.clickEvent != null && GuiHelper.onClickEvent(data.clickEvent))
		{
			GuiHelper.playClickSound();
		}
	}
}