package game;

import javax.swing.*;
import java.awt.*;

public class Fruit {
    private int x;
    private int y;
    private ImageIcon img;
    private int snakeSize;

    public Fruit(){
        //img = new ImageIcon("fruit.png");
        img = new ImageIcon(getClass().getResource("fruit.png"));
        this.x = (int) (Math.floor(Math.random() * Main.col) * Main.CELL_SIZE);
        this.y = (int) (Math.floor(Math.random() * Main.row) * Main.CELL_SIZE);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void drawFruit(Graphics g){
//        g.setColor(Color.YELLOW);
//        g.fillOval(this.x,this.y,Main.CELL_SIZE,Main.CELL_SIZE);
        img.paintIcon(null,g,this.x,this.y);
    }

    public void setNewLocation(Snake s){
        int new_x;
        int new_y;
        boolean overLapping;
        do{
            new_x = (int) (Math.floor(Math.random() * Main.col) * Main.CELL_SIZE);
            new_y = (int) (Math.floor(Math.random() * Main.row) * Main.CELL_SIZE);
            overLapping = check_overlap(new_x, new_y, s);
        } while (overLapping);

        this.x = new_x;
        this.y = new_y;
    }

    private boolean check_overlap(int x, int y, Snake s){
        //ArrayList<Node> snakeBody = s.getSnakeBody();
        for(int i = 0 ; i < s.getSnakeBody().size() ; i++){
            if(x == s.getSnakeBody().get(i).x && y == s.getSnakeBody().get(i).y){
                return true;
            }
        }
        return false;
    }
}
