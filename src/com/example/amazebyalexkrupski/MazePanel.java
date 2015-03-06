package com.example.amazebyalexkrupski;

//import java.awt.*; 
import java.io.Serializable;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import com.example.amazebyalexkrupski.Constants;
import com.example.amazebyalexkrupski.UI.AMazeActivity;
import com.example.amazebyalexkrupski.UI.DrawingView;
import com.example.amazebyalexkrupski.UI.FinishActivity;
import com.example.amazebyalexkrupski.UI.GeneratingActivity;
import com.example.amazebyalexkrupski.UI.PlayActivity;


public class MazePanel /*implements Parcelable*/ {
	/* Panel operates a double buffer see
	 * http://www.codeproject.com/Articles/2136/Double-buffer-in-standard-Java-AWT
	 * for details
	 */
	public Bitmap bufferImage ;
	public Canvas gc;
	public Bitmap bmp;
	public DrawingView draw;
	
	
	
	int BLACK =  -16777216;
	int DarkGray =  -12303292;
	int WHITE = -1;
	int GRAY = -7829368;
	int RED =  -65536;
	int YELLOW =  -256;

	//Font largeBannerFont = new Font("TimesRoman", Font.BOLD, 48);
	//Font smallBannerFont = new Font("TimesRoman", Font.BOLD, 16);
	
	public Paint col;
	//View view;
	//public DrawingView drawview;

	
	
	
	/**
	 * This is the constructor for the mazePanel. 
	 */
	public MazePanel() {
		bmp = bmp.createBitmap(400, 400, Config.RGB_565);
		gc = new Canvas(bmp);
		this.col = new Paint(80000000);
		
		
	}
	
	/**
	 * Returns an array instead of a point, as the point was being misused.  
	 * @param x
	 * @param y
	 * @return an array.
	 */
	public int[] getPoint(int x, int y){
		int[] array = {x,y}; 
		return array;
	}
	
	/**
	 * This returns a color according to the RGB values. 
	 * @param intr
	 * @param intg
	 * @param intb
	 * @return a Color object.
	 */
	public Paint MColor(int intr, int intg, int intb){
		//return new Color(intr, intg, intb);
		Paint p = new Paint();
		p.setARGB(255, intr, intg, intb);
		return p;
	}
	
	/**
	 * This returns the color black.
	 * @return Color.black.
	 */
	public Paint getBlack(){
		Paint p = new Paint(BLACK);
		return p;
	}
	
	/**
	 * This returns the color dark gray.
	 * @return Color.darkGray.
	 */
	public Paint getDarkGray(){
		Paint p = new Paint(DarkGray);
		return p;	}
	
	/**
	 * This returns the color white.
	 * @return Color.white.
	 */
	public Paint getWhite(){
		Paint p = new Paint(WHITE);
		return p;
	}
	
	/**
	 * This returns the color gray.
	 * @return Color.gray.
	 */
	public Paint getGray(){
		Paint p = new Paint(GRAY);
		return p;
	}
	
	/**
	 * This returns the color red.
	 * @return Color.red.
	 */
	public Paint getRed(){
		Paint p = new Paint(RED);
		return p;
	}
	
	/**
	 * This returns the color yellow.
	 * @return Color.yellow.
	 */
	public Paint getYellow(){
		Paint p = new Paint(YELLOW);
		return p;
	}
	
	/**
	 * This updates the graphics to the Finish page. 
	 * @param gc graphics page to be updated.
	 */
	public void redrawFinish(Canvas c, Maze m){
		//we could be coming from anywhere. 
		//instead try to change to finish page without creating a play activity?
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!
		//PlayActivity play = new PlayActivity();
		//play.shortcut(view);
		
		//THIS NEEDS TO TAKE US TO THE FINISH ACTIVITY
		//Intent myIntent = new Intent(PlayActivity.this, FinishActivity.class);
		//myIntent.putExtra("MazeObject", m);
		//PlayActivity.this.startActivity(myIntent);
		
		System.out.println("redraw Finish");
		
//		gc.setColor(Color.blue);
//		gc.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
//		gc.setFont(largeBannerFont);
//		FontMetrics fm = gc.getFontMetrics();
//		gc.setColor(Color.yellow);
//		centerString(gc, fm, "You won!", 100);
//		gc.setColor(Color.orange);
//		gc.setFont(smallBannerFont);
//		fm = gc.getFontMetrics();
//		centerString(gc, fm, "Congratulations!", 160);
//		gc.setColor(Color.white);
//		centerString(gc, fm, "Hit any key to restart", 300);
	}
	
