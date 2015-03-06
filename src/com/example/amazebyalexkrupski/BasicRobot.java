package com.example.amazebyalexkrupski;

import android.util.Log;

import com.example.amazebyalexkrupski.Constants;



public class BasicRobot implements Robot {
	
	boolean stopped = false;
	float batteryLevel = 0;
	public Maze maze;
	Cells cells;
	
	//current position of robot
	int px, py;
	
	//current forward direction
	int dx, dy;
	
	
	int[] positionArray;
	int[] directionArray;
	
	

	/**
	 * Turn robot on the spot. If robot runs out of energy, it stops and throws an Exception, 
	 * which can be checked by hasStopped() == true and by checking the battery level. 
	 * @param direction to turn to relative to current forward direction 
	 * @throws Exception if the robot stops for lack of energy. 
	 */
	public void rotate(Turn turn) throws Exception {
		
		dx = maze.dx;
		dy = maze.dy;
		
		if (turn == Turn.RIGHT){
			batteryLevel -= (getEnergyForFullRotation()/4);
			if (batteryLevel <= 0){
				throw new Exception("Battery is depleted");
			}
			
			
			//If right, make top
			if (dx == 1 & dy == 0){
				dx = 0;
				dy = -1;
			}
			// If top, make left
			else if (dx == 0 & dy == -1){
				dx = -1;
				dy = 0;
			}
			else if (dx == -1 & dy == 0){
				dx = 0;
				dy = 1;
			}
			else if (dx == 0 & dy == 1){
				dx = 1;
				dy = 0;
			}
			else{
				throw new Exception("Not a supported turn direction");
			}
			maze.dx = dx;
			maze.dy = dy; 
			
			maze.viewdx = dx << 16; 
			maze.viewdy = dy << 16;
			
			if (maze.panel != null){
				maze.notifyViewerRedraw();
			}
		}
		
		if (turn == Turn.LEFT){
			batteryLevel -= (getEnergyForFullRotation()/4);
			if (batteryLevel <= 0){
				throw new Exception("Battery is depleted");
				
			}
			// columns mean right, bottom, left, top (as implemented in getBit())
			//public static int[] DIRS_X = { 1, 0, -1, 0 };
			//public static int[] DIRS_Y = { 0, 1, 0, -1 };
			//If right, make bottom
			if (dx == 1 & dy == 0){
				dx = 0;
				dy = 1;
			}
			// If bottom, make left
			else if (dx == 0 & dy == 1){
				dx = -1;
				dy = 0;
			}
			//if left, make top
			else if (dx == -1 & dy == 0){
				dx = 0;
				dy = -1;
			}
			// if top, make right
			else if (dx == 0 & dy == -1){
				dx = 1;
				dy = 0;
			}
			else{
				throw new Exception("Not a supported turn direction");
			}
			maze.dx = dx;
			maze.dy = dy;
			
			maze.viewdx = dx << 16;
			maze.viewdy = dy << 16;
			if (maze.panel != null){
				maze.notifyViewerRedraw();
			}
		}
		
		if (turn == Turn.AROUND){
			batteryLevel -= (getEnergyForFullRotation()/2);
			if (batteryLevel <= 0){
				throw new Exception("Battery is depleted");
			}
			dx = -dx;
			dy = -dy;
			maze.dx = dx;
			maze.dy = dy;
			
			maze.viewdx = dx << 16;
			maze.viewdy = dy << 16;
			if (maze.panel != null){
				maze.notifyViewerRedraw();
			}
		}
	}	

	/**
	 * Moves robot forward a given number of steps. A step matches a single cell.
	 * Since a robot may only have a distance sensor in its front.
	 * If the robot runs out of energy somewhere on its way, it stops, 
	 * which can be checked by hasStopped() == true and by checking the battery level. 
	 * If the robot hits an obstacle like a wall, it remains at the position in front 
	 * of the obstacle but hasStopped() == false.
	 * @param distance is the number of cells to move in the robot's current forward direction
	 * @throws Exception if robot hits an obstacle like a wall or border, 
	 * which indicates that current position is not as expected. 
	 * Also thrown if robot runs out of energy. 
	 * @precondition distance >= 0
	 */
	
