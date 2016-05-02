package ftb.lib;

import ftb.lib.api.ForgePlayer;
import ftb.lib.api.gui.GuiIcons;
import ftb.lib.api.permissions.ForgePermissionRegistry;
import ftb.lib.mod.FTBLibPermissions;

public enum PrivacyLevel
{
	PUBLIC,
	PRIVATE,
	FRIENDS;
	
	public static final PrivacyLevel[] VALUES_3 = new PrivacyLevel[] {PUBLIC, PRIVATE, FRIENDS};
	public static final PrivacyLevel[] VALUES_2 = new PrivacyLevel[] {PUBLIC, PRIVATE};
	public static final String enumLangKey = "ftbl.security";
	
	public final String langKey;
	
	PrivacyLevel()
	{
		langKey = enumLangKey + '.' + name().toLowerCase();
	}
	
	public boolean isPublic()
	{ return this == PUBLIC; }
	
	public boolean isRestricted()
	{ return this == FRIENDS; }
	
	public PrivacyLevel next(PrivacyLevel[] l)
	{ return l[(ordinal() + 1) % l.length]; }
	
	public PrivacyLevel prev(PrivacyLevel[] l)
	{
		int id = ordinal() - 1;
		if(id < 0) { id = l.length - 1; }
		return l[id];
	}
	
	public TextureCoords getIcon()
	{ return GuiIcons.security[ordinal()]; }
	
	public boolean canInteract(ForgePlayer owner, ForgePlayer player)
	{
		if(FTBLib.ftbu == null) { return true; }
		if(this == PrivacyLevel.PUBLIC || owner == null) { return true; }
		if(player == null) { return false; }
		if(owner.equalsPlayer(player)) { return true; }
		if(player.isOnline() && ForgePermissionRegistry.hasPermission(FTBLibPermissions.interact_secure, player.getProfile()))
		{ return true; }
		if(this == PrivacyLevel.PRIVATE) { return false; }
		return this == PrivacyLevel.FRIENDS && owner.isFriend(player);
	}
	
	public static PrivacyLevel get(String s)
	{
		if(s == null || s.isEmpty()) { return PUBLIC; }
		else if(s.equalsIgnoreCase("friends")) { return FRIENDS; }
		else if(s.equalsIgnoreCase("private")) { return PRIVATE; }
		return PUBLIC;
	}
}