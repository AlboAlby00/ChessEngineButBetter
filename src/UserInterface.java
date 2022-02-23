import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class UserInterface extends JPanel implements MouseListener, MouseMotionListener {

    //WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BQ,BK,EP
    //UniversalCastleWK,UniversalCastleWQ,UniversalCastleBK,UniversalCastleBQ
    static long WP=0L,WR=0L,WN=0L,WB=0L,WK=0L,WQ=0L,BP=0L,BR=0L,BN=0L,BB=0L,BK=0L,BQ=0L,EP=0L;
    static int humanIsWhite=0;
    static public boolean UniversalCastleWK=true,UniversalCastleWQ=true,UniversalCastleBK=true,UniversalCastleBQ=true;

    static public boolean mousePressed;
    static int squareSize = 64;
    static int exitBarHeight = 35;
    static int currentPlayer=1; //1 white, 0 black
    static String poss;

    static BufferedImage piecesImage;
    static Image[] pieces;

    static int xPos, yPos, xPress=-1, yPress=-1, xRel, yRel;


    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        JFrame f = new JFrame("Chess engine");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.add(ui);
        f.setVisible(true);
        f.setSize(8*squareSize+15,8*squareSize+exitBarHeight);
        newGame();

        //Perft.perftRoot(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BQ,BK,UniversalCastleWQ,UniversalCastleWK,UniversalCastleBQ,UniversalCastleBK,EP,true,0);
        //System.out.println("total moves: "+ Perft.nTotalMoves);
        //System.out.println(Moves.possibleMovesB(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BK,BQ,UniversalCastleBQ,UniversalCastleBK,EP));
        Perft.nTotalMoves=0;


    }

    public static void colorPossibleMoves(Graphics g){
        String possibleMoves = currentPlayer==1 ? Moves.possibleMovesW(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BQ,BK,UniversalCastleWQ,UniversalCastleWK,EP) :
                                                  Moves.possibleMovesB(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BQ,BK,UniversalCastleBQ,UniversalCastleBK,EP);

        g.setColor(new Color(139,5,5,100));

        for(int xb=0; xb<8; xb++){
            for(int yb=0; yb<8; yb++){
                String move = getMove(yPress/squareSize,xPress/squareSize,yb,xb);
                if(containsMove(possibleMoves,move))
                    g.fillRect(xb*squareSize,yb*squareSize,squareSize,squareSize);

            }

        }
    }


    public UserInterface(){
        loadPieces();
        addMouseListener(this);
        addMouseMotionListener(this);
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
        colorPossibleMoves(g);
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

    static public String getMove(int oldY, int oldX, int newY, int newX){
        //promotion white
        String move="";
        int i = oldY*8+oldX;
        if(((1L<<i)&WP&Moves.RankMasks8[1])!=0&&newY==0){
            move=""+(oldX)+(newX)+"QP";
        }
        //promotion black
        else if(((1L<<i)&BP&Moves.RankMasks8[6])!=0&&newY==7){
            move=""+(oldX)+(newX)+"qP";
        }
        //standard move
        else {
            move = ""+(oldY)+(oldX)+(newY)+(newX);
        }
        return move;
    }

    public void drawPieces(Graphics g){
        int piecePos=yPress/squareSize*8+xPress/squareSize;
        for(int i=0; i<64; i++){
            if(piecePos!=i||!mousePressed){
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
            else{
                int x = xPos-squareSize/2, y = yPos-squareSize/2;
                if(((WK>>i)&1)==1){
                    g.drawImage(pieces[0],x, y, this);
                }
                if(((WQ>>i)&1)==1){
                    g.drawImage(pieces[1],x, y, this);
                }
                if(((WB>>i)&1)==1){
                    g.drawImage(pieces[2],x, y, this);
                }
                if(((WN>>i)&1)==1){
                    g.drawImage(pieces[3],x, y, this);
                }
                if(((WR>>i)&1)==1){
                    g.drawImage(pieces[4],x, y,this);
                }
                if(((WP>>i)&1)==1){
                    g.drawImage(pieces[5],x, y,this);
                }
                if(((BK>>i)&1)==1){
                    g.drawImage(pieces[6],x, y, this);
                }
                if(((BQ>>i)&1)==1){
                    g.drawImage(pieces[7],x, y,this);
                }
                if(((BB>>i)&1)==1){
                    g.drawImage(pieces[8],x, y, this);
                }
                if(((BN>>i)&1)==1){
                    g.drawImage(pieces[9],x, y,this);
                }
                if(((BR>>i)&1)==1){
                    g.drawImage(pieces[10],x, y, this);
                }
                if(((BP>>i)&1)==1){
                    g.drawImage(pieces[11],x, y, this);
                }
            }
        }


    }

    public static void newGame(){
        //BoardGeneration.initializeStandardChess();
        BoardGeneration.importFen("rnbqkbnr/Pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - ");

    }

    public static void togglePlayer(){
        currentPlayer=1-currentPlayer;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //System.out.println("x pos: "+xPos/squareSize+", y pos: "+yPos/squareSize);
        //System.out.println(move);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        xPress=e.getX();
        yPress=e.getY();
        xPos=e.getX();
        yPos=e.getY();
        mousePressed=true;
        repaint();

    }
    //Moves.possibleMovesW(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BQ,BK,UniversalCastleWQ,UniversalCastleWK,EP)
    @Override
    public void mouseReleased(MouseEvent e) {
        xRel=e.getX();
        yRel=e.getY();
        mousePressed=false;

        String move=getMove(yPress/squareSize,xPress/squareSize,yRel/squareSize,xRel/squareSize);
        String possibleMovesW=Moves.possibleMovesW(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BQ,BK,UniversalCastleWQ,UniversalCastleWK,EP);
        String possibleMovesB=Moves.possibleMovesB(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BQ,BK,UniversalCastleBQ,UniversalCastleBK,EP);
        if(currentPlayer==1&&containsMove(possibleMovesW,move)||currentPlayer==0&&containsMove(possibleMovesB,move)){
            Moves.makeMoveWrong(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BQ,BK,UniversalCastleWK,UniversalCastleWQ,UniversalCastleBK,UniversalCastleBQ,EP,move);
            togglePlayer();
        }
     /*   else {System.out.println("fail");
            if(currentPlayer==1) System.out.println("White moves: "+possibleMovesW);
                else System.out.println("Black moves: "+possibleMovesB);}*/

        repaint();
    }

    static public boolean containsMove(String listOfMoves, String move){
        for(int i=0; i<listOfMoves.length(); i+=4){
            if(move.equals(listOfMoves.substring(i,i+4))){
                return true;
            }
        }
        return false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        xPos=e.getX();
        yPos=e.getY();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
