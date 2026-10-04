import java.util.*;

class Point{
    int x, y, dir, num;
    public Point(int num, int x, int y, int dir) {
        this.num=num;
        this.x=x;
        this.y=y;
        this.dir=dir;
    }
    
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
    
    public void clock() {
        this.dir++;
        if(this.dir>3) this.dir=0; 
    }

    public void oclock() {
        this.dir--;
        if(this.dir<0) this.dir=3; 
    }
}

public class Main {
    static int r, c, k;
    static int[][] board;
    static int[] dx= {-1, 0, 1, 0};
    static int[] dy= {0, 1, 0, -1};
    static List<Point> angels=new ArrayList<>();
    static boolean[][] exit;
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        r=sc.nextInt();
        c=sc.nextInt();
        k=sc.nextInt();
        board=new int[r+3][c];
        exit=new boolean[r+3][c];
        int total=0;
        
        for(int i=0; i<k; i++) {
            angels.add(new Point(i+1, 1, sc.nextInt()-1, sc.nextInt()));
        }
        
        for(Point p : angels) {
            move(p);
            if(isInit()) {
                board=new int[r+3][c];
                exit=new boolean[r+3][c];
            }else total+=bfs(p)-2;
        }
        System.out.println(total);        
    }
    
    static int bfs(Point s) {
        Queue<Point> q=new ArrayDeque<>();
        boolean[][] visited=new boolean[r+3][c];
        q.offer(s);
        visited[s.x][s.y]=true;
        int max=s.x;
        
        while(!q.isEmpty()) {
            Point p=q.poll();
            
            for(int i=0; i<4; i++) {
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];
                
                if(nx>=0 && nx<r+3 && ny>=0 && ny<c && !visited[nx][ny] && board[nx][ny]>0) {  
                    if(board[nx][ny]==board[p.x][p.y] || exit[p.x][p.y]) {
                        visited[nx][ny]=true;
                        max=Math.max(max, nx);
                        q.offer(new Point(nx, ny));
                    }
                }
            }
        }
        
        return max;
    }
    
    static void move(Point p) {
        while(true) {
            if(isDown(p)) {
                p.x++;
                continue;
            }else if(isLeft(p)) {
                p.y--;
                p.x++;
                p.oclock();
                continue;
            }else if(isRight(p)) {
                p.y++;
                p.x++;
                p.clock();
                continue;
            }else break;
        }
        
        board[p.x-1][p.y]=p.num;
        board[p.x][p.y]=p.num;
        board[p.x+1][p.y]=p.num;
        board[p.x][p.y-1]=p.num;
        board[p.x][p.y+1]=p.num;
        exit[p.x+dx[p.dir]][p.y+dy[p.dir]]=true;
    }
    
    static boolean isInit() {
        for(int i=0; i<3; i++) {
            for(int j=0; j<c; j++) {
                if(board[i][j]>0) return true;
            }
        }
        return false;
    }
    
    static boolean isDown(Point p) {
        if(p.x+2<r+3 && p.y-1>=0 && p.y+1<c && board[p.x+1][p.y-1]==0 
                && board[p.x+1][p.y+1]==0 && board[p.x+2][p.y]==0) {
            return true;
        }
        return false;
    }
    
    static boolean isLeft(Point p) {
        if(p.x+2<r+3 && p.y-2>=0 && p.x-1>=0 &&
                board[p.x][p.y-2]==0 && board[p.x-1][p.y-1]==0 && 
                board[p.x+1][p.y-1]==0 && board[p.x+2][p.y-1]==0 && board[p.x+1][p.y-2]==0) {
            return true;
        }
        return false;
    }
    
    static boolean isRight(Point p) {
        if(p.x+2<r+3 && p.y+2<c && p.x-1>=0 &&
                board[p.x][p.y+2]==0 && board[p.x-1][p.y+1]==0 && 
                board[p.x+1][p.y+1]==0 && board[p.x+2][p.y+1]==0 && board[p.x+1][p.y+2]==0) {
            return true;
        }
        return false;
    }
}

