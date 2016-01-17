package ftb.lib.api.client.model;

import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public final class CubeRenderer
{
	public static final CubeRenderer instance = new CubeRenderer();
	private Tessellator tessellator;
	private WorldRenderer renderer;
	
	protected static final float[] normalsX = new float[] {0F, 0F, 0F, 0F, -1F, 1F};
	protected static final float[] normalsY = new float[] {-1F, 1F, 0F, 0F, 0F, 0F};
	protected static final float[] normalsZ = new float[] {0F, 0F, -1F, 1F, 0F, 0F};
	
	public boolean hasTexture = true;
	
	/**
	 * Unimplemented
	 */
	public boolean isInterpolated = false;
	
	protected EnumFacing currentFace = null;
	protected double minX, minY, minZ, maxX, maxY, maxZ;
	protected double minU, minV, maxU, maxV;
	
	public CubeRenderer()
	{ setTessellator(Tessellator.getInstance()); }
	
	public void setTessellator(Tessellator t)
	{
		if(t == null) return;
		tessellator = t;
		renderer = tessellator.getWorldRenderer();
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
	
	private void begin(EnumFacing f)
	{
		if(f != null)
		{
			end();
			return;
		}
		
		currentFace = f;
		int i = currentFace.ordinal();
		renderer.normal(normalsX[i], normalsY[i], normalsZ[i]);
		renderer.begin(7, hasTexture ? DefaultVertexFormats.POSITION_TEX_NORMAL : DefaultVertexFormats.POSITION_NORMAL);
	}
	
	private void end()
	{
		if(currentFace == null) return;
		tessellator.draw();
		currentFace = null;
	}
	
	private void vertex(double x, double y, double z, double u, double v)
	{
		if(currentFace == null) return;
		
		renderer.pos(x, y, z);
		if(hasTexture) renderer.tex(u, v);
		renderer.endVertex();
	}
	
	public void renderDown()
	{
		begin(EnumFacing.DOWN);
		vertex(minX, minY, minZ, minU, minV);
		vertex(maxX, minY, minZ, maxU, minV);
		vertex(maxX, minY, maxZ, maxU, maxV);
		vertex(minX, minY, maxZ, minU, maxV);
		end();
	}
	
	public void renderUp()
	{
		begin(EnumFacing.UP);
		vertex(minX, maxY, minZ, minU, minV);
		vertex(minX, maxY, maxZ, minU, maxV);
		vertex(maxX, maxY, maxZ, maxU, maxV);
		vertex(maxX, maxY, minZ, maxU, minV);
		end();
	}
	
	public void renderSouth()
	{
		begin(EnumFacing.SOUTH);
		vertex(minX, minY, maxZ, minU, maxV);
		vertex(maxX, minY, maxZ, maxU, maxV);
		vertex(maxX, maxY, maxZ, maxU, minV);
		vertex(minX, maxY, maxZ, minU, minV);
		end();
	}
	
	public void renderNorth()
	{
		begin(EnumFacing.NORTH);
		vertex(minX, minY, minZ, maxU, maxV);
		vertex(minX, maxY, minZ, maxU, minV);
		vertex(maxX, maxY, minZ, minU, minV);
		vertex(maxX, minY, minZ, minU, maxV);
		end();
	}
	
	public void renderWest()
	{
		begin(EnumFacing.WEST);
		vertex(minX, minY, minZ, minU, maxV);
		vertex(minX, minY, maxZ, maxU, maxV);
		vertex(minX, maxY, maxZ, maxU, minV);
		vertex(minX, maxY, minZ, minU, minV);
		end();
	}
	
	public void renderEast()
	{
		begin(EnumFacing.EAST);
		vertex(maxX, minY, minZ, maxU, maxV);
		vertex(maxX, maxY, minZ, maxU, minV);
		vertex(maxX, maxY, maxZ, minU, minV);
		vertex(maxX, minY, maxZ, minU, maxV);
		end();
	}
}