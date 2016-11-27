package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Spell extends MapObject
{
	public enum SpellType
	{
		NONE(0),
		FIREBOLT(1),
		EARTHSPIKE(2),
		GUST(3),
		FIREBALL(4),
		LAVAORB(5),
		SMOKEVORTEX(6),
		TERRASPIKES(7),
		STONESHARDS(8),
		VORTEX(9);
		
		public int val;
		
		SpellType(int i)
		{
			val = i;
		}
	};
	
	private int damage;	
	private Animation animation;
	
	public Spell(TileMap tm, SpellType spelltype)
	{
		super(tm);
		String loadPath = "";
		animation = new Animation();
		
		// load specific parameters for each spell
		switch(spelltype)
		{
			case NONE:
				break;
			case FIREBOLT:
				loadPath = "/MCs/Spells/Firebolt.png";
				damage = 10;
				spriteSheetWidth = 30;
				spriteSheetHeight = 25;
				animation.setTotalFrames(4);
				break;
			// TODO implement spells as i add them here 
			case EARTHSPIKE:
			case GUST:
			case FIREBALL:
			case LAVAORB:
			case SMOKEVORTEX:
			case TERRASPIKES:
			case STONESHARDS:
			case VORTEX:
			default:
				break;
		}
		
		try 
		{
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(loadPath));
			BufferedImage[] interimsheet = new BufferedImage[animation.getTotalFrames()];
			
			for(int counter = 0; counter < animation.getTotalFrames(); ++counter)
				interimsheet[counter] = spritesheet.getSubimage(counter * spriteSheetWidth, 0, spriteSheetWidth, spriteSheetHeight);
			
			animation.setFrames(interimsheet);
			animation.setDelay(200);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void update()
	{
		animation.update();
	}
	
	public void draw(Graphics2D g)
	{
		// this is important because it updates our xmap and ymap values so we can draw relative to the level.
		setMapPosition();
				
		// Draw item drop relative to the map
		//g.drawImage(animation.getImage(), (int)(xmap + x), (int)(ymap + y), null);
		g.drawImage(animation.getImage(), 100, 100, null);
	}
	
	public void setDamage(int val)
	{
		damage = val;
	}
	
	public int getDamage()
	{
		return damage;
	}

}
