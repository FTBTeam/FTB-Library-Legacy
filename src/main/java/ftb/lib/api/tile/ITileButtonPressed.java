package ftb.lib.api.tile;

import ftb.lib.api.MouseButton;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by LatvianModder on 14.05.2016.
 */
public interface ITileButtonPressed
{
	void handleButton(EntityPlayerMP player, int ID, MouseButton button, NBTTagCompound data);
}