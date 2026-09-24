import java.util.*;

class Point implements Comparable<Point>{
    int x, y, t;
    
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
    
    public int compareTo(Point p) {
        int sum1=this.x+this.y;
        int sum2=p.x+p.y;
        if(sum1==sum2) return p.y-this.y;
        return sum2-sum1;
    }
}

public class Main {
    static int n, m, k;
    static int[][] board;
    static int[] dx= {0, 1, 0, -1};
    static int[] dy= {1, 0, -1, 0};
    static int[] cx= {0, 1, 0, -1, 1, -1, 1, -1};
    static int[] cy= {1, 0, -1, 0, 1, 1, -1, -1};
    static List<Point> towers;
    static Point weak, strong;
    static boolean[][] demage;
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        k=sc.nextInt();
        board=new int[n][m];
        List<Point> weaks=new ArrayList<>();
        List<Point> strongs=new ArrayList<>();
        towers=new ArrayList<>();
        int turn=1;
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<m; j++) {
                board[i][j]=sc.nextInt();
                if(board[i][j]!=0) {
                    towers.add(new Point(i, j));
                }
            }
        }
        
        while(turn<=k) {
            if(towers.size() <= 1) break;
            demage=new boolean[n][m];
            selectWeak(weaks);
            weaks.clear();
            board[weak.x][weak.y]+=(n+m);
            demage[weak.x][weak.y]=true;
            selectStrong(strongs);
            strongs.clear();
            weak.t=turn;
            
            if(!razor()) canon();
            init();
            
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
    
    public static boolean razor() {
        int[][] dis;
        dis=bfs(strong);
        List<Point> move=new ArrayList<>();
        Point p=new Point(weak.x, weak.y);
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<m; j++) {
                for(int d=0; d<4; d++) {
                    int nx=p.x+dx[d];
                    int ny=p.y+dy[d];

                    if(nx<0) nx=n-1;
                    if(ny<0) ny=m-1;
                    if(nx>=n) nx=0;
                    if(ny>=m) ny=0;
            
                    
                    if(board[nx][ny]!=0 && dis[p.x][p.y]>dis[nx][ny]) {
                        p.x=nx;
                        p.y=ny;
                        if(dis[nx][ny]!=0)move.add(new Point(nx, ny));
                        break;
                    }
                }
            }
        }
        
        if(p.x!=strong.x || p.y!=strong.y) return false;
        
        int num=board[weak.x][weak.y]/2;
        for(Point t : move) {
            board[t.x][t.y]-=num;
            demage[t.x][t.y]=true;
            if(board[t.x][t.y]<=0) {
                board[t.x][t.y]=0;
                for(int i=0; i<towers.size(); i++) {
                    Point tower=towers.get(i);
                    if(t.x==tower.x && t.y==tower.y) {
                        towers.remove(tower);
                        break;
                    }
                }
            }
        }
        board[strong.x][strong.y]-=board[weak.x][weak.y];
        demage[strong.x][strong.y]=true;
        if(board[strong.x][strong.y]<=0){
            board[strong.x][strong.y]=0;
            towers.remove(strong);
        }
        
        return true;
    }
    
    public static void canon() {
        Point p=new Point(strong.x, strong.y);
        int num=board[weak.x][weak.y]/2;
        
        for(int i=0; i<8; i++) {
            int nx=p.x+cx[i];
            int ny=p.y+cy[i];
            
            if(nx<0) nx=n-1;
            if(ny<0) ny=m-1;
            if(nx>=n) nx=0;
            if(ny>=m) ny=0;
            
            if(board[nx][ny]!=0) {
                if(nx==weak.x && ny==weak.y) continue;
                board[nx][ny]-=num;
                demage[nx][ny]=true;
                
                if(board[nx][ny]<=0) {
                    board[nx][ny]=0;
                    for(int j=0; j<towers.size(); j++) {
                        Point tower=towers.get(j);
                        if(nx==tower.x && ny==tower.y) {
                            towers.remove(tower);
                            break;
                        }
                    }
                }
            }
        }
        
        board[strong.x][strong.y]-=board[weak.x][weak.y];
        demage[strong.x][strong.y]=true;
        if(board[strong.x][strong.y]<=0){
            board[strong.x][strong.y]=0;
            towers.remove(strong);
        }
    }
    
    public static void init() {
        for(int i=0; i<n; i++) {
            for(int j=0; j<m; j++) {
                if(board[i][j]!=0 && !demage[i][j]) {
                    board[i][j]++;
                }
            }
        }
    }
    
    public static int[][] bfs(Point s) {
        Queue<Point> q=new ArrayDeque<>();
        boolean[][] visited=new boolean[n][m];
        int[][] dis=new int[n][m];
        q.offer(s);
        visited[s.x][s.y]=true;
        
        while(!q.isEmpty()) {
            Point p=q.poll();
            for(int i=0; i<4; i++) {
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];
                
                if(nx<0) nx=n-1;
                if(ny<0) ny=m-1;
                if(nx>=n) nx=0;
                if(ny>=m) ny=0;
                
                if(!visited[nx][ny] && board[nx][ny]!=0) {
                    visited[nx][ny]=true;
                    q.offer(new Point(nx, ny));
                    dis[nx][ny]=dis[p.x][p.y]+1;
                }
            }
        }
        
        return dis;
    }
    
    public static void selectWeak(List<Point> weaks) {
        int min=Integer.MAX_VALUE;
        List<Point> list=new ArrayList<>();
        for(int i=0; i<towers.size(); i++) {
            Point p=towers.get(i);
            if(min>board[p.x][p.y]) {
                min=board[p.x][p.y];
                weaks.clear();
                weaks.add(p);
            }else if(min==board[p.x][p.y]) weaks.add(p);
        }
        
        if(weaks.size()==1) {
            weak=weaks.get(0);
            return;
        }
        
        int max=Integer.MIN_VALUE;
        for(int i=0; i<weaks.size(); i++) {
            Point p=weaks.get(i);
            if(max<p.t) {
                max=p.t;
                list.clear();
                list.add(p);
            }else if(max==p.t) list.add(p);
        }
        
        Collections.sort(list);
        if(!list.isEmpty()) {
            weak=list.get(0);
        }
    }
    
    public static void selectStrong(List<Point> strongs) {
        int max=Integer.MIN_VALUE;
        List<Point> list=new ArrayList<>();
        for(int i=0; i<towers.size(); i++) {
            Point p=towers.get(i);

            if(p.x==weak.x && p.y==weak.y) continue;

            if(max<board[p.x][p.y]) {
                max=board[p.x][p.y];
                strongs.clear();
                strongs.add(p);
            }else if(max==board[p.x][p.y]) strongs.add(p);
        }
        
        if(strongs.size()==1) {
            strong=strongs.get(0);
            return;
        }
        
        int min=Integer.MAX_VALUE;
        for(int i=0; i<strongs.size(); i++) {
            Point p=strongs.get(i);
            if(min>p.t) {
                min=p.t;
                list.clear();
                list.add(p);
            }else if(min==p.t) list.add(p);
        }
        
        Collections.sort(list);
        if(!list.isEmpty()) {
            int len=list.size()-1;
            strong=list.get(len);
        }
    }
}
