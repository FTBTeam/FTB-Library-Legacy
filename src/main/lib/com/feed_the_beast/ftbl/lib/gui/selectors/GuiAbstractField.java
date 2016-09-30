package com.feed_the_beast.ftbl.lib.gui.selectors;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.gui.ButtonSimpleLM;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiLM;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.TextBoxLM;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nullable;

public abstract class GuiAbstractField<T> extends GuiLM
{
    protected final Object ID;
    private final ButtonSimpleLM buttonCancel, buttonAccept;
    private final TextBoxLM textBox;

    GuiAbstractField(@Nullable Object id, T def)
    {
        super(100, 40);
        ID = id;

        int bsize = getWidth() / 2 - 4;

        buttonCancel = new ButtonSimpleLM(2, getHeight() - 18, bsize, 16, GuiLang.BUTTON_CANCEL.translate())
        {
            @Override
            public void onClicked(IGui gui, IMouseButton button)
            {
                GuiHelper.playClickSound();
                onCancelled(def);
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
                    onCallback(textBox.getText());
                }
            }
        };

        textBox = new TextBoxLM(2, 2, getWidth() - 4, 18)
        {
            @Override
            public boolean isValid()
            {
                return GuiAbstractField.this.isValid(getText());
            }

            @Override
            public void onEnterPressed(IGui gui)
            {
                buttonAccept.onClicked(GuiAbstractField.this, MouseButton.LEFT);
            }
        };

        textBox.setText(String.valueOf(def));
        textBox.textRenderX = -1;
        textBox.textRenderY = 6;
        textBox.textColor = 0xFFEEEEEE;
        textBox.setSelected(this, true);
    }

    protected abstract boolean isValid(String val);

    protected abstract void onCancelled(T def);

    protected abstract void onCallback(String val);

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