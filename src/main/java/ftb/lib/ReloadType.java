package ftb.lib;

import cpw.mods.fml.relauncher.Side;

/**
 * Created by LatvianModder on 24.04.2016.
 */
public enum ReloadType
{
	SYNC_ONLY,
	SERVER_ONLY,
	CLIENT_ONLY,
	SERVER_AND_CLIENT,
	SERVER_ONLY_NOTIFY_CLIENT;
	
	public boolean reload(Side side)
	{
		if(side == Side.SERVER)
		{
			return this == SERVER_ONLY || this == SERVER_AND_CLIENT || this == SERVER_ONLY_NOTIFY_CLIENT;
		}
		else
		{
			return this == CLIENT_ONLY || this == SERVER_AND_CLIENT;
		}
	}
}