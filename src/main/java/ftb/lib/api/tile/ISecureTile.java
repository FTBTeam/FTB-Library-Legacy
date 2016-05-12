package ftb.lib.api.tile;

import ftb.lib.api.ForgePlayerMP;

public interface ISecureTile extends ITileEntity
{
	boolean canPlayerInteract(ForgePlayerMP player, boolean breakBlock);
	void onPlayerNotOwner(ForgePlayerMP player, boolean breakBlock);
}