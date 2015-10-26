package ftb.lib.client;

import net.minecraft.util.ResourceLocation;

public final class TextureCoords
{
	public static final TextureCoords nullTexture = new TextureCoords(null, 0, 0, 0, 0, 0, 0);
	
	public final ResourceLocation texture;
	public final int posX, posY, width, height;
	public final int textureW, textureH;
	public final double minU, minV, maxU, maxV;
	
	public TextureCoords(ResourceLocation res, int x, int y, int w, int h, int tw, int th)
	{
		texture = res;
		posX = x;
		posY = y;
		width = w;
		height = h;
		textureW = tw;
		textureH = th;
		
		minU = posX / (double)textureW;
		minV = posY / (double)textureH;
		maxU = (posX + width) / (double)textureW;
		maxV = (posY + height) / (double)textureH;
	}
	
	public TextureCoords(ResourceLocation res, int x, int y, int w, int h)
	{ this(res, x, y, w, h, 256, 256); }
	
	public int getWidth(double h)
	{ return (int)(width * (h / (double)height)); }
	
	public int getHeight(double w)
	{ return (int)(height * (w / (double)width)); }
	
	public boolean isValid()
	{ return texture != null && width > 0 && height > 0; }
}