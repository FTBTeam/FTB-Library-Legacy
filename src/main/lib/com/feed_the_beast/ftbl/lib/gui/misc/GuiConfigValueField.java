package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.config.ConfigValue;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.TextBox;
import com.feed_the_beast.ftbl.lib.icon.Color4I;
import com.feed_the_beast.ftbl.lib.util.misc.MouseButton;

public class GuiConfigValueField extends GuiBase
{
	private final ConfigValue defValue, value;
	private final IGuiFieldCallback callback;

	private final Button buttonCancel, buttonAccept;
	private final TextBox textBox;

	GuiConfigValueField(ConfigValue val, IGuiFieldCallback c)
	{
		super(200, 60);
		defValue = val.copy();
		value = val.copy();
		callback = c;

		int bsize = width / 2 - 3;

		buttonCancel = new Button(this, 2, height - 28, bsize, 26, GuiLang.CANCEL.translate())
		{
			@Override
			public void onClicked(MouseButton button)
			{
				GuiHelper.playClickSound();
				callback.onCallback(defValue, false);
			}

			@Override
			public Color4I renderTitleInCenter()
			{
				return gui.getTheme().getContentColor(false);
			}
		};

		buttonAccept = new Button(this, width - bsize - 2, height - 28, bsize, 26, GuiLang.ACCEPT.translate())
		{
			@Override
			public void onClicked(MouseButton button)
			{
				GuiHelper.playClickSound();

				if (value.setValueFromString(textBox.getText(), false))
				{
					callback.onCallback(value, true);
				}
			}

			@Override
			public Color4I renderTitleInCenter()
			{
				return gui.getTheme().getContentColor(false);
			}
		};

		textBox = new TextBox(this, 2, 2, width - 4, 28)
		{
			@Override
			public boolean isValid(String txt)
			{
				return value.setValueFromString(txt, true);
			}

			@Override
			public void onTextChanged()
			{
				textBox.textColor = value.getColor();
			}

			@Override
			public void onEnterPressed()
			{
				buttonAccept.onClicked(MouseButton.LEFT);
			}
		};

		textBox.writeText(val.toString());
		textBox.setFocused(true);
	}

	@Override
	public void addWidgets()
	{
		add(buttonCancel);
		add(buttonAccept);
		add(textBox);
	}
}