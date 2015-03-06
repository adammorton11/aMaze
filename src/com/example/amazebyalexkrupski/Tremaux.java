package com.example.amazebyalexkrupski;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.amazebyalexkrupski.Robot.Direction;
import com.example.amazebyalexkrupski.Robot.Turn;

public class Tremaux extends ManualDriver {
	ArrayList<int[]> firstList = new ArrayList<int[]>(0);
	ArrayList<int[]> secondList = new ArrayList<int[]>(0);
	ArrayList<int[]> junctionList = new ArrayList<int[]>(0);
	
	SingleRandom random = new SingleRandom();
	int rightcount = 0;
	boolean b;
	int[] prev = null;

	/**
	 * Drives the robot towards the exit given it exists and given the robot's energy supply lasts long enough. 
	 * @return true if driver successfully reaches the exit, false otherwise
	 * @throws exception if robot stopped due to some problem, e.g. lack of energy
	 */
	public boolean drive2Exit() throws Exception {
		robot.setBatteryLevel(2500);

		firstList.add(robot.getCurrentPosition());
		while(true){ 
			firstList.trimToSize();
			secondList.trimToSize();
			if (robot.isInsideRoom()){
				System.out.println("Is in room");
				b = roomhandler(firstList,secondList);
				if (b == true) {return true;}
				continue;
			}
			else if (robot.isAtJunction() & !juncContains()){
				
					junctionList.add(pos());
					System.out.println("Adding to j list");
					if ((!backTracking(prev) & !juncContains(prev)) | (SecondContains() & !backTracking(prev))){
						System.out.println("Turning Around");
						robot.rotate(Turn.AROUND);
						robot.move(1);
						mark();
					}
				
			}
			else if (juncContains()){
					System.out.println("In j list");
					b = juncListHandler(firstList,secondList, prev);
					if (b == true) {return true;}
			}
			
			else if ((robot.distanceToObstacle(Direction.LEFT) >= 1)){
				System.out.println("Turning Left");
				robot.rotate(Turn.LEFT);
				b = forwardHandler(firstList,secondList);
				if (b == true) {return true;}
			}	
			else if (robot.distanceToObstacle(Direction.FORWARD) >= 1){
				rightcount = 0;
				System.out.println("Going Forward");
				b = forwardHandler(firstList,secondList);
				if (b == true) {return true;}
			}
			else{
				rightcount += 1;
				System.out.println("Going Right");
				robot.rotate(Turn.RIGHT);
				if (rightcount >= 2){
					mark();
				}
			}
			prev = pos();
		}		
	}

	private boolean forwardHandler(ArrayList<int[]> firstList,
			ArrayList<int[]> secondList) throws UnsupportedOperationException, Exception {

			if(robot.distanceToObstacle(Direction.FORWARD) >= 1){
				robot.move(1);
				mark();
			}
			if (robot.isAtGoal()){
				return true;
			}
			
			
			return false;
		
	}
	
	private boolean backTracking(int[] prev) throws Exception{
		if (SecondContains(prev)){
			return true;
		}
		else{
			return false;
		}
	}
	

