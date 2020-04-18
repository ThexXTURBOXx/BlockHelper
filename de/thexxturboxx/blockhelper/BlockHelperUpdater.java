package de.thexxturboxx.blockhelper;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BlockHelperUpdater implements Runnable {

	private static boolean isLatestVersion = true;
	private static String latestVersion = "";

	/** Let the Version Checker run */
	@Override
	public void run() {
		InputStream in = null;
		String jsonUrl = "https://raw.githubusercontent.com/ThexXTURBOXx/UpdateJSONs/master/blockhelper.json";
		try {
			in = new URL(jsonUrl).openStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("Update check for " + mod_BlockHelper.NAME + " failed.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Update check for " + mod_BlockHelper.NAME + " failed.");
		}
		if(in != null) {
			try {
				for (String s : readLines(in)) {
					if (s.contains("modVersion")) {
						s = s.substring(0, s.indexOf("|"));
						s = s.replace("modVersion", "");
						s = s.replace("\"", "");
						s = s.replace(",", "");
						s = s.replace("	", "");
						s = s.replace(":", "");
						latestVersion = s;
					}
				}
				if (!latestVersion.equalsIgnoreCase(mod_BlockHelper.VERSION)) {
					System.out.println("Newer version of " + mod_BlockHelper.NAME + " available: " + latestVersion);
				} else {
					System.out.println("Yay! You have the newest version of " + mod_BlockHelper.NAME + " :)");
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Update check for " + mod_BlockHelper.NAME + " failed.");
			}
		}
		isLatestVersion = mod_BlockHelper.VERSION.equals(latestVersion);
	}

	/** @return whether BlockHelper is up-to-date or not */
	static boolean isLatestVersion() {
		return isLatestVersion;
	}

	/** @return the latest version available or the current installed version */
	static String getLatestVersion() {
		if (latestVersion.equals("")) {
			latestVersion = mod_BlockHelper.VERSION;
		}
		return latestVersion;
	}

	/** @return the latest version available or an empty string */
	static String getLatestVersionOrEmpty() {
		return latestVersion;
	}

	private static List<String> readLines(InputStream input) throws IOException {
		InputStreamReader reader = new InputStreamReader(input);
		BufferedReader readers = new BufferedReader(reader);
		List<String> list = new ArrayList<String>();
		String line = readers.readLine();
		while (line != null) {
			list.add(line);
			line = readers.readLine();
		}
		return list;
	}

}