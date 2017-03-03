package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.gui.ButtonLM;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiLM;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.TextBoxLM;

public class GuiConfigValueField extends GuiLM
{
    private final IConfigValue defValue, value;
    private final IGuiFieldCallback callback;

    private final ButtonLM buttonCancel, buttonAccept;
    private final TextBoxLM textBox;

    GuiConfigValueField(IConfigValue val, IGuiFieldCallback c)
    {
        super(200, 60);
        defValue = val.copy();
        value = val.copy();
        callback = c;

        int bsize = getWidth() / 2 - 3;

        buttonCancel = new ButtonLM(2, getHeight() - 28, bsize, 26, GuiLang.BUTTON_CANCEL.translate())
        {
            @Override
            public void onClicked(IGui gui, IMouseButton button)
            {
                GuiHelper.playClickSound();
                callback.onCallback(defValue, false);
            }

            @Override
            public int renderTitleInCenter(IGui gui)
            {
                return gui.getTextColor();
            }
        };

        buttonCancel.setIcon(ButtonLM.DEFAULT_BACKGROUND);

        buttonAccept = new ButtonLM(getWidth() - bsize - 2, getHeight() - 28, bsize, 26, GuiLang.BUTTON_ACCEPT.translate())
        {
            @Override
            public void onClicked(IGui gui, IMouseButton button)
            {
                GuiHelper.playClickSound();

                if(value.setValueFromString(textBox.getText(), false))
                {
                    callback.onCallback(value, true);
                }
            }

            @Override
            public int renderTitleInCenter(IGui gui)
            {
                return gui.getTextColor();
            }
        };

        buttonAccept.setIcon(ButtonLM.DEFAULT_BACKGROUND);

        textBox = new TextBoxLM(2, 2, getWidth() - 4, 28)
        {
            @Override
            public boolean isValid(String txt)
            {
                return value.setValueFromString(txt, true);
            }

            @Override
            public void onTextChanged(IGui gui)
            {
                textBox.textColor = value.getColor();
            }

            @Override
            public void onEnterPressed(IGui gui)
            {
                buttonAccept.onClicked(GuiConfigValueField.this, MouseButton.LEFT);
            }
        };

        textBox.writeText(this, val.toString());
        textBox.setFocused(true);
        textBox.background = ButtonLM.DEFAULT_BACKGROUND;
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
        getIcon(this).draw(this);
    }

    @Override
    public IDrawableObject getIcon(IGui gui)
    {
        return DEFAULT_BACKGROUND;
    }
}