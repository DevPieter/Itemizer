package nl.devpieter.itemizer.models.cooldown;

import nl.devpieter.itemizer.managers.CooldownManager;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public record CooldownKey<T>(Class<?> clazz, String key, Duration defaultDuration) {

    public CooldownKey(Class<?> clazz, String key) {
        this(clazz, key, Duration.ofSeconds(0));
    }

    public void start(T identifier) {
        CooldownManager.getInstance().startCooldown(this, identifier);
    }

    public void start(T identifier, long duration) {
        CooldownManager.getInstance().startCooldown(this, identifier, duration);
    }

    public void start(T identifier, Duration duration) {
        CooldownManager.getInstance().startCooldown(this, identifier, duration);
    }

    public boolean isOnCooldown(T identifier) {
        return CooldownManager.getInstance().isOnCooldown(this, identifier);
    }

    public boolean isOrStartCooldown(T identifier) {
        return CooldownManager.getInstance().isOrStartCooldown(this, identifier);
    }

    public boolean isOrStartCooldown(T identifier, long duration) {
        return CooldownManager.getInstance().isOrStartCooldown(this, identifier, duration);
    }

    public boolean isOrStartCooldown(T identifier, Duration duration) {
        return CooldownManager.getInstance().isOrStartCooldown(this, identifier, duration);
    }

    public void cancel(T identifier) {
        CooldownManager.getInstance().removeCooldown(this, identifier);
    }

    public void cancelAll() {
        CooldownManager.getInstance().removeCooldowns(this);
    }

    @Override
    public @NotNull String toString() {
        String classPackage = clazz.getPackageName();
        String className = clazz.getSimpleName();

        return classPackage + "." + className + "." + key;
    }
}
