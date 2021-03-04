import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
	static final int ScreenWidth = 1300;
	static final int ScreenHeight = 750;
	static final int UnitSize = 50;
	static final int GameUnits = (ScreenHeight * ScreenWidth) / (UnitSize * UnitSize);// oyundaki kare sayisi
	static final int Delay = 175;// Oyun hizi
	final int x[] = new int[GameUnits];
	final int y[] = new int[GameUnits];
	int bodyParts = 6;// yilan baslangic boyu
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean life = false;
	Timer time;
	Random rand;
	int response;

	public GamePanel() {
		rand = new Random();
		this.setPreferredSize(new Dimension(ScreenWidth, ScreenHeight));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new myKey());
		startGame();
	}

	public void startGame() {
		Object[] options = { "Gökkuþaðý", "Yeþil" };
		response = JOptionPane.showOptionDialog(null, "Yilan Hangi Renk Olsun?", "Uyari",

				JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		newApple();
		life = true;
		time = new Timer(Delay, this);
		time.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		if (life) {
			g.setColor(Color.red);// elma
			g.fillOval(appleX, appleY, UnitSize, UnitSize);

			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.green);// yilan
					g.fillRect(x[i], y[i], UnitSize, UnitSize);
				} else {
					if (response == 0) {
						g.setColor(new Color(48, 185, 0));
						g.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));// renkli yilan
																										// icin
						g.fillRect(x[i], y[i], UnitSize, UnitSize);
					} else if (response == 1) {
						g.setColor(new Color(48, 185, 0));
						// g.setColor(new Color(rand.nextInt(255), rand.nextInt(255),
						// rand.nextInt(255)));// renkli yilan icin
						g.fillRect(x[i], y[i], UnitSize, UnitSize);
					}
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Skor: " + applesEaten, (ScreenWidth - metrics.stringWidth("Skor: " + applesEaten)) / 2,
					g.getFont().getSize());
		} else {
			gameOver(g);
		}
	}

	public void newApple() {
		appleX = rand.nextInt((int) (ScreenWidth / UnitSize)) * UnitSize;
		appleY = rand.nextInt((int) (ScreenHeight / UnitSize)) * UnitSize;
	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		switch (direction) {
		case 'U':
			y[0] = y[0] - UnitSize;
			break;
		case 'D':
			y[0] = y[0] + UnitSize;
			break;
		case 'L':
			x[0] = x[0] - UnitSize;
			break;
		case 'R':
			x[0] = x[0] + UnitSize;
			break;
		}
	}

	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}

	public void checkCollisions() {
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				life = false;
			}
		}

		if (x[0] < 0) {
			life = false;
		}
		if (x[0] > ScreenWidth) {
			life = false;
		}
		if (y[0] < 0) {
			life = false;
		}
		if (y[0] > ScreenHeight) {
			life = false;
		}

		if (!life) {
			time.stop();
		}

	}

	public void gameOver(Graphics g) {
		// Skor
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Skor: " + applesEaten, (ScreenWidth - metrics.stringWidth("Skor: " + applesEaten)) / 2,
				g.getFont().getSize());
		// GameOver

		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (ScreenWidth - metrics2.stringWidth("Game Over")) / 2, ScreenHeight / 2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (life) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	public class myKey extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}

	}

}
