
package SnakeGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;  
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

class GamePanel extends JPanel{
    Image snakeheadimg,snaketailimg,ratimg;
    static int ratposx,ratposy;
    
    GamePanel(){
        snakeheadimg = new ImageIcon(
                getClass().getResource("head.png")
        ).getImage(); 
        snaketailimg = new ImageIcon(
                getClass().getResource("tail.png")
        ).getImage(); 

        ratimg = new ImageIcon(
                getClass().getResource("rat.png")
        ).getImage();
        locateRat();
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);        
        g.drawImage(ratimg,ratposx,ratposy,40,40,this);
        for(int i=(demo1.lengthOfSnake)-1;i>=0;i--){
            if(i==0){
                g.drawImage(snakeheadimg,demo1.xPos[i],demo1.yPos[i],demo1.mouthSize,demo1.mouthSize,this);
            }else{
                g.drawImage(snaketailimg,demo1.xPos[i],demo1.yPos[i],demo1.mouthSize,demo1.mouthSize,this);
            }
        }
    }
     public static void locateRat(){
        ratposx = (int)(Math.random() * (700 / demo1.mouthSize)) * demo1.mouthSize;
        ratposy = (int)(Math.random() * (520 / demo1.mouthSize)) * demo1.mouthSize;
    }
}

public class demo1 extends JFrame implements KeyListener,ActionListener{
   
    JPanel mainPanel,panel1;
    boolean leftMove = false,rightMove = true,upMove = false,downMove = false;
    Timer timer;
    JButton restartbtn;
    static int xPos[]=new int[1000];
    static int yPos[]=new int[1000];
    static int mouthSize = 20;
    static int lengthOfSnake = 3;
    GamePanel boardPanel;
    JLabel lbl1,lbl2;
    int score = 0;
    boolean gameOver = false;
    demo1(){
        setBounds(10,10,800,700);
        setLocationRelativeTo(null);
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        requestFocusInWindow();
        addKeyListener(this);
        setFocusable(true);
        requestFocus();
                
        panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(this.getWidth(),80));
        panel1.setBackground(Color.BLUE);
        panel1.setLayout(null);
        lbl1 = new JLabel("Snake Game");
        lbl1.setBounds(2,15, getWidth(),40);
        lbl1.setHorizontalAlignment(JLabel.CENTER);
        lbl1.setForeground(Color.WHITE);
        lbl1.setFont(new Font("Arial",Font.BOLD,40));
        panel1.add(lbl1);
        
        
        lbl2 = new JLabel("Score : 0");
        lbl2.setBounds(670,50,500,30);
        lbl2.setForeground(Color.YELLOW);
        lbl2.setFont(new Font("Arial",Font.BOLD,20));
        panel1.add(lbl2);
        
        restartbtn = new JButton("Restart");
        restartbtn.setBounds(20,47,80,30);
        restartbtn.addActionListener(this);
        panel1.add(restartbtn);
        
        boardPanel = new GamePanel();
        boardPanel.setBackground(Color.BLACK);
        boardPanel.addKeyListener(this);
        
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel1,BorderLayout.NORTH);
        mainPanel.add(boardPanel,BorderLayout.CENTER);
        
        timer = new Timer(170,e->{
            move();
        });
        
        for(int i = 0; i < lengthOfSnake; i++){
            xPos[i] = 80 - (i * mouthSize);
            yPos[i] = mouthSize;
        }

        add(mainPanel);
        setVisible(true);
        boardPanel.requestFocusInWindow();
    }
    public void move(){
       for(int i=lengthOfSnake-1;i>0;i--){
            xPos[i] = xPos[i-1];
            yPos[i] = yPos[i-1]; 
        }
        
        if(leftMove){
            xPos[0] -= mouthSize;
        }
        if(rightMove){
            xPos[0] += mouthSize;
        }
        if(upMove){
            yPos[0] -= mouthSize;
        }
        if(downMove){
            yPos[0] += mouthSize;
        }
        System.out.println(GamePanel.ratposx+" "+xPos[0]);
        isCollidedWithRat();
        isCollidedWithWalls();
        isCollidedWithSelf();
        boardPanel.repaint();
    }
    private void OverTheGame(){
        if(gameOver==true){
            return;
        }
        gameOver = true;
        timer.stop();
        int x = JOptionPane.showConfirmDialog(this,
              "Game Over !",
              "Do u want to play again ? ",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.INFORMATION_MESSAGE);
        if(x==JOptionPane.YES_OPTION){
            restartGame();
            return;
        }else{
            timer.stop();
            this.dispose();
        }
      gameOver = true;
    }
    private void isCollidedWithSelf(){
        for(int i =1;i<lengthOfSnake;i++){
           if(xPos[i]==xPos[0] && yPos[i]==yPos[0]){
                OverTheGame();
            }
        }
        
    }
    public void isCollidedWithWalls(){
        if(gameOver==true){
            return;
        }
        if(xPos[0]<0 || xPos[0]>778 || yPos[0]>560 || yPos[0]<0){
            OverTheGame();
        }
       
    }
    public void isCollidedWithRat(){
        if((GamePanel.ratposx==xPos[0]) && (GamePanel.ratposy==yPos[0])){
            score++;
            lbl2.setText("Score : "+score);
            GamePanel.locateRat(); 
            lengthOfSnake++;
            return ;
        }
    }
    public static void main(String[] args) {
        new demo1();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        startGame();
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            upMove = false; downMove = false;
            leftMove = true;
        }else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
             leftMove = false;   upMove = false; downMove = false;
            rightMove = true;
        }else if(e.getKeyCode() == KeyEvent.VK_UP){
            leftMove = false;   rightMove = false; downMove = false;
            upMove = true;
        }else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            leftMove = false;   rightMove = false;  upMove = false; 
            downMove = true;
        }
    }
    public void startGame(){
        if(!timer.isRunning()){
            score = 0;
            lengthOfSnake = 3;
            boardPanel.setFocusable(true);
            boardPanel.requestFocusInWindow();
            gameOver = false;
            timer.start();
        }
    }
    public void restartGame(){
        score = 0;
        lengthOfSnake = 3;
        gameOver = false;
        leftMove = false;
        rightMove = true;
        upMove = false;
        downMove = false;
        for(int i = 0; i < lengthOfSnake; i++){
           xPos[i] = 80 - (i * mouthSize);
           yPos[i] = mouthSize;
        }
        lbl2.setText("Score : 0");
        startGame();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==restartbtn){
            timer.stop();
            int x = JOptionPane.showConfirmDialog(this,"Do u want to restart thhis game ? ",
                    "Restart Game",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
                    );
            if(x == JOptionPane.YES_OPTION){
                restartGame();
            }else{
                this.dispose();
            }
        }
    }
}

