package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.client.gui.GuiLang;
import com.feed_the_beast.ftbl.api.client.gui.LMGuis;
import com.feed_the_beast.ftbl.api.client.gui.widgets.ButtonSimpleLM;
import com.feed_the_beast.ftbl.api.client.gui.widgets.TextBoxLM;
import latmod.lib.ObjectCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSelectField extends GuiLM
{
    public final Object ID;
    public final LMGuis.FieldType type;
    public final String def;
    public final ObjectCallback.Handler callback;

    public final ButtonSimpleLM buttonCancel, buttonAccept;
    public final TextBoxLM textBox;

    public GuiSelectField(Object id, LMGuis.FieldType typ, String d, ObjectCallback.Handler c)
    {
        ID = id;
        type = typ;
        def = d;
        callback = c;

        widthW = 100;
        heightW = 40;

        double bsize = widthW / 2D - 4D;

        buttonCancel = new ButtonSimpleLM(2, heightW - 18, bsize, 16)
        {
            @Override
            public void onClicked(GuiLM gui, MouseButton button)
            {
                FTBLibClient.playClickSound();
                callback.onCallback(new ObjectCallback(ID, false, true, def));
            }
        };

        buttonCancel.title = GuiLang.button_cancel.translate();

        buttonAccept = new ButtonSimpleLM(widthW - bsize - 2, heightW - 18, bsize, 16)
        {
            @Override
            public void onClicked(GuiLM gui, MouseButton button)
            {
                FTBLibClient.playClickSound();
                if(textBox.isValid())
                {
                    switch(type)
                    {
                        case STRING:
                            callback.onCallback(new ObjectCallback(ID, true, true, textBox.getText()));
                            break;
                        case INTEGER:
                            callback.onCallback(new ObjectCallback(ID, true, true, Integer.parseInt(textBox.getText())));
                            break;
                        case DOUBLE:
                            callback.onCallback(new ObjectCallback(ID, true, true, Double.parseDouble(textBox.getText())));
                            break;
                    }
                }
            }
        };

        buttonAccept.title = GuiLang.button_accept.translate();

        textBox = new TextBoxLM(2, 2, widthW - 4, 18)
        {
            @Override
            public boolean isValid()
            {
                return type.isValid(getText());
            }

            @Override
            public void returnPressed()
            {
                buttonAccept.onClicked(GuiSelectField.this, MouseButton.LEFT);
            }
        };

        textBox.setText(def);
        textBox.textRenderX = -1;
        textBox.textRenderY = 6;
        textBox.textColor = 0xFFEEEEEE;
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
    public void drawBackground()
    {
        int size = 8 + font.getStringWidth(textBox.getText());
        if(size > widthW)
        {
            widthW = size;
            int bsize = size / 2 - 4;
            buttonAccept.widthW = buttonCancel.widthW = bsize;
            buttonAccept.posX = widthW - bsize - 2;
            textBox.widthW = widthW - 4;
            initGui();
        }

        GlStateManager.color(0.4F, 0.4F, 0.4F, 0.66F);
        drawBlankRect(posX, posY, widthW, heightW);
        GlStateManager.color(0.2F, 0.2F, 0.2F, 1F);
        drawBlankRect(textBox.getAX(), textBox.getAY(), textBox.widthW, textBox.heightW);
        GlStateManager.color(1F, 1F, 1F, 1F);
        buttonAccept.renderWidget(this);
        buttonCancel.renderWidget(this);
        textBox.renderWidget(this);
    }
}