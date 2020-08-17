package de.karasuma.discordbot.conannews.welcome.eventlistener;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildMemberJoinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        System.out.println("called onGuildMemberJoin");
        event.getGuild().getTextChannels().get(0).sendMessage("hi").queue();
    }
}
