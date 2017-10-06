package com.feed_the_beast.ftbl.lib.icon;

import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.util.misc.Color4I;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public class PlayerHeadIcon extends ImageIcon
{
	private final String username;

	public PlayerHeadIcon(String u)
	{
		super(AbstractClientPlayer.getLocationSkin(u).toString(), 0.125D, 0.125D, 0.25D, 0.25D);
		username = u;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ITextureObject bindTexture()
	{
		ClientUtils.getSkinTexture(username);
		return super.bindTexture();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		bindTexture();

		if (!col.hasColor())
		{
			col = Color4I.WHITE;
		}

		GuiHelper.drawTexturedRect(x, y, w, h, col, 0.125D, 0.125D, 0.25D, 0.25D);
		GuiHelper.drawTexturedRect(x, y, w, h, col, 0.625D, 0.125D, 0.75D, 0.25D);
	}
}