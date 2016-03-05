package ftb.lib.api.players;

import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.FakePlayer;

import java.util.List;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public final class ForgePlayerFake extends ForgePlayerMP
{
	public ForgePlayerFake(FakePlayer p)
	{
		super(p.getGameProfile());
		setPlayer(p);
	}
	
	public boolean isFake()
	{ return true; }
	
	public void sendUpdate() { }
	
	public boolean isOP()
	{ return false; }
	
	public void getInfo(ForgePlayerMP owner, List<IChatComponent> info) { }
	
	public void refreshStats() { }
	
	public void checkNewFriends() { }
}