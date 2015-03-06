package com.example.amazebyalexkrupski.UI;

import java.io.Serializable; 
import java.util.ArrayList;
import java.util.List;

import com.example.amazebyalexkrupski.Maze;
import com.example.amazebyalexkrupski.globals;
import com.example.amazebyalexkrupski.R;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AMazeActivity extends Activity implements OnSeekBarChangeListener{
	/** 
    * Displays the welcome page and takes parameter settings to start with the maze generation. Your design can follow the design you developed for Homework 4. Parameters include: 
    *    the complexity (size) of the maze. Use a SeekBar for this. Pick a meaningful, commonly used default value.
    *    the maze generation algorithm (Backtracking, Prim's, Kruskal's and Eller's algorithm). Use a Spinner  for this, pick a commonly used default value
    *    the selection of one of the following ways to operate the robot: a) Manual, b) Gambler, c) Curious Gambler, d) Wall Follower, e) Wizard. Pick manual as a default value.
    *    the selection of either generating a random maze or loading a maze from file.  Pick random generation as a default value.
    *    a start button to start with either the random generation of a maze or loading a maze from file (needs 1 previously stored maze for each size). The action bar may be a good location for a start button.
    * Possible transitions are to state Generating (via parameter settings and clicking on the start button). 
	*/
	public final static String EXTRA_MESSAGE = "com.example.amazebyalexkrupski.MESSAGE";
private SeekBar bar;
private TextView textProgress,textAction;
private int skill = 0;
private int buildMethod = 0;
private int driveMethod = 0;
private Maze maze;
private Spinner drive_spinner;
private Spinner build_spinner;
boolean writeToFile = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_amaze);
		bar=(SeekBar)findViewById(R.id.seekBar1);
		bar.setOnSeekBarChangeListener(this);
		textProgress = (TextView)findViewById(R.id.textView1);
		globals.makeMaze(this);
		maze = globals.maze;
	
		setdrivemethod();
		setbuildmethod();
		setdrivelistener();
		setbuildlistener();
		setwriterandlistener();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.amaze, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
//	        case R.id.action_search:
//	            return true;
	        case R.id.action_settings:
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void play(View view) throws Exception{

		
		Intent myIntent = new Intent(AMazeActivity.this, GeneratingActivity.class);
		myIntent.putExtra("buildMethod", buildMethod);
		myIntent.putExtra("driveMethod",  driveMethod);
		myIntent.putExtra("skill",  skill);
		myIntent.putExtra("write", writeToFile);

		AMazeActivity.this.startActivity(myIntent);
	}
	

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {	    	
	    //change progress text label with current seekbar value
	    textProgress.setText("SO MUCH DIFFICULT - "+progress);
	    skill = progress;
	    Log.i("DIFFICULTY", "Difficulty Level: "+skill);
	    }
	
	@Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    	
    }

	@Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    	seekBar.setSecondaryProgress(seekBar.getProgress());
    	//skill = seekBar.getProgress();
    	// set the shade of the previous value.  	
    }

	public void setdrivemethod(){
		
		 drive_spinner = (Spinner) findViewById(R.id.spinner1);
		 List<String> list = new ArrayList<String>();
		 list.add("Manual");
		 list.add("Random");
		 list.add("Wall Follower");
		 list.add("Wizard");
		 list.add("Tremaux");
		 
		 
		 ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
         (this, android.R.layout.simple_spinner_item,list);
          
		 dataAdapter.setDropDownViewResource
         (android.R.layout.simple_spinner_dropdown_item);
          
		 drive_spinner.setAdapter(dataAdapter);
		
	}
	
	public void setbuildmethod(){
		 build_spinner = (Spinner) findViewById(R.id.spinner2);
		 List<String> list = new ArrayList<String>();
		 list.add("Original");
		 list.add("Prim");
		 list.add("Aldous Broder");
		 list.add("Load From File");
		
		 
		 ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
         (this, android.R.layout.simple_spinner_item,list);
          
		 dataAdapter.setDropDownViewResource
         (android.R.layout.simple_spinner_dropdown_item);
          
		 build_spinner.setAdapter(dataAdapter);
		
	}
	
	public void setdrivelistener(){
			drive_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				driveMethod = drive_spinner.getSelectedItemPosition();
				Log.i("DRIVEMTH", "Drivemthod: "+driveMethod);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
			
		});
	}
	
	public void setbuildlistener(){
			build_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				buildMethod = build_spinner.getSelectedItemPosition();
				Log.i("BUILDMTH", "BuildMethod: "+buildMethod);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
			
		});
	}
	
	public void setwriterandlistener(){
		final CheckBox writer = (CheckBox)findViewById(R.id.checkBox1);
		writer.setOnClickListener(new OnClickListener() {
		
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (writer.isChecked()){
					writeToFile = true;
				}
				else{
					writeToFile = false;
				}
				
			}
			
		});
	}
}

