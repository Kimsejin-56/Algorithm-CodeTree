import java.util.*;

class Point{
    int x, y, p, cp, dir, num;
    boolean dead, pass;
    
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public Point(int x, int y, int p) {
        this.x=x;
        this.y=y;
        this.p=p;
    }
}

public class Main {
    static int n, m ,k;
    static int[] dx= {0, 1, 0, -1};
    static int[] dy= {1, 0, -1, 0};
    static int[][] board;
    static int[] answer;
    static List<Point> tutles=new ArrayList<>();
    static List<Point> volcanos=new ArrayList<>();
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        k=sc.nextInt();
        int turn=1;
        answer=new int[m];
        board=new int[n][n];
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                board[i][j]=sc.nextInt();
            }
        }
        
        for(int i=0; i<m; i++) {
            Point tutle=new Point(sc.nextInt(), sc.nextInt());
            tutle.num=i;
            tutles.add(tutle);
        }
        
        for(int i=0; i<k; i++) {
            volcanos.add(new Point(sc.nextInt(), sc.nextInt(), sc.nextInt()));
        }
        
        while(turn<=100) {
            for(int i=0; i<tutles.size(); i++) {
                Point t=tutles.get(i);
                if(t.dead) continue;
                move(t);
                if(t.x==n-1 && t.y==n-1) {
                    answer[t.num]=turn;
                    tutles.remove(t);
                    i--;
                }
            }
            
            addPress();
            actionVolcano();
            init();
            turn++;
        }
        
        for(int i=0; i<m; i++) {
            if(answer[i]==0) System.out.println("-1");
            else System.out.println(answer[i]);
        }
    }
    
    static void init() {
        for(Point v : volcanos) {
            if(v.pass) {
                v.cp=0;
                v.pass=false;
            }
        }
    }
    
    static void actionVolcano() {
        int[][] copy=new int[n][n];
        for(int i=0; i<volcanos.size(); i++) {
            Point v=volcanos.get(i);
            if(v.pass) continue;
            
            if(v.p<=copy[v.x][v.y]+v.cp) {
                spread(v, copy);
                v.pass=true;
                i=-1;
            }
        }
        
        //거북이 죽음 유무 확인
        for(Point t : tutles) {
            if(copy[t.x][t.y]>=20) {
                t.dead=true;
                answer[t.num]=-1;
            }
        }
    }
    
    static void spread(Point p, int[][] copy) {
        copy[p.x][p.y]+=p.p;
        
        for(int d=0; d<4; d++) {
            int num=p.p/2;
            int x=p.x;
            int y=p.y;
            
            while(num!=0) {
                x+=dx[d];
                y+=dy[d];
                if(x>=0 && x<n&& y>=0 && y<n && board[x][y]!=1) {
                    copy[x][y]+=num;
                    num/=2;
                }else break;
            }
        }
    }
    
    static void addPress() {
        for(Point v : volcanos) {
            v.cp+=10;
        }
    }
    
    static void move(Point s) {
        Queue<Point> q=new ArrayDeque<>();
        boolean[][] visited=new boolean[n][n];
        q.offer(s);
        s.dir=-1;
        visited[s.x][s.y]=true;
        
        while(!q.isEmpty()) {
            Point p=q.poll();
            
            if(p.x==n-1 && p.y==n-1) {
                s.x+=dx[p.dir];
                s.y+=dy[p.dir];
                return;
            }
            
            for(int i=0; i<4; i++) {
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];
                
                if(nx>=0 && nx<n && ny>=0 && ny<n && board[nx][ny]==0 && 
                        !visited[nx][ny] && !hasTutle(nx, ny)) {
                    Point t=new Point(nx, ny);
                    q.offer(t);
                    visited[nx][ny]=true;
                    if(p.dir==-1) t.dir=i;
                    else t.dir=p.dir;
                }
            }
        }
     }
    
    static boolean hasTutle(int x, int y) {
        for(Point p : tutles) {
            if(p.x==x && p.y==y) return true;
        }
        return false;
    }
    
}
