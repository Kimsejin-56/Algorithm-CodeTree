import java.util.*;

class Point implements Comparable<Point>{
    int x, y;
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
    static int n, k, l;
    static int[] dx= {-1, 0, 1, 0, 0};
    static int[] dy= {0, 1, 0, -1, 0};
    static int[][] board;
    static List<Point> robots=new ArrayList<>();
    static int[][] dirs= {
            {0,1,2,4},
            {1,2,3,4},
            {0,2,3,4},
            {0,1,3,4}
    };
    
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        k=sc.nextInt();
        l=sc.nextInt();
        board=new int[n][n];
        int turn=1;
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                board[i][j]=sc.nextInt();
            }
        }
        
        for(int i=0; i<k; i++) {
            robots.add(new Point(sc.nextInt()-1, sc.nextInt()-1));
        }
        
        while(turn<=l) {
            for(Point p : robots) {
                move(p);
            }
            clean();
            addDust();
            spreadDust();
            if(print()==0) break;
            turn++;
        }
    }
    
    static int print() {
        int sum=0;
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(board[i][j]>0) sum+=board[i][j];
            }
        }
        
        System.out.println(sum);
        return sum;
    }
    
    static void spreadDust() {
        int[][] copy=new int[n][n];
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(board[i][j]==0) {
                    int sum=0;
                    for(int d=0; d<4; d++) {
                        int nx=i+dx[d];
                        int ny=j+dy[d];
                        
                        if(nx>=0 && nx<n && ny>=0 && ny<n && board[nx][ny]!=-1) {
                            sum+=board[nx][ny];
                        }
                    }
                    copy[i][j]=sum/10;
                }
            }
        }
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                board[i][j]+=copy[i][j];
            }
        }
    }
    
    static void addDust() {
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(board[i][j]>0) board[i][j]+=5;
            }
        }
    }
    
    static void clean() {
        for(Point p : robots) {
            int d=selectDir(p);
            int[] dir=dirs[d];
            
            for(int i=0; i<dir.length; i++) {
                int nx=p.x+dx[dir[i]];
                int ny=p.y+dy[dir[i]];
                
                if(nx>=0 && nx<n && ny>=0 && ny<n && board[nx][ny]>0) {
                    if(board[nx][ny]>20) board[nx][ny]-=20;
                    else board[nx][ny]=0;
                }
            }
        }
    }
    
    static int selectDir(Point p) {
        int max=Integer.MIN_VALUE;
        int dir=-1;
        
        for(int d=0; d<4; d++) {
            int total=0;
            int[] rdir=dirs[d];
            
            for(int i=0; i<rdir.length; i++) {
                int nx=p.x+dx[rdir[i]];
                int ny=p.y+dy[rdir[i]];
                
                if(nx>=0 && nx<n && ny>=0 && ny<n && board[nx][ny]>0) {
                    if(board[nx][ny]>20) total+=20;
                    else total+=board[nx][ny];
                }
            }
            
            if(max<total) {
                max=total;
                dir=d;
            }
        }
        
        return dir;
    }
    
    static void move(Point s) {
        Queue<Point> q=new ArrayDeque<>();
        boolean[][] visited=new boolean[n][n];
        List<Point> list=new ArrayList<>();
        if(board[s.x][s.y]>0) return;
        
        q.offer(s);
        visited[s.x][s.y]=true;
        
        
        while(!q.isEmpty()) {
            int len=q.size();
            
            for(int l=0; l<len; l++) {
                Point p=q.poll();
                for(int i=0; i<4; i++) {
                    int nx=p.x+dx[i];
                    int ny=p.y+dy[i];
                    
                    if(nx>=0 && nx<n && ny>=0 && ny<n && board[nx][ny]!=-1 && !hasRobot(nx, ny) && !visited[nx][ny]) {
                        visited[nx][ny]=true;
                        Point t=new Point(nx, ny);
                        q.offer(t);
                        if(board[nx][ny]>0) list.add(t);
                    }
                }
            }
            if(!list.isEmpty()) break;
        }
        
        Collections.sort(list);
        if(!list.isEmpty()) {
            Point np=list.get(0);
            s.x=np.x;
            s.y=np.y;
        }
    }
    
    static boolean hasRobot(int x, int y) {
        for(Point p : robots) {
            if(p.x==x && p.y==y) return true;
        }
        return false;
    }
}
