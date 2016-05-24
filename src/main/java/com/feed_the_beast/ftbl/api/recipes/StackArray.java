package com.feed_the_beast.ftbl.api.recipes;

import com.feed_the_beast.ftbl.api.item.LMInvUtils;
import com.feed_the_beast.ftbl.api.item.MaterialItem;
import com.feed_the_beast.ftbl.api.item.ODItems;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class StackArray implements IStackArray
{
    public final Collection<ItemStack> items;
    private int hashCode;
    private IStackArray[] array;

    public StackArray(Object o)
    {
        items = getItems(o);
        hashCode = toString().hashCode();
        array = new IStackArray[] {this};
    }

    public static StackArray[] convert(ItemStack... o)
    {
        if(o == null)
        {
            return null;
        }
        StackArray[] se = new StackArray[o.length];
        for(int i = 0; i < o.length; i++)
        {
            se[i] = (o[i] == null) ? null : new StackArray(o[i]);
        }
        return se;
    }

    public static StackArray[] convert(Object... o)
    {
        if(o == null)
        {
            return null;
        }
        StackArray[] se = new StackArray[o.length];
        for(int i = 0; i < o.length; i++)
        {
            se[i] = (o[i] == null) ? null : new StackArray(o[i]);
        }
        return se;
    }

    public static StackArray[] convertInv(IInventory inv, EnumFacing side)
    {
        if(inv == null)
        {
            return null;
        }
        return convert(LMInvUtils.getAllItems(inv, side));
    }

    public static Collection<ItemStack> getItems(Object o)
    {
        if(o == null)
        {
            return new ArrayList<>();
        }
        ItemStack item0 = getFrom(o);
        if(item0 != null)
        {
            return Collections.singleton(item0);
        }
        else if(o instanceof ItemStack[])
        {
            return Arrays.asList((ItemStack[]) o);
        }
        else if(o instanceof String)
        {
            ODItems.getOres((String) o);
        }
        return new ArrayList<>();
    }

    public static ItemStack getFrom(Object o)
    {
        if(o == null)
        {
            return null;
        }
        else if(o instanceof ItemStack)
        {
            return ((ItemStack) o);
        }
        else if(o instanceof Item)
        {
            return new ItemStack((Item) o);
        }
        else if(o instanceof Block)
        {
            return new ItemStack((Block) o);
        }
        else if(o instanceof MaterialItem)
        {
            return ((MaterialItem) o).getStack(1);
        }
        else
        {
            return null;
        }
    }

    public static boolean itemsEquals(ItemStack is1, ItemStack is2)
    {
        if(is1 == null && is2 == null)
        {
            return true;
        }
        if(is1 == null || is2 == null)
        {
            return false;
        }

        if(is1.getItem() == is2.getItem())
        {
            int dmg1 = is1.getItemDamage();
            int dmg2 = is1.getItemDamage();
            return dmg1 == dmg2 || dmg2 == ODItems.ANY;// || dmg1 == ODItems.ANY;
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "StackEntry: " + items;
    }

    @Override
    public int hashCode()
    {
        return hashCode;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }
        if(o == this)
        {
            return true;
        }
        Collection<ItemStack> items1;
        if(o instanceof StackArray)
        {
            items1 = ((StackArray) o).items;
        }
        else
        {
            items1 = getItems(o);
        }

        if(items1 != null)
        {
            for(ItemStack is1 : items1)
            {
                if(equalsItem(is1))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean equalsItem(ItemStack is)
    {
        if(is == null)
        {
            return false;
        }

        for(ItemStack is1 : items)
        {
            if(itemsEquals(is, is1))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean matches(ItemStack[] ai)
    {
        return ai != null && ai.length == 1 && equalsItem(ai[0]);
    }

    @Override
    public IStackArray[] getItems()
    {
        return array;
    }
}