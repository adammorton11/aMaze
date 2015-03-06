package com.example.amazebyalexkrupski;

import java.util.ArrayList;

import com.example.amazebyalexkrupski.Robot.Direction;
import com.example.amazebyalexkrupski.Robot.Turn;

/**
 * Drives the robot towards the exit given it exists and given the robot's energy supply lasts long enough. 
 * @return true if driver successfully reaches the exit
 * @throws exception if robot stopped due to some problem, e.g. lack of energy
 */
public class Wizard extends ManualDriver {
	public boolean drive2Exit() throws Exception {	
		//robot.rotate(Turn.LEFT);
		int checkdist;
		robot.setBatteryLevel(2500);

		while(true){
			checkdist = 0;
			if (robot.isAtGoal()){
				return true;
			}
			
			if (robot.hasStopped()){
				throw new Exception("Out of battery");
			}
			if (robot.isInsideRoom()){
				System.out.println("Is in room");
				boolean b;
				b = roomhandler();
				if (b == true) {return true;}
				continue;
			}
			
			int[] parray = robot.getCurrentPosition();
			int[] darray = robot.getCurrentDirection();
			
			int current = pathDist.getDistance(parray[0], parray[1]);
			
			if (parray[0]+darray[0] >= 0 & parray[0]+darray[0] < robot.getMaze().mazew){
				if (parray[1]+darray[1] >= 0 & parray[1]+darray[1] < robot.getMaze().mazeh){
					checkdist = pathDist.getDistance(parray[0]+darray[0], parray[1]+darray[1]);
				}
			}
			
			System.out.format("check %d \n", checkdist);
			System.out.format("%d %d \n", darray[0], darray[1]);
			if (checkdist == (current-1)){
				try{
				robot.cheat(1);
				if (robot.distanceToObstacle(Direction.FORWARD) >= 1){
					robot.move(1);
					if (robot.isAtGoal()){
						return true;
					}
				}
				else {
					robot.rotate(Turn.LEFT);
				}
			}
				catch(ArrayIndexOutOfBoundsException e){
					robot.move(1);
				}
			}
			else{
				robot.cheat(3);
				robot.rotate(Turn.LEFT);
			}
			
		}
	}
	public boolean roomhandler() throws Exception{
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
			robot.move(1);
			if (robot.isAtGoal()){
				return true;
			}

		}
		else if(robot.distanceToObstacle(Direction.FORWARD) >= 1){
			robot.move(1);
			if (robot.isAtGoal()){
				return true;
			}
		}
		else{
			robot.rotate(Turn.RIGHT);
			}
		}
		
		return false;
	}

}