package com.feed_the_beast.ftbl.lib.item;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStackSerializer
{
    public static ItemStack parseItem(String input)
    {
        input = input.trim();
        if(input.isEmpty())
        {
            return ItemStackTools.getEmptyStack();
        }

        String[] s1 = input.split(" ", 4); //TODO: Use split limit

        if(s1.length == 0)
        {
            return ItemStackTools.getEmptyStack();
        }

        Item item = Item.REGISTRY.getObject(new ResourceLocation(s1[0]));

        if(item == null)
        {
            return ItemStackTools.getEmptyStack();
        }

        int stackSize = 1, meta = 0;

        if(s1.length >= 2)
        {
            stackSize = MathHelper.getInt(s1[1], 1);
        }

        if(s1.length >= 3)
        {
            meta = (s1[2].charAt(0) == '*') ? OreDictionary.WILDCARD_VALUE : MathHelper.getInt(s1[2], 0);
        }

        ItemStack itemstack = new ItemStack(item, stackSize, meta);

        if(s1.length >= 4)
        {
            try
            {
                itemstack.setTagCompound(JsonToNBT.getTagFromJson(s1[3]));
            }
            catch(Exception ex)
            {
            }
        }

        return itemstack;
    }

    public static String toString(ItemStack is)
    {
        if(ItemStackTools.isEmpty(is))
        {
            return "";
        }

        String output = Item.REGISTRY.getNameForObject(is.getItem()) + " " + ItemStackTools.getStackSize(is) + ' ' + is.getItemDamage();

        NBTTagCompound tagCompound = is.getTagCompound();
        if (null != tagCompound)
        {
          output += " " + tagCompound.toString();
        }

        return output;
    }

    public static JsonElement serialize(ItemStack is)
    {
        String s = toString(is);
        return s.isEmpty() ? JsonNull.INSTANCE : new JsonPrimitive(s);
    }

    public static ItemStack deserialize(JsonElement e)
    {
        if(e.isJsonNull())
        {
            return ItemStackTools.getEmptyStack();
        }
        else if(e.isJsonPrimitive())
        {
            return parseItem(e.getAsString());
        }
        else
        {
            try
            {
                return ItemStackTools.loadFromNBT(JsonToNBT.getTagFromJson(e.toString()));
            }
            catch(Exception ex)
            {
                return ItemStackTools.getEmptyStack();
            }
        }
    }
}