package com.latmod.lib.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@ParametersAreNonnullByDefault
public class LMFileUtils
{
    public static final File latmodHomeFolder = getFolder();

    public static final int KB = 1024;
    public static final int MB = KB * 1024;
    public static final int GB = MB * 1024;

    public static final double KB_D = 1024D;
    public static final double MB_D = KB_D * 1024D;
    public static final double GB_D = MB_D * 1024D;

    public static final Comparator<File> FILE_COMPARATOR = (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());
    public static final Comparator<File> DEEP_FILE_COMPARATOR = (o1, o2) -> o1.getAbsolutePath().compareToIgnoreCase(o2.getAbsolutePath());

    private static File getFolder()
    {
        //if(!f.exists()) f.mkdirs();
        return new File(System.getProperty("user.home"), "/LatMod/");
    }

    @Nonnull
    public static File newFile(File f)
    {
        if(f.exists())
        {
            return f;
        }

        try
        {
            File pf = f.getParentFile();
            if(!pf.exists())
            {
                pf.mkdirs();
            }
            f.createNewFile();
            return f;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return f;
    }

    public static void save(File f, List<String> al) throws Exception
    {
        save(f, LMStringUtils.fromStringList(al));
    }

    public static void save(File f, String s) throws Exception
    {
        FileWriter fw = new FileWriter(newFile(f));
        BufferedWriter br = new BufferedWriter(fw);
        br.write(s);
        br.close();
        fw.close();
    }

    @Nonnull
    public static List<String> load(File f) throws Exception
    {
        return LMStringUtils.readStringList(new FileInputStream(f));
    }

    @Nonnull
    public static String loadAsText(File f) throws Exception
    {
        return LMStringUtils.readString(new FileInputStream(f));
    }

    public static boolean downloadFile(String url, File out)
    {
        try
        {
            URL website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(out);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    @Nonnull
    public static List<File> listAll(File f)
    {
        List<File> l = new ArrayList<>();
        addAllFiles(l, f);
        return l;
    }

    public static void addAllFiles(Collection<File> l, File f)
    {
        if(f.isDirectory())
        {
            File[] fl = f.listFiles();

            if(fl != null && fl.length > 0)
            {
                for(File aFl : fl)
                {
                    addAllFiles(l, aFl);
                }
            }
        }
        else if(f.isFile())
        {
            l.add(f);
        }
    }

    public static long getSize(File f)
    {
        if(!f.exists())
        {
            return 0L;
        }
        else if(f.isFile())
        {
            return f.length();
        }
        else if(f.isDirectory())
        {
            long length = 0L;
            File[] f1 = f.listFiles();
            if(f1 != null && f1.length > 0)
            {
                for(File aF1 : f1)
                {
                    length += getSize(aF1);
                }
            }
            return length;
        }
        return 0L;
    }

    @Nonnull
    public static String getSizeS(double b)
    {
        if(b >= GB_D)
        {
            b /= GB_D;
            b = (long) (b * 10D) / 10D;
            return b + "GB";
        }
        else if(b >= MB_D)
        {
            b /= MB_D;
            b = (long) (b * 10D) / 10D;
            return b + "MB";
        }
        else if(b >= KB_D)
        {
            b /= KB_D;
            b = (long) (b * 10D) / 10D;
            return b + "KB";
        }

        return b + "B";
    }

    @Nonnull
    public static String getSizeS(File f)
    {
        return getSizeS(getSize(f));
    }

    public static void copyFile(File src, File dst) throws Exception
    {
        if(src.exists() && !src.equals(dst))
        {
            if(src.isDirectory() && dst.isDirectory())
            {
                for(File f : listAll(src))
                {
                    File dst1 = new File(dst.getAbsolutePath() + File.separatorChar + (f.getAbsolutePath().replace(src.getAbsolutePath(), "")));
                    copyFile(f, dst1);
                }
            }
            else
            {
                dst = newFile(dst);
                FileChannel srcC = new FileInputStream(src).getChannel();
                FileChannel dstC = new FileOutputStream(dst).getChannel();
                dstC.transferFrom(srcC, 0L, srcC.size());
                srcC.close();
                dstC.close();
            }
        }
    }

    public static boolean delete(File f)
    {
        if(!f.exists())
        {
            return false;
        }
        if(f.isFile())
        {
            return f.delete();
        }
        String[] files = f.list();
        for(String file : files)
        {
            delete(new File(f, file));
        }
        return f.delete();
    }

    @Nonnull
    public static File getSourceDirectory(Class<?> c)
    {
        return new File(c.getProtectionDomain().getCodeSource().getLocation().getFile());
    }

    @Nullable
    public static String getRawFileName(File f)
    {
        if(!f.exists())
        {
            return null;
        }
        else if(f.isDirectory())
        {
            return f.getName();
        }
        else if(f.isFile())
        {
            String s = f.getName();
            return s.substring(0, s.lastIndexOf('.'));
        }
        return null;
    }
}