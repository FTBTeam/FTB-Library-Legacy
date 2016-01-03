package ftb.lib;

import ftb.lib.mod.FTBLibFinals;
import latmod.lib.FastMap;

import javax.swing.*;
import java.util.Map;

/**
 * Created by LatvianModder on 04.01.2016.
 */
public class DevConsole
{
	public static final Tree text = new Tree()
	{
		public void set(String s, Object o)
		{ super.set(s, o); update(); }
	};

	private static JFrame frame = null;
	private static JTextArea textArea = new JTextArea();

	public static void update()
	{
		if(FTBLibFinals.DEV)
		{
			try
			{
				StringBuilder sb = new StringBuilder();
				text.toString(sb, "DevEnv", 0);
				textArea.setText(sb.toString());
			}
			catch(Exception ex)
			{ ex.printStackTrace(); }
		}
	}

	public static void open()
	{
		if(frame == null)
		{
			frame = new JFrame("FTBLib: DevEnv Console")
			{
				public void dispose(boolean b)
				{
					frame = null;
					text.clear();
					super.dispose();
				}
			};

			frame.setSize(400, 300);
			frame.setResizable(true);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			textArea.setEditable(false);
			frame.add(textArea);
		}

		if(!frame.isVisible()) frame.setVisible(true);
	}

	private static abstract class ConsoleObject
	{
		public abstract void toString(StringBuilder sb, String key, int level);

		public void printSpace(StringBuilder sb, int level)
		{
			for(int i = 0; i < level; i++)
			{ sb.append(' '); sb.append(' '); }
		}
	}

	public static class Line extends ConsoleObject
	{
		public final String line;

		public Line(String s)
		{ line = s; }

		public void toString(StringBuilder sb, String key, int level)
		{
			printSpace(sb, level);
			sb.append(key);
			sb.append(':');
			sb.append(' ');
			sb.append(line);
			sb.append('\n');
		}
	}

	public static class Tree extends ConsoleObject
	{
		private final Map<String, ConsoleObject> map = new FastMap<>();

		public void set(String s, Object o)
		{
			if(o instanceof Tree) map.put(s, (Tree)o);
			else map.put(s, new Line(String.valueOf(o)));
		}

		public void clear()
		{ map.clear(); }

		public void toString(StringBuilder sb, String key, int level)
		{
			printSpace(sb, level);
			sb.append(key);
			sb.append(':');
			sb.append('\n');
			for(Map.Entry<String, ConsoleObject> e : map.entrySet())
				e.getValue().toString(sb, e.getKey(), level + 1);
		}
	}
}