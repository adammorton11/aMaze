package com.example.amazebyalexkrupski.UI;

import com.example.amazebyalexkrupski.Maze;
import com.example.amazebyalexkrupski.MazePanel;
import com.example.amazebyalexkrupski.globals;
import android.annotation.SuppressLint;
import android.content.Context;
import android.drm.DrmManagerClient.OnEventListener;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

@SuppressLint("WrongCall")
public class DrawingView extends View{

    public Context context;
    public Maze maze;
    public MazePanel mazepanel;
    public Canvas gc;
    public Canvas c;
    public Bitmap bmp;
    public DrawingView draw = this;
    public Handler handler = new Handler(){
		  @Override
		  public void handleMessage(Message message) {
			  
				  draw.update(); 
			
			  
		     }
		 };
    
    
    
    public DrawingView(Context context,AttributeSet attrs) {
        super(context,attrs);
        
        maze = globals.maze;
        mazepanel = maze.panel;
        gc = mazepanel.gc;
        bmp = mazepanel.bmp;
        mazepanel.draw = this;
    }
    public void onDraw(Canvas canvas) {
    	maze = globals.maze;
        mazepanel = maze.panel;
        gc = mazepanel.gc;
        bmp = mazepanel.bmp;
        mazepanel.draw = this;
    	c = canvas;
    	
    	Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
    	canvas.drawBitmap(bmp,0,0,paint);
    	
        
        //canvas.drawColor(Color.WHITE);
        //paint.setColor(Color.BLUE);
        //canvas.drawCircle(20, 20, 15, paint);
    }
    
    
    
    public void update(){
    	this.invalidate();
    	Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        if(c!=null){
        	
        	c.drawBitmap(bmp,0,0,paint);
        	this.draw(c);
        }
    	
    }


}