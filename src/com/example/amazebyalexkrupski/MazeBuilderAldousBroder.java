package com.example.amazebyalexkrupski;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.amazebyalexkrupski.Constants;

/**
 * This class has the responsibility to create a maze of given dimensions (width, height) together with a solution based on a distance matrix.
 * The Maze class depends on it. The MazeBuilder performs its calculations within its own separate thread such that communication between 
 * Maze and MazeBuilder operates as follows. Maze calls the build() method and provides width and height. Maze has a call back method newMaze that
 * this class calls to communicate a new maze and a BSP root node and a solution. 
 * This class uses the AldousBroder maze generation algorithm. 

 * @author Maverick
 *
 */

public class MazeBuilderAldousBroder extends MazeBuilder implements Runnable {
int remaining;
int direction;

int[] darray;

int[] currentcell;
int[] checkcell;
int[] temp;

ArrayList <int[]> visitedList = new ArrayList<int[]>();


	/**
	 * This method generates a maze. It computes distances, determines a start and exit position that are as far apart as possible
	 * It iterates randomly through every cell in the maze, knocking down a minimum of 1 wall per cell visited.
	 * This ensures a random maze with one best pathway from start to finish. It combines all functionality of "generate" and "generatePathways"
	 * found in the original mazebuilder class. 
	 */
	
	public MazeBuilderAldousBroder(){
		random = SingleRandom.getRandom();
	}
	@Override
	public void generate() {
		
		remaining = ((width*height)-1);
		startx = random.nextIntWithinInterval(0, width-1);
		starty = random.nextIntWithinInterval(0, height-1);
		
		currentcell = new int[2];
		
		//Set current coordinates to start.
		currentcell[0] = startx;
		currentcell[1] = starty;
		
		
		visitedList.add(currentcell.clone());
		
		while (remaining>0){
			boolean inList = false;
			for (int i = 0; i < visitedList.size(); i += 1){
				if(Arrays.equals(visitedList.get(i), currentcell.clone())){
					inList = true;
					break;
				}
			}
			if (inList){
				currentcell = oldHandler(currentcell);
			}
			else{
				remaining -= 1;
				visitedList.add(currentcell.clone());

				currentcell = newHandler(currentcell);
			}
		}
		final int[] remote = dists.computeDistances(cells) ;
		final int[] pos = dists.getStartPosition();
		startx = pos[0] ;
		starty = pos[1] ;
		cells.setExitPosition(remote[0], remote[1]);

	}
	/**
	 * This method gets and returns the next random direction. When called, it returns a 2 element
	 * integer array containing the binary direction found in Constants.java. Indexed x, y
	 * @return
	 */
public int[] getNextRandom(){
		int direction = random.nextIntWithinInterval(0, 3);
		int directionx = (int) Constants.DIRS_X[direction];
		int directiony = (int) Constants.DIRS_Y[direction];
		int[] darray = new int[2];
		darray[0] = directionx;
		darray[1] = directiony;
		return darray;
	}

/**
 * This method handles a new cell, that is, one that has yet to be visited by the algorithm. It adds this cell to the list of previously visited cells,
 * then knocks down the wall from the previous cell to this one. It then picks a new direction and returns the cell coordinates of the cell in that direction. 
 * @param cur
 * @return
 */
public int[] newHandler(int[] cur){
	while (true){
		
		
		temp = cur.clone();
		cells.deleteWall(temp[0], temp[1], -darray[0], -darray[1]);
		darray = getNextRandom();
		
		temp[0] += darray[0];
		temp[1] += darray[1];
		
		if ((temp[0] >= width) | (temp[1] >= height) | (temp[0] <0) | (temp[1] <0)){
			continue;
		}
		else{
			cur = temp.clone();
			break;
		}
	}
	return cur;
	
}

/**
 * This method handles a cell that has been visited previously. No changes are made to said cell.
 * The handler chooses a random direction, then returns the coordinates of the cell in that direction. 
 * @param cur
 * @return
 */
public int[] oldHandler(int[] cur){
	
	while (true){
		temp = cur.clone();
		darray = getNextRandom();
		
		temp[0] += darray[0];
		temp[1] += darray[1];
		
		if ((temp[0] >= width) | (temp[1] >= height) | (temp[0] <0) | (temp[1] <0)){
			continue;
		}
		else{
			cur = temp.clone();
			break;
		}
	}
	return cur;
}

}