package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

public class TextBox extends Widget
{
    private boolean isFocused = false;
    public int charLimit = 250;
    public int textColor;
    public IDrawableObject background = ImageProvider.NULL;

    public String ghostText = "";
    private String text = "";
    private int lineScrollOffset;
    private int cursorPosition;
    private int selectionEnd;
    private boolean validText;

    public TextBox(int x, int y, int w, int h)
    {
        super(x, y, w, h);
        text = getText();
    }

    public final boolean isFocused()
    {
        return isFocused;
    }

    public final void setFocused(boolean v)
    {
        isFocused = v;
    }

    public final String getText()
    {
        return text;
    }

    public String getSelectedText()
    {
        return text.substring(cursorPosition < selectionEnd ? cursorPosition : selectionEnd, cursorPosition < selectionEnd ? selectionEnd : cursorPosition);
    }

    public final void setText(GuiBase gui, String s, boolean triggerChange)
    {
        text = s;

        if(text.isEmpty())
        {
            lineScrollOffset = 0;
            cursorPosition = 0;
            selectionEnd = 0;
        }

        validText = isValid(s);

        if(validText && triggerChange)
        {
            onTextChanged(gui);
        }
    }

    public final void setText(GuiBase gui, String s)
    {
        setText(gui, s, true);
    }

    public void setCursorPosition(GuiBase gui, int pos)
    {
        cursorPosition = pos;
        int i = text.length();
        cursorPosition = MathHelper.clamp(cursorPosition, 0, i);
        setSelectionPos(gui, cursorPosition);
    }

    public void moveCursorBy(GuiBase gui, int num)
    {
        setCursorPosition(gui, selectionEnd + num);
    }

    public void writeText(GuiBase gui, String textToWrite)
    {
        String s = "";
        String s1 = ChatAllowedCharacters.filterAllowedCharacters(textToWrite);
        int i = cursorPosition < selectionEnd ? cursorPosition : selectionEnd;
        int j = cursorPosition < selectionEnd ? selectionEnd : cursorPosition;
        int k = charLimit - text.length() - (i - j);

        if(!text.isEmpty())
        {
            s = s + text.substring(0, i);
        }

        int l;

        if(k < s1.length())
        {
            s = s + s1.substring(0, k);
            l = k;
        }
        else
        {
            s = s + s1;
            l = s1.length();
        }

        if(!text.isEmpty() && j < text.length())
        {
            s = s + text.substring(j);
        }

        setText(gui, s);
        moveCursorBy(gui, i - selectionEnd + l);
    }

    public void setSelectionPos(GuiBase gui, int position)
    {
        int i = text.length();

        if(position > i)
        {
            position = i;
        }

        if(position < 0)
        {
            position = 0;
        }

        selectionEnd = position;

        if(lineScrollOffset > i)
        {
            lineScrollOffset = i;
        }

        int j = width - 10;
        String s = gui.getFont().trimStringToWidth(text.substring(lineScrollOffset), j);
        int k = s.length() + lineScrollOffset;

        if(position == lineScrollOffset)
        {
            lineScrollOffset -= gui.getFont().trimStringToWidth(text, j, true).length();
        }

        if(position > k)
        {
            lineScrollOffset += position - k;
        }
        else if(position <= lineScrollOffset)
        {
            lineScrollOffset -= lineScrollOffset - position;
        }

        lineScrollOffset = MathHelper.clamp(lineScrollOffset, 0, i);
    }

    public int getNthWordFromCursor(int numWords)
    {
        return getNthWordFromPos(numWords, cursorPosition);
    }

    public int getNthWordFromPos(int n, int pos)
    {
        return getNthWordFromPosWS(n, pos, true);
    }

    public int getNthWordFromPosWS(int n, int pos, boolean skipWs)
    {
        int i = pos;
        boolean flag = n < 0;
        int j = Math.abs(n);

        for(int k = 0; k < j; ++k)
        {
            if(!flag)
            {
                int l = text.length();
                i = text.indexOf(32, i);

                if(i == -1)
                {
                    i = l;
                }
                else
                {
                    while(skipWs && i < l && text.charAt(i) == 32)
                    {
                        ++i;
                    }
                }
            }
            else
            {
                while(skipWs && i > 0 && text.charAt(i - 1) == 32)
                {
                    --i;
                }

                while(i > 0 && text.charAt(i - 1) != 32)
                {
                    --i;
                }
            }
        }

        return i;
    }

