package ftb.lib.api.friends;

import net.minecraft.world.WorldServer;

/**
 * Created by LatvianModder on 16.02.2016.
 */
public interface ITickingLMWorld
{
	void onTick(WorldServer w, long now);
}
