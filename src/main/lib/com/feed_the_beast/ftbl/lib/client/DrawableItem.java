package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.item.ItemStackSerializer;
import com.feed_the_beast.ftbl.lib.util.InvUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 24.02.2017.
 */
public class DrawableItem implements IDrawableObject
{
    public ItemStack stack;

    public DrawableItem(@Nullable ItemStack is)
    {
        stack = is;
    }

    @Override
    public void draw(int x, int y, int w, int h, Color4I col)
    {
        if(!GuiHelper.drawItem(Minecraft.getMinecraft().getRenderItem(), stack, x, y, w / 16D, h / 16D, true))
        {
            stack = InvUtils.ERROR_ITEM;
        }
    }

    @Override
    public JsonElement getJson()
    {
        return new JsonPrimitive("item:" + ItemStackSerializer.toString(stack));
    }
}