	private boolean juncListHandler(ArrayList<int[]> firstList,
			ArrayList<int[]> secondList, int[] prev) throws Exception {
		ArrayList<Direction> zeroMarks = new ArrayList<Direction>(0);
		ArrayList<Direction> oneMarks = new ArrayList<Direction>(0);
		
		System.out.println("InJunctionList");

		if (robot.distanceToObstacle(Direction.FORWARD) >= 1){
			int[] cellArray = new int[2];
			cellArray[0] = (pos()[0] + robot.getCurrentDirection()[0]);
			cellArray[1] = (pos()[1] + robot.getCurrentDirection()[1]);
			if (SecondContains(cellArray)){
				System.out.println("Recognized something is in the second list");
			}
			else if (FirstContains(cellArray)){
				oneMarks.add(Direction.FORWARD);
				System.out.println("Mark One");
			}
			else{
				zeroMarks.add(Direction.FORWARD);
				System.out.println("Mark Zero");
			}
		}
		robot.rotate(Turn.LEFT);
		if (robot.distanceToObstacle(Direction.FORWARD) >= 1){
			int[] cellArray = new int[2];
			cellArray[0] = (pos()[0] + robot.getCurrentDirection()[0]);
			cellArray[1] = (pos()[1] + robot.getCurrentDirection()[1]);
			if (SecondContains(cellArray)){
				System.out.println("Recognized something is in the second list");
			}
			else if (FirstContains(cellArray)){
				oneMarks.add(Direction.LEFT);
				System.out.println("Mark One");
			}
			else{
				zeroMarks.add(Direction.LEFT);
				System.out.println("Mark Zero");
			}
		}
		robot.rotate(Turn.LEFT);
		if (robot.distanceToObstacle(Direction.FORWARD) >= 1){
			int[] cellArray = new int[2];
			cellArray[0] = (pos()[0] + robot.getCurrentDirection()[0]);
			cellArray[1] = (pos()[1] + robot.getCurrentDirection()[1]);
			if (SecondContains(cellArray)){
				System.out.println("Recognized something is in the second list");
			}
			else if (FirstContains(cellArray)){
				oneMarks.add(Direction.BACKWARD);
				System.out.println("Mark One");
			}
			else{
				zeroMarks.add(Direction.BACKWARD);
				System.out.println("Mark Zero");
			}
		}
		robot.rotate(Turn.LEFT);
		if (robot.distanceToObstacle(Direction.FORWARD) >= 1){
			int[] cellArray = new int[2];
			cellArray[0] = (pos()[0] + robot.getCurrentDirection()[0]);
			cellArray[1] = (pos()[1] + robot.getCurrentDirection()[1]);
			if (SecondContains(cellArray)){
				System.out.println("Recognized something is in the second list");
			}
			else if (FirstContains(cellArray)){
				oneMarks.add(Direction.RIGHT);
				System.out.println("Mark One");
			}
			else{
				zeroMarks.add(Direction.RIGHT);
				System.out.println("Mark Zero");
			}
		}
		robot.rotate(Turn.LEFT);
		Direction d = null;
		if (zeroMarks.isEmpty()){		
			if (!oneMarks.isEmpty()){
				int r = random.nextIntWithinInterval(0, oneMarks.size()-1);
				d = oneMarks.get(r);
				System.out.println(oneMarks.get(r));
				oneMarks.remove(r);				
			}
			
		}
		else if (!zeroMarks.isEmpty()){
				int r = random.nextIntWithinInterval(0, zeroMarks.size()-1);
				d = zeroMarks.get(r);
				System.out.println(zeroMarks.get(r));
				zeroMarks.remove(r);
			}

		//Turn that direction
		if (d == Direction.FORWARD){
			forwardHandler(firstList, secondList);
		}
		
		if (d == Direction.LEFT){
			robot.rotate(Turn.LEFT);
			forwardHandler(firstList, secondList);
		}
		
		if (d == Direction.BACKWARD){
			robot.rotate(Turn.AROUND);
			forwardHandler(firstList, secondList);
		}
		
		if (d == Direction.RIGHT){
			robot.rotate(Turn.RIGHT);
			forwardHandler(firstList, secondList);
		}
		if (robot.isAtGoal()){
			return true;
		}
		else{
			return false;
		}

	}

	

	public int contains(ArrayList<int[]> list, int[] item){
		for (int i = 0; i < list.size(); i++){
			if (list.get(i)[0] == item[0]){
				if (list.get(i)[1] == item[1]){
					return i;
				}
			}
		}
		return -1;
		
		
	}
	public boolean roomhandler(ArrayList<int[]> firstList, ArrayList<int[]> secondList) throws Exception{
		/*if (!backTracking(prev) & (FirstContains() | SecondContains())){
			System.out.println("Turning Around");
			robot.rotate(Turn.AROUND);
			robot.move(1);
			mark();
		}*/
	while(robot.isInsideRoom()){
		
		if (robot.isAtGoal()){
			return true;
		}
		if (robot.hasStopped()){
			throw new Exception("Out of battery");
		}
		
		if (robot.distanceToObstacle(Direction.LEFT) >= 1){
			robot.rotate(Turn.LEFT);
			prev = pos();
			robot.move(1);
			if (robot.isAtGoal()){
				return true;
			}

			mark();
		}
		else if(robot.distanceToObstacle(Direction.FORWARD) >= 1){
			prev = pos();
			robot.move(1);
			if (robot.isAtGoal()){
				return true;
			}
			mark();
		}
		else{
			robot.rotate(Turn.RIGHT);
			}
		}
		
		return false;
	}

