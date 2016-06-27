package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 27.06.2016.
 */
@SideOnly(Side.CLIENT)
public class GuiLoading extends GuiLM
{
    @Override
    public void onInit()
    {
        width = 128;
        height = 128;
    }

    @Override
    public void addWidgets()
    {
    }

    @Override
    public void drawBackground()
    {
        drawBlankRect(getAX(), getAY(), width, height);
    }
}