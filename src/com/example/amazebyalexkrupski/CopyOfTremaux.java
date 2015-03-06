package com.example.amazebyalexkrupski;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.amazebyalexkrupski.Robot.Direction;
import com.example.amazebyalexkrupski.Robot.Turn;

public class CopyOfTremaux extends ManualDriver {
	public boolean drive2Exit() throws Exception {
		ArrayList<int[]> firstList = new ArrayList<int[]>();
		ArrayList<int[]> secondList = new ArrayList<int[]>();
		ArrayList<int[]> junctionList = new ArrayList<int[]>();
		robot.setBatteryLevel(100000);
		firstList.add(robot.getCurrentPosition());
		while(true){
			if (robot.canSeeGoal(Direction.FORWARD)){
				while (robot.canSeeGoal(Direction.FORWARD)){
					robot.move(1);
					if (robot.isAtGoal()){
						return true;
					}
				}
			}
			
			
			int[] cellArray = new int[2];
			
			
			
			
			if (contains(junctionList,robot.getCurrentPosition()) != -1){
				
				ArrayList<Integer> zeroMarks = new ArrayList<Integer>(0);
				ArrayList<Integer> oneMarks = new ArrayList<Integer>(0);
				System.out.println("InJunctionList");
				
				
				
				for (int i = 0; i < 4; i++){
					
					face(i);
					if (robot.distanceToObstacle(Direction.FORWARD) >= 1){
						
						cellArray[0] = robot.getCurrentPosition()[0] + robot.getCurrentDirection()[0];
						cellArray[1] = robot.getCurrentPosition()[1] + robot.getCurrentDirection()[1];
	
						if ((contains(firstList,cellArray) == -1)) { 
							if (contains(secondList,cellArray) == -1){
								if(contains(junctionList,cellArray) == -1)
									zeroMarks.add(i);
							}
						}
						
						else if (!(contains(firstList,cellArray) == -1)) {
							if (contains(secondList,cellArray) == -1){
								oneMarks.add(i);
							}
						}
					}
				}
				if (zeroMarks.isEmpty()){
					
					if (!oneMarks.isEmpty()){

					face(oneMarks.get(0));
					//Directions 0 1 2 3 == north east south west
					if (oneMarks.get(0) == 0){
						System.out.println("North");}
					else if (oneMarks.get(0) == 1){
						System.out.println("East");}
					else if (oneMarks.get(0) == 2){
						System.out.println("South");}
					else if (oneMarks.get(0) == 3){
						System.out.println("West");
					}
					oneMarks.remove(0);
					if(robot.distanceToObstacle(Direction.FORWARD) >= 1){
						robot.move(1);
						if (robot.isAtGoal()){
							return true;
						}
						if (contains(secondList,robot.getCurrentPosition()) != -1){
							robot.rotate(Turn.AROUND);
							robot.move(1);
							if (robot.isAtGoal()){
								return true;
							}
							if (robot.distanceToObstacle(Direction.FORWARD) >= 1){
								robot.move(1);
								if (robot.isAtGoal()){
									return true;
								}
								if (contains(firstList,robot.getCurrentPosition()) != -1){
									firstList.remove(contains(firstList,robot.getCurrentPosition()));
									secondList.add(robot.getCurrentPosition());
								}
								else{
									if (contains(secondList,robot.getCurrentPosition()) != -1){
										continue;
									}
									firstList.add(robot.getCurrentPosition());
								}
							}
						}
						}
					
					
					if (robot.isAtGoal()){
						return true;
					}
					
						
						firstList.add(robot.getCurrentPosition());
					}
					firstList.remove(robot.getCurrentPosition());
					secondList.add(robot.getCurrentPosition());
					
				}
				else{
					face(zeroMarks.get(0));
					if (zeroMarks.get(0) == 0){
						System.out.println("North");}
					else if (zeroMarks.get(0) == 1){
						System.out.println("East");}
					else if (zeroMarks.get(0) == 2){
						System.out.println("South");}
					else if (zeroMarks.get(0) == 3){
						System.out.println("West");
					}
					zeroMarks.remove(0);
					if(robot.distanceToObstacle(Direction.FORWARD) >= 1){
					robot.move(1);
					if (contains(secondList,robot.getCurrentPosition()) != -1){
						robot.rotate(Turn.AROUND);
						robot.move(1);
						if (robot.distanceToObstacle(Direction.FORWARD) >= 1){
							robot.move(1);
							if (contains(firstList,robot.getCurrentPosition()) != -1){
								firstList.remove(contains(firstList,robot.getCurrentPosition()));
								secondList.add(robot.getCurrentPosition());
							}
							else{
								if (contains(secondList,robot.getCurrentPosition()) != -1){
									continue;
								}
								firstList.add(robot.getCurrentPosition());
							}
						}
					}
					}
					if (robot.isAtGoal()){
						return true;
					}
					if (contains(firstList,robot.getCurrentPosition()) != -1){
						firstList.remove(contains(firstList,robot.getCurrentPosition()));
						secondList.add(robot.getCurrentPosition());
					}
					else{
						if (contains(secondList,robot.getCurrentPosition()) != -1){
							continue;
						}
						firstList.add(robot.getCurrentPosition());
					}
					firstList.add(robot.getCurrentPosition());
				}
			}
			if (robot.canSeeGoal(Direction.FORWARD)){
				while (true){
					robot.move(1);
					if (robot.isAtGoal()){
						return true;
					}
				}
			}
			if (robot.canSeeGoal(Direction.LEFT)){
				robot.rotate(Turn.LEFT);
				while (robot.canSeeGoal(Direction.FORWARD)){
					if (robot.isAtGoal()){
						return true;
					}
					if(robot.distanceToObstacle(Direction.FORWARD)>=1){
						robot.move(1);
					}
					else{
						continue;
					}
					robot.move(1);
					if (robot.isAtGoal()){
						return true;
					}
					
				}
			}
			if (robot.canSeeGoal(Direction.RIGHT)){
				robot.rotate(Turn.RIGHT);
				while (robot.canSeeGoal(Direction.FORWARD)){
					if (robot.isAtGoal()){
						return true;
					}
					robot.move(1);

					if (robot.isAtGoal()){
						return true;
					}
				}
			}
			else if (robot.isAtJunction()){
				System.out.println("At Junction");
				junctionList.add(robot.getCurrentPosition());
				continue;
			}
			else if ((robot.distanceToObstacle(Direction.LEFT) >= 1)){
				System.out.println("Turning Left");
				System.out.println(robot.distanceToObstacle(Direction.LEFT));
	

				robot.rotate(Turn.LEFT);
				if(robot.distanceToObstacle(Direction.FORWARD) >= 1){
					robot.move(1);
				}
				if (robot.isAtJunction()){
					continue;
				}
				if (robot.isAtGoal()){
					return true;
				}
				

				if (contains(firstList,robot.getCurrentPosition()) != -1){
					firstList.remove(contains(firstList,robot.getCurrentPosition()));
					secondList.add(robot.getCurrentPosition());
				}
				else{
					if (contains(secondList,robot.getCurrentPosition()) != -1){
						continue;
					}
					firstList.add(robot.getCurrentPosition());
				}
			}
			else if (robot.distanceToObstacle(Direction.FORWARD) >= 1){
				System.out.println("Going Forward");
					robot.move(1);
				if (robot.isAtGoal()){
					return true;
				}
				if (robot.isAtJunction()){
					continue;
				}
				/*if (contains(secondList, robot.getCurrentPosition()) != -1){
					System.out.println("TurnAround");
					robot.rotate(Turn.AROUND);
					if(robot.distanceToObstacle(Direction.FORWARD) >= 1){
						robot.move(1);
						}
					if (robot.isAtGoal()){
						return true;
					}
					continue;
				}*/
				
				if (contains(firstList,robot.getCurrentPosition()) != -1){
					firstList.remove(contains(firstList,robot.getCurrentPosition()));
					secondList.add(robot.getCurrentPosition());
				}
				else{
					if (contains(secondList,robot.getCurrentPosition()) != -1){
						continue;
					}
					firstList.add(robot.getCurrentPosition());
				}
			}
			else if ((robot.distanceToObstacle(Direction.LEFT) < 1) & (robot.distanceToObstacle(Direction.FORWARD) < 1)) {
				System.out.println("Turning Right");
				robot.rotate(Turn.RIGHT);
				if (contains(firstList,robot.getCurrentPosition()) != -1){
					System.out.println("Add to first list");
					firstList.remove(contains(firstList,robot.getCurrentPosition()));
					secondList.add(robot.getCurrentPosition());
				}
				else{
					if (contains(secondList,robot.getCurrentPosition()) != -1){
						System.out.println("Add to second list");
						continue;
					}
					firstList.add(robot.getCurrentPosition());
				}
				
				continue;
			}
		}
	}
	
	
	public int contains(ArrayList<int[]> list, int[] item){
		for (int i = 0; i <list.size(); i++){
			if (list.get(i)[0] == item[0]){
				if (list.get(i)[1] == item[1]){
					return i;
				}
			}
		}
		return -1;
		
		
	}
	
	public void face(int direction) throws Exception{
		
		//Directions 0 1 2 3 == north east south west
		
		int[] parray = robot.getCurrentPosition();
		int[] darray = robot.getCurrentDirection();
		
		if (direction == 1){
			
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
		else if (direction == 3 ){
	
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
		else if (direction == 0){
			
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
	}
	public void setMaze(Maze maze){
		robot.setMaze(maze);
	}
}
