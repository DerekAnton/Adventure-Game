package GameState;

import java.awt.Color;
import java.awt.Graphics2D;

import TileMap.*;
import Main.GamePanel;

public class Stage1State extends GameState
{
	private TileMap tileMap;
	
	public Stage1State(GameStateManager gsmanager)
	{
		this.gsmanager = gsmanager;
		init();
	}
	
	@Override
	public void init() 
	{
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tileset/grasstileset.gif");
		tileMap.loadMap("/MapFiles/level1-1.map");
		tileMap.setPosition(0, 0);
	}

	@Override
	public void update() 
	{
		
	}

	@Override
	public void draw(Graphics2D g) 
	{
		// Clear the Screen
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		// Draw the tile map
		tileMap.draw(g);
	}

	@Override
	public void keyPressed(int k) 
	{
		
	}

	@Override
	public void inkeyReleased(int k) 
	{
		
	}
	
}
