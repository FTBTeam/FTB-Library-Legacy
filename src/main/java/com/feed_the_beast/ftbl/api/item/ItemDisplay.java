package com.feed_the_beast.ftbl.api.item;

import com.feed_the_beast.ftbl.util.LMNBTUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;

import java.util.Collections;
import java.util.List;

public class ItemDisplay
{
    public final ItemStack item;
    public final String title;
    public final List<String> desc;
    public final float scale;
    
    public ItemDisplay(ItemStack is, String t, List<String> d, float s)
    {
        item = (is == null) ? new ItemStack(Blocks.STONE) : is;
        title = (t == null) ? "" : t;
        desc = (d == null) ? Collections.emptyList() : d;
        scale = MathHelper.clamp_float(s, 1F, 8F);
    }
    
    public void writeToNBT(NBTTagCompound tag)
    {
        NBTTagCompound tag1 = new NBTTagCompound();
        item.writeToNBT(tag1);
        tag.setTag("I", tag1);
        tag.setString("T", title);
        tag.setTag("D", LMNBTUtils.fromStringList(desc));
        tag.setFloat("S", scale);
    }
    
    public static ItemDisplay readFromNBT(NBTTagCompound tag)
    { return new ItemDisplay(ItemStack.loadItemStackFromNBT(tag.getCompoundTag("I")), tag.getString("T"), LMNBTUtils.toStringList(tag.getTagList("D", Constants.NBT.TAG_STRING)), tag.getFloat("S")); }
}