	public void move(int distance) throws Exception {
		px = maze.px;
		py = maze.py;
		dx = maze.dx;
		dy = maze.dy;
		
		if (distance <= 0){
			throw new Exception("invalid distance, negative");
		}
		int direction;
		
		//If right
		if (dx == 1 & dy == 0){
			direction = 3;
		}
		// If bottom
		else if (dx == 0 & dy == 1){
			direction = 1;
		}
		//if left
		else if (dx == -1 & dy == 0){
			direction = 2;
		}
		// if top
		else if (dx == 0 & dy == -1){
			direction = 0;
		}
		else{
			throw new Exception("Not a supported turn direction");
		}

		
		
		for (int i = 0; i < distance; i++){
			if (batteryLevel <= 0){
				stopped = true;
				throw new Exception("Out of Battery");
			}
			if(hasWallInFront(px,py,direction)){
				Log.i("Vibrate", "BZZZ");
				throw new Exception("Obstacle hit");
				
			}
 
			px += dx;
			py += dy;
			maze.px = px;
			maze.py = py;
			
			if(this.isAtGoal()){
				maze.state = Constants.STATE_FINISH;
			}
			
			if (maze.panel != null){
				maze.notifyViewerRedraw();
			}
			batteryLevel -= getEnergyForStepForward();
		}

	}
	
	public boolean hasWallInFront(int x, int y, int direction) {
		return maze.mazecells.hasMaskedBitsTrue(x, y, (int)Math.pow(2,direction));
	}
	
	public Maze getMaze(){
		return maze;
	}
	
