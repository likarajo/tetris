import com.sun.javafx.geom.Point2D;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MainArea extends JPanel {

    private GameData gameData;
    public Timer timer;
    public boolean isPause;
    public boolean isGG;
    public int blockSize;
    public Shape curShape;
    public int mapW,mapH;
    private NextArea nextArea;
    private DataArea dataArea;
    public  boolean menuOpen;

    public MainArea(GameData gameData,Timer timer,int blockSize,int mapW,int mapH,NextArea nextArea,DataArea dataArea){
        this.gameData = gameData;
        this.timer = timer;
        this.blockSize = blockSize;
        this.mapW = mapW;
        this.mapH = mapH;
        this.nextArea = nextArea;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                // test whether the mouse is inside the tetromino;
                Point2D p = new Point2D(e.getX(),e.getY());
                Point[] points = curShape.getPoints()[curShape.getPosNum()];
                for(Point i: points){
                    Point2D[] pol = calculatePol(i);
                    if((insidePolypon(p,pol))){
                        int curS = dataArea.gameData.getCurScore();
                        int sf = dataArea.gameData.getScoreFactor();
                        int lev = dataArea.gameData.getCurLevel();

                        // if is inside, check the score
                        if(curS >= lev*sf){
                            // change to next shape
                            dataArea.gameData.initShape(dataArea.gameData.getNextShapeNum());
                            dataArea.gameData.setCurTypeNum(dataArea.gameData.getNextShapeNum());
                            dataArea.gameData.setNextTypeNum((int)(Math.random()*7));
                            dataArea.gameData.setNextShape(dataArea.gameData.getNextShapeNum());
                            dataArea.gameData.setCurScore(curS - lev*sf);
                            dataArea.repaint();
                        }
                    }
                }
                repaint();
                nextArea.repaint();
            }
            @Override
            public void mouseEntered(MouseEvent e) {//if entered, display "PAUSE"
                if(!isGG){
                    timer.stop();
                    //gameData.setCanRotate(false);
                    isPause = true;
                    setRotate();
                    repaint();
                }
            }
            @Override
            public void mouseExited(MouseEvent e){//if exited, hide "PAUSE"
                if(!isGG && !menuOpen){
                    timer.start();
                    //gameData.setCanRotate(true);
                    isPause = false;
                    setRotate();
                    repaint();
                }
            }
        });
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);

        int width = this.getWidth();
        int height = this.getHeight();
        curShape = gameData.getCurShape();
        Point[][] points = curShape.getPoints();

        if(menuOpen){
            g.setColor(Color.BLUE);
            if(this.getWidth() < this.getHeight()){
                g.setFont(new Font("pause",Font.BOLD,(int)(width*0.15)));
                g.drawRect((int)(width-width*0.6)>>1,(int)(height-height*0.2)>>1,(int)(width*0.6),(int)(height*0.15));
                g.drawString("PAUSE",(int)(width-width*0.5)>>1,(int)(height-height*0.01)>>1);
            }
            else{
                g.setFont(new Font("pause",Font.BOLD,(int)(height*0.15)));
                g.drawRect((int)(width-height*0.6)>>1,(int)(height-height*0.33)>>1,(int)(height*0.6),(int)(height*0.3));
                g.drawString("PAUSE",(int)(width-height*0.5)>>1,7*height/13);
            }
        }

        if(isPause){
            g.setColor(Color.BLUE);
            if(this.getWidth() < this.getHeight()){
                g.setFont(new Font("pause",Font.BOLD,(int)(width*0.15)));
                g.drawRect((int)(width-width*0.6)>>1,(int)(height-height*0.2)>>1,(int)(width*0.6),(int)(height*0.15));
                g.drawString("PAUSE",(int)(width-width*0.5)>>1,(int)(height-height*0.01)>>1);
            }
            else{
                g.setFont(new Font("pause",Font.BOLD,(int)(height*0.15)));
                g.drawRect((int)(width-height*0.6)>>1,(int)(height-height*0.33)>>1,(int)(height*0.6),(int)(height*0.3));
                g.drawString("PAUSE",(int)(width-height*0.5)>>1,7*height/13);
            }
        }

        for(Point p:points[curShape.getPosNum()]){
            g.setColor(curShape.getColor());
            g.fill3DRect( (curShape.intiPos + p.x)*blockSize, blockSize * p.y, blockSize, blockSize, true);
        }

        boolean[][] map = gameData.playMap;
        for(int x=0; x < map.length; x++){
            for(int y=0; y < map[x].length; y++){
                if(map[x][y]) {
                    g.setColor(gameData.getColorMap()[x][y]);
                    g.fill3DRect(blockSize*x,blockSize*y,blockSize,blockSize,true);
                }
            }
        }
    }

    private Point2D[] calculatePol(Point i) {
        Point2D[] pol = new Point2D[4];
        pol[0] =  new Point2D((i.x+curShape.intiPos)*blockSize,i.y*blockSize);
        pol[1] =  new Point2D((i.x+curShape.intiPos)*blockSize,i.y*blockSize+blockSize);
        pol[2] =  new Point2D((i.x+curShape.intiPos)*blockSize+blockSize,i.y*blockSize+blockSize);
        pol[3] =  new Point2D((i.x+curShape.intiPos)*blockSize+blockSize,i.y*blockSize);
        return pol;
    }

    public static boolean insidePolypon(Point2D p, Point2D[] pol){
        int n = pol.length, j=n-1;
        boolean b =false;
        float x=p.x, y = p.y;
        for(int i=0; i<n; i++){
            if(pol[j].y <= y && y<pol[i].y && Tools2D.area2(pol[j],pol[i],p)>0
                    || pol[i].y <= y && y < pol[j].y && Tools2D.area2(pol[i],pol[j],p)>0) b = !b;
            j = i;
        }
        return b;
    }

    public void setMapW(int mapW) { this.mapW = mapW; }

    public void setMapH(int mapH) { this.mapH = mapH; }

    public GameData getGameData() { return gameData; }

    public void setGameData(GameData gameData) { this.gameData = gameData; }

    public void setRotate(){
        if(isPause){
            this.getGameData().setCanRotate(false);
        }else{
            this.getGameData().setCanRotate(true);
        }
    }

    public void setGG(boolean GG) { isGG = GG; }

    public void setBlockSize(int blockSize) { this.blockSize = blockSize; }

    public void setPause(boolean pause) { isPause = pause; }

}