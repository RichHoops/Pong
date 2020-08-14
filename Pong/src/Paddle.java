import java.awt.Color;

public class Paddle extends Sprite {

	private static final Color COLOUR_PADDLE = Color.WHITE;
	private static final int WIDTH_PADDLE = 10;
	private static final int HEIGHT_PADDLE = 100;
	private static final int DISTANCE_FROM_EDGE = 40;
	
	public Paddle(int panelWidth, int panelHeight, Player player) {
		setColour(COLOUR_PADDLE);
		setWidth(WIDTH_PADDLE);
		setHeight(HEIGHT_PADDLE);
		int xPos;
		if (player == Player.One) {
			xPos = DISTANCE_FROM_EDGE;
		} else {
			xPos = panelWidth - DISTANCE_FROM_EDGE - getWidth();
		}
		setInitialPosition(xPos, panelHeight / 2 - (getHeight() / 2));
		resetToInitialPosition();
	}
}
