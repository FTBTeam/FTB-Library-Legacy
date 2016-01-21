package ftb.lib.api.client.model;

import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class CubeRenderer
{
	public static final CubeRenderer instance = new CubeRenderer();
	private Tessellator tessellator = null;
	private WorldRenderer renderer = null;
	
	private static final float[] normalsX = new float[] {0F, 0F, 0F, 0F, -1F, 1F};
	private static final float[] normalsY = new float[] {-1F, 1F, 0F, 0F, 0F, 0F};
	private static final float[] normalsZ = new float[] {0F, 0F, -1F, 1F, 0F, 0F};
	
	public boolean hasTexture = true;
	
	private double minX, minY, minZ, maxX, maxY, maxZ;
	private double minU, minV, maxU, maxV;
	
	public CubeRenderer()
	{ setTessellator(Tessellator.getInstance()); }
	
	public CubeRenderer setTessellator(Tessellator t)
	{
		t = null; // Until I fix my tessellator code, its always going to use GL11
		tessellator = t;
		renderer = (t == null) ? null : tessellator.getWorldRenderer();
		return this;
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
		renderDown();
		renderUp();
		renderSouth();
		renderNorth();
		renderWest();
		renderEast();
	}
	
	public void renderSides()
	{
		renderSouth();
		renderNorth();
		renderWest();
		renderEast();
	}
	
	public void renderFace(EnumFacing f)
	{
		if(f == null) return;
		else if(f == EnumFacing.DOWN) renderDown();
		else if(f == EnumFacing.UP) renderUp();
		else if(f == EnumFacing.SOUTH) renderSouth();
		else if(f == EnumFacing.NORTH) renderNorth();
		else if(f == EnumFacing.WEST) renderWest();
		else if(f == EnumFacing.EAST) renderEast();
	}
	
	private void begin(int i)
	{
		if(tessellator == null)
		{
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glNormal3f(normalsX[i], normalsY[i], normalsZ[i]);
		}
		else
		{
			renderer.begin(7, hasTexture ? DefaultVertexFormats.POSITION_TEX_NORMAL : DefaultVertexFormats.POSITION_NORMAL);
			renderer.normal(normalsX[i], normalsY[i], normalsZ[i]);
		}
	}
	
	private void end()
	{
		if(tessellator == null) GL11.glEnd();
		else tessellator.draw();
	}
	
	private void vertex(double x, double y, double z, double u, double v)
	{
		if(tessellator == null)
		{
			if(hasTexture) GL11.glTexCoord2d(u, v);
			GL11.glVertex3d(x, y, z);
		}
		else
		{
			renderer.pos(x, y, z);
			if(hasTexture) renderer.tex(u, v);
			renderer.endVertex();
		}
	}
	
	public void renderDown()
	{
		begin(0);
		vertex(minX, minY, minZ, minU, minV);
		vertex(maxX, minY, minZ, maxU, minV);
		vertex(maxX, minY, maxZ, maxU, maxV);
		vertex(minX, minY, maxZ, minU, maxV);
		end();
	}
	
	public void renderUp()
	{
		begin(1);
		vertex(minX, maxY, minZ, minU, minV);
		vertex(minX, maxY, maxZ, minU, maxV);
		vertex(maxX, maxY, maxZ, maxU, maxV);
		vertex(maxX, maxY, minZ, maxU, minV);
		end();
	}
	
	public void renderSouth()
	{
		begin(2);
		vertex(minX, minY, maxZ, minU, maxV);
		vertex(maxX, minY, maxZ, maxU, maxV);
		vertex(maxX, maxY, maxZ, maxU, minV);
		vertex(minX, maxY, maxZ, minU, minV);
		end();
	}
	
	public void renderNorth()
	{
		begin(3);
		vertex(minX, minY, minZ, maxU, maxV);
		vertex(minX, maxY, minZ, maxU, minV);
		vertex(maxX, maxY, minZ, minU, minV);
		vertex(maxX, minY, minZ, minU, maxV);
		end();
	}
	
	public void renderWest()
	{
		begin(4);
		vertex(minX, minY, minZ, minU, maxV);
		vertex(minX, minY, maxZ, maxU, maxV);
		vertex(minX, maxY, maxZ, maxU, minV);
		vertex(minX, maxY, minZ, minU, minV);
		end();
	}
	
	public void renderEast()
	{
		begin(5);
		vertex(maxX, minY, minZ, maxU, maxV);
		vertex(maxX, maxY, minZ, maxU, minV);
		vertex(maxX, maxY, maxZ, minU, minV);
		vertex(maxX, minY, maxZ, minU, maxV);
		end();
	}
}