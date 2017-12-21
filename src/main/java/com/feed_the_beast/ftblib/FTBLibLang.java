package com.feed_the_beast.ftblib;

import com.feed_the_beast.ftblib.lib.util.LangKey;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

/**
 * @author LatvianModder
 */
public interface FTBLibLang
{
	LangKey WIP = LangKey.of("wip");
	LangKey DEPRECATED = LangKey.of("deprecated", String.class);
	LangKey EXAMPLE = LangKey.of("example");
	LangKey CLICK_HERE = LangKey.of("click_here").setDefaultStyle(new Style().setColor(TextFormatting.GOLD));
	LangKey FEATURE_DISABLED = LangKey.of("feature_disabled");
	LangKey ERROR = LangKey.of("error", String.class);
	LangKey OWNER = LangKey.of("owner", String.class);
	LangKey DELETE_ITEM = LangKey.of("delete_item", String.class);
	LangKey SERVER_FORCED = LangKey.of("server_forced", String.class);
	LangKey COMMANDS = LangKey.of("commands");
	LangKey MY_PERMISSIONS = LangKey.of("my_permissions");
	LangKey ALL_PERMISSIONS = LangKey.of("all_permissions");
	LangKey DIFFICULTY = LangKey.of("difficulty", String.class);
	LangKey MY_TEAM = LangKey.of("sidebar_button." + FTBLibFinals.MOD_ID + ".my_team");

	LangKey RELOAD_SERVER = FTBLibFinals.lang("lang.reload_server", String.class);
	LangKey RELOAD_CLIENT = FTBLibFinals.lang("lang.reload_client", String.class);
	LangKey RELOAD_FAILED = FTBLibFinals.lang("lang.reload_failed");

	LangKey MY_SERVER_SETTINGS = LangKey.of("player_config");
	LangKey COMMAND_PERMISSION = LangKey.of("commands.generic.permission");
	LangKey COMMAND_USAGE = LangKey.of("commands.generic.usage", String.class);
	LangKey PLAYER_NOT_FOUND = LangKey.of("commands.generic.player.notFound", String.class);

	LangKey CONFIG_COMMAND_INVALID_KEY = FTBLibFinals.lang("lang.config_command.invalid_key", String.class);
	LangKey CONFIG_COMMAND_SETTING = FTBLibFinals.lang("lang.config_command.setting", String.class, String.class);
	LangKey CONFIG_COMMAND_SET = FTBLibFinals.lang("lang.config_command.set", String.class, String.class);

	LangKey CONFIG_ADD_FAKE_PLAYER_INVALID_UUID = FTBLibFinals.lang("lang.add_fake_player.invalid_uuid");
	LangKey CONFIG_ADD_FAKE_PLAYER_PLAYER_EXISTS = FTBLibFinals.lang("lang.add_fake_player.player_exists");
	LangKey CONFIG_ADD_FAKE_PLAYER_ADDED = FTBLibFinals.lang("lang.add_fake_player.added", String.class);

	LangKey TEAM_CREATED = FTBLibFinals.lang("lang.team.created", String.class);
	LangKey TEAM_ID_ALREADY_EXISTS = FTBLibFinals.lang("lang.team.id_already_exists");
	LangKey TEAM_ID_INVALID = FTBLibFinals.lang("lang.team.id_invalid");
	LangKey TEAM_DELETED = FTBLibFinals.lang("lang.team.deleted", String.class);
	LangKey TEAM_MEMBER_LEFT = FTBLibFinals.lang("lang.team.member_left", String.class);
	LangKey TEAM_MEMBER_JOINED = FTBLibFinals.lang("lang.team.member_joined", String.class);
	LangKey TEAM_TRANSFERRED_OWNERSHIP = FTBLibFinals.lang("lang.team.transferred_ownership", String.class);
	LangKey TEAM_INVITED = FTBLibFinals.lang("lang.team.invited", String.class, String.class);
	LangKey TEAM_INVITED_YOU = FTBLibFinals.lang("lang.team.invited_you", String.class, String.class);
	LangKey TEAM_NOT_FOUND = FTBLibFinals.lang("lang.team.error.not_found");
	LangKey TEAM_NO_TEAM = FTBLibFinals.lang("lang.team.error.no_team");
	LangKey TEAM_MUST_LEAVE = FTBLibFinals.lang("lang.team.error.must_leave");
	LangKey TEAM_NOT_OWNER = FTBLibFinals.lang("lang.team.error.not_owner");
	LangKey TEAM_NOT_MEMBER = FTBLibFinals.lang("lang.team.error.not_member", String.class);
	LangKey TEAM_MUST_TRANSFER_OWNERSHIP = FTBLibFinals.lang("lang.team.error.must_transfer_ownership");
	LangKey TEAM_ALREADY_INVITED = FTBLibFinals.lang("lang.team.error.already_invited", String.class);
	LangKey TEAM_FAILED_TO_JOIN = FTBLibFinals.lang("lang.team.error.failed_to_join");
	LangKey TEAM_PERMISSION_SET = FTBLibFinals.lang("lang.team.permission.set", String.class, String.class, String.class);
	LangKey TEAM_PERMISSION_OWNER = FTBLibFinals.lang("lang.team.permission.owner");
	LangKey TEAM_NOTIFICATION = FTBLibFinals.lang("lang.team.notification", String.class, String.class);
	LangKey TEAM_NOTIFICATION_HIDE = FTBLibFinals.lang("lang.team.notification.hide");
	LangKey TEAM_CHAT_MESSAGE = FTBLibFinals.lang("lang.team.chat_message", String.class, String.class);

	LangKey TEAM_GUI_MEMBERS = FTBLibFinals.lang("lang.team.gui.members");
	LangKey TEAM_GUI_MEMBERS_INVITE = FTBLibFinals.lang("lang.team.gui.members.invite");
	LangKey TEAM_GUI_MEMBERS_REQUESTING_INVITE = FTBLibFinals.lang("lang.team.gui.members.requesting_invite");
	LangKey TEAM_GUI_MEMBERS_DENY_REQUEST = FTBLibFinals.lang("lang.team.gui.members.deny_request");
	LangKey TEAM_GUI_MEMBERS_CANCEL_INVITE = FTBLibFinals.lang("lang.team.gui.members.cancel_invite");
	LangKey TEAM_GUI_MEMBERS_KICK = FTBLibFinals.lang("lang.team.gui.members.kick");
	LangKey TEAM_GUI_ALLIES = FTBLibFinals.lang("lang.team.gui.allies");
	LangKey TEAM_GUI_MODS = FTBLibFinals.lang("lang.team.gui.mods");
	LangKey TEAM_GUI_ENEMIES = FTBLibFinals.lang("lang.team.gui.enemies");
	LangKey TEAM_GUI_LEAVE = FTBLibFinals.lang("lang.team.gui.leave");
	LangKey TEAM_GUI_TRANSFER_OWNERSHIP = FTBLibFinals.lang("lang.team.gui.transfer_ownership");
	LangKey TEAM_GUI_REQUESTING_INVITE = FTBLibFinals.lang("lang.team_status.requesting_invite");
}