package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.api_impl.SharedClientData;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.client.FTBLibClient;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.item.ODItems;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FTBLibClientEventHandler
{
    @SubscribeEvent
    public void onConnected(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        SharedClientData.INSTANCE.reset();
    }

    /* TODO: Close world / destroy cached data
    @SubscribeEvent
    public void onDisconnected(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
    }
    */

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event)
    {
        if(FTBLibClientConfig.ITEM_ORE_NAMES.getBoolean())
        {
            Collection<String> ores = ODItems.getOreNames(event.getItemStack());

            if(!ores.isEmpty())
            {
                event.getToolTip().add("Ore Dictionary names:");

                for(String or : ores)
                {
                    event.getToolTip().add("> " + or);
                }
            }
        }
    }

    /*
    @SubscribeEvent
    public void onDrawDebugText(RenderGameOverlayEvent.Text event)
    {
        if(!Minecraft.getMinecraft().gameSettings.showDebugInfo)
        {
            if(LMUtils.DEV_ENV)
            {
                event.getLeft().add("[MC " + TextFormatting.GOLD + Loader.MC_VERSION + TextFormatting.WHITE + " DevEnv]");
            }
        }

        Minecraft mc = FTBLibClient.mc();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        double width2 = scaledResolution.getScaledWidth_double();
        double height2 = scaledResolution.getScaledHeight_double();

        for(Entity entity : mc.theWorld.loadedEntityList)
        {
            if(entity != mc.thePlayer && entity.getDistanceSqToEntity(mc.thePlayer) <= 256D)
            {
                Vector4f pos = LMFrustumUtils.worldToViewport((float) entity.posX, (float) entity.posY, (float) entity.posZ);

                //if(pos.z >= 0D)
                {
                    //GuiLM.drawBlankRect(width2 + pos.getX() * 30D - 8D, height2 + pos.getY() * 30D - 8D, 0F, 16D, 16D);
                    GuiLM.drawBlankRect(width2 + pos.getX() * width2 - 4D, height2 + pos.getY() * height2 - 4D, 0F, 8D, 8D);

                    event.getRight().add(pos.toString());

                    //System.out.println(pos);
                }
            }
        }
    }
     */

    // Add Sidebar Buttons //

    @SubscribeEvent
    public void guiInitEvent(final GuiScreenEvent.InitGuiEvent.Post event)
    {
        if(event.getGui() instanceof InventoryEffectRenderer)
        {
            List<ISidebarButton> buttons = FTBLibModClient.getSidebarButtons(false);

            if(!buttons.isEmpty())
            {
                ButtonInvLMRenderer renderer = new ButtonInvLMRenderer(495830);
                event.getButtonList().add(renderer);

                if(!LMUtils.isNEILoaded && FTBLibClientConfig.ACTION_BUTTONS_ON_TOP.getBoolean())
                {
                    int i = 0;
                    for(ISidebarButton button : buttons)
                    {
                        int x = i % 4;
                        int y = i / 4;
                        ButtonInvLM b = new ButtonInvLM(495830 + i, button, 4 + x * 18, 4 + y * 18);
                        event.getButtonList().add(b);
                        renderer.buttons.add(b);
                        i++;
                    }
                }
                else
                {
                    int xSize = 176;
                    int ySize = 166;
                    int buttonX = -17;
                    int buttonY = 8;

                    if(event.getGui() instanceof GuiContainerCreative)
                    {
                        xSize = 195;
                        ySize = 136;
                        buttonY = 6;
                    }
                    boolean hasPotions = !event.getGui().mc.thePlayer.getActivePotionEffects().isEmpty();
                    if(hasPotions)
                    {
                        buttonX -= 4;
                        buttonY -= 26;
                    }

                    int guiLeft = (event.getGui().width - xSize) / 2;
                    int guiTop = (event.getGui().height - ySize) / 2;

                    int i = 0;
                    for(ISidebarButton button : buttons)
                    {
                        ButtonInvLM b;

                        if(hasPotions)
                        {
                            int x = i % 8;
                            int y = i / 8;
                            b = new ButtonInvLM(495830 + i, button, guiLeft + buttonX - 18 * x, guiTop + buttonY - y * 18);
                        }
                        else
                        {
                            int x = i / 8;
                            int y = i % 8;
                            b = new ButtonInvLM(495830 + i, button, guiLeft + buttonX - 18 * x, guiTop + buttonY + 18 * y);
                        }

                        event.getButtonList().add(b);
                        renderer.buttons.add(b);
                        i++;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void guiActionEvent(GuiScreenEvent.ActionPerformedEvent.Post event)
    {
        if(event.getButton() instanceof ButtonInvLM)
        {
            (((ButtonInvLM) event.getButton()).button).onClicked(MouseButton.LEFT);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    //public void renderGui(RenderGameOverlayEvent event)
    public void renderGui(TickEvent.RenderTickEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        //if(event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        if(event.phase == TickEvent.Phase.END && mc.theWorld != null && !mc.gameSettings.hideGUI)
        {
            if(ClientNotifications.shouldRenderTemp())
            {
                ClientNotifications.renderTemp(new ScaledResolution(mc));
            }
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event)
    {
        FTBLibClient.updateRenderInfo();
    }

    private static class ButtonInvLM extends GuiButton
    {
        public final ISidebarButton button;
        public final String title;

        public ButtonInvLM(int id, ISidebarButton b, int x, int y)
        {
            super(id, x, y, 16, 16, "");
            button = b;

            ITextComponent c = b.getDisplayNameOverride();
            title = ((c == null) ? new TextComponentTranslation("sidebar_button." + b.getName()) : c).getFormattedText();
        }

        @Override
        public void drawButton(Minecraft mc, int mx, int my)
        {
        }
    }

    private static class ButtonInvLMRenderer extends GuiButton
    {
        public final List<ButtonInvLM> buttons;

        public ButtonInvLMRenderer(int id)
        {
            super(id, -1000, -1000, 0, 0, "");
            buttons = new ArrayList<>();
        }

        @Override
        public void drawButton(Minecraft mc, int mx, int my)
        {
            //if(creativeContainer != null && creativeContainer.getSelectedTabIndex() != CreativeTabs.tabInventory.getTabIndex())
            //	return;

            zLevel = 0F;

            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GlStateManager.color(1F, 1F, 1F, 1F);

            for(ButtonInvLM b : buttons)
            {
                b.button.render(b.xPosition, b.yPosition);

                if(mx >= b.xPosition && my >= b.yPosition && mx < b.xPosition + b.width && my < b.yPosition + b.height)
                {
                    GlStateManager.color(1F, 1F, 1F, 0.3F);
                    GuiHelper.drawBlankRect(b.xPosition, b.yPosition, b.width, b.height);
                    GlStateManager.color(1F, 1F, 1F, 1F);
                }
            }

            for(ButtonInvLM b : buttons)
            {
                b.button.postRender(b.xPosition, b.yPosition);

                if(mx >= b.xPosition && my >= b.yPosition && mx < b.xPosition + b.width && my < b.yPosition + b.height)
                {
                    GlStateManager.pushMatrix();
                    double mx1 = mx - 4D;
                    double my1 = my - 12D;

                    int tw = mc.fontRendererObj.getStringWidth(b.title);

                    if(LMUtils.isNEILoaded || !FTBLibClientConfig.ACTION_BUTTONS_ON_TOP.getBoolean())
                    {
                        mx1 -= tw + 8;
                        my1 += 4;
                    }

                    if(mx1 < 4D)
                    {
                        mx1 = 4D;
                    }
                    if(my1 < 4D)
                    {
                        my1 = 4D;
                    }

                    GlStateManager.translate(mx1, my1, zLevel);

                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GlStateManager.color(0.13F, 0.13F, 0.13F, 1F);
                    GuiHelper.drawBlankRect(-3, -2, tw + 6, 12);
                    GlStateManager.color(1F, 1F, 1F, 1F);
                    mc.fontRendererObj.drawString(b.title, 0, 0, 0xFFFFFFFF);
                    GlStateManager.popMatrix();
                }
            }

            GlStateManager.color(1F, 1F, 1F, 1F);
        }
    }
}