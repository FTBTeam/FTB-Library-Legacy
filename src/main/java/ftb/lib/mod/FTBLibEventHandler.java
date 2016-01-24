package ftb.lib.mod;

import ftb.lib.*;
import ftb.lib.api.ServerTickCallback;
import ftb.lib.api.item.ICreativeSafeItem;
import ftb.lib.api.tile.ISecureTile;
import ftb.lib.mod.net.MessageSendWorldID;
import latmod.lib.util.Phase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

public class FTBLibEventHandler
{
	public static final List<ServerTickCallback> callbacks = new ArrayList<>();
	public static final List<ServerTickCallback> pendingCallbacks = new ArrayList<>();
	
	@SubscribeEvent
	public void onWorldLoaded(WorldEvent.Load e)
	{
		if(e.world.provider.getDimensionId() == 0 && e.world instanceof WorldServer)
			FTBLib.reload(FTBLib.getServer(), false, false);
	}
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e)
	{
		if(e.player instanceof EntityPlayerMP)
		{
			EntityPlayerMP ep = (EntityPlayerMP) e.player;
			if(FTBLib.ftbu != null) FTBLib.ftbu.onPlayerJoined(ep, Phase.PRE);
			new MessageSendWorldID(FTBWorld.server, ep).sendTo(ep);
			if(FTBLib.ftbu != null) FTBLib.ftbu.onPlayerJoined(ep, Phase.POST);
		}
	}
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent e)
	{
		if(!e.world.isRemote && e.side == Side.SERVER && e.phase == TickEvent.Phase.END && e.type == TickEvent.Type.WORLD)
		{
			if(e.world.provider.getDimensionId() == 0)
			{
				if(!pendingCallbacks.isEmpty())
				{
					callbacks.addAll(pendingCallbacks);
					pendingCallbacks.clear();
				}
				
				if(!callbacks.isEmpty())
				{
					for(int i = callbacks.size() - 1; i >= 0; i--)
						if(callbacks.get(i).incAndCheck()) callbacks.remove(i);
				}
			}
			
			if(FTBLib.ftbu != null) FTBLib.ftbu.onServerTick(e.world);
		}
	}
	
	@SubscribeEvent
	public void onRightClick(PlayerInteractEvent e)
	{
		if(e.entityPlayer instanceof FakePlayer || e.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) return;
		else if(!canInteract(e.entityPlayer, e.pos, e.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK))
			e.setCanceled(true);
		else if(FTBLib.ftbu != null) FTBLib.ftbu.onRightClick(e);
	}
	
	private boolean canInteract(EntityPlayer ep, BlockPos pos, boolean leftClick)
	{
		ItemStack heldItem = ep.getHeldItem();
		
		if(ep.capabilities.isCreativeMode && leftClick && heldItem != null && heldItem.getItem() instanceof ICreativeSafeItem)
		{
			if(!ep.worldObj.isRemote) ep.worldObj.markBlockRangeForRenderUpdate(pos, pos);
			else ep.worldObj.markBlockForUpdate(pos);
			return false;
		}
		
		if(!ep.worldObj.isRemote)
		{
			IBlockState state = ep.worldObj.getBlockState(pos);
			
			if(state.getBlock().hasTileEntity(state))
			{
				TileEntity te = ep.worldObj.getTileEntity(pos);
				if(te instanceof ISecureTile && !te.isInvalid() && !((ISecureTile) te).canPlayerInteract(ep, leftClick))
				{
					((ISecureTile) te).onPlayerNotOwner(ep, leftClick);
					return false;
				}
			}
		}
		
		return true;
	}
}