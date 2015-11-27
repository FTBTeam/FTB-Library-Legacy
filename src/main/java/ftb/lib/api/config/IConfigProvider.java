package ftb.lib.api.config;

import cpw.mods.fml.relauncher.*;
import latmod.lib.config.ConfigList;
import net.minecraft.util.IChatComponent;

@SideOnly(Side.CLIENT)
public interface IConfigProvider
{
	public IChatComponent getTitle();
	public ConfigList getList();
	public void save();
}