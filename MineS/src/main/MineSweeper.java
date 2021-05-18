package main;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class MineSweeper extends JFrame {

    private JPanel gamePanel;
    private JPanel hudPanel;

    private JButton resetButton;

    private JButton[][] matrix;

    private boolean minesMatrix[][];

    private ImageIcon smileyIcon; //aici tinem iconita
    private ImageIcon deadIcon;

    private static final int[] I = {-1,-1,0,1,1,1,0,-1};
    private static final int[] J = {0,1,1,1,0,-1,-1,-1};
    public MineSweeper(){
        setTitle("Mine Sweeper");
        initComponents();

        setSize(700,700);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void initComponents(){
        gamePanel = new JPanel();

        initHudPanel();
        initGame(10,10);
        add(hudPanel, BorderLayout.NORTH); // adaugam panoul pe fereastra in partea de sus
        add(gamePanel);

    }

    private void initHudPanel(){
        resetButton = new JButton(); //instantiem butonul de reset
        //ADAUGA ICONITA PE BUTON
        Image scaledImage = new ImageIcon("./smiley.png")  //scalam iconitele ca sa le inghesuim pe buton
                .getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH);
        smileyIcon = new ImageIcon(scaledImage);

        scaledImage = new ImageIcon("./dead.png")  //scalam iconitele ca sa le inghesuim pe buton
                .getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH);
        deadIcon = new ImageIcon(scaledImage);

        resetButton.setIcon(smileyIcon);

        resetButton.addActionListener(ev -> {
            int rows = matrix.length;
            int columns = matrix[0].length;

            initGame(rows,columns);
        });


        hudPanel = new JPanel(); //instantiem panoul hud
        LayoutManager manager = new FlowLayout();  //FlowLayout va face butonul sa fie plutitor
        hudPanel.setLayout(manager); // setez layout ul nostru pe panou
        hudPanel.add(resetButton);
    }

    private void initGame(int rows, int columns){
        matrix = new JButton[rows][columns];// instantiem matricea de butoane
        resetButton.setIcon(smileyIcon);

        gamePanel.removeAll();
        gamePanel.setLayout(new GridLayout(rows,columns));
        for (int i=0; i < rows; i++ ){
            for(int j=0; j<columns; j++){
                matrix[i][j] = new JButton();
                gamePanel.add(matrix[i][j]);

                int row = i;
                int column = j;
                matrix[i][j].addActionListener(ev ->clickButton(row, column));
            }
        }
        gamePanel.revalidate();
        generateMines(rows,columns);


    }
    private void generateMines(int rows, int columns){
        Random random = new Random();

        minesMatrix = new boolean[rows][columns]; // matricea care retine daca la poz i si j exista o mina
        int mines = rows * columns / 6; // un buton din 6 are mina
        int count = 0; //nr de mine care sunt generate

        int i;
        int j;
        while(count < mines){

            i= random.nextInt(rows);
            j= random.nextInt(columns);

            if(!minesMatrix[i][j]){

                minesMatrix[i][j] = true;
                count++;
            }
        }

    }
    private void showMines(){
        int rows = matrix.length;
        int columns = matrix[0].length;

        for(int i = 0; i < rows; i++ ){

            for(int j=0; j< columns; j++){

                if(minesMatrix[i][j]){
                    JButton button = matrix[i][j];
                    button.setBackground(Color.RED);
                }
            }
        }

    }

    private void clickButton(int i, int j){
        if(minesMatrix[i][j]){
            gameOver();
            return;
        }
        expose(i,j);


    }
    private void gameOver(){
        showMines();
        resetButton.setIcon(deadIcon);
        JOptionPane.showMessageDialog(this, "You Lost :(");

    }
    private void expose(int i, int j){

        if(!matrix[i][j].isEnabled()){  //see if we ve been previously on this button
            return;
        }
        int minesCount = countMines(i,j);
        matrix[i][j].setEnabled(false); //set it as visited
        if(minesCount != 0){

            matrix[i][j].setText(String.valueOf(minesCount)); //set the text(how many mines are around)
        }else{
            int rows = matrix.length;
            int columns = matrix[0].length;

            for(int k=0; k < I.length; k++){
                int newI = i+I[k];
                int newJ = j+J[k];


                if(newI < 0 || newI >= rows ){
                    continue;
                }
                if(newJ < 0 || newJ >= columns ){
                    continue;
                }
                expose(newI,newJ);

            }
        }
    }
    private int countMines(int i, int j){  // cate mine sunt in jurul butonului
        int rows = matrix.length;
        int columns = matrix[0].length;
        int count =0;
        for(int k=0; k < I.length; k++){
            int newI = i+I[k];
            int newJ = j+J[k];


            if(newI < 0 || newI >= rows ){
                continue;
            }
            if(newJ < 0 || newJ >= columns ){
                continue;
            }
            if(minesMatrix[newI][newJ]){
                count++;
            }
        }
        return count;

    }

    public static void main(String[] args) {

        new MineSweeper();
    }
}


