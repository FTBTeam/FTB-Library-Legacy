package com.feed_the_beast.ftbl.api.client.gui.guibuttons;

import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public abstract class PlayerAction extends ActionButton
{
    public PlayerAction(int p, TextureCoords c)
    {
        super(p, c, null);
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName(ResourceLocation id)
    {
        ITextComponent c = getDisplayNameOverride();
        return (c == null) ? new TextComponentTranslation("player_action." + id) : c;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isVisibleFor(ForgePlayerSP player)
    {
        return !player.equalsPlayer(ForgeWorldSP.inst.clientPlayer);
    }
}