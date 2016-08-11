package com.latmod.lib.util;

import com.latmod.lib.IIDObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Made by LatvianModder
 */
@ParametersAreNonnullByDefault
public class LMUtils
{
    // Class / Object //

    public static final Comparator<Package> PACKAGE_COMPARATOR = (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());
    public static final Comparator<Object> ID_COMPARATOR = (o1, o2) -> LMUtils.getID(o1).compareToIgnoreCase(LMUtils.getID(o2));

    @Nullable
    public static <T> T convert(@Nullable Object t)
    {
        return (t == null) ? null : (T) t;
    }

    @SuppressWarnings("all")
    @Nonnull
    public static <E> E newObject(Class<?> c, Object... o) throws Exception
    {
        if(o != null && o.length > 0)
        {
            Class<?>[] params = new Class<?>[o.length];
            for(int i = 0; i < o.length; i++)
            {
                params[i] = o.getClass();
            }

            Constructor<?> c1 = c.getConstructor(params);
            return (E) c1.newInstance(o);
        }

        return (E) c.newInstance();
    }

    @Nonnull
    public static Package[] getAllPackages()
    {
        Package[] p = Package.getPackages();
        Arrays.sort(p, PACKAGE_COMPARATOR);
        return p;
    }

    @Nonnull
    public static Collection<Class<?>> addSubclasses(Class<?> c, Collection<Class<?>> al, boolean all)
    {
        if(al == null)
        {
            al = new HashSet<>();
        }

        List<Class<?>> al1 = new ArrayList<>();
        Collections.addAll(al1, c.getDeclaredClasses());
        if(all && !al1.isEmpty())
        {
            for(Class<?> anAl1 : al1)
            {
                al.addAll(addSubclasses(anAl1, null, true));
            }
        }

        al.addAll(al1);
        return al;
    }

    public static boolean areObjectsEqual(@Nullable Object o1, @Nullable Object o2, boolean allowNulls)
    {
        return (o1 == null && o2 == null) ? allowNulls : (!(o1 == null || o2 == null) && (o1 == o2 || o1.equals(o2)));
    }

    public static int hashCodeOf(@Nullable Object o)
    {
        return o == null ? 0 : o.hashCode();
    }

    public static int hashCode(Object... o)
    {
        if(o == null || o.length == 0)
        {
            return 0;
        }
        else if(o.length == 1)
        {
            return hashCodeOf(o[0]);
        }
        else
        {
            return Arrays.hashCode(o);
        }
    }

    public static long longHashCode(Object... o)
    {
        if(o == null || o.length == 0)
        {
            return 0L;
        }
        else if(o.length == 1)
        {
            return hashCodeOf(o[0]);
        }

        long h = 1L;
        for(Object anO : o)
        {
            h = h * 31L + hashCodeOf(anO);
        }

        return h;
    }

    // Net //

    @Nullable
    public static String getHostAddress()
    {
        try
        {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch(Exception e)
        {
            return null;
        }
    }

    @Nullable
    public static String getExternalAddress()
    {
        try
        {
            return LMStringUtils.readString(new URL("http://checkip.amazonaws.com").openStream());
        }
        catch(Exception e)
        {
            return null;
        }
    }

    // Misc //

    public static boolean openURI(URI uri) throws Exception
    {
        Class<?> oclass = Class.forName("java.awt.Desktop");
        Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null);
        oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, uri);
        return true;
    }

    public static void moveBytes(InputStream is, OutputStream os, boolean close) throws Exception
    {
        byte[] buffer = new byte[1024];
        int len;
        while((len = is.read(buffer, 0, buffer.length)) > 0)
        {
            os.write(buffer, 0, len);
        }
        os.flush();

        if(close)
        {
            is.close();
            os.close();
        }
    }

    public static String getID(@Nullable Object o)
    {
        if(o == null)
        {
            return null;
        }
        else if(o instanceof IIDObject)
        {
            return ((IIDObject) o).getID();
        }
        else
        {
            return o.toString();
        }
    }

    public static String fromUUID(@Nullable UUID id)
    {
        if(id != null)
        {
            long msb = id.getMostSignificantBits();
            long lsb = id.getLeastSignificantBits();
            StringBuilder sb = new StringBuilder(32);
            digitsUUID(sb, msb >> 32, 8);
            digitsUUID(sb, msb >> 16, 4);
            digitsUUID(sb, msb, 4);
            digitsUUID(sb, lsb >> 48, 4);
            digitsUUID(sb, lsb, 12);
            return sb.toString();
        }

        return null;
    }

    private static void digitsUUID(StringBuilder sb, long val, int digits)
    {
        long hi = 1L << (digits * 4);
        String s = Long.toHexString(hi | (val & (hi - 1)));
        sb.append(s, 1, s.length());
    }

    public static UUID fromString(@Nullable String s)
    {
        if(s == null || !(s.length() == 32 || s.length() == 36))
        {
            return null;
        }

        try
        {
            if(s.indexOf('-') != -1)
            {
                return UUID.fromString(s);
            }

            int l = s.length();
            StringBuilder sb = new StringBuilder(36);
            for(int i = 0; i < l; i++)
            {
                sb.append(s.charAt(i));
                if(i == 7 || i == 11 || i == 15 || i == 19)
                {
                    sb.append('-');
                }
            }

            return UUID.fromString(sb.toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Nonnull
    public static <E> List<E> getObjects(@Nullable Class<E> type, Class<?> fields, @Nullable Object obj) throws IllegalAccessException
    {
        List<E> l = new ArrayList<>();

        for(Field f : fields.getDeclaredFields())
        {
            f.setAccessible(true);
            Object o = f.get(obj);

            if(type == null || type.isAssignableFrom(o.getClass()))
            {
                l.add((E) o);
            }
        }

        return l;
    }
}