package GameState;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import Entity.ItemDrop;
import Entity.Player;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;
import TileMap.TileMap.Element;

public class Stage1State extends GameState
{
	private TileMap tileMap;
	private Background background;

	private Player player;
	
	// List of item drops on the map
	private List<ItemDrop> itemDrops;

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

		player = new Player(tileMap);
		player.setPosition(100, 100);
		itemDrops = new ArrayList<ItemDrop>();
		
		itemDrops.add(new ItemDrop(tileMap));
		itemDrops.get(0).setPosition(220, 175);
		itemDrops.get(0).setElementType(Element.FIRE); // place holder element for testing purposes
	}

	@Override
	public void update()
	{
		player.update();
		
		// For each item drop on the map
		for(ItemDrop itemDrop : itemDrops)
			itemDrop.update(player);
		
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
	}

	@Override
	public void draw(Graphics2D g)
	{
		// Draw background
		background.draw(g);
		// Draw the tile map
		tileMap.draw(g);
		// Draw item drops
		for(ItemDrop itemDrop : itemDrops)
			itemDrop.draw(g);
		// Draw player
		player.draw(g);
	}

	@SuppressWarnings("restriction")
	@Override
	public void keyPressed(int k)
	{
		if (k == KeyEvent.VK_LEFT)
			player.setIsMovingLeft(true);
		if (k == KeyEvent.VK_RIGHT)
			player.setIsMovingRight(true);
		if (k == KeyEvent.VK_UP)
			player.setUpElement(itemDrops);
		if (k == KeyEvent.VK_DOWN)
			player.setDownElement(itemDrops);
		if (k == KeyEvent.VK_SHIFT)
			player.setIsJumping(true);
		if (k == KeyEvent.VK_Z)
			player.setAttacking(true);

	}

	@SuppressWarnings("restriction")
	@Override
	public void keyReleased(int k)
	{
		if (k == KeyEvent.VK_LEFT)
			player.setIsMovingLeft(false);
		if (k == KeyEvent.VK_RIGHT)
			player.setIsMovingRight(false);
		if (k == KeyEvent.VK_UP)
			player.setIsMovingUp(false);
		if (k == KeyEvent.VK_DOWN)
			player.setIsMovingDown(false);
		if (k == KeyEvent.VK_SHIFT)
			player.setIsJumping(false);
		if (k == KeyEvent.VK_Z)
			player.setAttacking(false);
	}

}
