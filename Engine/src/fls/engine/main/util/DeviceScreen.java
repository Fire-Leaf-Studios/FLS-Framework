package fls.engine.main.util;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import fls.engine.main.Init;
import sun.awt.X11GraphicsDevice;

public class DeviceScreen {

	
	public static GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
	
	public static void listDisplayModes(){
		for(int i = 0; i < screen.getDisplayModes().length; i++){
			DisplayMode dm = screen.getDisplayModes()[i];
			log(dm.getWidth() + " : " + dm.getHeight() + " : " + dm.getBitDepth() + " : " + dm.getRefreshRate());
		}
	}
	
	public static boolean setFullScreen(Init init){		
		log("Going to find a fullscreen model");
		DisplayMode disp = null;
		int lw = 9999;
		int lh = 9999;
		int tw = init.width;
		int th = init.height;
		for(int i = 0; i < screen.getDisplayModes().length; i++){
			DisplayMode dm = screen.getDisplayModes()[i];
			int dw = dm.getWidth() - tw;
			int dh = dm.getHeight() - th;
			
			if(dw == 0 && dh == 0){
				disp = dm;
				break;
			}
			
			if(dw < lw && lh < dh){// Closest macth yet
				lw = dm.getWidth();
				lh = dm.getHeight();
				disp = dm;
			}
		}
		try{
			screen.setFullScreenWindow(init.frame);
			screen.setDisplayMode(disp);
			log("Successfully set window to fullscreen mode");
			return true;
		}catch(Exception e){
			e.printStackTrace();
			err("Unable to set window to full screen, continuing in windowed set up");
		}
		return false;
	}
	
	private static void log(String msg){
		System.out.println("[Display Manager] " + msg);
	}
	
	private static void err(String msg){
		System.err.println("[Display Manager] " + msg);
	}
}
