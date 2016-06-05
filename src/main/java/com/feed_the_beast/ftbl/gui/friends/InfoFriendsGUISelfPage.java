package com.feed_the_beast.ftbl.gui.friends;

import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.ActionButton;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.ActionButtonRegistry;
import com.feed_the_beast.ftbl.gui.info.GuiInfo;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 24.03.2016.
 */
@SideOnly(Side.CLIENT)
public class InfoFriendsGUISelfPage extends InfoFriendsGUIPage
{
    public InfoFriendsGUISelfPage()
    {
        super(ForgeWorldSP.inst.clientPlayer);
    }

    @Override
    public void refreshGui(GuiInfo gui)
    {
        clear();

        text.add(new InfoPlayerViewLine(this, playerLM));

        if(!playerLM.clientInfo.isEmpty())
        {
            for(ITextComponent s : playerLM.clientInfo)
            {
                println(s);
            }

            text.add(null);
        }

        for(ActionButton a : ActionButtonRegistry.getButtons(ForgeWorldSP.inst.clientPlayer, true, true))
        {
            text.add(new InfoPlayerActionLine(this, playerLM, a));
        }
    }
}