import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Tetromino extends JPanel{

    public ArrayList<Shape> tetrominos;

    public Tetromino(int mapW, int mapH) {

        tetrominos = new ArrayList<>();

        //square shape
        tetrominos.add(0, new Shape(2,0,new Color(50,205,50),
                new Point[][]{{new Point(0,0),new Point(1,0),new Point(0,1),new Point(1,1)},
                        {new Point(0,0),new Point(1,0),new Point(0,1),new Point(1,1)},
                        {new Point(0,0),new Point(1,0),new Point(0,1),new Point(1,1)},
                        {new Point(0,0),new Point(1,0),new Point(0,1),new Point(1,1)}},mapW,mapH));

        // I shape
        tetrominos.add(1,new Shape(4,1,Color.CYAN,
                new Point[][]{{new Point(0,0),new Point(1,0),new Point(2,0),new Point(3,0)},
                        {new Point(1,0),new Point(1,1),new Point(1,2),new Point(1,3)},
                        {new Point(0,0),new Point(1,0),new Point(2,0),new Point(3,0)},
                        {new Point(1,0),new Point(1,1),new Point(1,2),new Point(1,3)}},mapW,mapH));

        // L shape
        tetrominos.add(2,new Shape(3,2,Color.RED,
                new Point[][]{{new Point(2,0),new Point(0,1),new Point(1,1),new Point(2,1)},
                        {new Point(1,0),new Point(1,1),new Point(1,2),new Point(2,2)},
                        {new Point(0,1),new Point(1,1),new Point(2,1),new Point(0,2)},
                        {new Point(0,0),new Point(1,0),new Point(1,1),new Point(1,2)}},mapW,mapH));

        // J shape
        tetrominos.add(3,new Shape(3,3,Color.BLUE,
                new Point[][]{{new Point(0,0),new Point(0,1),new Point(1,1),new Point(2,1)},
                        {new Point(1,0),new Point(2,0),new Point(1,1),new Point(1,2)},
                        {new Point(0,1),new Point(1,1),new Point(2,1),new Point(2,2)},
                        {new Point(1,0),new Point(1,1),new Point(0,2),new Point(1,2)}},mapW,mapH));

        // S shape
        tetrominos.add(4,new Shape(3,4,Color.YELLOW,
                new Point[][]{{new Point(1,0),new Point(2,0),new Point(0,1),new Point(1,1)},
                        {new Point(1,0),new Point(1,1),new Point(2,1),new Point(2,2)},
                        {new Point(1,0),new Point(2,0),new Point(0,1),new Point(1,1)},
                        {new Point(1,0),new Point(1,1),new Point(2,1),new Point(2,2)}},mapW,mapH));

        // Z shape
        tetrominos.add(5,new Shape(3,5,new Color(138,43,226),
                new Point[][]{{new Point(0,0),new Point(1,0),new Point(1,1),new Point(2,1)},
                        {new Point(2,0),new Point(1,1),new Point(2,1),new Point(1,2)},
                        {new Point(0,0),new Point(1,0),new Point(1,1),new Point(2,1)},
                        {new Point(2,0),new Point(1,1),new Point(2,1),new Point(1,2)}},mapW,mapH));

        // T shape
        tetrominos.add(6,new Shape(3,6,Color.ORANGE,
                new Point[][]{{new Point(1,0),new Point(0,1),new Point(1,1),new Point(2,1)},
                        {new Point(1,0),new Point(1,1),new Point(2,1),new Point(1,2)},
                        {new Point(0,1),new Point(1,1),new Point(2,1),new Point(1,2)},
                        {new Point(1,0),new Point(0,1),new Point(1,1),new Point(1,2)}},mapW,mapH));
    }

    public Shape getShape(int typeNum){
        return tetrominos.get(typeNum);
    }

}