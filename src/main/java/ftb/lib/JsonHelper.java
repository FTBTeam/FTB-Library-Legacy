package ftb.lib;

import ftb.lib.api.item.ItemStackTypeAdapter;
import ftb.lib.notification.*;
import latmod.lib.LMJsonUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;

public class JsonHelper
{
	public static void init()
	{
		LMJsonUtils.register(IChatComponent.class, new IChatComponent.Serializer());
		LMJsonUtils.register(ChatStyle.class, new ChatStyle.Serializer());
		LMJsonUtils.register(ItemStack.class, new ItemStackTypeAdapter());
		LMJsonUtils.register(Notification.class, new Notification.Serializer());
		LMJsonUtils.register(MouseAction.class, new MouseAction.Serializer());
		//LMJsonUtils.register(NBTBase.class, NBTSerializer.instance);
	}
}