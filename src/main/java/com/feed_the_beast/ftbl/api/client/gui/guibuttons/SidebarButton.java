package com.feed_the_beast.ftbl.api.client.gui.guibuttons;

import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;

public abstract class SidebarButton extends ActionButton
{
    public SidebarButton(int p, TextureCoords c, Boolean b)
    {
        super(p, c, b);
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName(ResourceLocation id)
    {
        ITextComponent c = getDisplayNameOverride();
        return (c == null) ? new TextComponentTranslation("sidebar_button." + id) : c;
    }

    @Override
    public boolean isVisibleFor(ForgePlayerSP player)
    {
        return ForgeWorldSP.inst == null || ForgeWorldSP.inst.clientPlayer == null || player.equalsPlayer(ForgeWorldSP.inst.clientPlayer);
    }
}