package Entity;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;
import TileMap.TileMap.Element;

public class PlayerUI 
{
	// UI
	private Element currentElementUp;
	private Element currentElementDown;
	private BufferedImage interimSheet;
	private BufferedImage[] UiSprites = new BufferedImage[4];
	private BufferedImage[] UiElements = new BufferedImage[4];
	private String[][] SpellNames;
	
	public PlayerUI()
	{
		SpellNames = new String[4][4];
		initSpellNames();
		
		currentElementUp = Element.NONE;
		currentElementDown = Element.NONE;
		
		// Load UI
		BufferedImage interimElementContainer = null;
		BufferedImage interimElements = null;
		interimSheet = null;
		try 
		{
			interimSheet = ImageIO.read(getClass().getResourceAsStream("/MCs/HealthBar.png"));
			interimElementContainer = ImageIO.read(getClass().getResourceAsStream("/MCs/ElementContainer.png"));
			interimElements = ImageIO.read(getClass().getResourceAsStream("/MCs/Elements.png"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		// Metal Health Bar
		UiSprites[0] = interimSheet.getSubimage(0, 1, 157, 23);	
		
		// Inner Health Bar
		UiSprites[1] = interimSheet.getSubimage(156, 10, 152, 13);
		
		// Element Container
		UiSprites[2] = interimElementContainer.getSubimage(0, 1, 26, 23);
		
		// Load Elements
		UiElements[0] = interimElements.getSubimage(1, 1, 1, 1); // Blank 
		UiElements[1] = interimElements.getSubimage(6, 6, 22, 13); // Fire
		UiElements[2] = interimElements.getSubimage(30, 7, 16, 9); // Earth
		UiElements[3] = interimElements.getSubimage(52, 7, 13, 10); // Wind
	}
	
	public void draw(Graphics2D g)
	{	
		// Health Bar
		g.drawImage(UiSprites[0], 0, 0, null);
		g.drawImage(UiSprites[1], 1, 5, null);
		
		// Element Containers
		g.drawImage(UiSprites[2], GamePanel.WIDTH - 26, 0, null);
		g.drawImage(UiSprites[2], GamePanel.WIDTH - 26, 25, null);
		
		// Draw up and down elements if they are not NONE
		if(currentElementUp.val != Element.NONE.val)
			g.drawImage(UiElements[currentElementUp.val], GamePanel.WIDTH - 26 + 5, 5, null);
		
		if(currentElementDown.val != Element.NONE.val)
			g.drawImage(UiElements[currentElementDown.val], GamePanel.WIDTH - 26 + 5, 25 + 5, null);
		
		// Draw spell combination names 
		g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		g.setColor(Color.BLACK);
		g.drawString(SpellNames[currentElementUp.val][currentElementDown.val],  GamePanel.WIDTH - 90, 25);
	}
	
	public void setUpElement(Element element)
	{
		currentElementUp = element;
	}
	
	public Element getUpElement()
	{
		return currentElementUp;
	}
	
	public void setDownElement(Element element)
	{
		currentElementDown = element;
	}
	
	public Element getDownElement()
	{
		return currentElementDown;
	}
	
	public void setPlayersHealth(double playersHealth)
	{
		if(playersHealth > 0)
		{// Takes in the current amount of health the player has and draws that exact amount as a bar
			UiSprites[1] = interimSheet.getSubimage(156, 10, (int)Math.round( 148 * (playersHealth/100)), 13);
		}
		else
		{// If the hero has 0 health, draw the smallest amount of life possible. They have died.
			UiSprites[1] = interimSheet.getSubimage(156, 10, 1, 13);
		}
	}
	
	private void initSpellNames()
	{
		SpellNames[Element.NONE.val][Element.NONE.val] = "";
		
		SpellNames[Element.NONE.val][Element.FIRE.val] = "Firebolt";
		SpellNames[Element.FIRE.val][Element.NONE.val] = "Firebolt";
		
		SpellNames[Element.NONE.val][Element.EARTH.val] = "Earth Spike";
		SpellNames[Element.EARTH.val][Element.NONE.val] = "Earth Spike";
		
		SpellNames[Element.NONE.val][Element.WIND.val] = "Gust";
		SpellNames[Element.WIND.val][Element.NONE.val] = "Gust";
		
		SpellNames[Element.FIRE.val][Element.FIRE.val] = "Fireball";
		
		
		SpellNames[Element.FIRE.val][Element.EARTH.val] = "Lava Orb";
		SpellNames[Element.EARTH.val][Element.FIRE.val] = "Lava Orb";
		
		SpellNames[Element.FIRE.val][Element.WIND.val] = "Smoke Vortex";
		SpellNames[Element.WIND.val][Element.FIRE.val] = "Smoke Vortex";
		
		SpellNames[Element.EARTH.val][Element.EARTH.val] = "Terra Spikes";
		
		SpellNames[Element.EARTH.val][Element.WIND.val] = "Stone Shards";
		SpellNames[Element.WIND.val][Element.EARTH.val] = "Stone Shards";
		
		SpellNames[Element.WIND.val][Element.WIND.val] = "Vortex";
	}

}
