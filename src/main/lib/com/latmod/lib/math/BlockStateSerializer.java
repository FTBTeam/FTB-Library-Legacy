package com.latmod.lib.math;

import com.latmod.lib.util.LMUtils;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;

import javax.annotation.Nonnull;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 29.04.2016.
 */
public class BlockStateSerializer
{
    public static class StateEntry extends AbstractMap.SimpleImmutableEntry<IProperty<?>, Comparable<?>> implements Comparable<StateEntry>
    {
        public final String valueString;

        public StateEntry(IProperty<?> key, Comparable<?> value)
        {
            super(key, value);
            valueString = value instanceof String ? value.toString() : getKey().getName(LMUtils.convert(value));
        }

        @Override
        public String toString()
        {
            return getKey().getName() + '=' + valueString;
        }

        @Override
        public int compareTo(@Nonnull StateEntry o)
        {
            return getKey().getName().compareTo(o.getKey().getName());
        }
    }

    public static String getString(IBlockState state)
    {
        if(state == null)
        {
            return "normal";
        }

        int size = state.getProperties().size();

        List<StateEntry> entries = new ArrayList<>(size);

        for(Map.Entry<IProperty<?>, Comparable<?>> entry : state.getProperties().entrySet())
        {
            entries.add(new StateEntry(entry.getKey(), entry.getValue()));
        }

        Collections.sort(entries, null);

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < size; i++)
        {
            sb.append(entries.get(i));

            if(i != size - 1)
            {
                sb.append(',');
            }
        }

        return sb.toString();
    }
}
