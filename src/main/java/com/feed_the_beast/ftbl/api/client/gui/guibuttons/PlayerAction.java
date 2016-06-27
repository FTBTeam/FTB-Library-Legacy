package com.feed_the_beast.ftbl.api.client.gui.guibuttons;

import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class PlayerAction extends ActionButton
{
    public PlayerAction(ResourceLocation id, int p, TextureCoords c)
    {
        super(id, p, c, null);
    }

    @Override
    protected ITextComponent getDisplayName()
    {
        return new TextComponentTranslation("player_action." + getID());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isVisibleFor(ForgePlayerSP player)
    {
        return !player.equalsPlayer(ForgeWorldSP.inst.clientPlayer);
    }
}