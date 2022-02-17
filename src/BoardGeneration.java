import javax.swing.*;
import java.util.Arrays;

public class BoardGeneration {


    public static void main(String[] args) {
        initializeStandardChess();
    }
    public static void initializeStandardChess(){
        long WP=0L,WR=0L,WN=0L,WB=0L,WK=0L,WQ=0L,BP=0L,BR=0L,BN=0L,BB=0L,BK=0L,BQ=0L;
        String chessBoard[][] = {
                {"r","n","b","q","k","b","n","r"},
                {"p","p","p","p","p","p","p","p"},
                {" "," "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "," "},
                {"P","P","P","P","P","P","P","P"},
                {"R","N","B","Q","K","B","N","R"},
        };
        arrayToBitboards(chessBoard,WP,WR,WN,WB,WK,WQ,BP,BR,BN,BB,BK,BQ);
    }

    public static void initializeChess960(){
        long WP=0L,WR=0L,WN=0L,WB=0L,WK=0L,WQ=0L,BP=0L,BR=0L,BN=0L,BB=0L,BK=0L,BQ=0L;
        String chessBoard[][] = {
                {" "," "," "," "," "," "," "," "},
                {"p","p","p","p","p","p","p","p"},
                {" "," "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "," "},
                {"P","P","P","P","P","P","P","P"},
                {" "," "," "," "," "," "," "," "},
        };
        int random1 = (int) (Math.random()*8);
        chessBoard[0][random1]="b";
        chessBoard[7][random1]="B";
        int random2 = (int) (Math.random()*8);
        while(random2%2==random1%2){
            random2 = (int) (Math.random()*8);
        }
        chessBoard[0][random2]="b";
        chessBoard[7][random2]="B";
        int random3= (int) (Math.random()*8);
        while(random3==random1||random3==random2){
            random3= (int) (Math.random()*8);
        }
        chessBoard[0][random3]="q";
        chessBoard[7][random3]="Q";
        int random4= (int) (Math.random()*8);
        while(random4==random1||random4==random2||random4==random3){
            random4= (int) (Math.random()*8);
        }
        chessBoard[0][random4]="n";
        chessBoard[7][random4]="N";
        int random5= (int) (Math.random()*8);
        while(random5==random1||random5==random2||random5==random3||random5==random4){
            random5= (int) (Math.random()*8);
        }
        chessBoard[0][random5]="n";
        chessBoard[7][random5]="N";

        int[] freePos = new int[3];
        int j=0;
        for(int i=0; i<8; i++){
            if(chessBoard[0][i]==" ") {freePos[j]=i; j++;}
        }
        chessBoard[0][freePos[0]]="r";
        chessBoard[7][freePos[0]]="R";
        chessBoard[0][freePos[1]]="k";
        chessBoard[7][freePos[1]]="K";
        chessBoard[0][freePos[2]]="r";
        chessBoard[7][freePos[2]]="R";

        arrayToBitboards(chessBoard,WP,WR,WN,WB,WK,WQ,BP,BR,BN,BB,BK,BQ);
    }

    public static void arrayToBitboards(String[][] board,long WP,long WR,long WN,long WB,long WQ,long WK,long BP,long BR,long BN,long BB,long BQ,long BK){
        for(int i=0; i<64; i++){
            String binary="0000000000000000000000000000000000000000000000000000000000000000";
            binary=binary.substring(i+1)+"1"+binary.substring(0,i);
            switch(board[i/8][i%8]){
                case "P": WP+=convertStringToBitboard(binary);
                    break;
                case "R": WR+=convertStringToBitboard(binary);
                    break;
                case "N": WN+=convertStringToBitboard(binary);
                    break;
                case "B": WB+=convertStringToBitboard(binary);
                    break;
                case "Q": WQ+=convertStringToBitboard(binary);
                    break;
                case "K": WK+=convertStringToBitboard(binary);
                    break;
                case "p": BP+=convertStringToBitboard(binary);
                    break;
                case "r": BR+=convertStringToBitboard(binary);
                    break;
                case "n": BN+=convertStringToBitboard(binary);
                    break;
                case "b": BB+=convertStringToBitboard(binary);
                    break;
                case "q": BQ+=convertStringToBitboard(binary);
                    break;
                case "k": BK+=convertStringToBitboard(binary);
                    break;
            }
        }
        //drawArray(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BQ,BK);
        UserInterface.WP=WP;
        UserInterface.WR=WR;
        UserInterface.WN=WN;
        UserInterface.WB=WB;
        UserInterface.WQ=WQ;
        UserInterface.WK=WK;
        UserInterface.BP=BP;
        UserInterface.BR=BR;
        UserInterface.BN=BN;
        UserInterface.BB=BB;
        UserInterface.BQ=BQ;
        UserInterface.BK=BK;

    }

    private static long convertStringToBitboard(String string){
        if(string.charAt(0)=='0') return Long.parseLong(string,2);
        else return Long.parseLong("1"+string.substring(2),2)*2;
    }

    private static void drawArray(long WP,long WR,long WN,long WB,long WQ,long WK,long BP,long BR,long BN,long BB,long BQ,long BK){
        String[][] chessBoard = new String[8][8];
        for(int i=0; i<64; i++){
            chessBoard[i/8][i%8]=" ";
        }
        for(int i=0; i<64; i++){
            if(((WP>>i)&1)==1){chessBoard[i/8][i%8]="P";}
            if(((WR>>i)&1)==1){chessBoard[i/8][i%8]="R";}
            if(((WN>>i)&1)==1){chessBoard[i/8][i%8]="N";}
            if(((WB>>i)&1)==1){chessBoard[i/8][i%8]="B";}
            if(((WQ>>i)&1)==1){chessBoard[i/8][i%8]="Q";}
            if(((WK>>i)&1)==1){chessBoard[i/8][i%8]="K";}
            if(((BP>>i)&1)==1){chessBoard[i/8][i%8]="p";}
            if(((BR>>i)&1)==1){chessBoard[i/8][i%8]="r";}
            if(((BN>>i)&1)==1){chessBoard[i/8][i%8]="n";}
            if(((BB>>i)&1)==1){chessBoard[i/8][i%8]="b";}
            if(((BQ>>i)&1)==1){chessBoard[i/8][i%8]="q";}
            if(((BK>>i)&1)==1){chessBoard[i/8][i%8]="k";}
        }
        for(int i=0; i<8; i++){
            System.out.println(Arrays.toString(chessBoard[i]));
        }

    }
}
