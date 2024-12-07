
import javax.swing.JFrame;

public class App {
    public static void main(String args[]) throws Exception {
        int boardWidth = 360;
        int boardHeight = 640;
 
         
        JFrame frame = new JFrame("Flappy Bird");
        //frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);  // the frame will be located at the centre of the screen
        frame.setResizable(false);  // frame can not be resizable
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // when user click on the x on the screen the program closes


        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack(); // it helps to save the title bar of the page from getting colored in blue i:e givinng true 360*640 screen
        flappyBird.requestFocus();  //it will help to have focus on keyListener events
        frame.setVisible(true);

    }
}
