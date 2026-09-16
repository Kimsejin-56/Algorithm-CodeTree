import java.util.*;

class Point implements Comparable<Point>{
    int num, x, y;

    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int compareTo(Point p){
        if(this.x==p.x) return this.y-p.y;
        return this.x-p.x;
    }
}

public class Main {
    static int n,k,l;
    static int[][] board;
    static boolean[][] check;

    static int[] dx={0, 1, 0, -1};
    static int[] dy={1, 0, -1, 0};
    static int[][] dir={
            {3,0,1},
            {0,1,2},
            {1,2,3},
            {2,3,0}
    };

    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        n=sc.nextInt();
        k=sc.nextInt();
        l=sc.nextInt();
        board=new int[n][n];
        check=new boolean[n][n];
        List<Point> robots=new ArrayList<>();
        int turn=1;

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                board[i][j]=sc.nextInt();
            }
        }

        for(int i=0; i<k; i++){
            Point r=new Point(sc.nextInt()-1, sc.nextInt()-1);
            r.num=i;
            check[r.x][r.y]=true;
            robots.add(r);
        }

       while(turn<=l){
            for(int i=0; i<k; i++){
                move(robots.get(i));
            }

            for(int i=0; i<k; i++){
                clean(robots.get(i));
            }

            addDust();
            extendDust();
            print();
            turn++;
       }
    }

    public static void move(Point s){
        Queue<Point> q=new ArrayDeque<>();
        boolean[][] visited=new boolean[n][n];
        List<Point> list=new ArrayList<>();
        q.offer(s);
        visited[s.x][s.y]=true;

        if(board[s.x][s.y]>0) return;

        while(!q.isEmpty()){
            int len=q.size();
            for(int l=0; l<len; l++){
                Point p=q.poll();
                for(int i=0; i<4; i++){
                    int nx=p.x+dx[i];
                    int ny=p.y+dy[i];

                    if(nx>=0 && nx<n && ny>=0 && ny<n && !visited[nx][ny] && board[nx][ny]!=-1 && !check[nx][ny]){
                        Point r=new Point(nx, ny);
                        if(board[nx][ny]>0) list.add(r);
                        visited[nx][ny]=true;
                        q.offer(r);
                    }
                }
            }
            if(!list.isEmpty()) break;
        }

        if(!list.isEmpty()){
            Collections.sort(list);
            check[s.x][s.y]=false;
            Point select=list.get(0);
            s.x=select.x;
            s.y=select.y;
            check[s.x][s.y]=true;
        }
    }

    public static void clean(Point r){
        int[] dust=new int[4];

        for(int i=0; i<4; i++){
            int total=board[r.x][r.y];
            for(int j=0; j<3; j++){
                int nx=r.x+dx[dir[i][j]];
                int ny=r.y+dy[dir[i][j]];

                if(nx>=0 && nx<n && ny>=0 && ny<n && board[nx][ny]!=-1){
                    if(board[nx][ny]>20) total+=20;
                    else total+=board[nx][ny];
                }
            }
            dust[i]=total;
        }

        int max=0;
        int idx=0;
        for(int i=0; i<4; i++){
            if(max<dust[i]){
                max=dust[i];
                idx=i;
            }
        }

        if(board[r.x][r.y]>20) board[r.x][r.y]-=20;
        else board[r.x][r.y]=0;
        for(int i=0; i<3; i++){
            int nx=r.x+dx[dir[idx][i]];
            int ny=r.y+dy[dir[idx][i]];

            if(nx>=0 && nx<n && ny>=0 && ny<n && board[nx][ny]!=-1){
                if(board[nx][ny]>20) board[nx][ny]-=20;
                else board[nx][ny]=0;
            }
        }
    }

    public static void addDust(){
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                if(board[i][j]>0) board[i][j]+=5;
            }
        }
    }

    public static void extendDust(){
        int[][] tmp=new int[n][n];
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                if(board[i][j]==0) {
                    int total=0;
                    for(int d=0; d<4; d++){
                        int nx=i+dx[d];
                        int ny=j+dy[d];

                        if(nx>=0 && nx<n && ny>=0 && ny<n && board[nx][ny]!=-1){
                            total+=board[nx][ny];
                        }
                    }

                    tmp[i][j]=total/10;
                }
            }
        }

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                if(tmp[i][j]>0) board[i][j]=tmp[i][j];
            }
        }
    }

    public static void print(){
        int sum=0;
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                if(board[i][j]>0) sum+=board[i][j];
            }
        }

        System.out.println(sum);
    }

}
