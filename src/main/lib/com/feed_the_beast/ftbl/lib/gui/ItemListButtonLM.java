package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.client.DrawableItem;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemListButtonLM extends ButtonLM
{
    private final DrawableItem item;
    private final int cols;

    public ItemListButtonLM(int x, int y, DrawableItem i, int c)
    {
        super(x, y, 16, 16);
        item = i;

        cols = c;
        setWidth(cols == 0 ? 16 : (4 + Math.min(cols, i.getItemCount()) * 16));
        setHeight(cols == 0 ? 16 : (4 + (i.getItemCount() / cols + 1) * 16));
    }

    @Override
    public void renderWidget(IGui gui)
    {
        int ax = getAX();
        int ay = getAY();

        if(cols == 0)
        {
            item.draw(ax, ay, 16, 16);
        }
        else
        {
            for(int i = item.getItemCount() - 1; i >= 0; i--)
            {
                item.setIndex(i);
                item.draw(ax + 2 + (i % cols) * 16, ay + 2 + (i / cols) * 16, 16, 16);
            }
        }
    }

    @Override
    public void addMouseOverText(IGui gui, List<String> list)
    {
        int index = -1;

        if(cols > 0)
        {
            int mx = gui.getMouseX() - getAX() - 2;
            int my = gui.getMouseY() - getAY() - 2;

            if(mx < 0 || my < 0 || mx >= getWidth() - 4 || my >= getHeight() - 4)
            {
                return;
            }

            index = ((mx / 16) % cols + (my / 16) * cols);
        }

        ItemStack stack = item.getStack(index);

        if(stack != null)
        {
            list.add(stack.getDisplayName());
            stack.getItem().addInformation(stack, Minecraft.getMinecraft().player, list, false);
        }
    }

    @Override
    public void onClicked(IGui gui, IMouseButton button)
    {
        /*
        if(cols == 0)
        {
            //getStack(-1)
        }
        else
        {
            //int x = gui.getMouseX() - 2;
            //int y = gui.getMouseY() - 2;
        }
        */
    }
}