package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
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
		hitboxWidth = 18;
		hitboxHeight = 24;
		
		try 
		{
			// right now a placeholder image is used, will need a generic background, and then load the element top on top of it
			image = ImageIO.read(getClass().getResourceAsStream("/MCs/FireBallContainer.png"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void update(MapObject player)
	{	
		// Create hitbox rects. r1 is this' hitbox. 
		Rectangle r1 = new Rectangle((int)x,(int)y,hitboxWidth, hitboxHeight);
		Rectangle r2 = player.getRectangle();
		
		if(r1.intersects(r2))
			System.out.println("hit");
	}
	
	public void draw(Graphics2D g)
	{
		// this is important because it updates our xmap and ymap values so we can draw relative to the level.
		setMapPosition();
				
		// Draw item drop relative to the map
		g.drawImage(image, (int)(xmap + x), (int)(ymap + y), null);
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
