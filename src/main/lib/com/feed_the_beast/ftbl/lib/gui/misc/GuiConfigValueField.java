package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.TextBox;

public class GuiConfigValueField extends GuiBase
{
	private final IConfigValue defValue, value;
	private final IGuiFieldCallback callback;

	private final Button buttonCancel, buttonAccept;
	private final TextBox textBox;

	GuiConfigValueField(IConfigValue val, IGuiFieldCallback c)
	{
		super(200, 60);
		defValue = val.copy();
		value = val.copy();
		callback = c;

		int bsize = width / 2 - 3;

		buttonCancel = new Button(2, height - 28, bsize, 26, GuiLang.BUTTON_CANCEL.translate())
		{
			@Override
			public void onClicked(GuiBase gui, IMouseButton button)
			{
				GuiHelper.playClickSound();
				callback.onCallback(defValue, false);
			}

			@Override
			public Color4I renderTitleInCenter(GuiBase gui)
			{
				return gui.getContentColor();
			}
		};

		buttonCancel.setIcon(Button.DEFAULT_BACKGROUND);

		buttonAccept = new Button(width - bsize - 2, height - 28, bsize, 26, GuiLang.BUTTON_ACCEPT.translate())
		{
			@Override
			public void onClicked(GuiBase gui, IMouseButton button)
			{
				GuiHelper.playClickSound();

				if (value.setValueFromString(textBox.getText(), false))
				{
					callback.onCallback(value, true);
				}
			}

			@Override
			public Color4I renderTitleInCenter(GuiBase gui)
			{
				return gui.getContentColor();
			}
		};

		buttonAccept.setIcon(Button.DEFAULT_BACKGROUND);

		textBox = new TextBox(2, 2, width - 4, 28)
		{
			@Override
			public boolean isValid(String txt)
			{
				return value.setValueFromString(txt, true);
			}

			@Override
			public void onTextChanged(GuiBase gui)
			{
				textBox.textColor = value.getColor();
			}

			@Override
			public void onEnterPressed(GuiBase gui)
			{
				buttonAccept.onClicked(GuiConfigValueField.this, MouseButton.LEFT);
			}
		};

		textBox.writeText(this, val.toString());
		textBox.setFocused(true);
		textBox.background = Button.DEFAULT_BACKGROUND;
	}

	@Override
	public void addWidgets()
	{
		add(buttonCancel);
		add(buttonAccept);
		add(textBox);
	}

	@Override
	public void drawBackground()
	{
		getIcon(this).draw(this, Color4I.NONE);
	}

	@Override
	public IDrawableObject getIcon(GuiBase gui)
	{
		return DEFAULT_BACKGROUND;
	}
}