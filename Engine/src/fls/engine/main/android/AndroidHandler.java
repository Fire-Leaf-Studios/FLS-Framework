package fls.engine.main.android;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class AndroidHandler {

	
	private Context context;
	private GameSurface surface;
	private SurfaceHolder sh;
	private Canvas c;
	
	
	public AndroidHandler(Context c, GameSurface s, SurfaceHolder sh){
		this.context = c;
		this.surface = s;
		this.sh = sh;
		this.c = new Canvas();
	}
	
	public Context getContext(){
		return this.context;
	}
	
	public GameSurface getSurface(){
		return this.surface;
	}
	
	public SurfaceHolder getSurfaceHolder(){
		return this.sh;
	}
	
	public Canvas getCanavs(){
		return this.c;
	}
	
	public void setCanvas(Canvas nc){
		this.c = nc;
	}
}