	/**
	 * This updates the graphics to the generating page.
	 * @param gc graphics page to be updated.
	 * @param maze the maze being generated.
	 * @throws Exception 
	 */
	void redrawGenerating(Canvas c, Maze m) throws Exception {
		//we could be coming from anywhere. 
		//instead try to change to generating page without creating a maze activity. 
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		//AMazeActivity activity = new AMazeActivity();
		//activity.play(view);
		
		//THIS NEEDS TO TAKE US TO THE GENERATING ACTIVITY
		//Intent myIntent = new Intent(AMazeActivity.this, GeneratingActivity.class);
		//myIntent.putExtra("MazeObject", m);
		//AMazeActivity.this.startActivity(myIntent);
		
		System.out.println("Redraw Generating");
		
//		gc.setColor(Color.yellow);
//		gc.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
//		gc.setFont(largeBannerFont);
//		FontMetrics fm = gc.getFontMetrics();
//		gc.setColor(Color.red);
//		centerString(gc, fm, "Building maze", 150);
//		gc.setFont(smallBannerFont);
//		fm = gc.getFontMetrics();
//		gc.setColor(Color.black);
//		centerString(gc, fm, maze.getPercentDone()+"% completed", 200);
//		centerString(gc, fm, "Hit escape to stop", 300);
	}
	
	/**
	 * This updates the graphics to the title page.
	 * @param gc the graphics page to be updated. 
	 */
	public void redrawTitle(Canvas c, Maze m){
		//we could be coming from anywhere. 
		//instead try to change to title page without creating a finish activity.
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		//FinishActivity finish = new FinishActivity();
		//finish.restart(view);
		
		
		//THIS NEEDS TO TAKE US TO THE TITLE ACTIVITY
		//Intent myIntent = new Intent(FinishActivity.this, AMazeActivity.class);
		//FinishActivity.this.startActivity(myIntent);
		
		System.out.println("Redraw Title");
		
//		gc.setColor(Color.white);
//		gc.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
//		gc.setFont(largeBannerFont);
//		FontMetrics fm = gc.getFontMetrics();
//		gc.setColor(Color.red);
//		centerString(gc, fm, "MAZE", 100);
//		gc.setColor(Color.blue);
//		gc.setFont(smallBannerFont);
//		fm = gc.getFontMetrics();
//		centerString(gc, fm, "by Paul Falstad", 160);
//		centerString(gc, fm, "www.falstad.com", 190);
//		gc.setColor(Color.black);
//		centerString(gc, fm, "To start, select a skill level.", 250);
//		centerString(gc, fm, "(Press a number from 0 to 9,", 300);
//		centerString(gc, fm, "or a letter from A to F)", 320);
//		centerString(gc, fm, "v1.2", 350);
	}
	
	/**
	 * This helps locate the center, and where to place a string. 
	 * @param g graphics page to be updated.
	 * @param fm the information about the font. 
	 * @param str the string to be centered. 
	 * @param ypos 
	 */
	//private void centerString(Graphics g, FontMetrics fm, String str, int ypos) {
	///	g.drawString(str, (Constants.VIEW_WIDTH-fm.stringWidth(str))/2, ypos);
	//}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This updated the graphics. 
	 */
	public void update(Canvas g) {
		//Log.i("!!!", "Update was called");
		paint(g) ;
		if (draw!= null){
			Message m = new Message();
			draw.handler.sendMessage(m);
		}
		
	}
	
	
	/**
	 * This paints on the graphics.
	 */
	public void paint(Canvas g) {
		if (bufferImage == null){
			bufferImage = Bitmap.createBitmap(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, Bitmap.Config.valueOf("ARGB_8888") );
		}
		
		g.drawBitmap(bufferImage, 0, 0, null);
		
		
	}

	/**
	 * This creates a buffer image. 
	 */
	public void initBufferImage() {
		
		bufferImage = Bitmap.createBitmap(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, Bitmap.Config.valueOf("ARGB_8888") );
		if (null == bufferImage)
		{
			System.out.println("Error: creation of buffered image failed, presumedly container not displayable");
		}
	}
	/**
	 * This finds the graphics that are being used. 
	 * @return the graphics. 
	 */
	public Canvas getBufferGraphics() {
		//Log.i("!!!", "Get buffer Graphics called");
		if (null == bufferImage)
			initBufferImage() ;
		return getGraphics(bufferImage) ;
	}
	/**
	 * This updates the graphics. 
	 */
	public void update() {
	
		paint(getGraphics()) ;
		if (draw!= null){
			Message m = new Message();
			draw.handler.sendMessage(m);
		}
	
	}
	
	public Canvas getGraphics(){
		Canvas c = new Canvas();
		return c;
	}
	
	public Canvas getGraphics(Bitmap bufferImage){
		return gc;
	}
}
