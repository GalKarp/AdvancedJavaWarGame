package launchers;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;

import Missiles.Destroyer_Missile;
import war.IdfControlServlet;
import war.War;
import logger.Handler;

public class Launcher_Destroyer extends Thread {

	private static int runningId = 1; // for setting id to launcher destryoer
	private int id;
	private String type;
	private boolean isAlive = true;
	private String destructAfterLaunch;

	/**
	 * Launcher Destroyer constructor
	 * 
	 * @param type
	 *            -->String, the type of the destroyer (ship/plane)
	 */
	public Launcher_Destroyer(String type) {
		this.type = type;
		// setting id for destroyer
		this.id = runningId++;
		try {
		  War.theLogger.addHandler((new Handler(type, id, this)));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (isAlive) {
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Add launcher to destroy by given launcher and time
	 * 
	 * @param launcher
	 *            --> Enemy_Launcher, the launcher that will be destroyed
	 * @param destructTime
	 *            --> String, the destruct time of the launcher
	 */
	public void addLauncherToDestroy(Enemy_Launcher launcher,
			String destructTime) {
		new Destroyer_Missile(launcher, destructTime, this);

	}

	/**
	 * Add launcher to destroy only by given launcher for user input
	 * 
	 * @param launcher
	 *            --> Enemy_Launcher, the launcher that will be destroyed
	 */
	public void destroyLauncher(Enemy_Launcher launcher) {
		new Destroyer_Missile(launcher.getLauncherId(), "0", this, launcher);
	}

	// Getters And setters
	public String getLauncherType() {
		return type;
	}

	public int getLauncherId() {
		return id;
	}

	public String getDestructAfterLaunch() {
		return destructAfterLaunch;
	}

	public void setDestructAfterLaunch(String destructAfterLaunch) {
		this.destructAfterLaunch = destructAfterLaunch;
	}

	public boolean alive() {
		return isAlive;
	}

}
