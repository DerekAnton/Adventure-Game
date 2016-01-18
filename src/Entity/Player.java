package Entity;
import TileMap.*;

import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;


public class Player extends MapObject
{
	private int health;
	private int maxHealth;
	private int fire;
	private int maxFire;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	
	// Fire ball
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	
	// Melee
	private boolean isMeleeAttacking;
	private int meleeAttackDmg;
	private int meleeRange;
	
	// Animations
	private ArrayList<BufferedImage[]> sprites;
	// how many frames each state has, i.e. walking has 8 frames, so map it to that final int below
	private final int[] numFrames = 
		{
			// TODO these values are from the tutorial, change later
			2,8,1,2,4,2,5
		};
	
	// Animation Actions
	private static final int IDLE = 0 ;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int MELEEATTACKING = 6;
	
	public Player(TileMap tm)
	{
		super(tm);
		spriteSheetWidth = 30;
		spriteSheetHeight = 30;
		hitboxWidth = 20;
		hitboxHeight = 20;
		
		moveSpeed = 0.3;
		maxMoveSpeed = 1.6;

		declerationSpeed = 0.4; //stopSpeed
		gravityFallSpeed = 0.15; //fallSpeed
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		isFacingRight = true;
		
		health = maxHealth = 5;
		fire = maxFire = 2500;
		
		fireBallDamage = 5;
		
		meleeAttackDmg = 8;
		meleeRange = 40;// pixles
		
		// Load Sprite sheet
		try
		{
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/MCs/playersprites.gif"));
			
			sprites = new ArrayList<BufferedImage[]>();
			// Iterate through each sprite set (walking, idle, attacking etc)
			// sscounter stands for Sprite Set Counter
			for(int sscounter = 0; sscounter < 7; sscounter ++)
			{
				BufferedImage[] bi = new BufferedImage[numFrames[sscounter]];
				// Iterate through individual sprite sets to get each frame
				// iscounter stands for Individual Sprite counter
				for(int iscounter = 0; iscounter < numFrames[sscounter]; iscounter++)
				{
					// TODO hard coded here, since the tutorial sprite sheet normally has 
					// animations of 30px each, but the melee attack has 60px, hence the skip on 6
					if(sscounter != 6)
						bi[iscounter] = spritesheet.getSubimage(iscounter * spriteSheetWidth, sscounter * spriteSheetHeight, spriteSheetWidth, spriteSheetHeight);
					else
						bi[iscounter] = spritesheet.getSubimage(iscounter * spriteSheetWidth * 2, sscounter * spriteSheetHeight, spriteSheetWidth, spriteSheetHeight);
				}
				sprites.add(bi);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
	}
	
	public int getHealth(){return health;}
	public int getMaxHealth(){return maxHealth;}
	public int getFire(){return fire;}
	public int getMaxFire(){return maxFire;}
	
	public void setFiring(){firing = true;}
	public void setMeleeAttacking(boolean b){isMeleeAttacking = b;}
	
	private void getNextPosition()
	{
		// Left/Right Movement Logic
		if(isMovingLeft)
		{
			dx -= moveSpeed;
			if(dx < -maxMoveSpeed )
				dx = -maxMoveSpeed;
		}
		else if(isMovingRight)
		{
			dx += moveSpeed;
			if(dx > maxMoveSpeed )
				dx = maxMoveSpeed;
		}
		else
		{
			if(dx > 0)
			{
				dx -= declerationSpeed;
				if(dx < 0)
					dx = 0;
			}
			else
			{
				dx += declerationSpeed;
				if(dx > 0)
					dx = 0;
			}
		}
		
		// Logic to stop the player from attacking while moving on the ground
		// they can attack while airbourn however.
		if(currentAction == MELEEATTACKING && !(isJumping || isFalling))
			dx = 0;
		// Jumping Logic
		if(isJumping && !isFalling)
		{
			dy = jumpStart;
			isFalling = true;
		}
		// Falling Logic
		if(isFalling)
		{
			// Apply gravity
			if(dy > 0)
			{
				isJumping = false;
				dy += gravityFallSpeed;
			}
			if(dy < 0 && !isJumping)
				dy += stopJumpSpeed;
			if(dy > maxFallSpeed)
				dy = maxFallSpeed;
		}
	}
	
	public void update()
	{
		// Update the players position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// Basic State Checks
		
		if(isMeleeAttacking)
		{
			if(currentAction != MELEEATTACKING)
			{
				currentAction = MELEEATTACKING;
				animation.setFrames(sprites.get(MELEEATTACKING));
				animation.setDelay(50);
				spriteSheetWidth = 60;
			}
		}
		else if(dy > 0)
		{
			if(currentAction != FALLING)
			{
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				spriteSheetWidth = 30;
			}
		}
		else if(dy < 0)
		{
			if(currentAction != JUMPING)
			{
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1); // set to -1 since there is only 1 frame for jumping
				spriteSheetWidth = 30;
			}
		}
		else if (isMovingLeft || isMovingRight)
		{
			if(currentAction != WALKING)
			{
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				spriteSheetWidth = 30;
			}
		}
		else // Player is Idle
		{
			if(currentAction != IDLE)
			{
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				spriteSheetWidth = 30;
			}
		}
		animation.update();
	}
	
	public void draw(Graphics2D g)
	{
		// After you are hit, you are flinching, which looks like blinking
		if(flinching)
		{
			long elapsed = (System.nanoTime() - flinchTimer)/ 1000000;
			if(elapsed / 100 % 2 == 0)
				return;
		}
		if(isFacingRight)
		{
			g.drawImage(animation.getImage(), 
					(int)(x+xmap - spriteSheetWidth/2), 
					(int)(y + ymap - spriteSheetHeight/2),
					null);
		}
		else
		{
			// Draws the sprite flipped to the left
			g.drawImage(animation.getImage(), 
					(int)(x+xmap - spriteSheetWidth/2 + spriteSheetWidth), 
					(int)(y + ymap - spriteSheetHeight/2),
					null);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
