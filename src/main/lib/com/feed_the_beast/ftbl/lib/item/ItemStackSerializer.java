package com.feed_the_beast.ftbl.lib.item;

import com.feed_the_beast.ftbl.lib.util.StringUtils;
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

import javax.annotation.Nullable;

public class ItemStackSerializer
{
    @Nullable
    public static ItemStack parseItem(String input)
    {
        input = input.trim();
        if(input.isEmpty())
        {
            return ItemStackTools.getEmptyStack();
        }

        String[] s1 = input.split(" ");

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

        if(s1.length > 3)
        {
            try
            {
                itemstack.setTagCompound(JsonToNBT.getTagFromJson(StringUtils.joinSpaceUntilEnd(3, s1)));
            }
            catch(Exception ex)
            {
            }
        }

        return itemstack;
    }

    public static String toString(@Nullable ItemStack is)
    {
        int s = ItemStackTools.getStackSize(is);
        return (s == 0) ? "" : Item.REGISTRY.getNameForObject(is.getItem()) + " " + s + ' ' + is.getItemDamage();
    }

    public static JsonElement serialize(@Nullable ItemStack is)
    {
        if(ItemStackTools.getStackSize(is) == 0)
        {
            return JsonNull.INSTANCE;
        }

        String s = toString(is);
        return s.isEmpty() ? JsonNull.INSTANCE : new JsonPrimitive(s);
    }

    @Nullable
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