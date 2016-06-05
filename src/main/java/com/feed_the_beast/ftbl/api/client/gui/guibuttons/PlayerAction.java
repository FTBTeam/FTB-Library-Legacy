package com.feed_the_beast.ftbl.api.client.gui.guibuttons;

import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraft.util.ResourceLocation;

public abstract class PlayerAction extends ActionButton
{
    public PlayerAction(ResourceLocation id, int p, TextureCoords c)
    {
        super(id, p, c, null);
    }

    @Override
    public String getLangKey()
    {
        return "player_action." + getID();
    }

    @Override
    public boolean isVisibleFor(ForgePlayerSP player)
    {
        return !player.equalsPlayer(ForgeWorldSP.inst.clientPlayer);
    }
}