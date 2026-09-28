import java.util.*;

class Point{
    int x, y, dir;
    boolean rock;
    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }
}

public class Main {
    static int n, m, rock;
    static int[] dx={-1, 1, 0, 0};
    static int[] dy={0, 0, -1, 1};
    static int[] dx2={0, 0,-1, 1,};
    static int[] dy2={-1, 1, 0, 0,};
    static List<Point> peoples;
    static int[][] board, dist;
    static Point house, park, target;
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        board=new int[n][n];
        dist=new int[n][n];
        peoples=new ArrayList<>();
        house=new Point(sc.nextInt(), sc.nextInt());
        park=new Point(sc.nextInt(), sc.nextInt());
        target=new Point(house.x, house.y);
        rock=0;

        for(int i=0; i<m; i++){
            peoples.add(new Point(sc.nextInt(), sc.nextInt()));
        }

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                board[i][j]=sc.nextInt();
                if(board[i][j]==1) dist[i][j]=-1;
            }
        }

        bfs(park);
        if(dist[target.x][target.y]==0) {
            System.out.println(-1);
            return;
        }

        while(true) {
            move();
            if(target.x==park.x && target.y==park.y) {
                System.out.println(0);
                break;
            }
            
            for(int i=0; i<peoples.size(); i++){
                Point p=peoples.get(i);
                if(p.x==target.x && p.y==target.y){
                    peoples.remove(p);
                    i--;
                }
            }

            int[][] eye=eyes();
            int t=movePeople(eye);
            int attack=attack();
            
            System.out.println(t+" "+rock+" "+attack);
            for(Point p : peoples) p.rock=false;
        }
    }

    public static int attack(){
        int attack=0;
        for(int i=0; i<peoples.size(); i++){
            Point p=peoples.get(i);
            if(target.x==p.x && target.y==p.y){
                attack++;
                peoples.remove(p);
                i--;
            }
        }
        
        return attack;
    }

    public static int movePeople(int[][] eye){
        int total=0;
        for(Point p : peoples){
            if(p.rock) continue;

            for(int i=0; i<4; i++){
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];

                if(nx>=0 && nx<n && ny>=0 && ny<n && eye[nx][ny]!=1 &&
                        dis(p)>dis(new Point(nx, ny))){
                    p.x=nx;
                    p.y=ny;
                    total++;
                    break;
                }
            }

            for(int i=0; i<4; i++){
                int nx=p.x+dx2[i];
                int ny=p.y+dy2[i];

                if(nx>=0 && nx<n && ny>=0 && ny<n && eye[nx][ny]!=1 &&
                        dis(p)>dis(new Point(nx, ny))){
                    p.x=nx;
                    p.y=ny;
                    total++;
                    break;
                }
            }
        }
        return total;
    }

    public static int dis(Point p){
        return Math.abs(target.x-p.x)+Math.abs(target.y-p.y);
    }

    public static int[][] eyes(){
        int[][][] eye=new int[4][n][n];
        List<Integer> candidate=new ArrayList<>();
        rock=0;
        int max=0;
        
        for(int d=0; d<4; d++){
            int cnt=0;
            target.dir=d;
            eye[d]=range(d);
            for(Point p : peoples){
                exist(eye[d], p);
            }

            for(Point p : peoples){
                if(eye[d][p.x][p.y]==1) cnt++;
            }

            if(max<cnt){
                max=cnt;
                candidate.clear();
                candidate.add(d);
            }else if(cnt==max) candidate.add(d);
        }

        Collections.sort(candidate);
        target.dir=candidate.get(0);

        for(Point p : peoples){
            if(eye[target.dir][p.x][p.y]==1) {
                p.rock=true;
                rock++;
            }
        }
        return eye[target.dir];
    }

    public static void move(){
        for(int i=0; i<4; i++){
            int nx=target.x+dx[i];
            int ny=target.y+dy[i];

            if(nx>=0 && nx<n && ny>=0 && ny<n && board[nx][ny]==0 &&
                    dist[target.x][target.y]>dist[nx][ny]){
                target.x=nx;
                target.y=ny;
                break;
            }
        }
    }

    public static void bfs(Point s){
        Queue<Point> q=new ArrayDeque<>();
        boolean[][] visited=new boolean[n][n];
        q.offer(s);
        visited[s.x][s.y]=true;

        while(!q.isEmpty()){
            Point p=q.poll();
            for(int i=0; i<4; i++){
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];

                if(nx>=0 && nx<n && ny>=0 && ny<n && !visited[nx][ny] && board[nx][ny]==0){
                    q.offer(new Point(nx, ny));
                    visited[nx][ny]=true;
                    dist[nx][ny]=dist[p.x][p.y]+1;
                }
            }
        }
    }
    
    public static void exist(int[][] eye, Point p){
        if(eye[p.x][p.y]==0) return;

        int wf=getFront(p.x, p.y, target.dir);
        int ws=getSide(p.x, p.y, target.dir);


        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(eye[x][y]==0) continue;
                if(p.x==x && p.y==y)continue;
                
                 int f=getFront(x, y, target.dir);
                 int s=getSide(x, y, target.dir);

                 if(f<=wf) continue;
                 
                 int gap=f-wf;

                 if(ws==0) {
                     if(s==0) eye[x][y]=0;
                 }else if(ws<0) {
                     if(ws-gap<=s&& s<=ws) eye[x][y]=0;
                 }else {
                     if(ws<=s&&s<=ws+gap) eye[x][y]=0;
                 }
            }
        }
    }

    public static int[][] range(int dir){
        int[][] eye=new int[n][n];
       
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                int front=getFront(x, y, dir);
                int side=getSide(x, y, dir);
                
                if(front>0 && Math.abs(side)<=front) {
                    eye[x][y]=1;
                }
            }
        }

        return eye;
    }
    
    public static int getFront(int x, int y, int dir) {
        if(dir==0) return target.x-x;
        else if(dir==1) return x-target.x;
        else if(dir==2) return target.y-y;
        else return y-target.y;
    }
    
    public static int getSide(int x, int y, int dir) {
        if(dir==0 || dir==1) return y-target.y;
        return x-target.x;
    }
}