	public void mark() throws Exception{
		if (SecondContains(pos())){
			System.out.println("In Second List");
		}
		else if(FirstContains(pos())){
			System.out.println("Added to second List");
			firstList.remove(contains(firstList,pos()));
			secondList.add(pos());
		}
		else{
			System.out.println("Added to first List");
			firstList.add(pos());
		}
	}
	
	public int[] pos() throws Exception{
		return robot.getCurrentPosition();
	}
	
	public boolean FirstContains() throws Exception{
		return(contains(firstList,pos()) != -1);
	}
	public boolean SecondContains() throws Exception{
		return(contains(secondList,pos()) != -1);
	}
	public boolean FirstContains(int[] array) throws Exception{
		return(contains(firstList,array) != -1);
	}
	public boolean SecondContains(int[] array) throws Exception{
		return(contains(secondList,array) != -1);
	}
	public boolean juncContains() throws Exception{
		return(contains(junctionList,pos()) != -1);
	}
	public boolean juncContains(int[] prev) throws Exception{
		return(contains(junctionList,prev) != -1);
	}
	
	
	public void face(int direction) throws Exception{
		//original
		
		//new
		//Directions 0 1 2 3 == north south west east
		
		
		
		int[] parray = robot.getCurrentPosition();
		int[] darray = robot.getCurrentDirection();
		
		if (direction == 1){
			if ((darray[0] == 1) && (darray[1] == 0)){ //east
				robot.rotate(Turn.RIGHT);
			}
			else if ((darray[0] == 0) && (darray[1] == 1)){ //south
				
			}
			else if ((darray[0] == -1) && (darray[1] == 0)){ //west
					robot.rotate(Turn.LEFT);
			}
			else if ((darray[0] == 0) && (darray[1] == -1)){ //north

				robot.rotate(Turn.AROUND);
			}
		
	}
		else if (direction == 3 ){
			// columns mean right, bottom, left, top (as implemented in getBit())
			// note that multiplication with -1 to a column switches directions
			//public static int[] DIRS_X = { 1, 0, -1, 0 };
			//public static int[] DIRS_Y = { 0, 1, 0, -1 };

			if ((darray[0] == 1) && (darray[1] == 0)){ //east
				
			}
			else if ((darray[0] == 0) && (darray[1] == 1)){ //south
				
					robot.rotate(Turn.LEFT);
			}
			else if ((darray[0] == -1) && (darray[1] == 0)){ //west

				robot.rotate(Turn.AROUND);
			}
			else if ((darray[0] == 0) && (darray[1] == -1)){ //north

				robot.rotate(Turn.RIGHT);
		}
	

		
		
	}
		else if (direction == 0){
			// columns mean right, bottom, left, top (as implemented in getBit())
			// note that multiplication with -1 to a column switches directions
			//public static int[] DIRS_X = { 1, 0, -1, 0 };
			//public static int[] DIRS_Y = { 0, 1, 0, -1 };
			
			if ((darray[0] == 1) && (darray[1] == 0)){ //east			
				robot.rotate(Turn.LEFT);
			}
			else if ((darray[0] == 0) && (darray[1] == 1)){ //south
				robot.rotate(Turn.AROUND);
			}
			else if ((darray[0] == -1) && (darray[1] == 0)){ //west
				robot.rotate(Turn.RIGHT);
			}
			else if ((darray[0] == 0) && (darray[1] == -1)){ //north

			

		}
		
	}
		else if (direction == 2){

			if ((darray[0] == 1) && (darray[1] == 0)){ //east
				robot.rotate(Turn.AROUND);
			}
			else if ((darray[0] == 0) && (darray[1] == 1)){ //south
				robot.rotate(Turn.RIGHT);
			}
			else if ((darray[0] == -1) && (darray[1] == 0)){ //west

			}
			else if ((darray[0] == 0) && (darray[1] == -1)){ //north			
					robot.rotate(Turn.LEFT);
			}
		}
	}
	public void setMaze(Maze maze){
		robot.setMaze(maze);
	}
}
