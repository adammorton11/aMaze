package com.example.amazebyalexkrupski.UI;

import com.example.amazebyalexkrupski.Maze;
import com.example.amazebyalexkrupski.globals;
import com.example.amazebyalexkrupski.R;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class FinishActivity extends Activity {
	/**
    * Displays the finish page and informs the user what happened and how to restart the game. 
    * Shows the overall energy consumption and length of path.
    * Visualizes if robot stopped for lack of energy, or if it is broken, or if it reached the exit (consider using audio to enhance the user experience).
    * Pressing the back button returns to State Title.
    */
private Maze maze;
private TextView textWin, textBat, textPath, textWinStats, textBatteryStats, textPathStats;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SharedPreferences prefs = this.getSharedPreferences(
			      "edu.wm.cs.cs301", Context.MODE_PRIVATE);
		
		Intent intent = getIntent();
		super.onCreate(savedInstanceState);
		maze = globals.maze;
		int battery = (Integer) getIntent().getSerializableExtra("Battery");
		String DIDYAWIN = (String) getIntent().getSerializableExtra("Win");
		setContentView(R.layout.activity_finish);
		textWin = (TextView)findViewById(R.id.textView1);
		textBat = (TextView)findViewById(R.id.textView3);
		textPath = (TextView)findViewById(R.id.textView2);
		
		
		
		if(DIDYAWIN.equals("lose")){
			Log.i("LOSE", "SUCKS TO SUCK");
			MediaPlayer mp = MediaPlayer.create(this, R.raw.dying);
			mp.start();
			textWin.setText("YOU LOST. ENJOY SPENDING ALL ETERNITY HERE. =(");
			textBat.setText("Battery level=0.");

			
		}
		else if(DIDYAWIN.equals("win")){
			Log.i("WINNER WINNER", "CHICKEN DINNER");
			MediaPlayer mp = MediaPlayer.create(this, R.raw.winning);
			mp.start();
			
			textWin.setText("YOU WON! CONGRATZ YOU AREN'T FUCKED. =D");
			textBat.setText("Battery level="+battery);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.finish, menu);
		return true;
	}
	
public void restart(View view){
		//maze.restart();
		Intent myIntent = new Intent(FinishActivity.this, AMazeActivity.class);
		FinishActivity.this.startActivity(myIntent);
	}

}
