package ftb.lib.mod.client.gui;

import cpw.mods.fml.relauncher.*;
import ftb.lib.TextureCoords;
import ftb.lib.api.client.*;
import ftb.lib.api.gui.*;
import ftb.lib.api.gui.callback.*;
import ftb.lib.api.gui.widgets.*;
import latmod.lib.*;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSelectColor extends GuiLM
{
	public static final ResourceLocation tex = new ResourceLocation("ftbl", "textures/gui/colselector.png");
	public static final ResourceLocation tex_wheel = new ResourceLocation("ftbl", "textures/gui/colselector_wheel.png");
	
	public static final TextureCoords col_tex = new TextureCoords(tex, 143, 10, 29, 16);
	public static final TextureCoords cursor_tex = new TextureCoords(tex, 143, 36, 8, 8);
	
	public static final int SLIDER_W = 6, SLIDER_H = 10, SLIDER_BAR_W = 64;
	public static final TextureCoords slider_tex = new TextureCoords(tex, 143, 26, SLIDER_W, SLIDER_H);
	public static final TextureCoords slider_col_tex = new TextureCoords(tex, 143, 0, SLIDER_BAR_W, SLIDER_H);
	
	public final IColorCallback callback;
	public final LMColor.HSB initCol;
	public final Object colorID;
	public final boolean isInstant;
	public final LMColor.HSB currentColor;
	
	public final ButtonLM colorInit, colorCurrent;
	public final SliderLM sliderHue, sliderSaturation, sliderBrightness;
	public final ColorSelector colorSelector;
	
	public GuiSelectColor(IColorCallback cb, LMColor col, Object id, boolean instant)
	{
		super(null, tex);
		callback = cb;
		initCol = new LMColor.HSB();
		initCol.set(col);
		currentColor = new LMColor.HSB();
		colorID = id;
		isInstant = instant;
		
		mainPanel.width = 143;
		mainPanel.height = 93;
		
		colorInit = new ButtonLM(this, 74, 71, col_tex.widthI(), col_tex.heightI())
		{
			public void onButtonPressed(int b)
			{ closeGui(false); }
			
			public void addMouseOverText(List<String> s)
			{
				s.add(FTBLibLang.button_cancel());
				s.add(initCol.toString());
			}
		};
		
		colorCurrent = new ButtonLM(this, 107, 71, col_tex.widthI(), col_tex.heightI())
		{
			public void onButtonPressed(int b)
			{ closeGui(true); }
			
			public void addMouseOverText(List<String> s)
			{
				s.add(FTBLibLang.button_accept());
				s.add(currentColor.toString());
			}
		};
		
		/*
		currentColR = new SliderLM(this, 6, 25, SLIDER_BAR_W, SLIDER_H, SLIDER_W);
		currentColR.value = currentColor.red() / 255F;
		currentColR.displayMax = 255;
		currentColR.title = EnumMCColor.RED.toString();
		currentColR.scrollStep = 1F / 255F;
		
		currentColG = new SliderLM(this, 6, 41, SLIDER_BAR_W, SLIDER_H, SLIDER_W);
		currentColG.value = currentColor.green() / 255F;
		currentColG.displayMax = 255;
		currentColG.title = EnumMCColor.GREEN.toString();
		currentColG.scrollStep = 1F / 255F;
		
		currentColB = new SliderLM(this, 6, 57, SLIDER_BAR_W, SLIDER_H, SLIDER_W);
		currentColB.value = currentColor.blue() / 255F;
		currentColB.displayMax = 255;
		currentColB.title = EnumMCColor.BLUE.toString();
		currentColB.scrollStep = 1F / 255F;
		*/
		
		sliderHue = new SliderLM(this, 4, 51, SLIDER_BAR_W, SLIDER_H, SLIDER_W);
		sliderHue.displayMax = 255;
		sliderHue.title = "Hue";
		sliderHue.scrollStep = 1F / 255F;
		
		sliderSaturation = new SliderLM(this, 4, 64, SLIDER_BAR_W, SLIDER_H, SLIDER_W);
		sliderSaturation.displayMax = 255;
		sliderSaturation.title = "Saturation";
		sliderSaturation.scrollStep = 1F / 255F;
		
		sliderBrightness = new SliderLM(this, 4, 77, SLIDER_BAR_W, SLIDER_H, SLIDER_W);
		sliderBrightness.displayMax = 255;
		sliderBrightness.title = "Brightness";
		sliderBrightness.scrollStep = 1F / 255F;
		
		colorSelector = new ColorSelector(this, 73, 5, 64, 64);
		
		setColor(initCol);
	}
	
	public void addWidgets()
	{
		mainPanel.add(colorInit);
		mainPanel.add(colorCurrent);
		mainPanel.add(sliderHue);
		mainPanel.add(sliderSaturation);
		mainPanel.add(sliderBrightness);
		mainPanel.add(colorSelector);
	}
	
	public void setColor(LMColor col)
	{
		currentColor.set(col);
		sliderHue.value = initCol.hue();
		sliderSaturation.value = initCol.saturation();
		sliderBrightness.value = initCol.brightness();
	}
	
	public void drawBackground()
	{
		super.drawBackground();
		
		float br0 = sliderBrightness.value;
		update();
		if(sliderBrightness.value != br0)
		{
			updateColor();
			ColorSelector.shouldRedraw = true;
		}
		
		FTBLibClient.setGLColor(initCol.color(), 255);
		colorInit.render(col_tex);
		FTBLibClient.setGLColor(currentColor.color(), 255);
		colorCurrent.render(col_tex);
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		FTBLibClient.setTexture(tex);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		
		double z = zLevel;
		double w = slider_col_tex.width;
		double h = slider_col_tex.height;
		double u0 = slider_col_tex.minU;
		double v0 = slider_col_tex.minV;
		double u1 = slider_col_tex.maxU;
		double v1 = slider_col_tex.maxV;
		
		int x = mainPanel.posX + sliderBrightness.posX;
		int y = mainPanel.posY + sliderBrightness.posY;
		
		GL11.glBegin(GL11.GL_QUADS);
		GlStateManager.color(0F, 0F, 0F, 1F);
		GL11.glTexCoord2d(u0, v0);
		GL11.glVertex3d(x + 0, y + 0, z);
		GL11.glTexCoord2d(u0, v1);
		GL11.glVertex3d(x + 0, y + h, z);
		
		LMColor.HSB tempColor = new LMColor.HSB();
		tempColor.setHSB(tempColor.hue(), tempColor.saturation(), 1F);
		FTBLibClient.setGLColor(tempColor.color(), 255);
		GL11.glTexCoord2d(u1, v1);
		GL11.glVertex3d(x + w, y + h, z);
		GL11.glTexCoord2d(u1, v0);
		GL11.glVertex3d(x + w, y + 0, z);
		GL11.glEnd();
		
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.enableTexture2D();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		
		colorSelector.renderWidget();
		
		sliderBrightness.renderSlider(slider_tex);
	}
	
	public void update()
	{
		sliderBrightness.update();
	}
	
	public void updateColor()
	{
		float h = (float) (Math.atan2(colorSelector.cursorPosY - 0.5D, colorSelector.cursorPosX - 0.5D) / MathHelperLM.TWO_PI);
		float s = (float) (MathHelperLM.dist(colorSelector.cursorPosX, colorSelector.cursorPosY, 0D, 0.5D, 0.5D, 0D) * 2D);
		currentColor.setHSB(h, s, sliderBrightness.value);
		if(isInstant) callback.onColorSelected(new ColorSelected(colorID, true, currentColor, false));
	}
	
	public void closeGui(boolean set)
	{
		FTBLibClient.playClickSound();
		callback.onColorSelected(new ColorSelected(colorID, set, set ? currentColor : initCol, true));
	}
	
	public static class ColorSelector extends WidgetLM
	{
		public static boolean shouldRedraw = true;
		
		public final GuiSelectColor gui;
		public boolean grabbed = false;
		public double cursorPosX = 0D;
		public double cursorPosY = 0D;
		
		public ColorSelector(GuiSelectColor g, int x, int y, int w, int h)
		{
			super(g, x, y, w, h);
			gui = g;
			cursorPosX = cursorPosY = -1D;
			shouldRedraw = true;
			//cursorPosX = Math.sin(0D) + 0.5D;
			//cursorPosY = Math.cos(0D) + 0.5D;
		}
		
		public void renderWidget()
		{
			int ax = getAX();
			int ay = getAY();
			
			if(grabbed && !Mouse.isButtonDown(0)) grabbed = false;
			
			if(grabbed)
			{
				cursorPosX = (gui.mouse().x - ax) / (double) width;
				cursorPosY = (gui.mouse().y - ay) / (double) height;
				
				double s = MathHelperLM.dist(cursorPosX, cursorPosY, 0D, 0.5D, 0.5D, 0D) * 2D;
				
				if(s > 1D)
				{
					cursorPosX = (cursorPosX - 0.5D) / s + 0.5D;
					cursorPosY = (cursorPosY - 0.5D) / s + 0.5D;
				}
				
				cursorPosX = MathHelperLM.clamp(cursorPosX, 0D, 1D);
				cursorPosY = MathHelperLM.clamp(cursorPosY, 0D, 1D);
				
				gui.updateColor();
			}
			
			GlStateManager.color(1F, 1F, 1F, 1F);
			FTBLibClient.setTexture(tex_wheel);
			GuiLM.drawTexturedRectD(ax, ay, gui.zLevel, width, height, 0D, 0D, 1D, 1D);
			
			if(cursorPosX >= 0D && cursorPosY >= 0D)
			{
				GlStateManager.color(1F - gui.currentColor.red() / 255F, 1F - gui.currentColor.green() / 255F, 1F - gui.currentColor.blue() / 255F, 1F);
				GuiLM.render(cursor_tex, ax + cursorPosX * width - 2, ay + cursorPosY * height - 2, gui.getZLevel(), 4, 4);
				GlStateManager.color(1F, 1F, 1F, 1F);
			}
		}
		
		public void mousePressed(int b)
		{ if(b == 0 && mouseOver()) grabbed = true; }
	}
}