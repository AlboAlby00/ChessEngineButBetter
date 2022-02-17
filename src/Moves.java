import java.util.Arrays;
import java.util.concurrent.Callable;

public class Moves {
    static long FILE_A=72340172838076673L;
    static long FILE_H=-9187201950435737472L;
    static long FILE_AB=217020518514230019L;
    static long FILE_GH=-4557430888798830400L;
    static long RANK_1=-72057594037927936L;
    static long RANK_4=1095216660480L;
    static long RANK_5=4278190080L;
    static long RANK_8=255L;
    static long CENTRE=103481868288L;
    static long EXTENDED_CENTRE=66229406269440L;
    static long KING_SIDE=-1085102592571150096L;
    static long QUEEN_SIDE=1085102592571150095L;
    static long KING_B7=460039L;
    static long KNIGHT_C6=43234889994L;

    static long NOT_WHITE_PIECES;
    static long BLACK_PIECES;
    static long EMPTY;
    static long OCCUPIED;

    static long RankMasks8[] =/*from rank1 to rank8*/
            {
                    0xFFL, 0xFF00L, 0xFF0000L, 0xFF000000L, 0xFF00000000L, 0xFF0000000000L, 0xFF000000000000L, 0xFF00000000000000L
            };
    static long FileMasks8[] =/*from fileA to FileH*/
            {
                    0x101010101010101L, 0x202020202020202L, 0x404040404040404L, 0x808080808080808L,
                    0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L
            };
    static long DiagonalMasks8[] =/*from top left to bottom right*/
            {
                    0x1L, 0x102L, 0x10204L, 0x1020408L, 0x102040810L, 0x10204081020L, 0x1020408102040L,
                    0x102040810204080L, 0x204081020408000L, 0x408102040800000L, 0x810204080000000L,
                    0x1020408000000000L, 0x2040800000000000L, 0x4080000000000000L, 0x8000000000000000L
            };
    static long AntiDiagonalMasks8[] =/*from top right to bottom left*/
            {
                    0x80L, 0x8040L, 0x804020L, 0x80402010L, 0x8040201008L, 0x804020100804L, 0x80402010080402L,
                    0x8040201008040201L, 0x4020100804020100L, 0x2010080402010000L, 0x1008040201000000L,
                    0x804020100000000L, 0x402010000000000L, 0x201000000000000L, 0x100000000000000L
            };

