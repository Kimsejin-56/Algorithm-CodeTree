import java.util.*;

class Point{
    int x, y;

    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }
}

public class Main {
    static int N,M,K,time;
    static int[][] board, turn;
    static boolean[][] attacked;
    static Point weak, strong;
    static int[] dx={0, 1, 0, -1, -1, 1, 1, -1};
    static int[] dy={1, 0, -1, 0, -1, -1, 1, 1};


    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        N=sc.nextInt();
        M=sc.nextInt();
        K=sc.nextInt();

        board=new int[N][M];
        turn=new int[N][M];

        for(int i=0; i<N; i++){
            for(int j=0; j<M; j++){
                board[i][j]=sc.nextInt();
            }
        }

        time=0;
        while(time!=K){
            time++;
            weakTower();
            strongTower();
            attack();
            breakTower();
            if(exit()) break;
            readyTower();
        }

        int max=0;
        for(int i=0; i<N; i++){
            for(int j=0; j<M; j++){
                max=Math.max(max, board[i][j]);
            }
        }

        System.out.println(max);
    }

    static void weakTower(){
        int min=Integer.MAX_VALUE;
        Point wt=new Point(-1,-1);
        for(int i=0; i<N; i++){
            for(int j=0; j<M; j++){
                int pw=board[i][j];
                int t=turn[i][j];
                if(pw==0) continue;

                if(min > pw){
                    min=pw;
                    wt.x=i;
                    wt.y=j;
                } else if(min == pw){
                    if(t > turn[wt.x][wt.y]){
                        wt.x=i;
                        wt.y=j;
                    } else if(t == turn[wt.x][wt.y]){
                        int totalpw=i+j;
                        if(totalpw > wt.x+wt.y){
                            wt.x=i;
                            wt.y=j;
                        } else if(totalpw == wt.x+wt.y){
                            if(j >wt.y){
                                wt.x=i;
                                wt.y=j;
                            }
                        }
                    }
                }
            }
        }
        board[wt.x][wt.y] += N+M;
        turn[wt.x][wt.y]=time;
        weak=wt;
    }

    static void strongTower(){
        int max=-1;
        Point wt=new Point(-1,-1);
        for(int i=0; i<N; i++){
            for(int j=0; j<M; j++){
                int pw=board[i][j];
                int t=turn[i][j];

                if(board[i][j]==0) continue;
                if(i==weak.x && j==weak.y) continue;
                
                if(max < pw){
                    max=pw;
                    wt.x=i;
                    wt.y=j;
                } else if(max == pw){
                     if(t < turn[wt.x][wt.y]){
                        wt.x=i;
                        wt.y=j;
                    } else if(t == turn[wt.x][wt.y]){
                         int totalpw=i+j;
                        if(totalpw < wt.x+wt.y){
                            wt.x=i;
                            wt.y=j;
                        } else if(totalpw == wt.x+wt.y){
                            if(j <wt.y){
                                wt.x=i;
                                wt.y=j;
                            }
                        }
                    }
                }
            }
        }
        strong=wt;
    }

    static void attack(){
        attacked=new boolean[N][M];
        attacked[strong.x][strong.y]=true;
        attacked[weak.x][weak.y]=true;
        if(razor(weak)) return;
        else canon(strong);
    }

    static void canon(Point p){
        int pw=board[weak.x][weak.y]/2;

        for(int i=0; i<8; i++){
            int nx=(p.x+dx[i]+N)%N;
            int ny=(p.y+dy[i]+M)%M;

            if(board[nx][ny]!=0){
                if(nx==weak.x && ny==weak.y) continue;
                board[nx][ny]-=pw;
                attacked[nx][ny]=true;
            }
        }

        board[p.x][p.y]-=board[weak.x][weak.y];
    }

    static boolean razor(Point point){
        Queue<Point> q=new LinkedList<>();
        boolean[][] visited= new boolean[N][M];
        Point[][] prev=new Point[N][M];

        q.offer(point);
        visited[point.x][point.y]=true;

        while(!q.isEmpty()){
            Point p=q.poll();
            
            if(strong.x==p.x && strong.y==p.y) break;

            for(int i=0; i<4; i++){
                int nx=(p.x+dx[i]+N)%N;
                int ny=(p.y+dy[i]+M)%M;
                if(!visited[nx][ny] && board[nx][ny]!=0){
                    q.offer(new Point(nx, ny));
                    visited[nx][ny]=true;
                    prev[nx][ny]=p;
                }
            }
        }

        if(!visited[strong.x][strong.y]) return false;
        
        Point cur=prev[strong.x][strong.y];
        int pw=board[point.x][point.y]/2;
        while(point.x!=cur.x || point.y!=cur.y){
            board[cur.x][cur.y]-=pw;
            attacked[cur.x][cur.y]=true;
            cur=prev[cur.x][cur.y];
        }
        board[strong.x][strong.y]-=board[point.x][point.y];
        return true;
    }

    static void breakTower(){
        for(int i=0; i<N; i++){
            for(int j=0; j<M; j++){
                if(board[i][j]<0)board[i][j]=0;
            }
        }
    }

    static void readyTower(){
        for(int i=0; i<N; i++){
            for(int j=0; j<M; j++){
                if(!attacked[i][j] && board[i][j]!=0) board[i][j]++;
            }
        }
    }

    static boolean exit(){
        int cnt=0;
        for(int i=0; i<N; i++){
            for(int j=0; j<M; j++){
                if(board[i][j] > 0) cnt++;
            }
        }

        if(cnt==1) return true;
        return false;
    }
}
