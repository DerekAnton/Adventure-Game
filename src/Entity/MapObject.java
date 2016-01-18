package Entity;

import TileMap.Tile;
import TileMap.TileMap;
import java.awt.Rectangle;

import Main.GamePanel;

public abstract class MapObject 
{
	// Tiles
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	// Position and Vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	// Dimensions for reading in sprite sheet width and height
	protected int spriteSheetWidth;
	protected int spriteSheetHeight;
	
	// Hit boxes
	protected int hitboxWidth;
	protected int hitboxHeight;
	
	// Collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	// For collision, 4 point collision check
	protected boolean isTopLeft;
	protected boolean isTopRight;
	protected boolean isBottomLeft;
	protected boolean isBottomRight;
	
	// Animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean isFacingRight;
	
	// Movement
	protected boolean isMovingLeft;
	protected boolean isMovingRight;
	protected boolean isMovingUp;
	protected boolean isMovingDown;
	protected boolean isJumping;
	protected boolean isFalling;
	
	// Moving attributes
	protected double moveSpeed;
	protected double maxMoveSpeed;
	protected double declerationSpeed;
	protected double gravityFallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	
	public MapObject(TileMap tm)
	{
		tileMap = tm;
		tileSize = tm.getTileSize();
	}	
	
	// Fetches this' hit box
	public Rectangle getRectangle()
	{
		return new Rectangle((int)x - hitboxWidth , (int)y - hitboxHeight, hitboxWidth, hitboxHeight);
	}
	
	// Takes in another map object and checks to see if this' hit box intersects the passsed in params hitbox
	public boolean intersects(MapObject o)
	{
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	public void calculateCorners(double x, double y) 
	{
        int leftTile = (int)(x - hitboxWidth / 2) / tileSize;
        int rightTile = (int)(x + hitboxWidth / 2 - 1) / tileSize;
        int topTile = (int)(y - hitboxHeight / 2) / tileSize;
        int bottomTile = (int)(y + hitboxHeight / 2 - 1) / tileSize;
        if(topTile < 0 || bottomTile >= tileMap.getNumRows() || leftTile < 0 || rightTile >= tileMap.getNumCols()) 
        {
                isTopLeft = isTopRight = isBottomLeft = isBottomRight = false;
                return;
        }
        int tl = tileMap.getType(topTile, leftTile);
        int tr = tileMap.getType(topTile, rightTile);
        int bl = tileMap.getType(bottomTile, leftTile);
        int br = tileMap.getType(bottomTile, rightTile);
        isTopLeft = (tl == Tile.BLOCKED);
        isTopRight = (tr == Tile.BLOCKED);
        isBottomLeft = (bl == Tile.BLOCKED);
        isBottomRight = (br == Tile.BLOCKED);
}
	
	public void checkTileMapCollision()
	{
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		// Collision checking
		checkTopBottom(x, ydest);
		checkLeftRight( xdest,  y);
		
		// Check if we're falling
		if(!isFalling)
		{
			calculateCorners(x, ydest+1);
			// We're no longer colliding with the ground, so we begin to fall
			if(!isBottomLeft && !isBottomRight)
			{
				isFalling = true;
			}
		}
	}
	
	
	public void checkTopBottom(double x, double ydest)
	{
		// Calculate the Top and Bottom of hit box
		calculateCorners(x, ydest);
		
		// Check Top corners (jumping)
		if(dy < 0)
		{
			// If we are colliding with either top left or right
			if(isTopLeft || isTopRight)
			{
				// Undo the move and set our y velocity to 0
				dy = 0;
				ytemp = currRow * tileSize + hitboxHeight / 2;
			}
			else
			{
				// Keep going up
				ytemp += dy;
			}
		}
		// Check Bottom corners (falling)
		if(dy > 0)
		{
			// If we are colliding with either top bottom left or right
			if(isBottomLeft || isBottomRight)
			{
				// Undo the move and set our y velocity to 0
				dy = 0;
				isFalling = false;
				ytemp = (currRow+1) * tileSize - hitboxHeight / 2;
			}
			else
			{
				// Keep going down
				ytemp += dy;
			}
		}
	}
	
	public void checkLeftRight(double xdest, double y)
	{
		// Check left and right sides of the hit box
		calculateCorners(xdest, y);
		
		// Check Left corners
		if(dx < 0)
		{
			// If intersecting left
			if(isTopLeft || isBottomLeft)
			{
				// undo the last move and set our x velocity to 0
				dx = 0;
				xtemp = currCol * tileSize + hitboxWidth / 2;
			}
			else
			{
				xtemp += dx;
			}
		}
		if(dx > 0)
		{
			if(isTopRight || isBottomRight)
			{
				dx = 0;
				xtemp = (currCol +1) * tileSize - hitboxWidth / 2;
			}
			else
			{
				xtemp += dx;
			}
		}
	}
	public int getx() {return (int)x;}
	public int gety() {return (int)y;}
	public int getWidthOfSpriteSheet() {return (int)spriteSheetWidth;}
	public int getHeightOfSpriteSheet() {return (int)spriteSheetHeight;}
	public int gethitboxWidth() {return (int)hitboxWidth;}
	public int gethitboxHeight() {return (int)hitboxHeight;}
	
	
	public void setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setVector(double dx, double dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	
	// Every MapObject has 2 different positions
	// one being the global position (it's regular x and y)
	// the second being their local position (x & y position + tileMap position)
	public void setMapPosition()
	{
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	
	public void setIsMovingLeft(boolean b){isMovingLeft = b;}
	public void setIsMovingRight(boolean b){isMovingRight = b;}
	public void setIsMovingUp(boolean b){isMovingUp = b;}
	public void setIsMovingDown(boolean b){isMovingDown = b;}
	public void setIsJumping(boolean b){isJumping = b;}
	
	// Returns whether or not this map object is on the screen
	public boolean notOnScreen()
	{
		return x+xmap + spriteSheetWidth < 0 ||
			   y+ymap + spriteSheetHeight < 0 || 
			   x+xmap - spriteSheetWidth > GamePanel.WIDTH || 
			   y+ymap - spriteSheetHeight > GamePanel.HEIGHT;
	}	
}
