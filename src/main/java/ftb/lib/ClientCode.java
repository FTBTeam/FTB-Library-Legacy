package ftb.lib;

import net.minecraftforge.fml.relauncher.*;

/**
 * Created by LatvianModder on 11.02.2016.
 */
public interface ClientCode
{
	@SideOnly(Side.CLIENT)
	void run();
}
