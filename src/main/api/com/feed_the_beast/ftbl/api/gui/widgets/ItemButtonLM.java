package com.feed_the_beast.ftbl.api.gui.widgets;

import com.feed_the_beast.ftbl.api.gui.GuiLM;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public abstract class ItemButtonLM extends ButtonLM
{
    public ItemStack item;

    public ItemButtonLM(int x, int y, int w, int h, @Nullable ItemStack is)
    {
        super(x, y, w, h);
        item = is;
    }

    public void setItem(@Nullable ItemStack is)
    {
        item = is;
    }

    @Override
    public void renderWidget(GuiLM gui)
    {
        if(item != null)
        {
            GuiLM.renderGuiItem(gui.mc.getRenderItem(), item, getAX(), getAY());
        }
    }
}