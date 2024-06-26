import javax.swing.JFrame;

public class GameFrame extends JFrame
{
    GameFrame() // constructor
    {
        this.add(new GamePanel());
        this.setTitle("Snake");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setResizable(false);
        this.pack();

        this.setVisible(true);
        this.setLocationRelativeTo(null); // for window to appear in middle of the screen
    }
}
