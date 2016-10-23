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
  private int jumpsRemaining;
	private boolean dead;
	private boolean flinching;
	
	// Fire ball
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	
	// Attack
	private boolean isAttacking;
	private int attackDmg;
	private int attackRange;
	
  // Timer
  // *NOTE* To make the player flinch, call flinchStartTime = System.nanoTime(); in a conditional statement, This will kick off a 1.25 second flinch
  private long flinchStartTime;
  private long flinchElapsed;
  
	// Animations
	private ArrayList<BufferedImage[]> sprites;
	// how many frames each state has, i.e. walking has 8 frames, so map it to that final int below
	private final int[] numFrames = 
  {
    2,8,1,2,4,2,5
  };
	
	// Animation Actions
	private static final int IDLE = 0 ;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int ATTACKING = 5;
	
  // Bounds
  private static final int BOTTOM_OF_MAP = 350;
  
	public Player(TileMap tm)
	{
		super(tm);
		spriteSheetWidth = 30;
		spriteSheetHeight = 30;
		hitboxWidth = 20;
		hitboxHeight = 20;
    jumpsRemaining = 2;
    
    flinchStartTime = -1;
    flinchElapsed = 0;
    
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
		
		attackDmg = 8;
		attackRange = 40;// pixles
		
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
					// animations of 30px each, iscounter corresponds with the static final Animation Actions declared above
          bi[iscounter] = spritesheet.getSubimage(iscounter * spriteSheetWidth, sscounter * spriteSheetHeight, spriteSheetWidth, spriteSheetHeight);
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
	public void setAttacking(boolean b){isAttacking = b;}
	
	private void getNextPosition()
	{

    if(isMovingLeft && isMovingRight)
    { // If both left and right buttons are held down, idle
      if(dx > 0)
			{
				dx -= declerationSpeed;
				if(dx < 0)
					dx = 0;
			}
			else if(dx < 0)
			{
				dx += declerationSpeed;
				if(dx > 0)
					dx = 0;
			}
    }
    else if(isMovingLeft)
		{ // If only left is pressed, move in the negitive direction
			dx -= moveSpeed;
			if(dx < -maxMoveSpeed )
				dx = -maxMoveSpeed;
		}
		else if(isMovingRight)
		{ // If only right is pressed, move in the positive direction
			dx += moveSpeed;
			if(dx > maxMoveSpeed )
				dx = maxMoveSpeed;
		}
		else
		{ // we are not holding any keys, idle
			if(dx > 0)
			{
				dx -= declerationSpeed;
				if(dx < 0)
					dx = 0;
			}
			else if(dx < 0)
			{
				dx += declerationSpeed;
				if(dx > 0)
					dx = 0;
			}
		}
		
    // Jumping Logic
		if(isJumping && !isFalling && jumpsRemaining == 2)
		{
			dy = jumpStart * 1.5; // initial jump is short because of double jump logic, so increase it.
			isFalling = true;
      --jumpsRemaining;
      isJumping = false;
		}
		// Falling Logic
    else if(isFalling)
		{ // Decrement by fall speed

			// Checks for second jump     
      if(isJumping && jumpsRemaining != 0)
      {
        dy = jumpStart;
        --jumpsRemaining; 
        return;
      }
  
      // gravity applied
			dy += gravityFallSpeed;			
      
      if(dy > 0) 
				isJumping = false;
			if(dy < 0 && !isJumping) 
				dy += stopJumpSpeed;
			
			if(dy > maxFallSpeed)
				dy = maxFallSpeed;        
		}
    
    // If we have landed, reset our jump counter
    if(!isJumping && !isFalling)
      jumpsRemaining = 2;
          
    // The player has fallen into a hole
    if(y > BOTTOM_OF_MAP)
    {
      health -= 50;
      flinchStartTime = System.nanoTime();
      setPosition(100, 100); 
    }
    
    // This will constantly check if the player has begun to flinch, denoted by flinchStartTime = System.nanoTime();
    HandleFlinching();    
	}
	
	public void update()
	{
		// Update the players position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// Basic State Checks
		if(isAttacking)
		{
			if(currentAction != ATTACKING)
			{
        HandleFacingDirection();
				currentAction = ATTACKING;
				animation.setFrames(sprites.get(ATTACKING));
				animation.setDelay(50);
				spriteSheetWidth = 60;
			}
		}
		else if(dy > 0) // We're falling
		{
      HandleFacingDirection();
      
			if(currentAction != FALLING)
			{
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				spriteSheetWidth = 30;
			}
		}
		else if(dy < 0) // We're jumping
		{
      HandleFacingDirection();
      
			if(currentAction != JUMPING)
			{
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1); // set to -1 since there is only 1 frame for jumping
				spriteSheetWidth = 30;
			}
		}
    else if (isMovingLeft && isMovingRight)
    {
      if(currentAction != IDLE)
			{
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				spriteSheetWidth = 30;
			}
    }
		else if (isMovingLeft || isMovingRight)
		{
      HandleFacingDirection();
      
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
		setMapPosition();
		// After you are hit, you are flinching, which looks like blinking
		if(flinching)
		{
			long elapsed = System.nanoTime() / 1000000;
			if(elapsed / 100 % 2 == 0)
				return;
		}
		if(isFacingRight)
		{
			g.drawImage(animation.getImage(), 
					(int)(x + xmap - spriteSheetWidth/2), 
					(int)(y + ymap - spriteSheetHeight/2),
					null);
		}
		else
		{
			// Draws the sprite flipped to the left
			g.drawImage(animation.getImage(), 
					(int)(x + xmap - spriteSheetWidth/2 + spriteSheetWidth), 
					(int)(y + ymap - spriteSheetHeight/2),
					-spriteSheetWidth,
					spriteSheetHeight,
					null);
		}
	}
  
  private void HandleFacingDirection()
  {
    if(isMovingLeft)
      isFacingRight = false;
    if(isMovingRight)
      isFacingRight = true;
  }
  
  private void HandleFlinching()
  {
    // Begin flinch timer logic
    if(flinchStartTime != -1)
    { // If the flinch timer has started 
      flinching = true;
      flinchElapsed = (System.nanoTime() - flinchStartTime) / 1000000;
    }
    
    if(flinchElapsed >= 1250)
    { // If the player has flinched for 1.25 seconds, stop flinching
      flinching = false;
      flinchStartTime = -1;
      flinchElapsed = 0;
    }  
  }
}
