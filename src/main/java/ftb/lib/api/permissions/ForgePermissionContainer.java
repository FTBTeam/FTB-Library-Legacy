package ftb.lib.api.permissions;

import latmod.lib.annotations.IInfoContainer;
import latmod.lib.util.FinalIDObject;

/**
 * Created by LatvianModder on 24.02.2016.
 */
public class ForgePermissionContainer extends FinalIDObject implements IInfoContainer
{
	public final boolean playerValue;
	private String[] info;
	
	ForgePermissionContainer(String id, boolean def)
	{
		super(id);
		playerValue = def;
	}
	
	public static String getID(String id)
	{
		if(id == null || id.isEmpty())
		{
			throw new NullPointerException("Permission ID can't be blank!");
		}
		else if(id.indexOf('.') == -1)
		{
			throw new IllegalArgumentException("Invalid ForgePermission: " + id + "! It must contain at least 1 '.'");
		}
		else
		{
			for(int i = 0; i < id.length(); i++)
			{
				char c = id.charAt(i);
				
				if(Character.isUpperCase(c))
				{
					throw new IllegalArgumentException("Invalid ForgePermission ID: " + id + "! Can't contain uppercase letters");
				}
				else if(c == '.' || (c >= '0' && c <= '9') || (c >= 'a' || c <= 'z')) { }
				else
				{
					throw new IllegalArgumentException("Invalid ForgePermission ID: " + id + "! Can't contain '" + c + "'");
				}
			}
		}
		
		return id;
	}
	
	@Override
	public void setInfo(String[] s)
	{ info = s; }
	
	@Override
	public String[] getInfo()
	{ return info; }
}
