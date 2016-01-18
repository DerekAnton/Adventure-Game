package GameState;

import java.awt.Color;
import java.awt.Graphics2D;

import TileMap.*;
import Main.GamePanel;

public class Stage1State extends GameState
{
	private TileMap tileMap;
	private Background background;
	
	public Stage1State(GameStateManager gsmanager)
	{
		this.gsmanager = gsmanager;
		init();
	}
	
	@Override
	public void init() 
	{
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tileset/maplesheet.png");
		tileMap.loadMap("/MapFiles/level1-1.map");
		tileMap.setPosition(0, 0);
		background = new Background("/Backgrounds/grassbg1.gif", 0.1);
	}

	@Override
	public void update() 
	{
		
	}

	@Override
	public void draw(Graphics2D g) 
	{
		// Draw background
		background.draw(g);
		// Draw the tile map
		tileMap.draw(g);
	}

	@Override
	public void keyPressed(int k) 
	{
		
	}

	@Override
	public void keyReleased(int k) 
	{
		
	}
	
}
