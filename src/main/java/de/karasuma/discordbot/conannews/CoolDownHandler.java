package de.karasuma.discordbot.conannews;

import java.time.Instant;

public class CoolDownHandler {
    private long coolDownTimeInMillis = 10000;
    static long lastMessageSendTimestamp;

    public boolean isOnCoolDown() {
        return lastMessageSendTimestamp + coolDownTimeInMillis > Instant.now().toEpochMilli();
    }

    public void setOnCoolDown() {
        lastMessageSendTimestamp = Instant.now().toEpochMilli();
    }

    public long getCoolDownTimeInMillis() {
        return coolDownTimeInMillis;
    }

    public void setCoolDownTimeInMillis(long coolDownTimeInMillis) {
        this.coolDownTimeInMillis = coolDownTimeInMillis;
    }
}
