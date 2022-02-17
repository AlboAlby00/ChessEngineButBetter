import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class UserInterface extends JPanel {

    //WP,WR,WN,WB,WK,WQ,BP,BR,BN,BB,BK,BQ
    static long WP=0L,WR=0L,WN=0L,WB=0L,WK=0L,WQ=0L,BP=0L,BR=0L,BN=0L,BB=0L,BK=0L,BQ=0L;
    static int humanIsWhite=0;

    static int squareSize = 64;
    static int exitBarHeight = 35;

    static BufferedImage piecesImage;
    static Image[] pieces;


    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        JFrame f = new JFrame("Chess engine");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.add(ui);
        f.setVisible(true);
        f.setSize(8*squareSize,8*squareSize+exitBarHeight);


    }

    public UserInterface(){
        loadPieces();
    }

    public static void loadPieces(){
        try{
            piecesImage= ImageIO.read(new File("Images/chess.png"));
            //getScaledInstance(64*5,64*2,Image.SCALE_SMOOTH);
        } catch (Exception e){e.printStackTrace();}
        int orWidth=piecesImage.getWidth()/6, orHeight = piecesImage.getWidth()/6;
        pieces=new Image[12];
        for(int j=0; j<2; j++ ){
            for(int i=0;i<6; i++ ){
                pieces[j*6+i]=piecesImage.getSubimage(i*orWidth,j*orHeight,orWidth,orHeight).getScaledInstance(64,64,Image.SCALE_SMOOTH);
            }
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.white);
        drawBoard(g);
        drawPieces(g);
        newGame();
    }

    public void drawBoard(Graphics g){
        boolean white=true;
        for(int xb=0; xb<8; xb++){
            for(int yb=0; yb<8; yb++){
                if(white) g.setColor(Color.white.darker());
                else g.setColor(new Color(125,135,150));
                g.fillRect(xb*squareSize,yb*squareSize,squareSize,squareSize);
                white=!white;
            }
            white=!white;
        }
    }

    public void drawPieces(Graphics g){
        for(int i=0; i<64; i++){
            if(((WK>>i)&1)==1){
                g.drawImage(pieces[0],i%8*squareSize, i/8*squareSize, this);
            }
            if(((WQ>>i)&1)==1){
                g.drawImage(pieces[1],i%8*squareSize, i/8*squareSize, this);
            }
            if(((WB>>i)&1)==1){
                g.drawImage(pieces[2],i%8*squareSize, i/8*squareSize, this);
            }
            if(((WN>>i)&1)==1){
                g.drawImage(pieces[3],i%8*squareSize, i/8*squareSize, this);
            }
            if(((WR>>i)&1)==1){
                g.drawImage(pieces[4],i%8*squareSize, i/8*squareSize, this);
            }
            if(((WP>>i)&1)==1){
                g.drawImage(pieces[5],i%8*squareSize, i/8*squareSize, this);
            }
            if(((BK>>i)&1)==1){
                g.drawImage(pieces[6],i%8*squareSize, i/8*squareSize, this);
            }
            if(((BQ>>i)&1)==1){
                g.drawImage(pieces[7],i%8*squareSize, i/8*squareSize,this);
            }
            if(((BB>>i)&1)==1){
                g.drawImage(pieces[8],i%8*squareSize, i/8*squareSize, this);
            }
            if(((BN>>i)&1)==1){
                g.drawImage(pieces[9],i%8*squareSize, i/8*squareSize, this);
            }
            if(((BR>>i)&1)==1){
                g.drawImage(pieces[10],i%8*squareSize, i/8*squareSize, this);
            }
            if(((BP>>i)&1)==1){
                g.drawImage(pieces[11],i%8*squareSize, i/8*squareSize, this);
            }
        }

    }

    public static void newGame(){
        BoardGeneration.initializeStandardChess();
        Moves.possibleMovesW("",WP,WR,WN,WB,WK,WQ,BP,BR,BN,BB,BK,BQ);
    }

}
