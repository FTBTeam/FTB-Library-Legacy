package com.feed_the_beast.ftblib.lib.gui.misc;

import com.feed_the_beast.ftblib.lib.config.ConfigValue;
import com.feed_the_beast.ftblib.lib.gui.Button;
import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.GuiLang;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.TextBox;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;

public class GuiConfigValueField extends GuiBase
{
	private final ConfigValue defValue, value;
	private final IGuiFieldCallback callback;

	private final Button buttonCancel, buttonAccept;
	private final TextBox textBox;

	GuiConfigValueField(ConfigValue val, IGuiFieldCallback c)
	{
		super(200, 54);
		defValue = val.copy();
		value = val.copy();
		callback = c;

		int bsize = width / 2 - 10;

		buttonCancel = new SimpleTextButton(this, 8, height - 24, GuiLang.CANCEL.translate(), Icon.EMPTY)
		{
			@Override
			public void onClicked(MouseButton button)
			{
				GuiHelper.playClickSound();
				callback.onCallback(defValue, false);
			}

			@Override
			public boolean renderTitleInCenter()
			{
				return true;
			}
		};

		buttonCancel.setWidth(bsize);
		buttonCancel.setHeight(16);

		buttonAccept = new SimpleTextButton(this, width - bsize - 8, height - 24, GuiLang.ACCEPT.translate(), Icon.EMPTY)
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
			public boolean renderTitleInCenter()
			{
				return true;
			}
		};

		buttonAccept.setWidth(bsize);
		buttonAccept.setHeight(16);

		textBox = new TextBox(this, 8, 8, width - 16, 16)
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