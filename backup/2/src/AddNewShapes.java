import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class AddNewShapes extends JFrame {

    private int mapW,mapH;
    private Shape[] newShapes = new Shape[8];
    private Shape[] newShapesNext = new Shape[8];
    private GameData gameData;

    public AddNewShapes(int mainWidth,int mainHeight,GameData gameData,NextArea nextArea,Timer timer,MainArea mainArea){
        this.setTitle("Add New Shapes");
        this.setSize(800,500);
        this.setLayout(null);
        this.mapW = mainWidth;
        this.mapH = mainHeight;
        this.gameData = gameData;

        boolean[] selectedType = new boolean[8];

        intiNewShape(newShapes);
        intiNewShape(newShapesNext);

        //set up check box
        JCheckBox b0 = new JCheckBox();
        b0.setBounds(95,140,40,40);
        add(b0);
        b0.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox cb = (JCheckBox) e.getItem();
                if (cb.isSelected()) {
                    selectedType[0] = true;
                } else {
                    selectedType[0] = false;
                }
            }
        });

        JCheckBox b1 = new JCheckBox();
        b1.setBounds(274,140,40,40);
        add(b1);
        b1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox cb = (JCheckBox) e.getItem();
                if (cb.isSelected()) {
                    selectedType[1] = true;
                } else {
                    selectedType[1] = false;
                }
            }
        });

        JCheckBox b2 = new JCheckBox();
        b2.setBounds(453,140,40,40);
        add(b2);
        b2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox cb = (JCheckBox) e.getItem();
                if (cb.isSelected()) {
                    selectedType[2] = true;
                } else {
                    selectedType[2] = false;
                }
            }
        });

        JCheckBox b3 = new JCheckBox();
        b3.setBounds(632,140,40,40);
        add(b3);
        b3.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox cb = (JCheckBox) e.getItem();
                if (cb.isSelected()) {
                    selectedType[3] = true;
                } else {
                    selectedType[3] = false;
                }
            }
        });

        JCheckBox b4 = new JCheckBox();
        b4.setBounds(95,315,40,40);
        add(b4);
        b4.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox cb = (JCheckBox) e.getItem();
                if (cb.isSelected()) {
                    selectedType[4] = true;
                } else {
                    selectedType[4] = false;
                }
            }
        });

        JCheckBox b5 = new JCheckBox();
        b5.setBounds(274,315,40,40);
        add(b5);
        b5.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox cb = (JCheckBox) e.getItem();
                if (cb.isSelected()) {
                    selectedType[5] = true;
                } else {
                    selectedType[5] = false;
                }
            }
        });

        JCheckBox b6 = new JCheckBox();
        b6.setBounds(453,315,40,40);
        add(b6);
        b6.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox cb = (JCheckBox) e.getItem();
                if (cb.isSelected()) {
                    selectedType[6] = true;
                } else {
                    selectedType[6] = false;
                }
            }
        });

        JCheckBox b7 = new JCheckBox();
        b7.setBounds(632,315,40,40);
        add(b7);
        b7.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox cb = (JCheckBox) e.getItem();
                if (cb.isSelected()) {
                    selectedType[7] = true;
                } else {
                    selectedType[7] = false;
                }
            }
        });

        //set buttons
        JButton cancel = new JButton("CANCEL");
        cancel.setFont(new Font("font",Font.BOLD,15));
        cancel.setBounds(200,400, 100,40);

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.start();
                gameData.setCanRotate(true);
                mainArea.menuOpen = false;
                mainArea.setPause(false);
                mainArea.repaint();
                dispose();
            }
        });

        JButton submit = new JButton("SUBMIT");
        submit.setBounds(500,400,100,40);
        submit.setFont(new Font("font",Font.BOLD,15));
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tetromino t = new Tetromino(mapW,mapH);
                Tetromino tn = new Tetromino(mapW,mapH);

                int cout = 0, typeNum=7;
                for(int i=0;i<selectedType.length;i++){
                    if(selectedType[i]){
                        cout ++;
                        newShapes[i].setTypeNum(typeNum);
                        newShapesNext[i].setTypeNum(typeNum);
                        t.tetrominos.add(typeNum,newShapes[i]);
                        tn.tetrominos.add(typeNum,newShapesNext[i]);
                        typeNum++;
                    }
                }

                gameData.setTetrominos(t);
                nextArea.setNextT(tn);
                gameData.setRandom(7 + cout);
                timer.start();
                gameData.setCanRotate(true);
                mainArea.menuOpen = false;
                mainArea.setPause(false);
                mainArea.repaint();
                dispose();
            }
        });

        add(cancel);
        add(submit);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);

        Shape s = newShapes[0];
        g.setColor(s.getColor());
        for(Point p: s.getPoints()[s.getPosNum()]){
            g.fill3DRect((p.x+4)*20, 20 * (p.y+5), 20, 20, true);
        }

        s = newShapes[1];
        g.setColor(s.getColor());
        for(Point p: s.getPoints()[s.getPosNum()]){
            g.fill3DRect( (p.x+13)*20, 20 * (p.y+5), 20, 20, true);
        }

        s = newShapes[2];
        g.setColor(s.getColor());
        for(Point p: s.getPoints()[s.getPosNum()]){
            g.fill3DRect((p.x+22)*20, 20 * (p.y+5), 20, 20, true);
        }

        s = newShapes[3];
        g.setColor(s.getColor());
        for(Point p: s.getPoints()[s.getPosNum()]){
            g.fill3DRect( (p.x+31)*20, 20 * (p.y+5), 20, 20, true);
        }

        s = newShapes[4];
        g.setColor(s.getColor());
        for(Point p: s.getPoints()[s.getPosNum()]){
            g.fill3DRect((p.x+4)*20, 20 * (p.y+13), 20, 20, true);
        }

        s = newShapes[5];
        g.setColor(s.getColor());
        for(Point p: s.getPoints()[s.getPosNum()]){
            g.fill3DRect( (p.x+13)*20, 20 * (p.y+13), 20, 20, true);
        }

        s = newShapes[6];
        g.setColor(s.getColor());
        for(Point p: s.getPoints()[s.getPosNum()]){
            g.fill3DRect((p.x+22)*20, 20 * (p.y+13), 20, 20, true);
        }

        s = newShapes[7];
        g.setColor(s.getColor());
        for(Point p: s.getPoints()[s.getPosNum()]){
            g.fill3DRect( (p.x+31)*20, 20 * (p.y+13), 20, 20, true);
        }

    }

    public void intiNewShape(Shape[] shapes){

        shapes[0] = new Shape(2,0,Color.GRAY,
                new Point[][]{{new Point(1,0),new Point(0,1),new Point(1,1)},
                        {new Point(1,0),new Point(1,1),new Point(2,1)},
                        {new Point(1,1),new Point(2,1),new Point(1,2)},
                        {new Point(0,1),new Point(1,1),new Point(1,2)}},mapW,mapH);

        shapes[1] = new Shape(3,0,new Color(64,224,208),
                new Point[][]{{new Point(0,0),new Point(1,1),new Point(2,1)},
                        {new Point(2,0),new Point(1,1),new Point(1,2)},
                        {new Point(0,1),new Point(1,1),new Point(2,2)},
                        {new Point(1,0),new Point(1,1),new Point(0,2)}},mapW,mapH);

        shapes[2] = new Shape(3,0,new Color(225,160,122),
                new Point[][]{{new Point(1,1),new Point(2,0)},
                        {new Point(1,1),new Point(2,2)},
                        {new Point(1,1),new Point(0,2)},
                        {new Point(0,0),new Point(1,1)}},mapW,mapH);

        shapes[3] = new Shape(3,0,new Color(160,82,45),
                new Point[][]{{new Point(1,0),new Point(0,1),new Point(2,1)},
                        {new Point(1,0),new Point(2,1),new Point(1,2)},
                        {new Point(0,1),new Point(2,1),new Point(1,2)},
                        {new Point(1,0),new Point(0,1),new Point(1,2)}},mapW,mapH);

        shapes[4] = new Shape(3,0,Color.PINK,
                new Point[][]{{new Point(1,0),new Point(1,1),new Point(1,2)},
                        {new Point(0,1),new Point(1,1),new Point(2,1)},
                        {new Point(1,0),new Point(1,1),new Point(1,2)},
                        {new Point(0,1),new Point(1,1),new Point(2,1)}},mapW,mapH);

        shapes[5] = new Shape(2,0,new Color(33,128,113),
                new Point[][]{{new Point(0,1),new Point(1,1)},
                        {new Point(1,0),new Point(1,1)},
                        {new Point(0,1),new Point(1,1)},
                        {new Point(1,0),new Point(1,1)}},mapW,mapH);

        shapes[6] = new Shape(3,0,new Color(144,196,223),
                new Point[][]{{new Point(2,0),new Point(1,1),new Point(0,2)},
                        {new Point(0,0),new Point(1,1),new Point(2,2)},
                        {new Point(2,0),new Point(1,1),new Point(0,2)},
                        {new Point(0,0),new Point(1,1),new Point(2,2)}},mapW,mapH);

        shapes[7] = new Shape(2,0,new Color(12,119,170),
                new Point[][]{{new Point(1,1)},
                        {new Point(1,1)},
                        {new Point(1,1)},
                        {new Point(1,1)}},mapW,mapH);
    }

}
