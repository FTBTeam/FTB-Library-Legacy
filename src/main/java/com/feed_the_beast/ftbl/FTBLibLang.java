package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.LangKey;

/**
 * Created by LatvianModder on 13.04.2016.
 */
public class FTBLibLang
{
    public static final LangKey mode_loaded = get("mode.loaded");
    public static final LangKey mode_not_found = get("mode.not_found");
    public static final LangKey mode_already_set = get("mode.already_set");
    public static final LangKey mode_current = get("mode.current");
    public static final LangKey mode_list = get("mode.list");
    public static final LangKey reload_server = get("reload_server");
    public static final LangKey reload_client = get("reload_client");
    public static final LangKey reload_client_config = get("reload_client_config");
    //@Deprecated

    public static final LangKey raw = get("raw");
    public static final LangKey missing_args = get("missing_args");
    public static final LangKey feature_disabled = get("feature_disabled");
    public static final LangKey invalid_subcmd = get("invalid_subcmd");
    public static final LangKey client_config = new LangKey("client_config");
    public static final LangKey owner = get("owner");
    public static final LangKey delete_item = get("delete_item");
    public static final LangKey server_forced = get("server_forced");
    public static final LangKey commands = get("commands");
    public static final LangKey my_permissions = get("my_permissions");
    public static final LangKey difficulty = get("difficulty");

    public static final LangKey team_created = get("team.created");
    public static final LangKey team_deleted = get("team.deleted");
    public static final LangKey team_member_left = get("team.member_left");
    public static final LangKey team_member_joined = get("team.member_joined");
    public static final LangKey team_transfered_ownership = get("team.transfered_ownership");
    public static final LangKey team_invite = get("team.invite");
    public static final LangKey team_cancel_invite = get("team.cancel_invite");
    public static final LangKey team_not_found = get("team.error.team_not_found");
    public static final LangKey team_no_team = get("team.error.no_team");
    public static final LangKey team_must_leave = get("team.error.must_leave");
    public static final LangKey team_not_owner = get("team.error.not_owner");
    public static final LangKey team_not_member = get("team.error.not_member");
    public static final LangKey team_must_transfer_ownership = get("team.error.must_transfer_ownership");

    public static LangKey get(String s)
    {
        return new LangKey("ftbl.lang." + s);
    }
}
