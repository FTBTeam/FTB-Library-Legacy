package ftb.lib.api.gui;

import cpw.mods.fml.relauncher.*;
import ftb.lib.api.client.*;
import ftb.lib.api.gui.widgets.PanelLM;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.*;

/**
 * Created by LatvianModder on 30.01.2016.
 */
@SideOnly(Side.CLIENT)
public abstract class GuiContainerLM extends GuiContainer implements IGuiLM
{
	private static final ArrayList<String> tempTextList = new ArrayList<>();
	
	private boolean refreshWidgets = true;
	public final GuiScreen parentScreen;
	public final ResourceLocation texture;
	public final ContainerLM container;
	public final PanelLM mainPanel;
	private final MouseLM mouse;
	public float delta;
	
	public GuiContainerLM(GuiScreen parent, ContainerLM c, ResourceLocation tex)
	{
		super(c);
		
		parentScreen = parent;
		texture = tex;
		container = c;
		mc = FTBLibClient.mc;
		
		if(parent != null) mc.thePlayer.closeScreen();
		
		ScaledResolution scr = new ScaledResolution(mc, FTBLibClient.mc.displayWidth, FTBLibClient.mc.displayHeight);
		
		mainPanel = new PanelLM(this, 0, 0, scr.getScaledWidth(), scr.getScaledHeight())
		{
			public void addWidgets()
			{ GuiContainerLM.this.addWidgets(); }
		};
		mouse = new MouseLM();
		refreshWidgets();
	}
	
	public void refreshWidgets()
	{ refreshWidgets = true; }
	
	public final void initGui()
	{
		super.initGui();
		initLMGui();
		mainPanel.width = xSize;
		mainPanel.height = ySize;
		mainPanel.posX = (width - mainPanel.width) / 2;
		mainPanel.posY = (height - mainPanel.height) / 2;
		refreshWidgets();
	}
	
	public void initLMGui()
	{
	}
	
	public abstract void addWidgets();
	
	public ItemStack getHeldItem()
	{ return mc.thePlayer.inventory.getItemStack(); }
	
	public GuiScreen getGui()
	{ return this; }
	
	public PanelLM getMainPanel()
	{ return mainPanel; }
	
	public MouseLM mouse()
	{ return mouse; }
	
	public final float getZLevel()
	{ return zLevel; }
	
	public final void setZLevel(float z)
	{ zLevel = z; }
	
	public FontRenderer getFontRenderer()
	{ return fontRendererObj; }
	
	public final void close(GuiScreen g)
	{ FTBLibClient.openGui((g == null) ? parentScreen : g); }
	
	protected final void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
	}
	
	protected final void mouseClicked(int mx, int my, int b)
	{
		mouse.onClicked(b, true);
		mainPanel.mousePressed(b);
		super.mouseClicked(mx, my, b);
		mouseClicked();
	}
	
	public void mouseClicked()
	{
	}
	
	protected void keyTyped(char keyChar, int key)
	{
		if(mainPanel.keyPressed(key, keyChar)) return;
		if(key == 1 || key == mc.gameSettings.keyBindInventory.getKeyCode()) onClosedByKey();
		super.keyTyped(keyChar, key);
	}
	
	public void onClosedByKey()
	{ close(null); }
	
	public void drawBackground()
	{
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		if(texture != null)
		{
			FTBLibClient.setTexture(texture);
			drawTexturedModalRect(mainPanel.posX, mainPanel.posY, 0, 0, mainPanel.width, mainPanel.height);
		}
	}
	
	public void drawForeground()
	{
	}
	
	public final void drawScreen(int mx, int my, float f)
	{
		mouse.onUpdate(mx, my);
		delta = f;
		
		if(refreshWidgets)
		{
			mainPanel.refreshWidgets();
			refreshWidgets = false;
		}
		
		drawDefaultBackground();
		drawBackground();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		super.drawScreen(mx, my, f);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		tempTextList.clear();
		drawText(tempTextList);
		if(!tempTextList.isEmpty()) drawHoveringText(tempTextList, mouse.x, Math.max(mouse.y, 18), fontRendererObj);
		GlStateManager.disableLighting();
		drawForeground();
		GlStateManager.color(1F, 1F, 1F, 1F);
	}
	
	public void drawText(List<String> l)
	{
		mainPanel.addMouseOverText(l);
	}
	
	public final void onGuiClosed()
	{
		super.onGuiClosed();
		onLMGuiClosed();
	}
	
	public void onLMGuiClosed()
	{
	}
}
