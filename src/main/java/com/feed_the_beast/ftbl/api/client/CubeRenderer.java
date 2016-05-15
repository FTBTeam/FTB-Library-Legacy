package com.feed_the_beast.ftbl.api.client;

import latmod.lib.LMColor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class CubeRenderer
{
	public static final CubeRenderer instance = new CubeRenderer();
	private Tessellator tessellator;
	private VertexBuffer buffer;
	
	private static final float[] normalsX = new float[] {0F, 0F, 0F, 0F, -1F, 1F};
	private static final float[] normalsY = new float[] {-1F, 1F, 0F, 0F, 0F, 0F};
	private static final float[] normalsZ = new float[] {0F, 0F, -1F, 1F, 0F, 0F};
	
	private VertexFormat format;
	public boolean beginAndEnd = true;
	private boolean hasTexture = false;
	private boolean hasNormals = false;
	public LMColor color = null;
	
	private double minX, minY, minZ, maxX, maxY, maxZ;
	private double minU, minV, maxU, maxV;
	
	public CubeRenderer()
	{
		format = new VertexFormat();
		format.addElement(DefaultVertexFormats.POSITION_3F);
	}
	
	public CubeRenderer setTessellator(Tessellator t)
	{
		tessellator = t;
		buffer = (t == null) ? null : tessellator.getBuffer();
		return this;
	}
	
	public CubeRenderer setHasTexture()
	{
		if(!hasTexture)
		{
			hasTexture = true;
			format.addElement(DefaultVertexFormats.TEX_2F);
		}
		
		return this;
	}
	
	public CubeRenderer setHasNormals()
	{
		if(!hasNormals)
		{
			hasNormals = true;
			format.addElement(DefaultVertexFormats.NORMAL_3B);
			format.addElement(DefaultVertexFormats.PADDING_1B);
		}
		
		return this;
	}
	
	public CubeRenderer setHasColor()
	{
		if(color == null)
		{
			color = new LMColor.RGB();
			format.addElement(DefaultVertexFormats.COLOR_4UB);
		}
		
		return this;
	}
	
	public void setColor(LMColor c)
	{
		if(c != null && color != null)
		{
			color = c;
		}
	}
	
	public void setSize(double x0, double y0, double z0, double x1, double y1, double z1)
	{
		minX = x0;
		minY = y0;
		minZ = z0;
		maxX = x1;
		maxY = y1;
		maxZ = z1;
	}
	
	public void setSize(AxisAlignedBB aabb)
	{ setSize(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ); }
	
	public void setSizeWHD(double x, double y, double z, double w, double h, double d)
	{ setSize(x, y, z, x + w, y + h, z + d); }
	
	public void setUV(double u0, double v0, double u1, double v1)
	{
		minU = u0;
		minV = v0;
		maxU = u1;
		maxV = v1;
	}
	
	public void renderAll()
	{
		begin();
		beginAndEnd = false;
		renderDown();
		renderUp();
		renderSouth();
		renderNorth();
		renderWest();
		renderEast();
		beginAndEnd = true;
		end();
	}
	
	public void renderSides()
	{
		begin();
		beginAndEnd = false;
		renderSouth();
		renderNorth();
		renderWest();
		renderEast();
		beginAndEnd = true;
		end();
	}
	
	public void renderFace(EnumFacing f)
	{
		if(f == null) { return; }
		else if(f == EnumFacing.DOWN) { renderDown(); }
		else if(f == EnumFacing.UP) { renderUp(); }
		else if(f == EnumFacing.SOUTH) { renderSouth(); }
		else if(f == EnumFacing.NORTH) { renderNorth(); }
		else if(f == EnumFacing.WEST) { renderWest(); }
		else if(f == EnumFacing.EAST) { renderEast(); }
	}
	
	public void begin()
	{
		if(beginAndEnd)
		{
			buffer.begin(7, format);
		}
	}
	
	public void end()
	{
		if(beginAndEnd)
		{
			tessellator.draw();
		}
	}
	
	private void vertex(int i, double x, double y, double z, double u, double v)
	{
		buffer.pos(x, y, z);
		if(hasTexture) { buffer.tex(u, v); }
		if(hasNormals) { buffer.normal(normalsX[i], normalsY[i], normalsZ[i]); }
		if(color != null) { buffer.color(color.red(), color.green(), color.blue(), color.alpha()); }
		buffer.endVertex();
	}
	
	public void renderDown()
	{
		begin();
		vertex(0, minX, minY, minZ, minU, minV);
		vertex(0, maxX, minY, minZ, maxU, minV);
		vertex(0, maxX, minY, maxZ, maxU, maxV);
		vertex(0, minX, minY, maxZ, minU, maxV);
		end();
	}
	
	public void renderUp()
	{
		begin();
		vertex(1, minX, maxY, minZ, minU, minV);
		vertex(1, minX, maxY, maxZ, minU, maxV);
		vertex(1, maxX, maxY, maxZ, maxU, maxV);
		vertex(1, maxX, maxY, minZ, maxU, minV);
		end();
	}
	
	public void renderSouth()
	{
		begin();
		vertex(2, minX, minY, maxZ, minU, maxV);
		vertex(2, maxX, minY, maxZ, maxU, maxV);
		vertex(2, maxX, maxY, maxZ, maxU, minV);
		vertex(2, minX, maxY, maxZ, minU, minV);
		end();
	}
	
	public void renderNorth()
	{
		begin();
		vertex(3, minX, minY, minZ, maxU, maxV);
		vertex(3, minX, maxY, minZ, maxU, minV);
		vertex(3, maxX, maxY, minZ, minU, minV);
		vertex(3, maxX, minY, minZ, minU, maxV);
		end();
	}
	
	public void renderWest()
	{
		begin();
		vertex(4, minX, minY, minZ, minU, maxV);
		vertex(4, minX, minY, maxZ, maxU, maxV);
		vertex(4, minX, maxY, maxZ, maxU, minV);
		vertex(4, minX, maxY, minZ, minU, minV);
		end();
	}
	
	public void renderEast()
	{
		begin();
		vertex(5, maxX, minY, minZ, maxU, maxV);
		vertex(5, maxX, maxY, minZ, maxU, minV);
		vertex(5, maxX, maxY, maxZ, minU, minV);
		vertex(5, maxX, minY, maxZ, minU, maxV);
		end();
	}
}