package ftb.lib.mod.client;

import ftb.lib.DevConsole;
import ftb.lib.api.*;
import ftb.lib.api.config.*;
import ftb.lib.api.gui.*;
import ftb.lib.client.FTBLibClient;
import ftb.lib.gui.GuiLM;
import ftb.lib.mod.client.gui.*;
import ftb.lib.notification.ClientNotifications;
import latmod.lib.config.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class FTBLibGuiEventHandler
{
	public static final FTBLibGuiEventHandler instance = new FTBLibGuiEventHandler();
	
	public static final ConfigGroup sidebar_buttons_config = new ConfigGroup("sidebar_buttons");
	public static final ConfigEntryBool button_settings = new ConfigEntryBool("settings", true).setHidden();
	
	public static final PlayerAction notifications = new PlayerAction(PlayerAction.Type.SELF, "ftbl.notifications", 1000, GuiIcons.comment)
	{
		public void onClicked(ILMPlayer self, ILMPlayer other)
		{ FTBLibClient.mc.displayGuiScreen(new GuiNotifications(FTBLibClient.mc.currentScreen)); }
		
		public String getTitleKey()
		{ return FTBLibModClient.notifications.getFullID(); }
		
		public boolean isVisibleFor(ILMPlayer self, ILMPlayer other)
		{ return !ClientNotifications.Perm.list.isEmpty(); }
		
		public void postRender(int ax, int ay, double z)
		{
			String n = String.valueOf(ClientNotifications.Perm.list.size());
			int nw = FTBLibClient.mc.fontRendererObj.getStringWidth(n);
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.disableTexture2D();
			int width = 16;
			GuiLM.drawRect(ax + width - nw, ay - 4, ax + width + 1, ay + 5, 0xAAFF2222);
			GlStateManager.enableTexture2D();
			FTBLibClient.mc.fontRendererObj.drawString(n, ax + width - nw + 1, ay - 3, 0xFFFFFFFF);
		}
	};
	
	public static final PlayerAction settings = new PlayerAction(PlayerAction.Type.SELF, "ftbl.settings", -1000, GuiIcons.settings)
	{
		public void onClicked(ILMPlayer self, ILMPlayer other)
		{ FTBLibClient.mc.displayGuiScreen(new GuiEditConfig(FTBLibClient.mc.currentScreen, ClientConfigRegistry.provider())); }
		
		public String getTitleKey()
		{ return "client_config"; }
	};
	
	public static final PlayerAction dev_console = new PlayerAction(PlayerAction.Type.SELF, "ftbl.dev_console", -2000, GuiIcons.bug)
	{
		public void onClicked(ILMPlayer self, ILMPlayer other)
		{
			if(DevConsole.enabled())
			{
				DevConsole.open();
				
				DevConsole.Tree tree = new DevConsole.Tree();
				
				tree.set("synced", ConfigRegistry.synced);
				
				for(IConfigFile f : ConfigRegistry.map.values())
				{
					tree.set(f.getGroup().ID, f.getGroup().getPrettyJsonString(true));
				}
				
				DevConsole.text.set("Config", tree);
			}
		}
		
		public boolean isVisibleFor(ILMPlayer self, ILMPlayer other)
		{ return DevConsole.enabled(); }
		
		public String getTitleKey()
		{ return "dev_console"; }
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
			{
				buttonX -= 4;
				buttonY -= 26;
			}
			
			int guiLeft = (e.gui.width - xSize) / 2;
			int guiTop = (e.gui.height - ySize) / 2;
			
			ILMPlayer p = FTBLibClient.getClientLMPlayer();
			List<PlayerAction> buttons = PlayerActionRegistry.getPlayerActions(PlayerAction.Type.SELF, p, p, true);
			
			for(int i = 0; i < buttons.size(); i++)
			{
				PlayerAction a = buttons.get(i);
				if(hasPotions)
					e.buttonList.add(new ButtonInvLM(495830 + i, a, e.gui, guiLeft + buttonX - 18 * i, guiTop + buttonY));
				else
					e.buttonList.add(new ButtonInvLM(495830 + i, a, e.gui, guiLeft + buttonX, guiTop + buttonY + 18 * i));
			}
		}
	}
	
	@SubscribeEvent
	public void guiActionEvent(GuiScreenEvent.ActionPerformedEvent.Post e)
	{
		if(e.button instanceof ButtonInvLM)
		{
			final GuiContainerCreative creativeContainer = (e.gui instanceof GuiContainerCreative) ? (GuiContainerCreative) e.gui : null;
			
			if(creativeContainer == null || creativeContainer.getSelectedTabIndex() == CreativeTabs.tabInventory.getTabIndex())
			{
				PlayerAction b = ((ButtonInvLM) e.button).action;
				ILMPlayer p = FTBLibClient.getClientLMPlayer();
				b.onClicked(p, p);
			}
		}
	}
	
	private static class ButtonInvLM extends GuiButton
	{
		public final PlayerAction action;
		private final GuiContainerCreative creativeContainer;
		
		public ButtonInvLM(int id, PlayerAction b, GuiScreen g, int x, int y)
		{
			super(id, x, y, 16, 16, "");
			action = b;
			creativeContainer = (g instanceof GuiContainerCreative) ? (GuiContainerCreative) g : null;
		}
		
		public void drawButton(Minecraft mc, int mx, int my)
		{
			if(creativeContainer != null && creativeContainer.getSelectedTabIndex() != CreativeTabs.tabInventory.getTabIndex())
				return;
			
			GlStateManager.pushAttrib();
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			GlStateManager.color(1F, 1F, 1F, 1F);
			action.render(xPosition, yPosition, 0D);
			
			if(mx >= xPosition && my >= yPosition && mx < xPosition + width && my < yPosition + height)
			{
				GlStateManager.color(1F, 1F, 1F, 0.3F);
				GuiLM.drawBlankRect(xPosition, yPosition, 0D, width, height);
				GlStateManager.color(1F, 1F, 1F, 1F);
			}
			
			action.postRender(xPosition, yPosition, 0D);
			GlStateManager.color(1F, 1F, 1F, 1F);
			
			GlStateManager.popAttrib();
		}
	}
}