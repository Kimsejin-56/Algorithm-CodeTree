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

        int rx=p.x-target.x;
        int ry=p.y-target.y;
        int si=0;
        int sj=0;
        if(target.dir==0){
            for(int i=p.x-1; i>=0; i--) eye[i][p.y]=0;
            if(ry<0){
                si=p.x-1;
                for(int j=p.y-1; j>=0; j--){
                    for(int i=si; i>=0; i--){
                        eye[i][j]=0;
                    }
                    si-=1;
                }
            }else if(ry>0) {
                si=p.x-1;
                for(int j=p.y+1; j<n; j++){
                    for(int i=si; i>=0; i--){
                        eye[i][j]=0;
                    }
                    si-=1;
                }
            }
        }else if(target.dir==1) {
            for(int i=p.x+1; i<n; i++) eye[i][p.y]=0;
            if(ry<0){
                si=p.x+1;
                for(int j=p.y-1; j>=0; j--){
                    for(int i=si; i<n; i++){
                        eye[i][j]=0;
                    }
                    si+=1;
                }
            }else if(ry>0){
                si=p.x+1;
                for(int j=p.y+1; j<n; j++){
                    for(int i=si; i<n; i++){
                        eye[i][j]=0;
                    }
                    si+=1;
                }
            }
        }else if(target.dir==2) {
            for(int i=p.y-1; i>=0; i--) eye[p.x][i]=0;
            if(rx<0){
                sj=p.y-1;
                for(int i=p.x-1; i>=0; i--){
                    for(int j=sj; j>=0; j--){
                        eye[i][j]=0;
                    }
                    sj-=1;
                }
            }else if(rx>0) {
                sj=p.y-1;
                for(int i=p.x+1; i<n; i++){
                    for(int j=sj; j>=0; j--){
                        eye[i][j]=0;
                    }
                    sj-=1;
                }
            }
        }else {
            for(int i=p.y+1; i<n; i++) eye[p.x][i]=0;
            if(rx<0){
                sj=p.y+1;
                for(int i=p.x-1; i>=0; i--){
                    for(int j=sj; j<n; j++){
                        eye[i][j]=0;
                    }
                    sj+=1;
                }
            }else if(rx>0){
                sj=p.y+1;
                for(int i=p.x+1; i<n; i++){
                    for(int j=sj; j<n; j++){
                        eye[i][j]=0;
                    }
                    sj+=1;
                }
            }
        }

    }

    public static int[][] range(int dir){
        int[][] eye=new int[n][n];
        int si=0;
        int sj=0;
        if(dir==0){
            si=target.x-1;
            for(int j=target.y-1; j>=0; j--){
                for(int i=si; i>=0; i--){
                    eye[i][j]=1;
                }
                si-=1;
            }

            for(int j=target.x-1; j>=0; j--) eye[j][target.y]=1;

            si=target.x-1;
            for(int j=target.y+1; j<n; j++){
                for(int i=si; i>=0; i--){
                    eye[i][j]=1;
                }
                si-=1;
            }
        }else if(dir==1){
            si=target.x+1;
            for(int j=target.y-1; j>=0; j--){
                for(int i=si; i<n; i++){
                    eye[i][j]=1;
                }
                si+=1;
            }

            for(int j=target.x+1; j<n; j++) eye[j][target.y]=1;

            si=target.x+1;
            for(int j=target.y+1; j<n; j++){
                for(int i=si; i<n; i++){
                    eye[i][j]=1;
                }
                si+=1;
            }
        }else if(dir==2){
            sj=target.y-1;
            for(int i=target.x-1; i>=0; i--){
                for(int j=sj; j>=0; j--){
                    eye[i][j]=1;
                }
                sj-=1;
            }

            for(int j=target.y-1; j>=0; j--) eye[target.x][j]=1;

            sj=target.y-1;
            for(int i=target.x+1; i<n; i++){
                for(int j=sj; j>=0; j--){
                    eye[i][j]=1;
                }
                sj-=1;
            }
        }else if(dir==3){
            sj=target.y+1;
            for(int i=target.x-1; i>=0; i--){
                for(int j=sj; j<n; j++){
                    eye[i][j]=1;
                }
                sj+=1;
            }

            for(int j=target.y+1; j<n; j++) eye[target.x][j]=1;

            sj=target.y+1;
            for(int i=target.x+1; i<n; i++){
                for(int j=sj; j<n; j++){
                    eye[i][j]=1;
                }
                sj+=1;
            }
        }

        return eye;
    }
}
