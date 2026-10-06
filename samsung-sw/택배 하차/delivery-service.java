import java.util.*;

class Point {
    int x, y, k, h, w;

    public Point(int k, int h, int w, int y){
        this.k=k;
        this.h=h;
        this.w=w;
        this.y=y;
    }


}

public class Main {
    static int n, m;
    static int[][] board;
    static List<Point> boxs = new ArrayList<>();
    static List<Integer> answer=new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        board=new int[n][n];

        for(int i=0; i<m; i++){
            boxs.add(new Point(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt()-1));
        }

        //메인 로직
        for(Point b : boxs){
            init(b);
        }
        boxs.sort((a,b)->a.k-b.k);
        
        while(!boxs.isEmpty()){
            for(Point b : boxs){
                if(moveLeft(b)) {
                    boxs.remove(b);
                    break;
                }
            }
            moveDown();

            for(Point b : boxs){
                if(moveRight(b)) {
                    boxs.remove(b);
                    break;
                }
            }
            moveDown();
        }

        for(int i : answer) System.out.println(i);
    }

    static void moveDown(){
        for(int i=0; i<boxs.size(); i++){
            Point b=boxs.get(i);
            int nx=b.x+b.h-1;

            while(isGraviyry(b, nx)){
                nx++;
            }

            if(b.x+b.h-1!=nx){
                for(int r=b.x; r<b.x+b.h; r++) {
                    for (int c=b.y; c<b.y+b.w; c++) {
                        board[r][c]=0;
                    }
                }
                b.x=nx-b.h+1;
                for(int r=b.x; r<b.x+b.h; r++) {
                    for (int c=b.y; c<b.y+b.w; c++) {
                        board[r][c]=b.k;
                    }
                }

                i=-1;
            }
        }
    }

    static boolean isGraviyry(Point b, int x){
        for(int j=b.y; j<b.y+b.w; j++){
            if(x+1<0 || x+1>=n || j<0 || j>=n) return false;
            if(board[x+1][j]!=0) return false;
        }

        return true;
    }

    static boolean isDown(Point b, int x){
        for(int i=0; i<b.h; i++){
            for(int j=b.y; j<b.y+b.w; j++){
                int nx=x+i+1;
                if(nx<0 || nx>=n || j<0 || j>=n) return false;
                if(board[nx][j]!=0) return false;
            }
        }
        return true;
    }

    static boolean moveRight(Point b){
        int ny=b.y+b.w-1;
        while(isRight(b, ny)){
            ny++;
        }

        if(ny==n-1){
            answer.add(b.k);
            for(int i=b.x; i<b.x+b.h; i++) {
                for (int j=b.y; j<b.y+b.w; j++) {
                    board[i][j]=0;
                }
            }
            return true;
        }

        return false;
    }

    static boolean isRight(Point b, int y){
        for(int i=b.x; i<b.x+b.h; i++){
            int ny=y+1;
            if(i<0 || i>=n || ny<0 || ny>=n) return false;
            if(board[i][ny]!=0) return false;
        }
        return true;
    }

    static boolean moveLeft(Point b){
        int ny=b.y;
        while(isLeft(b, ny)){
            ny--;
        }

        if(ny==0){
            answer.add(b.k);
            for(int i=b.x; i<b.x+b.h; i++) {
                for (int j=b.y; j<b.y+b.w; j++) {
                    board[i][j]=0;
                }
            }
            return true;
        }

        return false;
    }

    static boolean isLeft(Point b, int y){
        for(int i=b.x; i<b.x+b.h; i++){
            int ny=y-1;
            if(i<0 || i>=n || ny<0 || ny>=n) return false;
            if(board[i][ny]!=0) return false;

        }
        return true;
    }

    static void init(Point b){
        int nx=b.x;
        while(isDown(b, nx)){
            nx++;
            b.x=nx;
        }

        for(int i=b.x; i<b.x+b.h; i++) {
            for (int j=b.y; j<b.y+b.w; j++) {
                board[i][j]=b.k;
            }
        }
    }
}
