import java.util.*;


class Point implements Comparable<Point>{
    int x, y, dir;
    
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
    
    public int compareTo(Point o) {
        if(this.y==o.y) return o.x-this.x;
        return this.y-o.y;
    }
}

public class Main {
    static int idx, k, m, n; 
    static int[][] board;
    static int[] dx= {-1, 0, 1, 0};
    static int[] dy= {0, -1, 0, 1};
    static List<Integer> rocks=new ArrayList<>();
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        k=sc.nextInt();
        m=sc.nextInt();
        idx=0;
        n=5;
        board=new int[n][n];
        int turn=1;
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                board[i][j]=sc.nextInt();
            }
        }
        
        for(int i=0; i<m; i++) {
            rocks.add(sc.nextInt());
        }
        
        while(turn<=k) {
            int total=0;
            
            search();
            total=getScore();
            if(total==0) break;
            System.out.print(total+" ");
            turn++;
        }
    }
    
    static int getScore() {
        List<Point> list=getRock(board);
        int total=0;
        
        while(list.size()!=0) {
            total+=list.size();
            Collections.sort(list);
            
            for(Point p : list) {
                board[p.x][p.y]=rocks.get(idx++);
            }
            
            list=getRock(board);
        }
        
        return total;
    }
    
    static void search() {
        int[][] copy=new int[n][n];
        int[][] next=new int[n][n];
        int max=0;
        int mdir=4;
        int mx=6;
        int my=6;
        int cur=0;
        
        for(int i=1; i<=3; i++) {
            for(int j=1; j<=3; j++) {
                for(int l=0; l<n; l++) {
                    copy[l]=board[l].clone();
                }
                
                int[][] temp=selectSquare(i, j);
                Point center=new Point(i, j);
                
                for(int d=0; d<3; d++) {
                    temp=spin(temp);
                    putSquare(copy, temp, center);
 
                    List<Point> tmp=getRock(copy);
                    cur=tmp.size();
                    
                    if(isBetter(max, mdir, mx, my, cur, d, center)) {
                        max=cur;
                        mdir=d;
                        mx=center.x;
                        my=center.y;
                        for(int l=0; l<n; l++) {
                            next[l]=copy[l].clone();
                        }
                    }
                    
                }
            }
        }
        board=next;
    }
    
    static boolean isBetter(int max, int mdir, int mx, int my, int cur, int dir, Point p) {
        if(max!=cur) return max<cur;
        if(mdir!=dir) return mdir>dir;
        if(my!=p.y) return my>p.y;
        return mx>p.x;
    }
    
    static void putSquare(int[][] arr, int[][] cut, Point center){
        int r=0;
        int c=0;
        for(int i=center.x-1; i<=center.x+1; i++) {
            for(int j=center.y-1; j<=center.y+1; j++) {
                arr[i][j]=cut[r][c++];
            }
            c=0;
            r++;
        }
    }
    
    static int[][] spin(int[][] temp){
        int[][] arr=new int[3][3];
        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                arr[j][2-i]=temp[i][j];
            }
        }
        
        return arr;
    }
    
    static int[][] selectSquare(int x, int y) {
        int[][] temp=new int[3][3];
        int r=0;
        int c=0;
        for(int i=x-1; i<=x+1; i++) {
            for(int j=y-1; j<=y+1; j++) {
                temp[r][c++]=board[i][j];
            }
            c=0;
            r++;
        }
        
        return temp;
    }
    
    static List<Point> getRock(int[][] arr) {
        boolean[][] visited=new boolean[n][n];
        List<Point> list=new ArrayList<>();
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(visited[i][j]) continue;
                List<Point> tmp=bfs(new Point(i, j), visited, arr);
                if(tmp.size()>=3) list.addAll(tmp);
            }
        }
        
        return list;
    }
    
    static List<Point> bfs(Point s, boolean[][] visited, int[][] arr) {
        Queue<Point> q=new ArrayDeque<>();
        List<Point> list=new ArrayList<>();
        visited[s.x][s.y]=true;
        q.offer(s);
        int num=arr[s.x][s.y];
        list.add(s);
        
        while(!q.isEmpty()) {
            Point p=q.poll();
            
            for(int i=0; i<4; i++) {
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];
                
                if(nx>=0 && nx<n && ny>=0 && ny<n && !visited[nx][ny] && arr[nx][ny]==num) {
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
