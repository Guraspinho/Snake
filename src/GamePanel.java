import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener
{
    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 700;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) / UNIT_SIZE;
    static final int DELAY = 75;

    final int x[] = new int[GAME_UNITS]; // hold all of the x coordinates of bodyparts of a snake
    final int y[] = new int[GAME_UNITS]; // hold all of the y coordinates of bodyparts of a snake

    int bodyparts = 6; // starting bodypart size

    int applesEaten; // track the amount of apples eaten

    int appleX; // X coordinate of an apple
    int appleY; // Y coordinate of an apple

    char direction = 'R'; // the starting direction of a snake
    boolean running = false;

    Timer timer;
    Random random;

    GamePanel()
    {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(0,51,25));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());



        startGame();
    }
    
    public void drawChessBoard(Graphics g)
    {
        // Colors for the chessboard
        Color lightColor = new Color(51, 225, 153);
        Color darkColor = new Color(0, 204, 102);

        // Draw the chessboard pattern
        for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            for (int j = 0; j < SCREEN_WIDTH / UNIT_SIZE; j++) {
                if ((i + j) % 2 == 0) {
                    g.setColor(lightColor);
                } else {
                    g.setColor(darkColor);
                }
                g.fillRect(j * UNIT_SIZE, i * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }
        }
    }
    // method to start a game
    public void startGame()
    {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        drawChessBoard(g);
        draw(g);
    }
    public void draw(Graphics g)
    {
        if(running)
        {
            // for(int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++  )
            // {
            //     g.setColor(Color.white);
            //     g.drawLine(i * UNIT_SIZE, 0 , i * UNIT_SIZE , SCREEN_HEIGHT);
            //     g.drawLine(0, i * UNIT_SIZE ,SCREEN_WIDTH, i * UNIT_SIZE);
            // }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
    
            for(int i = 0; i < bodyparts; i++)
            {
                if(i == 0)
                {
                    g.setColor( new Color(0, 128, 255));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else
                {
                    g.setColor(new Color(0,0,102));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor( new Color (0,0,102));
            g.setFont( new Font("Roboto", Font.BOLD, 25));
            g.drawString("Score: " + applesEaten, 5, g.getFont().getSize());
        }
        else
        {
            gameOver(g);
        }
    }

    public void newApple()
    {
        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    // a method to move around
    public void move()
    {
        for(int i = bodyparts; i>0; i--)
        {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction)
        {
            case 'U':

                y[0] = y[0] - UNIT_SIZE;
                break;

            case 'D':

                y[0] = y[0] + UNIT_SIZE;
                break;
            
            case 'L':
            
                x[0] = x[0] - UNIT_SIZE;
                break;  

            case 'R':

                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void checkApple()
    {
        if((x[0] == appleX) && (y[0] == appleY))
        {
            bodyparts++;
            applesEaten++;
            newApple();
        }
    }

    // a method to check collisions
    public void checkCollisions()
    {
        // the loop checks for snake coliding itself
        for(int i = bodyparts; i > 0; i--)
        {
            if(x[0] == x[i] && y[0] == y[i])
            {
                running = false;
            }
        }
        
        // left wall colision
        if(x[0] < 0)
        {
            running = false;
        }

        // right wall colision
        if(x[0] > SCREEN_WIDTH)
        {
            running = false;
        }

        // top wall colision
        if(y[0] < 0)
        {
            running = false;
        }

        // bottom wall colision
        if(y[0] > SCREEN_HEIGHT)
        {
            running = false;
        }
    }
    public void gameOver(Graphics g)
    {
        // game over text
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        // score
        g.setColor( new Color (0,0,102));
        g.setFont( new Font("Roboto", Font.BOLD, 25));
        g.drawString("Score: " + applesEaten, 5, g.getFont().getSize());

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (running)
        {
            move();
            checkApple();
            checkCollisions();    
        } 
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_LEFT: // move left (limiting a movement of a snake to 90 degrees)
                    if(direction != 'R')
                    {
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT: // move right
                    if(direction != 'L')
                    {
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP: // move up
                    if(direction != 'D')
                    {
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN: // move down
                    if(direction != 'W')
                    {
                        direction = 'D';
                    }
                    break;

            }
        }
    }
}
