package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import TileMap.TileMap;
import TileMap.TileMap.Element;

public class ItemDrop extends MapObject
{
	private BufferedImage image;
	private Element elementType;
	
	public ItemDrop(TileMap tm)
	{
		super(tm);
		
		// Each item drop is none by default, need to set the item drop element later.
		elementType = Element.NONE;
		
		// Set the default hitbox for our item drop
		hitboxWidth = 38;
		hitboxHeight = 24;
		
		try 
		{
			image = ImageIO.read(getClass().getResourceAsStream("/MCs/FireBallContainer.png"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void update(MapObject player)
	{
		if(intersects(player))
			System.out.println("hit");
	}
	
	public void draw(Graphics2D g)
	{
		// this is important because it updates our xmap and ymap values so we can draw relative to the level.
		setMapPosition();
		
		// this is incorrect as of now, need to figure out what values to update my x & y with relative to the PLAYER.
		x = xmap;
		y = ymap;
		
		// Draw item drop relative to the map
		g.drawImage(image, (int)xmap + 220, (int)ymap + 175, null);
	}
	
	public void setElementType(TileMap.Element ele)
	{
		elementType = ele;
	}
	
	public TileMap.Element getElementType()
	{
		return elementType;
	}
}
