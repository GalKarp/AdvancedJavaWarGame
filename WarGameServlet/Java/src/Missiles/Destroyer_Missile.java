package Missiles;


import java.util.logging.Level;

import launchers.Enemy_Launcher;
import launchers.Launcher_Destroyer;
import war.War;

public class Destroyer_Missile extends Thread {

    private int destructTime;
    private String id;
    private Launcher_Destroyer father;
    private Enemy_Launcher launcherToDestroy;

    /**
     * Destroyer_Missile constructor
     * 
     * @param time
     * @param id
     * @param launcher_Destroyer
     * @param launcher
     */
    public Destroyer_Missile(String time, String id,
	    Launcher_Destroyer launcher_Destroyer, Enemy_Launcher launcher) {
	this.launcherToDestroy = launcher;
	this.destructTime = Integer.parseInt(time);
	this.id = id;
	this.father = launcher_Destroyer;
	start();
    }

    /**
     * Destroyer_Missile constructor
     * 
     * @param launcher
     * @param destructTime
     * @param launcher_Destroyer
     */
    public Destroyer_Missile(Enemy_Launcher launcher, String destructTime,
	    Launcher_Destroyer launcher_Destroyer) {
	this.launcherToDestroy = launcher;
	this.destructTime = Integer.parseInt(destructTime);
	this.launcherToDestroy = launcher;
	this.father = launcher_Destroyer;
	start();
    }

    /**
     * Destroys the luncher if its not hidden
     * 
     * @param launcherToDestroy
     *            --> assign the launcher that will be destroyed by the missile
     * 
     */
    private void destroyLauncher(Enemy_Launcher launcherToDestroy) {
	synchronized (this) {
	    if (launcherToDestroy.isHidden()) {
		War.theLogger.log(
			Level.INFO,
			" " + father.getLauncherType() + "#"
				+ father.getLauncherId()
				+ "Failed to destroy launcher "
				+ launcherToDestroy.getLauncherId(), father);
	    } else {
		War.theLogger.log(Level.INFO, " " + father.getLauncherType()
			+ "#" + father.getLauncherId() + " destroyed launcher "
			+ launcherToDestroy.getLauncherId(), father);
		War.theLogger.log(Level.INFO, " " + father.getLauncherType()
			+ "#" + father.getLauncherId() + " destroyed launcher "
			+ launcherToDestroy.getLauncherId(), launcherToDestroy);
		launcherToDestroy.setIsAlive(false);

	    }

	}

    }

    public void run() {
	try {
	    sleep(destructTime * War.THREAD_SLEEP_TIME);
	    destroyLauncher(launcherToDestroy);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    // Getters AND Setters
    public int getDestructTime() {
	return destructTime;
    }

    public void setDestructTime(int destructTime) {
	this.destructTime = destructTime;
    }

    public String getMissileId() {
	return id;
    }

    public void setMissileId(String id) {
	this.id = id;
    }

    public Launcher_Destroyer getFather() {
	return father;
    }

    public void setFather(Launcher_Destroyer father) {
	this.father = father;
    }

}
