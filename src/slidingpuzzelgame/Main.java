package slidingpuzzelgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is a 15 piece sliding puzzle game.
 * Challenge 001 out of Pro/g/ramming Challenges v3.0 
 * @author Emil.Karpowicz
 */

public class Main extends JFrame 
{
    private static int scrWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static int scrHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
    
    public static int cubit = scrWidth / 15;
    
    public static int frameWidth = 4 * cubit; 
    public static int frameHeight = 5 * cubit; 
    
    private static JPanel mPanel = new JPanel(); // main panel
    public  static JPanel gPanel = new JPanel(); // game panel
    private static JPanel cPanel = new  JPanel(); // control panel
    private static JPanel sPanel = new  JPanel(); // score panel
    
    private static CustomButton bNewGame = new CustomButton("New Game"); 
    private static CustomButton bResetGame = new CustomButton("Reset Game"); 
    private static CustomButton bExitGame = new CustomButton("Exit Game"); 
    private static int time = 0;
    private static int moves = 0;
    private static CustomLabel lTimePassed = new CustomLabel("Time passed: " + time + " sec.             ");
    private static CustomLabel lMoveCount = new CustomLabel("Number of moves: " + moves);

    private static Puzzle[][] puzzleMatrix = new Puzzle[4][4];
    
    private static Thread countTime = new Thread(new TimePlaied());
    
    public Main()
    {
        this.setTitle("SlidinFg Puzzle");
        this.setBounds((scrWidth-frameWidth)/2,(scrHeight - frameHeight)/2,frameWidth,frameHeight);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        this.getContentPane().add(mPanel);
        mPanel.setLayout(new BorderLayout());
        mPanel.add(sPanel, BorderLayout.NORTH);
        sPanel.setBorder(BorderFactory.createBevelBorder(0));
        sPanel.add(lTimePassed);
        sPanel.add(lMoveCount);
        mPanel.add(gPanel, BorderLayout.CENTER);
        gPanel.setBorder(BorderFactory.createBevelBorder(0));
        gPanel.setLayout(new GridLayout(4, 4));
        mPanel.add(cPanel, BorderLayout.SOUTH);
        cPanel.setBorder(BorderFactory.createBevelBorder(0));
        cPanel.add(bNewGame);
        cPanel.add(bResetGame);
        cPanel.add(bExitGame);
    }
    
    public static void startCountingTime()
    {
        countTime.start();
    }
    
    public static void addMove()
    {
        moves++;
        lMoveCount.setText("Number of moves: " + moves);
    }
    
    public static void addTime()
    {
        time++;
        lTimePassed.setText("Time passed: " + time + " sec.             ");
    }
    
    public static int getTime()
    {
        return time;
    }
    
    public static int getMove()
    {
        return moves;
    }
    
    public static void repaintGamePanel()
    {
        gPanel.repaint();
    }
    
    public static void revalidateGamePanel()
    {
        gPanel.revalidate();
    }
    
    public static void addToGamePanel(Puzzle puzzle)
    {
        gPanel.add(puzzle);
    }
    public static void main(String[] args) 
    {
        new Main().setVisible(true);
    }
    
}

class TimePlaied implements Runnable
{
    @Override
    public void run() 
    {
        
        try 
        {
            while (!Thread.currentThread().isInterrupted())
            {
            Main.addTime();
            Thread.sleep(1000);
            }
        } 
        catch (InterruptedException ex) 
        {
            System.out.println(ex.getMessage());
        }
    }
    
}

class PuzzleMatrix  
{
    private static Puzzle[][] puzzleMatrix = new Puzzle[4][4];
    private static int emptyX;
    private static int emptyY;
    
    public PuzzleMatrix()
    {
        
    }
    
    public static void havePlayerWon()
    {
        int tmp = 1;
        int tmp2 = 0;
        int tmp3;
        
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
            {
                if (puzzleMatrix[i][j].getText() == "") tmp3 = 0;
                else tmp3 = Integer.parseInt(puzzleMatrix[i][j].getText());
                
                if (tmp3 == tmp) tmp2++;
                tmp++;
            }
        if (tmp2 == 15) JOptionPane.showMessageDialog(null, "Brawo!. Ułożyłeś puzzle po "+ Main.getTime()+ " sekundach i "+ Main.getMove()+ " ruchach.");
    }
    
    public static int getEmptyX()
    {
        return emptyX;
    }
    
    public static int getEmptyY()
    {
        return emptyY;
    }
    
    public static void changeEmptySlot(int x, int y)
    {
        emptyX = x;
        emptyY = y;
    }
    
    
    public static void addToPuzzleMatrix(int xCoordinate, int yCoordinate, String pID)
    {
        puzzleMatrix[xCoordinate][yCoordinate] = new Puzzle(xCoordinate, yCoordinate, pID);
    }
    
    public static void deletePuzzleFromPuzzleMatrix(int xCoordinate, int yCoordinate)
    {
        puzzleMatrix[xCoordinate][yCoordinate] = null;
    }
    
    public static Puzzle getPuzzleFromPuzzleMatrix(int xCoordinate, int yCoordinate)
    {
        return puzzleMatrix[xCoordinate][yCoordinate];
    }
    
