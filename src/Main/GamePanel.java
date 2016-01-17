package Main;

import javax.swing.JPanel;

import GameState.GameStateManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

public class GamePanel extends JPanel implements Runnable,  KeyListener
{
	// Dimensions
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;
	
	// Game Parameters
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000/ FPS;
	
	// Image
	private BufferedImage image;
	private Graphics2D g;
	
	// Game State Manager
	private GameStateManager gsmanager;
	
	public GamePanel()
	{
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT*SCALE));
		setFocusable(true);
		requestFocus();
		
	}	
	
	private void init()
	{
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		running = true;
		gsmanager = new GameStateManager();
	}
	public void addNotify()
	{
		super.addNotify();
		if(thread == null)
		{
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}

	@Override
	public void run() 
	{
		init();
		
		// Main Game Loop
		long start, elapsed, wait;
		while(running)
		{
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			elapsed = System.nanoTime() - start;
			
			// Target time is in milliseconds, elapsed time is in nanoseconds, so we must divide by 1 million to keep them common
			wait = targetTime - elapsed / 1000000;
			
			try
			{
				Thread.sleep(Math.abs(wait));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	private void update()
	{
		gsmanager.update();
	}
	private void draw()
	{
		gsmanager.draw(g);
	}
	private void drawToScreen()
	{
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g2.dispose();
	}
	@Override
	public void keyPressed(KeyEvent key) 
	{
		gsmanager.keyPressed(key.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent key) 
	{
		gsmanager.keyReleased(key.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent key) 
	{
		
	}
}
