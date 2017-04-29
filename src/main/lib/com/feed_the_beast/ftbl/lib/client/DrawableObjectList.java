package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.math.MathUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 24.02.2017.
 */
public class DrawableObjectList implements IDrawableObject
{
    public final List<IDrawableObject> list;
    private IDrawableObject current = ImageProvider.NULL;
    public long timer = 1000L;

    public DrawableObjectList(Collection<IDrawableObject> l)
    {
        if(l.contains(null) || l.contains(ImageProvider.NULL))
        {
            list = new ArrayList<>();

            for(IDrawableObject o : l)
            {
                if(o != null && o != ImageProvider.NULL)
                {
                    list.add(o);
                }
            }
        }
        else
        {
            list = new ArrayList<>(l);
        }

        if(!list.isEmpty())
        {
            current = list.get(0);
        }
    }

    @SideOnly(Side.CLIENT)
    public DrawableObjectList(Item item)
    {
        this(Collections.emptyList());

        try
        {
            List<ItemStack> stacks = new ArrayList<>();
            item.getSubItems(item, CreativeTabs.SEARCH, stacks);

            for(ItemStack stack : stacks)
            {
                list.add(new DrawableItem(stack));
            }
        }
        catch(Exception ex)
        {
            list.clear();
        }
    }

    public int getItemCount()
    {
        return list.size();
    }

    public IDrawableObject getObject(int index)
    {
        if(index < 0 || index >= list.size())
        {
            return current;
        }

        return list.get(index);
    }

    public void setIndex(int i)
    {
        current = list.get(MathUtils.wrap(i, list.size()));
    }

    @Override
    public void draw(int x, int y, int w, int h, Color4I col)
    {
        if(current != null)
        {
            current.draw(x, y, w, h, col);
        }

        if(!list.isEmpty())
        {
            setIndex((int) (System.currentTimeMillis() / timer));
        }
    }
}