    static long HAndVMoves(int s)
    {
        long binaryS=1L<<s;
        long possibilitiesHorizontal = (OCCUPIED - 2 * binaryS) ^ Long.reverse(Long.reverse(OCCUPIED) - 2 * Long.reverse(binaryS));
        long possibilitiesVertical = ((OCCUPIED&FileMasks8[s % 8]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(OCCUPIED&FileMasks8[s % 8]) - (2 * Long.reverse(binaryS)));
        return (possibilitiesHorizontal&RankMasks8[s / 8]) | (possibilitiesVertical&FileMasks8[s % 8]);
    }
    static long DAndAntiDMoves(int s)
    {
        long binaryS=1L<<s;
        long possibilitiesDiagonal = ((OCCUPIED&DiagonalMasks8[(s / 8) + (s % 8)]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(OCCUPIED&DiagonalMasks8[(s / 8) + (s % 8)]) - (2 * Long.reverse(binaryS)));
        long possibilitiesAntiDiagonal = ((OCCUPIED&AntiDiagonalMasks8[(s / 8) + 7 - (s % 8)]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(OCCUPIED&AntiDiagonalMasks8[(s / 8) + 7 - (s % 8)]) - (2 * Long.reverse(binaryS)));
        return (possibilitiesDiagonal&DiagonalMasks8[(s / 8) + (s % 8)]) | (possibilitiesAntiDiagonal&AntiDiagonalMasks8[(s / 8) + 7 - (s % 8)]);
    }

    static public String possibleMovesW(String history,long WP,long WR,long WN,long WB,long WQ,long WK,long BP,long BR,long BN,long BB,long BQ,long BK){
        NOT_WHITE_PIECES=~(WP|WR|WN|WB|WQ|WK|BK);
        BLACK_PIECES=BP|BR|BN|BB|BQ;
        OCCUPIED = WP|WR|WN|WB|WQ|WK|BK|BP|BR|BN|BB|BQ;
        EMPTY=~OCCUPIED;
        String list="";
        list += possibleWP(history,WP,BP)+
                possibleWB(OCCUPIED, WB);
        return list;
    }

    static public String possibleWP(String history, long WP, long BP){
        long PAWN_MOVES;
        long possibility;
        String list="";
        //list format: oy+ox+ny+nx
        //right attack
        PAWN_MOVES=(WP>>7)&BLACK_PIECES&~FILE_A&~RANK_8;
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while(possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            if(((possibility>>i)&1)==1) list+=""+(i/8+1)+(i%8-1)+(i/8)+(i%8);
            PAWN_MOVES&=PAWN_MOVES&~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //left attack
        PAWN_MOVES=(WP>>9)&BLACK_PIECES&~FILE_H&~RANK_8;
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while(possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            if(((possibility>>i)&1)==1) list+=""+(i/8+1)+(i%8+1)+(i/8)+(i%8);
            PAWN_MOVES&=PAWN_MOVES&~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //one up
        PAWN_MOVES=(WP>>8)&EMPTY&~RANK_8;
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            if(((possibility>>i)&1)==1) list+=""+(i/8+1)+i%8+i/8+i%8;
            PAWN_MOVES&=PAWN_MOVES&~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //two up
        PAWN_MOVES=(WP>>16)&EMPTY&(EMPTY>>8)&RANK_4;
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            if(((possibility>>i)&1)==1) list+=""+(i/8+2)+i%8+i/8+i%8;
            PAWN_MOVES&=PAWN_MOVES&~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }

        //promotion, format: ox,nx,PT,"P".
        //right attack
        String[] piecesForPromotion={"Q","N","R","B"};
        PAWN_MOVES=(WP>>7)&BLACK_PIECES&~FILE_A&RANK_8;
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while(possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            for(String p:piecesForPromotion){
                if(((possibility>>i)&1)==1) list+=""+(i%8-1)+(i%8)+p+"P";}
            PAWN_MOVES&=PAWN_MOVES&~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //left attack
        PAWN_MOVES=(WP>>9)&BLACK_PIECES&~FILE_H&RANK_8;
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while(possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            for(String p:piecesForPromotion){
                if(((possibility>>i)&1)==1) list+=""+(i%8+1)+(i%8)+p+"P";}
            PAWN_MOVES&=PAWN_MOVES&~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //one up
        PAWN_MOVES=(WP>>8)&EMPTY&RANK_8;
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            for(String p:piecesForPromotion){
                if(((possibility>>i)&1)==1) list+=""+(i%8)+(i%8)+p+"P";}
            PAWN_MOVES&=PAWN_MOVES&~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //en passant
        //list format: oy+ox+ny+nx
        //list format: ox+nx+" E"
        if(history.length()>=4){
            if(history.charAt(history.length()-1)==history.charAt(history.length()-3)&&Math.abs(history.charAt(history.length()-2)-history.charAt(history.length()-4))==2){
                //en passant right
                int aFile=history.charAt(history.length()-1)-'0';
                possibility=(WP<<1)&BLACK_PIECES&FileMasks8[aFile];
                if(possibility!=0){
                    int index=Long.numberOfTrailingZeros(possibility);
                    list+=""+(index%8-1)+(index%8)+" E";
                }
                aFile=history.charAt(history.length()-1)-'0';
                possibility=(WP>>1)&BLACK_PIECES&FileMasks8[aFile];
                if(possibility!=0){
                    int index=Long.numberOfTrailingZeros(possibility);
                    list+=""+(index%8+1)+(index%8)+" E";
                }
            }
        }

        //System.out.println(list);
        return list;
    }

    static public String possibleWB(long OCCUPIED, long WB){
        String list="";
        long i = WB&~(WB-1);
        long possibility;
        while (i!=0){
            //System.out.println("ciao1");
            int iLocation = Long.numberOfTrailingZeros(i);
            possibility=DAndAntiDMoves(iLocation)&NOT_WHITE_PIECES;
            long j = possibility&~(possibility-1);
            while (j!=0){
                int index = Long.numberOfTrailingZeros(j);
                list+=""+(iLocation/8)+(iLocation%8)+(index/8)+(index%8);
                possibility&=~j;
                j=possibility&~(possibility-1);
            }
            WB&=~i;
            i=WB&~(WB-1);
        }
        //System.out.println(list.length()/4);
        return list;
    }

    static public void checkTime(){
        long PAWN_MOVES,WP=RANK_4,possibility;
        String list="";
        long startTime = System.currentTimeMillis();
        for(int j=0; j<10000; j++) {
            PAWN_MOVES=(WP>>7)&BLACK_PIECES&~FILE_A&~RANK_8;
            for(int i=0; i<64; i++){
                if(((PAWN_MOVES>>i)&1)==1) list+=""+(i/8+1)+(i%8-1)+(i/8)+(i%8);
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("first method took "+(endTime-startTime)+" milliseconds");
        startTime = System.currentTimeMillis();
        for(int j=0; j<10000; j++) {
            PAWN_MOVES=(WP>>7)&BLACK_PIECES&~FILE_A&~RANK_8;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
            while(possibility!=0){
                int i=Long.numberOfTrailingZeros(possibility);
                if(((possibility>>i)&1)==1) list+=""+(i/8+1)+(i%8-1)+(i/8)+(i%8);
                PAWN_MOVES&=PAWN_MOVES&~possibility;
                possibility=PAWN_MOVES&~(PAWN_MOVES-1);
            }
        }
        endTime = System.currentTimeMillis();
        System.out.println("second method took "+(endTime-startTime)+" milliseconds");
    }

    static public void drawBitboard(long bitboard){
        String[][] board = new String[8][8];
        for(int i=0; i<64; i++){
            if(((bitboard>>i)&1)==1)
                board[i/8][i%8]="x";
            else
                board[i/8][i%8]=" ";
        }
        for(String[] line : board){
            System.out.println(Arrays.toString(line));
        }

    }
}