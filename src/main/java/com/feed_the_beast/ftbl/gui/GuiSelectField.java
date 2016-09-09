package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.api.gui.GuiHelper;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.GuiLang;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.widgets.ButtonSimpleLM;
import com.feed_the_beast.ftbl.api.gui.widgets.TextBoxLM;
import com.feed_the_beast.ftbl.api_impl.MouseButton;
import com.latmod.lib.ObjectCallbackHandler;
import com.latmod.lib.math.Converter;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nullable;

public class GuiSelectField extends GuiLM
{
    public enum FieldType
    {
        STRING,
        INTEGER,
        DOUBLE;

        public boolean isValid(String s)
        {
            switch(this)
            {
                case INTEGER:
                {
                    return Converter.canParseInt(s);
                }
                case DOUBLE:
                {
                    return Converter.canParseDouble(s);
                }
                default:
                {
                    return true;
                }
            }
        }
    }

    public final Object ID;
    public final FieldType type;
    public final String def;
    public final ObjectCallbackHandler callback;

    public final ButtonSimpleLM buttonCancel, buttonAccept;
    public final TextBoxLM textBox;

    public GuiSelectField(@Nullable Object id, FieldType typ, String d, ObjectCallbackHandler c)
    {
        super(100, 40);
        ID = id;
        type = typ;
        def = d;
        callback = c;

        int bsize = getWidth() / 2 - 4;

        buttonCancel = new ButtonSimpleLM(2, getHeight() - 18, bsize, 16, GuiLang.BUTTON_CANCEL.translate())
        {
            @Override
            public void onClicked(IGui gui, IMouseButton button)
            {
                GuiHelper.playClickSound();
                callback.onCallback(ID, def);
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
                    switch(type)
                    {
                        case STRING:
                            callback.onCallback(ID, textBox.getText());
                            break;
                        case INTEGER:
                            callback.onCallback(ID, Integer.parseInt(textBox.getText()));
                            break;
                        case DOUBLE:
                            callback.onCallback(ID, Double.parseDouble(textBox.getText()));
                            break;
                    }
                }
            }
        };

        textBox = new TextBoxLM(2, 2, getWidth() - 4, 18)
        {
            @Override
            public boolean isValid()
            {
                return type.isValid(getText());
            }

            @Override
            public void onEnterPressed(IGui gui)
            {
                buttonAccept.onClicked(GuiSelectField.this, MouseButton.LEFT);
            }
        };

        textBox.setText(def);
        textBox.textRenderX = -1;
        textBox.textRenderY = 6;
        textBox.textColor = 0xFFEEEEEE;
    }

    public static void display(@Nullable Object id, FieldType type, Object d, ObjectCallbackHandler c)
    {
        new GuiSelectField(id, type, String.valueOf(d), c).openGui();
    }

    public GuiSelectField setCharLimit(int i)
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