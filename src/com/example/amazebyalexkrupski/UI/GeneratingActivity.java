package com.example.amazebyalexkrupski.UI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable; 

import com.example.amazebyalexkrupski.*;

import android.os.Build; 
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class GeneratingActivity extends Activity implements Runnable {
	
	private Maze maze;
	
	/** 
    * Intermediate page that shows progress of the maze generation algorithm and informs a user. 
    * Once the generation algorithm is finished the display switches to State Play.
    * Pressing the back button will stop the maze generation and return to State Title.
	*/
	Thread t;

	
	GeneratingActivity gen = this;
	
     private ProgressBar mProgress;
     private int mProgressStatus = 0;

     public Handler handler = new Handler(){
		  @Override
		  public void handleMessage(Message message) {
			  gen.startPlay();
			  
		     }
		 };

     @Override
     protected void onCreate(Bundle boom) {
        super.onCreate(boom);
        //maze = (Maze) getIntent().getSerializableExtra("MazeObject");9
        maze = globals.maze;
        maze.gen = this;
        //System.out.println("In Generating, maze: "+maze);
        int buildMethod = (Integer) getIntent().getSerializableExtra("buildMethod");
        int driveMethod = (Integer) getIntent().getSerializableExtra("driveMethod");
        int skill = (Integer) getIntent().getSerializableExtra("skill");
        boolean writeToFile = (Boolean) getIntent().getSerializableExtra("write");
        setContentView(R.layout.activity_generating);

        mProgress = (ProgressBar) findViewById(R.id.progressBar1);
        mProgress.setProgress(0);
        
        String fn = "maze" + Integer.toString(skill) + ".xml";
		
		
		if(buildMethod == 0){
			Log.i("BLDTYP", "Change Builder to Original");
			maze.changeDriveType(driveMethod);
			maze.methodChange(0);
			try {
				maze.skillBuild(skill);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(writeToFile){
				try {
					maze.mazebuilder.buildThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MazeFileWriter fw = new MazeFileWriter();
				fw.store(getApplicationContext(), fn, maze.mazew, maze.mazeh, Constants.SKILL_ROOMS[skill], Constants.SKILL_PARTCT[skill], maze.rootnode, maze.mazecells, maze.mazedists.getDists(), maze.startx, maze.starty);
			}
		}
		else if(buildMethod == 1){
			Log.i("BLDTYP", "Change Builder to Prim");
			maze.changeDriveType(driveMethod);
			maze.methodChange(1);
			try {
				maze.skillBuild(skill);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(buildMethod == 2){
			Log.i("BLDTYP", "Change Builder to AB");
			maze.changeDriveType(driveMethod);
			maze.methodChange(2);
			try {
				maze.skillBuild(skill);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		else if(buildMethod == 3){
			Log.i("BLDTYP", "Change builder to file");
			
			MazeFileReader fr = new MazeFileReader(fn, getApplicationContext());
			
			if (!fr.isNull()){
			
			maze.panel = new MazePanel();
			maze.changeH(fr.getHeight());
			maze.changeW(fr.getWidth());
			Distance d = new Distance(fr.getDistances());
			maze.changeDriveType(driveMethod);
			
			try {
				maze.init();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				maze.newMaze(fr.getRootNode(), fr.getCells(), d, fr.getStartX(), fr.getStartY());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			}
			else{
				Log.i("BLDTYP", "DEFAULT Original");
				maze.changeDriveType(driveMethod);
				maze.methodChange(0);
				try {
					maze.skillBuild(skill);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			}
		else{
			Log.i("BLDTYP", "DEFAULT Original");
			maze.changeDriveType(driveMethod);
			maze.methodChange(0);
			try {
				maze.skillBuild(skill);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }

     public void startPlay(){
    	 Intent myIntent = new Intent(GeneratingActivity.this, PlayActivity.class);
     	 GeneratingActivity.this.startActivity(myIntent);
     }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.generating, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			maze.Escape();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}


