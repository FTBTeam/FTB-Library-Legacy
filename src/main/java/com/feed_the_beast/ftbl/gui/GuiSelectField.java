package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.GuiLang;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.widgets.ButtonSimpleLM;
import com.feed_the_beast.ftbl.api.gui.widgets.TextBoxLM;
import com.feed_the_beast.ftbl.api_impl.MouseButton;
import com.latmod.lib.ObjectCallbackHandler;
import com.latmod.lib.math.Converter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
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

    public GuiSelectField(Object id, FieldType typ, String d, ObjectCallbackHandler c)
    {
        super(100, 40);
        ID = id;
        type = typ;
        def = d;
        callback = c;

        int bsize = width / 2 - 4;

        buttonCancel = new ButtonSimpleLM(2, height - 18, bsize, 16, GuiLang.button_cancel.translate())
        {
            @Override
            public void onClicked(@Nonnull GuiLM gui, @Nonnull IMouseButton button)
            {
                GuiLM.playClickSound();
                callback.onCallback(ID, def);
            }
        };

        buttonAccept = new ButtonSimpleLM(width - bsize - 2, height - 18, bsize, 16, GuiLang.button_accept.translate())
        {
            @Override
            public void onClicked(@Nonnull GuiLM gui, @Nonnull IMouseButton button)
            {
                GuiLM.playClickSound();
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

        textBox = new TextBoxLM(2, 2, width - 4, 18)
        {
            @Override
            public boolean isValid()
            {
                return type.isValid(getText());
            }

            @Override
            public void onEnterPressed(GuiLM gui)
            {
                buttonAccept.onClicked(GuiSelectField.this, MouseButton.LEFT);
            }
        };

        textBox.setText(def);
        textBox.textRenderX = -1;
        textBox.textRenderY = 6;
        textBox.textColor = 0xFFEEEEEE;
    }

    public static void display(Object id, FieldType type, Object d, ObjectCallbackHandler c)
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
        int size = 8 + font.getStringWidth(textBox.getText());
        if(size > width)
        {
            width = size;
            int bsize = size / 2 - 4;
            buttonAccept.width = buttonCancel.width = bsize;
            buttonAccept.posX = width - bsize - 2;
            textBox.width = width - 4;
            initGui();
        }

        GlStateManager.color(0.4F, 0.4F, 0.4F, 0.66F);
        drawBlankRect(posX, posY, width, height);
        GlStateManager.color(0.2F, 0.2F, 0.2F, 1F);
        drawBlankRect(textBox.getAX(), textBox.getAY(), textBox.width, textBox.height);
        GlStateManager.color(1F, 1F, 1F, 1F);
        buttonAccept.renderWidget(this);
        buttonCancel.renderWidget(this);
        textBox.renderWidget(this);
    }
}