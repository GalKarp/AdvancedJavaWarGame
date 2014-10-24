package war;

import java.io.File;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import launchers.Enemy_Launcher;
import launchers.Iron_Dome;
import launchers.Launcher_Destroyer;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Missiles.Enemy_Missile;

public class readXml {

	private Enemy_Launcher enemy_launcher;
	private Iron_Dome ironDome;
	private Launcher_Destroyer launcherDestroyer;

	public readXml(War war, Queue<Enemy_Launcher> launchers,
			Queue<Iron_Dome> ironDomes,
			Queue<Launcher_Destroyer> launcherDestroyers,
			Queue<Enemy_Missile> enemyMissiles) {
		try {
		    	//Write your own xml path
			// File("C:/Users/DELL-PC/git/Java_Project/Java/src/war2.xml");
			File file = new File(
//					"C:/Users/Andrey/Desktop/Java_Project/Java/src/war.xml");
			"C:/Users/DELL-PC/git/WarGameServlet/WarGameServlet/Java/src/war3.xml");
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			if (doc.hasChildNodes()) {
				printNote(doc.getChildNodes(), war);
			}
		} catch (Exception e) {
			e.getCause();
			e.getMessage();
			e.getStackTrace();
		}
	}

	/**
	 * Recursive method for reading the XML Element by element
	 * 
	 * @param nodeList
	 *            --> The element node that will be checked
	 * @param war
	 *            --> The war that running
	 */
	private void printNote(NodeList nodeList, War war) {
		for (int count = 0; count < nodeList.getLength(); count++) {
			Node tempNode = nodeList.item(count);

			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				if (tempNode.hasAttributes()) {
					NamedNodeMap nodeMap = tempNode.getAttributes();
					for (int i = 0; i < nodeMap.getLength(); i++) {
						if (tempNode.getNodeName() == "launcher") {
							// Creating launcher element by element
							// See constructor tooltip for Details
							enemy_launcher = new Enemy_Launcher(nodeMap.item(i)
									.getNodeValue(), nodeMap.item(++i)
									.getNodeValue());
							war.Create_enemy_launcher(enemy_launcher);
						} else if (tempNode.getNodeName() == "missile") {
							// Creating missile element by element
							// See constructor tooltip for Details
							Enemy_Missile missile = new Enemy_Missile(nodeMap
									.item(i).getNodeValue(), nodeMap.item(++i)
									.getNodeValue(), nodeMap.item(++i)
									.getNodeValue(), nodeMap.item(++i)
									.getNodeValue(), nodeMap.item(++i)
									.getNodeValue(), enemy_launcher);
							war.addMissileToLauncher(enemy_launcher, missile);

						} else if (tempNode.getNodeName() == "destructor") {
							// Creating destructor element by element
							// See constructor tooltip for Details
							if (nodeMap.item(i).getNodeName() == "id") {
								// Check if destructor is iron dome or
								// plane/ship
								try {
									ironDome = war.Create_Iron_Dome(nodeMap
											.item(i).getNodeValue());
								} catch (DOMException e) {
									e.printStackTrace();
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								launcherDestroyer = war
										.Create_Launcher_Destroyer(nodeMap
												.item(i).getNodeValue());
							}
						} else if (tempNode.getNodeName() == "destructedLanucher") {
							// Adding launcher to destroy
							war.DestroyLauncher(nodeMap.item(i).getNodeValue(),
									nodeMap.item(++i).getNodeValue(),
									launcherDestroyer);
						} else if (tempNode.getNodeName() == "destructdMissile") {
							// Adding missile to destroy
							war.InterceptMissile(
									nodeMap.item(i).getNodeValue(), nodeMap
											.item(++i).getNodeValue(), ironDome);
						}
					}
				}
				if (tempNode.hasChildNodes()) {
					// loop again if has child nodes
					printNote(tempNode.getChildNodes(), war);
				}
			}
		}
	}
}
