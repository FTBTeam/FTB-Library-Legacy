package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;

public class TextBoxLM extends WidgetLM
{
    private boolean isSelected = false;
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
    public void mousePressed(IGui gui, IMouseButton b)
    {
        if(charLimit == 0)
        {
            return;
        }

        if(gui.isMouseOver(this))
        {
            setSelected(gui, true);
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
            setSelected(gui, false);
        }
    }

    public boolean isSelected(IGui gui)
    {
        return isSelected;
    }

    public void setSelected(IGui gui, boolean v)
    {
        isSelected = v;
    }

    @Override
    public boolean keyPressed(IGui gui, int key, char keyChar)
    {
        if(charLimit == 0)
        {
            return false;
        }

        if(isSelected(gui))
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
                setSelected(gui, false);
            }
            else if(key == Keyboard.KEY_TAB)
            {
                if(isValid())
                {
                    onTabPressed(gui);
                    setSelected(gui, false);
                }
            }
            else if(key == Keyboard.KEY_RETURN)
            {
                if(isValid())
                {
                    onEnterPressed(gui);
                    setSelected(gui, false);
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

    public void onTextChanged(IGui gui)
    {
    }

    public void onTabPressed(IGui gui)
    {
    }

    public void onEnterPressed(IGui gui)
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
    public void renderWidget(IGui gui)
    {
        String s = getText();

        String ns = s;

        if(isSelected(gui) && Minecraft.getSystemTime() % 1000L > 500L)
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
                gui.getFont().drawString(ns, (int) (getAX() + textRenderX - (gui.getFont().getStringWidth(s) / 2D) + getWidth() / 2D), (int) (getAY() + textRenderY), col);
            }
            else
            {
                gui.getFont().drawString(ns, (int) (getAX() + textRenderX), (int) (getAY() + textRenderY), col);
            }
        }
    }

    public boolean isValid()
    {
        return true;
    }
}