import java.util.*;

class Point implements Comparable<Point>{
    int x, y, num, dir;
    boolean close;
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
    
    public int compareTo(Point p) {
        if(p.x==this.x) return this.y-p.y;
        return this.x-p.x;
    }
}

public class Main {
    static int n, m; 
    static int[] dx= {-1, 0, 0, 1};
    static int[] dy= {0, -1, 1, 0};
    static int[][] board;
    static List<Point> stores=new ArrayList<>();
    static List<Point> camps=new ArrayList<>();
    static List<Point> peoples=new ArrayList<>();
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        board=new int[n][n];
        int turn=1;
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                board[i][j]=sc.nextInt();
                if(board[i][j]==1) camps.add(new Point(i, j));
            }
        }
        
        for(int i=0; i<m; i++) {
            Point s=new Point(sc.nextInt()-1, sc.nextInt()-1);
            s.num=i+1;
            stores.add(s);
        }
        
        while(true) {
            for(Point p : peoples) {
                if(p.close) continue;
                Point store=stores.get(p.num-1);
                move(p, store);
            }

            for(Point p : peoples) {
                if(p.close) continue;
                Point store=stores.get(p.num-1);
                if(p.x==store.x && p.y==store.y) {
                    p.close=true;
                    store.close=true;
                }
            }
            
            if(turn<=m) {
                selectStroe(turn);
            }
            
            int cnt=0;
            for(Point p : peoples) {
                if(p.close) cnt++;
            }
            
            if(cnt==m) {
                System.out.println(turn);
                return;
            }    
            
            turn++;
        }
    }
    
    static void move(Point s, Point e) {
        Queue<Point> q=new ArrayDeque<>();
        boolean[][] visited=new boolean[n][n];
        q.offer(s);
        visited[s.x][s.y]=true;
        s.dir=-1;
        
        while(!q.isEmpty()) {
            Point p=q.poll();
            
            if(p.x==e.x && p.y==e.y) {
                s.x+=dx[p.dir];
                s.y+=dy[p.dir];
                return;
            }
            
            for(int i=0; i<4; i++) {
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];
                
                if(nx>=0 && nx<n && ny>=0 && ny<n && !visited[nx][ny] && isMove(nx, ny)) {
                    visited[nx][ny]=true;
                    Point np=new Point(nx, ny);
                    if(p.dir==-1) np.dir=i;
                    else np.dir=p.dir;
                    q.offer(np);
                }
            }
                
        }
    }
    
    static void selectStroe(int t) {
        Point store=stores.get(t-1);
        int min=Integer.MAX_VALUE;
        List<Point> list=new ArrayList<>();
        
        for(Point c : camps) {
            if(c.close) continue;
            int num=bfs(c, store);
            if(num==-1) continue;
            
            if(min>num) {
                min=num;
                list.clear();
                list.add(c);
            }else if(min==num) list.add(c);
        }
        
        Collections.sort(list);
        Point camp=list.get(0);
        camp.close=true;
        Point p=new Point(camp.x, camp.y);
        p.num=t;
        peoples.add(p);
        
    }
    
    static int bfs(Point s, Point e) {
        Queue<Point> q=new ArrayDeque<>();
        boolean[][] visited=new boolean[n][n];
        q.offer(s);
        visited[s.x][s.y]=true;
        int level=0;
        
        while(!q.isEmpty()) {
            int len=q.size();
            
            for(int l=0; l<len; l++) {
                Point p=q.poll();
                
                if(p.x==e.x && p.y==e.y) {
                    return level;
                }
                
                for(int i=0; i<4; i++) {
                    int nx=p.x+dx[i];
                    int ny=p.y+dy[i];
                    
                    if(nx>=0 && nx<n && ny>=0 && ny<n && !visited[nx][ny] && isMove(nx, ny)) {
                        visited[nx][ny]=true;
                        q.offer(new Point(nx, ny));
                    }
                }
            }
            level++;
        }
        return -1;
    }
    
    static boolean isMove(int x, int y) {
        for(Point c : camps) {
            if(!c.close) continue;
            if(c.x==x && c.y==y) return false;
        }
        
        for(Point s : stores) {
            if(!s.close) continue;
            if(s.x==x && s.y==y) return false;
        }
        
        return true;
    }
}
