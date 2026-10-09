import java.util.*;

class Point{
    int x, y, d, v, l, time;
    
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
    
    public Point(int x, int y, int d, int v) {
        this.x=x;
        this.y=y;
        this.d=d;
        this.v=v;
    }
    
    public Point(int x, int y, int l) {
        this.x=x;
        this.y=y;
        this.l=l;
    }
}

public class Main {
    static int n, m, f;
    static int[][] board, timeSpace;
    static int[][][] walls;
    static List<Point> time=new ArrayList<>();
    static int[] dx= {0, 0, 1, -1};
    static int[] dy= {1, -1, 0, 0};
    static Point tp, sw;
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        f=sc.nextInt();
        board=new int[n][n];
        timeSpace=new int[n][n];
        walls=new int[5][m][m];
        boolean pass=false;
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                board[i][j]=sc.nextInt();
            }
        }

        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(board[i][j]==3) {
                    sw=new Point(i, j);
                    pass=true;
                    break;
                }
            }
            if(pass) break;
        }
        
        for(int l=0; l<5; l++) {
            for(int i=0; i<m; i++) {
                for(int j=0; j<m; j++) {
                    walls[l][i][j]=sc.nextInt();
                    if(walls[l][i][j]==2) tp=new Point(i, j, l);
                }
            }
        }
        
        for(int i=0; i<f; i++) {
            time.add(new Point(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt()));
        }
        
        timeSpace();
        if(bfsWall()) {
            bfs();
            if(board[tp.x][tp.y]!=4) {
                System.out.println(-1);
                return;
            }
            System.out.println(tp.time);
        }else System.out.println(-1);
    }
    
    static void bfs() {
        boolean[][]visited=new boolean[n][n];
        Queue<Point> q=new ArrayDeque<>();
        q.offer(tp);
        visited[tp.x][tp.y]=true;
        
        while(!q.isEmpty()) {
            Point p=q.poll();
            
            for(int d=0; d<4; d++) {
                int nx=p.x+dx[d];
                int ny=p.y+dy[d];
                int nt=p.time+1;
                
                if(nx<0 || nx>=n || ny<0 || ny>=n) continue; 
                
                if(board[nx][ny]==4) {
                    tp.x=nx;
                    tp.y=ny;
                    tp.time=nt;
                    return;
                }
                
                if(board[nx][ny]==0 && !visited[nx][ny] && timeSpace[nx][ny]>nt) {
                    visited[nx][ny]=true;
                    Point t=new Point(nx, ny);
                    t.time=nt;
                    q.offer(t);
                }
            }
        }
    }
   
    static boolean bfsWall() {
        boolean[][][] wall=new boolean[5][m][m];
        Queue<Point> q=new ArrayDeque<>();
        q.offer(tp);
        wall[tp.l][tp.x][tp.y]=true;
        
        while(!q.isEmpty()) {
            Point p=q.poll();
            
            for(int d=0; d<4; d++) {
                int nx=p.x+dx[d];
                int ny=p.y+dy[d];
                int nl=p.l;
                
                if(nl==4) {
                    int[] up=upWall(nx, ny, nl);
                    nx=up[0];
                    ny=up[1];
                    nl=up[2];
                }else {
                    int[] side=sideWall(nx, ny, nl);
                    nx=side[0];
                    ny=side[1];
                    nl=side[2];
                    
                    if(nl==5) {
                        if(nx<0 || nx>=n || ny<0 || ny>=n)
                            continue;
                        if(board[nx][ny]==0 && timeSpace[nx][ny]>p.time+1) {
                            tp.x=nx;
                            tp.y=ny;
                            tp.time=p.time+1;
                            return true;
                        }
                        continue;
                    }
                }
                
                if(walls[nl][nx][ny]==0 && !wall[nl][nx][ny]) {
                    wall[nl][nx][ny]=true;
                    Point t=new Point(nx, ny, nl);
                    t.time=p.time+1;
                    q.offer(t);
                }
            }
        }
        return false;
    }
    
    static int[] sideWall(int nx, int ny, int nl) {
        if(nl==0) {
            if(nx<0) {
                nx=m-1-ny;
                ny=m-1;
                nl=4;
            }
            else if(nx>=m) {
                nl=5;
                nx=sw.x+m-1-ny;
                ny=sw.y+m;
            }else if(ny<0) {
                nl=2;
                ny=m-1;
            }else if(ny>=m) {
                nl=3;
                ny=0;
            }
        }else if(nl==1) {
            if(nx<0){
                nl=4;
                nx=ny;
                ny=0;
            }else if(nx>=m) {
                nl=5;
                nx=sw.x+ny;
                ny=sw.y-1;
            }else if(ny<0) {
                nl=3;
                ny=m-1;
            }else if(ny>=m) {
                nl=2;
                ny=0;
            }
        }else if(nl==2) {
            if(nx<0){
                nl=4;
                nx=m-1;
            }else if(nx>=m) {
                nl=5;
                nx=sw.x+m;
                ny=sw.y+ny;
            }else if(ny<0) {
                nl=1;
                ny=m-1;
            }else if(ny>=m) {
                nl=0;
                ny=0;
            }
        }else if(nl==3) {
            if(nx<0) {
                nl=4;
                nx=0;
                ny=m-1-ny;
            }else if(nx>=m) {
                nl=5;
                nx=sw.x-1;
                ny=sw.y+m-1-ny;
            }else if(ny<0) {
                nl=0;
                ny=m-1;
            }else if(ny>=m) {
                nl=1;
                ny=0;
            }
        }
        
        return new int[] {nx, ny, nl};
    }
    
    static int[] upWall(int nx, int ny, int nl) {
        if(nx<0) {
            nl=3;
            ny=m-1-ny;
            nx=0;
        }else if(nx>=m) {
            nl=2;
            nx=0;
        }else if(ny<0) {
            nl=1;
            ny=nx;
            nx=0;
        }else if(ny>=m) {
            nl=0;
            ny=m-1-nx;
            nx=0;
        }
        
        return new int[] {nx, ny, nl};
    }
    
    static void timeSpace() {
        
        for(int i=0; i<n; i++) {
            Arrays.fill(timeSpace[i], Integer.MAX_VALUE);
        }
        
        for(Point p :time) {
            int cnt=1;
            int nx=p.x+dx[p.d];
            int ny=p.y+dy[p.d];
            timeSpace[p.x][p.y]=1;
            
            while(true) {
                int t=p.v*cnt;
                if(nx<0 || nx>=n || ny<0 || ny>=n) break;
                if(board[nx][ny]!=0) break;

                if(timeSpace[nx][ny]>t) {
                    timeSpace[nx][ny]=t;
                }

                cnt++;
                nx+=dx[p.d];
                ny+=dy[p.d];
            }
        }
    }
}
        
        
        
        