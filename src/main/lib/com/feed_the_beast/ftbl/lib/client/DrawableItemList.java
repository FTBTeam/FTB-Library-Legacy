package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.item.ItemStackSerializer;
import com.feed_the_beast.ftbl.lib.math.MathUtils;
import com.feed_the_beast.ftbl.lib.util.InvUtils;
import com.google.gson.JsonElement;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 24.02.2017.
 */
public class DrawableItemList extends DrawableItem
{
    public List<ItemStack> list;

    public DrawableItemList(List<ItemStack> l)
    {
        super(null);

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
    }

    @SideOnly(Side.CLIENT)
    public DrawableItemList(Item item)
    {
        super(null);
        list = new ArrayList<>();

        try
        {
            item.getSubItems(item, CreativeTabs.SEARCH, list);
        }
        catch(Exception ex)
        {
            list.clear();
        }
    }

    public DrawableItemList(JsonElement json)
    {
        super(null);
        try
        {
            if(json.isJsonArray())
            {
                list = new ArrayList<>(json.getAsJsonArray().size());

                for(JsonElement e : json.getAsJsonArray())
                {
                    ItemStack stack = ItemStackSerializer.parseItem(e.getAsString());

                    if(stack != null)
                    {
                        list.add(stack);
                    }
                }
            }
            else
            {
                list = Collections.singletonList(ItemStackSerializer.parseItem(json.getAsString()));
            }
        }
        catch(Exception ex)
        {
            list = Collections.singletonList(InvUtils.ERROR_ITEM);
        }
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    @Override
    @Nullable
    public ItemStack getStack(int index)
    {
        if(index < 0 || index >= list.size())
        {
            return stack;
        }

        return list.get(index);
    }

    @Override
    public void setIndex(int i)
    {
        stack = list.get(MathUtils.wrap(i, list.size()));
    }

    @Override
    public void draw(int x, int y, int w, int h, Color4I col)
    {
        super.draw(x, y, w, h, col);

        if(!list.isEmpty())
        {
            setIndex((int) (System.currentTimeMillis() / 1000L));
        }
    }
}