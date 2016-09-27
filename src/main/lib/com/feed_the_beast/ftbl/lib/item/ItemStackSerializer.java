package com.feed_the_beast.ftbl.lib.item;

import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStackSerializer
{
    private static String getParseRegex(String s)
    {
        if(s.indexOf(' ') != -1)
        {
            return " ";
        }
        else if(s.indexOf(';') != -1)
        {
            return ";";
        }
        else if(s.indexOf('@') != -1)
        {
            return "@";
        }
        else
        {
            return " x ";
        }
    }

    public static ItemStack parseItem(String s)
    {
        if(s == null)
        {
            return null;
        }

        s = s.trim();
        if(s.isEmpty())
        {
            return null;
        }

        String[] s1 = s.split(getParseRegex(s));

        if(s1.length == 0)
        {
            return null;
        }

        String itemID = s1[0];
        int dmg = 0;
        int size = 1;
        String nbt = null;

        if(s1.length > 1)
        {
            size = Integer.parseInt(s1[1]);
        }

        if(s1.length > 2)
        {
            dmg = (s1[2].charAt(0) == '*') ? OreDictionary.WILDCARD_VALUE : Integer.parseInt(s1[2]);
        }

        if(s1.length > 3)
        {
            nbt = LMStringUtils.unsplitSpaceUntilEnd(3, s1);
        }

        return GameRegistry.makeItemStack(itemID, size, dmg, nbt);
    }

    public static String toString(ItemStack is)
    {
        return (is == null) ? null : Item.REGISTRY.getNameForObject(is.getItem()).toString() + is + ' ' + is.stackSize + ' ' + is.getItemDamage();
    }

    public static JsonElement serialize(ItemStack is)
    {
        return (is == null) ? JsonNull.INSTANCE : new JsonPrimitive(toString(is));
    }

    public static ItemStack deserialize(JsonElement e)
    {
        if(e == null || e.isJsonNull())
        {
            return null;
        }
        else if(e.isJsonPrimitive())
        {
            return parseItem(e.getAsString());
        }
        else
        {
            try
            {
                NBTTagCompound nbt = JsonToNBT.getTagFromJson(e.toString());
                return ItemStack.loadItemStackFromNBT(nbt);
            }
            catch(Exception ex)
            {
                return null;
            }
        }
    }
}