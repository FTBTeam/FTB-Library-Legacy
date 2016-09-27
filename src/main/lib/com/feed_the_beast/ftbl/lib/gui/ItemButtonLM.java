package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IGui;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public abstract class ItemButtonLM extends ButtonLM
{
    private ItemStack item;

    public ItemButtonLM(int x, int y, int w, int h, @Nullable ItemStack is)
    {
        super(x, y, w, h);
        item = is;
    }

    public void setItem(@Nullable ItemStack is)
    {
        item = is;
    }

    @Nullable
    public ItemStack getItem()
    {
        return item;
    }

    @Override
    public void renderWidget(IGui gui)
    {
        ItemStack stack = getItem();

        if(stack != null)
        {
            GuiHelper.renderGuiItem(Minecraft.getMinecraft().getRenderItem(), stack, getAX(), getAY());
        }
    }
}