    public void deleteWords(GuiBase gui, int num)
    {
        if(!text.isEmpty())
        {
            if(selectionEnd != cursorPosition)
            {
                writeText(gui, "");
            }
            else
            {
                deleteFromCursor(gui, getNthWordFromCursor(num) - cursorPosition);
            }
        }
    }

    public void deleteFromCursor(GuiBase gui, int num)
    {
        if(text.isEmpty())
        {
            return;
        }

        if(selectionEnd != cursorPosition)
        {
            writeText(gui, "");
        }
        else
        {
            boolean flag = num < 0;
            int i = flag ? cursorPosition + num : cursorPosition;
            int j = flag ? cursorPosition : cursorPosition + num;
            String s = "";

            if(i >= 0)
            {
                s = text.substring(0, i);
            }

            if(j < text.length())
            {
                s = s + text.substring(j);
            }

            setText(gui, s);

            if(flag)
            {
                moveCursorBy(gui, num);
            }
        }
    }

    @Override
    public void mousePressed(GuiBase gui, IMouseButton button)
    {
        if(gui.isMouseOver(this))
        {
            setFocused(true);
            Keyboard.enableRepeatEvents(true);

            if(button.isLeft())
            {
                if(isFocused)
                {
                    int i = gui.getMouseX() - getAX();
                    String s = gui.getFont().trimStringToWidth(text.substring(lineScrollOffset), width);
                    setCursorPosition(gui, gui.getFont().trimStringToWidth(s, i).length() + lineScrollOffset);
                }
            }
            else if(getText().length() > 0)
            {
                setText(gui, "");
            }
        }
        else
        {
            Keyboard.enableRepeatEvents(false);
            setFocused(false);
        }
    }

    @Override
    public boolean keyPressed(GuiBase gui, int keyCode, char keyChar)
    {
        if(!isFocused())
        {
            return false;
        }
        else if(GuiScreen.isKeyComboCtrlA(keyCode))
        {
            setCursorPosition(gui, text.length());
            setSelectionPos(gui, 0);
            return true;
        }
        else if(GuiScreen.isKeyComboCtrlC(keyCode))
        {
            GuiScreen.setClipboardString(getSelectedText());
            return true;
        }
        else if(GuiScreen.isKeyComboCtrlV(keyCode))
        {
            writeText(gui, GuiScreen.getClipboardString());
            return true;
        }
        else if(GuiScreen.isKeyComboCtrlX(keyCode))
        {
            GuiScreen.setClipboardString(getSelectedText());
            writeText(gui, "");
            return true;
        }
        else
        {
            switch(keyCode)
            {
                case Keyboard.KEY_BACK:
                    if(GuiScreen.isCtrlKeyDown())
                    {
                        deleteWords(gui, -1);
                    }
                    else
                    {
                        deleteFromCursor(gui, -1);
                    }
                    return true;
                case Keyboard.KEY_HOME:
                    if(GuiScreen.isShiftKeyDown())
                    {
                        setSelectionPos(gui, 0);
                    }
                    else
                    {
                        setCursorPosition(gui, 0);
                    }
                    return true;
                case Keyboard.KEY_LEFT:
                    if(GuiScreen.isShiftKeyDown())
                    {
                        if(GuiScreen.isCtrlKeyDown())
                        {
                            setSelectionPos(gui, getNthWordFromPos(-1, selectionEnd));
                        }
                        else
                        {
                            setSelectionPos(gui, selectionEnd - 1);
                        }
                    }
                    else if(GuiScreen.isCtrlKeyDown())
                    {
                        setCursorPosition(gui, getNthWordFromCursor(-1));
                    }
                    else
                    {
                        moveCursorBy(gui, -1);
                    }
                    return true;
                case Keyboard.KEY_RIGHT:
                    if(GuiScreen.isShiftKeyDown())
                    {
                        if(GuiScreen.isCtrlKeyDown())
                        {
                            setSelectionPos(gui, getNthWordFromPos(1, selectionEnd));
                        }
                        else
                        {
                            setSelectionPos(gui, selectionEnd + 1);
                        }
                    }
                    else if(GuiScreen.isCtrlKeyDown())
                    {
                        setCursorPosition(gui, getNthWordFromCursor(1));
                    }
                    else
                    {
                        moveCursorBy(gui, 1);
                    }
                    return true;
                case Keyboard.KEY_END:
                    if(GuiScreen.isShiftKeyDown())
                    {
                        setSelectionPos(gui, text.length());
                    }
                    else
                    {
                        setCursorPosition(gui, text.length());
                    }
                    return true;
                case Keyboard.KEY_DELETE:
                    if(GuiScreen.isCtrlKeyDown())
                    {
                        deleteWords(gui, 1);
                    }
                    else
                    {
                        deleteFromCursor(gui, 1);
                    }
                    return true;
                case Keyboard.KEY_RETURN:
                    if(validText)
                    {
                        setFocused(false);
                        onEnterPressed(gui);
                    }
                    return true;
                case Keyboard.KEY_TAB:
                    if(validText)
                    {
                        setFocused(false);
                        onTabPressed(gui);
                    }
                    return true;
                default:
                    if(ChatAllowedCharacters.isAllowedCharacter(keyChar))
                    {
                        writeText(gui, Character.toString(keyChar));
                        return true;
                    }
                    else
                    {
                        return false;
                    }
            }
        }
    }

