package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.config.PropertyColor;
import com.feed_the_beast.ftbl.lib.gui.GuiLM;
import net.minecraft.client.renderer.GlStateManager;

public class GuiColorField extends GuiLM
{
    private final int initCol;
    private final IGuiFieldCallback callback;

    GuiColorField(PropertyColor p, IGuiFieldCallback c)
    {
        super(256, 256);
        initCol = p.getColorValue();
        callback = c;
    }

    @Override
    public void onClosed()
    {
        callback.onCallback(new PropertyColor(initCol), false);
    }

    @Override
    public void addWidgets()
    {
    }

    @Override
    public void drawBackground()
    {
        GlStateManager.color(1F, 1F, 1F, 1F);
    }
}