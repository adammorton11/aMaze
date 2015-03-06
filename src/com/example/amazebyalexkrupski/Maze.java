package com.example.amazebyalexkrupski;
 

import java.io.Serializable; 
import java.util.ArrayList; 
import java.util.Iterator;

import com.example.amazebyalexkrupski.BSPNode;
import com.example.amazebyalexkrupski.BasicRobot;
import com.example.amazebyalexkrupski.Cells;
import com.example.amazebyalexkrupski.Constants;
import com.example.amazebyalexkrupski.Distance;
import com.example.amazebyalexkrupski.FirstPersonDrawer;
import com.example.amazebyalexkrupski.Gambler;
import com.example.amazebyalexkrupski.ManualDriver;
import com.example.amazebyalexkrupski.MapDrawer;
import com.example.amazebyalexkrupski.MazeBuilder;
import com.example.amazebyalexkrupski.MazeBuilderAldousBroder;
import com.example.amazebyalexkrupski.MazeBuilderPrim;
import com.example.amazebyalexkrupski.MazePanel;
import com.example.amazebyalexkrupski.MazeView;
import com.example.amazebyalexkrupski.RangeSet;
import com.example.amazebyalexkrupski.Robot;
import com.example.amazebyalexkrupski.Robot.Turn;
import com.example.amazebyalexkrupski.RobotDriver;
import com.example.amazebyalexkrupski.Viewer;
import com.example.amazebyalexkrupski.WallFollower;
import com.example.amazebyalexkrupski.Wizard;
import com.example.amazebyalexkrupski.UI.FinishActivity;
import com.example.amazebyalexkrupski.UI.GeneratingActivity;
import com.example.amazebyalexkrupski.UI.PlayActivity;
//import javax.swing.JPanel;
//We left this statement here because the only things swing is used for is the button panel, which is going to be removed in the next project anyway. 











import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ProgressBar;


/**
 * Class handles the user interaction for the maze. 
 * It implements a state-dependent behavior that controls the display and reacts to key board input from a user. 
 * After refactoring the original code from an applet into a panel, it is wrapped by a MazeApplication to be a java application 
 * and a MazeApp to be an applet for a web browser. At this point user keyboard input is first dealt with a key listener
 * and then handed over to a Maze object by way of the keyDown method.
 *
 * This code is refactored code from Maze.java by Paul Falstad, www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 * 
 * 
 */
// MEMO: original code: public class Maze extends Applet {

//public class Maze extends Panel {
public class Maze /*implements Parcelable/*Serializable*/ {
	
	public PlayActivity play;
	public GeneratingActivity gen;
	// Model View Controller pattern, the model needs to know the viewers
	// however, all viewers share the same graphics to draw on, such that the share graphics
	// are administered by the Maze object
	final private ArrayList<Viewer> views = new ArrayList<Viewer>() ; 
	public MazePanel panel ; // graphics to draw on, shared by all views
	
		
	public int startx;
	public int starty;

	int state;			// keeps track of the current GUI state, one of STATE_TITLE,...,STATE_FINISH, mainly used in redraw()
	// possible values are defined in Constants
	// user can navigate 
	// title -> generating -(escape) -> title
	// title -> generation -> play -(escape)-> title
	// title -> generation -> play -> finish -> title
	// STATE_PLAY is the main state where the user can navigate through the maze in a first person view

	private int percentdone = 0; // describes progress during generation phase
	boolean showMaze;		 	// toggle switch to show overall maze on screen
	boolean showSolution;		// toggle switch to show solution in overall maze on screen
	boolean solving;			// toggle switch 
	boolean mapMode; // true: display map of maze, false: do not display map of maze
	// map_mode is toggled by user keyboard input, causes a call to draw_map during play mode

	//static final int viewz = 50;    
	int viewx, viewy, angle;
	int dx, dy;  // current direction
	int px, py ; // current position on maze grid (x,y)
	int walkStep;
	int viewdx, viewdy; // current view direction


	boolean success; 
	
	// debug stuff
	boolean deepdebug = false;
	boolean allVisible = false;
	boolean newGame = false;

	// properties of the current maze
	public int mazew; // width of maze
	public int mazeh; // height of maze
	public Cells mazecells ; // maze as a matrix of cells which keep track of the location of walls
	public Distance mazedists ; // a matrix with distance values for each cell towards the exit
	public Cells seencells ; // a matrix with cells to memorize which cells are visible from the current point of view
	// the FirstPersonDrawer obtains this information and the MapDrawer uses it for highlighting currently visible walls on the map
	public BSPNode rootnode ; // a binary tree type search data structure to quickly locate a subset of segments
	// a segment is a continuous sequence of walls in vertical or horizontal direction
	// a subset of segments need to be quickly identified for drawing
	// the BSP tree partitions the set of all segments and provides a binary search tree for the partitions
	