    public static synchronized void moveEmptyPuzzle(int x, int y)
    {
        if (PuzzleMatrix.getEmptyX() + x < 0 || PuzzleMatrix.getEmptyX() + x > 3 || PuzzleMatrix.getEmptyY() + y < 0 || PuzzleMatrix.getEmptyY() + y > 3)
        {
            System.out.println("Poza zasiegiem");
        }
        else
        {
            
        puzzleMatrix[PuzzleMatrix.getEmptyX()][PuzzleMatrix.getEmptyY()].setText(puzzleMatrix[PuzzleMatrix.getEmptyX()+x][PuzzleMatrix.getEmptyY()+y].getText());
        puzzleMatrix[PuzzleMatrix.getEmptyX()][PuzzleMatrix.getEmptyY()].setBackground(Color.LIGHT_GRAY);
        
        puzzleMatrix[PuzzleMatrix.getEmptyX()+x][PuzzleMatrix.getEmptyY()+y].setText("");
        puzzleMatrix[PuzzleMatrix.getEmptyX()+x][PuzzleMatrix.getEmptyY()+y].setBackground(Color.BLUE);
        
        PuzzleMatrix.changeEmptySlot(PuzzleMatrix.getEmptyX() + x, PuzzleMatrix.getEmptyY() + y);
        }

    }
}

class Puzzle extends JLabel
{
    String puzzleID;
    int xCoordinate;
    int yCoordinate;
    
    public Puzzle(int xCoordinate, int yCoordinate, String puzzleID)
    {
        super(puzzleID, HORIZONTAL);
        this.puzzleID = puzzleID;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.setLocation(xCoordinate*Main.cubit, yCoordinate*Main.cubit);
        this.setPreferredSize(new Dimension(Main.cubit, Main.cubit));
        this.setBorder(BorderFactory.createBevelBorder(0));
        if (this.puzzleID == "") this.setBackground(Color.BLUE);
        else this.setBackground(Color.LIGHT_GRAY);
        
    }
    
}

class CustomButton extends JButton
{
    private String buttonLabel;
    
    CustomButton(String buttonLabel)
    {
        super(buttonLabel);
        this.buttonLabel = buttonLabel;
        this.setPreferredSize(new Dimension(Main.frameWidth / 4 , Main.cubit / 4));
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if (((CustomButton)e.getSource()).getName() == "New Game") newGame();
                if (((CustomButton)e.getSource()).getName() == "Reset Game") resetGame();
                if (((CustomButton)e.getSource()).getName() == "Exit Game") exitGame();
            }
        });
        
        this.addKeyListener(new KeyAdapter() 
        {
            @Override
            public void keyPressed(KeyEvent e) 
            {
                
                if (e.getKeyCode() == KeyEvent.VK_UP) 
                {
                    PuzzleMatrix.moveEmptyPuzzle(-1, 0);
                    Main.addMove();
                    PuzzleMatrix.havePlayerWon();
                }
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) 
                {
                    PuzzleMatrix.moveEmptyPuzzle(1, 0);
                    Main.addMove();
                    PuzzleMatrix.havePlayerWon();
                }
                else if (e.getKeyCode() == KeyEvent.VK_LEFT) 
                {
                    PuzzleMatrix.moveEmptyPuzzle(0, -1);
                    Main.addMove();
                    PuzzleMatrix.havePlayerWon();
                }
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT) 
                {
                    PuzzleMatrix.moveEmptyPuzzle(0, 1);
                    Main.addMove();
                    PuzzleMatrix.havePlayerWon();
                }
            }
        });
    }
    
    public void newGame()
    {
        int pID;
        Set<Integer> randomNumberMatrix = new TreeSet<Integer>();
        System.out.println("New Game");
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
            {
                do
                {
                    pID = RanodomNumber.RandomNumber(0, 15);
                }
                while (randomNumberMatrix.contains(pID));
                randomNumberMatrix.add(pID);
                if (pID == 0) 
                {
                    PuzzleMatrix.changeEmptySlot(i, j);
                    PuzzleMatrix.addToPuzzleMatrix(i, j, "");
                }
                else PuzzleMatrix.addToPuzzleMatrix(i, j, ""+pID);
                Main.addToGamePanel(PuzzleMatrix.getPuzzleFromPuzzleMatrix(i, j));
            }
        Main.startCountingTime();
        Main.revalidateGamePanel();
    }
    
    public void resetGame()
    {
        System.out.println("Reset Game");
        Main.gPanel.removeAll();
        Main.repaintGamePanel();
        newGame();
    }
    
    public void exitGame()
    {
        System.out.println("Exit Game");
        System.exit(1);
    }
    
    @Override
    public String getName()
    {
        return buttonLabel;
    }
}

class RanodomNumber
{
    private static int randomNumber;
    
    public static int RandomNumber(int Min, int Max)
    {
        randomNumber = Min + (int)(Math.random()*((Max - Min)+1));
        
        return randomNumber;
    }
}

class CustomLabel extends JLabel
{
    
    public CustomLabel(String label)
    {
        super(label);
        this.setFont(new Font("Arial", TOP, 15));
    }
    
    
}