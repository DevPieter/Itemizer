package nl.devpieter.itemizer.runners;

import nl.devpieter.itemizer.listeners.ShootableItemListener;
import nl.devpieter.itemizer.managers.CooldownManager;

public class CleanupRunner implements Runnable {

    private final CooldownManager cooldownManager = CooldownManager.getInstance();

    private final ShootableItemListener shootableItemListener;

    public CleanupRunner(ShootableItemListener shootableItemListener) {
        this.shootableItemListener = shootableItemListener;
    }

    @Override
    public void run() {
        this.cooldownManager.removeForgottenCooldowns();
        this.shootableItemListener.removeForgottenProjectiles();
    }
}
