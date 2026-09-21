import java.util.*;

class Point {
    int x, y, dir, num;

    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }

    public void clock(){
        dir++;
        if(dir==4) dir=0;
    }

    public void oclock(){
        dir--;
        if(dir==-1) dir=3;
    }
}

public class Main {
    static int r, c, k, total;
    static List<Point> angles;
    static int[] dx={-1, 0, 1, 0};
    static int[] dy={0, 1, 0, -1};
    static int[][] board;
    static boolean[][] exit;

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        r=sc.nextInt();
        c=sc.nextInt();
        k=sc.nextInt();
        total=0;
        angles=new ArrayList<>();
        board=new int[r+3][c];
        exit=new boolean[r+3][c];

        for(int i=0; i<k; i++){
            Point p=new Point(1, sc.nextInt()-1);
            p.dir=sc.nextInt();
            p.num=i+1;
            angles.add(p);
        }

        for(Point p : angles){
            move(p);
        }
        System.out.println(total);
    }

    public static void move(Point p){
        while(true){
            if(canSouth(p)) {
                p.x++;
                continue;
            }

            if(canWest(p)) {
                p.x++;
                p.y--;
                p.oclock();
                continue;
            }

            if(canEast(p)) {
                p.x++;
                p.y++;
                p.clock();
                continue;
            }


            if(p.x<4){
                board=new int[r+3][c];
                exit=new boolean[r+3][c];
                return;
            }

            board[p.x][p.y]=p.num;
            exit[p.x+dx[p.dir]][p.y+dy[p.dir]]=true;
            for(int i=0; i<4; i++){
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];
                board[nx][ny]=p.num;
            }

            total+=bfs(p);
            break;
        }
    }

    public static int bfs(Point s){
        Queue<Point> q=new ArrayDeque<>();
        boolean[][] visited=new boolean[r+3][c];
        q.offer(s);
        visited[s.x][s.y]=true;
        int max=-1;

        while(!q.isEmpty()){
            Point p=q.poll();
            for(int i=0; i<4; i++){
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];

                if(nx>=0 && nx<r+3 && ny>=0 && ny<c && !visited[nx][ny] && board[nx][ny]!=0){
                    if(board[nx][ny]==board[p.x][p.y] || exit[p.x][p.y]){
                        visited[nx][ny]=true;
                        max=Math.max(max, nx);
                        q.offer(new Point(nx, ny));
                    }
                }
            }
        }

        return max-2;
    }

    public static boolean canSouth(Point p){
        return empty(p.x+2, p.y) && empty(p.x+1, p.y-1) && empty(p.x+1, p.y+1);
    }

    public static boolean canWest(Point p){
        return empty(p.x-1, p.y-1) && empty(p.x, p.y-2) && empty(p.x+1, p.y-1)
                && empty(p.x+1, p.y-2) && empty(p.x+2, p.y-1);
    }

    public static boolean canEast(Point p){
        return empty(p.x-1, p.y+1) && empty(p.x, p.y+2) && empty(p.x+1, p.y+1)
                && empty(p.x+1, p.y+2) && empty(p.x+2, p.y+1);
    }

    public static boolean empty(int x, int y){
        return x>=0 && x<r+3 && y>=0 && y<c && board[x][y]==0;
    }
}