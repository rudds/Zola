package jp.ac.kyoto_su.rudds.zola;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;


public class NetworkInterfaceTest {

	public static void main(String[] args) throws SocketException{
		
		try {
    		InetAddress address = InetAddress.getByName("10.10.10.2");

			NetworkInterface nit = NetworkInterface.getByInetAddress(address);
			System.out.println(nit.getDisplayName());
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO é©ìÆê∂ê¨Ç≥ÇÍÇΩ catch ÉuÉçÉbÉN
			e.printStackTrace();
		}
	
		/*
		java.util.Enumeration enuIfs = NetworkInterface.getNetworkInterfaces();
		if (null != enuIfs)
		{
		    while (enuIfs.hasMoreElements()) 
		    {
		        System.out.println("INTERFECE FOUND");
		        NetworkInterface ni = (NetworkInterface)enuIfs.nextElement();
		        System.out.println("getDisplayName:\t" + ni.getDisplayName());
		        System.out.println("getName:\t" + ni.getName());
		        java.util.Enumeration enuAddrs = ni.getInetAddresses();
		        while (enuAddrs.hasMoreElements()) 
		        {
		            InetAddress in4 = (InetAddress)enuAddrs.nextElement();
		            System.out.println("getHostAddress:\t" + in4.getHostAddress());
		        }
		    }
		}
		*/
		
	}
	
}
