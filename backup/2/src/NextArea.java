import javax.swing.*;
import java.awt.*;

public class NextArea extends JPanel{

    public int blockSize;
    public GameData gameData;
    private Shape shape;
    private int mapW,mapH;
    private Tetromino nextT;

    public NextArea(GameData gameData,int blockSize,int mapW,int mapH) {

        this.gameData = gameData;
        this.blockSize = blockSize;
        nextT = new Tetromino(mapW,mapH);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        shape = nextT.getShape(gameData.getNextShapeNum());
        Point[] points = shape.getPoints()[shape.getPosNum()];
        int offY = 2;
        for(Point p:points){
            g.setColor(shape.getColor());
            g.fill3DRect( ((7-shape.offX)/2 + p.x)*blockSize, ((4-offY)/2 + p.y)*blockSize, blockSize, blockSize, true);
        }
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public void setNextT(Tetromino nextT) {
        this.nextT = nextT;
    }
}