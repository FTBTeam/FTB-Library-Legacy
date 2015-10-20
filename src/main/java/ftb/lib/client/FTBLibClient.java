package ftb.lib.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.*;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public class FTBLibClient
{
	public static final Minecraft mc = FMLClientHandler.instance().getClient();
}