package ftb.lib.api.tile;

import ftb.lib.*;
import ftb.lib.api.*;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.mod.FTBLibMod;
import ftb.lib.mod.net.MessageClientTileAction;
import latmod.lib.LMUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.*;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class TileLM extends TileEntity implements ITileEntity, IClientActionTile, IWorldNameable, ITickable
{
	public static final String ACTION_BUTTON_PRESSED = "button";
	public static final String ACTION_OPEN_GUI = "open_gui";
	public static final String ACTION_CUSTOM_NAME = "custom_name";
	
	protected enum EnumSync
	{
		OFF,
		SYNC,
		RERENDER;
		
		boolean sync()
		{ return this == SYNC || this == RERENDER; }
		
		boolean rerender()
		{ return this == RERENDER; }
	}
	
	public static final int[] NO_SLOTS = new int[0];
	
	private boolean isDirty = true;
	public boolean isLoaded = false;
	public UUID ownerID;
	public boolean redstonePowered = false;
	public IBlockState currentState;
	
	public boolean useOwnerID()
	{ return true; }
	
	public final TileEntity getTile()
	{ return this; }
	
	public final void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		writeTileData(tag);
		
		if(ownerID != null && useOwnerID())
		{
			LMNBTUtils.setUUID(tag, "OwnerID", ownerID, true);
		}
	}
	
	public final void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		ownerID = useOwnerID() ? LMNBTUtils.getUUID(tag, "OwnerID", true) : null;
		readTileData(tag);
	}
	
	public final Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeTileClientData(tag);
		
		if(ownerID != null && useOwnerID())
		{
			LMNBTUtils.setUUID(tag, "OID", ownerID, false);
		}
		
		return new SPacketUpdateTileEntity(getPos(), 0, tag);
	}
	
	public final void onDataPacket(NetworkManager m, SPacketUpdateTileEntity p)
	{
		NBTTagCompound data = p.getNbtCompound();
		ownerID = useOwnerID() ? LMNBTUtils.getUUID(data, "OID", false) : null;
		readTileClientData(data);
		onUpdatePacket();
		FTBLibClient.onGuiClientAction();
	}
	
	public void writeTileData(NBTTagCompound tag)
	{
	}
	
	public void readTileData(NBTTagCompound tag)
	{
	}
	
	public void writeTileClientData(NBTTagCompound tag)
	{
		writeTileData(tag);
	}
	
	public void readTileClientData(NBTTagCompound tag)
	{
		readTileData(tag);
	}
	
	public void onUpdatePacket()
	{
		if(getSync().rerender())
		{
			worldObj.markBlockRangeForRenderUpdate(getPos(), getPos());
		}
	}
	
	public final boolean rerenderBlock()
	{ return false; }
	
	public EnumSync getSync()
	{ return EnumSync.SYNC; }
	
	public boolean onRightClick(EntityPlayer ep, ItemStack is, EnumFacing side, float x, float y, float z)
	{
		return false;
	}
	
	public void onLoad()
	{ isLoaded = true; }
	
	public void onChunkUnload()
	{ isLoaded = false; }
	
	public final void update()
	{
		onUpdate();
		
		if(isDirty)
		{
			if(getSide().isServer())
			{
				sendDirtyUpdate();
			}
			
			isDirty = false;
		}
	}
	
	public void onUpdate() { }
	
	public void markDirty()
	{ isDirty = true; }
	
	public void sendDirtyUpdate()
	{
		if(worldObj != null)
		{
			currentState = worldObj.getBlockState(pos);
			
			if(getSync().sync())
			{
				//FIXME: Send update
				//worldObj.send(getPos(), this);
			}
			
			worldObj.markChunkDirty(pos, this);
			
			if(getBlockType() != Blocks.air)
			{
				worldObj.updateComparatorOutputLevel(pos, getBlockType());
			}
		}
	}
	
	public void onPlacedBy(EntityPlayer ep, ItemStack is, IBlockState state)
	{
		if(!(ep instanceof FakePlayer))
		{
			ownerID = ep.getGameProfile().getId();
		}
		
		if(is.hasDisplayName()) setName(is.getDisplayName());
		
		markDirty();
	}
	
	public void onBroken(IBlockState state)
	{
	}
	
	public final void printOwner(EntityPlayer ep)
	{
		if(ep == null) return;
		String ownerS = "None";
		if(ownerID != null)
		{
			ForgePlayer player = ForgeWorld.getFrom(getSide()).getPlayer(ep);
			if(player != null) ownerS = player.getProfile().getName();
			else ownerS = ownerID.toString();
		}
		
		ep.addChatMessage(FTBLibMod.mod.chatComponent("owner", ownerS));
	}
	
	public boolean recolourBlock(EnumFacing side, EnumDyeColor col)
	{ return false; }
	
	public PrivacyLevel getPrivacyLevel()
	{ return PrivacyLevel.PUBLIC; }
	
	/**
	 * Player can be null
	 */
	public boolean isMinable(EntityPlayer ep)
	{ return ep == null || getPrivacyLevel().canInteract(ForgeWorldMP.inst.getPlayer(ownerID), ForgeWorldMP.inst.getPlayer(ep)); }
	
	public boolean isExplosionResistant()
	{ return !getPrivacyLevel().isPublic(); }
	
	public final void sendClientAction(String action, NBTTagCompound data)
	{ new MessageClientTileAction(this, action, data).sendToServer(); }
	
	public void clientPressButton(String button, int mouseButton, NBTTagCompound data)
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("ID", button);
		tag.setByte("MB", (byte) mouseButton);
		if(data != null) tag.setTag("D", data);
		sendClientAction(ACTION_BUTTON_PRESSED, tag);
	}
	
	public final void clientPressButton(String button, int mouseButton)
	{ clientPressButton(button, mouseButton, null); }
	
	public void clientOpenGui(NBTTagCompound data)
	{ sendClientAction(ACTION_OPEN_GUI, data); }
	
	public void clientCustomName(String name)
	{
		NBTTagCompound data = new NBTTagCompound();
		data.setString("Name", name);
		sendClientAction(ACTION_CUSTOM_NAME, data);
	}
	
	public void onClientAction(EntityPlayerMP ep, String action, NBTTagCompound data)
	{
		switch(action)
		{
			case ACTION_BUTTON_PRESSED:
				handleButton(data.getString("ID"), data.getByte("MB"), data.getCompoundTag("D"), ep);
				markDirty();
				break;
			case ACTION_OPEN_GUI:
				FTBLib.openGui(ep, (IGuiTile) this, data);
				break;
			case ACTION_CUSTOM_NAME:
				String name = data.getString("Name");
				if(!name.isEmpty()) setName(name);
				markDirty();
				break;
		}
	}
	
	public void handleButton(String button, int mouseButton, NBTTagCompound data, EntityPlayerMP ep)
	{
	}
	
	public final Side getSide()
	{ return (worldObj != null && !worldObj.isRemote) ? Side.SERVER : Side.CLIENT; }
	
	public void notifyNeighbors()
	{ worldObj.notifyBlockOfStateChange(getPos(), getBlockType()); }
	
	public DimensionType getDimension()
	{ return worldObj == null ? DimensionType.OVERWORLD : worldObj.provider.getDimensionType(); }
	
	public final int hashCode()
	{ return LMUtils.hashCode(getPos(), getDimension()); }
	
	public final boolean equals(Object o)
	{
		if(o == null) return false;
		if(o == this) return true;
		
		if(o instanceof TileLM)
		{
			TileLM t = (TileLM) o;
			return t.getDimension() == getDimension() && t.getPos().equals(getPos());
		}
		
		return false;
	}
	
	public void onNeighborBlockChange(BlockPos pos)
	{
		if(worldObj != null)
		{
			redstonePowered = worldObj.isBlockPowered(getPos());
			updateBlockState();
		}
	}
	
	public final void updateBlockState()
	{
		currentState = (worldObj != null) ? worldObj.getBlockState(getPos()) : null;
	}
	
	public void setName(String s) { }
	
	public String getName()
	{ return ""; }
	
	public boolean hasCustomName()
	{ return !getName().isEmpty(); }
	
	public ITextComponent getDisplayName()
	{ return hasCustomName() ? new TextComponentString(getName()) : new TextComponentTranslation(getBlockType().getLocalizedName() + ".name"); }
}