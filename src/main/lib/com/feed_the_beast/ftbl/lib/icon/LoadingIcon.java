package com.feed_the_beast.ftbl.lib.icon;

import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class LoadingIcon extends Icon
{
	public static final LoadingIcon INSTANCE = new LoadingIcon();
	public static final ResourceLocation TEXTURE = FTBLibFinals.get("textures/gui/loading.png");

	private LoadingIcon()
	{
	}

	@Override
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		long t = System.currentTimeMillis() % 3600L;
		col = col.whiteIfEmpty();
		ClientUtils.MC.getTextureManager().bindTexture(TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + w / 2D, y + h / 2D, 0D);
		GlStateManager.pushMatrix();
		GlStateManager.rotate(t * 0.3F, 0F, 0F, 1F);
		GuiHelper.drawTexturedRect(-w / 2, -h / 2, w, h, col, 0D, 0D, 1D, 1D);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.rotate(t * -0.13F, 0F, 0F, 1F);
		GlStateManager.scale(0.5D, 0.5D, 1D);
		GuiHelper.drawTexturedRect(-w / 2, -h / 2, w, h, col, 0D, 0D, 1D, 1D);
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}

	@Override
	public JsonElement getJson()
	{
		return new JsonPrimitive("loading");
	}
}