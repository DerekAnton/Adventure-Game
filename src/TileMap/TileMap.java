package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class TileMap
{
	private double x;
	private double y;

	// for smooth scrolling
	private double tween;

	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;

	// Map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;

	// Tile set
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;

	// Drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	// UI
	private BufferedImage[] UiSprites = new BufferedImage[3];
	BufferedImage interimSheet;
	
	

	public TileMap(int tileSize)
	{
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		
		tween = 0.07;
		
		// Load UI
		interimSheet = null;
		try 
		{
			interimSheet = ImageIO.read(getClass().getResourceAsStream("/MCs/HealthBar.png"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}			
		// 199W x 28H, the metal health bar size
		UiSprites[0] = interimSheet.getSubimage(0, 1, 157, 23);	
		// 191W x 17H, the inner part of health bar. 200 is X-axis pixel, 13 is Y-Axis pixel offset.
		UiSprites[1] = interimSheet.getSubimage(156, 10, 148, 13);
		
	}

	// Read tile images from path
	public void loadTiles(String path)
	{
		try
		{
			tileset = ImageIO.read(getClass().getResourceAsStream(path));
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[2][numTilesAcross];

			BufferedImage subimage;
			for (int col = 0; col < numTilesAcross; col++)
			{
				subimage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
				tiles[0][col] = new Tile(subimage, Tile.NORMAL);
				subimage = tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
				tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// Read from a flat file that contains the desired layout for our tiles
	public void loadMap(String path)
	{
		try
		{
			InputStream in = getClass().getResourceAsStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;

			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;

			String delims = "\\s+";
			for (int row = 0; row < numRows; row++)
			{
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for (int col = 0; col < numCols; col++)
				{
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// Getters
	public int getTileSize()
	{
		return tileSize;
	}

	public int getx()
	{
		return (int) Math.round(x);
	}
	
	public int getxMin()
	{
		return (int) Math.round(xmin);
	}
	
	public int getxMax()
	{
		return (int) Math.round(xmax);
	}

	public int gety()
	{
		return (int) Math.round(y);
	}
	
	public int getyMax()
	{
		return (int) Math.round(ymax);
	}
	
	public int getyMin()
	{
		return (int) Math.round(ymin);
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public int getNumRows()
	{
		return numRows;
	}

	public int getNumCols()
	{
		return numCols;
	}

	public int getType(int row, int col)
	{
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}

	public void setPosition(double x, double y)
	{
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		fixBounds();

		colOffset = (int) -this.x / tileSize;
		rowOffset = (int) -this.y / tileSize;
	}

	private void fixBounds()
	{
		if (x < xmin)
			x = xmin;
		if (y < ymin)
			y = ymin;
		if (x > xmax)
			x = xmax;
		if (y > ymax)
			y = ymax;
	}

	// Read the sprite sheet and go tile by tile drawing them from the array
	public void draw(Graphics2D g)
	{
		for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++)
		{
			// Stop drawing if you're at the end
			if (row >= numRows)
				break;

			for (int col = colOffset; col < colOffset + numColsToDraw; col++)
			{
				if (col >= numCols)
					break;

				// note, this skips the first tile in the set, because of the tutorial
				// remove later
				if (map[row][col] == 0)
					continue;

				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;

				g.drawImage(tiles[r][c].getImage(), (int) x + col * tileSize, (int) y + row * tileSize, null);
			}
		}
		
		// Draw UI elements
		// Always draw the UI at 0,0 of the screen
		g.drawImage(UiSprites[0], 0, 0, null);
		g.drawImage(UiSprites[1], 4, 5, null);
	}
	
	public void setTween(int i)
	{
		tween = i;
	}
	
	public double getTween()
	{
		return tween;
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
}
