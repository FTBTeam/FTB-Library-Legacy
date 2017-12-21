package com.feed_the_beast.ftblib.lib.icon;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public class IconWithParent extends Icon
{
	public final Icon parent;

	public IconWithParent(Icon i)
	{
		parent = i;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ITextureObject bindTexture()
	{
		return parent.bindTexture();
	}

	@Override
	public void draw(int x, int y, int w, int h, Color4I col)
	{
	}
}