package flappyBird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {
	public static FlappyBird flappyBird;
	public Renderer renderer;
	public final int WIDTH = 900, HEIGHT= 600;

	public Rectangle bird;
	
	public int ticks;
	
	public ArrayList<Rectangle> pipes;
	
	public Random randomom;
	public int vertical, score;
	
	public boolean gameOver, gameStarted;
	
	public FlappyBird() {
		JFrame jFrame = new JFrame();
		Timer timer =  new Timer(20, this);
		
		renderer = new Renderer();
		randomom = new Random();
		jFrame.setTitle("Flappy Bird");
		jFrame.add(renderer);
		
		jFrame.addMouseListener(this);
		jFrame.addKeyListener(this);
		
		jFrame.setSize(WIDTH, HEIGHT);
		jFrame.setResizable(false);
		jFrame.setVisible(true);
		
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
		pipes = new ArrayList<Rectangle>();
		
		addPipe(true);
		addPipe(true);
		addPipe(true);
		addPipe(true);
		
		timer.start();
	}
	
	public void jump() {
		if(gameOver) {
			
			bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
			pipes.clear();
			vertical = 0;
			score = 0;
			
			addPipe(true);
			addPipe(true);
			
			gameOver = false;	
		}
		
		if(!gameStarted) {
			gameStarted = true;
			
		} else if(!gameOver) {
			if(vertical > 0) {
				vertical = 0;
			}
			
			vertical = vertical - 10;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		int speed = 10;
		ticks++;
		
		if(gameStarted) {

			for(int i = 0; i < pipes.size(); i++) {
				Rectangle pipe = pipes.get(i);
				pipe.x = pipe.x - speed;
			}
			
			if(ticks % 2 == 0 && vertical < 15) {
				vertical = vertical + 2;	
			}
			
			for(int i = 0; i < pipes.size(); i++) {
				Rectangle pipe = pipes.get(i);
				
				if(pipe.x + pipe.width < 0) {
					pipes.remove(pipe);
					
					if(pipe.y == 0) {
						addPipe(false);
					}
				}
			}
			
			bird.y = bird.y + vertical;
			
			for(Rectangle pipe : pipes) {
				
				if(pipe.y==0 && bird.x + bird.width /2 > pipe.x + pipe.width / 2 - 10 && bird.x + bird.width / 2 < pipe.x + pipe.width /2 + 10) {
					score++;
				}
				
				if (pipe.intersects(bird)) {
					gameOver = true;

					if (bird.x <= pipe.x) {
						bird.x = pipe.x - bird.width;

					} else if(pipe.y != 0){
						bird.y = pipe.y - bird.height;
						
					} else if (bird.y < pipe.height) {
						bird.y = pipe.height;
					}
				}
			}
			
			
			if(bird.y > HEIGHT - 100 || bird.y < 0) {
				gameOver = true;
			}
			
			if(bird.y + vertical >= HEIGHT - 100) {
				bird.y = HEIGHT - 100 - bird.height;
				gameOver = true;
			}
		}

		renderer.repaint();

	}
	
	public void addPipe(boolean start) {
		int space = 300;
		int widthOfPipesOfPipes = 100;
		int height = 5 + randomom.nextInt(250); //min height is 50; max height is 300
		
		if(start) {
			pipes.add(new Rectangle(WIDTH + widthOfPipesOfPipes + pipes.size()* 300, HEIGHT - height - 100, widthOfPipesOfPipes, height));
			pipes.add(new Rectangle(WIDTH + widthOfPipesOfPipes + (pipes.size()-1) * 300, 0, widthOfPipesOfPipes, HEIGHT - height - space));
		}else {
			pipes.add(new Rectangle(pipes.get(pipes.size()-1).x + 600, HEIGHT - height - 100, widthOfPipesOfPipes, height));
			pipes.add(new Rectangle(pipes.get(pipes.size()-1).x, 0, widthOfPipesOfPipes, HEIGHT - height - space));
		}
		
	}
	
	public void paintPipe(Graphics g, Rectangle pipe) { //paintComponent
		
		g.setColor(Color.green.darker());
		g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
	}
	
	
	public void repaint(Graphics g) {
		
		g.setColor(Color.cyan);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.orange.darker());
		g.fillRect(0, HEIGHT - 100, WIDTH, 100);
		
		g.setColor(Color.green);
		g.fillRect(0, HEIGHT - 100, WIDTH, 7);
		
		g.setColor(Color.yellow);
		g.fillRect(bird.x, bird.y, bird.width, bird.height);
		
		for(Rectangle pipe : pipes) {
			paintPipe(g, pipe);
		}
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 75));
		
		if(!gameStarted) {
			g.drawString("Click to Start", 175, HEIGHT / 2 - 50);
		}
		
		if(gameOver) {
			g.drawString("Game Over!", 175, HEIGHT / 2 - 50);
		}
		
		if(!gameOver && gameStarted) {
			g.drawString(String.valueOf(score), WIDTH/2 -25, 100);
		}
	}
	
	public static void main(String[] args) {
		flappyBird = new FlappyBird();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		jump();
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//jump();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			jump();
		}
		
		
	}



}
