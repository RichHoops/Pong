import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Font;

public class PongPanel extends JPanel implements ActionListener, KeyListener {

	private static final Color BACKGROUND_COLOUR = Color.BLACK;
	private final static int TIMER_DELAY = 5;
	private static final int BALL_MOVEMENT_SPEED = 2;
	private static final int POINTS_TO_WIN = 3;
	private static final int X_POS_SCORE = 100, Y_POS_SCORE = 100, SCORE_FONT_SIZE = 50;
	private static final int X_POS_WIN = 200, Y_POS_WIN = 200, WIN_FONT_SIZE = 50;
	private static final String SCORE_FONT_FAMILY = "Serif";
	int player1Score = 0, player2Score = 0;
	Player gameWinner;
	Ball ball;
	Paddle paddle1, paddle2;
	GameState gameState = GameState.Initialising;
	
	private void createObjects() {
		ball = new Ball(getWidth(), getHeight());
		paddle1 = new Paddle(getWidth(), getHeight(), Player.One);
		paddle2 = new Paddle(getWidth(), getHeight(), Player.Two);
	}
	
	private void moveObject(Sprite sprite) {
		sprite.setXPosition(sprite.getXPosition() + sprite.getXVelocity(),getWidth());
		sprite.setYPosition(sprite.getYPosition() + sprite.getYVelocity(),getHeight());
	}
	
	private void checkWallBounce() {
		if (ball.getXPosition() <= 0 ) {
			ball.setXVelocity(-ball.getXVelocity());
			addScore(Player.Two);
			resetBall();
			
		} else if(ball.getXPosition() >= getWidth() - ball.getWidth()) {
			ball.setXVelocity(-ball.getXVelocity());
			addScore(Player.One);
			resetBall();
		}
		
		if (ball.getYPosition() <= 0 || ball.getYPosition() >= getHeight() - ball.getHeight()) {
			ball.setYVelocity(-ball.getYVelocity());
		}
	}
	
	private void checkPaddleBounce() {
		if(ball.getXVelocity() < 0 && ball.getRectangle().intersects(paddle1.getRectangle())) {
	          ball.setXVelocity(-ball.getXVelocity());
	      }
	      if(ball.getXVelocity() > 0 && ball.getRectangle().intersects(paddle2.getRectangle())) {
	          ball.setXVelocity(-ball.getXVelocity());
	      }
	}
	
	private void resetBall() {
		ball.resetToInitialPosition();
	}
	
	private void addScore(Player player) {
		if (player == Player.One) {
			player1Score++;
		} else {
			player2Score++;
		}
	}
	
	private void checkWin() {
		if (player1Score >= POINTS_TO_WIN) {
			gameWinner = Player.One;
			gameState = GameState.GameOver;
		} else if(player2Score >= POINTS_TO_WIN) {
			gameWinner = Player.Two;
			gameState = GameState.GameOver;
		}
	}
	
	private void update() {
		switch(gameState) {
			case Initialising: {
				createObjects();
				gameState = GameState.Playing;
				ball.setXVelocity(BALL_MOVEMENT_SPEED);
				ball.setYVelocity(BALL_MOVEMENT_SPEED);
				break;
			}
			case Playing:{
				moveObject(paddle1);
				moveObject(paddle2);
				moveObject(ball);
				checkWallBounce();
				checkPaddleBounce();
				checkWin();
				break;
			}
			case GameOver:{
				break;
			}
		}
	}
	
	public PongPanel() {
        setBackground(BACKGROUND_COLOUR);
        Timer timer = new Timer(TIMER_DELAY, this);
        timer.start();
        addKeyListener(this);
        setFocusable(true);
    }
	
	@Override
	public void keyTyped(KeyEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent event) {
		
		if(event.getKeyCode() == KeyEvent.VK_W) {
            paddle1.setYVelocity(-3);
       } else if(event.getKeyCode() == KeyEvent.VK_S) {
            paddle1.setYVelocity(3);
        }
		
		if(event.getKeyCode() == KeyEvent.VK_UP) {
            paddle2.setYVelocity(-3);
       } else if(event.getKeyCode() == KeyEvent.VK_DOWN) {
            paddle2.setYVelocity(3);
        }
		
	}

	@Override
	public void keyReleased(KeyEvent event) {
		
		if(event.getKeyCode() == KeyEvent.VK_UP || event.getKeyCode() == KeyEvent.VK_DOWN) {
            paddle2.setYVelocity(0);
        }
		
		if(event.getKeyCode() == KeyEvent.VK_W || event.getKeyCode() == KeyEvent.VK_S) {
            paddle1.setYVelocity(0);
        }
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		update();
		repaint();
		
	}
	
	private void paintScores(Graphics g) {
        Font scoreFont = new Font(SCORE_FONT_FAMILY, Font.BOLD, SCORE_FONT_SIZE);
        String leftScore = Integer.toString(player1Score);
        String rightScore = Integer.toString(player2Score);
        g.setFont(scoreFont);
        g.drawString(leftScore, X_POS_SCORE, Y_POS_SCORE);
       g.drawString(rightScore, getWidth()-X_POS_SCORE, Y_POS_SCORE);
   }
	
	private void paintWin(Graphics g) {
		Font winFont = new Font(SCORE_FONT_FAMILY, Font.BOLD, WIN_FONT_SIZE);
		String win = "WIN!";
		g.setFont(winFont);
		if (gameWinner == Player.One) {
			g.drawString(win, X_POS_WIN, Y_POS_WIN);
		} else if (gameWinner == Player.Two) {
			g.drawString(win, getWidth() - X_POS_WIN, Y_POS_WIN);
		}
	
	}
	
	@Override
	 public void paintComponent(Graphics g) {
	     super.paintComponent(g);
	     paintDottedLine(g);
	     if(gameState != GameState.Initialising) {
	    	 paintSprite(g, ball);
	    	 paintSprite(g, paddle1);
	    	 paintSprite(g, paddle2);
	    	 paintScores(g);
	     }
	     if(gameState == GameState.GameOver) {
	    	 paintWin(g);
	     }
	 }
	
	private void paintDottedLine(Graphics g) {
	      Graphics2D g2d = (Graphics2D) g.create();
	         Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
	         g2d.setStroke(dashed);
	         g2d.setPaint(Color.WHITE);
	         g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
	         g2d.dispose();
	 }
	
	private void paintSprite(Graphics g, Sprite sprite) {
	      g.setColor(sprite.getColour());
	      g.fillRect(sprite.getXPosition(), sprite.getYPosition(), sprite.getWidth(), sprite.getHeight());
	}
	
	

}
