package com.ieat.gcm;

import java.util.HashSet;
import java.util.Set;

public class MyData {
	public static final String SERVER_KEY = "AIzaSyCm7wvQN_yjwg-MdHAgN_8WPMfSZOoyyv4";
	public static final int RETRIES = 5;
	public static final int MULTICAST_SIZE = 1000;
	private static Set<String> devicesRegistered = new HashSet<String>();

	/**
	 * register a device
	 */
	public static void register(String regId) {
		synchronized (devicesRegistered) {
			devicesRegistered.add(regId);
		}
		System.out.println("Registering " + regId);
	}
	
	/**
	 * unregister a device
	 */
	public static void unregister(String regId) {
		synchronized (devicesRegistered) {
			devicesRegistered.remove(regId);
		}
		System.out.println("Unregistering " + regId);
	}
	
	/**
	 * updates the registration id of a device
	 */
	public static void updateRegistration(String oldId, String newId) {
		System.out.println("Updating " + oldId + " to " + newId);
		synchronized (devicesRegistered) {
			devicesRegistered.remove(oldId);
			devicesRegistered.add(newId);
		}
	}

	/**
	 * gets all registered devices
	 */
	public static Set<String> getDevices() {
		synchronized (devicesRegistered) {
			// return new set to avoid modifying the original one
			return new HashSet<String>(devicesRegistered);
		}
	}
}
