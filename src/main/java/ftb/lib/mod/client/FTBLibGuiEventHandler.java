package ftb.lib.mod.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.*;
import ftb.lib.*;
import ftb.lib.api.*;
import ftb.lib.api.config.ClientConfigRegistry;
import ftb.lib.api.gui.GuiIcons;
import ftb.lib.client.*;
import ftb.lib.gui.GuiLM;
import ftb.lib.mod.FTBLibFinals;
import ftb.lib.mod.client.gui.*;
import ftb.lib.notification.ClientNotifications;
import latmod.lib.config.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.client.event.GuiScreenEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class FTBLibGuiEventHandler
{
	public static final FTBLibGuiEventHandler instance = new FTBLibGuiEventHandler();
	private static final ArrayList<PlayerAction> buttons = new ArrayList<>();
	
	public static final ConfigGroup sidebar_buttons_config = new ConfigGroup("sidebar_buttons");
	public static final ConfigEntryBool button_settings = new ConfigEntryBool("settings", true).setHidden();
	
	public static final PlayerAction notifications = new PlayerAction("ftbl.notifications", GuiIcons.comment)
	{
		public void onClicked(int playerID)
		{ FTBLibClient.mc.displayGuiScreen(new GuiNotifications(FTBLibClient.mc.currentScreen)); }
		
		public String getTitleKey()
		{ return FTBLibModClient.notifications.getFullID(); }
		
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
	
	public static final PlayerAction settings = new PlayerAction("ftbl.settings", GuiIcons.settings)
	{
		public void onClicked(int playerID)
		{ FTBLibClient.mc.displayGuiScreen(new GuiEditConfig(FTBLibClient.mc.currentScreen, ClientConfigRegistry.provider)); }
		
		public String getTitleKey()
		{ return "client_config"; }
	};

	public static final PlayerAction dev_console = new PlayerAction("ftbl.dev_console", GuiIcons.bug)
	{
		public void onClicked(int playerID)
		{ DevConsole.open(); }

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
			{ buttonX -= 4; buttonY -= 26; }
			
			int guiLeft = (e.gui.width - xSize) / 2;
			int guiTop = (e.gui.height - ySize) / 2;
			
			buttons.clear();
			if(!ClientNotifications.Perm.list.isEmpty()) buttons.add(notifications);
			EventPlayerActionButtons event = new EventPlayerActionButtons((FTBLib.ftbu == null) ? 0 : FTBLib.ftbu.getPlayerID(FTBLibClient.mc.thePlayer), true, false);
			event.post();
			for(PlayerAction a : event.actions) buttons.add(a);
			if(button_settings.get()) buttons.add(settings);
			if(FTBLibFinals.DEV || FTBLib.userIsLatvianModder) buttons.add(dev_console);

			int i = -1;
			for(PlayerAction a : buttons)
			{
				i++;
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
			final GuiContainerCreative creativeContainer = (e.gui instanceof GuiContainerCreative) ? (GuiContainerCreative)e.gui : null;
			
			if(creativeContainer == null || creativeContainer.func_147056_g() == CreativeTabs.tabInventory.getTabIndex())
			{
				PlayerAction b = ((ButtonInvLM)e.button).action;
				b.onClicked(FTBLib.ftbu.getPlayerID(e.gui.mc.thePlayer));
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
			action.render(xPosition, yPosition, 0D);
			
			if(mx >= xPosition && my >= yPosition && mx < xPosition + width && my < yPosition + height)
				GuiLM.drawBlankRect(xPosition, yPosition, 0D, width, height, 0x55FFFFFF);
			
			action.postRender(xPosition, yPosition, 0D);
			
			GlStateManager.popAttrib();
		}
	}
}