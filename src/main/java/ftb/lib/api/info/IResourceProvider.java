package ftb.lib.api.info;

import latmod.lib.net.LMConnection;

/**
 * Created by LatvianModder on 09.05.2016.
 */
public interface IResourceProvider
{
	LMConnection getConnection(String s);
}