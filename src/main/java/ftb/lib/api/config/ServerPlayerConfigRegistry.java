package ftb.lib.api.config;

import latmod.lib.util.FinalIDObject;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public class ServerPlayerConfigRegistry
{
	public static final Map<String, PlayerConfig> map = new HashMap<>();
	
	public static abstract class PlayerConfig<N extends NBTBase> extends FinalIDObject implements INBTSerializable<N>
	{
		public PlayerConfig(String id)
		{
			super(id);
		}
		
		public abstract EnumChatFormatting getValueColor();
		public abstract IChatComponent getValueString();
	}
	
	public static class BooleanPlayerConfig extends PlayerConfig<NBTTagByte>
	{
		private boolean value;
		
		public BooleanPlayerConfig(String id)
		{ super(id); }
		
		public void set(boolean v)
		{ value = v; }
		
		public boolean get()
		{ return value; }
		
		public final EnumChatFormatting getValueColor()
		{ return get() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED; }
		
		public final IChatComponent getValueString()
		{ return new ChatComponentText(get() ? "true" : "false"); }
		
		public NBTTagByte serializeNBT()
		{ return new NBTTagByte((byte) (get() ? 1 : 0)); }
		
		public void deserializeNBT(NBTTagByte nbt)
		{ set(nbt.getByte() == 1); }
	}
}
