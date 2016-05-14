package ftb.lib.api.tile;

import ftb.lib.FTBLib;
import ftb.lib.api.IEditableName;
import ftb.lib.api.MouseButton;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 14.05.2016.
 */
public class TileClientActionRegistry
{
	public static final Map<ResourceLocation, TileClientAction> map = new HashMap<>();
	
	public static TileClientAction register(TileClientAction a)
	{
		map.put(a.getResourceLocation(), a);
		return a;
	}
	
	public static final TileClientAction OPEN_GUI = register(new TileClientAction(new ResourceLocation("ftbl", "open_gui"))
	{
		@Override
		public void onAction(TileEntity te, NBTTagCompound data, EntityPlayerMP player)
		{
			if(te instanceof IGuiTile)
			{
				FTBLib.openGui(player, (IGuiTile) te, data);
			}
		}
	});
	
	public static final TileClientAction BUTTON_PRESSED = register(new TileClientAction(new ResourceLocation("ftbl", "button"))
	{
		@Override
		public void onAction(TileEntity te, NBTTagCompound data, EntityPlayerMP player)
		{
			if(te instanceof ITileButtonPressed)
			{
				((ITileButtonPressed) te).handleButton(player, data.getInteger("I"), MouseButton.get(data.getByte("M")), data.getCompoundTag("D"));
			}
		}
	});
	
	public static final TileClientAction CUSTOM_NAME = register(new TileClientAction(new ResourceLocation("ftbl", "custom_name"))
	{
		@Override
		public void onAction(TileEntity te, NBTTagCompound data, EntityPlayerMP player)
		{
			if(te instanceof IEditableName)
			{
				String name = data.getString("N");
				
				if(((IEditableName) te).setName(name, player))
				{
					te.markDirty();
				}
			}
		}
	});
}