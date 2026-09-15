import java.util.*;

class Point implements Comparable<Point>{
    int x,y,dir;
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
    static int n,r,c,d;
    static int[][] board;
    static int[] dx={-1, 1, 0, 0};
    static int[] dy={0, 0, -1, 1};
    static boolean[][] visited;
    static int[] dirs={3, 2, 4, 1};

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        r=sc.nextInt();
        c=sc.nextInt();
        d=sc.nextInt();
        board=new int[n][n];
        visited=new boolean[n][n];
        int t=2;
        int cnt=0;

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                board[i][j]=sc.nextInt();
                if(board[i][j]==1) cnt++;
            }
        }
        Point fish=new Point(r-1, c-1);
        fish.dir=d;
        visited[fish.x][fish.y]=true; 
        int x=fish.x+1;
        int y=fish.y+1;

        int finish=n*n-cnt;

        System.out.println(x+" "+y);
        while(t<=finish){
            move(fish);
            x=fish.x+1;
            y=fish.y+1;
            System.out.println(x+" "+y);
            t++;
        }
        
    }

    public static void move(Point fish){
        int dir=fish.dir;
        if(canMove(fish, dir)) return;

        //좌회전
        if(fish.dir==4)fish.dir=1;
        else if(fish.dir==3) fish.dir=2;
        else if(fish.dir==2) fish.dir=4;
        else if(fish.dir==1) fish.dir=3;

        if(canMove(fish, dir)) return;
        
        //우회전
        if(fish.dir==4)fish.dir=2;
        else if(fish.dir==3) fish.dir=1;
        else if(fish.dir==2) fish.dir=3;
        else if(fish.dir==1) fish.dir=4;

        if(canMove(fish, dir)) return;

        //180회전
        if(fish.dir==4)fish.dir=3;
        else if(fish.dir==3) fish.dir=4;
        else if(fish.dir==2) fish.dir=1;
        else if(fish.dir==1) fish.dir=2;

        if(canMove(fish, dir)) return;

        bfs(fish);
        visited[fish.x][fish.y]=true;
    }

    public static boolean canMove(Point fish, int dir){
        int nx=fish.x+dx[fish.dir-1];
        int ny=fish.y+dy[fish.dir-1];

        if(nx>=0 && nx<n && ny>=0 && ny<n && board[nx][ny]==0 && !visited[nx][ny]){
            fish.x=nx;
            fish.y=ny;
            visited[nx][ny]=true;
            return true;
        }

        fish.dir=dir;
        return false;
    }

    public static void bfs(Point s){
        List<Point> list=new ArrayList<>();
        boolean[][] check=new boolean[n][n];
        Queue<Point> q=new ArrayDeque<>();
        q.offer(s);
        check[s.x][s.y]=true;

        while(!q.isEmpty()){
            int len=q.size();
            for(int l=0; l<len; l++){
                Point p=q.poll();

                for(int dir : dirs){
                    int nx=p.x+dx[dir-1];
                    int ny=p.y+dy[dir-1];

                    if(nx>=0 && nx<n && ny>=0 && ny<n && board[nx][ny]==0 && !check[nx][ny]){
                        Point next=new Point(nx, ny);
                        next.dir=dir;
                        q.offer(next);
                        check[nx][ny]=true;
                        if(!visited[nx][ny]) list.add(next);
                    }
                }
            }
            
            if(!list.isEmpty()) break;
        }

        Collections.sort(list);
        Point t=list.get(0);
        s.x=t.x;
        s.y=t.y;
        s.dir=t.dir;
    }
}