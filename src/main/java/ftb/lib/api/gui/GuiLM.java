package ftb.lib.api.gui;

import ftb.lib.TextureCoords;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.gui.widgets.PanelLM;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiLM extends GuiScreen implements IGuiLM
{
	private static final ArrayList<String> tempTextList = new ArrayList<>();
	
	// GuiLM //
	
	public final GuiScreen parentScreen;
	public final ResourceLocation texture;
	public final PanelLM mainPanel;
	private final MouseLM mouse;
	public float delta;
	
	private boolean refreshWidgets = true;
	
	public GuiLM(GuiScreen parent, ResourceLocation tex)
	{
		parentScreen = parent;
		texture = tex;
		mc = FTBLibClient.mc;
		
		if(parent != null) { mc.thePlayer.closeScreen(); }
		
		ScaledResolution scr = new ScaledResolution(mc);
		
		mainPanel = new PanelLM(this, 0, 0, scr.getScaledWidth(), scr.getScaledHeight())
		{
			@Override
			public void addWidgets()
			{ GuiLM.this.addWidgets(); }
		};
		mouse = new MouseLM();
		refreshWidgets();
	}
	
	@Override
	public void refreshWidgets()
	{ refreshWidgets = true; }
	
	public abstract void addWidgets();
	
	public final ItemStack getHeldItem()
	{ return mc.thePlayer.inventory.getItemStack(); }
	
	@Override
	public final GuiScreen getGui()
	{ return this; }
	
	@Override
	public final PanelLM getMainPanel()
	{ return mainPanel; }
	
	@Override
	public final MouseLM mouse()
	{ return mouse; }
	
	@Override
	public final float getZLevel()
	{ return zLevel; }
	
	@Override
	public final void setZLevel(float z)
	{ zLevel = z; }
	
	@Override
	public final FontRenderer getFontRenderer()
	{ return fontRendererObj; }
	
	@Override
	public final void close(GuiScreen g)
	{ FTBLibClient.openGui((g == null) ? parentScreen : g); }
	
	@Override
	public final void initGui()
	{
		super.initGui();
		initLMGui();
		mainPanel.posX = (width - mainPanel.width) / 2;
		mainPanel.posY = (height - mainPanel.height) / 2;
		refreshWidgets();
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{ return false; }
	
	@Override
	public void initLMGui()
	{
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
		if(!tempTextList.isEmpty()) { drawHoveringText(tempTextList, mouse.x, Math.max(mouse.y, 18), fontRendererObj); }
		GlStateManager.disableLighting();
		drawForeground();
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
	
	@Override
	public void drawTexturedModalRect(int x, int y, int u, int v, int w, int h)
	{ drawTexturedModalRectD(x, y, u, v, w, h); }
	
	public void drawTexturedModalRectD(double x, double y, double u, double v, double w, double h)
	{ drawTexturedModalRectD(x, y, zLevel, u, v, w, h, 256, 256); }
	
	public static void drawTexturedModalRectD(double x, double y, double z, double u, double v, double w, double h, int width, int height)
	{ drawTexturedRect(x, y, z, w, h, u, v, u + w, v + h, width, height); }
	
	public static void drawTexturedRectD(double x, double y, double z, double w, double h, double u0, double v0, double u1, double v1)
	{
		if(u0 == 0D && v0 == 0D && u1 == 0D && v1 == 0D)
		{
			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer buffer = tessellator.getBuffer();
			buffer.begin(7, DefaultVertexFormats.POSITION);
			buffer.pos(x + 0, y + h, z).endVertex();
			buffer.pos(x + w, y + h, z).endVertex();
			buffer.pos(x + w, y + 0, z).endVertex();
			buffer.pos(x + 0, y + 0, z).endVertex();
			tessellator.draw();
		}
		else
		{
			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer buffer = tessellator.getBuffer();
			buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
			buffer.pos(x + 0, y + h, z).tex(u0, v1).endVertex();
			buffer.pos(x + w, y + h, z).tex(u1, v1).endVertex();
			buffer.pos(x + w, y + 0, z).tex(u1, v0).endVertex();
			buffer.pos(x + 0, y + 0, z).tex(u0, v0).endVertex();
			tessellator.draw();
		}
	}
	
	public static void drawTexturedRect(double x, double y, double z, double w, double h, double u0, double v0, double u1, double v1, int textureW, int textureH)
	{
		double scX = 1D / (double) textureW;
		double scY = 1D / (double) textureH;
		drawTexturedRectD(x, y, z, w, h, u0 * scX, v0 * scY, u1 * scX, v1 * scY);
	}
	
	public void playSoundFX(SoundEvent e, float pitch)
	{ mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(e, pitch)); }
	
	public static void drawPlayerHead(String username, double x, double y, double w, double h, double z)
	{
		FTBLibClient.setTexture(FTBLibClient.getSkinTexture(username));
		drawTexturedRectD(x, y, z, w, h, 0.125D, 0.125D, 0.25D, 0.25D);
		drawTexturedRectD(x, y, z, w, h, 0.625D, 0.125D, 0.75D, 0.25D);
	}
	
	public static void drawBlankRect(double x, double y, double z, double w, double h)
	{
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableTexture2D();
		drawTexturedRectD(x, y, z, w, h, 0D, 0D, 0D, 0D);
		GlStateManager.enableTexture2D();
	}
	
	public static void drawItem(IGuiLM gui, ItemStack is, int x, int y)
	{
		if(is == null) { return; }
		FTBLibClient.setTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		float z0 = gui.getZLevel();
		gui.setZLevel(z0 + 200F);
		RenderItem itemRender = FTBLibClient.mc.getRenderItem();
		itemRender.zLevel = 200F;
		FTBLibClient.renderGuiItem(is, itemRender, gui.getFontRenderer(), x, y);
		itemRender.zLevel = 0F;
		gui.setZLevel(z0);
	}
	
	public static void render(TextureCoords tc, double x, double y, double z, double w, double h)
	{
		if(tc == null || !tc.isValid()) { return; }
		FTBLibClient.setTexture(tc.texture);
		GuiLM.drawTexturedRectD(x, y, z, w, h, tc.minU, tc.minV, tc.maxU, tc.maxV);
	}
	
	public static void render(TextureCoords tc, double x, double y, double z)
	{ if(tc != null && tc.isValid()) { render(tc, x, y, z, tc.width, tc.height); } }
}