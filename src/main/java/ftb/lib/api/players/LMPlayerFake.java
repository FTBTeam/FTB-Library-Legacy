package ftb.lib.api.players;

import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.FakePlayer;

import java.util.List;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public final class LMPlayerFake extends LMPlayerMP
{
	public LMPlayerFake(FakePlayer p)
	{
		super(p.getGameProfile());
		setPlayer(p);
	}
	
	public boolean isFake()
	{ return true; }
	
	public void sendUpdate() { }
	
	public boolean isOP()
	{ return false; }
	
	public void getInfo(LMPlayerMP owner, List<IChatComponent> info) { }
	
	public void refreshStats() { }
	
	public void checkNewFriends() { }
}