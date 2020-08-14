import java.awt.Color;

public class Ball extends Sprite {

	private static final Color COLOUR_BALL = Color.WHITE;
	private static final int WIDTH_BALL = 25;
	private static final int HEIGHT_BALL = 25;
	
	public Ball(int panelWidth, int panelHeight) {
		setColour(COLOUR_BALL);
		setWidth(WIDTH_BALL);
		setHeight(HEIGHT_BALL);
		setInitialPosition(panelWidth / 2 - (getWidth() / 2), panelHeight / 2 - (getHeight() / 2));
		resetToInitialPosition();
	}
}
