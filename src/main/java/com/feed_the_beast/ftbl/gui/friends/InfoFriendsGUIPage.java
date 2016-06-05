package com.feed_the_beast.ftbl.gui.friends;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.ActionButton;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.ActionButtonRegistry;
import com.feed_the_beast.ftbl.api.info.InfoPage;
import com.feed_the_beast.ftbl.gui.info.ButtonInfoPage;
import com.feed_the_beast.ftbl.gui.info.GuiInfo;
import com.feed_the_beast.ftbl.net.MessageRequestPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 24.03.2016.
 */
@SideOnly(Side.CLIENT)
public class InfoFriendsGUIPage extends InfoPage
{
    private class Button extends ButtonInfoPage
    {
        public Button(GuiInfo g, InfoFriendsGUIPage p)
        {
            super(g, p, null);
            height = 20;
        }

        @Override
        public void updateTitle()
        {
            title = playerLM.getProfile().getName();
            hover = null;

            if(gui.getFontRenderer().getStringWidth(title) > width - 24)
            {
                hover = title + "";
                title = gui.getFontRenderer().trimStringToWidth(title, width - 22) + "...";
            }
        }

        @Override
        public void onClicked(MouseButton button)
        {
            new MessageRequestPlayerInfo(playerLM.getProfile().getId()).sendToServer();
            super.onClicked(button);
        }

        @Override
        public void renderWidget()
        {
            int ay = getAY();
            int ax = getAX();

            double z = gui.getZLevel();

            if(mouseOver())
            {
                GlStateManager.color(1F, 1F, 1F, 0.2F);
                GuiLM.drawBlankRect(ax, ay, z, width, height);
            }

            if(ForgeWorldSP.inst.clientPlayer.hasTeam())
            {
                EnumTeamStatus status = ForgeWorldSP.inst.clientPlayer.getTeam().getStatus(playerLM);
                FTBLibClient.setGLColor(status.color.getMapColor().colorValue, 255);
            }
            else
            {
                GlStateManager.color(0F, 0F, 0F, 1F);
            }

            GuiLM.drawBlankRect(ax + 1, ay + 1, z, 18, 18);

            GlStateManager.color(1F, 1F, 1F, 1F);
            GuiLM.drawPlayerHead(playerLM.getProfile().getName(), ax + 2, ay + 2, 16, 16, z);

            gui.getFontRenderer().drawString(title, ax + 22, ay + 6, playerLM.isOnline() ? 0xFF11FF11 : 0xFFFFFFFF);
        }
    }

    public final ForgePlayerSP playerLM;

    public InfoFriendsGUIPage(ForgePlayerSP p)
    {
        super(p.getProfile().getName());
        playerLM = p;
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

        for(ActionButton a : ActionButtonRegistry.getButtons(playerLM, true, true))
        {
            text.add(new InfoPlayerActionLine(this, playerLM, a));
        }
        
		/*
        if(LMWorldClient.inst.clientPlayer.isFriend(playerLM))
		{
			text.add(null);
			text.add(new InfoPlayerInventoryLine(this, playerLM));
		}
		*/
    }

    @Override
    public ButtonInfoPage createButton(GuiInfo gui)
    {
        return new Button(gui, this);
    }
}
