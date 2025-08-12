package nl.devpieter.itemizer.managers;

import nl.devpieter.itemizer.models.cooldown.CooldownHolder;
import nl.devpieter.itemizer.models.cooldown.CooldownKey;

import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;

public class CooldownManager {

    private static CooldownManager INSTANCE;

    private final HashMap<CooldownKey<?>, HashMap<Object, CooldownHolder>> cooldowns = new HashMap<>();

    public CooldownManager() {
    }

    public static CooldownManager getInstance() {
        if (INSTANCE == null) INSTANCE = new CooldownManager();
        return INSTANCE;
    }

    public void startCooldown(CooldownKey<?> key, Object identifier) {
        this.startCooldown(key, identifier, key.defaultDuration());
    }

    public void startCooldown(CooldownKey<?> key, Object identifier, long duration) {
        this.startCooldown(key, identifier, Duration.ofMillis(duration));
    }

    public void startCooldown(CooldownKey<?> key, Object identifier, Duration duration) {
        if (!this.cooldowns.containsKey(key)) this.cooldowns.put(key, new HashMap<>());

        HashMap<Object, CooldownHolder> map = this.cooldowns.get(key);
        if (map == null) return;

        CooldownHolder holder = new CooldownHolder(duration, System.currentTimeMillis());
        map.put(identifier, holder);
    }

    public boolean isOnCooldown(CooldownKey<?> key, Object identifier) {
        if (!this.cooldowns.containsKey(key)) return false;

        HashMap<Object, CooldownHolder> map = this.cooldowns.get(key);
        if (map == null) return false;

        CooldownHolder holder = map.get(identifier);
        if (holder == null) return false;

        boolean expired = holder.hasExpired();

        if (expired) {
            map.remove(identifier);
            if (map.isEmpty()) this.cooldowns.remove(key);
        }

        return !expired;
    }

    public boolean isOrStartCooldown(CooldownKey<?> key, Object identifier) {
        return this.isOrStartCooldown(key, identifier, key.defaultDuration());
    }

    public boolean isOrStartCooldown(CooldownKey<?> key, Object identifier, long duration) {
        return this.isOrStartCooldown(key, identifier, Duration.ofMillis(duration));
    }

    public boolean isOrStartCooldown(CooldownKey<?> key, Object identifier, Duration duration) {
        if (this.isOnCooldown(key, identifier)) return true;

        this.startCooldown(key, identifier, duration);
        return false;
    }

    public long getRemainingTime(CooldownKey<?> key, Object identifier) {
        if (!this.cooldowns.containsKey(key)) return 0;

        HashMap<Object, CooldownHolder> map = this.cooldowns.get(key);
        if (map == null) return 0;

        CooldownHolder holder = map.get(identifier);
        if (holder == null) return 0;

        return Duration.ofMillis(System.currentTimeMillis() - holder.start()).compareTo(holder.duration());
    }

    public void removeCooldown(CooldownKey<?> key, Object identifier) {
        if (!this.cooldowns.containsKey(key)) return;

        HashMap<Object, CooldownHolder> map = this.cooldowns.get(key);
        if (map == null) return;

        map.remove(identifier);
        if (map.isEmpty()) this.cooldowns.remove(key);
    }

    public void removeCooldowns(CooldownKey<?> key) {
        if (!this.cooldowns.containsKey(key)) return;

        HashMap<Object, CooldownHolder> map = this.cooldowns.get(key);
        if (map == null) return;

        map.clear();
        this.cooldowns.remove(key);
    }

    public void removeForgottenCooldowns() {
        Iterator<CooldownKey<?>> iterator = this.cooldowns.keySet().iterator();
        while (iterator.hasNext()) {
            CooldownKey<?> key = iterator.next();

            HashMap<Object, CooldownHolder> map = this.cooldowns.get(key);
            if (map == null) continue;

            map.entrySet().removeIf(entry -> entry.getValue().hasExpired());
            if (map.isEmpty()) iterator.remove();
        }
    }
}