	// Mazebuilder is used to calculate a new maze together with a solution
	// The maze is computed in a separate thread. It is started in the local Build method.
	// The calculation communicates back by calling the local newMaze() method.
	public MazeBuilder mazebuilder;
	
	ProgressBar progress ;
	
	private Robot r;
	private RobotDriver driver;
	

	

	
	// fixing a value matching the escape key
	final int ESCAPE = 27; 

	// generation method used to compute a maze
	private int method = 0 ; // 0 : default method, Falstad's original code
	// method == 1: Prim's algorithm
	
	private int driveType = 0; 

	int zscale = Constants.VIEW_HEIGHT/2;
	
	private int skill;
	

	private RangeSet rset;
	
	Context context;
	
	/**
	 * Constructor
	 */
	public Maze(Context context) {
		super() ;
		panel = new MazePanel() ;
		this.context = context;
		progress = new ProgressBar(context);
	}
	/**
	 * Constructor that also selects a particular generation method
	 */
	public Maze(int method)
	{
		super() ;
		// 0 is default, do not accept other settings but 0 and 1
		if (1 == method)
			this.method = 1 ;
		if (2 == method)
			this.method = 2 ;
		panel = new MazePanel() ;
	}
	
	public void giveProgessBar(ProgressBar p){
		progress = p;
	}
	
	
	//update driver and robot. 
	public float RobotBattery(){
		return r.getBatteryLevel();
	}

