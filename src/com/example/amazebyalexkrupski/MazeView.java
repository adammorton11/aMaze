package com.example.amazebyalexkrupski;

import com.example.amazebyalexkrupski.Constants;
import com.example.amazebyalexkrupski.MazePanel;


public class MazeView extends DefaultViewer {

	Maze maze ; // need to know the maze model to check the state 
	// and to provide progress information in the generating state
	
	public MazeView(Maze m) {
		super() ;
		maze = m;
	}

	@Override
	public void redraw(MazePanel mp, int state, int px, int py, int view_dx,
			int view_dy, int walk_step, int view_offset, RangeSet rset, int ang) {
		//dbg("redraw") ;
		switch (state) {
		case Constants.STATE_TITLE:
			redrawTitle(mp);
			break;
		case Constants.STATE_GENERATING:
			redrawGenerating(mp);
			break;
		case Constants.STATE_PLAY:
			// skip this one
			break;
		case Constants.STATE_FINISH:
			redrawFinish(mp);
			break;
		}
	}
	
	private void dbg(String str) {
		System.out.println("MazeView:" + str);
	}
	
	// 
	
	/**
	 * Helper method for redraw to draw the title screen, screen is hardcoded
	 * @param  mp MazePanel is the off screen image
	 */
	
	

	
	void redrawTitle(MazePanel mp) {
		
		
		/*mp.setColor(Color.white);
		mp.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		mp.setFont(largeBannerFont);
		FontMetrics fm = mp.getFontMetrics();
		mp.setColor(Color.red);
		centerString(mp, fm, "MAZE", 100);
		mp.setColor(Color.blue);
		mp.setFont(smallBannerFont);
		fm = mp.getFontMetrics();
		centerString(mp, fm, "by Paul Falstad", 160);
		centerString(mp, fm, "www.falstad.com", 190);
		mp.setColor(Color.black);
		centerString(mp, fm, "To start, select a skill level.", 250);
		centerString(mp, fm, "(Press a number from 0 to 9,", 300);
		centerString(mp, fm, "or a letter from A to F)", 320);
		centerString(mp, fm, "v1.2", 350);*/

	}
	/**
	 * Helper method for redraw to draw final screen, screen is hard coded
	 * @param mp MazePanel is the off screen image
	 */
	void redrawFinish(MazePanel mp) {
		/*mp.setColor(7);
		mp.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		mp.setFont(0);
		mp.setColor(5);
		centerString(mp, "You won!", 100);
		mp.setColor(6);
		mp.setFont(1);
		centerString(mp, "Congratulations!", 160);
		mp.setColor(2);
		centerString(mp, "Hit any key to restart", 300);*/
	}

	/**
	 * Helper method for redraw to draw screen during phase of maze generation, screen is hard coded
	 * only attribute percentdone is dynamic
	 * @param mp MazePanel is the off screen image
	 */
	void redrawGenerating(MazePanel mp) {
		/*mp.setColor(5);
		mp.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		mp.setFont(0);
		mp.setColor(4);
		centerString(mp, "Building maze", 150);
		mp.setFont(1);
		mp.setColor(0);
		centerString(mp, maze.getPercentDone()+"% completed", 200);
		centerString(mp, "Hit escape to stop", 300);
	
	*/
	}
	private void centerString(MazePanel mp, String str, int ypos) {
	//	mp.drawString(str, (Constants.VIEW_WIDTH-mp.getFontMetrics().stringWidth(str))/2, ypos);
	}


}
