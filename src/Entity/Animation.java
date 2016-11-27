package Entity;

import java.awt.image.BufferedImage;

public class Animation
{
	private BufferedImage[] frames;
	private int currentFrame;
	
	private int totalFrames;

	private long startTime;
	private long delay;

	private boolean hasPlayedOnce;

	public void Animation()
	{
		// Looping by default
		hasPlayedOnce = false;
	}

	public void setFrames(BufferedImage[] frames)
	{
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		hasPlayedOnce = false;
	}

	public void setDelay(long d)
	{
		delay = d;
	}

	public void setFrame(int i)
	{
		currentFrame = i;
	}

	// Determines the logic whether or not to move to the next frame
	public void update()
	{
		if (delay == -1)
			return;
		long elapsed = (System.nanoTime() - startTime) / 1000000;

		// Main sprite sheet reading loop
		if (elapsed > delay)
		{
			currentFrame++;
			startTime = System.nanoTime();
		}
		// We've gotten to the end of the sprite sheet one time
		if (currentFrame == frames.length)
		{
			hasPlayedOnce = true;
			currentFrame = 0;
		}
	}

	public int getFrame()
	{
		return currentFrame;
	}

	// Returns the image that needs to be drawn
	public BufferedImage getImage()
	{
		return frames[currentFrame];
	}

	public boolean hasPlayedOnce()
	{
		return hasPlayedOnce;
	}
	
	public void setTotalFrames(int newframes)
	{
		totalFrames = newframes;
	}
	
	public int getTotalFrames()
	{
		return totalFrames;
	}

}
