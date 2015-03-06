package com.example.amazebyalexkrupski;


import java.io.Serializable;

import com.example.amazebyalexkrupski.Robot.Direction;
import com.example.amazebyalexkrupski.Robot.Turn;

public class ManualDriver implements RobotDriver,Serializable {

	Robot robot;
	int mazew;
	int mazeh;
	Distance pathDist;
	float initialBattery;
	int pathCount = 0;
	
	@Override
	public
		// TODO Auto-generated method stub
		/**
		 * Assigns a robot platform to the driver. Not all robot configurations may be suitable such that the method 
		 * will throw an exception if the robot does not match minimal configuration requirements, e.g. providing a sensor
		 * to measure the distance to an object in a particular direction. 
		 * @param r robot to operate
		 * @throws UnsuitableRobotException if driver cannot operate the given robot
		 */
		void setRobot(Robot r) throws UnsuitableRobotException {
			robot = r;
			
			if (!(robot.hasDistanceSensor(Direction.FORWARD) & robot.hasDistanceSensor(Direction.BACKWARD) &
					robot.hasDistanceSensor(Direction.LEFT) & robot.hasDistanceSensor(Direction.RIGHT) & 
					robot.hasJunctionSensor() & robot.hasRoomSensor())){
				throw new UnsuitableRobotException();
			}
			initialBattery = robot.getBatteryLevel();
			
			
			
		}
		
		/**
		 * Provides the robot driver with information on the dimensions of the 2D maze
		 * measured in the number of cells in each direction.
		 * Only some drivers such as Tremaux's algorithm need this information.
		 * @param width of the maze
		 * @param height of the maze
		 * @precondition 0 <= width, 0 <= height of the maze.
		 */
		public void setDimensions(int width, int height) {
			mazew = width;
			mazeh = height;
		}
		/**
		 * Provides the robot driver with information on the distance to the exit.
		 * Only some drivers such as the wizard rely on this information to find the exit.
		 * @param distance gives the length of path from current position to the exit.
		 * @precondition null != distance, a full functional distance object for the current maze.
		 */
		public void setDistance(Distance distance) {
			if (distance != null){
			pathDist = distance;
			}
		}
		/**
		 * Drives the robot towards the exit given it exists and given the robot's energy supply lasts long enough. 
		 * @return true if driver successfully reaches the exit, false otherwise
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
			}
		}
		/**
		 * Method incorporates all reactions to keyboard input in original code, 
		 * after refactoring, Java Applet and Java Application wrapper call this method to communicate input.
		 * @throws Exception 
		 */
		/*public boolean keyDown(Event evt, int key)  {

				switch (key) {
				case Event.UP: case 'k': case '8':
					try {
						robot.move(1);
						pathCount++;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case Event.LEFT: case 'h': case '4':
					try {
						robot.rotate(Turn.LEFT);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case Event.RIGHT: case 'l': case '6':
					try {
						robot.rotate(Turn.RIGHT);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case Event.DOWN: case 'j': case '2':
					break;
				}
				return true;
				
		}*/
		/**
		 * Returns the total energy consumption of the journey, i.e.,
		 * the difference between the robot's initial energy level at
		 * the starting position and its energy level at the exit position. 
		 * This is used as a measure of efficiency for a robot driver.
		 */
		public float getEnergyConsumption() {
			return initialBattery - robot.getBatteryLevel();
		}
		
		/**
		 * Returns the total length of the journey in number of cells traversed. 
		 * Being at the initial position counts as 0. 
		 * This is used as a measure of efficiency for a robot driver.
		 */
		
	}