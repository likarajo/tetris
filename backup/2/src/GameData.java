import java.awt.*;

public class GameData {

    public boolean[][] playMap;
    public Color[][] colorMap;
    public Tetromino tetrominos;
    private int random = 7;
    private int curTypeNum=(int)(Math.random() * random),nextTypeNum;
    private Shape curShape, nextShape;
    public int curLevel=1, curLines=0, curScore=0;
    private boolean canRotate = true;
    private int scoreFactor = 1, requiredLine = 10;
    public int MAP_WIDTH, MAP_HEIGHT;

    public GameData(int mapW,int mapH){
        this.MAP_WIDTH = mapW;
        this.MAP_HEIGHT = mapH;
        playMap = new boolean[MAP_WIDTH][MAP_HEIGHT];
        colorMap = new Color[MAP_WIDTH][MAP_HEIGHT];
        tetrominos = new Tetromino(MAP_WIDTH,MAP_HEIGHT);
        nextTypeNum = (int)(Math.random() * random);
        while (nextTypeNum == curTypeNum){
            nextTypeNum = (int)(Math.random() * random);
        }
        initShape(curTypeNum);
        nextShape = tetrominos.getShape(nextTypeNum);
    }

    public void initShape(int type) {
        Shape s = tetrominos.getShape(type);
        Point[][] p = s.getPoints();
        Point[][] newP = new Point[p.length][p[0].length];
        for(int i=0; i<p.length;i++){
            for(int j=0; j<p[i].length;j++){
                newP[i][j] = new Point(p[i][j].x,p[i][j].y);
            }
        }
        curShape = new Shape(s.offX,s.typeNum,s.getColor(),newP,MAP_WIDTH,MAP_HEIGHT);
    }

    private boolean isGameOver(Shape shape) {
        Point[] points = shape.getPoints()[shape.getPosNum()];
        for (Point p : points) {
            if(playMap[p.x+shape.intiPos][0]){
                return true;
            }
        }
        return false;
    }

    public Color[][] getColorMap(){
        return colorMap;
    }

    public void setCurTypeNum(int curType){
        this.curTypeNum = curType;
    }

    public Shape getCurShape(){
        return curShape;
    }

    public int getNextShapeNum(){
        return nextTypeNum;
    }
    public void setNextTypeNum(int x){
        this.nextTypeNum = x;
    }

    public Shape getNextShape(){
        return nextShape;
    }
    public void setNextShape(int x){
        this.nextShape = tetrominos.getShape(x);
    }


    public int getCurLevel(){
        return curLevel;
    }

    public int getCurLines(){
        return curLines;
    }

    public int getCurScore(){
        return curScore;
    }
    public void setCurScore(int score){
        this.curScore = score;
    }

    public void setCanRotate(boolean x){
        canRotate = x;
    }

    public boolean getCanRotate(){
        return canRotate;
    }

    public void setMAP_WIDTH(int MAP_WIDTH) {
        this.MAP_WIDTH = MAP_WIDTH;
    }

    public void setMAP_HEIGHT(int MAP_HEIGHT) {
        this.MAP_HEIGHT = MAP_HEIGHT;
    }


    public int getRequiredLine() {
        return requiredLine;
    }

    public int getScoreFactor() {
        return scoreFactor;
    }

    public void setRequiredLine(int requiredLine) {
        this.requiredLine = requiredLine;
    }

    public void setScoreFactor(int scoreFactor) {
        this.scoreFactor = scoreFactor;
    }

    public void setCurShape(Shape curShape) {
        this.curShape = curShape;
    }

    public void setTetrominos(Tetromino tetrominos) {
        this.tetrominos = tetrominos;
    }

    public int getRandom() {
        return random;
    }

    public void setRandom(int random) {
        this.random = random;
    }

    public void setCurLines(int curLines) {
        this.curLines = curLines;
    }

    public void setCurLevel(int curLevel) {
        this.curLevel = curLevel;
    }
}