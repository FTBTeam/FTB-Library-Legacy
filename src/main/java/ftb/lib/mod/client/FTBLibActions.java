package ftb.lib.mod.client;

import ftb.lib.*;
import ftb.lib.api.PlayerAction;
import ftb.lib.api.client.*;
import ftb.lib.api.config.*;
import ftb.lib.api.friends.ILMPlayer;
import ftb.lib.api.gui.*;
import ftb.lib.mod.FTBLibMod;
import ftb.lib.mod.client.gui.*;
import ftb.lib.notification.ClientNotifications;
import latmod.lib.config.IConfigFile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.opengl.GL11;

import java.util.*;

@SideOnly(Side.CLIENT)
public class FTBLibActions
{
	public static void init()
	{
		EventBusHelper.register(new FTBLibActions());
		
		PlayerActionRegistry.add(notifications);
		PlayerActionRegistry.add(settings);
		PlayerActionRegistry.add(dev_console);
		PlayerActionRegistry.add(toggle_gamemode);
		PlayerActionRegistry.add(toggle_rain);
		PlayerActionRegistry.add(toggle_day);
		PlayerActionRegistry.add(toggle_night);
		
		GuiScreenRegistry.register("notifications", new GuiScreenRegistry.Entry()
		{
			public GuiScreen openGui(EntityPlayer ep)
			{ return new GuiNotifications(FTBLibClient.mc.currentScreen); }
		});
		
		GuiScreenRegistry.register("client_config", new GuiScreenRegistry.Entry()
		{
			public GuiScreen openGui(EntityPlayer ep)
			{ return new GuiEditConfig(FTBLibClient.mc.currentScreen, ClientConfigRegistry.provider()); }
		});
	}
	
	public static final PlayerAction notifications = new PlayerAction(PlayerAction.Type.SELF, "ftbl.notifications", 1000, GuiIcons.chat)
	{
		public void onClicked(ILMPlayer self, ILMPlayer other)
		{ FTBLibClient.openGui(new GuiNotifications(FTBLibClient.mc.currentScreen)); }
		
		public String getDisplayName()
		{ return FTBLibMod.proxy.translate(FTBLibModClient.notifications.getFullID()); }
		
		public boolean isVisibleFor(ILMPlayer self, ILMPlayer other)
		{ return !ClientNotifications.Perm.list.isEmpty(); }
		
		public void postRender(int ax, int ay, double z)
		{
			String n = String.valueOf(ClientNotifications.Perm.list.size());
			int nw = FTBLibClient.mc.fontRendererObj.getStringWidth(n);
			int width = 16;
			GlStateManager.color(1F, 0.13F, 0.13F, 0.66F);
			GuiLM.drawBlankRect(ax + width - nw, ay - 4, z, ax + width + 1, ay + 5);
			GlStateManager.color(1F, 1F, 1F, 1F);
			FTBLibClient.mc.fontRendererObj.drawString(n, ax + width - nw + 1, ay - 3, 0xFFFFFFFF);
		}
	};
	
	public static final PlayerAction settings = new PlayerAction(PlayerAction.Type.SELF, "ftbl.settings", -1000, GuiIcons.settings)
	{
		public void onClicked(ILMPlayer self, ILMPlayer other)
		{ FTBLibClient.openGui(new GuiEditConfig(FTBLibClient.mc.currentScreen, ClientConfigRegistry.provider())); }
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
		
		public String getDisplayName()
		{ return "Dev Console"; }
	};
	
	public static final PlayerAction toggle_gamemode = new PlayerAction(PlayerAction.Type.SELF, "ftbl.toggle_gamemode", -10, GuiIcons.toggle_gamemode)
	{
		public void onClicked(ILMPlayer self, ILMPlayer other)
		{
			int i = self.getPlayer().capabilities.isCreativeMode ? 0 : 1;
			FTBLibClient.execClientCommand("/gamemode " + i);
		}
		
		public Boolean configDefault()
		{ return Boolean.TRUE; }
	};
	
	public static final PlayerAction toggle_rain = new PlayerAction(PlayerAction.Type.SELF, "ftbl.toggle_rain", -11, GuiIcons.toggle_rain)
	{
		public void onClicked(ILMPlayer self, ILMPlayer other)
		{ FTBLibClient.execClientCommand("/toggledownfall"); }
		
		public Boolean configDefault()
		{ return Boolean.TRUE; }
	};
	
	public static final PlayerAction toggle_day = new PlayerAction(PlayerAction.Type.SELF, "ftbl.toggle_day", -12, GuiIcons.toggle_day)
	{
		public void onClicked(ILMPlayer self, ILMPlayer other)
		{ FTBLibClient.execClientCommand("/time set 6000"); }
		
		public Boolean configDefault()
		{ return Boolean.TRUE; }
	};
	
