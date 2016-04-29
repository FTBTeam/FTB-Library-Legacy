package ftb.lib;

import net.minecraft.block.properties.IProperty;

import java.util.AbstractMap;
import java.util.Arrays;

/**
 * Created by LatvianModder on 29.04.2016.
 */
public class BlockStateSerializer
{
	public static class StateEntry extends AbstractMap.SimpleImmutableEntry<IProperty<?>, Object> implements Comparable<StateEntry>
	{
		public StateEntry(IProperty<?> key, Object value)
		{ super(key, value); }
		
		@Override
		public String toString()
		{ return getKey().getName() + '=' + getKey().getName(getValueE()); }
		
		private <E> E getValueE()
		{ return (E) getValue(); }
		
		@Override
		public int compareTo(StateEntry o)
		{ return getKey().getName().compareTo(o.getKey().getName()); }
	}
	
	public static String getString(Object... o)
	{
		StateEntry[] entries = new StateEntry[o.length / 2];
		
		for(int i = 0; i < entries.length; i++)
		{
			entries[i] = new StateEntry((IProperty<?>) o[i * 2 + 0], o[i * 2 + 1]);
		}
		
		Arrays.sort(entries);
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < entries.length; i++)
		{
			sb.append(entries[i]);
			
			if(i != entries.length - 1)
			{
				sb.append(',');
			}
		}
		
		return sb.toString();
	}
}
