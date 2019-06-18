import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class GameFunction {

    private GameData gameData;
    private DataArea dataArea;
    private boolean[][] playMap;
    private Color[][] colorMap;
    private Shape shape;
    private MainArea mainArea;
    private NextArea nextArea;
    private Timer timer;
    private int fallSpeed;
    private int speedFactor=0;
    private boolean isGG;

    public GameFunction(GameData gameData,MainArea mainArea,NextArea nextArea,Timer timer,int fallSpeed,boolean isGG){
        this.mainArea = mainArea;
        this.nextArea = nextArea;
        this.gameData = gameData;
        this.playMap = gameData.playMap;
        this.colorMap = gameData.colorMap;
        this.timer = timer;
        this.fallSpeed = fallSpeed;
        this.isGG = isGG;
    }

    //move tetromino
    public void move(int moveX,int moveY){
        if(isGG){
            return;
        }
        playMap = gameData.playMap;
        shape = gameData.getCurShape();
        shape.move(moveX,moveY,playMap);
    }

    public void rotateClockwise() {
        if(isGG){
            return;
        }
        playMap = gameData.playMap;
        if(gameData.getCanRotate()){
            shape = gameData.getCurShape();
            if(shape.getType() == 0){
                return;
            }
            shape.rotateClockwise(playMap);
        }
    }

    public void rotateCounterClockwise() {
        if(isGG){
            return;
        }
        playMap = gameData.playMap;
        if(gameData.getCanRotate()){
            shape = gameData.getCurShape();
            if(shape.getType() == 0){
                return;
            }
            shape.rotateCounterClockwise(playMap);
        }
    }

    public void falling() {
        this.shape = gameData.getCurShape();
        boolean[][] map = gameData.playMap;
        Color[][] colorMap = gameData.colorMap;
        //check whether the shape can move
        if(shape.move(0,1,map)){
            return;
        }

        // if it can not move, draw it in the playMap.
        Point[] tetrPonints = shape.getPoints()[shape.getPosNum()];
        for(int i=0; i<tetrPonints.length; i++){
            map[tetrPonints[i].x+shape.intiPos][tetrPonints[i].y] = true;
            colorMap[tetrPonints[i].x+shape.intiPos][tetrPonints[i].y] = shape.getColor();
        }

        this.updateLines(playMap,colorMap);

        //initial another shape
        gameData.initShape(gameData.getNextShapeNum());
        gameData.setCurTypeNum(gameData.getNextShapeNum());
        Shape shape1 = gameData.getCurShape();

        //check whether game is over
        if(isGameOver(shape1)){
            int result = JOptionPane.showConfirmDialog(new JFrame(), "Game over! Restart?", "",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                gameData.playMap = new boolean[mainArea.mapW][mainArea.mapH];
                gameData.colorMap = new Color[mainArea.mapW][mainArea.mapH];
                gameData.setCurScore(0);
                gameData.setCurLines(0);
                gameData.setCurLevel(1);
                gameData.setRequiredLine(10);
                gameData.setScoreFactor(1);
                Tetris.fallSpeed = 1000;
                mainArea.timer.setDelay(Tetris.fallSpeed);
                this.speedFactor = 0;
            } else {
                System.exit(0);
            }
        }

        gameData.setNextTypeNum((int)(Math.random()*gameData.getRandom()));
        gameData.setNextShape(gameData.getNextShapeNum());
    }

    private boolean isGameOver(Shape shape) {
        Point[] points = shape.getPoints()[shape.getPosNum()];
        for (Point p : points) {
            if(gameData.playMap[p.x+shape.intiPos][p.y]){
                return true;
            }
        }
        return false;
    }


    private void updateLines(boolean[][] playMap,Color[][] colorMap) {
        for(int y=0; y<playMap[0].length;y++){
            if(checkRemove(y)){ // check whether the line can be removed
                removeLine(y,playMap,colorMap); // if yes, remove the line
                updateData();
            }
        }
    }

    private boolean checkRemove(int y){
        for(int x=0; x< playMap.length; x++){
            if(!playMap[x][y]){ // if any block is false, continue to check next row
                return false;
            }
        }
        return true;
    }

    private void removeLine(int line,boolean[][] playMap,Color[][] colorMap){
        for(int x=0; x<playMap.length;x++){
            for (int y=line; y>0; y--){
                playMap[x][y]=playMap[x][y-1];
                colorMap[x][y]=colorMap[x][y-1];
            }
            playMap[x][0]=false;
        }
        mainArea.repaint();
    }

    private void updateData(){
        gameData.curLines = gameData.curLines + 1; // update counter of lines
        gameData.curScore = gameData.curScore + gameData.curLevel * gameData.getScoreFactor(); // update scores
        if(gameData.curLines >= gameData.getRequiredLine()){ // if level up,
            gameData.curLevel += 1;
            gameData.setScoreFactor(gameData.getScoreFactor()+1);
            if(gameData.getScoreFactor() > 10){ // set the upper bounds of required line.
                gameData.setScoreFactor(10);
            }
            gameData.setRequiredLine(gameData.getRequiredLine()+gameData.getRequiredLine());
            if(gameData.getRequiredLine() > 50){ // set the upper bounds of required line.
                gameData.setRequiredLine(50);
            }
            speedFactor = speedFactor+10;
            if(speedFactor > 100){
                speedFactor=100;
            }
            timer.setDelay((int)(fallSpeed/(1+speedFactor/100.0*gameData.curLevel)));//change the falling speed.
        }
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    public int getSpeedFactor() {
        return speedFactor;
    }

    public void setSpeedFactor(int speedFactor) {
        this.speedFactor = speedFactor;
    }

    public boolean[][] getPalyMap() {
        return playMap;
    }

    public void setPalyMap(boolean[][] palyMap) {
        this.playMap = palyMap;
    }

    public Color[][] getColorMap() {
        return colorMap;
    }

    public void setColorMap(Color[][] colorMap) {
        this.colorMap = colorMap;
    }

    public void setFallSpeed(int fallSpeed) {
        this.fallSpeed = fallSpeed;
    }

}