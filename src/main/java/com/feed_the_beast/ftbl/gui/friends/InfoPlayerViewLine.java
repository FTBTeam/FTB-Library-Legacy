package com.feed_the_beast.ftbl.gui.friends;

import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.info.InfoPage;
import com.feed_the_beast.ftbl.api.info.InfoTextLine;
import com.feed_the_beast.ftbl.gui.info.ButtonInfoTextLine;
import com.feed_the_beast.ftbl.gui.info.GuiInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Created by LatvianModder on 23.03.2016.
 */
@SideOnly(Side.CLIENT)
public class InfoPlayerViewLine extends InfoTextLine
{
    public class ButtonInfoPlayerView extends ButtonInfoTextLine
    {
        public class Player extends AbstractClientPlayer
        {
            public Player(ForgePlayerSP p)
            {
                super(Minecraft.getMinecraft().theWorld, p.getProfile());
            }

            @Override
            public boolean equals(Object o)
            {
                return playerLM.equals(o);
            }

            @Override
            public void addChatMessage(ITextComponent i)
            {
            }

            @Override
            public boolean canCommandSenderUseCommand(int i, String s)
            {
                return false;
            }

            @Nonnull
            @Override
            public BlockPos getPosition()
            {
                return BlockPos.ORIGIN;
            }

            @Override
            public boolean isInvisibleToPlayer(@Nonnull EntityPlayer ep)
            {
                return true;
            }

            @Nonnull
            @Override
            public ResourceLocation getLocationSkin()
            {
                return playerLM.getSkin();
            }

            //FIXME: Cape
            //@Override
            //public boolean hasCape()
            //{ return false; }

            @Override
            public ResourceLocation getLocationCape()
            {
                return null;
            }
        }

        private Player player;

        public ButtonInfoPlayerView(GuiInfo g, InfoPlayerViewLine w)
        {
            super(g, null);
            height = 1;
        }

        @Override
        public void renderWidget(GuiLM gui)
        {
            double ay = getAY();
            //double ax = getAX();

            if(player == null)
            {
                player = new Player(ForgeWorldSP.inst.clientPlayer);
            }

            if(gui.isMouseOver(this) && Mouse.isButtonDown(1))
            {
                for(int i = 0; i < player.inventory.armorInventory.length; i++)
                {
                    player.inventory.armorInventory[i] = null;
                }
            }
            else
            {
                EntityPlayer ep1 = playerLM.getPlayer();

                if(ep1 != null)
                {
                    System.arraycopy(ep1.inventory.mainInventory, 0, player.inventory.mainInventory, 0, player.inventory.mainInventory.length);
                    System.arraycopy(ep1.inventory.armorInventory, 0, player.inventory.armorInventory, 0, player.inventory.armorInventory.length);
                    player.inventory.currentItem = ep1.inventory.currentItem;
                }
                else
                {
                    player.inventory.clear();

                    for(Map.Entry<EntityEquipmentSlot, ItemStack> e : playerLM.lastArmor.entrySet())
                    {
                        player.setItemStackToSlot(e.getKey(), e.getValue());
                    }
                }
            }

            GlStateManager.pushMatrix();

            double pheight = 120D;
            double pwidth = pheight / 1.625D;

            double playerX = guiInfo.width - pwidth / 2 - 30;
            double playerY = ay + pheight + 10;

            pheight = pheight / 2;
            pwidth = pheight / 1.625D;

            FTBLibClient.setTexture(player.getLocationSkin());
            GlStateManager.translate(0F, 0F, 100F);
            GlStateManager.color(1F, 1F, 1F, 1F);
            GuiInventory.drawEntityOnScreen((int) playerX, (int) playerY, (int) pheight, (float) playerX - guiInfo.mouseX, (float) (playerY - (pheight + pwidth) - guiInfo.mouseY), player);
            GlStateManager.color(1F, 1F, 1F, 1F);
            GlStateManager.popMatrix();
        }
    }

    public final ForgePlayerSP playerLM;

    public InfoPlayerViewLine(InfoPage c, ForgePlayerSP p)
    {
        super(c, null);
        playerLM = p;
    }

    @Override
    public ButtonInfoTextLine createWidget(GuiInfo gui)
    {
        return new ButtonInfoPlayerView(gui, this);
    }
}
