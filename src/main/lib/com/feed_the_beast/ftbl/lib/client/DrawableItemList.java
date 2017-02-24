package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.lib.math.MathHelperLM;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 24.02.2017.
 */
public class DrawableItemList extends DrawableItem
{
    public final List<ItemStack> list;

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
        stack = list.get(MathHelperLM.wrap(i, list.size()));
    }

    @Override
    public void draw(int x, int y, int w, int h)
    {
        super.draw(x, y, w, h);

        if(!list.isEmpty())
        {
            setIndex((int) (System.currentTimeMillis() / 1000L));
        }
    }
}