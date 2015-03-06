package com.example.amazebyalexkrupski;

import com.example.amazebyalexkrupski.UI.PlayActivity;
import android.app.Activity;
import android.content.Context;

public class globals { 
	public static Maze maze;
	
	public static void makeMaze(Activity a){
		maze = new Maze(a.getBaseContext());
	}
}
