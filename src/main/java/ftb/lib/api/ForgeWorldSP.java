package ftb.lib.api;

import com.mojang.authlib.GameProfile;
import ftb.lib.*;
import ftb.lib.api.client.FTBLibClient;
import latmod.lib.LMUtils;
import net.minecraft.nbt.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

/**
 * Created by LatvianModder on 09.02.2016.
 */
@SideOnly(Side.CLIENT)
public final class ForgeWorldSP extends ForgeWorld
{
	public static ForgeWorldSP inst = null;
	public final ForgePlayerSPSelf clientPlayer;
	public List<String> serverDataIDs;
	
	public ForgeWorldSP(GameProfile p)
	{
		super(Side.CLIENT);
		clientPlayer = new ForgePlayerSPSelf(p);
		serverDataIDs = new ArrayList<>();
	}
	
	public World getMCWorld()
	{ return FTBLibClient.mc.theWorld; }
	
	public ForgeWorldMP toWorldMP()
	{ return null; }
	
	public ForgeWorldSP toWorldSP()
	{ return this; }
	
	public ForgePlayerSP getPlayer(Object o)
	{
		ForgePlayer p = super.getPlayer(o);
		return (p == null) ? null : p.toPlayerSP();
	}
	
	public void readDataFromNet(NBTTagCompound tag, boolean login)
	{
		worldID = new UUID(tag.getLong("IDM"), tag.getLong("IDL"));
		currentMode = new GameMode(tag.getString("M"));
		
		if(login)
		{
			playerMap.clear();
			
			GameProfile gp = FTBLibClient.mc.getSession().getProfile();
			//TODO: Improve this
			clientPlayer.init();
			
			NBTTagCompound tag1 = tag.getCompoundTag("PM");
			
			for(String s : tag1.getKeySet())
			{
				UUID uuid = LMUtils.fromString(s);
				String name = tag1.getString(s);
				
				if(uuid.equals(clientPlayer.getProfile().getId())) playerMap.put(uuid, clientPlayer);
				else
				{
					ForgePlayerSP p = new ForgePlayerSP(new GameProfile(uuid, name));
					p.init();
					playerMap.put(uuid, p);
				}
			}
			
			FTBLib.dev_logger.info("Client player ID: " + clientPlayer.getProfile().getId() + " and " + (playerMap.size() - 1) + " other players");
			
			Map<String, NBTBase> map1 = LMNBTUtils.toMap(tag.getCompoundTag("PMD"));
			
			clientPlayer.readFromNet((NBTTagCompound) map1.get(clientPlayer.getStringUUID()), true);
			map1.remove(clientPlayer.getStringUUID());
			
			for(Map.Entry<String, NBTBase> e : map1.entrySet())
			{
				ForgePlayerSP p = playerMap.get(LMUtils.fromString(e.getKey())).toPlayerSP();
				p.readFromNet((NBTTagCompound) e.getValue(), false);
			}
		}
		
		serverDataIDs.clear();
		
		if(tag.hasKey("SFWD"))
		{
			NBTTagCompound tag1 = tag.getCompoundTag("SFWD");
			serverDataIDs.addAll(tag1.getKeySet());
			
			for(String s : serverDataIDs)
			{
				if(customData.containsKey(s))
				{
					NBTTagCompound tag2 = tag1.getCompoundTag(s);
					customData.get(s).readFromNet(tag2, login);
				}
			}
		}
		
		//customCommonData.read(io);
	}
}