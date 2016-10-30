package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class Background
{
	private BufferedImage image;

	// Absolute Position Coordinates
	private double x;
	private double y;

	// Vectors
	private double dx;
	private double dy;

	private double moveScale;

	public Background(String path, double ms)
	{
		try
		{
			// Attempt to read the image from the passed in string
			image = ImageIO.read(getClass().getResourceAsStream(path));
			moveScale = ms;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setPosition(double x, double y)
	{
		this.x = (x * moveScale) % GamePanel.WIDTH;
		this.y = (y * moveScale) % GamePanel.HEIGHT;
	}

	public void setVector(double dx, double dy)
	{
		this.dx = dx;
		this.dy = dy;
	}

	public void update()
	{
		x += dx;
		y += dy;
	}

	public void draw(Graphics2D g)
	{
		// Draws the background image at the current x,y coordinates
		g.drawImage(image, (int) x, (int) y, null);

		// If x is less than 0, we've scrolled the background too far
		// To compensate, we draw an extra background to patch up the hole
		if (x < 0)
		{
			g.drawImage(image, (int) x + GamePanel.WIDTH, (int) y, null);
		}
		if (x > 0)
		{
			g.drawImage(image, (int) x - GamePanel.WIDTH, (int) y, null);
		}
	}
}
