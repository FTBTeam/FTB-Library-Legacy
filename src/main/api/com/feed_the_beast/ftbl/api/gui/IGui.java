package com.feed_the_beast.ftbl.api.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    @SideOnly(Side.CLIENT)
    ScaledResolution getScreen();

    int getMouseX();

    int getMouseY();

    int getMouseWheel();

    float getPartialTicks();

    boolean isMouseButtonDown(int button);

    boolean isKeyDown(int key);

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

    default int getTextColor()
    {
        return 0xFF666666;
    }
}