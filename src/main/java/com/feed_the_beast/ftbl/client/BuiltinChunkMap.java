package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.misc.ChunkSelectorMap;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiChunkSelectorBase;
import com.feed_the_beast.ftbl.lib.icon.Color4I;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.math.MathUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public class BuiltinChunkMap extends ChunkSelectorMap
{
	public static final Icon TEX_ENTITY = Icon.getIcon(FTBLibFinals.MOD_ID + ":textures/gui/entity.png");
	public static final double UV = (double) TILES_GUI / (double) TILES_TEX;

	@Override
	@SideOnly(Side.CLIENT)
	public void resetMap(int startX, int startZ)
	{
		ThreadReloadChunkSelector.reloadArea(ClientUtils.MC.world, startX, startZ);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawMap(GuiBase gui, int ax, int ay, int startX, int startZ)
	{
		ThreadReloadChunkSelector.updateTexture();
		GlStateManager.enableTexture2D();
		GlStateManager.bindTexture(ThreadReloadChunkSelector.getTextureId());
		GuiHelper.drawTexturedRect(ax, ay, TILES_GUI * GuiChunkSelectorBase.TILE_SIZE, TILES_GUI * GuiChunkSelectorBase.TILE_SIZE, Color4I.WHITE, 0D, 0D, UV, UV);

		EntityPlayer player = ClientUtils.MC.player;

		int cx = MathUtils.chunk(player.posX);
		int cy = MathUtils.chunk(player.posZ);

		if (cx >= startX && cy >= startZ && cx < startX + TILES_GUI && cy < startZ + TILES_GUI)
		{
			double x = ((cx - startX) * 16D + MathUtils.wrap(player.posX, 16D));
			double y = ((cy - startZ) * 16D + MathUtils.wrap(player.posZ, 16D));

			GlStateManager.pushMatrix();
			GlStateManager.translate(ax + x * GuiChunkSelectorBase.TILE_SIZE / 16D, ay + y * GuiChunkSelectorBase.TILE_SIZE / 16D, 0D);
			GlStateManager.pushMatrix();
			//GlStateManager.rotate((int)((ep.rotationYaw + 180F) / (180F / 8F)) * (180F / 8F), 0F, 0F, 1F);
			GlStateManager.rotate(player.rotationYaw + 180F, 0F, 0F, 1F);
			TEX_ENTITY.draw(-8, -8, 16, 16, Color4I.WHITE_A[33]);
			GlStateManager.popMatrix();
			ClientUtils.localPlayerHead.draw(-2, -2, 4, 4);
			GlStateManager.popMatrix();
		}
	}
}