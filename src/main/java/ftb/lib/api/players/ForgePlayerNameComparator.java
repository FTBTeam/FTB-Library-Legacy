package ftb.lib.api.players;

import java.util.Comparator;

public class ForgePlayerNameComparator implements Comparator<ForgePlayer>
{
	public int compare(ForgePlayer o1, ForgePlayer o2)
	{
		return o1.getProfile().getName().compareToIgnoreCase(o2.getProfile().getName());
	}
}