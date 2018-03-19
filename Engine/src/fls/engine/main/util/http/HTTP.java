package fls.engine.main.util.http;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.UnknownHostException;

public class HTTP {

	private static boolean active = true;

	public static void setActive(boolean v) {
		HTTP.active = v;
	}

	public static String get(String path) {
		if (!HTTP.active)
			return "";
		String url = path;

		URL obj = null;
		try {
			obj = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) obj.openConnection();
			con.setReadTimeout(10000);
			con.setConnectTimeout(10000);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		// optional default is GET
		try {
			con.setRequestMethod("GET");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}

		int responseCode = 0;
		try {
			responseCode = con.getResponseCode();
		} catch (Exception e) {
			// e.printStackTrace();
			return "";
		}

		if (responseCode == 404)
			return "404 No page!";
		if (responseCode == 408)
			return "408 No time!";
		// System.out.println("\nSending 'GET' request to URL : " + url);
		// System.out.println("Response Code : " + responseCode);

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String inputLine;
		StringBuffer response = new StringBuffer();

		try {
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response.toString();
	}

	public static String post(String path) {
		if (!HTTP.active)
			return "";
		String url = path;
		URL ur = null;

		try {
			ur = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		HttpURLConnection con = null;

		try {
			con = (HttpURLConnection) ur.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			con.setRequestMethod("POST");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}

		BufferedReader br = null;

		try {
			br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		String res = "";
		String cLine = "";

		try {
			while ((cLine = br.readLine()) != null) {
				res += cLine + "\n";
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res;
	}

	public static void openInBrowser(String address) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(address));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isPageReachable(String address) {
		try {
			URL url = new URL(address);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			return (connection.getResponseCode() == 200);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
