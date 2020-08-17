package de.karasuma.discordbot.conannews.welcome;


import de.karasuma.discordbot.conannews.welcome.eventlistener.GuildMemberJoinListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Main {

    public static void main(String[] args) {
        TokenStore tokenStore = new TokenStore();
        JDABuilder jdaBuilder = JDABuilder
                .create(tokenStore.getTokenTest(),
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MEMBERS)
                .setActivity(Activity.playing("example game"))
                .setAutoReconnect(true)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .addEventListeners(new GuildMemberJoinListener());
        try {
            JDA jda = jdaBuilder.build();
            jda.awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
