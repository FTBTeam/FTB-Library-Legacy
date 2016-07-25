package com.feed_the_beast.ftbl.api.client.gui.guibuttons;

import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ActionButton
{
    public final int priority;
    public final TextureCoords icon;
    public final Boolean configDefault;

    public ActionButton(int p, TextureCoords c, Boolean b)
    {
        priority = p;
        icon = c;
        configDefault = b;
    }

    @Nullable
    protected ITextComponent getDisplayNameOverride()
    {
        return null;
    }

    @Nonnull
    public abstract ITextComponent getDisplayName(ResourceLocation id);

    @SideOnly(Side.CLIENT)
    public abstract void onClicked(ForgePlayerSP player);

    @SideOnly(Side.CLIENT)
    public boolean isVisibleFor(ForgePlayerSP player)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void render(Minecraft mc, double ax, double ay)
    {
        GuiLM.render(icon, ax, ay, 16D, 16D);
    }

    @SideOnly(Side.CLIENT)
    public void postRender(Minecraft mc, double ax, double ay)
    {
    }
}