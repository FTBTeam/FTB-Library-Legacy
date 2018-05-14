package com.feed_the_beast.ftblib.lib.icon;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class LoadingIcon extends Icon
{
	public static final LoadingIcon INSTANCE = new LoadingIcon();
	public static final ResourceLocation TEXTURE = new ResourceLocation(FTBLib.MOD_ID, "textures/gui/loading.png");
	public static float value = 0F;

	private LoadingIcon()
	{
	}

	@Override
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		value += ClientUtils.MC.getRenderPartialTicks();
		col = col.whiteIfEmpty();
		ClientUtils.MC.getTextureManager().bindTexture(TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + w / 2D, y + h / 2D, 0D);
		GlStateManager.pushMatrix();
		GlStateManager.rotate(value * 13F, 0F, 0F, 1F);
		GuiHelper.drawTexturedRect(-w / 2, -h / 2, w, h, col, 0D, 0D, 1D, 1D);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.rotate(value * -6F, 0F, 0F, 1F);
		GlStateManager.scale(0.5D, 0.5D, 1D);
		GuiHelper.drawTexturedRect(-w / 2, -h / 2, w, h, col, 0D, 0D, 1D, 1D);
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}

	public String toString()
	{
		return "loading";
	}
}