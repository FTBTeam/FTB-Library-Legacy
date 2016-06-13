package com.feed_the_beast.ftbl.gui.friends;

import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.info.InfoPage;
import com.feed_the_beast.ftbl.api.info.InfoTextLine;
import com.feed_the_beast.ftbl.gui.info.ButtonInfoTextLine;
import com.feed_the_beast.ftbl.gui.info.GuiInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by LatvianModder on 23.03.2016.
 */
@SideOnly(Side.CLIENT)
public class InfoPlayerInventoryLine extends InfoTextLine
{
    public class ButtonInfoPlayerInventory extends ButtonInfoTextLine
    {
        public ButtonInfoPlayerInventory(GuiInfo g, InfoPlayerInventoryLine w)
        {
            super(g, null);
            width = 18 * 9;
            height = 18 * 4 + 4;
        }

        @Override
        public void addMouseOverText(GuiLM gui, List<String> l)
        {
        }

        @Override
        public void onClicked(GuiLM gui, MouseButton button)
        {
        }

        @Override
        public void renderWidget(GuiLM gui)
        {
            double ay = getAY();
            double ax = getAX();

            GlStateManager.color(1F, 1F, 1F, 0.2F);
            GuiLM.drawBlankRect(ax, ay, width, height);

            for(int i = 0; i < 9 * 3; i++)
            {
                GuiLM.drawBlankRect(ax + (i % 9) * 18 + 1, ay + (i / 9) * 18 + 1, 16, 16);
            }

            for(int i = 0; i < 9; i++)
            {
                GuiLM.drawBlankRect(ax + i * 18 + 1, ay + 18 * 3 + 5, 16, 16);
            }

            GlStateManager.color(1F, 1F, 1F, 1F);

            EntityPlayer ep = playerLM.getPlayer();

            if(ep != null)
            {
                for(int i = 0; i < ep.inventory.mainInventory.length - 9; i++)
                {
                    if(ep.inventory.mainInventory[i + 9] != null)
                    {
                        FTBLibClient.renderGuiItem(ep.inventory.mainInventory[i + 9], ax + (i % 9) * 18 + 1, ay + (i / 9) * 18 + 1);
                    }
                }

                for(int i = 0; i < 9; i++)
                {
                    if(ep.inventory.mainInventory[i] != null)
                    {
                        FTBLibClient.renderGuiItem(ep.inventory.mainInventory[i], ax + i * 18 + 1, ay + 18 * 3 + 5);
                    }
                }
            }
        }
    }

    public final ForgePlayerSP playerLM;

    public InfoPlayerInventoryLine(InfoPage c, ForgePlayerSP p)
    {
        super(c, null);
        playerLM = p;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ButtonInfoTextLine createWidget(GuiInfo gui)
    {
        return new ButtonInfoPlayerInventory(gui, this);
    }
}
