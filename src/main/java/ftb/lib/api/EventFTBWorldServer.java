package ftb.lib.api;

import ftb.lib.FTBWorld;
import net.minecraft.world.World;

public class EventFTBWorldServer extends EventLM
{
	public final FTBWorld world;
	public final World worldMC;
	
	public EventFTBWorldServer(FTBWorld w, World w1)
	{ world = w; worldMC = w1; }
}