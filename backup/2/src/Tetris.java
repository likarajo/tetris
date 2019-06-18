import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

public class Tetris extends JFrame {

    private int pixelSize = 20;
    private int newW,newH,actualHeight;
    private int mainWidth =10 ,mainHeight =20;
    public static int fallSpeed=1000;
    public boolean isGG = false;
    public Timer timer = new Timer(fallSpeed,null);
    private GameData gameData = new GameData(mainWidth,mainHeight);

    //components on JFrame
    private JMenuBar menuBar = new JMenuBar();
    private NextArea nextArea = new NextArea(gameData,pixelSize,mainWidth,mainHeight);
    private DataArea dataArea = new DataArea(gameData);
    private MainArea mainArea = new MainArea(gameData,timer,pixelSize,mainWidth,mainHeight,nextArea,dataArea);
    private GameFunction gameFunction = new GameFunction(gameData,mainArea,nextArea,timer,fallSpeed,isGG);


    public Tetris(){
        //setup JFrame
        this.setTitle("Tetris Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(440,480);
        //this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setLayout(null);
        this.setJMenuBar(menuBar);

        //setup menubar
        menuGui(this);

        //set and add all components on the JFrame
        mainArea.setBorder(BorderFactory.createLineBorder(Color.black));
        mainArea.setLayout(null);
        mainArea.setVisible(true);

        nextArea.setBorder(BorderFactory.createLineBorder(Color.black));
        nextArea.setLayout(null);
        nextArea.setVisible(true);

        dataArea.setLayout(null);
        dataArea.setVisible(true);


        //monitor change of size of JFrame
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                Insets insets = Tetris.this.getInsets();
                //get new width and height
                newW = getWidth();
                newH = getHeight();
                actualHeight = newH-insets.top-menuBar.getHeight();

                //compute the position and size of main_area with shorter one of new width and height
                if(newH < newW){
                    pixelSize = (int)(Math.ceil(newH*1.0 / (mainHeight+4)));
                }
                else{
                    pixelSize = (int)(Math.ceil(newW*1.0/ (mainWidth+13)));
                }

                mainArea.blockSize = pixelSize;
                nextArea.blockSize = pixelSize;
                //setup size of each component with width and height of main_area.
                mainArea.setBounds(2*pixelSize,(actualHeight-mainHeight*pixelSize)>>1,mainWidth*pixelSize,mainHeight*pixelSize);
                nextArea.setBounds(mainArea.getX()+mainArea.getWidth()+2*pixelSize,(actualHeight-mainHeight*pixelSize)>>1,7*pixelSize, 4*pixelSize);
                dataArea.setBounds(mainArea.getX()+mainArea.getWidth()+2*pixelSize,nextArea.getY()+nextArea.getHeight()+2*pixelSize,
                        8*pixelSize, 14*pixelSize);
            }
        });

        add(mainArea);
        add(nextArea);
        add(dataArea);

        //monitor player control
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getButton() ==  1){ //left-click , shape move to left
                    gameFunction.move(-1,0);
                    repaint();
                }
                if(e.getButton() ==  3){//right-click, shape move to right
                    gameFunction.move(1,0);
                    repaint();
                }
            }
        });

        // mouse wheel motion, rotation
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(e.getWheelRotation() == -1){
                    gameFunction.rotateClockwise();
                    repaint();
                }
                if(e.getWheelRotation() == 1) {
                    gameFunction.rotateCounterClockwise();
                    repaint();
                }
            }
        });

        // tetromino falls with constant speed
        timer.start();
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFunction.falling();
                mainArea.repaint();
                nextArea.repaint();
                dataArea.repaint();
            }
        });

        this.setVisible(true);
    }

    // setup all item on the menubar
    public void menuGui(JFrame jf){

        JMenu set = new JMenu("Game Setting");

        JMenuItem parameterSet = new JMenuItem("Game Parameters");
        JMenuItem areaSet = new JMenuItem("Play Area Size");
        JMenuItem blockSet = new JMenuItem("Tetromino Size");
        set.add(parameterSet);
        set.add(areaSet);
        set.add(blockSet);
        set.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gamePause();

            }
        });

        parameterSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                gamePause();

                JFrame paraFrame = new JFrame("Game parameters setting..."); // game parameter setting window.
                paraFrame.setSize(460,335);

                //set labels
                JLabel sf = new JLabel("Score  Factor:");
                JLabel ql = new JLabel("Required Line:");
                JLabel fs = new JLabel("Speed Factor:");

                sf.setBounds(25,30, 150,50);
                ql.setBounds(25,100,150,50);
                fs.setBounds(25,170,150,50);

                sf.setFont(new Font("para",Font.BOLD,15));
                ql.setFont(new Font("para",Font.BOLD,15));
                fs.setFont(new Font("para",Font.BOLD,15));

                JSlider sfSlider = new JSlider(1,10,gameData.getScoreFactor());
                sfSlider.setBounds(140,40,300,40);
                sfSlider.setPaintTicks(true);
                sfSlider.setPaintLabels(true);
                sfSlider.setSnapToTicks(true);
                sfSlider.setMajorTickSpacing(1);
                paraFrame.add(sfSlider);

                JSlider qlSlider = new JSlider(10,50,gameData.getRequiredLine());
                qlSlider.setBounds(140,110,300,40);
                qlSlider.setPaintTicks(true);
                qlSlider.setPaintLabels(true);
                qlSlider.setSnapToTicks(true);
                qlSlider.setMajorTickSpacing(10);
                qlSlider.setMinorTickSpacing(5);
                paraFrame.add(qlSlider);

                //the labels for the fsSlider
                Hashtable labelTable = new Hashtable();
                for (int i = 0; i <= 100; i+=10) {
                    labelTable.put(new Integer(i), new JLabel(String.valueOf(i/100.0)));
                }

                JSlider fsSlider = new JSlider(0,100,gameFunction.getSpeedFactor());
                fsSlider.setLabelTable(labelTable);
                fsSlider.setPaintLabels(true);
                fsSlider.setBounds(140,180,300,40);
                fsSlider.setPaintTicks(true);
                fsSlider.setPaintLabels(true);
                fsSlider.setSnapToTicks(true);
                fsSlider.setMajorTickSpacing(10);
                paraFrame.add(fsSlider);

                //set buttons
                JButton cancel = new JButton("CANCEL");
                cancel.setBounds(75,250, 100,40);
                cancel.setFont(new Font("font",Font.BOLD,15));
                cancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gameRestart();
                        paraFrame.dispose();
                    }
                });

                JButton submit = new JButton("SUBMIT");
                submit.setBounds(280,250,100,40);
                submit.setFont(new Font("font",Font.BOLD,15));
                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gameData.setRequiredLine(qlSlider.getValue());
                        gameData.setScoreFactor(sfSlider.getValue());
                        gameData.setCanRotate(true);
                        gameFunction.setSpeedFactor(fsSlider.getValue());
                        gameFunction.setPalyMap(gameData.playMap);
                        gameFunction.setColorMap(gameData.colorMap);
                        fallSpeed = (int)(fallSpeed/(1+gameFunction.getSpeedFactor()/100.0*gameData.curLevel));
                        timer.setDelay(fallSpeed);
                        gameRestart();
                        paraFrame.dispose();
                    }
                });

                // add all components
                paraFrame.add(sf);
                paraFrame.add(ql);
                paraFrame.add(fs);
                paraFrame.add(cancel);
                paraFrame.add(submit);

                paraFrame.setLayout(null);
                paraFrame.setLocationRelativeTo(null);
                paraFrame.setResizable(false);
                paraFrame.setVisible(true);
            }
        });

        areaSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePause();

                JFrame playAreaFrame = new JFrame("Play area size setting..."); // play area setting window.
                playAreaFrame.setSize(320,300);

                //set labels
                JLabel wblock = new JLabel("WIDTH:");
                wblock.setBounds(25,40, 100,50);
                wblock.setFont(new Font("para",Font.BOLD,15));

                JLabel hblock = new JLabel("HEIGHT:");
                hblock.setBounds(25,110,100,50);
                hblock.setFont(new Font("para",Font.BOLD,15));

                JSlider wSlider = new JSlider(10,40,10);
                wSlider.setBounds(90,50,200,40);
                wSlider.setPaintTicks(true);
                wSlider.setPaintLabels(true);
                wSlider.setSnapToTicks(true);
                wSlider.setMajorTickSpacing(10);
                wSlider.setMinorTickSpacing(10);
                playAreaFrame.add(wSlider);

                JSlider hSlider = new JSlider(20,40,20);
                hSlider.setBounds(90,120,200,40);
                hSlider.setPaintTicks(true);
                hSlider.setPaintLabels(true);
                hSlider.setSnapToTicks(true);
                hSlider.setMajorTickSpacing(10);
                hSlider.setMinorTickSpacing(10);
                playAreaFrame.add(hSlider);

                //set buttons
                JButton cancel = new JButton("CANCEL");
                cancel.setBounds(50,200, 100,40);
                cancel.setFont(new Font("font",Font.BOLD,15));
                cancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gameRestart();
                        playAreaFrame.dispose();
                    }
                });

                JButton submit = new JButton("SUBMIT");
                submit.setBounds(170,200,100,40);
                submit.setFont(new Font("font",Font.BOLD,15));
                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainWidth = wSlider.getValue();
                        mainHeight = hSlider.getValue();
                        mainArea.setMapW(mainWidth);
                        mainArea.setMapH(mainHeight);
                        gameData = new GameData(mainWidth,mainHeight);
                        mainArea.setGameData(gameData);
                        nextArea.setGameData(gameData);
                        dataArea.setGameData(gameData);
                        gameFunction.setGameData(gameData);
                        jf.setSize(pixelSize*(mainWidth+13),pixelSize*(mainHeight+4));
                        gameRestart();
                        playAreaFrame.dispose();
                    }
                });

                // add all components
                playAreaFrame.add(wblock);
                playAreaFrame.add(hblock);
                playAreaFrame.add(cancel);
                playAreaFrame.add(submit);

                playAreaFrame.setLayout(null);
                playAreaFrame.setLocationRelativeTo(null);
                playAreaFrame.setResizable(false);
                playAreaFrame.setVisible(true);
            }
        });

        blockSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePause();

                JFrame tsFrame = new JFrame("Tetromino size setting..."); // tetromino size setting window.
                tsFrame.setSize(400,200);

                JLabel blockS = new JLabel("BLOCK SIZE:");
                blockS.setBounds(25,25, 150,50);
                blockS.setFont(new Font("para",Font.BOLD,15));

                JSlider blockSlider = new JSlider(10,50,30);
                blockSlider.setBounds(150,35,200,40);
                blockSlider.setPaintTicks(true);
                blockSlider.setPaintLabels(true);

                Hashtable<Integer, JLabel> hashtable = new Hashtable<>();
                hashtable.put(10,new JLabel("5"));
                hashtable.put(20,new JLabel("10"));
                hashtable.put(30,new JLabel("20"));
                hashtable.put(40,new JLabel("40"));
                hashtable.put(50,new JLabel("50"));
                blockSlider.setLabelTable(hashtable);
                blockSlider.setSnapToTicks(true);
                blockSlider.setMajorTickSpacing(10);
                tsFrame.add(blockSlider);

                //set buttons
                JButton cancel = new JButton("CANCEL");
                cancel.setFont(new Font("font",Font.BOLD,15));
                cancel.setBounds(50,100, 100,40);

                cancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gameRestart();
                        tsFrame.dispose();
                    }
                });

                JButton submit = new JButton("SUBMIT");
                submit.setBounds(240,100,100,40);
                submit.setFont(new Font("font",Font.BOLD,15));
                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int key = blockSlider.getValue();
                        int newBlockSize = Integer.parseInt(hashtable.get(key).getText());
                        mainArea.setBlockSize(newBlockSize);
                        mainWidth = (int)(Math.ceil(mainArea.getWidth()*1.0/newBlockSize));
                        mainHeight =(int)(Math.ceil(mainArea.getHeight()*1.0/newBlockSize));
                        gameData = new GameData(mainWidth,mainHeight);
                        mainArea.setGameData(gameData);
                        nextArea.setGameData(gameData);
                        dataArea.setGameData(gameData);
                        gameFunction.setGameData(gameData);
                        gameRestart();
                        tsFrame.dispose();
                    }
                });

                tsFrame.add(blockS);
                tsFrame.add(cancel);
                tsFrame.add(submit);
                tsFrame.setLayout(null);
                tsFrame.setLocationRelativeTo(null);
                tsFrame.setResizable(false);
                tsFrame.setVisible(true);
            }
        });

        JMenu createShape = new JMenu("Add New Shape");
        createShape.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gamePause();
            }
        });

        JMenuItem addNewShape = new JMenuItem("Add new shapes...");
        createShape.add(addNewShape);

        addNewShape.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePause();
                new AddNewShapes(mainWidth,mainHeight,gameData,nextArea,timer,mainArea);
            }
        });


        menuBar.add(set);
        menuBar.add(createShape);
        menuBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gamePause();
            }
        });
    }

    public static void main(String[] args) {
        new Tetris();
    }

    public void gamePause(){
        timer.stop();
        gameData.setCanRotate(false);
        mainArea.menuOpen =true;
        mainArea.setPause(true);
        mainArea.repaint();
    }

    public void gameRestart(){
        timer.start();
        gameData.setCanRotate(true);
        mainArea.menuOpen = false;
        mainArea.setPause(false);
        mainArea.repaint();
    }
}