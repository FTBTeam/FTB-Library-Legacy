package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.api.gui.GuiHelper;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.client.FTBLibColors;
import com.latmod.lib.ObjectCallbackHandler;
import com.latmod.lib.util.LMColorUtils;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GuiSelectColor extends GuiLM
{
    private final ObjectCallbackHandler callback;
    private final byte initCol;
    private final Object colorID;
    private final List<ButtonColor> colorButtons;

    private class ButtonColor extends ButtonLM
    {
        private final byte colID;

        private ButtonColor(int x, int y, byte cid)
        {
            super(x, y, 16, 16);
            colID = cid;
            setTitle(LMColorUtils.getHex(FTBLibColors.get(colID)));
        }

        @Override
        public void onClicked(IGui gui, IMouseButton button)
        {
            GuiHelper.playClickSound();
            callback.onCallback(colorID, colID);
        }
    }

    public GuiSelectColor(@Nullable Object id, byte cid, ObjectCallbackHandler cb)
    {
        super(143, 93);
        callback = cb;
        initCol = cid;
        colorID = id;

        colorButtons = new ArrayList<>();

        for(int y = 0; y < FTBLibColors.HEIGHT; y++)
        {
            for(int x = 0; x < FTBLibColors.WIDTH; x++)
            {
                colorButtons.add(new ButtonColor(x * 16, y * 16, (byte) (x + y * FTBLibColors.WIDTH)));
            }
        }
    }

    @Override
    public void addWidgets()
    {
        addAll(colorButtons);
    }

    @Override
    public void drawBackground()
    {
        GlStateManager.color(0.5F, 0.5F, 0.5F, 0.3F);
        GuiHelper.drawBlankRect(getAX(), getAY(), getWidth(), getHeight());
        GlStateManager.color(1F, 1F, 1F, 1F);
    }
}