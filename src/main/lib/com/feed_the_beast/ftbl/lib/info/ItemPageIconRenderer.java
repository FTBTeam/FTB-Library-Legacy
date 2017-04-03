package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.api.info.IPageIconRenderer;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.item.ItemStackSerializer;
import com.feed_the_beast.ftbl.lib.util.LMInvUtils;
import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 06.10.2016.
 */
public class ItemPageIconRenderer implements IPageIconRenderer
{
    private List<ItemStack> items;

    public ItemPageIconRenderer(ItemStack is)
    {
        items = Collections.singletonList(is);
    }

    public ItemPageIconRenderer(Item item)
    {
        items = new ArrayList<>();

        try
        {
            item.getSubItems(item, CreativeTabs.SEARCH, items);
        }
        catch(Exception ex)
        {
            items.clear();
        }
    }

    public ItemPageIconRenderer(JsonElement json)
    {
        try
        {
            if(json.isJsonArray())
            {
                items = new ArrayList<>(json.getAsJsonArray().size());

                for(JsonElement e : json.getAsJsonArray())
                {
                    items.add(ItemStackSerializer.parseItem(e.getAsString()));
                }
            }
            else
            {
                items = Collections.singletonList(ItemStackSerializer.parseItem(json.getAsString()));
            }
        }
        catch(Exception ex)
        {
            items = Collections.singletonList(LMInvUtils.ERROR_ITEM);
        }
    }

    @Override
    public void renderIcon(IGui gui, IWidget widget, int x, int y)
    {
        if(!items.isEmpty())
        {
            GuiHelper.drawItem(Minecraft.getMinecraft().getRenderItem(), items.get((int) ((System.currentTimeMillis() / 1000L) % items.size())), x, y, true);
        }
    }
}