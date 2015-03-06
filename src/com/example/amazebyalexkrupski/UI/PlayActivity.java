package com.example.amazebyalexkrupski.UI;

import com.example.amazebyalexkrupski.R;
import com.example.amazebyalexkrupski.R.*;
import com.example.amazebyalexkrupski.Maze;
import com.example.amazebyalexkrupski.MazePanel;
import com.example.amazebyalexkrupski.globals;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends Activity {
	/** 
    * Displays the maze and lets the user either watch a robot exploring the maze or allows the user to manually navigate the robot through the maze. 
    *    As an intermediate step only for this project introduce a Button "Shortcut" to directly move the UI to State Finish (possibly enhanced with a selection mechanism for: success, failure due to an accident or due to lack of energy).
    * Displays the remaining energy, use a ProgressBar for this.
    * Provides a feature to toggle visibility of the map plus functionality to toggle visibility of the solution on the map. Consider a split action bar and/or floating action bar (overlay) for this. Similar to state title above:
    *    show the whole maze from top or not (toggle).
    *    show the solution in the maze or not (toggle).
    *    show the currently visible walls or not (toggle)
    * Pressing the back button returns to State Title to allow the user to choose different parameter settings and restart. 
    * If in manual exploration mode: screen provides navigation buttons (up, down, left, right), consider a split action bar and/or floating action bar (overlay). 
    * If in robot exploration mode: screen provides a start/pause button to start the exploration and a pause the animation, consider a split action bar and/or floating action bar (overlay).
    * If the robot stops (no energy, accident, at exit) the screen switches to the finish screen.
	*/
	private Maze maze;
	private Vibrator myVib;
	private ProgressBar progress;
	MediaPlayer bkmp;
	
	
	public PlayActivity play = this;
	public Handler handler = new Handler(){
		  @Override
		  public void handleMessage(Message message) {
			  play.startPlay();
			  
		     }
		 };
	
    Thread thread = new Thread(new Runnable(){
  	public void run(){

  		Boolean success = true;
  		try {
  			success = maze.DriverDrive();
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  		
  		if (success){
  			int Bat = (int) maze.RobotBattery();
  			String win = "win";
  			Intent myIntent = new Intent(PlayActivity.this, FinishActivity.class);
  			myIntent.putExtra("Battery", Bat);
  			myIntent.putExtra("Win", win);
  			myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  			
  			PlayActivity.this.startActivity(myIntent);				
  		}
  		
  		else{
  			//Take us to the finish activity
  			//Tell the finish activity that you LOST (Ran out of Battery)
  			int Bat = 0;
  			String win = "lose";
  			Intent myIntent = new Intent(PlayActivity.this, FinishActivity.class);
  			myIntent.putExtra("Battery", Bat);
  			myIntent.putExtra("Win", win);
  			myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  			
  			PlayActivity.this.startActivity(myIntent);
  		}
  		
  		
  	finish();
  	}
  });
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_play);
		
		RelativeLayout layout = new RelativeLayout(this);
	    DrawingView  myView = new DrawingView(this,null);
	    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	    params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
	    layout.addView(myView,params);
		
	    myVib = (Vibrator)this.getSystemService(VIBRATOR_SERVICE);
		maze = globals.maze;
		maze.play = this;
		
		bkmp = MediaPlayer.create(this, R.raw.saria);
		bkmp.start();
		
		
		progress = (ProgressBar)findViewById(R.id.progressBar1);
		progress.setMax(2500);
		progress.setProgress(progress.getMax());
		
		maze.giveProgessBar(progress);
		
		
			startPlay();
		
		
	}


	public void startPlay(){
		thread.start();
	}
	
	
	public void win(int battery, String win, int Path){
		bkmp.stop();
		Intent myIntent = new Intent(PlayActivity.this, FinishActivity.class);
		myIntent.putExtra("Battery", battery);
		myIntent.putExtra("Win", win);
		PlayActivity.this.startActivity(myIntent);
	}

	public void shortcut(View view){
		Intent myIntent = new Intent(PlayActivity.this, FinishActivity.class);
		bkmp.stop();
		PlayActivity.this.startActivity(myIntent);
	}
	
	public void restart(View view){
		
		maze.restartPlay();
		Intent myIntent = new Intent(PlayActivity.this, AMazeActivity.class);
		bkmp.stop();
		PlayActivity.this.startActivity(myIntent);
	}
	
	//button methods:
	public void onMapOff(View v){
		RadioButton button = (RadioButton) v;
		Log.i("MAP", "NO MAP 4 U");
		maze.mapOff();
	}
	
	public void onWhole(View v){
		RadioButton button = (RadioButton) v;
		Log.i("MAP", "HAVE A MAP");
		maze.mazeShow();
	}
	
	public void onSeen(View v){
		RadioButton button = (RadioButton) v;
		Log.i("MAP", "IVE BEEN HERE B4");
		maze.mapOn();
	}
	
	public void onSolution(View v){
		RadioButton button = (RadioButton) v;
		Log.i("SOL", "GTFO");
		maze.solShow();
	}
	
	public void onSolutionOff(View v){
		RadioButton button = (RadioButton) v;
		Log.i("SOL", "NEVER LEAVING");
		maze.solShow();
	}
	
	public void onForward(View v) throws Exception{
		Log.i("MOVE", "FORWARD");
		Button button = (Button) v;
		if (maze.getDriveType() == 0){
			maze.up();
		}
		
		int newprogress = progress.getProgress() - 5;
		if (newprogress < 0){
			newprogress = 0;
		}
		progress.setProgress(newprogress);
		
	}
	

	public void onRight(View v) throws Exception {
		Log.i("ROTATE", "RIGHT");
		Button button = (Button) v;
		if (maze.getDriveType() == 0){
			maze.right();
			
		int newprogress = progress.getProgress() - 3;
		if (newprogress < 0){
			newprogress = 0;
		}
		progress.setProgress(newprogress);
		}
	}
	
	public void onLeft(View v) throws Exception  {
		Log.i("ROTATE", "LEFT");
		Button button = (Button) v;
		if (maze.getDriveType() == 0){
			maze.left();
			
		int newprogress = progress.getProgress() - 3;
		if (newprogress < 0){
			newprogress = 0;
		}
		progress.setProgress(newprogress);
		}
	}
	 public void onZoomIn (View v){
		Log.i("ZOOOOOM", "Zoom In");
		maze.zoomIn();
	 }
	 public void onZoomOut (View v) {
		 Log.i("ZOOOOOM", "Zoom Out");
		 maze.zoomOut();
	 }

}
