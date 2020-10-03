package GraphAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphVisualizingScreen {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 728;
    public static final int win_WIDTH = 1000;
    public static final int gridWIDTH = 990;    //Width of grid
    public static final int gridHEIGHT = 630;   //Height of grid
    public static final int gridSIZE = 30;      //Size of each grid
    public static final int gridRows = gridHEIGHT/gridSIZE ;      //Rows in grid
    public static final int gridCols = gridWIDTH/gridSIZE;           //Columns in grid

    public Color themeColor  = new Color(110,217,161);

    JFrame f;
    JPanel p,bp, pp;
    JButton sBtn, rBtn, stBtn;
    JLabel statusText, speedText, speedSlider, instructionText;
    JLabel iterationText, pathLengthText, infoText;
    JComboBox<String> algorithmSelection;
    GraphVisualize gv;

    String[] algorithms = {
            "Breadth First Search",
            "Depth First Search",
            "Dijkstra (Not added)",
    };

    public Point mousePos;

    public String mainFont = "Century Gothic";

    public GraphVisualizingScreen(){
        gv = new GraphVisualize();

        f = new JFrame("Graph Visualization");
        f.setSize(WIDTH, HEIGHT);   //Setting dimensions
        f.setLayout(null);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //Will close when close BUTTON pressed
        f.setResizable(false);


        pp = new JPanel();
        pp.setBounds(0,gridHEIGHT, gridWIDTH, HEIGHT-gridHEIGHT);
        pp.setBackground(Color.darkGray);
        pp.setLayout(null);
        pp.setVisible(true);

        infoText = new JLabel("<html><font color='#D63031'>Start&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>" + " " +
                "<font color='#121212'>Wall&nbsp;&nbsp;&nbsp;</font>" + " " +
                "<font color='#74B9FF'>Visiting&nbsp;&nbsp;&nbsp;</font>" + " " +
                "<font color='#6C5CE7'>Target&nbsp;&nbsp;&nbsp;</font>" +
                "<font color='#FDFD96'>Path</font>" + " " +
                "</html>");
        infoText.setBounds(25, 1, 300, 50);
        infoText.setFont(new Font(mainFont, Font.BOLD, 18));

        iterationText = new JLabel("");
        iterationText.setBounds(280, 5, 180, 50);
        iterationText.setFont(new Font(mainFont, Font.BOLD, 18));
        iterationText.setForeground(Color.white);

        pathLengthText = new JLabel("");
        pathLengthText.setBounds(430, 5, 180, 50);
        pathLengthText.setFont(new Font(mainFont, Font.BOLD, 18));
        pathLengthText.setForeground(Color.white);

        p = new JPanel();
        p.setBounds(0,0,gridWIDTH, gridHEIGHT);
        p.setLayout((new GridLayout(1,1 )));
        p.add(gv);
        p.setVisible(true);
        p.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //if (!e.isAltDown() && !e.isShiftDown())
                    gridPaint(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
        p.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isAltDown() || e.isShiftDown())
                {
                    //gridPaint2(e);
                }
                    gridPaint(e);


            }

            @Override
            public void mousePressed(MouseEvent e) {
                //if (!e.isAltDown() && !e.isShiftDown())
                    gridPaint(e);

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                gridPaint(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                gv.mPos = new Point(0,0);
            }
        });
        p.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.isControlDown())
                {
                    if (e.getWheelRotation() < 0) {
                        gv.getAnimSpeed+= 100;
                    } else {
                        if (gv.getAnimSpeed - 100 >= 1)    gv.getAnimSpeed-= 100;
                        else    gv.getAnimSpeed = 1;
                    }
                }else if(e.isShiftDown()){
                    if (e.getWheelRotation() < 0) {
                        gv.getAnimSpeed+= 10;
                    } else {
                        if (gv.getAnimSpeed - 10 >= 1)    gv.getAnimSpeed-= 10;
                        else    gv.getAnimSpeed = 1;
                    }
                }else{
                    if (e.getWheelRotation() < 0) {
                        gv.getAnimSpeed+= 1;
                    } else {
                        if (gv.getAnimSpeed - 1 >= 1)    gv.getAnimSpeed-= 1;
                    }
                }
                speedText.setText( "Speed: " + gv.getAnimSpeed + " ms");
            }
        });

        bp = new JPanel();
        bp.setBounds(gridWIDTH,0,WIDTH-gridWIDTH, HEIGHT);
        bp.setBackground(Color.darkGray);
        bp.setLayout(null);
        bp.setVisible(true);


        instructionText = new JLabel("<html>Hold the Alt key and click on a point to set the target point<br>" +
                "Click while holding Shift key to set the source point<br>" +
                "Use the left and right mouse button to add and remove obstacles</html>");
        instructionText.setBounds((WIDTH-gridWIDTH -180)/2, 8, 180, 150);
        instructionText.setFont(new Font(mainFont, Font.PLAIN, 13));
        instructionText.setForeground(Color.white);

        statusText = new JLabel("<html>Press Find to start pathfinding</html>");
        statusText.setBounds((WIDTH-gridWIDTH -180)/2, 150, 180, 80);
        statusText.setFont(new Font(mainFont, Font.BOLD, 18));
        statusText.setForeground(Color.white);

        stBtn = new JButton("Find");
        stBtn.setBounds((WIDTH-gridWIDTH -180)/2, 235, 180,50);
        stBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rBtn.setEnabled(false);
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    public Void doInBackground() {
                        if (gv.willFind){
                            System.out.println("Stopped");
                            rBtn.setEnabled(true);
                            if (gv.status){
                                statusText.setText("Path found");
                                stBtn.setText("Find");
                            }else{
                                statusText.setText("No path found");
                                stBtn.setText("Find");
                            }
                            gv.willFind = false;
                        }else
                        {
                            statusText.setText("Pathfinding...");
                            gv.willAnimate = false;
                            gv.willFind = true;
                            gv.iterations = 0;
                            gv.pathPlace = 0;
                            iterationText.setText("");
                            pathLengthText.setText("");
                            if (algorithmSelection.getSelectedItem() == "Depth First Search"){
                                new DepthFirstSearch(gv, new Point(gv.sourceX, gv.sourceY));
                            }else if (algorithmSelection.getSelectedItem() == "Breadth First Search"){
                                new BreadthFirstSearch(gv);
                            }
                            gv.willFind = false;
                        }
                        return null;
                    }
                    @Override
                    protected void done() {
                        System.out.println("Done");
                        rBtn.setEnabled(true);
                        if (gv.status){
                            statusText.setText("Path found");
                            iterationText.setText("Iterations: " + gv.iterations);
                            pathLengthText.setText("Path length: " + gv.pathPlace);
                            stBtn.setText("Find");
                        }else{
                            statusText.setText("No path found");
                            iterationText.setText("Iterations: " + gv.iterations);
                            stBtn.setText("Find");
                        }
                    }
                };
                worker.execute();
            }
        });
        stBtn.setBackground(themeColor);
        stBtn.setFont(new Font(mainFont, Font.BOLD, 20));
        stBtn.setForeground(Color.white);
        stBtn.setFocusable(false);
        stBtn.setBorder(null);
        stBtn.setVisible(true);
        //When the button is hovered
        stBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (stBtn.isEnabled())
                    stBtn.setBackground(themeColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                stBtn.setBackground(themeColor);
            }
        });




        sBtn = new JButton("Animate");
        sBtn.setBounds((WIDTH-gridWIDTH -180)/2, 300, 180,50);
        sBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rBtn.setEnabled(false);
                stBtn.setEnabled(false);
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    public Void doInBackground() {
                        if (gv.willFind){
                            System.out.println("Stopped");
                            rBtn.setEnabled(true);
                            stBtn.setEnabled(true);
                            if (gv.status){
                                statusText.setText("Path found");
                                iterationText.setText("Iterations: " + gv.iterations);
                                pathLengthText.setText("Path length: " + gv.pathPlace);
                                sBtn.setText("Animate");
                            }else{
                                statusText.setText("No path found");
                                iterationText.setText("Iterations: " + gv.iterations);
                                sBtn.setText("Animate");
                            }
                            gv.willFind = false;
                        }else
                        {
                            statusText.setText("Pathfinding...");
                            sBtn.setText("Stop");
                            gv.willAnimate = true;
                            gv.willFind = true;
                            gv.iterations = 0;
                            gv.pathPlace = 0;
                            iterationText.setText("");
                            pathLengthText.setText("");
                            if (algorithmSelection.getSelectedItem() == "Depth First Search"){
                                new DepthFirstSearch(gv, new Point(gv.sourceX, gv.sourceY));
                            }else if (algorithmSelection.getSelectedItem() == "Breadth First Search"){
                                new BreadthFirstSearch(gv);
                            }


                            gv.willFind = false;
                        }


                        return null;
                    }
                    @Override
                    protected void done() {
                        System.out.println("Done");
                        rBtn.setEnabled(true);
                        stBtn.setEnabled(true);
                        if (gv.status){
                            statusText.setText("Path found");
                            iterationText.setText("Iterations: " + gv.iterations);
                            pathLengthText.setText("Path length: " + gv.pathPlace);
                            sBtn.setText("Animate");
                        }else{
                            statusText.setText("No path found");
                            iterationText.setText("Iterations: " + gv.iterations);
                            sBtn.setText("Animate");
                        }
                    }
                };
                    worker.execute();




            }
        });
        sBtn.setBackground(themeColor);
        sBtn.setFont(new Font(mainFont, Font.BOLD, 20));
        sBtn.setForeground(Color.white);
        sBtn.setFocusable(false);
        sBtn.setBorder(null);
        sBtn.setVisible(true);
        //When the button is hovered
        sBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (sBtn.isEnabled())
                    sBtn.setBackground(themeColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                sBtn.setBackground(themeColor);
            }
        });

        rBtn = new JButton("Reset");
        rBtn.setBounds((WIDTH-gridWIDTH -180)/2, 365, 180,50);
        rBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                gv.iterations = 0;
                gv.pathPlace = 0;
                iterationText.setText("");
                pathLengthText.setText("");
                sBtn.setEnabled(false);
                rBtn.setEnabled(false);
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    public Void doInBackground() {
                        gv.drawGrid();

                        return null;
                    }
                    @Override
                    protected void done() {
                        System.out.println("Done");
                        statusText.setText("<html>Press Find to start pathfinding</html>");
                        sBtn.setText("Animate");
                        sBtn.setEnabled(true);
                        rBtn.setEnabled(true);
                    }
                };
                worker.execute();
            }
        });
        rBtn.setBackground(themeColor);
        rBtn.setFont(new Font(mainFont, Font.BOLD, 20));
        rBtn.setForeground(Color.white);
        rBtn.setFocusable(false);
        rBtn.setBorder(null);
        rBtn.setVisible(true);
        //When the button is hovered
        rBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (rBtn.isEnabled())
                    rBtn.setBackground(themeColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                rBtn.setBackground(themeColor);
            }
        });

        algorithmSelection = new JComboBox<String>(algorithms);
        algorithmSelection.setBounds((WIDTH-gridWIDTH -180)/2, 430, 180, 50);
        algorithmSelection.setFont(new Font(mainFont, Font.BOLD, 15));
        algorithmSelection.setBackground(themeColor);
        algorithmSelection.setForeground(Color.white);
        algorithmSelection.setRenderer(new MyListCellRenderer(themeColor));

        algorithmSelection.setSelectedIndex(0);
        algorithmSelection.setVisible(true);



        speedSlider = new JLabel("<html>Use the mouse scroll wheel to change the animation delay.<br>" +
                " Use the Ctrl or Shift keys along with the scroll wheel to change by bigger amount.</html>");
        speedSlider.setBounds((WIDTH-gridWIDTH -180)/2, 500, 180, 120);
        speedSlider.setFont(new Font(mainFont, Font.PLAIN, 13));
        speedSlider.setForeground(Color.white);

        speedText = new JLabel( "Speed: " + gv.getAnimSpeed + " ms");
        speedText.setBounds((WIDTH-gridWIDTH -180)/2, 615, 180, 50);
        speedText.setFont(new Font(mainFont, Font.PLAIN, 18));
        speedText.setForeground(Color.white);

        bp.add(instructionText);
        bp.add(statusText);
        bp.add(stBtn);
        bp.add(sBtn);
        bp.add(rBtn);
        bp.add(algorithmSelection);
        bp.add(speedSlider);
        bp.add(speedText);

        pp.add(iterationText);
        pp.add(pathLengthText);
        pp.add(infoText);


        f.add(p);
        f.add(bp);
        f.add(pp);

        f.setVisible(true);

    }

    public static void main(String[] args) {
        GraphVisualizingScreen gvs = new GraphVisualizingScreen();
    }

    public void gridPaint(MouseEvent e){
        if (e.getPoint().x >= 32 && e.getPoint().y >= 32 &&
                e.getPoint().x <= 958 && e.getPoint().y <= 598 && !gv.willFind){
            if (e.getButton() == MouseEvent.BUTTON1 && e.isShiftDown()) gv.clickState = 2;
            else if (e.getButton() == MouseEvent.BUTTON1 && e.isAltDown()) gv.clickState = 4;
            else if (e.getButton() == MouseEvent.BUTTON1) gv.clickState = 1;
            else if (e.getButton() == MouseEvent.BUTTON3) gv.clickState = 0;
            mousePos = e.getPoint();
            gv.mPos = mousePos;
            gv.Update();
        }
    }
    public void gridPaint2(MouseEvent e){
        if (e.getPoint().x >= 32 && e.getPoint().y >= 32 &&
                e.getPoint().x <= 958 && e.getPoint().y <= 658){
            if (e.getButton() == MouseEvent.BUTTON1 && e.isShiftDown()) gv.clickState = 2;
            else if (e.getButton() == MouseEvent.BUTTON1 && e.isAltDown()) gv.clickState = 4;
            mousePos = e.getPoint();
            gv.mPos = mousePos;
            gv.Update();
        }
    }
}
//Custom dropdown menu settings
class MyListCellRenderer extends DefaultListCellRenderer {
    public Color themeColor;

    public MyListCellRenderer(Color t) {
        setOpaque(true);  //Not transparent
        this.themeColor = t;
    }

    public Component getListCellRendererComponent(JList jc, Object val, int idx,
                                                  boolean isSelected, boolean cellHasFocus) {
        setText(val.toString());        //Set the texts to string
        setForeground(Color.white);     //Text color to white
        jc.setSelectionBackground(themeColor);      //Sets the BG of selected item
        jc.setSelectionForeground(Color.white);     //Sets the font color of selected item

        if (isSelected)
            setBackground(themeColor);      //Sets the BG of hovered item
        else
            setBackground(Color.darkGray);      //Sets the BG of non hovered items
        return this;
    }
}