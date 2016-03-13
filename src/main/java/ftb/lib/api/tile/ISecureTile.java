package ftb.lib.api.tile;

import net.minecraft.entity.player.EntityPlayer;

public interface ISecureTile extends ITileEntity
{
	boolean canPlayerInteract(EntityPlayer ep, boolean breakBlock);
	void onPlayerNotOwner(EntityPlayer ep, boolean breakBlock);
}