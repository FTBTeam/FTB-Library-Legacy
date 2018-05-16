package com.feed_the_beast.ftblib.lib.icon;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

/**
 * @author LatvianModder
 */
public class LoadingIcon extends Icon
{
	public static final LoadingIcon INSTANCE = new LoadingIcon();
	public static final ResourceLocation TEXTURE = new ResourceLocation(FTBLib.MOD_ID, "textures/gui/loading.png");
	public static float value = 1000F;

	public static float[] speeds = new float[20];

	static
	{
		Random random = new Random(30382L);

		for (int i = 0; i < speeds.length; i++)
		{
			speeds[i] = random.nextFloat();
		}
	}

	private LoadingIcon()
	{
	}

	@Override
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		value += ClientUtils.MC.getTickLength();
		col = col.whiteIfEmpty().withAlpha(30);
		ClientUtils.MC.getTextureManager().bindTexture(TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + w / 2D, y + h / 2D, 0D);

		for (float speed : speeds)
		{
			GlStateManager.pushMatrix();
			GlStateManager.rotate((float) (value * 13D * MathUtils.map(0D, 1D, 0.1D, 1D, speed)), 0F, 0F, 1F);
			double scale = MathUtils.map(0D, 1D, 1D, 0.8D, speed);
			GlStateManager.scale(scale, scale, 1D);
			GuiHelper.drawTexturedRect(-w / 2, -h / 2, w, h, col.withTint(Color4I.hsb(speed, 0.2F, 1F)), 0D, 0D, 1D, 1D);
			GlStateManager.popMatrix();
		}

		GlStateManager.popMatrix();
	}

	public String toString()
	{
		return "loading";
	}
}