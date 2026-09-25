import java.util.*;

class Point{
    int x, y;
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
}

public class Main {
    static int n, total;
    static int[][] board;
    static int[] dx= {-1, 0, 1, 0};
    static int[] dy= {0, -1, 0, 1};
    static Map<Integer, List<Point>> map;
    
    public static void main(String[] args) {
       Scanner sc=new Scanner(System.in);
       n=sc.nextInt();
       board=new int[n][n];
       map=new HashMap<>();
       total=0;
       int t=0;
       
       for(int i=0; i<n; i++) {
           for(int j=0; j<n; j++) {
               board[i][j]=sc.nextInt();
           }
       }
       
       while(t<4) {
           int cnt=1;
           int m=n/2;
           int len=(n-1)/2;
           int[] arr=new int[2];
           boolean[][] visited=new boolean[n][n];
           
           for(int i=0; i<n; i++) {
               for(int j=0; j<n; j++) {
                   if(visited[i][j]) continue;
                   map.put(cnt, bfs(new Point(i, j), visited));
                   cnt++;
               }
           }
           
           
           dfs(1, 0, arr);
           rotate(m, len);
           map.clear();
           t++;
       }
       System.out.println(total);
    }
    
    public static void rotate(int m, int len){
        int[][] tmp=new int[n][n];

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                if(i==m || j==m){
                    tmp[n-j-1][i]=board[i][j];
                }
            }
        }

        spin(0, 0, len, tmp);
        spin(0, m+1, len, tmp);
        spin(m+1, 0, len, tmp);
        spin(m+1, m+1, len, tmp);

        board=tmp;
    }
    
    public static void spin(int sx, int sy, int len, int[][] arr) {
        for(int i=0; i<len; i++) {
            for(int j=0; j<len; j++) {
                int nx=j;
                int ny=len-i-1;
                arr[nx+sx][ny+sy]=board[sx+i][sy+j];
            }
        }
        
    }
    
    public static boolean isAvailable(List<Point> g1, List<Point> g2) {
        for(Point p : g1) {
            for(int i=0; i<4; i++) {
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];
                
                if(nx>=0 && nx<n && ny>=0 && ny<n && board[p.x][p.y]!=board[nx][ny]) {
                    for(Point p2 : g2) {
                        if(p2.x==nx &&  p2.y==ny) return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public static int calc(List<Point> g1, List<Point> g2) {
        int s1=g1.size();
        int s2=g2.size();
        int v1=board[g1.get(0).x][g1.get(0).y];
        int v2=board[g2.get(0).x][g2.get(0).y];
        int cnt=0;
        
        for(Point p1 : g1) {
            for(int i=0; i<4; i++) {
                int nx=p1.x+dx[i];
                int ny=p1.y+dy[i];
                
                if(nx>=0 && nx<n && ny>=0 && ny<n && board[p1.x][p1.y]!=board[nx][ny]) {
                    for(Point p2 : g2) {
                        if(p2.x==nx &&  p2.y==ny) {
                            cnt++;
                            break;
                        }
                    }
                }
            }
        }
        
        return (s1+s2)*v1*v2*cnt;
    }
    
    
    public static void dfs(int start, int depth, int[] arr) {
        if(depth==2) {
            if(isAvailable(map.get(arr[0]), map.get(arr[1]))) {
                total+=calc(map.get(arr[0]), map.get(arr[1]));
            }
        }else {
            for(int i=start; i<=map.size(); i++) {
                arr[depth]=i;
                dfs(i+1, depth+1, arr);
            }
        }
    }
    
    public static List<Point> bfs(Point s, boolean[][] visited) {
        Queue<Point> q=new ArrayDeque<>();
        visited[s.x][s.y]=true;
        q.offer(s);
        List<Point> list=new ArrayList<>();
        list.add(s);
        
        while(!q.isEmpty()) {
            Point p=q.poll();
            
            for(int i=0; i<4; i++) {
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];
                
                if(nx>=0 && nx<n && ny>=0 && ny<n && !visited[nx][ny] && board[p.x][p.y]==board[nx][ny]) {
                    visited[nx][ny]=true;
                    Point t=new Point(nx, ny);
                    q.offer(t);
                    list.add(t);
                }
            }
        }
        
        return list;
    }
}
