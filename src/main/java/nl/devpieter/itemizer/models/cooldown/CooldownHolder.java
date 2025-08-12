package nl.devpieter.itemizer.models.cooldown;

import java.time.Duration;

public record CooldownHolder(Duration duration, long start) {

    public boolean hasExpired() {
        return Duration.ofMillis(System.currentTimeMillis() - start).compareTo(duration) >= 0;
    }
}
