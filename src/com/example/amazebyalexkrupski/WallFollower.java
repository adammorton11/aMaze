package com.example.amazebyalexkrupski;

import com.example.amazebyalexkrupski.Robot.Direction;
import com.example.amazebyalexkrupski.Robot.Turn;

public class WallFollower extends ManualDriver{

	/**
	 * Drives the robot towards the exit given it exists and given the robot's energy supply lasts long enough. 
	 * @return true if driver successfully reaches the exit
	 * @throws exception if robot stopped due to some problem, e.g. lack of energy
	 */
	public boolean drive2Exit() throws Exception {
		robot.setBatteryLevel(2500);

		while(true){
			if (robot.isAtGoal()){
				return true;
			}
			if (robot.hasStopped()){
				throw new Exception("Out of battery");
			}
			
			try {
				if (robot.distanceToObstacle(Direction.LEFT) >= 1){
				robot.rotate(Turn.LEFT);
				robot.move(1);
				continue;
				}
			}
			catch (ArrayIndexOutOfBoundsException e){
				robot.move(1);
			}
			
			
			try{
			if (robot.distanceToObstacle(Direction.FORWARD) >= 1){
				robot.move(1);
				continue;
				}
			}
			catch(ArrayIndexOutOfBoundsException e){
				robot.rotate(Turn.RIGHT);
				}
			
			robot.rotate(Turn.RIGHT);
			
		}
	}
}
 