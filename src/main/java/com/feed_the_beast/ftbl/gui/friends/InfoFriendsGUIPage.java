package com.feed_the_beast.ftbl.gui.friends;

import com.feed_the_beast.ftbl.api.info.impl.InfoPage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 24.03.2016.
 */
@SideOnly(Side.CLIENT)
public class InfoFriendsGUIPage extends InfoPage
{
    /*
    private class Button extends ButtonInfoPage
    {
        public Button(GuiInfo g, IGuiInfoPageTree p)
        {
            super(g, p, null);
            height = 20;
        }

        @Override
        public void updateTitle(@Nonnull GuiLM gui)
        {
            title = playerLM.getProfile().getName();
            hover = null;

            if(guiInfo.font.getStringWidth(title) > width - 24)
            {
                hover = title + "";
                title = guiInfo.font.trimStringToWidth(title, width - 22) + "...";
            }
        }

        @Override
        public void onClicked(@Nonnull GuiLM gui, @Nonnull MouseButton button)
        {
            new MessageRequestPlayerInfo(playerLM.getProfile().getId()).sendToServer();
            super.onClicked(gui, button);
        }

        @Override
        public void renderWidget(@Nonnull GuiLM gui)
        {
            double ay = getAY();
            double ax = getAX();

            GlStateManager.enableBlend();

            if(gui.isMouseOver(this))
            {
                GlStateManager.color(1F, 1F, 1F, 0.2F);
                GuiLM.drawBlankRect(ax, ay, width, height);
            }

            if(ForgeWorldSP.inst.clientPlayer.getTeam() != null)
            {
                EnumTeamStatus status = ForgeWorldSP.inst.clientPlayer.getTeam().getStatus(playerLM);
                FTBLibClient.setGLColor((status == EnumTeamStatus.NONE) ? 0xFF000000 : status.color.getMapColor().colorValue, 255);
            }
            else
            {
                GlStateManager.color(0F, 0F, 0F, 1F);
            }

            GuiLM.drawBlankRect(ax + 1, ay + 1, 18, 18);

            GlStateManager.color(1F, 1F, 1F, 1F);
            GuiLM.drawPlayerHead(playerLM.getProfile().getName(), ax + 2, ay + 2, 16, 16);

            guiInfo.font.drawString(title, (int) ax + 22, (int) ay + 6, 0xFFFFFFFF);
        }
    }

    public final ForgePlayerSP playerLM;

    public InfoFriendsGUIPage(ForgePlayerSP p)
    {
        playerLM = p;
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

        List<Map.Entry<ResourceLocation, ActionButton>> buttons = SidebarButtonRegistry.getButtons(playerLM, true);
        Collections.sort(buttons, SidebarButtonRegistry.COMPARATOR);

        for(Map.Entry<ResourceLocation, ActionButton> entry : buttons)
        {
            println(new InfoPlayerActionLine(playerLM, entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public ButtonInfoPage createButton(GuiInfo gui, IGuiInfoPageTree p)
    {
        return new Button(gui, p);
    }
    */
}