	public Boolean DriverDrive(){
		Boolean sucess = false;
		try {
			sucess = driver.drive2Exit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sucess;
	}

	
	public void methodChange(int i){
		
		method = i;
	}
	
	public void changeDriveType(int driveMethod){
		
		driveType = driveMethod;
	}
	
	public int getDriveType(){
		return driveType;
	}
	
	public int getSkill(){
		return skill;
	}
	
	
	/**
	 * Method to initialize internal attributes. Called separately from the constructor. 
	 * @throws Exception 
	 */
	public void init() throws Exception {
		state = Constants.STATE_TITLE;
	
		rset = new RangeSet();
		panel.initBufferImage() ;
		addView(new MazeView(this)) ;
		notifyViewerRedraw() ;
			
	}
	
	
	
	
	
	/**
	 * Method obtains a new Mazebuilder and has it compute new maze, 
	 * it is only used in keyDown()
	 * @param skill level determines the width, height and number of rooms for the new maze
	 * @throws Exception 
	 */
	void build(int skill) throws Exception {
		this.skill = skill;
	
		// switch screen
		state = Constants.STATE_GENERATING;

		percentdone = 0;
		//panel.revalidate();
		//panel.repaint();
		notifyViewerRedraw() ;
		// select generation method
		switch(method){
		case 2 : mazebuilder = new MazeBuilderAldousBroder();
		break;
		case 1 : mazebuilder = new MazeBuilderPrim(); // generate with Prim's algorithm
		break ;
		case 0: // generate with Falstad's original algorithm (0 and default), note the missing break statement
		default : mazebuilder = new MazeBuilder(); 
		break ;
		}
		// adjust settings and launch generation in a separate thread
		mazew = Constants.SKILL_X[skill];
		mazeh = Constants.SKILL_Y[skill];
		
		mazebuilder.build(this, mazew, mazeh, Constants.SKILL_ROOMS[skill], Constants.SKILL_PARTCT[skill]);
		// mazebuilder performs in a separate thread and calls back by calling newMaze() to return newly generated maze
		
		
	}
	
	/**
	 * Call back method for MazeBuilder to communicate newly generated maze as reaction to a call to build()
	 * @param root node for traversals, used for the first person perspective
	 * @param cells encodes the maze with its walls and border
	 * @param dists encodes the solution by providing distances to the exit for each position in the maze
	 * @param startx current position, x coordinate
	 * @param starty current position, y coordinate
	 * @throws Exception 
	 */
	public void newMaze(BSPNode root, Cells c, Distance dists, int startx, int starty) throws Exception {
		if (Cells.deepdebugWall)
		{   // for debugging: dump the sequence of all deleted walls to a log file
			// This reveals how the maze was generated
			c.saveLogFile(Cells.deepedebugWallFileName);
		}
		
		// adjust internal state of maze model
		showMaze = showSolution = solving = false;
		mazecells = c ;
		mazedists = dists;
		seencells = new Cells(mazew+1,mazeh+1) ;
		rootnode = root ;
		setCurrentDirection(1, 0) ;
		setCurrentPosition(startx,starty) ;
		this.startx = startx;
		this.starty = starty;
		walkStep = 0;
		viewdx = dx<<16; 
		viewdy = dy<<16;
		angle = 0;
		mapMode = false;
		

		// set the current state for the state-dependent behavior
		state = Constants.STATE_PLAY;
		
		if(gen!=null){
			Message m = new Message();
			gen.handler.sendMessage(m);
		}

		cleanViews() ;
		notifyViewerRedraw();
		// register views for the new maze
		// mazew and mazeh have been set in build() method before mazebuider was called to generate a new maze.
		// reset map_scale in mapdrawer to a value of 10
		addView(new FirstPersonDrawer(context,Constants.VIEW_WIDTH,Constants.VIEW_HEIGHT,
				Constants.MAP_UNIT,Constants.STEP_SIZE, mazecells, seencells, 10, mazedists.getDists(), mazew, mazeh, root, this)) ;
		// order of registration matters, code executed in order of appearance!
		addView(new MapDrawer(Constants.VIEW_WIDTH,Constants.VIEW_HEIGHT,Constants.MAP_UNIT,Constants.STEP_SIZE, mazecells, seencells, 10, mazedists.getDists(), mazew, mazeh, this)) ;
		// notify viewers
		notifyViewerRedraw() ;
		
		
		
		
		if (driveType == 0){
			Log.i("DRVTYP", "Driver - Manual");
			r = new BasicRobot();
			r.setMaze(this);
		
			driver = new ManualDriver();
			driver.setRobot(r);
			
			


		}
		else if (driveType == 1){
			Log.i("DRVTYP", "Driver - Random");
			r = new BasicRobot();
			r.setMaze(this);
			
			driver = new Gambler();
			driver.setRobot(r);
			


		}

		else if (driveType == 2){
			Log.i("DRVTYP", "Driver - WallFollower");
			r = new BasicRobot();
			r.setMaze(this);
		
			driver = new WallFollower();
			driver.setRobot(r);
			


		}
		else if (driveType == 3){
			Log.i("DRVTYP", "Driver - Wizard");
			r = new BasicRobot();
			
			r.setMaze(this);
			
			driver = new Wizard();
			driver.setRobot(r);
			driver.setDistance(mazedists);
			


		}
		
		else if (driveType == 4){
			Log.i("DRVTYP", "Driver - Tremaux");
			r = new BasicRobot();
			
			r.setMaze(this);
			
			driver = new Tremaux();
			driver.setRobot(r);
			driver.setDistance(mazedists);
			
//			if ((play!= null) && (skill>1)){
//				Log.i("!!!!!!!!!!", "Calling Handler");
//				Message m = new Message();
//				play.handler.sendMessage(m);
//			}

		}
		
		

		
	}

	
	/////////////////////////////// Methods for the Model-View-Controller Pattern /////////////////////////////
	/**
	 * Register a view
	 */
	public void addView(Viewer view) {
		views.add(view) ;
	}
	/**
	 * Unregister a view
	 */
	public void removeView(Viewer view) {
		views.remove(view) ;
	}
	/**
	 * Remove obsolete FirstPersonDrawer and MapDrawer
	 */
	protected void cleanViews() {
		// go through views and notify each one
		Iterator<Viewer> it = views.iterator() ;
		while (it.hasNext())
		{
			Viewer v = it.next() ;
			if ((v instanceof FirstPersonDrawer)||(v instanceof MapDrawer))
			{
				//System.out.println("Removing " + v);
				it.remove() ;
			}
		}

	}
	/**
	 * Notify all registered viewers to redraw their graphics
	 * @return 
	 */
	protected void notifyViewerRedraw() {
		// go through views and notify each one
		Iterator<Viewer> it = views.iterator() ;
		while (it.hasNext())
		{
			Viewer v = it.next() ;
			// viewers draw on the buffer graphics
			v.redraw(panel, state, px, py, viewdx, viewdy, walkStep, Constants.VIEW_OFFSET, rset, angle) ;
		}
		// update the screen with the buffer graphics
		panel.update() ;
	}
	/** 
	 * Notify all registered viewers to increment the map scale
	 */
	void notifyViewerIncrementMapScale() {
		// go through views and notify each one
		Iterator<Viewer> it = views.iterator() ;
		while (it.hasNext())
		{
			Viewer v = it.next() ;
			v.incrementMapScale() ;
		}
		// update the screen with the buffer graphics
		panel.update() ;
	}
	/** 
	 * Notify all registered viewers to decrement the map scale
	 */
	void notifyViewerDecrementMapScale() {
		// go through views and notify each one
		Iterator<Viewer> it = views.iterator() ;  
		while (it.hasNext())
		{ 
			Viewer v = it.next() ;   
			v.decrementMapScale() ;  
		}  
		// update the screen with the buffer graphics  
		panel.update() ; 
	}   
	////////////////////////////// get methods ///////// //////////////// ////////////////////////////////////// 
	boolean isInMapMode() { 
		return mapMode ; 
	} 
	boolean isInShowMazeMode() { 
		return showMaze ; 
	} 
	boolean isInShowSolutionMode() { 
		return showSolution ; 
	} 
	public String getPercentDone(){
		return String.valueOf(percentdone) ;
	}
	
	public int getPercent(){
		return percentdone;
	}
	
	public MazePanel getPanel() {
		return panel ;
	}
	////////////////////////////// set methods ///////////////////////////////////////////////////////////////
	////////////////////////////// Actions that can be performed on the maze model ///////////////////////////
	void setCurrentPosition(int x, int y)
	{
		px = x ;
		py = y ;
	}
	protected void setCurrentDirection(int x, int y)
	{
		dx = x ;
		dy = y ;
		//r.dx = x;
		//r.dy = y;
	}
	
	
	void buildInterrupted() {
		state = Constants.STATE_TITLE;

		
		notifyViewerRedraw() ;
		mazebuilder = null;
	}

	final double radify(int x) {
		return x*Math.PI/180;
	}


	/**
	 * Allows external increase to percentage in generating mode with subsequence graphics update
	 * @param pc gives the new percentage on a range [0,100]
	 * @return true if percentage was updated, false otherwise
	 */
	public boolean increasePercentage(int pc) {
		if (percentdone < pc && pc < 100) {
			percentdone = pc;
			if (state == Constants.STATE_GENERATING)
			{
				notifyViewerRedraw() ;
			}
			else
				dbg("Warning: Receiving update request for increasePercentage while not in generating state, skip redraw.") ;
			return true ;
		}
		return false ;
	}


	/////////////////////// Methods for debugging ////////////////////////////////
	private void dbg(String str) {
		//System.out.println(str);
	}

	protected void logPosition() {
		if (!deepdebug)
			return;
		dbg("x="+viewx/Constants.MAP_UNIT+" ("+
				viewx+") y="+viewy/Constants.MAP_UNIT+" ("+viewy+") ang="+
				angle+" dx="+dx+" dy="+dy+" "+viewdx+" "+viewdy);
	}
	///////////////////////////////////////////////////////////////////////////////

	/**
	 * Helper method for walk()
	 * @param dir
	 * @return true if there is no wall in this direction
	 */
	boolean checkMove(int dir) {
		// obtain appropriate index for direction (CW_BOT, CW_TOP ...) 
		// for given direction parameter
		int a = angle/90;
		if (dir == -1)
			a = (a+2) & 3;
		// check if cell has walls in this direction
		// returns true if there are no walls in this direction
		return mazecells.hasMaskedBitsFalse(px, py, Constants.MASKS[a]) ;
	} 



	private void rotateStep() {
		angle = (angle+1800) % 360;
		viewdx = (int) (Math.cos(radify(angle))*(1<<16));
		viewdy = (int) (Math.sin(radify(angle))*(1<<16));
		moveStep();
	}

	void moveStep() {
		notifyViewerRedraw() ;
		try {
			Thread.currentThread().sleep(25);
		} catch (Exception e) { }
	}

	private void rotateFinish() {
		setCurrentDirection((int) Math.cos(radify(angle)), (int) Math.sin(radify(angle))) ;
		logPosition();
	}

	void walkFinish(int dir) {
		setCurrentPosition(px + dir*dx, py + dir*dy) ;
		//driver.increaselength(1);
		
		if (isEndPosition(px,py)) {
			state = Constants.STATE_FINISH;
			//driver.latchFix();
			
			//CHANGE TO THE CORRECT AVTIVITY PAGE
			
			
			notifyViewerRedraw() ;
		}
		walkStep = 0;
		logPosition();
	}

	/**
	 * checks if the given position is outside the maze
	 * @param x
	 * @param y
	 * @return true if position is outside, false otherwise
	 */
	boolean isEndPosition(int x, int y) {
		return x < 0 || y < 0 || x >= mazew || y >= mazeh;
	}



	synchronized void walk(int dir) {
		if (!checkMove(dir)) 
			return;
		for (int step = 0; step != 4; step++) {
			walkStep += dir;
			moveStep();
		}
		walkFinish(dir);
	}

	synchronized void rotate(int dir) {
		final int originalAngle = angle;
		final int steps = 4;

		for (int i = 0; i != steps; i++) {
			angle = originalAngle + dir*(90*(i+1))/steps;
			rotateStep();
		}
		rotateFinish();
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * The next few methods are to be used by the android buttons!
	 */

	/**
	 * Builds from a given skill level.
	 * @param s skill.
	 * @return true if successful
	 * @throws Exception 
	 */
	public boolean skillBuild(int s) throws Exception{
		System.out.println("SkillBuild skill: "+s);
		init();
		if (s<0 || s>9){
			return false;
		}
		System.out.println("Build: "+s);
		build(s);
		return true;
	}
	
	
	
	public void changeH(int h){
		mazeh = h;
	}
	public void changeW(int w){
		mazew = w;
	}
	
	/**
	 * Interrupts the build and returns to the title page.
	 * @return true if successful
	 */
	public boolean Escape(){
		mazebuilder.interrupt();
		buildInterrupted();
		//RETURN TO THE ORRIGINAL SCREEN (?) MIGHT ALREADY BE DONE BY buildInterrupted 
		return true;
	}
	
	/**
	 * Is the up arrow basically.
	 * @return true if successful
	 * @throws Exception 
	 * @throws HitObstacleException 
	 */
	public boolean up()  {
		try {
			r.move(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	
	/**
	 * Is the left arrow, basically.
	 * @return true if successful 
	 * @throws Exception 
	 * @throws UnsupportedArgumentException 
	 */
	public boolean left() {
		try {
			r.rotate(Turn.LEFT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Is the right arrow, basically.
	 * @return true if successful
	 * @throws Exception 
	 * @throws UnsupportedArgumentException
	 */
	public boolean right() {
		try {
			r.rotate(Turn.RIGHT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	

	/**
	 * Restarts the game from play.
	 * @return true if successful.
	 */
	public boolean restartPlay(){
		state = Constants.STATE_TITLE;
		//driver.InterruptDriver();
		notifyViewerRedraw();
		//MOVE TO THE CORRECT ACTIVITY??????
		return true;
	}
	
	/**
	 * Toggles the map of and off.
	 * @return true if successful. 
	 */
	public boolean mapOn(){
		if(showMaze){
			showMaze=!showMaze;
		}
		if (mapMode){
			return true;
		}
		mapMode = !mapMode; 		
		notifyViewerRedraw() ;
		return true;
	}
	
		/**
		 * Toggles the map of and off.
		 * @return true if successful. 
		 */
		public boolean mapOff(){
			if (mapMode){
				mapMode = !mapMode;
			}	
			notifyViewerRedraw() ;
			return true;
		}	
		
	/**
	 * Toggles the full map on and off.
	 * @return true if successful.
	 */
	public boolean mazeShow(){
		if(!mapMode){
			mapMode=!mapMode;
		}
		if(showMaze){
			notifyViewerRedraw() ;
			return true;
		}
		showMaze = !showMaze; 		
		notifyViewerRedraw() ;
		return true;
	}
	
	/**
	 * Toggles the solution on and off.
	 * @return true if successful.
	 */
	public boolean solShow(){
		showSolution = !showSolution; 		
		notifyViewerRedraw() ;
		return true;
	}
	
	/**
	 * Zooms the maze in by one unit.
	 * @return true if successful.
	 */
	public boolean zoomIn(){
		notifyViewerIncrementMapScale() ;
		notifyViewerRedraw() ;
		return true;
	}
	
	/**
	 * Zooms the maze out by one unit.
	 * @return true if successful.
	 */
	public boolean zoomOut(){
		notifyViewerDecrementMapScale() ;
		notifyViewerRedraw() ;
		return true;
	}
	
	/**
	 * Restarts from the finish screen.
	 * @return true if successful. 
	 */
	public boolean restart(){
		state = Constants.STATE_TITLE;
		//CHANGE TO THE CORRECT AVTIVITY PAGE?????
		notifyViewerRedraw() ;
		return true;
	}
	



}


	