	public void cheat(int amount){
		batteryLevel += amount;
	}
	/**
	 * Provides the current position as (x,y) coordinates for the maze cell as an array of length 2 with [x,y].
	 * @postcondition 0 <= x < width, 0 <= y < height of the maze. 
	 * @return array of length 2, x = array[0], y=array[1]
	 * @throws Exception if position is outside of the maze
	 */
	public int[] getCurrentPosition() throws Exception  {
		int[] positionArray = new int[2];
		positionArray[0] = px;
		positionArray[1] = py;
		return positionArray;
	}
	/**
	 * Provides the robot with a reference to the maze it is currently in.
	 * The robot memorizes the maze such that this method is most likely called only once
	 * and for initialization purposes. The maze serves as the main source of information
	 * about the current location, the presence of walls, the reaching of an exit.
	 * @param maze is the current maze
	 * @precondition maze != null, maze refers to a fully operational, configured maze object
	 */
	public void setMaze(Maze maze) {
		
		if (maze != null){
			this.maze = maze;
			this.dx = maze.dx;
			this.dy = maze.dy;
			this.px = maze.px;
			this.py = maze.py;
		}
	}
	/**
	 * Tells if current position is at the goal (the exit). Used to recognize termination of a search.
	 * @return true if robot is at the goal, false otherwise
	 */
	public boolean isAtGoal() {
			return this.maze.mazecells.isExitPosition(px, py);
	}
	/**
	 * Tells if a sensor can identify the goal in given direction relative to 
	 * the robot's current forward direction from the current position.
	 * @return true if the goal (here: exit of the maze) is visible in a straight line of sight
	 * @throws Exception 
	 */
	public boolean canSeeGoal(Direction direction) throws Exception {
		try{
		if (distanceToObstacle(direction) == Integer.MAX_VALUE){
			
		
			return true;
		}
		else{
			return false;
		}
		} catch (ArrayIndexOutOfBoundsException e){
			return true;
		}
		
	}
	/**
	 * Tells if current position is at a junction. 
	 * A junction is a position where there is no wall to the robot's right or left. 
	 * Note that this method is not helpful when the robot is inside a room. 
	 * For most positions inside a room, the robot has no walls to it's right or left
	 * such that the method returns true.
	 * @return true if robot is at a junction, false otherwise
	 * @throws Exception 
	 */	
	public boolean isAtJunction() throws Exception {
		batteryLevel += 2;
		if(this.distanceToObstacle(Direction.LEFT)>=1 & this.distanceToObstacle(Direction.RIGHT)>=1){
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * Tells if the robot has a junction sensor.
	 */
	public boolean hasJunctionSensor() {
		return true;
	}
	/**
	 * Tells if current position is inside a room. 
	 * @return true if robot is inside a room, false otherwise
	 * @throws UnsupportedOperationException if not supported by robot
	 */	
	public boolean isInsideRoom() throws UnsupportedOperationException {
		return this.maze.mazecells.isInRoom(px, py);
	}
	/**
	 * Tells if the robot has a room sensor.
	 */
	public boolean hasRoomSensor() {
		return true;
	}
	/**
	 * Provides the current direction as (dx,dy) values for the robot as an array of length 2 with [dx,dy].
	 * Note that dx,dy are elements of {-1,0,1} and as in bitmasks masks in Cells.java and dirsx,dirsy in MazeBuilder.java.
	 * 
	 * @return array of length 2, dx = array[0], dy=array[1]
	 */	
	public int[] getCurrentDirection() {
		int[] directionArray = new int[]{dx,dy};
		
		directionArray[0] = dx;
		directionArray[1] = dy;
		
		//System.out.println(directionArray[0] );
		//System.out.println(directionArray[1] );
		return directionArray;
	}
	/**
	 * Returns the current battery level.
	 * The robot has a given battery level (energy level) that it draws energy from during operations. 
	 * The particular energy consumption is device dependent such that a call for distance2Obstacle may use less energy than a move forward operation.
	 * If battery level <= 0 then robot stops to function and hasStopped() is true.
	 * @return current battery level, level is > 0 if operational. 
	 */
	public float getBatteryLevel() {
		return batteryLevel;
	}
	/**
	 * Sets the current battery level.
	 * The robot has a given battery level (energy level) that it draws energy from during operations. 
	 * The particular energy consumption is device dependent such that a call for distance2Obstacle may use less energy than a move forward operation.
	 * If battery level <= 0 then robot stops to function and hasStopped() is true.
	 * @param level is the current battery level
	 * @precondition level >= 0 
	 */
	public void setBatteryLevel(float level) {
		batteryLevel = level;
	}
	/**
	 * Gives the energy consumption for a full 360 degree rotation.
	 * Scaling by other degrees approximates the corresponding consumption. 
	 * @return energy for a full rotation
	 */
	public float getEnergyForFullRotation() {
		return 12;
	}
	/**
	 * Gives the energy consumption for moving forward for a distance of 1 step.
	 * For simplicity, we assume that this equals the energy necessary 
	 * to move 1 step backwards and that scaling by a larger number of moves is 
	 * approximately the corresponding multiple.
	 * @return energy for a single step forward
	 */
	public float getEnergyForStepForward() {
		return 5;
		
	}
	/**
	 * Tells if the robot has stopped for reasons like lack of energy, hitting an obstacle, etc.
	 * @return true if the robot has stopped, false otherwise
	 */
	public boolean hasStopped() {
		if(batteryLevel <= 0){
			stopped = true;
		}
		
		return stopped;
	}

	/**
	 * Tells the distance to an obstacle (a wall or border) for a the robot's current forward direction.
	 * Distance is measured in the number of cells towards that obstacle, 
	 * e.g. 0 if current cell has a wall in this direction
	 * @return number of steps towards obstacle if obstacle is visible 
	 * in a straight line of sight, Integer.MAX_VALUE otherwise
	 * @throws Exception 
	 */
	public int distanceToObstacle(Direction direction) throws Exception {

		int toReturn;
		
		if (direction == Direction.RIGHT){

				this.batteryLevel += getEnergyForFullRotation()/2;
				rotate(Turn.RIGHT);

			toReturn = checkDistance();

				rotate(Turn.LEFT);
	
			
		}
		else if (direction == Direction.LEFT){
				this.batteryLevel += getEnergyForFullRotation()/2;
				rotate(Turn.LEFT);
			toReturn = checkDistance();
				rotate(Turn.RIGHT);
		}
		else if (direction == Direction.FORWARD){
			toReturn = checkDistance();
			
		}
		
		else if (direction == Direction.BACKWARD){
			try {
				this.batteryLevel += getEnergyForFullRotation();
				rotate(Turn.AROUND);
			} catch (Exception e) {
				e.printStackTrace();
			}
			toReturn = checkDistance();
			try {
				rotate(Turn.AROUND);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			
		}
		else{
			throw new Exception();
		
		}
	
		batteryLevel -= 1;
		return toReturn;

	}
	
	public int checkDistance(){
		int distance;
		int count = 0;
		int currentdirection;
		int tempx = px;
		int tempy = py;
		dx = maze.dx;
		dy = maze.dy;
		// columns mean right, bottom, left, top (as implemented in getBit())
					//public static int[] DIRS_X = { 1, 0, -1, 0 };
					//public static int[] DIRS_Y = { 0, 1, 0, -1 };
				//If East
				if (dx == 1 & dy == 0){
					currentdirection = 3;
					distance = maze.mazew - px;
				}
				// If South
				else if (dx == 0 & dy == 1){
					currentdirection = 1;
					distance = maze.mazeh - py;
				}
				//if West
				else if (dx == -1 & dy == 0){
					currentdirection = 2;
					distance = px;
				}
				// if North
				else if (dx == 0 & dy == -1){
					currentdirection = 0;
					distance = py;
				}
				else{
					throw new UnsupportedOperationException();
				}
		
		for (int i = 0; i <= distance; i++){
			try{
			if(hasWallInFront(tempx,tempy,currentdirection)){
				return i;
			}
			}
			catch(ArrayIndexOutOfBoundsException e){
				return i;
			}
			tempx += dx;
			tempy += dy;
		}
		
		return Integer.MAX_VALUE;

	}
	@Override
	public boolean hasDistanceSensor(Direction direction) {
		return true;
	}
}