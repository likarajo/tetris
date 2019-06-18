import java.awt.*;

public class Shape{
    private Color color;
    private Point[][] points;
    public int typeNum;
    public int posNum;
    public int offX;
    public int intiPos;
    private static int MIN_X=0;
    public static int MAX_X;
    private static int MIN_Y=0;
    public static int MAX_Y;

    public Shape(int offX,int typeNum,Color color, Point[][] points,int mapW,int mapH){

        this.offX = offX;
        this.color = color;
        this.points = points;
        this.typeNum = typeNum;
        this.posNum = 0;
        this.intiPos = (mapW - offX)/2;
        MAX_X = mapW - 1;
        MAX_Y = mapH - 1;

    }

    public int getType(){ return typeNum; }

    public void setTypeNum(int typeNum) {
        this.typeNum = typeNum;
    }

    public int getPosNum(){ return posNum; }

    public Color getColor(){ return color; }

    public Point[][] getPoints(){ return points; }

    //move the blocks
    public boolean move(int moveX,int moveY,boolean[][] map){
        for(Point p:points[posNum]){
            int newX = p.x + moveX;
            int newY = p.y + moveY;
            if(this.isOutOfBoundary(newX,newY,map)){
                return false;
            }
        }

        for(int x=0;x<points.length;x++){
            for(int y=0; y<points[x].length;y++){
                points[x][y].x += moveX;
                points[x][y].y += moveY;
            }
        }
        return true;
    }

    public void rotateClockwise(boolean[][] map){
        int newPosNum = posNum+1;
        for(Point p:getPoints()[newPosNum % 4]){
            if(isOutOfBoundary(p.x,p.y,map)){
                return;
            }
        }
        posNum = newPosNum % 4;
    }

    public void rotateCounterClockwise(boolean[][] map){
        int newPosNum = posNum-1;
        if(newPosNum < 0){
            newPosNum = 3;
        }
        for(Point p:getPoints()[newPosNum % 4]){
            if(isOutOfBoundary(p.x,p.y,map)){
                return;
            }
        }
        posNum = newPosNum % 4;
    }

    //check whether the shape touched the boundary
    private boolean isOutOfBoundary(int x,int y,boolean map[][]){
        return x+intiPos < MIN_X || x+intiPos > MAX_X || y < MIN_Y || y > MAX_Y || map[x+this.intiPos][y];
    }

    public void setIntiPos(int intiPos) { this.intiPos = intiPos; }
}
