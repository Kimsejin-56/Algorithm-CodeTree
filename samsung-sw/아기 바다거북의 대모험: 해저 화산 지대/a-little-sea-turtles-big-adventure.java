import java.util.*;

class Point{
    int x, y, limit, num, v, tmp;
    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }

    public Point(int x, int y, int limit){
        this.x=x;
        this.y=y;
        this.limit=limit;
    }
}
public class Main {
    static int[] dx={0, 1, 0, -1};
    static int[] dy={1, 0, -1, 0};
    static int n, m, k;
    static int[][] arr;
    static boolean[][] visited;

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        k=sc.nextInt();
        arr=new int[n][n];
        int[] answer=new int[m];
        List<Point> tutles=new ArrayList<>();
        List<Point> vol=new ArrayList<>();
        visited=new boolean[n][n];
        int turn=1;

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                arr[i][j]=sc.nextInt();
            }
        }

        for(int i=0; i<m; i++){
            Point t=new Point(sc.nextInt(), sc.nextInt());
            t.num=i;
            visited[t.x][t.y]=true;
            tutles.add(t);
        }

        for(int i=0; i<k; i++){
            vol.add(new Point(sc.nextInt(), sc.nextInt(), sc.nextInt()));
        }

        while(turn<=100){
            for(int i=0; i<tutles.size(); i++){
                Point p=tutles.get(i);
                move(p);
                if(p.x==n-1 && p.y==n-1){
                    answer[p.num]=turn;
                    visited[p.x][p.y]=false;
                    tutles.remove(p);
                    i--;
                }
            }

            plus(vol);
            action(vol, tutles);

            turn++;
        }

        for(int i=0; i<m; i++){
            if(answer[i]==0){
                System.out.println(-1);
                continue;
            }
            System.out.println(answer[i]);
        }
    }

    public static void move(Point s){
        boolean[][] check=new boolean[n][n];
        Queue<Point> q=new ArrayDeque<>();
        q.offer(s);
        check[s.x][s.y]=true;
        s.tmp=-1;

        while(!q.isEmpty()){
            Point p=q.poll();

            if(p.x==n-1 && p.y==n-1){
                visited[s.x][s.y]=false;

                if(p.tmp==-1) return;

                s.x+=dx[p.tmp];
                s.y+=dy[p.tmp];
                visited[s.x][s.y]=true;
                return;
            }

            for(int i=0; i<4; i++){
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];

                if(nx>=0 && nx<n && ny>=0 && ny<n && arr[nx][ny]==0 && !visited[nx][ny] && !check[nx][ny]){
                    Point t=new Point(nx, ny);

                    if(p.tmp==-1) t.tmp=i;
                    else t.tmp=p.tmp;

                    check[nx][ny]=true;
                    q.offer(t);
                }
            }
        }
    }

    public static void plus(List<Point> vol){
        for(Point p : vol) p.v+=10;
    }

    public static void action(List<Point> vol, List<Point> tutles){
        int[][] board=new int[n][n];
        boolean[] boom=new boolean[vol.size()];
        boolean again=true;

        while(again){
            again=false;

            for(int i=0; i<vol.size(); i++){
                Point p=vol.get(i);
                if(!boom[i] && p.v+board[p.x][p.y]>=p.limit){
                    boom[i]=true;
                    again=true;
                    arrVol(p, board);
                }
            }
        }

        for(int i=0; i<vol.size(); i++){
            if(boom[i]) vol.get(i).v=0;
        } 
       

        for(int i=0; i<tutles.size(); i++){
            Point p=tutles.get(i);
            p.v=board[p.x][p.y];
            if(p.v>=20){
                arr[p.x][p.y]=4;
                tutles.remove(p);
                i--;
            }
        }
    }

    public static void arrVol(Point p, int[][] board){
        int v=p.limit;
        int x=p.x;
        int y=p.y;
        board[x][y]+=v;

        while(v!=0){
            v=v/2;
            y++;
             if(x>=0 && x<n && y>=0 && y<n){
                if(arr[x][y]==1) break;
                board[x][y]+=v;
            } 
        }

        x=p.x;
        y=p.y;
        v=p.limit;
        while(v!=0){
            v=v/2;
            y--;
            if(x>=0 && x<n && y>=0 && y<n){
                if(arr[x][y]==1) break;
                board[x][y]+=v;
            } 
        }

        x=p.x;
        y=p.y;
        v=p.limit;
        while(v!=0){
            v=v/2;
            x++;
             if(x>=0 && x<n && y>=0 && y<n){
                if(arr[x][y]==1) break;
                board[x][y]+=v;
            } 
        }

        x=p.x;
        y=p.y;
        v=p.limit;
        while(v!=0){
            v=v/2;
            x--;
            if(x>=0 && x<n && y>=0 && y<n){
                if(arr[x][y]==1) break;
                board[x][y]+=v;
            } 
        }
    }
}