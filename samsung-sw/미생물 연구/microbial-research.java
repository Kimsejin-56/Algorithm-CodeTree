import java.util.*;

class Point{
    int x, y, num;
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
}

class Info{
    int num, x1, y1, x2, y2; 
    
    public Info(int num, int y1, int x1, int y2, int x2) {
        this.num=num;
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
    }
}

public class Main {
    static int n, q, total;
    static int[][] board;
    static List<Info> result=new ArrayList<>();
    static List<List<Point>> creatures=new ArrayList<>();
    static int[] dx= {-1, 0, 1, 0};
    static int[] dy= {0, -1, 0, 1};
    static int[] size;
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        q=sc.nextInt();
        board=new int[n][n];
        size=new int[q];
        
        for(int i=0; i<q; i++) {
            result.add(new Info(i+1, sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt()));
        }
        
        for(int i=0; i<q; i++) {
            total=0;
            Info info=result.get(i);
            batch(info);
            removeCreature();
            moveCreature();
            creatures.clear();
            boolean[][] visited=new boolean[n][n];
            for(int l=0; l<n; l++) {
                for(int j=0; j<n; j++) {
                    if(visited[l][j] || board[l][j]==0) continue;
                    creatures.add(bfs(new Point(l, j), visited));
                }
            
            }
            getScore(0, 0, new int[2]);
            System.out.println(total);
            creatures.clear();
        }
    }
    
    static boolean isOverlap(List<Point> g1, List<Point> g2) {
        for(Point p1 : g1) {
            for(int i=0; i<4; i++) {
                int nx=p1.x+dx[i];
                int ny=p1.y+dy[i];
                
                for(Point p2 : g2) {
                    if(nx==p2.x && ny==p2.y) return true;
                    
                }
            }
        }
        return false;
    }
    
    static void getScore(int depth, int start, int[] arr) {
        if(depth==2) {
            List<Point> g1=creatures.get(arr[0]);
            List<Point> g2=creatures.get(arr[1]);
            if(isOverlap(g1, g2)) {
                total+=g1.size()*g2.size();
            }
            
        }else {
            for(int i=start; i<creatures.size(); i++) {
                arr[depth]=i;
                getScore(depth+1, i+1, arr);
            }
        }
    }
    
    static void moveCreature() {
        int[][] move=new int[n][n];
        
        creatures.sort((a, b) -> {
            if(a.size()==b.size()) return a.get(0).num -b.get(0).num;
            return b.size()-a.size();
        });
        
        for(int i=0; i<creatures.size(); i++) {
            List<Point> list=creatures.get(i);
            boolean stop=false;
            for(int c=0; c<n; c++) {
                for(int r=0; r<n; r++) {
                    if(isPut(r, c, list, move)) {
                        put(r, c, list, move);
                        stop=true;
                        break;
                    }
                }
                if(stop) break;
            }
            board=move;
        }
    }
    
    static boolean isPut(int x, int y, List<Point> list, int[][] move) {
        int mx=Integer.MAX_VALUE;
        int my=Integer.MAX_VALUE;
        for(Point c : list) {
            mx=Math.min(c.x, mx);
            my=Math.min(c.y, my);
        }
        
        for(Point c : list) {
            int nx=x+(c.x-mx);
            int ny=y+(c.y-my);
            if(nx<0 || nx>=n || ny<0 || ny>=n) return false;
            if(move[nx][ny]!=0) return false;
        }
        return true;
    }
    
    static boolean put(int x, int y, List<Point> list, int[][] move) {
        int mx=Integer.MAX_VALUE;
        int my=Integer.MAX_VALUE;
        for(Point c : list) {
            mx=Math.min(c.x, mx);
            my=Math.min(c.y, my);
        }
        
        for(Point c : list) {
            int nx=x+(c.x-mx);
            int ny=y+(c.y-my);
            move[nx][ny]=c.num;
        }
        return true;
    }
    
    static void removeCreature() {
        boolean[][] visited=new boolean[n][n];
        int[] nc=new int[q];
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(visited[i][j] || board[i][j]==0) continue;
                nc[board[i][j]-1]++;
                creatures.add(bfs(new Point(i, j), visited));
            }
        }
        
        for(int i=0; i<q; i++) {
            if(nc[i]>=2) {
                for(int j=0; j<creatures.size(); j++) {
                    List<Point> list=creatures.get(j);
                    int num=list.get(0).num;
                    if(num==i+1) {
                        for(Point p : list) board[p.x][p.y]=0;
                        creatures.remove(list);
                        j--;
                    }
                }
            }
        }
    }
    
    static List<Point> bfs(Point s, boolean[][] visited){
        Queue<Point> q=new ArrayDeque<>();
        List<Point> list=new ArrayList<>();
        int num=board[s.x][s.y];
        s.num=num;
        q.offer(s);
        list.add(s);
        visited[s.x][s.y]=true;
        
        while(!q.isEmpty()) {
            Point p=q.poll();
            
            for(int i=0; i<4; i++) {
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];
                
                if(nx>=0 && nx<n && ny>=0 && ny<n && 
                        !visited[nx][ny] && board[nx][ny]==num) {
                    visited[nx][ny]=true;
                    Point t=new Point(nx, ny);
                    t.num=num;
                    q.offer(t);
                    list.add(t);
                }
            }
        }
        return list;
    }
    
    static void batch(Info info) {
        for(int i=info.x1; i<info.x2; i++) {
            for(int j=info.y1; j<info.y2; j++) {
                board[i][j]=info.num;
            }
        }
    }
}

