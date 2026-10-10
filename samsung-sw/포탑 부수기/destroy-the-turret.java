import java.util.*;

class Point{
    int x, y, t;
    
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
}

public class Main {
    static int n, m, k, turn;
    static int[][] board;
    static boolean[][] check;
    static List<Point> towers=new ArrayList<>();
    static int[] dx= {0, 1, 0, -1, -1, -1, 1, 1};
    static int[] dy= {1, 0, -1, 0, -1, 1, -1, 1};
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        k=sc.nextInt();
        board=new int[n][m];
        turn=1;
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<m; j++) {
                board[i][j]=sc.nextInt();
                if(board[i][j]>0) {
                    towers.add(new Point(i, j));
                }
            }
        }
        
        while(turn<=k) {
            check=new boolean[n][m];
            Point weak=selectWeak();
            Point strong=selectStrong();
            board[weak.x][weak.y]+=n+m;
            
            attack(weak, strong);
            brokenTower();
            refreshTower();
            if(towers.size()==1) break;
            turn++;
        }
        
        int max=0;
        for(int i=0; i<n; i++) {
            for(int j=0; j<m; j++) {
                max=Math.max(max, board[i][j]);
            }
        }
        System.out.println(max);
    }
    
    static void refreshTower() {
        for(int i=0; i<n; i++) {
            for(int j=0; j<m; j++) {
                if(board[i][j]>0 && !check[i][j]) board[i][j]++;
            }
        }
    }
    
    static void brokenTower() {
        for(int i=0; i<n; i++) {
            for(int j=0; j<m; j++) {
                if(board[i][j]<0) board[i][j]=0;
            }
        }
        
        for(int i=0; i<towers.size(); i++) {
            Point t=towers.get(i);
            if(board[t.x][t.y]==0) {
                towers.remove(t);
                i--;
            }
        }
    }
    
    static void attack(Point s, Point e) {
        if(razor(s, e)) return;
        else canon(s, e);
        
    }
    
    static void canon(Point s, Point e) {
        for(int i=0; i<8; i++) {
            int nx=e.x+dx[i];
            int ny=e.y+dy[i];
            
            if(nx<0) nx=n-1;
            if(nx>=n) nx=0;
            if(ny<0) ny=m-1;
            if(ny>=m) ny=0;
            
            if(s.x==nx && s.y==ny) continue;
            
            if(board[nx][ny]>0) {
                check[nx][ny]=true;
                board[nx][ny]-=board[s.x][s.y]/2;
            }
        }
        check[s.x][s.y]=true;
        check[e.x][e.y]=true;
        board[e.x][e.y]-=board[s.x][s.y];
    }
    
    
    static boolean razor(Point s, Point e) {
        int[][] dist=bfs(e);
        List<Point> path=getPath(s, e, dist);
        
        if(path==null) return false;
            
        for(Point p : path) {
            if(p.x==e.x && p.y==e.y) board[e.x][e.y]-=board[s.x][s.y];
             else board[p.x][p.y]-=board[s.x][s.y]/2;
        }
        
        return true;
    }
    
    static List<Point> getPath(Point s, Point e, int[][] dist) {
        List<Point> path=new ArrayList<>();
        check[s.x][s.y]=true;
        int x=s.x;
        int y=s.y;
        
        while(x!=e.x || y!=e.y) {
            boolean move=false;
            for(int i=0; i<4; i++) {
                int nx=x+dx[i];
                int ny=y+dy[i];
                
                if(nx<0) nx=n-1;
                if(nx>=n) nx=0;
                if(ny<0) ny=m-1;
                if(ny>=m) ny=0;
                
                if(dist[x][y]>dist[nx][ny] && board[nx][ny]>0) {
                    check[nx][ny]=true;
                    path.add(new Point(nx, ny));
                    x=nx;
                    y=ny;
                    move=true;
                    break;
                }
            }
            if(!move) return null;
        }
        return path;
    }
    
    static int[][] bfs(Point s){
        int[][] dist=new int[n][m];
        Queue<Point> q=new ArrayDeque<>();
        boolean[][] visited=new boolean[n][m];
        q.offer(s);
        visited[s.x][s.y]=true;
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<m; j++) {
                dist[i][j]=Integer.MAX_VALUE;
            }
        }
        dist[s.x][s.y]=0;
        
        while(!q.isEmpty()) {
            Point p=q.poll();
            for(int i=0; i<4; i++) {
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];
                
                if(nx<0) nx=n-1;
                if(nx>=n) nx=0;
                if(ny<0) ny=m-1;
                if(ny>=m) ny=0;
                
                if(visited[nx][ny] || board[nx][ny]==0) continue;
                
                visited[nx][ny]=true;
                dist[nx][ny]=dist[p.x][p.y]+1;
                q.offer(new Point(nx, ny));
            }
        }
        return dist;
    }
    
    static Point selectStrong() {
        int ma=Integer.MIN_VALUE;
        int mr=Integer.MAX_VALUE;;
        int mxy=Integer.MAX_VALUE;;
        int my=Integer.MAX_VALUE;;
        Point strong=new Point(0, 0);
        
        for(Point p : towers) {
            if(bestStrong(ma, mr, mxy, my, p)) {
                ma=board[p.x][p.y];
                mr=p.t;
                mxy=p.x+p.y;
                my=p.y;
                strong=p;
            }
        }
        
        return strong;
    }
    
    static boolean bestStrong(int ma, int mr, int mxy, int my, Point p) {
        if(ma!=board[p.x][p.y]) return ma<board[p.x][p.y];
        if(mr!=p.t) return mr>p.t;
        if(mxy!=p.x+p.y) return mxy>p.x+p.y;
        return my>p.y;
    }
    
    static Point selectWeak() {
        int ma=Integer.MAX_VALUE;
        int mr=Integer.MIN_VALUE;
        int mxy=Integer.MIN_VALUE;
        int my=Integer.MIN_VALUE;
        Point weak=new Point(0, 0);
        
        for(Point p : towers) {
            if(bestWeak(ma, mr, mxy, my, p)) {
                ma=board[p.x][p.y];
                mr=p.t;
                mxy=p.x+p.y;
                my=p.y;
                weak=p;
            }
        }
        
        weak.t=turn;
        return weak;
    }
    
    static boolean bestWeak(int ma, int mr, int mxy, int my, Point p) {
        if(ma!=board[p.x][p.y]) return ma>board[p.x][p.y];
        if(mr!=p.t) return mr<p.t;
        if(mxy!=p.x+p.y) return mxy<p.x+p.y;
        return my<p.y;
    }
}


