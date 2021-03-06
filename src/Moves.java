import java.util.Arrays;

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
    static long KING_SPAN=460039L;
    static long KNIGHT_C6=43234889994L;
    static long KNIGHT_SPAN=43234889994L;

    static long NOT_MY_PIECES;
    static long BLACK_PIECES;
    static long WHITE_PIECES;
    static long EMPTY;
    static long OCCUPIED;
    static long CASTLE_ROOKS[] = {63,56,7,0};

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

    static long HAndVs(int s)
    {
        long binaryS=1L<<s;
        long possibilitiesHorizontal = (OCCUPIED - 2 * binaryS) ^ Long.reverse(Long.reverse(OCCUPIED) - 2 * Long.reverse(binaryS));
        long possibilitiesVertical = ((OCCUPIED&FileMasks8[s % 8]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(OCCUPIED&FileMasks8[s % 8]) - (2 * Long.reverse(binaryS)));
        return (possibilitiesHorizontal&RankMasks8[s / 8]) | (possibilitiesVertical&FileMasks8[s % 8]);
    }
    static long DAndAntiDs(int s)
    {
        long binaryS=1L<<s;
        long possibilitiesDiagonal = ((OCCUPIED&DiagonalMasks8[(s / 8) + (s % 8)]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(OCCUPIED&DiagonalMasks8[(s / 8) + (s % 8)]) - (2 * Long.reverse(binaryS)));
        long possibilitiesAntiDiagonal = ((OCCUPIED&AntiDiagonalMasks8[(s / 8) + 7 - (s % 8)]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(OCCUPIED&AntiDiagonalMasks8[(s / 8) + 7 - (s % 8)]) - (2 * Long.reverse(binaryS)));
        return (possibilitiesDiagonal&DiagonalMasks8[(s / 8) + (s % 8)]) | (possibilitiesAntiDiagonal&AntiDiagonalMasks8[(s / 8) + 7 - (s % 8)]);
    }

    static public String possibleMovesW(long WP,long WR,long WN,long WB,long WQ,long WK,long BP,long BR,long BN,long BB,long BQ,long BK,boolean CWQ, boolean CWK, long EP){
        NOT_MY_PIECES =~(WP|WR|WN|WB|WQ|WK|BK);
        BLACK_PIECES=BP|BR|BN|BB|BQ;
        OCCUPIED = WP|WR|WN|WB|WQ|WK|BK|BP|BR|BN|BB|BQ;
        EMPTY=~OCCUPIED;
        String list="";
        list += possibleWP(WP,EP)+
                possibleB(OCCUPIED,WB)+
                possibleR(OCCUPIED,WR)+
                possibleQ(OCCUPIED,WQ)+
                possibleN(OCCUPIED,WN)+
                possibleK(OCCUPIED,WK)+
                possibleCW(CWQ,CWK,WR);
        unsafeForBlack(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BQ,BK);
        //System.out.println(list);
        return list;
    }

    static String possibleMovesB(long WP,long WR,long WN,long WB,long WQ,long WK,long BP,long BR,long BN,long BB,long BQ,long BK, boolean CBQ, boolean CBK, long EP){
        NOT_MY_PIECES =~(BP|BR|BN|BB|BQ|BK|WK);
        WHITE_PIECES=WP|WR|WN|WB|WQ;
        OCCUPIED = WP|WR|WN|WB|WQ|WK|BK|BP|BR|BN|BB|BQ;
        EMPTY=~OCCUPIED;
        String list="";
        list += possibleBP(BP,EP)+
                possibleB(OCCUPIED,BB)+
                possibleR(OCCUPIED,BR)+
                possibleQ(OCCUPIED,BQ)+
                possibleN(OCCUPIED,BN)+
                possibleK(OCCUPIED,BK)+
                possibleCB(CBQ,CBK,BR);
        unsafeForWhite(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BQ,BK);
        //System.out.println(list.length()/4);
        return list;
    }

    static public String possibleWP( long WP, long EP){
        long PAWN_S;
        long possibility;
        String list="";
        //list format: oy+ox+ny+nx
        //right attack
        PAWN_S=(WP>>7)&BLACK_PIECES&~FILE_A&~RANK_8;
        possibility=PAWN_S&~(PAWN_S-1);
        while(possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            if(((possibility>>i)&1)==1) list+=""+(i/8+1)+(i%8-1)+(i/8)+(i%8);
            PAWN_S&=PAWN_S&~possibility;
            possibility=PAWN_S&~(PAWN_S-1);
        }
        //left attack
        PAWN_S=(WP>>9)&BLACK_PIECES&~FILE_H&~RANK_8;
        possibility=PAWN_S&~(PAWN_S-1);
        while(possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            if(((possibility>>i)&1)==1) list+=""+(i/8+1)+(i%8+1)+(i/8)+(i%8);
            PAWN_S&=PAWN_S&~possibility;
            possibility=PAWN_S&~(PAWN_S-1);
        }
        //one up
        PAWN_S=(WP>>8)&EMPTY&~RANK_8;
        possibility=PAWN_S&~(PAWN_S-1);
        while (possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            if(((possibility>>i)&1)==1) list+=""+(i/8+1)+i%8+i/8+i%8;
            PAWN_S&=PAWN_S&~possibility;
            possibility=PAWN_S&~(PAWN_S-1);
        }
        //two up
        PAWN_S=(WP>>16)&EMPTY&(EMPTY>>8)&RANK_4;
        possibility=PAWN_S&~(PAWN_S-1);
        while (possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            if(((possibility>>i)&1)==1) list+=""+(i/8+2)+i%8+i/8+i%8;
            PAWN_S&=PAWN_S&~possibility;
            possibility=PAWN_S&~(PAWN_S-1);
        }

        //promotion, format: ox,nx,PT,"P".
        //right attack
        String[] piecesForPromotion={"Q","N","R","B"};
        PAWN_S=(WP>>7)&BLACK_PIECES&~FILE_A&RANK_8;
        possibility=PAWN_S&~(PAWN_S-1);
        while(possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            for(String p:piecesForPromotion){
                if(((possibility>>i)&1)==1) list+=""+(i%8-1)+(i%8)+p+"P";}
            PAWN_S&=PAWN_S&~possibility;
            possibility=PAWN_S&~(PAWN_S-1);
        }
        //left attack
        PAWN_S=(WP>>9)&BLACK_PIECES&~FILE_H&RANK_8;
        possibility=PAWN_S&~(PAWN_S-1);
        while(possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            for(String p:piecesForPromotion){
                if(((possibility>>i)&1)==1) list+=""+(i%8+1)+(i%8)+p+"P";}
            PAWN_S&=PAWN_S&~possibility;
            possibility=PAWN_S&~(PAWN_S-1);
        }
        //one up
        PAWN_S=(WP>>8)&EMPTY&RANK_8;
        possibility=PAWN_S&~(PAWN_S-1);
        while (possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            for(String p:piecesForPromotion){
                if(((possibility>>i)&1)==1) list+=""+(i%8)+(i%8)+p+"P";}
            PAWN_S&=PAWN_S&~possibility;
            possibility=PAWN_S&~(PAWN_S-1);
        }
        //en passant
        //list format: oy+ox+ny+nx
        //list format: ox+nx+" E"
        possibility=(WP<<1)&BLACK_PIECES&EP;
        if(possibility!=0){
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8-1)+(index%8)+"wE";
        }
        //en passant left
        possibility=(WP>>1)&BLACK_PIECES&EP;
        if(possibility!=0){
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8+1)+(index%8)+"wE";
        }

        //System.out.println(list);
        return list;
    }

    static public String possibleBP(long BP, long EP){
        long PAWN_S;
        long possibility;
        String list="";
        //list format: oy+ox+ny+nx
        //right attack
        PAWN_S=(BP<<7)&WHITE_PIECES&~FILE_H&~RANK_1;
        //System.out.println("wp"+PAWN_S);
        possibility=PAWN_S&~(PAWN_S-1);
        while(possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            if(((possibility>>i)&1)==1) list+=""+(i/8-1)+(i%8+1)+(i/8)+(i%8);
            PAWN_S&=PAWN_S&~possibility;
            possibility=PAWN_S&~(PAWN_S-1);
        }
        //left attack
        PAWN_S=(BP<<9)&WHITE_PIECES&~FILE_A&~RANK_1;
        possibility=PAWN_S&~(PAWN_S-1);
        while(possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            if(((possibility>>i)&1)==1) list+=""+(i/8-1)+(i%8-1)+(i/8)+(i%8);
            PAWN_S&=PAWN_S&~possibility;
            possibility=PAWN_S&~(PAWN_S-1);
        }
        //one up

        PAWN_S=(BP<<8)&EMPTY&~RANK_1;
        //drawBitboard(PAWN_S);
        possibility=PAWN_S&~(PAWN_S-1);
        while (possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            if(((possibility>>i)&1)==1) list+=""+(i/8-1)+i%8+i/8+i%8;
            PAWN_S&=PAWN_S&~possibility;
            possibility=PAWN_S&~(PAWN_S-1);
        }
        //two up
        PAWN_S=(BP<<16)&EMPTY&(EMPTY<<8)&RANK_5;
        possibility=PAWN_S&~(PAWN_S-1);
        while (possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            if(((possibility>>i)&1)==1) list+=""+(i/8-2)+i%8+i/8+i%8;
            PAWN_S&=PAWN_S&~possibility;
            possibility=PAWN_S&~(PAWN_S-1);
        }

        //promotion, format: ox,nx,PT,"P".
        //right attack
        String[] piecesForPromotion={"q","n","r","b"};
        PAWN_S=(BP<<7)&WHITE_PIECES&~FILE_H&RANK_1;
        possibility=PAWN_S&~(PAWN_S-1);
        while(possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            for(String p:piecesForPromotion){
                if(((possibility>>i)&1)==1) list+=""+(i%8+1)+(i%8)+p+"P";}
            PAWN_S&=PAWN_S&~possibility;
            possibility=PAWN_S&~(PAWN_S-1);
        }
        //left attack
        PAWN_S=(BP<<9)&WHITE_PIECES&~FILE_A&RANK_1;
        possibility=PAWN_S&~(PAWN_S-1);
        while(possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            for(String p:piecesForPromotion){
                if(((possibility>>i)&1)==1) list+=""+(i%8-1)+(i%8)+p+"P";}
            PAWN_S&=PAWN_S&~possibility;
            possibility=PAWN_S&~(PAWN_S-1);
        }
        //one up
        PAWN_S=(BP<<8)&EMPTY&RANK_1;
        possibility=PAWN_S&~(PAWN_S-1);
        while (possibility!=0){
            int i=Long.numberOfTrailingZeros(possibility);
            for(String p:piecesForPromotion){
                if(((possibility>>i)&1)==1) list+=""+(i%8)+(i%8)+p+"P";}
            PAWN_S&=PAWN_S&~possibility;
            possibility=PAWN_S&~(PAWN_S-1);
        }
        //en passant
        //list format: oy+ox+ny+nx
        //list format: ox+nx+" E"
        //en passant left
        possibility=(BP>>1)&WHITE_PIECES&RANK_4&~FILE_H&EP;
        if(possibility!=0){
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8+1)+(index%8)+"bE";
        }
        //en passant right
        possibility=(BP<<1)&WHITE_PIECES&RANK_4&~FILE_A&EP;
        if(possibility!=0){
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8-1)+(index%8)+"bE";
        }

        //System.out.println(list);
        return list;

    }

    static public String possibleB(long OCCUPIED, long B){
        String list="";
        long i = B&~(B-1);
        long possibility;
        while (i!=0){
            //System.out.println("ciao1");
            int iLocation = Long.numberOfTrailingZeros(i);
            possibility=DAndAntiDs(iLocation)& NOT_MY_PIECES;
            long j = possibility&~(possibility-1);
            while (j!=0){
                int index = Long.numberOfTrailingZeros(j);
                list+=""+(iLocation/8)+(iLocation%8)+(index/8)+(index%8);
                possibility&=~j;
                j=possibility&~(possibility-1);
            }
            B&=~i;
            i=B&~(B-1);
        }
        //System.out.println(list.length()/4);
        return list;
    }

    static public String possibleN(long OCCUPIED, long N){
        String list="";
        //System.out.println("nwpn: "+NOT_MY_PIECES);
        long i = N&~(N-1);
        long possibility;
        while (i!=0){
            //System.out.println("ciao1");
            int iLocation = Long.numberOfTrailingZeros(i);
            if(iLocation>18){
                possibility=KNIGHT_SPAN<<(iLocation-18);
            }
            else {
                possibility=KNIGHT_SPAN>>(18-iLocation);
            }
            if(iLocation%8<4){
                possibility&=~FILE_GH& NOT_MY_PIECES;
            } else {
                possibility&=~FILE_AB& NOT_MY_PIECES;
            }
            long j = possibility&~(possibility-1);
            while (j!=0){
                int index = Long.numberOfTrailingZeros(j);
                list+=""+(iLocation/8)+(iLocation%8)+(index/8)+(index%8);
                possibility&=~j;
                j=possibility&~(possibility-1);
            }
            N&=~i;
            i=N&~(N-1);
        }
        //System.out.println(list.length()/4);
        return list;
    }

    static public String possibleK(long OCCUPIED, long K){
        //System.out.println(K);
        String list="";
        long possibility;
        long iLocation=Long.numberOfTrailingZeros(K);
        if(iLocation>9){
            possibility=KING_SPAN<<(iLocation-9);
        }
        else {
            possibility=KING_SPAN>>(9-iLocation);
        }
        //System.out.println("nwpk: "+NOT_MY_PIECES);
        if(iLocation%8<4){
            possibility&=~FILE_GH& NOT_MY_PIECES;
        } else {
            possibility&=~FILE_AB& NOT_MY_PIECES;
        }
        long j = possibility&-possibility;
        while (j!=0){
            int index=Long.numberOfTrailingZeros(j);
            list+=""+(iLocation/8)+(iLocation%8)+(index/8)+(index%8);
            possibility&=~j;
            j=possibility&-possibility;
        }

        //System.out.println(list.length()/4);
        return list;
    }

    static public String possibleQ(long OCCUPIED, long Q){
        String list="";
        long i = Q&~(Q-1);
        long possibility;
        while (i!=0){
            //System.out.println("ciao1");
            int iLocation = Long.numberOfTrailingZeros(i);
            possibility=(DAndAntiDs(iLocation)|HAndVs(iLocation))& NOT_MY_PIECES;
            //drawBitboard(NOT_MY_PIECES);
            //System.out.println("");
            //drawBitboard(possibility);
            long j = possibility&~(possibility-1);
            while (j!=0){
                int index = Long.numberOfTrailingZeros(j);
                list+=""+(iLocation/8)+(iLocation%8)+(index/8)+(index%8);
                possibility&=~j;
                j=possibility&~(possibility-1);
            }
            Q&=~i;
            i=Q&~(Q-1);
        }


        //System.out.println(list.length()/4);
        return list;
    }

    static public String possibleR(long OCCUPIED, long R){
        String list="";
        long i = R&~(R-1);
        long possibility;
        while (i!=0){
            //System.out.println("ciao1");
            int iLocation = Long.numberOfTrailingZeros(i);
            possibility=HAndVs(iLocation)& NOT_MY_PIECES;
            long j = possibility&~(possibility-1);
            while (j!=0){
                int index = Long.numberOfTrailingZeros(j);
                list+=""+(iLocation/8)+(iLocation%8)+(index/8)+(index%8);
                possibility&=~j;
                j=possibility&~(possibility-1);
            }
            R&=~i;
            i=R&~(R-1);
        }
        //System.out.println(list.length()/4);
        return list;
    }

    static public long unsafeForBlack(long WP,long WR,long WN,long WB,long WQ,long WK,long BP,long BR,long BN,long BB,long BQ,long BK){
        long unsafe;
        //pawn
        unsafe=(WP>>7)&~FILE_A;
        unsafe|=(WP>>9)&~FILE_H;
        long possibility;
        //knight
        long i = WN&~(WN-1);
        while (i!=0){
            //System.out.println("ciao1");
            int iLocation = Long.numberOfTrailingZeros(i);
            if(iLocation>18){
                possibility=KNIGHT_SPAN<<(iLocation-18);
            }
            else {
                possibility=KNIGHT_SPAN>>(18-iLocation);
            }
            if(iLocation%8<4){
                possibility&=~FILE_GH& NOT_MY_PIECES;
            } else {
                possibility&=~FILE_AB& NOT_MY_PIECES;
            }
            long j = possibility&~(possibility-1);
            unsafe|=possibility;
            WN&=~i;
            i=WN&~(WN-1);
        }
        //bishop/queen
        long QB=WQ|WB;
        i=QB&-QB;
        while (i!=0){
            int index=Long.numberOfTrailingZeros(i);
            possibility=DAndAntiDs(index);
            unsafe|=possibility;
            QB&=~i;
            i=QB&-QB;
        }

        //rook/queen
        long QR=WQ|WR;
        i=QR&-QR;
        while (i!=0){
            int index=Long.numberOfTrailingZeros(i);
            possibility=HAndVs(index);
            unsafe|=possibility;
            QR&=~i;
            i=QR&-QR;
        }

        //king
       long iLocation=Long.numberOfTrailingZeros(WK);
        if(iLocation>9){
            possibility=KING_SPAN<<(iLocation-9);
        }
        else {
            possibility=KING_SPAN>>(9-iLocation);
        }
        //System.out.println("nwpk: "+NOT_MY_PIECES);
        if(iLocation%8<4){
            possibility&=~FILE_GH;
        } else {
            possibility&=~FILE_AB;
        }
        unsafe|=possibility;
        //drawBitboard(unsafe);
        return unsafe;
    }

    static public long unsafeForWhite(long WP,long WR,long WN,long WB,long WQ,long WK,long BP,long BR,long BN,long BB,long BQ,long BK){
        long unsafe=0;
        //pawn

        unsafe=(BP<<7)&~FILE_H;
        unsafe|=(BP<<9)&~FILE_A;
        long possibility;
        //knight
        long i = BN&~(BN-1);
        while (i!=0){
            //System.out.println("ciao1");
            int iLocation = Long.numberOfTrailingZeros(i);
            if(iLocation>18){
                possibility=KNIGHT_SPAN<<(iLocation-18);
            }
            else {
                possibility=KNIGHT_SPAN>>(18-iLocation);
            }
            if(iLocation%8<4){
                possibility&=~FILE_GH& NOT_MY_PIECES;
            } else {
                possibility&=~FILE_AB& NOT_MY_PIECES;
            }
            long j = possibility&~(possibility-1);
            unsafe|=possibility;
            BN&=~i;
            i=BN&~(BN-1);
        }
        //bishop/queen
        long QB=BQ|BB;
        i=QB&-QB;
        while (i!=0){
            int index=Long.numberOfTrailingZeros(i);
            possibility=DAndAntiDs(index);
            unsafe|=possibility;
            QB&=~i;
            i=QB&-QB;
        }

        //rook/queen
        long QR=BQ|BR;
        i=QR&-QR;
        while (i!=0){
            int index=Long.numberOfTrailingZeros(i);
            possibility=HAndVs(index);
            unsafe|=possibility;
            QR&=~i;
            i=QR&-QR;
        }

        //king
        long iLocation=Long.numberOfTrailingZeros(BK);
        if(iLocation>9){
            possibility=KING_SPAN<<(iLocation-9);
        }
        else {
            possibility=KING_SPAN>>(9-iLocation);
        }
        //System.out.println("nwpk: "+NOT_MY_PIECES);
        if(iLocation%8<4){
            possibility&=~FILE_GH;
        } else {
            possibility&=~FILE_AB;
        }
        unsafe|=possibility;
        //drawBitboard(unsafe);
        return unsafe;
    }

    static public String possibleCW(boolean CWQ, boolean CWK, long WR){
        String list="";
        if(CWK&&(1L<<CASTLE_ROOKS[0]&WR)!=0){
            if((OCCUPIED&(1L<<62|1L<<61))==0){
                list+="7476";
            }

        }
        if(CWQ&&(1L<<CASTLE_ROOKS[1]&WR)!=0){
            if((OCCUPIED&(1L<<57|1L<<58|1L<<59))==0){
                list+="7472";
            }
        }
        return list;
    }

    static public String possibleCB(boolean CBQ, boolean CBK, long BR){
        String list="";
        if(CBK&&(1L<<CASTLE_ROOKS[2]&BR)!=0){
            if((OCCUPIED&(1L<<5|1L<<6))==0){
                list+="0406";
            }
        }
        if(CBQ&&(1L<<CASTLE_ROOKS[3]&BR)!=0){
            if((OCCUPIED&(1L<<1|1L<<2|1L<<3))==0){
                list+="0402";
            }
        }
        return list;
    }

    static public long makeMove(long board, String move, char type){
        if(Character.isDigit(move.charAt(3))){
            int start = Character.getNumericValue(move.charAt(0))*8+Character.getNumericValue(move.charAt(1));
            int end = Character.getNumericValue(move.charAt(2))*8+Character.getNumericValue(move.charAt(3));
            if(((board>>>start)&1)==1) {
                board&=~(1L<<start);
                board|=(1L<<end);
            } else board&=~(1L<<end);
        } else if(move.charAt(3)=='P'){
            int start,end;
            if(Character.isUpperCase(move.charAt(2))){
                start=8+Character.getNumericValue(move.charAt(0));
                //System.out.println("Start: "+start);
                end=Character.getNumericValue(move.charAt(1));
            }
            else{
                start=48+Character.getNumericValue(move.charAt(0));
                end=56+Character.getNumericValue(move.charAt(1));
            }
            board&=~(1L<<start);
            if(type==move.charAt(2)){
                board|=1L<<end;
            }
            else board&=~(1L<<end);
        } else if(move.charAt(3)=='E'){
            int start,end,k;
            if(Character.isUpperCase(move.charAt(2))){
                start=24+Character.getNumericValue(move.charAt(0));
                end=16+Character.getNumericValue(move.charAt(1));
                k=+8;
            } else{
                start=32+Character.getNumericValue(move.charAt(0));
                end=40+Character.getNumericValue(move.charAt(1));
                k=-8;
            }
            if(((board>>>start)&1)==1){
                board&=~(1L<<start);
                board|=1L<<end;
            }
            else board &= ~(1L<<(end+k));


        } else System.out.println("Error!  format is wrong!");
        return board;
    }

    static public long makeEp(long board, String move ){
        if(Character.isDigit(move.charAt(3))){
            int start = Character.getNumericValue((move.charAt(0)))*8+Character.getNumericValue((move.charAt(0)));
            if(((1L<<start)&board)!=0&&Math.abs(move.charAt(0)-move.charAt(2))==2) return FileMasks8[move.charAt(1)-'0'];
        }
        return 0;
    }
    
    static public void makeMoveWrong(long WP,long WR,long WN,long WB,long WQ,long WK,long BP,long BR,long BN,
                                     long BB,long BQ,long BK,boolean CWQ, boolean CWK,boolean CBQ, boolean CBK, long EP, String move ){
        //System.out.println(s);

        long tWP=Moves.makeMove(WP,move,'P');
        long tWR=Moves.makeMove(WR,move,'R');
        long tWN=Moves.makeMove(WN,move,'N');
        long tWB=Moves.makeMove(WB,move,'B');
        long tWQ=Moves.makeMove(WQ,move,'Q');
        long tWK=Moves.makeMove(WK,move,'K');
        long tBP=Moves.makeMove(BP,move,'p');
        long tBR=Moves.makeMove(BR,move,'r');
        long tBN=Moves.makeMove(BN,move,'n');
        long tBB=Moves.makeMove(BB,move,'b');
        long tBQ=Moves.makeMove(BQ,move,'q');
        long tBK=Moves.makeMove(BK,move,'k');
        long tEP=Moves.makeEp(WP|BP,move);
        boolean tCWQ=CWQ, tCWK=CWK ,tCBQ=CBQ, tCBK=CBK;
        if(Character.isDigit(move.charAt(3))){
            int start = Character.getNumericValue(move.charAt(0))*8+Character.getNumericValue(move.charAt(1));
            if(((1L<<start)&WK)!=0) {tCWK=false; tCWQ=false;}
            if(((1L<<start)&BK)!=0) {tCBK=false; tCBQ=false;}
            if(((1L<<start)&WR&(1L<<63))!=0){tCWK=false;}
            if(((1L<<start)&WR&(1L<<56))!=0){tCWQ=false;}
            if(((1L<<start)&BR&(1L<<7))!=0){tCBK=false;}
            if(((1L<<start)&BR&(1L))!=0){tCBQ=false;}
        }
        UserInterface.WP=tWP;
        UserInterface.WR=tWR;
        UserInterface.WN=tWN;
        UserInterface.WB=tWB;
        UserInterface.WQ=tWQ;
        UserInterface.WK=tWK;
        UserInterface.BP=tBP;
        UserInterface.BR=tBR;
        UserInterface.BN=tBN;
        UserInterface.BB=tBB;
        UserInterface.BQ=tBQ;
        UserInterface.BK=tBK;
        UserInterface.EP=tEP;
        UserInterface.UniversalCastleWQ=tCWQ;
        UserInterface.UniversalCastleWK=tCWK;
        UserInterface.UniversalCastleBQ=tCBQ;
        UserInterface.UniversalCastleBK=tCBK;
    }

    static public void checkTime(){
        long PAWN_S,WP=RANK_4,possibility;
        String list="";
        long startTime = System.currentTimeMillis();
        for(int j=0; j<10000; j++) {
            PAWN_S=(WP>>7)&BLACK_PIECES&~FILE_A&~RANK_8;
            for(int i=0; i<64; i++){
                if(((PAWN_S>>i)&1)==1) list+=""+(i/8+1)+(i%8-1)+(i/8)+(i%8);
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("first method took "+(endTime-startTime)+" milliseconds");
        startTime = System.currentTimeMillis();
        for(int j=0; j<10000; j++) {
            PAWN_S=(WP>>7)&BLACK_PIECES&~FILE_A&~RANK_8;
            possibility=PAWN_S&~(PAWN_S-1);
            while(possibility!=0){
                int i=Long.numberOfTrailingZeros(possibility);
                if(((possibility>>i)&1)==1) list+=""+(i/8+1)+(i%8-1)+(i/8)+(i%8);
                PAWN_S&=PAWN_S&~possibility;
                possibility=PAWN_S&~(PAWN_S-1);
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
