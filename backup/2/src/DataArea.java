import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DataArea extends JPanel{

    public GameData gameData;
    private int width;
    private int height;
    private JLabel level,lines,score;

    public DataArea (GameData gameData){
        this.gameData = gameData;

        JButton quit =new JButton("Quit");
        level = new JLabel("Level:  "+gameData.getCurLevel());
        lines = new JLabel("Lines:  "+gameData.getCurLines());
        score = new JLabel("Score:  "+gameData.getCurScore());

        //monitor change of size of data_area
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                width = getWidth();
                height = getHeight();

                //setup the "QUIT" button.
                quit.setBorder(BorderFactory.createLineBorder(Color.black));
                quit.setBounds((int)(width*0.1),(int)(height*0.8),
                        (int)(width*0.6),(int)(height*0.134));
                quit.setFont(new Font("quit",Font.BOLD,(int)(width*0.12)));

                //set quit_button for quit the program
                quit.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        System.exit(0);
                    }
                });
                quit.setVisible(true);

                //setup all labels to display the data
                level.setBounds(1,(int)(height * 0.067), width-2,(int)(height *0.134));
                level.setFont(new Font("level",Font.BOLD,(int)(width *0.12)));

                lines.setBounds(1,(int)(height*0.267), width-2,(int)(height*0.134));
                lines.setFont(new Font("lines",Font.BOLD,(int)(width*0.12)));

                score.setBounds(1,(int)(height*0.467),width-2,(int)(height*0.134));
                score.setFont(new Font("score",Font.BOLD,(int)(width*0.12)));

                //add all components to data_area
                add(quit);
                add(level);
                add(lines);
                add(score);
            }
        });

    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        level.setText("Level:  "+gameData.getCurLevel());
        lines.setText("Lines:  "+gameData.getCurLines());
        score.setText("Score:  "+gameData.getCurScore());
    }

    public void setGameData(GameData gameData) { this.gameData = gameData; }

}