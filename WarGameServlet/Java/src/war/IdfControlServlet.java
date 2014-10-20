package war;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Missiles.Enemy_Missile;
import launchers.Enemy_Launcher;
import launchers.Iron_Dome;
import launchers.Launcher_Destroyer;
import war.War;

@WebServlet(name = "IdfControlServlet", urlPatterns = { "/IdfControlServlet" })
public class IdfControlServlet extends HttpServlet {

	public static final String LAUNCHER = "launcher";
	public static final String MISSILE = "missile";
	public static final int TAKES_TIME_MIN = 1;
	public static final int TAKES_TIME_MAX = 10;
	private War war;
	private static final long serialVersionUID = 1L;
	private String status;

	@Override
	public void init() throws ServletException {
		try {
			this.war = new War();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		status = "";

		StringBuilder launcherList = new StringBuilder();
		StringBuilder ironDomeList = new StringBuilder();
		StringBuilder missileList = new StringBuilder();
		StringBuilder destroyersList = new StringBuilder();
		for (Enemy_Launcher launcher : war.getLaunchers()) {
			launcherList.append("  " + launcher.getLauncherId() + " ");
		}
		for (Iron_Dome dome : war.getIronDomes()) {
			ironDomeList.append("  " + dome.getDomeId() + " ");
		}

		for (Enemy_Missile missile : war.getAllMissiles()) {
			missileList.append("  " + missile.getID() + "  ");
		}

		for (Launcher_Destroyer destroyer : war.getWarLauncherDestroyer()) {
			destroyersList.append("  " + destroyer.getLauncherType() + "#"
					+ destroyer.getLauncherId() + " ");
		}

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		// interceptMissile button was clicked
		if (request.getParameter("interceptMissile") != null) {

			status = interceptMissile(request.getParameter("ironDomeId"),
					request.getParameter("missileId"));

			out.println("<html>");
			out.println(status);
			out.println("<html>");
		}
		// destroyLauncher button was clicked
		else if (request.getParameter("destroyLauncher") != null) {
			String status = destroyLauncher(request.getParameter("destroyerType"),
					request.getParameter("destroyerId"),
					request.getParameter("launcherId"));

			out.println("<html>");
			out.println(status);
			out.println("<html>");

		}

	}

	private String destroyLauncher(String destroyerType, String destroyerId,
			String launcherId) {
		try {
			int destroyer_id = Integer.parseInt(destroyerId);
		} catch (Exception e) {
			return "Wrong Data";
		}
		
	 if (war.findDestroyerById(Integer.parseInt(destroyerId)) == null
				|| war.findLauncherById(launcherId) == null){
			return "Wrong Data";

		}
		else{
			war.DestroyLauncher("0", launcherId, war.findDestroyerById(Integer.parseInt(destroyerId)));
			return "Destroyed";
		}
		
	}

	public String interceptMissile(String ironDomeId, String missileId) {

		if (war.findIronDomeById(ironDomeId) == null
				|| war.findMissileById(missileId) == null) {
			return "Wrong Data";
		} else {
			war.InterceptMissile("0", war.findMissileById(missileId).getID(),
					war.findIronDomeById(ironDomeId));
			return "Intercepted";
		}

		

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);

	}
}
