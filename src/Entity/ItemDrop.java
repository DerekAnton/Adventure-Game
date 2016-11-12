package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class ItemDrop extends MapObject
{
	private BufferedImage placeholder;
	
	public ItemDrop(TileMap tm)
	{
		super(tm);
		try 
		{
			placeholder = ImageIO.read(getClass().getResourceAsStream("/MCs/FireBallContainer.png"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g, int x, int y)
	{
		g.drawImage(placeholder, x, y, null);
	}
}
