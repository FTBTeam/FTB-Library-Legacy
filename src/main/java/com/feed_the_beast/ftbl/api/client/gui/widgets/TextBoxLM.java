package com.feed_the_beast.ftbl.api.client.gui.widgets;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
public class TextBoxLM extends WidgetLM
{
    public boolean isSelected = false;
    public int charLimit = -1;
    public double textRenderX = 4, textRenderY = 4;
    public int textColor = 0xFFFFFFFF;
    private String text = "";

    public TextBoxLM(int x, int y, int w, int h)
    {
        super(x, y, w, h);
        text = getText();
    }

    @Override
    public void mousePressed(GuiLM gui, MouseButton b)
    {
        if(charLimit == 0)
        {
            return;
        }

        if(gui.isMouseOver(this))
        {
            isSelected = true;
            Keyboard.enableRepeatEvents(true);

            if(b.isRight() && getText().length() > 0)
            {
                clear();
                onTextChanged(gui);
            }
        }
        else
        {
            Keyboard.enableRepeatEvents(false);
            isSelected = false;
        }
    }

    @Override
    public boolean keyPressed(GuiLM gui, int key, char keyChar)
    {
        if(charLimit == 0)
        {
            return false;
        }

        if(isSelected)
        {
            if(key == Keyboard.KEY_BACK)
            {
                text = getText();
                if(text.length() > 0)
                {
                    if(GuiScreen.isCtrlKeyDown())
                    {
                        clear();
                    }
                    else
                    {
                        setText(text.substring(0, text.length() - 1));
                    }
                    onTextChanged(gui);
                }
            }
            else if(key == Keyboard.KEY_ESCAPE)
            {
                isSelected = false;
            }
            else if(key == Keyboard.KEY_TAB)
            {
                if(isValid())
                {
                    onTabPressed(gui);
                    isSelected = false;
                }
            }
            else if(key == Keyboard.KEY_RETURN)
            {
                if(isValid())
                {
                    onEnterPressed(gui);
                    isSelected = false;
                }
            }
            else
            {
                text = getText();
                if((charLimit == -1 || text.length() + 1 <= charLimit) && ChatAllowedCharacters.isAllowedCharacter(keyChar))
                {
                    setText(text + keyChar);
                    onTextChanged(gui);
                }
            }

            return true;
        }

        return false;
    }

    public void onTextChanged(GuiLM gui)
    {
    }

    public void onTabPressed(GuiLM gui)
    {
    }

    public void onEnterPressed(GuiLM gui)
    {
    }

    public void clear()
    {
        setText("");
    }

    public String getText()
    {
        return text;
    }

    public void setText(@Nullable String s)
    {
        text = s == null ? "" : s;
    }

    @Override
    public void renderWidget(GuiLM gui)
    {
        String s = getText();

        String ns = s;

        if(isSelected && Minecraft.getSystemTime() % 1000L > 500L)
        {
            ns += '_';
        }

        if(ns.length() > 0)
        {
            int col = textColor;
            if(!isValid())
            {
                col = 0xFFFF0000;
            }

            if(textRenderX == -1)
            {
                gui.font.drawString(ns, (int) (getAX() + textRenderX - (gui.font.getStringWidth(s) / 2D) + width / 2D), (int) (getAY() + textRenderY), col);
            }
            else
            {
                gui.font.drawString(ns, (int) (getAX() + textRenderX), (int) (getAY() + textRenderY), col);
            }
        }
    }

    public boolean isValid()
    {
        return true;
    }
}