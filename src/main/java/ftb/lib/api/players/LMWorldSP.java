package ftb.lib.api.players;

import com.mojang.authlib.GameProfile;
import ftb.lib.*;
import ftb.lib.api.GameMode;
import ftb.lib.api.client.FTBLibClient;
import latmod.lib.json.UUIDTypeAdapterLM;
import net.minecraft.nbt.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

/**
 * Created by LatvianModder on 09.02.2016.
 */
@SideOnly(Side.CLIENT)
public final class LMWorldSP extends LMWorld
{
	public static LMWorldSP inst = null;
	public final LMPlayerSPSelf clientPlayer;
	public List<String> serverDataIDs;
	
	public LMWorldSP(GameProfile p)
	{
		super(Side.CLIENT);
		clientPlayer = new LMPlayerSPSelf(p);
		serverDataIDs = new ArrayList<>();
	}
	
	public World getMCWorld()
	{ return FTBLibClient.mc.theWorld; }
	
	public LMWorldMP toWorldMP()
	{ return null; }
	
	public LMWorldSP toWorldSP()
	{ return this; }
	
	public LMPlayerSP getPlayer(Object o)
	{
		LMPlayer p = super.getPlayer(o);
		return (p == null) ? null : p.toPlayerSP();
	}
	
	public void readDataFromNet(NBTTagCompound tag, boolean first)
	{
		currentMode = new GameMode(tag.getString("M"));
		
		if(first)
		{
			playerMap.clear();
			
			GameProfile gp = FTBLibClient.mc.getSession().getProfile();
			//TODO: Improve this
			for(Map.Entry<String, NBTBase> e : LMNBTUtils.entrySet(tag.getCompoundTag("PM")))
			{
				UUID uuid = UUIDTypeAdapterLM.getUUID(e.getKey());
				String name = ((NBTTagString) e.getValue()).getString();
				
				if(uuid.equals(clientPlayer.getProfile().getId())) playerMap.put(uuid, clientPlayer);
				else playerMap.put(uuid, new LMPlayerSP(new GameProfile(uuid, name)));
			}
			
			FTBLib.dev_logger.info("Client player ID: " + clientPlayer.getProfile().getId() + " and " + (playerMap.size() - 1) + " other players");
			
			Map<String, NBTBase> map1 = LMNBTUtils.toMap(tag.getCompoundTag("PMD"));
			
			clientPlayer.readFromNet((NBTTagCompound) map1.get(clientPlayer.getStringUUID()), true);
			map1.remove(clientPlayer.getStringUUID());
			
			for(Map.Entry<String, NBTBase> e : map1.entrySet())
			{
				LMPlayerSP p = playerMap.get(UUIDTypeAdapterLM.getUUID(e.getKey())).toPlayerSP();
				p.readFromNet((NBTTagCompound) e.getValue(), false);
			}
		}
		
		//customCommonData.read(io);
	}
}
