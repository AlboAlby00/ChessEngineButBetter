public class Perft {

    public static int nTotalMoves=0;
    static public int nMoves=0;
    public static int maxDepth=4;


    public static void perft(long WP,long WR,long WN,long WB,long WQ,long WK,long BP,long BR,long BN,long BB,long BQ,long BK,boolean CWQ, boolean CWK,boolean CBQ, boolean CBK, long EP, boolean isWhite, int depth){
       if(depth<maxDepth){
           String moves="";
           if(isWhite) moves=Moves.possibleMovesW(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BQ,BK,CWQ,CWK,EP);
           else moves=Moves.possibleMovesB(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BQ,BK,CBQ,CBK,EP);
           //System.out.println("moves: "+moves);
           for(int i=0; i<moves.length(); i+=4){
               long tWP=Moves.makeMove(WP,moves.substring(i,i+4),'P');
               long tWR=Moves.makeMove(WR,moves.substring(i,i+4),'R');
               long tWN=Moves.makeMove(WN,moves.substring(i,i+4),'N');
               long tWB=Moves.makeMove(WB,moves.substring(i,i+4),'B');
               long tWQ=Moves.makeMove(WQ,moves.substring(i,i+4),'Q');
               long tWK=Moves.makeMove(WK,moves.substring(i,i+4),'K');
               long tBP=Moves.makeMove(BP,moves.substring(i,i+4),'p');
               long tBR=Moves.makeMove(BR,moves.substring(i,i+4),'r');
               long tBN=Moves.makeMove(BN,moves.substring(i,i+4),'n');
               long tBB=Moves.makeMove(BB,moves.substring(i,i+4),'b');
               long tBQ=Moves.makeMove(BQ,moves.substring(i,i+4),'q');
               long tBK=Moves.makeMove(BK,moves.substring(i,i+4),'k');
               long tEP=Moves.makeEp(WP|BP,moves.substring(i,i+4));
               boolean tCWQ=CWQ, tCWK=CWK ,tCBQ=CBQ, tCBK=CBK;
               if(Character.isDigit(moves.charAt(i+3))){
                   int start = Character.getNumericValue(moves.charAt(i))*8+Character.getNumericValue(moves.charAt(i+1));
                   if(((1L<<start)&WK)!=0) {tCWK=false; tCWQ=false;}
                   if(((1L<<start)&BK)!=0) {tCBK=false; tCBQ=false;}
                   if(((1L<<start)&WR&(1L<<63))!=0){tCWK=false;}
                   if(((1L<<start)&WR&(1L<<56))!=0){tCWQ=false;}
                   if(((1L<<start)&BR&(1L<<7))!=0){tCBK=false;}
                   if(((1L<<start)&BR&(1L))!=0){tCBQ=false;}
               }
               if((tWK&Moves.unsafeForWhite(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BK,BQ))==0&&isWhite||
                       (tBK&Moves.unsafeForBlack(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BK,BQ))==0&&!isWhite){
                   if(depth+1==maxDepth) {nMoves++;}
                   perft(tWP,tWR,tWN,tWB,tWQ,tWK,tBP,tBR,tBN,tBB,tBQ,tBK,tCWQ,tCWK,tCBQ,tCBK,tEP,!isWhite,depth+1);
               }
           }
       }
    }

    public static void perftRoot(long WP,long WR,long WN,long WB,long WQ,long WK,long BP,long BR,long BN,long BB,long BQ,long BK,boolean CWQ, boolean CWK,boolean CBQ, boolean CBK, long EP, boolean isWhite, int depth){
        if(depth<maxDepth){
            String moves="";
            if(isWhite) moves=Moves.possibleMovesW(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BQ,BK,CWQ,CWK,EP);
            else moves=Moves.possibleMovesB(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BQ,BK,CBQ,CBK,EP);
            //System.out.println("moves: "+moves);
            for(int i=0; i<moves.length(); i+=4){
                long tWP=Moves.makeMove(WP,moves.substring(i,i+4),'P');
                long tWR=Moves.makeMove(WR,moves.substring(i,i+4),'R');
                long tWN=Moves.makeMove(WN,moves.substring(i,i+4),'N');
                long tWB=Moves.makeMove(WB,moves.substring(i,i+4),'B');
                long tWQ=Moves.makeMove(WQ,moves.substring(i,i+4),'Q');
                long tWK=Moves.makeMove(WK,moves.substring(i,i+4),'K');
                long tBP=Moves.makeMove(BP,moves.substring(i,i+4),'p');
                long tBR=Moves.makeMove(BR,moves.substring(i,i+4),'r');
                long tBN=Moves.makeMove(BN,moves.substring(i,i+4),'n');
                long tBB=Moves.makeMove(BB,moves.substring(i,i+4),'b');
                long tBQ=Moves.makeMove(BQ,moves.substring(i,i+4),'q');
                long tBK=Moves.makeMove(BK,moves.substring(i,i+4),'k');
                long tEP=Moves.makeEp(WP|BP,moves.substring(i,i+4));
                boolean tCWQ=CWQ, tCWK=CWK ,tCBQ=CBQ, tCBK=CBK;
                if(Character.isDigit(moves.charAt(i+3))){
                    int start = Character.getNumericValue(moves.charAt(i))*8+Character.getNumericValue(moves.charAt(i+1));
                    if(((1L<<start)&WK)!=0) {tCWK=false; tCWQ=false;}
                    if(((1L<<start)&BK)!=0) {tCBK=false; tCBQ=false;}
                    if(((1L<<start)&WR&(1L<<63))!=0){tCWK=false;}
                    if(((1L<<start)&WR&(1L<<56))!=0){tCWQ=false;}
                    if(((1L<<start)&BR&(1L<<7))!=0){tCBK=false;}
                    if(((1L<<start)&BR&(1L))!=0){tCBQ=false;}
                }
                if((tWK&Moves.unsafeForWhite(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BK,BQ))==0&&isWhite||
                        (tBK&Moves.unsafeForBlack(WP,WR,WN,WB,WQ,WK,BP,BR,BN,BB,BK,BQ))==0&&!isWhite){
                    perft(tWP,tWR,tWN,tWB,tWQ,tWK,tBP,tBR,tBN,tBB,tBQ,tBK,tCWQ,tCWK,tCBQ,tCBK,tEP,!isWhite,depth+1);
                    System.out.println("" + moveToStandard(moves.substring(i,i+4)) + " " + nMoves);
                    nTotalMoves+=nMoves;
                    nMoves=0;
                }
            }
        }
    }

    static public String moveToStandard(String move){
        return Character.toString(move.charAt(1)-'0'+'a')+(8-move.charAt(0)+'0')+Character.toString(move.charAt(3)-'0'+'a')+(8-move.charAt(2)+'0');
    }
}
