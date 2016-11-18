package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.gui.ButtonSimpleLM;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiLM;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.TextBoxLM;
import net.minecraft.client.renderer.GlStateManager;

public abstract class GuiAbstractField extends GuiLM
{
    private final IConfigValue defValue, value;
    private final IGuiFieldCallback callback;

    private final ButtonSimpleLM buttonCancel, buttonAccept;
    private final TextBoxLM textBox;

    GuiAbstractField(IConfigValue val, IGuiFieldCallback c)
    {
        super(100, 40);
        defValue = val.copy();
        value = val.copy();
        callback = c;

        int bsize = getWidth() / 2 - 4;

        buttonCancel = new ButtonSimpleLM(2, getHeight() - 18, bsize, 16, GuiLang.BUTTON_CANCEL.translate())
        {
            @Override
            public void onClicked(IGui gui, IMouseButton button)
            {
                GuiHelper.playClickSound();
                callback.onCallback(defValue, false);
            }
        };

        buttonAccept = new ButtonSimpleLM(getWidth() - bsize - 2, getHeight() - 18, bsize, 16, GuiLang.BUTTON_ACCEPT.translate())
        {
            @Override
            public void onClicked(IGui gui, IMouseButton button)
            {
                GuiHelper.playClickSound();
                if(textBox.isValid())
                {
                    setValue(value, textBox.getText());
                    callback.onCallback(value, true);
                }
            }
        };

        textBox = new TextBoxLM(2, 2, getWidth() - 4, 18)
        {
            @Override
            public boolean isValid()
            {
                return GuiAbstractField.this.isValidText(value, getText());
            }

            @Override
            public void onEnterPressed(IGui gui)
            {
                buttonAccept.onClicked(GuiAbstractField.this, MouseButton.LEFT);
            }
        };

        textBox.setText(val.toString());
        textBox.textRenderX = -1;
        textBox.textRenderY = 6;
        textBox.textColor = 0xFFEEEEEE;
        textBox.setSelected(this, true);
    }

    protected abstract boolean isValidText(IConfigValue value, String val);

    protected abstract void setValue(IConfigValue value, String val);

    public GuiAbstractField setCharLimit(int i)
    {
        textBox.charLimit = i;
        return this;
    }

    @Override
    public void addWidgets()
    {
        add(buttonCancel);
        add(buttonAccept);
        add(textBox);
    }

    @Override
    public void renderWidgets()
    {
    }

    @Override
    public void drawBackground()
    {
        int size = 8 + getFont().getStringWidth(textBox.getText());
        if(size > getWidth())
        {
            setWidth(size);
            int bsize = size / 2 - 4;
            buttonAccept.setWidth(bsize);
            buttonCancel.setWidth(bsize);
            buttonAccept.posX = getWidth() - bsize - 2;
            textBox.setWidth(getWidth() - 4);
            initGui();
        }

        GlStateManager.color(0.4F, 0.4F, 0.4F, 0.66F);
        GuiHelper.drawBlankRect(posX, posY, getWidth(), getHeight());
        GlStateManager.color(0.2F, 0.2F, 0.2F, 1F);
        GuiHelper.drawBlankRect(textBox.getAX(), textBox.getAY(), textBox.getWidth(), textBox.getHeight());
        GlStateManager.color(1F, 1F, 1F, 1F);
        buttonAccept.renderWidget(this);
        buttonCancel.renderWidget(this);
        textBox.renderWidget(this);
    }
}