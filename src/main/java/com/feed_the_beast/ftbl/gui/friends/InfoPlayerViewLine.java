package com.feed_the_beast.ftbl.gui.friends;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.api.info.impl.ButtonInfoTextLine;
import com.feed_the_beast.ftbl.api.info.impl.EmptyInfoPageLine;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

/**
 * Created by LatvianModder on 23.03.2016.
 */
@SideOnly(Side.CLIENT)
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
            height = 1;
        }

        @Override
        public void renderWidget(GuiLM gui)
        {
            double ay = getAY();
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

            double pheight = 120D;
            double pwidth = pheight / 1.625D;

            double playerX = gui.width - pwidth / 2 - 30;
            double playerY = ay + pheight + 10;

            pheight = pheight / 2;
            pwidth = pheight / 1.625D;

            FTBLibClient.setTexture(player.getLocationSkin());
            GlStateManager.translate(0F, 0F, 100F);
            GlStateManager.color(1F, 1F, 1F, 1F);
            GuiInventory.drawEntityOnScreen((int) playerX, (int) playerY, (int) pheight, (float) playerX - gui.mouseX, (float) (playerY - (pheight + pwidth) - gui.mouseY), player);
            GlStateManager.color(1F, 1F, 1F, 1F);
            GlStateManager.popMatrix();
        }

        @Override
        public boolean shouldRender(GuiLM gui)
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
