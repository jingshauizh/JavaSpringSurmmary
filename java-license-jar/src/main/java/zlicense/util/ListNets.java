
package zlicense.util;


import java.net.*;
import java.util.*;

import static java.lang.System.out;

public class ListNets {

	public static void main(String args[]) throws SocketException {	
		String ip = "150.236.220.200";
		String mac = "80-00-0B-56-3B-32";
		boolean flag = validatoIpAndMacAddress(ip, mac);
		boolean macflag = validateMacAddress( mac);
		out.printf("validatoMacAddress flag=%s\n", macflag);
		out.printf("validatoIpAndMacAddress flag=%s\n", flag);		
	}

	static void displayInterfaceInformation(NetworkInterface netint)
			throws SocketException {
		out.printf("Display name: %s\n", netint.getDisplayName());
		out.printf("Name: %s\n", netint.getName());
		byte[] mac = netint.getHardwareAddress();
		if (mac != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i],
						(i < mac.length - 1) ? "-" : ""));
			}
			System.out.println("mac=" + sb.toString());
		}

		Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
		for (InetAddress inetAddress : Collections.list(inetAddresses)) {
			out.printf("InetAddress: %s\n", inetAddress);
			System.out
					.println("InetAddress ip=" + inetAddress.getHostAddress());
		}
		out.printf("\n");
	}

	public static boolean validateMacAddress(String macAddress)
			throws SocketException {
		boolean returnFlag = false;
		Enumeration<NetworkInterface> nets = NetworkInterface
				.getNetworkInterfaces();
		for (NetworkInterface netint : Collections.list(nets)) {
			byte[] mac = netint.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			if (mac != null) {
				for (int i = 0; i < mac.length; i++) {
					sb.append(String.format("%02X%s", mac[i],
							(i < mac.length - 1) ? "-" : ""));
				}
				System.out.println("mac=" + sb.toString());
			}
			if (sb.toString().equals(macAddress)) {
				returnFlag = true;
			}
		}
		return returnFlag;

	}

	public static boolean validatoIpAndMacAddress(String ipAddress,
			String macAddress) throws SocketException {
		boolean returnFlag = false;
		Enumeration<NetworkInterface> nets = NetworkInterface
				.getNetworkInterfaces();
		for (NetworkInterface netint : Collections.list(nets)) {
			byte[] mac = netint.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			if (mac != null) {
				for (int i = 0; i < mac.length; i++) {
					sb.append(String.format("%02X%s", mac[i],
							(i < mac.length - 1) ? "-" : ""));
				}
				System.out.println("mac=" + sb.toString());
			}
			if (sb.toString().equals(macAddress)) {
				Enumeration<InetAddress> inetAddresses = netint
						.getInetAddresses();
				String ip = "";
				for (InetAddress inetAddress : Collections.list(inetAddresses)) {
					ip = inetAddress.getHostAddress();
					System.out.println("InetAddress ip="
							+ inetAddress.getHostAddress());
					if (ipAddress.toString().equals(ip)) {
						returnFlag = true;
					}
				}
			}
		}
		return returnFlag;

	}
}