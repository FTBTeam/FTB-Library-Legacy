package com.feed_the_beast.ftbl.gui.friends;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 24.03.2016.
 */
@SideOnly(Side.CLIENT)
public class InfoFriendsGUISelfPage extends InfoFriendsGUIPage
{
    /*
    public InfoFriendsGUISelfPage()
    {
        super(ForgeWorldSP.inst.clientPlayer);
    }

    @Override
    public void refreshGui(@Nonnull GuiInfo gui)
    {
        clear();

        println(new InfoPlayerViewLine(playerLM));

        if(!playerLM.clientInfo.isEmpty())
        {
            for(ITextComponent s : playerLM.clientInfo)
            {
                println(s);
            }

            println(null);
        }

        List<Map.Entry<ResourceLocation, ActionButton>> buttons = SidebarButtonRegistry.getButtons(ForgeWorldSP.inst.clientPlayer, true);
        Collections.sort(buttons, SidebarButtonRegistry.COMPARATOR);

        for(Map.Entry<ResourceLocation, ActionButton> entry : buttons)
        {
            println(new InfoPlayerActionLine(playerLM, entry.getKey(), entry.getValue()));
        }
    }
    */
}