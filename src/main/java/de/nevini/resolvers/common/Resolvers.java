package de.nevini.resolvers.common;

public class Resolvers {

    public static final ChannelResolver CHANNEL = new ChannelResolver();
    public static final FeedResolver FEED = new FeedResolver();
    public static final GameResolver GAME = new GameResolver();
    public static final GuildResolver GUILD = new GuildResolver();
    public static final MemberResolver MEMBER = new MemberResolver();
    public static final MemberAndBotResolver MEMBER_OR_BOT = new MemberAndBotResolver();
    public static final ModuleResolver MODULE = new ModuleResolver();
    public static final NodeResolver NODE = new NodeResolver();
    public static final PermissionResolver PERMISSION = new PermissionResolver();
    public static final RoleResolver ROLE = new RoleResolver();
    public static final TimestampResolver TIMESTAMP = new TimestampResolver();
    public static final VoiceChannelResolver VOICE_CHANNEL = new VoiceChannelResolver();

}
