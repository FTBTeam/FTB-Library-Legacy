package ftb.lib.api.tile;

import ftb.lib.*;
import ftb.lib.api.block.BlockLM;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.friends.*;
import ftb.lib.mod.FTBLibMod;
import ftb.lib.mod.net.MessageClientTileAction;
import latmod.lib.LMUtils;
import latmod.lib.json.UUIDTypeAdapterLM;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.*;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IWorldNameable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class TileLM extends TileEntity implements IClientActionTile, IWorldNameable
{
	public static final String ACTION_BUTTON_PRESSED = "button";
	public static final String ACTION_OPEN_GUI = "open_gui";
	public static final String ACTION_CUSTOM_NAME = "custom_name";
	
	public static final int[] NO_SLOTS = new int[0];
	
	private boolean isDirty = true;
	public boolean isLoaded = false;
	public long tick = 0L;
	public UUID ownerID = null;
	public boolean redstonePowered = false;
	
	public final void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		readTileData(tag);
	}
	
	public final void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		writeTileData(tag);
	}
	
	public final Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeTileData(tag);
		writeTileClientData(tag);
		return new S35PacketUpdateTileEntity(getPos(), 0, tag);
	}
	
	public final void onDataPacket(NetworkManager m, S35PacketUpdateTileEntity p)
	{
		NBTTagCompound tag = p.getNbtCompound();
		readTileData(tag);
		readTileClientData(tag);
		FTBLibClient.onGuiClientAction();
	}
	
	public void readTileData(NBTTagCompound tag)
	{
		ownerID = UUIDTypeAdapterLM.getUUID(tag.getString("OwnerID"));
		String customName = tag.getString("CustomName");
		if(!customName.isEmpty()) setName(customName);
		tick = tag.getLong("Tick");
		if(tick < 0L) tick = 0L;
	}
	
	public void writeTileData(NBTTagCompound tag)
	{
		if(ownerID != null) tag.setString("OwnerID", UUIDTypeAdapterLM.getString(ownerID));
		String customName = getName();
		if(customName == null && !customName.isEmpty()) tag.setString("CustomName", customName);
		if(tick < 0L) tick = 0L;
		tag.setLong("Tick", tick);
	}
	
	public void readTileClientData(NBTTagCompound tag)
	{
		readTileData(tag);
	}
	
	public void writeTileClientData(NBTTagCompound tag)
	{
		writeTileData(tag);
	}
	
	public void onUpdatePacket()
	{
		//TODO: Check this
		if(rerenderBlock()) worldObj.markBlockRangeForRenderUpdate(getPos(), getPos());
	}
	
	public boolean rerenderBlock()
	{ return false; }
	
	public boolean onRightClick(EntityPlayer ep, ItemStack is, EnumFacing side, float x, float y, float z)
	{
		return false;
	}
	
	public void invalidate()
	{
		if(isLoaded) onUnloaded();
		super.invalidate();
	}
	
	public void onChunkUnload()
	{
		if(isLoaded) onUnloaded();
		super.onChunkUnload();
	}
	
	public void onLoaded()
	{
		blockType = getBlockType();
		
		if(blockType != null)
		{
			isLoaded = true;
			//onNeighborBlockChange(blockType);
		}
	}
	
	public void onUnloaded()
	{
		isLoaded = false;
	}
	
	public final void updateEntity()
	{
		if(!isLoaded) onLoaded();
		
		onUpdate();
		
		if(isDirty)
		{
			isDirty = false;
			if(isServer()) sendDirtyUpdate();
		}
		
		tick++;
	}
	
	public void onUpdate() { }
	
	public void sendDirtyUpdate()
	{
		super.markDirty();
		//worldObj.markBlockForUpdate(getPos());
	}
	
	public void onPlacedBy(EntityPlayer ep, ItemStack is, IBlockState state)
	{
		if(!(ep instanceof FakePlayer))
		{
			ownerID = ep.getGameProfile().getId();
		}
		
		markDirty();
	}
	
	public void onBroken(IBlockState state)
	{
		sendDirtyUpdate();
	}
	
	public final void printOwner(EntityPlayer ep)
	{
		if(ep == null) return;
		String ownerS = "None";
		if(ownerID != null)
		{
			LMPlayer player = FTBLibMod.proxy.getForgeWorld(isServer() ? Side.SERVER : Side.CLIENT).getPlayer(ep);
			if(player != null) ownerS = player.getProfile().getName();
			else ownerS = ownerID.toString();
		}
		
		ep.addChatMessage(FTBLibMod.mod.chatComponent("owner", ownerS));
	}
	
	public BlockLM getBlockType()
	{
		Block b = super.getBlockType();
		if(b instanceof BlockLM) return (BlockLM) b;
		return null;
	}
	
	public boolean recolourBlock(EnumFacing side, EnumDyeColor col)
	{ return false; }
	
	public PrivacyLevel getPrivacyLevel()
	{ return PrivacyLevel.PUBLIC; }
	
	/**
	 * Player can be null
	 */
	public boolean isMinable(EntityPlayer ep)
	{ return ep == null || getPrivacyLevel().canInteract(LMWorldMP.inst.getPlayer(ownerID), LMWorldMP.inst.getPlayer(ep)); }
	
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
		if(action.equals(ACTION_BUTTON_PRESSED))
		{
			handleButton(data.getString("ID"), data.getByte("MB"), data.getCompoundTag("D"), ep);
			markDirty();
		}
		else if(action.equals(ACTION_OPEN_GUI)) FTBLib.openGui(ep, (IGuiTile) this, data);
		else if(action.equals(ACTION_CUSTOM_NAME))
		{
			String name = data.getString("Name");
			if(!name.isEmpty()) setName(name);
			markDirty();
		}
	}
	
	public void handleButton(String button, int mouseButton, NBTTagCompound data, EntityPlayerMP ep)
	{
	}
	
	public final boolean isServer()
	{ return !worldObj.isRemote; }
	
	public void notifyNeighbors()
	{ worldObj.notifyBlockOfStateChange(getPos(), blockType); }
	
	public int getDimension()
	{ return worldObj == null ? 0 : worldObj.provider.getDimensionId(); }
	
	public final int hashCode()
	{ return LMUtils.hashCode(getPos(), getDimension()); }
	
	public final boolean equals(Object o)
	{
		if(o == null) return false;
		if(o == this) return true;
		
		if(o.hashCode() == hashCode() && o instanceof TileLM)
		{
			TileLM t = (TileLM) o;
			return t.getDimension() == getDimension() && t.getPos().equals(getPos());
		}
		
		return false;
	}
	
	public void markDirty()
	{ isDirty = true; }
	
	public void onNeighborBlockChange(BlockPos pos)
	{ redstonePowered = worldObj.isBlockPowered(getPos()); }
	
	public TileEntity getTile(EnumFacing side)
	{ return worldObj.getTileEntity(getPos().offset(side)); }
	
	public IBlockState getBlockState(EnumFacing side)
	{ return worldObj.getBlockState(getPos().offset(side)); }
	
	public void setName(String s) { }
	
	public String getName()
	{ return ""; }
	
	public boolean hasCustomName()
	{ return !getName().isEmpty(); }
	
	public IChatComponent getDisplayName()
	{ return hasCustomName() ? new ChatComponentText(getName()) : new ChatComponentTranslation(getBlockType().getLocalizedName() + ".name"); }
}