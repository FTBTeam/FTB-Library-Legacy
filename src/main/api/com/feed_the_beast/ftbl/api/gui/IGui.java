package com.feed_the_beast.ftbl.api.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Created by LatvianModder on 04.09.2016.
 */
public interface IGui extends IPanel
{
    @SideOnly(Side.CLIENT)
    GuiScreen getWrapper();

    void openGui();

    void closeGui();

    @SideOnly(Side.CLIENT)
    FontRenderer getFont();

    int getScreenWidth();

    int getScreenHeight();

    int getScreenScaleFactor();

    int getMouseX();

    int getMouseY();

    int getMouseWheel();

    float getPartialTicks();

    @Override
    default boolean isInside(IWidget w)
    {
        int a = w.getAY();

        if(a < -w.getHeight() || a > getScreenHeight())
        {
            return false;
        }

        a = w.getAX();

        return a >= -w.getWidth() && a <= getScreenWidth();
    }

    default void scissor(int x, int y, int w, int h)
    {
        int scale = getScreenScaleFactor();
        int h1 = getScreenHeight() * scale;
        GL11.glScissor(x * scale, h1 - (y * scale + h * scale), w * scale, h * scale);
    }

    default void playSoundFX(SoundEvent e, float pitch)
    {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(e, pitch));
    }

    default boolean isMouseOver(int x, int y, int w, int h)
    {
        return getMouseX() >= x && getMouseY() >= y && getMouseX() < x + w && getMouseY() < y + h;
    }

    default boolean isMouseOver(IWidget w)
    {
        return isMouseOver(w.getAX(), w.getAY(), w.getWidth(), w.getHeight());
    }
}