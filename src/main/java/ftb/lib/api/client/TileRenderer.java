package ftb.lib.api.client;

import ftb.lib.FTBLibClient;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public abstract class TileRenderer<T extends TileEntity> extends TileEntitySpecialRenderer
{
	@SuppressWarnings("unchecked")
	public final void renderTileEntityAt(TileEntity te, double rx, double ry, double rz, float pt, int l)
	{ if(te != null && !te.isInvalid()) renderTile((T) te, rx, ry, rz, pt, l); }
	
	public abstract void renderTile(T t, double rx, double ry, double rz, float pt, int l);
	
	public final void register(Class<? extends T> c)
	{ FTBLibClient.addTileRenderer(c, this); }
}