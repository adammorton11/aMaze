package com.example.amazebyalexkrupski;



import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.example.amazebyalexkrupski.MazeFileWriter;

/**
 * A segment is a continuous sequence of walls in the maze.
 * 
 * This code is refactored code from Maze.java by Paul Falstad, www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 */
public class Seg {
	public int x, y, dx, dy, dist;
	//public Color col;
	public boolean partition, seen;
	MazePanel mazepanel;

	/**
	 * Constructor
	 * @param psx
	 * @param psy
	 * @param pdx
	 * @param pdy
	 * @param cl
	 * @param cc
	 */
	Seg(int psx, int psy, int pdx, int pdy, int cl, int cc, Maze maze) {
		if (maze!= null){
			mazepanel = maze.panel;
			x = psx;
			y = psy;
			dx = pdx;
			dy = pdy;
			dist = cl;
			seen = false;
			cl /= 4;
			int add = (dx != 0) ? 1 : 0;
			int part1 = cl & 7;
			int part2 = ((cl >> 3) ^ cc) % 6;
			int val1 = ((part1 + 2 + add) * 70)/8 + 80;
			switch (part2) {
			case 0: mazepanel.col = mazepanel.MColor(val1, 20, 20); break;
			case 1: mazepanel.col = mazepanel.MColor(20, val1, 20); break;
			case 2: mazepanel.col = mazepanel.MColor(20, 20, val1); break;
			case 3: mazepanel.col = mazepanel.MColor(val1, val1, 20); break;
			case 4: mazepanel.col = mazepanel.MColor(20, val1, val1); break;
			case 5: mazepanel.col = mazepanel.MColor(val1, 20, val1); break;
			default: mazepanel.col = mazepanel.MColor(20, 20, 20); break;
		}
		}
			else{
				mazepanel = new MazePanel();
				x = psx;
				y = psy;
				dx = pdx;
				dy = pdy;
				dist = cl;
				seen = false;
				cl /= 4;
				int add = (dx != 0) ? 1 : 0;
				int part1 = cl & 7;
				int part2 = ((cl >> 3) ^ cc) % 6;
				int val1 = ((part1 + 2 + add) * 70)/8 + 80;
				switch (part2) {
				case 0: mazepanel.col = mazepanel.MColor(val1, 20, 20); break;
				case 1: mazepanel.col = mazepanel.MColor(20, val1, 20); break;
				case 2: mazepanel.col = mazepanel.MColor(20, 20, val1); break;
				case 3: mazepanel.col = mazepanel.MColor(val1, val1, 20); break;
				case 4: mazepanel.col = mazepanel.MColor(20, val1, val1); break;
				case 5: mazepanel.col = mazepanel.MColor(val1, 20, val1); break;
				default: mazepanel.col = mazepanel.MColor(20, 20, 20); break;
			}
		
		}
	}

	int getDir() {
		if (dx != 0)
			return (dx < 0) ? 1 : -1;
		return (dy < 0) ? 2 : -2;
	}
	
	void storeSeg(Document doc, Element mazeXML, int number, int i) {
		MazeFileWriter.appendChild(doc, mazeXML, "distSeg_" + number+ "_" + i, dist) ;
		MazeFileWriter.appendChild(doc, mazeXML, "dxSeg_" + number+ "_" + i, dx) ;
		MazeFileWriter.appendChild(doc, mazeXML, "dySeg_" + number+ "_" + i, dy) ;
		MazeFileWriter.appendChild(doc, mazeXML, "partitionSeg_" + number+ "_" + i, partition) ;
		MazeFileWriter.appendChild(doc, mazeXML, "seenSeg_" + number+ "_" + i, seen) ;
		MazeFileWriter.appendChild(doc, mazeXML, "xSeg_" + number+ "_" + i, x) ;
		MazeFileWriter.appendChild(doc, mazeXML, "ySeg_" + number+ "_" + i, y) ;
		MazeFileWriter.appendChild(doc, mazeXML, "colSeg_" + number+ "_" + i, mazepanel.col.getColor()) ;
	}
}
