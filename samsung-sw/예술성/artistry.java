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
    static int[] dx={-1, 0, 1, 0};
    static int[] dy={0, -1, 0, 1};
    static List<List<Point>> groups=new ArrayList<>();
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        board=new int[n][n];
        
        total=0;
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                board[i][j]=sc.nextInt();
            }
        }
        
        for(int l=0; l<4; l++) {
            grouping();
            spin();
            groups.clear();
        }
        System.out.println(total);
    }
    
    static void grouping() {
        boolean[][] visited=new boolean[n][n];
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(visited[i][j]) continue;
                groups.add(bfs(new Point(i, j), visited));
            }
        }
        
        dfs(0, 0, new int[2]);
    }
    
    static void spin() {
        int mx=n/2;
        int my=n/2;
        int[][] next=new int[n][n];
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(i==mx || j==my) {
                    next[n-j-1][i]=board[i][j];
                }
            }
        }
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(i==mx || j==my) {
                    board[i][j]=next[i][j];
                }
            }
        }
        
        square(mx, 0, 0);
        square(mx, 0, mx+1);
        square(mx, mx+1, 0);
        square(mx, mx+1, mx+1);
    }
    
    static void square(int len, int x, int y) {
        int[][] arr=new int[len][len];
        
        for(int i=0; i<len; i++) {
            for(int j=0; j<len; j++) {
                arr[i][j]=board[x+i][y+j];
            }
        }
        
        int[][] tmp=new int[len][len];
        for(int i=0; i<len; i++) {
            for(int j=0; j<len; j++) {
                tmp[j][len-i-1]=arr[i][j];
            }
        }
        arr=tmp;
        
        for(int i=0; i<len; i++) {
            for(int j=0; j<len; j++) {
                board[x+i][y+j]=arr[i][j];
            }
        }
    }
    
    static void dfs(int depth, int start, int[] arr) {
        if(depth==2) {
            int g1=arr[0];
            int g2=arr[1];
            List<Point> p1=groups.get(g1);
            List<Point> p2=groups.get(g2);
            total+=getScore(p1, p2);
        }else {
            for(int i=start; i<groups.size(); i++) {
                arr[depth]=i;
                dfs(depth+1, i+1, arr);
            }
        }
    }
    
    static int getScore(List<Point> g1, List<Point> g2) {
        Point p1=g1.get(0);
        Point p2=g2.get(0);
        int n1=board[p1.x][p1.y];
        int n2=board[p2.x][p2.y];
        int s1=g1.size();
        int s2=g2.size();
        int cnt=0;
        
        for(Point np1 : g1) {
            for(int i=0; i<4; i++) {
                int nx=np1.x+dx[i];
                int ny=np1.y+dy[i];
                
                for(Point np2 : g2) {
                    if(nx==np2.x && ny==np2.y) cnt++;
                }
            }
        }
        
        return (s1+s2)*n1*n2*cnt;
    }
    
    
    static List<Point> bfs(Point s, boolean[][] visited){
        Queue<Point> q=new ArrayDeque<>();
        List<Point> list=new ArrayList<>();
        visited[s.x][s.y]=true;
        q.offer(s);
        list.add(s);
        int num=board[s.x][s.y];
        
        while(!q.isEmpty()) {
            Point p=q.poll();
            
            for(int i=0; i<4; i++) {
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];
                
                if(nx>=0 && nx<n && ny>=0 && ny<n && 
                        board[nx][ny]==num && !visited[nx][ny]) {
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