	public static final PlayerAction toggle_night = new PlayerAction(PlayerAction.Type.SELF, "ftbl.toggle_night", -13, GuiIcons.toggle_night)
	{
		public void onClicked(ILMPlayer self, ILMPlayer other)
		{ FTBLibClient.execClientCommand("/time set 18000"); }
		
		public Boolean configDefault()
		{ return Boolean.TRUE; }
	};
	
	@SubscribeEvent
	public void guiInitEvent(final GuiScreenEvent.InitGuiEvent.Post e)
	{
		if(!FTBLibClient.isIngame()) return;
		
		if(e.gui instanceof InventoryEffectRenderer)
		{
			ILMPlayer p = FTBLibClient.getClientLMPlayer();
			List<PlayerAction> buttons = PlayerActionRegistry.getPlayerActions(PlayerAction.Type.SELF, p, p, false);
			
			for(Shortcuts.Shortcut s : Shortcuts.shortcuts)
			{
				if(s instanceof Shortcuts.ButtonAction)
				{
					final Shortcuts.ButtonAction a = (Shortcuts.ButtonAction) s;
					TextureCoords tex = GuiIcons.iconMap.get(a.icon);
					if(tex == null) tex = GuiIcons.marker;
					
					PlayerAction pa = new PlayerAction(PlayerAction.Type.SELF, "temp-" + UUID.randomUUID(), a.priority, tex)
					{
						public void onClicked(ILMPlayer self, ILMPlayer other)
						{ a.action.onClicked(a.data); }
						
						public String getDisplayName()
						{ return a.name; }
					};
					
					buttons.add(pa);
				}
			}
			
			if(!buttons.isEmpty())
			{
				Collections.sort(buttons);
				
				ButtonInvLMRenderer renderer = new ButtonInvLMRenderer(495830, e.gui);
				e.buttonList.add(renderer);
				
				if(FTBLibModClient.action_buttons_on_top.get())
				{
					for(int i = 0; i < buttons.size(); i++)
					{
						PlayerAction a = buttons.get(i);
						int x = i % 4;
						int y = i / 4;
						ButtonInvLM b = new ButtonInvLM(495830 + i, a, 4 + x * 18, 4 + y * 18);
						e.buttonList.add(b);
						renderer.buttons.add(b);
					}
				}
				else
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
					
					if(hasPotions) guiLeft += 60;
					
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
						
						e.buttonList.add(b);
						renderer.buttons.add(b);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void guiActionEvent(GuiScreenEvent.ActionPerformedEvent.Post e)
	{
		if(e.button instanceof ButtonInvLM)
		{
			PlayerAction b = ((ButtonInvLM) e.button).action;
			ILMPlayer p = FTBLibClient.getClientLMPlayer();
			b.onClicked(p, p);
		}
	}
	
	private static class ButtonInvLM extends GuiButton
	{
		public final PlayerAction action;
		
		public ButtonInvLM(int id, PlayerAction b, int x, int y)
		{
			super(id, x, y, 16, 16, "");
			action = b;
		}
		
		public void drawButton(Minecraft mc, int mx, int my)
		{
		}
	}
	
	private static class ButtonInvLMRenderer extends GuiButton
	{
		public final List<ButtonInvLM> buttons;
		private final GuiContainerCreative creativeContainer;
		
		public ButtonInvLMRenderer(int id, GuiScreen g)
		{
			super(id, -1000, -1000, 0, 0, "");
			buttons = new ArrayList<>();
			creativeContainer = (g instanceof GuiContainerCreative) ? (GuiContainerCreative) g : null;
		}
		
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
					int tw = FTBLibClient.mc.fontRendererObj.getStringWidth(s);
					
					if(!FTBLibModClient.action_buttons_on_top.get())
					{
						mx1 -= tw + 8;
						my1 += 4;
					}
					
					if(mx1 < 4D) mx1 = 4D;
					if(my1 < 4D) my1 = 4D;
					
					GlStateManager.translate(mx1, my1, zLevel);
					
					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GlStateManager.color(0.13F, 0.13F, 0.13F, 1F);
					GuiLM.drawBlankRect(-3, -2, zLevel, tw + 6, 12);
					GlStateManager.color(1F, 1F, 1F, 1F);
					FTBLibClient.mc.fontRendererObj.drawString(s, 0, 0, 0xFFFFFFFF);
					GlStateManager.popMatrix();
				}
			}
			
			GlStateManager.color(1F, 1F, 1F, 1F);
		}
	}
}