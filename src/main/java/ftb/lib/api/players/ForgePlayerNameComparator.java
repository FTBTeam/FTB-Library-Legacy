package ftb.lib.api.players;

import java.util.Comparator;

public class ForgePlayerNameComparator implements Comparator<LMPlayer>
{
	public int compare(LMPlayer o1, LMPlayer o2)
	{
		return o1.getProfile().getName().compareToIgnoreCase(o2.getProfile().getName());
	}
}