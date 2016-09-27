package com.feed_the_beast.ftbl.gui.friends;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import com.feed_the_beast.ftbl.lib.gui.ButtonLM;
import com.feed_the_beast.ftbl.lib.info.ButtonInfoTextLine;
import com.feed_the_beast.ftbl.lib.info.EmptyInfoPageLine;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.input.Mouse;

/**
 * Created by LatvianModder on 23.03.2016.
 */
public class InfoPlayerViewLine extends EmptyInfoPageLine
{
    public class ButtonInfoPlayerView extends ButtonInfoTextLine
    {
        public class Player extends AbstractClientPlayer
        {
            public Player(GameProfile profile)
            {
                super(Minecraft.getMinecraft().theWorld, profile);
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

            @Override
            public BlockPos getPosition()
            {
                return BlockPos.ORIGIN;
            }

            @Override
            public boolean isInvisibleToPlayer(EntityPlayer ep)
            {
                return true;
            }

            @Override
            public ResourceLocation getLocationCape()
            {
                return null;
            }
        }

        private Player player;

        public ButtonInfoPlayerView(GuiInfo g)
        {
            super(g, null);
            setHeight(1);
        }

        @Override
        public void renderWidget(IGui gui)
        {
            int ay = getAY();
            //double ax = getAX();

            if(player == null)
            {
                player = new Player(Minecraft.getMinecraft().getSession().getProfile());
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
                /*
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
                */
            }

            GlStateManager.pushMatrix();

            int pheight = 120;
            int pwidth = (int) (pheight / 1.625D);

            int playerX = gui.getWidth() - pwidth / 2 - 30;
            int playerY = ay + pheight + 10;

            pheight = pheight / 2;
            pwidth = (int) (pheight / 1.625D);

            FTBLibClient.setTexture(player.getLocationSkin());
            GlStateManager.translate(0F, 0F, 100F);
            GlStateManager.color(1F, 1F, 1F, 1F);
            GuiInventory.drawEntityOnScreen(playerX, playerY, pheight, playerX - gui.getMouseX(), playerY - (pheight + pwidth) - gui.getMouseY(), player);
            GlStateManager.color(1F, 1F, 1F, 1F);
            GlStateManager.popMatrix();
        }

        @Override
        public boolean shouldRender(IGui gui)
        {
            return true;
        }
    }

    @Override
    public ButtonLM createWidget(GuiInfo gui, IGuiInfoPage page)
    {
        return new ButtonInfoPlayerView(gui);
    }
}
