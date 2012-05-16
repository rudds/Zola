package jp.ac.kyoto_su.rudds.zola;
// Licensed under Apache License version 2.0
// Original license LGPL

//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA



import java.io.IOException;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

public class DaapRemote {

    public final static String REMOTE_TYPE = "_daap._tcp.local.";

  
    public static void main(String[] args) {

    	try {
    		InetAddress address = InetAddress.getByName(args[0]);
    		System.out.println(address);
    		
            System.out.println("Opening JmDNS...");
            JmDNS jmdns = JmDNS.create(address);
            System.out.println("Opened JmDNS!");
            System.out.println("\nPress r and Enter, to register Itunes Remote service 'Android-'");
            int b;
            while ((b = System.in.read()) != -1 && (char) b != 'r') {
                /* Stub */
            }



            ServiceInfo pairservice = ServiceInfo.create(REMOTE_TYPE, "murata", 3689, 0, 0, "test of afp");
            jmdns.registerService(pairservice);

            System.out.println("Service ip address is " + pairservice.getInet4Address());
            System.out.println("Press q and Enter, to quit");
            // int b;
            
            while ((b = System.in.read()) != -1 && (char) b != 'q') {
                /* Stub */
            }
            System.out.println("Closing JmDNS...");
            jmdns.unregisterService(pairservice);
            jmdns.unregisterAllServices();
            jmdns.close();
            System.out.println("Done!");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
