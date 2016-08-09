package com.feed_the_beast.ftbl.gui.friends;

import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.ActionButton;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.ActionButtonRegistry;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

        List<Map.Entry<ResourceLocation, ActionButton>> buttons = ActionButtonRegistry.getButtons(ForgeWorldSP.inst.clientPlayer, true);
        Collections.sort(buttons, ActionButtonRegistry.COMPARATOR);

        for(Map.Entry<ResourceLocation, ActionButton> entry : buttons)
        {
            println(new InfoPlayerActionLine(playerLM, entry.getKey(), entry.getValue()));
        }
    }
}