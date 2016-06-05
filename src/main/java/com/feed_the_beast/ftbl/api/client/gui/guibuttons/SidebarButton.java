package com.feed_the_beast.ftbl.api.client.gui.guibuttons;

import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraft.util.ResourceLocation;

public abstract class SidebarButton extends ActionButton
{
    public SidebarButton(ResourceLocation id, int p, TextureCoords c, Boolean b)
    {
        super(id, p, c, b);
    }

    @Override
    public String getLangKey()
    {
        return "sidebar_button." + getID();
    }

    @Override
    public boolean isVisibleFor(ForgePlayerSP player)
    {
        return player.equalsPlayer(ForgeWorldSP.inst.clientPlayer);
    }
}