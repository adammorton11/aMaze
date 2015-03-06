package com.example.amazebyalexkrupski;

import java.util.ArrayList;

import com.example.amazebyalexkrupski.Robot.Direction;
import com.example.amazebyalexkrupski.Robot.Turn;

public class Gambler extends ManualDriver {
	
	/**
	 * Drives the robot towards the exit given it exists and given the robot's energy supply lasts long enough. 
	 * @return true if driver successfully reaches the exit, false otherwise
	 * @throws exception if robot stopped due to some problem, e.g. lack of energy
	 */
	public boolean drive2Exit() throws Exception {
		SingleRandom random = SingleRandom.getRandom();
		robot.setBatteryLevel(2500);
		while(true){
			ArrayList<Direction> movelist = new ArrayList<Direction>();
			
			 
			if (robot.isAtGoal()){
				return true;
			}
			if (robot.hasStopped()){
				throw new Exception("Out of battery");
			}
			if (robot.distanceToObstacle(Direction.FORWARD) >= 1){
				movelist.add(Direction.FORWARD);
			}
		
			if (robot.distanceToObstacle(Direction.LEFT) >= 1){
				movelist.add(Direction.LEFT);
			}
			
			robot.rotate(Turn.AROUND);
			
			if (robot.distanceToObstacle(Direction.LEFT) >= 1){
				movelist.add(Direction.RIGHT);
			}
			if (robot.distanceToObstacle(Direction.FORWARD) >= 1){
				movelist.add(Direction.BACKWARD);
			}
			
			int r = random.nextIntWithinInterval(0, (movelist.size()- 1));
			
			if (movelist.get(r) == Direction.FORWARD){
				robot.rotate(Turn.AROUND);
				if(robot.distanceToObstacle(Direction.FORWARD)>=1){
				robot.move(1);
				}
			}
			
			if (movelist.get(r) == Direction.RIGHT){
				robot.rotate(Turn.LEFT);
				if(robot.distanceToObstacle(Direction.FORWARD)>=1){
				robot.move(1);
				}
			}
			
			if (movelist.get(r) == Direction.BACKWARD){
				if(robot.distanceToObstacle(Direction.FORWARD)>=1){
				robot.move(1);
				}
			}
			
			if (movelist.get(r) == Direction.LEFT){
				robot.rotate(Turn.RIGHT);
				if(robot.distanceToObstacle(Direction.FORWARD)>=1){
				robot.move(1);
				}
			}
		}
	}
}