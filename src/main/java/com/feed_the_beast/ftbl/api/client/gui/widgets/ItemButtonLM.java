package com.feed_the_beast.ftbl.api.client.gui.widgets;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class ItemButtonLM extends ButtonLM
{
    public ItemStack item;

    public ItemButtonLM(double x, double y, double w, double h, ItemStack is)
    {
        super(x, y, w, h);
        item = is;
    }

    public void setItem(ItemStack is)
    {
        item = is;
    }

    @Override
    public void renderWidget(GuiLM gui)
    {
        if(item != null)
        {
            FTBLibClient.renderGuiItem(item, getAX(), getAY());
        }
    }
}