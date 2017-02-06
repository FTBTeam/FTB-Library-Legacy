package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.math.MathHelperLM;
import com.feed_the_beast.ftbl.lib.util.LMInvUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemListButtonLM extends ButtonLM
{
    private final List<ItemStack> list;
    private final int cols;

    public ItemListButtonLM(int x, int y, List<ItemStack> l, int c)
    {
        super(x, y, 16, 16);

        if(l.contains(null))
        {
            list = new ArrayList<>();

            for(ItemStack is : l)
            {
                if(is != null)
                {
                    list.add(is);
                }
            }
        }
        else
        {
            list = l;
        }

        cols = c;
        setWidth(cols == 0 ? 16 : (4 + Math.min(cols, list.size()) * 16));
        setHeight(cols == 0 ? 16 : (4 + (list.size() / cols + 1) * 16));
    }

    public ItemStack getStack(int index)
    {
        if(list.isEmpty())
        {
            return LMInvUtils.ERROR_ITEM;
        }

        if(index == -1)
        {
            return list.get((int) ((System.currentTimeMillis() / 1000L) % list.size()));
        }

        return list.get(MathHelperLM.wrap(index, list.size()));
    }

    @Override
    public void renderWidget(IGui gui)
    {
        int ax = getAX();
        int ay = getAY();
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

        if(cols == 0)
        {
            try
            {
                GuiHelper.renderGuiItem(renderItem, getStack(-1), ax, ay, true);
            }
            catch(Exception ex)
            {
                GlStateManager.popMatrix();
                GuiHelper.renderGuiItem(renderItem, LMInvUtils.ERROR_ITEM, ax, ay, true);
            }
        }
        else
        {
            for(int i = list.size() - 1; i >= 0; i--)
            {
                try
                {
                    GuiHelper.renderGuiItem(renderItem, getStack(i), ax + 2 + (i % cols) * 16, ay + 2 + (i / cols) * 16, true);
                }
                catch(Exception ex)
                {
                    GlStateManager.popMatrix();
                    GuiHelper.renderGuiItem(renderItem, LMInvUtils.ERROR_ITEM, ax + 2 + (i % cols) * 16, ay + 2 + (i / cols) * 16, true);
                }
            }
        }
    }

    @Override
    public void addMouseOverText(IGui gui, List<String> l)
    {
        ItemStack stack;

        if(cols > 0)
        {
            int mx = gui.getMouseX() - getAX() - 2;
            int my = gui.getMouseY() - getAY() - 2;

            if(mx < 0 || my < 0 || mx >= getWidth() - 4 || my >= getHeight() - 4)
            {
                return;
            }

            int index = ((mx / 16) % cols + (my / 16) * cols);

            if(index < 0 || index >= list.size())
            {
                return;
            }

            stack = getStack(index);
        }
        else
        {
            stack = getStack(-1);
        }

        l.add(stack.getDisplayName());
        stack.getItem().addInformation(stack, Minecraft.getMinecraft().thePlayer, l, false);
    }

    @Override
    public void onClicked(IGui gui, IMouseButton button)
    {
        if(cols == 0)
        {
            //getStack(-1)
        }
        else
        {
            //int x = gui.getMouseX() - 2;
            //int y = gui.getMouseY() - 2;
        }
    }
}