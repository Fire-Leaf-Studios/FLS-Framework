package fls.engine.main.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import fls.engine.main.Init;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback{
	
	
	Init game;
	public Bitmap b;
	
	public GameSurface(Context context, AttributeSet s,Init i) {
		super(context,s);
		getHolder().addCallback(this);
		this.game = i;
		this.game.setupAndroidHandler(new AndroidHandler(getContext(), this, getHolder()));
		this.setFocusable(true);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.game.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.game.stop();
	}
	
	@Override
	public void onDraw(Canvas c){
		if(b!= null){
			System.out.println("Android drawing!");
			c.drawBitmap(b,10,10,null);
		}
	}

}
