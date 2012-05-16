/*
 * Zola: A platform for zero-configuration network federation
 * This platform has the following functions.
 * - Searching services that are enable in the LAN.
 * - Relaying local services to another LANs via Zola servers.
 * 
 * This class is the main class of this package.
 * 
 * @author Daiki Ito (2010-2012)
 * @author Naohiro Hayashibara (2012-)
 * @author Mitsuhiro Nakano (2012-)
 */

package jp.ac.kyoto_su.rudds.zola;

public class Zola {
	public static void main (String [] args) {
		new ZolaLauncher();
	}
}