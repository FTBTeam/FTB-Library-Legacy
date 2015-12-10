package ftb.lib.mod.client;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.*;
import ftb.lib.api.*;
import ftb.lib.api.config.ClientConfigRegistry;
import ftb.lib.api.gui.*;
import ftb.lib.client.*;
import ftb.lib.gui.GuiLM;
import ftb.lib.mod.FTBUIntegration;
import ftb.lib.mod.client.gui.GuiEditConfig;
import ftb.lib.notification.ClientNotifications;
import latmod.lib.FastList;
import latmod.lib.config.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.client.event.GuiScreenEvent;

@SideOnly(Side.CLIENT)
public class FTBLibGuiEventHandler
{
	public static final FTBLibGuiEventHandler instance = new FTBLibGuiEventHandler();
	private static final FastList<PlayerAction> buttons = new FastList<PlayerAction>();
	
	private static int nextID = 2427180;
	public static final int getNextButtonID()
	{ return ++nextID; }
	
	public static final ConfigGroup sidebar_buttons_config = new ConfigGroup("sidebar_buttons");
	public static final ConfigEntryBool button_settings = new ConfigEntryBool("settings", true).setHidden();
	
	public static final PlayerAction notifications = new PlayerAction(GuiIcons.comment)
	{
		public void onClicked(int playerID)
		{ /* TODO: Make Notifications gui */ }
		
		public String getTitle()
		{ return I18n.format(FTBLibModClient.notifications.getFullID()); }
		
		public void postRender(int ax, int ay, double z)
		{
			String n = String.valueOf(ClientNotifications.Perm.list.size());
			int nw = FTBLibClient.mc.fontRenderer.getStringWidth(n);
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.disableTexture();
			int width = 16;
			GuiLM.drawRect(ax + width - nw, ay - 4, ax + width + 1, ay + 5, 0xAAFF2222);
			GlStateManager.enableTexture();
			FTBLibClient.mc.fontRenderer.drawString(n, ax + width - nw + 1, ay - 3, 0xFFFFFFFF);
		}
	};
	
	public static final PlayerAction settings = new PlayerAction(GuiIcons.settings)
	{
		public void onClicked(int playerID)
		{ FTBLibClient.mc.displayGuiScreen(new GuiEditConfig(FTBLibClient.mc.currentScreen, ClientConfigRegistry.provider)); }
		
		public String getTitle()
		{ return FTBLibLang.client_config(); }
	};
	
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public void guiInitEvent(final GuiScreenEvent.InitGuiEvent.Post e)
	{
		if(!FTBLibClient.isPlaying()) return;
		
		if(e.gui instanceof GuiInventory || e.gui instanceof GuiContainerCreative)
		{
			int xSize = 176;
			int ySize = 166;
			int buttonX = -17;
			int buttonY = 8;
			
			if(e.gui instanceof GuiContainerCreative)
			{
				xSize = 195;
				ySize = 136;
				buttonY = 6;
			}
			boolean hasPotions = !e.gui.mc.thePlayer.getActivePotionEffects().isEmpty();
			if(hasPotions)
			{ buttonX -= 4; buttonY -= 26; }
			
			int guiLeft = (e.gui.width - xSize) / 2;
			int guiTop = (e.gui.height - ySize) / 2;
			
			buttons.clear();
			if(!ClientNotifications.Perm.list.isEmpty()) buttons.add(notifications);
			EventPlayerActionButtons event = new EventPlayerActionButtons((FTBUIntegration.instance == null) ? 0 : FTBUIntegration.instance.getPlayerID(FTBLibClient.mc.thePlayer), true, false);
			event.post();
			buttons.addAll(event.actions);
			if(button_settings.get()) buttons.add(settings);
			
			for(int i = 0; i < buttons.size(); i++)
			{
				if(hasPotions)
					e.buttonList.add(new ButtonInvLM(buttons.get(i), e.gui, guiLeft + buttonX - 18 * i, guiTop + buttonY));
				else
					e.buttonList.add(new ButtonInvLM(buttons.get(i), e.gui, guiLeft + buttonX, guiTop + buttonY + 18 * i));
			}
		}
	}
	
	@SubscribeEvent
	public void guiActionEvent(GuiScreenEvent.ActionPerformedEvent.Post e)
	{
		if(e.button.id == settings.ID)
			e.gui.mc.displayGuiScreen(new GuiEditConfig(e.gui, ClientConfigRegistry.provider));
		else if(buttons.contains(e.button.id))
		{
			final GuiContainerCreative creativeContainer = (e.gui instanceof GuiContainerCreative) ? (GuiContainerCreative)e.gui : null;
			
			if(creativeContainer == null || creativeContainer.func_147056_g() == CreativeTabs.tabInventory.getTabIndex())
			{
				PlayerAction b = buttons.getObj(e.button.id);
				b.onClicked(FTBUIntegration.instance.getPlayerID(e.gui.mc.thePlayer));
			}
		}
	}
	
	private static class ButtonInvLM extends GuiButton
	{
		public final PlayerAction button;
		private final GuiContainerCreative creativeContainer;
		
		public ButtonInvLM(PlayerAction b, GuiScreen g, int x, int y)
		{
			super(b.ID, x, y, 16, 16, "");
			button = b;
			creativeContainer = (g instanceof GuiContainerCreative) ? (GuiContainerCreative)g : null;
		}
		
		public void drawButton(Minecraft mc, int mx, int my)
		{
			if(creativeContainer != null && creativeContainer.func_147056_g() != CreativeTabs.tabInventory.getTabIndex())
				return;
			
			GlStateManager.pushAttrib();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			GlStateManager.color(1F, 1F, 1F, 1F);
			button.render(xPosition, yPosition, 0D);
			
			if(mx >= xPosition && my >= yPosition && mx < xPosition + width && my < yPosition + height)
				GuiLM.drawBlankRect(xPosition, yPosition, 0D, width, height, 0x55FFFFFF);
			
			button.postRender(xPosition, yPosition, 0D);
			
			GlStateManager.popAttrib();
		}
	}
}