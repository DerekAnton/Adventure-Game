package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import TileMap.Background;

public class MenuState extends GameState
{
	private Background background;
	private int currentChoice = 0;
	private String[] options = { "Start", "Help", "Quit" };
	private Color titleColor;
	private Font titleFont;

	private Font font;

	public MenuState(GameStateManager gsmanager)
	{
		this.gsmanager = gsmanager;
		try
		{
			// Menu screen background
			background = new Background("/Backgrounds/menubg.gif", 1);
			// Moving to the left at .1 pixels
			background.setVector(-0.1, 0);

			titleColor = new Color(128, 0, 0);
			titleFont = new Font("Century Gothic", Font.PLAIN, 28);

			font = new Font("Century Gothic", Font.PLAIN, 12);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void init()
	{
	}

	@Override
	public void update()
	{
		background.update();
	}

	@Override
	public void draw(Graphics2D g)
	{
		// Draw the background
		background.draw(g);

		// Draw the title
		g.setColor(titleColor);
		g.setFont(titleFont);
		// TODO change magic numbers to the center of the screen
		g.drawString("Danton's Adventure", 30, 70);

		// Draw the menu options
		g.setFont(font);
		for (int counter = 0; counter < options.length; counter++)
		{
			if (counter == currentChoice)
				g.setColor(Color.BLACK);
			else
				g.setColor(Color.RED);

			g.drawString(options[counter], 145, 140 + counter * 15);
		}
	}

	private void select()
	{
		// Start was selected
		if (0 == currentChoice)
		{
			gsmanager.setState(GameStateManager.STAGE1STATE);
		}
		// Help was selected
		else if (1 == currentChoice)
		{
		}
		// Quit was selected
		else if (2 == currentChoice)
			System.exit(0);
	}

	@SuppressWarnings("restriction")
	@Override
	public void keyPressed(int k)
	{
		if (KeyEvent.VK_ENTER == k)
			select();
		else if (KeyEvent.VK_UP == k)
		{
			currentChoice--;
			// If we've gone up too far, wrap to the bottom choice
			if (currentChoice <= -1)
				currentChoice = 2;
		} else if (KeyEvent.VK_DOWN == k)
		{
			currentChoice++;
			// If we've gone down too far, wrap to the top choice
			if (currentChoice >= options.length)
				currentChoice = 0;
		}

	}

	@Override
	public void keyReleased(int k)
	{
		// Blank
	}
}
