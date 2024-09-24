package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

public class Main extends JPanel implements KeyListener {

    public static final int CELL_SIZE = 20;
    public static int width = 400;
    public static int height = 400;
    public static int row = height/CELL_SIZE;
    public static int col = width/CELL_SIZE;
    private Snake snake;
    private Fruit fruit;
    private Timer timer;
    private int speed = 100;
    private static String direction;
    private boolean allowKeyPress;
    private int score;
    private int highestScore;
    String desktop = System.getProperty("user.home") + "/Desktop/";
    String myFile = desktop + "ScoreHistory.txt";

    public Main(){
        readHigherScore();
        reset();
        addKeyListener(this);
    }

    private void setTimer(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        },0,speed);
    }

    private void reset(){
        score = 0;
        if(snake != null){
            snake.getSnakeBody().clear();
        }
        allowKeyPress = true;
        direction = "Right";
        snake = new Snake();
        fruit = new Fruit();
        setTimer();
    }

    @Override
    public void paintComponent(Graphics g){
        //check if the snake bites itself
        ArrayList<Node> snake_body = snake.getSnakeBody();
        Node head = snake_body.get(0);
        for(int i = 1; i < snake_body.size(); i++){
            if(snake_body.get(i).x == head.x && snake_body.get(i).y == head.y){
                allowKeyPress = false;
                timer.cancel();
                timer.purge();
                int response = JOptionPane.showOptionDialog(this,"Game Over! Your score is " + score + " . The highest score was " + highestScore + " .Would you like to start over?","Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,null,JOptionPane.YES_OPTION);
                writeAFile(score);
                switch(response){
                    case JOptionPane.CLOSED_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.NO_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.YES_OPTION:
                        reset();
                        return;
                }
            }
        }

        //draw a black background
        g.fillRect(0, 0, width, height);
        fruit.drawFruit(g);
        snake.drawSnack(g);


        //remove snake tail and put it in head
        int snakeX = snake.getSnakeBody().get(0).x;
        int snakeY = snake.getSnakeBody().get(0).y;
        // right, x+= cell_size
        // left, x-= cell_size
        // down, y+= cell_size
        // up, y-= cell_size
        if(direction.equals("Left")){
            snakeX -= CELL_SIZE;
        } else if(direction.equals("Up")){
            snakeY -= CELL_SIZE;
        } else if(direction.equals("Right")){
            snakeX += CELL_SIZE;
        } else if(direction.equals("Down")){
            snakeY += CELL_SIZE;
        }
        Node newHead = new Node(snakeX, snakeY);

        //check if the snake eats the fruit
        if(snake.getSnakeBody().get(0).x == fruit.getX() && snake.getSnakeBody().get(0).y == fruit.getY()){
            fruit.setNewLocation(snake);
            fruit.drawFruit(g);
            score++;
            //1. set fruit into new location
            //2. draw fruit
            //3. score++
        } else {
            snake.getSnakeBody().remove(snake.getSnakeBody().size()-1);
        }

        snake.getSnakeBody().add(0,newHead);

        allowKeyPress = true;
        requestFocusInWindow();
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(width, height);
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Game.Snake Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new Main());
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setResizable(false);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (allowKeyPress){
            if(e.getKeyCode() == 37 && !direction.equals("Right")){
                direction = "Left";
            } else if(e.getKeyCode() == 38 && !direction.equals("Down")){
                direction = "Up";
            } else if(e.getKeyCode() == 39 && !direction.equals("Left")){
                direction = "Right";
            } else if(e.getKeyCode() == 40 && !direction.equals("Up")){
                direction = "Down";
            }
            allowKeyPress = false;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public void readHigherScore(){
        try{
            File myObj = new File(myFile);
            Scanner myReader = new Scanner(myObj);
            try {
                highestScore = myReader.nextInt();
            } catch (NoSuchElementException e) {
                highestScore = 0;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            highestScore = 0;
            try{
                File myObj = new File(myFile);
                if(myObj.createNewFile()){
                    System.out.println("File created: " + myObj.getName());
                }
                FileWriter myWriter = new FileWriter(myObj.getName());
                myWriter.write("" + 0);
            }catch (IOException err){
                System.out.println("An error occurred.");
                err.printStackTrace();
            }
        }
    }

    public void writeAFile(int score){
        try{
            FileWriter myWriter = new FileWriter(myFile);
            if(score > highestScore){
                myWriter.write("" + score);
                highestScore = score;
            } else{
                myWriter.write("" + highestScore);
            }
            myWriter.close();
        }catch (IOException err){
            err.printStackTrace();
        }
    }
}
