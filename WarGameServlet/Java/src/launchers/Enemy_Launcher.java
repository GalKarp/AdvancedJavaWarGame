package launchers;

import java.util.LinkedList;
import java.util.Queue;
import logger.Handler;
import war.War;
import Missiles.Enemy_Missile;

public class Enemy_Launcher extends Thread {
	private String id;
	private boolean isHidden;
	private Queue<Enemy_Missile> missileQueue = new LinkedList<Enemy_Missile>();
	private Queue<Enemy_Missile> allMissiles = new LinkedList<Enemy_Missile>(); // statistics
	private boolean iSAlive = true;
	private Enemy_Missile currentMissile;

	/**
	 * Constructor for Enemy launcher
	 * 
	 * @param id
	 *            --> String, the id for the launcher
	 * @param isHidden
	 *            --> String, if the launcher is hidden or not, User write
	 *            true/false
	 */
	public Enemy_Launcher(String id, String isHidden) {
		this.id = id;
		this.isHidden = Boolean.parseBoolean(isHidden);
		try {
			War.theLogger.addHandler((new Handler(this.getClass().getName(),
					id, this)));
		} catch (Exception e) {

		}
	}

	/**
	 * Add a new missile to current launcher and start Thread
	 * 
	 * @param newMissile
	 *            --> Enemy_Missile, the missile you want to add
	 */
	public void addMissile(Enemy_Missile newMissile) {
		allMissiles.add(newMissile);
		missileQueue.add(newMissile);
		newMissile.start();
	}

	/**
	 * Taking out enemy missile from launcher missile queue and notify them for
	 * launch
	 */
	public void notifyMissile() {
		currentMissile = missileQueue.poll();
		if (currentMissile != null) {
			synchronized (currentMissile) {
				if (currentMissile.isAlive()) {
					currentMissile.notifyAll();
				}

			}
		}
	}

	/**
	 * while there are missile waiting for launch he activate notify method for
	 * notify next missile
	 */
	public void run() {
		while (iSAlive) {
			if (!missileQueue.isEmpty()) {
				notifyMissile();

			} else {
				synchronized (this) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Removes missile and setting is not alive
	 */
	public void removeMissile() {
		Enemy_Missile currentMissile = allMissiles.poll();
		if (currentMissile != null && !currentMissile.isAlive()) {
			currentMissile.setIsAlive(false);
		}
	}

	/**
	 * Removes SPECIFIC missile and setting is not alive
	 */
	public void removeMissile(Enemy_Missile enemy_Missile) {
		if (enemy_Missile != null && !enemy_Missile.isAlive()) {
			enemy_Missile.setIsAlive(false);
		}
		missileQueue.remove(enemy_Missile);
	}

	/**
	 * Set missile status ( alive or not) and remove them from the relevant
	 * queues Especially use for killing/destroy missiles
	 * 
	 * @param isAlive
	 *            --> boolean, if missile is alive or not
	 */
	public void setIsAlive(boolean isAlive) {
		this.iSAlive = isAlive;
		destroyAllMissiles();
		emptyMissileQueue();

	}

	/**
	 * If launcher is destroyed this method set live status for all his missile
	 * as false
	 */
	private void destroyAllMissiles() {
		for (Enemy_Missile enemy_Missile : missileQueue) {
			if (!(enemy_Missile.getMode() == Enemy_Missile.Mode.Launched))
				enemy_Missile.setIsAlive(false);
		}
		for (Enemy_Missile enemy_Missile : allMissiles) {
			if (!(enemy_Missile.getMode() == Enemy_Missile.Mode.Launched))
				enemy_Missile.setIsAlive(false);
		}

	}

	// Getters and Setters
	@Override
	public String toString() {
		return "Launcher id= " + id + ", isHidden= " + isHidden + " ";
	}

	public Enemy_Missile getCurrentMissile() {
		return currentMissile;
	}

	public String getLauncherId() {
		return id;
	}

	public Queue<Enemy_Missile> getAllMissiles() {
		return allMissiles;
	}

	public Queue<Enemy_Missile> getMissleQueue() {
		return allMissiles;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void emptyMissileQueue() {
		this.missileQueue.clear();
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	/**
	 * @return The live status of the missile
	 */
	public boolean alive() {
		return iSAlive;
	}

}
