import java.util.*;

class Point implements Comparable<Point>{
    int x, y, tmpX, tmpY;
    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int compareTo(Point p){
        if(this.y==p.y) return p.x-this.x;
        return this.y-p.y;
    }
} 

public class Main {
    static int n, k, m, idx;
    static List<Integer> rocks;
    static int[][] board;
    static int[] dx={-1, 0, 1, 0};
    static int[] dy={0, -1, 0, 1};

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=5;
        idx=0;
        k=sc.nextInt();
        m=sc.nextInt();
        board=new int[n][n];
        rocks=new ArrayList<>();

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                board[i][j]=sc.nextInt();
            }
        }

        for(int i=0; i<m; i++){
            rocks.add(sc.nextInt());
        }

        int t=0;
        while(t!=k){
            int total=0;
            if(searchArr()) return;
            while(true){
                int cnt=getRock();
                if(cnt==0) break;
                total+=cnt;
            }
            
            System.out.print(total+" ");
            t++;
        }
        
    }

    public static int getRock(){
        boolean[][] visited=new boolean[n][n];
        List<Point> list=new ArrayList<>();
         List<Point> temp;

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                if(visited[i][j]) continue;
                temp=bfs(new Point(i, j), board, visited);
                if(temp.size()>=3) list.addAll(temp);
            }
        }

        Collections.sort(list);
        for(Point p : list){
            board[p.x][p.y]=rocks.get(idx++);
        }

        return list.size();
    }

    public static boolean searchArr(){
        Point p=new Point(0,0);
        p.x=Integer.MIN_VALUE;
        int cnt=0;
        int[][] select=new int[n][n];

        for(int i=1; i<n-1; i++){
            for(int j=1; j<n-1; j++){
                int[][] arr=new int[n][n];
                for(int x=0; x<n; x++){
                    arr[x]=Arrays.copyOf(board[x], n);
                }

                int[][] tmp=new int[3][3];
                int r=0;
                for(int x=i-1; x<=i+1; x++){
                    int c=0;
                    for(int y=j-1; y<=j+1; y++){
                        tmp[r][c++]=arr[x][y];
                    }
                    r++;
                }

                for(int x=0; x<3; x++){
                    tmp=spinArr(tmp);
                    makeArr(arr, tmp, i, j);
                    cnt=count(arr);

                    if(isBetter(cnt, x, i, j, p)){
                        p.x=cnt;
                        p.y=x;
                        p.tmpX=i;
                        p.tmpY=j;
                        copyArr(select, arr);
                    }
                }
            }
        }

        if(p.x==0) return true;
        board=select;
        return false;
    }

    public static int[][] spinArr(int[][] arr){
        int[][] tmp=new int[3][3];

        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                tmp[j][3-i-1]=arr[i][j];
            }
        }

        return tmp;
    }

     public static int count(int[][] arr){
        int cnt=0;
        int num=0;
        boolean[][] visited=new boolean[n][n];

        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                if(!visited[i][j]){
                    num=bfs(new Point(i, j), arr, visited).size();
                    if(num>=3){
                        cnt+=num;
                    }
                }
            }
        }
        return cnt;
    }

    public static List<Point> bfs(Point s, int[][] arr, boolean[][] visited){
        Queue<Point> q=new ArrayDeque<>();
        List<Point> list=new ArrayList<>();
        int num=arr[s.x][s.y];
        q.offer(s);
        list.add(s);
        visited[s.x][s.y]=true;

        while(!q.isEmpty()){
            Point p=q.poll();
            for(int i=0; i<4; i++){
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];
                if(nx>=0 && nx<n && ny>=0 && ny<n && !visited[nx][ny] && arr[nx][ny]==num){
                    Point next=new Point(nx, ny);
                    q.offer(next);
                    list.add(next);
                    visited[nx][ny]=true;
                }
            }
        }

        return list;
    }

    public static void makeArr(int[][] arr, int[][] tmp, int x, int y){
        int r=0;
        for(int i=x-1; i<=x+1; i++){
            int c=0;
            for(int j=y-1; j<=y+1; j++){
                arr[i][j]=tmp[r][c++];
            }
            r++;
        }
    }

    public static void copyArr(int[][] select, int[][] arr){
        for(int r=0; r<n; r++){
            select[r]=Arrays.copyOf(arr[r], n);
        }
    }

    public static boolean isBetter(int cnt, int angle, int x, int y, Point p){
        if(cnt!=p.x) return cnt>p.x;
        if(angle!=p.y) return angle<p.y;
        if(y!=p.tmpY) return y<p.tmpY;
        return x<p.tmpX;
    }
}