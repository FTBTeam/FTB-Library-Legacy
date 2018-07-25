package com.feed_the_beast.ftblib.lib.gui.misc;

import com.feed_the_beast.ftblib.lib.config.ConfigValue;
import com.feed_the_beast.ftblib.lib.gui.Button;
import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.TextBox;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiConfigValueField extends GuiBase
{
	private final ConfigValue defValue, value;
	private final IGuiFieldCallback callback;

	private final Button buttonCancel, buttonAccept;
	private final TextBox textBox;

	GuiConfigValueField(ConfigValue val, IGuiFieldCallback c)
	{
		setSize(200, 54);
		defValue = val.copy();
		value = val.copy();
		callback = c;

		int bsize = width / 2 - 10;

		buttonCancel = new SimpleTextButton(this, I18n.format("gui.cancel"), Icon.EMPTY)
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

		buttonCancel.setPosAndSize(8, height - 24, bsize, 16);

		buttonAccept = new SimpleTextButton(this, I18n.format("gui.accept"), Icon.EMPTY)
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

		buttonAccept.setPosAndSize(width - bsize - 8, height - 24, bsize, 16);

		textBox = new TextBox(this)
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

		textBox.setPosAndSize(8, 8, width - 16, 16);
		textBox.writeText(val.getString());
		textBox.setFocused(true);
	}

	@Override
	public void addWidgets()
	{
		add(buttonCancel);
		add(buttonAccept);
		add(textBox);
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		GuiScreen screen = getPrevScreen();
		return screen != null && screen.doesGuiPauseGame();
	}
}