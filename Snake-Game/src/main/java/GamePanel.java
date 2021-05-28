import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 3;
    int applesEaten;
    int highScore;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    boolean gameOvered = false;
    boolean pause = false;

    Timer timer = new Timer(DELAY, this);
    Random random;

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
    }

    public void startGame() {
        for (int i = 0; i < x.length; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        direction = 'R';
        applesEaten = 0;
        bodyParts = 3;
        gameOvered = false;
        running = true;

        newApple();
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running && !gameOvered) {
//            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//            }

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        } if (gameOvered) {
            gameOver(g);
        } if (!running && !gameOvered) {
            startGamePanel(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE) * UNIT_SIZE;

    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX && y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
            setHighScore();
        }
    }

    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                gameOvered = true;
                break;
            }
        }

        if ((x[0] >= SCREEN_WIDTH) || x[0] < 0) {
            running = false;
            gameOvered = true;
        }
        if (y[0] >= SCREEN_HEIGHT || y[0] < 0) {
            running = false;
            gameOvered = true;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void startGamePanel(Graphics g) {
        //show start text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 35));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Welcome to Sssnake!",
                (SCREEN_WIDTH - metrics.stringWidth("Welcome to Sssnake!")) / 2, (SCREEN_HEIGHT/ 3) - g.getFont().getSize());
        g.drawString("Enjoy!",
                (SCREEN_WIDTH - metrics.stringWidth("Enjoy!")) / 2, (SCREEN_HEIGHT / 3) + g.getFont().getSize());

        //show key configuration
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.ITALIC, 25));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Press \"Enter\" to start game",
                (SCREEN_WIDTH - metrics2.stringWidth("Press \"Enter\" to start game")) / 2, (SCREEN_HEIGHT/ 2) - g.getFont().getSize() / 2);
        g.drawString("Press \"Space\" to pause game",
                (SCREEN_WIDTH - metrics2.stringWidth("Press \"Space\" to pause game")) / 2, (SCREEN_HEIGHT / 2) + g.getFont().getSize() * 2);
    }

    public void gameOver(Graphics g) {
        //Show Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 35));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 3 - g.getFont().getSize());

        //show key configuration
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.ITALIC, 25));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Press \"Enter\" to start game",
                (SCREEN_WIDTH - metrics2.stringWidth("Press \"Enter\" to start game")) / 2, (SCREEN_HEIGHT/ 2) - g.getFont().getSize() / 2);
        g.drawString("Press \"Space\" to pause game",
                (SCREEN_WIDTH - metrics2.stringWidth("Press \"Space\" to pause game")) / 2, (SCREEN_HEIGHT / 2) + g.getFont().getSize() * 2);

        //Show your score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics3.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());

        //Show high score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics4 = getFontMetrics(g.getFont());
        g.drawString("High score: " + highScore,
                (SCREEN_WIDTH - metrics4.stringWidth("High score: " + highScore)) / 2, (SCREEN_HEIGHT - g.getFont().getSize()));
    }

    public void gamePause() {
        pause = true;
        timer.stop();
    }

    public void gameResume() {
        pause = false;
        timer.start();
    }

    public void setHighScore() {
        if (applesEaten > highScore) {
            highScore = applesEaten;
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
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
                case KeyEvent.VK_SPACE:
                    if (running) {
                        if (pause) {
                            gameResume();
                        } else {
                            gamePause();
                        }
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    if (!running) {
                        startGame();
                    }
                    break;
            }
        }
    }
}
