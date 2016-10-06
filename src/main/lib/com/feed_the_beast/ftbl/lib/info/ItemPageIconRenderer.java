package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.api.info.IPageIconRenderer;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.item.ItemStackSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 * Created by LatvianModder on 06.10.2016.
 */
public class ItemPageIconRenderer implements IPageIconRenderer
{
    private ItemStack item;

    public ItemPageIconRenderer(ItemStack is)
    {
        item = is;
    }

    public ItemPageIconRenderer(String s)
    {
        try
        {
            item = ItemStackSerializer.parseItem(s);
        }
        catch(Exception ex)
        {
            item = new ItemStack(Blocks.BARRIER);
            item.setStackDisplayName("Broken Item!");
        }
    }

    @Override
    public void renderIcon(IGui gui, IWidget widget, int x, int y)
    {
        GuiHelper.renderGuiItem(Minecraft.getMinecraft().getRenderItem(), item, x, y);
    }
}