    public void onTextChanged(GuiBase gui)
    {
    }

    public void onTabPressed(GuiBase gui)
    {
    }

    public void onEnterPressed(GuiBase gui)
    {
    }

    @Override
    public void renderWidget(GuiBase gui)
    {
        background.draw(this);

        String textToDraw = (!isFocused() && text.isEmpty()) ? ghostText : text;

        FontRenderer font = gui.getFont();
        int ax = getAX();
        int ay = getAY();
        GuiHelper.pushScissor(gui.getScreen(), ax, ay, width, height);

        int col = validText ? (textColor == 0 ? gui.getTextColor() : textColor) : 0xFFFF0000;
        int j = cursorPosition - lineScrollOffset;
        int k = selectionEnd - lineScrollOffset;
        String s = font.trimStringToWidth(textToDraw.substring(lineScrollOffset), width);
        boolean flag = j >= 0 && j <= s.length();
        boolean flag1 = isFocused() && flag && (Minecraft.getSystemTime() % 1000L > 500L);
        int textX = ax + 4;
        int textY = ay + (height - 8) / 2;
        int textX1 = textX;

        if(k > s.length())
        {
            k = s.length();
        }

        if(!s.isEmpty())
        {
            String s1 = flag ? s.substring(0, j) : s;
            textX1 = font.drawString(s1, textX, textY, col);
        }

        boolean drawCursor = cursorPosition < textToDraw.length() || textToDraw.length() >= charLimit;
        int cursorX = textX1;

        if(!flag)
        {
            cursorX = j > 0 ? textX + width : textX;
        }
        else if(drawCursor)
        {
            cursorX = textX1 - 1;
            //--textX1; //why?
        }

        if(!s.isEmpty() && flag && j < s.length())
        {
            font.drawString(s.substring(j), textX1, textY, col);
        }

        if(flag1)
        {
            if(drawCursor)
            {
                GuiHelper.drawBlankRect(cursorX, textY - 1, 1, font.FONT_HEIGHT + 2);
            }
            else
            {
                GuiHelper.drawBlankRect(cursorX, textY + font.FONT_HEIGHT - 2, 5, 1);
            }
        }

        if(k != j)
        {
            int l1 = textX + font.getStringWidth(s.substring(0, k));

            int startX = cursorX, startY = textY - 1, endX = l1 - 1, endY = textY + 1 + font.FONT_HEIGHT;

            if(startX < endX)
            {
                int i = startX;
                startX = endX;
                endX = i;
            }

            if(startY < endY)
            {
                int j12 = startY;
                startY = endY;
                endY = j12;
            }

            if(endX > ax + width)
            {
                endX = ax + width;
            }

            if(startX > ax + width)
            {
                startX = ax + width;
            }

            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer vertexbuffer = tessellator.getBuffer();
            GlStateManager.color(0F, 0F, 255F, 255F);
            GlStateManager.disableTexture2D();
            GlStateManager.enableColorLogic();
            GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
            vertexbuffer.begin(7, DefaultVertexFormats.POSITION);
            vertexbuffer.pos(startX, endY, 0).endVertex();
            vertexbuffer.pos(endX, endY, 0).endVertex();
            vertexbuffer.pos(endX, startY, 0).endVertex();
            vertexbuffer.pos(startX, startY, 0).endVertex();
            tessellator.draw();
            GlStateManager.disableColorLogic();
            GlStateManager.enableTexture2D();
        }

        GuiHelper.popScissor();
        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    public boolean isValid(String txt)
    {
        return true;
    }
}