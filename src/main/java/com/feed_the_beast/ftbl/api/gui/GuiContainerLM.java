package com.feed_the_beast.ftbl.api.gui;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.widgets.PanelLM;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 30.01.2016.
 */
@SideOnly(Side.CLIENT)
public abstract class GuiContainerLM extends GuiContainer implements IGuiLM
{
	private static final ArrayList<String> tempTextList = new ArrayList<>();
	
	private boolean refreshWidgets = true;
	public final ResourceLocation texture;
	public final ContainerLM container;
	public final PanelLM mainPanel;
	private final MouseLM mouse;
	public float delta;
	
	public GuiContainerLM(ContainerLM c, ResourceLocation tex)
	{
		super(c);
		
		texture = tex;
		container = c;
		mc = FTBLibClient.mc;
		
		mainPanel = new PanelLM(this, 0, 0, 176, 166)
		{
			@Override
			public void addWidgets()
			{ GuiContainerLM.this.addWidgets(); }
		};
		
		mouse = new MouseLM();
		refreshWidgets();
	}
	
	@Override
	public void refreshWidgets()
	{ refreshWidgets = true; }
	
	@Override
	public final void initGui()
	{
		super.initGui();
		initLMGui();
		xSize = mainPanel.width;
		ySize = mainPanel.height;
		guiLeft = mainPanel.posX = (width - mainPanel.width) / 2;
		guiTop = mainPanel.posY = (height - mainPanel.height) / 2;
		refreshWidgets();
	}
	
	@Override
	public void initLMGui()
	{
	}
	
	public abstract void addWidgets();
	
	public ItemStack getHeldItem()
	{ return mc.thePlayer.inventory.getItemStack(); }
	
	@Override
	public GuiScreen getGui()
	{ return this; }
	
	@Override
	public PanelLM getMainPanel()
	{ return mainPanel; }
	
	@Override
	public MouseLM mouse()
	{ return mouse; }
	
	@Override
	public final float getZLevel()
	{ return zLevel; }
	
	@Override
	public final void setZLevel(float z)
	{ zLevel = z; }
	
	@Override
	public FontRenderer getFontRenderer()
	{ return fontRendererObj; }
	
	@Override
	public final void close(GuiScreen g)
	{ FTBLibClient.openGui(g); }
	
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{
		drawForeground();
	}
	
	@Override
	protected final void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		drawBackground();
	}
	
	@Override
	protected final void mouseClicked(int mx, int my, int b) throws IOException
	{
		mouse.onClicked(b, true);
		mainPanel.mousePressed(b);
		super.mouseClicked(mx, my, b);
		mouseClicked();
	}
	
	public void mouseClicked()
	{
	}
	
	@Override
	protected void keyTyped(char keyChar, int key) throws IOException
	{
		if(mainPanel.keyPressed(key, keyChar)) { return; }
		if(key == 1 || key == mc.gameSettings.keyBindInventory.getKeyCode()) { onClosedByKey(); }
		super.keyTyped(keyChar, key);
	}
	
	public void onClosedByKey()
	{ close(null); }
	
	@Override
	public void drawTexturedModalRect(int x, int y, int u, int v, int w, int h)
	{ drawTexturedModalRectD(x, y, u, v, w, h); }
	
	public void drawTexturedModalRectD(double x, double y, double u, double v, double w, double h)
	{ GuiLM.drawTexturedModalRectD(x, y, zLevel, u, v, w, h, 256, 256); }
	
	public void drawBackground()
	{
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		if(texture != null)
		{
			FTBLibClient.setTexture(texture);
			drawTexturedModalRectD(mainPanel.posX, mainPanel.posY, 0D, 0D, mainPanel.width, mainPanel.height);
		}
	}
	
	public void drawForeground()
	{
	}
	
	@Override
	public final void drawScreen(int mx, int my, float f)
	{
		mouse.onUpdate(mx, my);
		delta = f;
		
		if(refreshWidgets)
		{
			mainPanel.refreshWidgets();
			refreshWidgets = false;
		}
		
		super.drawScreen(mx, my, f);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		
		tempTextList.clear();
		drawText(tempTextList);
		if(!tempTextList.isEmpty())
		{
			drawHoveringText(tempTextList, mouse.x, Math.max(mouse.y, 18), fontRendererObj);
		}
		
		GlStateManager.disableLighting();
		GlStateManager.color(1F, 1F, 1F, 1F);
	}
	
	public void drawText(List<String> l)
	{
		mainPanel.addMouseOverText(l);
	}
	
	@Override
	public final void onGuiClosed()
	{
		super.onGuiClosed();
		onLMGuiClosed();
	}
	
	public void onLMGuiClosed()
	{
	}
}
