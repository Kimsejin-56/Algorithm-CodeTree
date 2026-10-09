import java.util.*;

class Point{
    int x, y, num, dir;
    boolean dead;
    
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public Point(int num, int x, int y) {
        this.num=num;
        this.x=x;
        this.y=y;
    }
}

public class Main {
    static int n,m,k;
    static int[][] board, effect, prev;;
    static int[] dx= {-1, 1, 0, 0};
    static int[] dy= {0, 0, -1, 1};
    static List<Point> peoples=new ArrayList<>();
    static int[][][] opDir;
  
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        k=sc.nextInt();
        board=new int[n][n];
        effect=new int[n][n];
        prev=new int[n][n];
        opDir=new int[m][4][4];
        int turn=1;
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                board[i][j]=sc.nextInt();
                if(board[i][j]>0) {
                    peoples.add(new Point(board[i][j], i, j));
                }
            }
        }
        
        peoples.sort((a,b)->a.num-b.num);
        
        for(int i=0; i<m; i++) {
            Point p=peoples.get(i);
            p.dir=sc.nextInt();
        }
        
        for(int i=0; i<m; i++) {
            for(int j=0; j<4; j++) {
                for(int d=0; d<4; d++) {
                    opDir[i][j][d]=sc.nextInt();
                }
            }
        }
        
        for(int i=0; i<n; i++) {
            prev[i]=board[i].clone();
        }
        
        for(Point p : peoples) effect[p.x][p.y]=k;
        
        while(turn<1000) {
            update();
            move();
            if(exit()) {
                System.out.println(turn);
                return;
            }
            turn++;
        }
        System.out.println(-1);
    }
    
    static boolean exit() {
        int cnt=0;
        for(Point p : peoples) {
            if(p.dead) continue;
            cnt++;
        }
        return cnt==1;
    }
    
    static void move() {
        for(Point p : peoples) {
            if(p.dead) continue;
            int x=p.x;
            int y=p.y;
            
            for(int d : opDir[p.num-1][p.dir-1]) {
                int nx=p.x+dx[d-1];
                int ny=p.y+dy[d-1];
                
                if(nx<0 || nx>=n || ny<0 || ny>=n) continue;
                if(prev[nx][ny]!=0) continue;
                
                p.x=nx;
                p.y=ny;
                p.dir=d;
                break;
            }
            
            if(p.x==x && p.y==y) {
                for(int d : opDir[p.num-1][p.dir-1]) {
                    int nx=p.x+dx[d-1];
                    int ny=p.y+dy[d-1];
                    
                    if(nx<0 || nx>=n || ny<0 || ny>=n) continue;
                    if(prev[nx][ny]==p.num) {
                        p.x=nx;
                        p.y=ny;
                        p.dir=d;
                        break;
                    }
                }
            }
        }
        
        conflict();
        reflection();
        
         for(int i=0; i<n; i++) {
             prev[i]=board[i].clone();
         }
    }
    
    static void update() {
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(effect[i][j]>0) effect[i][j]--;
            }
        }
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(board[i][j]>0) {
                    if(effect[i][j]==0) board[i][j]=0;
                }
            }
        }
    }
    
    static void reflection() {
        for(Point p : peoples) {
            if(p.dead) continue;
            
            board[p.x][p.y]=p.num;
            effect[p.x][p.y]=k;
        }
    }
    
    static void conflict() {
        int[][] exist=new int[n][n];
        
        for(int i=0; i<n; i++) {
            Arrays.fill(exist[i], Integer.MAX_VALUE);
        }
        
        for(Point p : peoples) {
            if(p.dead) continue;
            
            exist[p.x][p.y]=Math.min(exist[p.x][p.y], p.num);
        }
        
        for(Point p : peoples) {
            if(p.dead) continue;
            
            if(exist[p.x][p.y]!=p.num) {
                p.dead=true;
            }
        }
    }
}
        
        
        
        