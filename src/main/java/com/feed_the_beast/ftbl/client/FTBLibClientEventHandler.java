package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.EnumSelf;
import com.feed_the_beast.ftbl.api.ForgePlayer;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.client.gui.PlayerAction;
import com.feed_the_beast.ftbl.api.client.gui.PlayerActionRegistry;
import com.feed_the_beast.ftbl.api.item.ODItems;
import com.feed_the_beast.ftbl.util.FTBLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class FTBLibClientEventHandler
{
    public static final FTBLibClientEventHandler instance = new FTBLibClientEventHandler();
    
	/*@SubscribeEvent
    public void onConnected(FMLNetworkEvent.ClientConnectedToServerEvent e)
	{
	}*/

    private static class ButtonInvLM extends GuiButton
    {
        public final PlayerAction action;

        public ButtonInvLM(int id, PlayerAction b, int x, int y)
        {
            super(id, x, y, 16, 16, "");
            action = b;
        }

        @Override
        public void drawButton(Minecraft mc, int mx, int my)
        {
        }
    }

    private static class ButtonInvLMRenderer extends GuiButton
    {
        public final List<ButtonInvLM> buttons;
        private final GuiContainerCreative creativeContainer;
        private Minecraft mc;

        public ButtonInvLMRenderer(int id, GuiScreen g)
        {
            super(id, -1000, -1000, 0, 0, "");
            buttons = new ArrayList<>();
            creativeContainer = (g instanceof GuiContainerCreative) ? (GuiContainerCreative) g : null;
            mc = FTBLibClient.mc();
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
                b.action.render(b.xPosition, b.yPosition, zLevel);

                if(mx >= b.xPosition && my >= b.yPosition && mx < b.xPosition + b.width && my < b.yPosition + b.height)
                {
                    GlStateManager.color(1F, 1F, 1F, 0.3F);
                    GuiLM.drawBlankRect(b.xPosition, b.yPosition, 0D, b.width, b.height);
                    GlStateManager.color(1F, 1F, 1F, 1F);
                }
            }

            for(ButtonInvLM b : buttons)
            {
                b.action.postRender(b.xPosition, b.yPosition, 0D);

                if(mx >= b.xPosition && my >= b.yPosition && mx < b.xPosition + b.width && my < b.yPosition + b.height)
                {
                    GlStateManager.pushMatrix();
                    double mx1 = mx - 4D;
                    double my1 = my - 12D;

                    String s = b.action.getDisplayName();
                    int tw = mc.fontRendererObj.getStringWidth(s);

                    if(!FTBLibModClient.action_buttons_on_top.getAsBoolean())
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
                    GuiLM.drawBlankRect(-3, -2, zLevel, tw + 6, 12);
                    GlStateManager.color(1F, 1F, 1F, 1F);
                    mc.fontRendererObj.drawString(s, 0, 0, 0xFFFFFFFF);
                    GlStateManager.popMatrix();
                }
            }

            GlStateManager.color(1F, 1F, 1F, 1F);
        }
    }

    @SubscribeEvent
    public void onDisconnected(FMLNetworkEvent.ClientDisconnectionFromServerEvent e)
    {
        if(ForgeWorldSP.inst != null)
        {
            ForgeWorldSP.inst.onClosed();
            ForgeWorldSP.inst = null;
        }
    }
    
	/*
    @SubscribeEvent
	public void onKeyEvent(InputEvent.KeyInputEvent e)
	{
		if(Keyboard.getEventKeyState())
		{
			Shortcuts.onKeyPressed(Keyboard.getEventKey());
		}
	}
	*/

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent e)
    {
        if(e.getItemStack() == null || e.getItemStack().getItem() == null)
        {
            return;
        }
        
		/*
        if(FTBLibModClient.item_reg_names.getAsBoolean())
		{
			e.getToolTip().add(LMInvUtils.getRegName(e.getItemStack()).toString());
		}
		*/

        if(FTBLibModClient.item_ore_names.getAsBoolean())
        {
            Collection<String> ores = ODItems.getOreNames(e.getItemStack());

            if(!ores.isEmpty())
            {
                e.getToolTip().add("Ore Dictionary names:");

                for(String or : ores)
                {
                    e.getToolTip().add("> " + or);
                }
            }
        }

        if(FTBLib.ftbu != null)
        {
            FTBLib.ftbu.onTooltip(e);
        }
    }

    @SubscribeEvent
    public void onDrawDebugText(RenderGameOverlayEvent.Text e)
    {
        if(!FTBLibClient.mc().gameSettings.showDebugInfo)
        {
            if(FTBLib.DEV_ENV)
            {
                e.getLeft().add("[MC " + TextFormatting.GOLD + Loader.MC_VERSION + TextFormatting.WHITE + " DevEnv]");
            }
        }
    }

    @SubscribeEvent
    public void guiInitEvent(final GuiScreenEvent.InitGuiEvent.Post e)
    {
        if(ForgeWorldSP.inst == null)
        {
            return;
        }

        if(e.getGui() instanceof InventoryEffectRenderer)
        {
            List<PlayerAction> buttons = PlayerActionRegistry.getPlayerActions(EnumSelf.SELF, ForgeWorldSP.inst.clientPlayer, ForgeWorldSP.inst.clientPlayer, false, false);

            if(!buttons.isEmpty())
            {
                Collections.sort(buttons);

                ButtonInvLMRenderer renderer = new ButtonInvLMRenderer(495830, e.getGui());
                e.getButtonList().add(renderer);

                if(FTBLibModClient.action_buttons_on_top.getAsBoolean())
                {
                    for(int i = 0; i < buttons.size(); i++)
                    {
                        PlayerAction a = buttons.get(i);
                        int x = i % 4;
                        int y = i / 4;
                        ButtonInvLM b = new ButtonInvLM(495830 + i, a, 4 + x * 18, 4 + y * 18);
                        e.getButtonList().add(b);
                        renderer.buttons.add(b);
                    }
                }
                else
                {
                    int xSize = 176;
                    int ySize = 166;
                    int buttonX = -17;
                    int buttonY = 8;

                    if(e.getGui() instanceof GuiContainerCreative)
                    {
                        xSize = 195;
                        ySize = 136;
                        buttonY = 6;
                    }
                    boolean hasPotions = !e.getGui().mc.thePlayer.getActivePotionEffects().isEmpty();
                    if(hasPotions)
                    {
                        buttonX -= 4;
                        buttonY -= 26;
                    }

                    int guiLeft = (e.getGui().width - xSize) / 2;
                    int guiTop = (e.getGui().height - ySize) / 2;

                    if(hasPotions)
                    {
                        guiLeft += 60;
                    }

                    for(int i = 0; i < buttons.size(); i++)
                    {
                        PlayerAction a = buttons.get(i);
                        ButtonInvLM b;

                        if(hasPotions)
                        {
                            int x = i % 8;
                            int y = i / 8;
                            b = new ButtonInvLM(495830 + i, a, guiLeft + buttonX - 18 * x, guiTop + buttonY - y * 18);
                        }
                        else
                        {
                            int x = i / 8;
                            int y = i % 8;
                            b = new ButtonInvLM(495830 + i, a, guiLeft + buttonX - 18 * x, guiTop + buttonY + 18 * y);
                        }

                        e.getButtonList().add(b);
                        renderer.buttons.add(b);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void guiActionEvent(GuiScreenEvent.ActionPerformedEvent.Post e)
    {
        if(e.getButton() instanceof ButtonInvLM)
        {
            PlayerAction b = ((ButtonInvLM) e.getButton()).action;
            ForgePlayer p = ForgeWorldSP.inst.clientPlayer;
            b.onClicked(p, p);
        }
    }
}