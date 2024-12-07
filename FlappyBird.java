import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener{
    int boardWidth = 360;
    int boardHeight = 640;

    //Images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;


    //Bird
    int birdX = boardWidth/8;
    int birdY = boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;
   
    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;
        
        Bird(Image img) {
            this.img = img;
        }
    }


    // pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;  //scaled by 6 times
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }



    //game logic
    Bird bird;
    int velocityX = -4;  //moves the pipe leftwards
    int velocityY = 0;  // negative means bird goes upward direction
    int gravity = 1;  //every frame the bird goes slow by 1 pixel

    ArrayList<Pipe> pipes;
    Random random = new Random();
    Timer gameLoop;
    Timer placePipesTimer;
    boolean gameOver = false;
    double score = 0;



    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
       // setBackground(Color.BLUE);
       setFocusable(true); //it makes sure that this constructor will follow the keyEvents
       addKeyListener(this);

        backgroundImg = new ImageIcon(getClass().getResource("./background.jpg")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./floppybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./pipe_top.jpg")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./pipe_bottom.jpg")).getImage();

        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        //timer to place each pipe at some time interval(1.5 sec)
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });  // at every 1.5 sec a new pipe image will come to game
        placePipesTimer.start();

        gameLoop = new Timer(1000/60, this); // here the draw method is getting performed 16 times per second(1000 means 1 second)
        gameLoop.start();
    }


    //to place new pipes
    public void placePipes() {
        //math.random() gives value between 0-1
        // so the below wil be like = [ 0 - (512/4=128) - ((0-1)*256 = 0-256) ]
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/5;

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // it willl invoke function from JPanel
        draw(g);
        //setVisible(true);
    }


    public void draw(Graphics g) {
        //System.out.println("draw");  // here you can check it is getting executed 16 times per second
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        //to draw the pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        //score
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        if(gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
            g.drawString("Press R to Restart", 10, 75);
        }
        else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }


    public void move() {
        //to move the bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        //to move the pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;


            if(!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score+= 0.5; //here we used 0.5 instead of 1 as for passing 1 pipe the bird also passing bottom pipe. so score will be 1 in case of 0.5. If i take 1, then scre will be 2.
            }
            if(collision(bird, pipe)) {
                gameOver = true;
            }
        }

        if (bird.y > boardHeight) {
            gameOver = true;
        }
    }

    public boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&
            a.x + a.width > b.x &&
            a.y < b.y + b.height &&
            a.y + a.height > b.y ;
    }


    public void restartGame() {
        bird.x = birdX;
        bird.y = birdY;
        velocityY = 0;
        pipes.clear();
        score = 0;
        gameOver = false;
        placePipesTimer.start();
        gameLoop.start();
        repaint();
    }
    
    //ActionListener methods
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }


    //keyListener methods
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -8;
        }
        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            restartGame(); // Restart the game if it's over
        }
    }
    @Override
    public void keyReleased(KeyEvent e){}
}
