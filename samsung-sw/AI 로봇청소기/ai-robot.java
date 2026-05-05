import java.util.*;

class Point implements Comparable<Point>{
    int x;
    int y;
    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int compareTo(Point o){
        if(o.x==this.x) return this.y-o.y;
        return this.x-o.x;
    }
}

public class Main {
    static int n, k, l;
    static int[][] arr;
    static Point[] robot;
    static boolean[][] visited, isRobot;
    static int[] dx={0,1,0,-1,0};
    static int[] dy={1,0,-1,0,0};
    static int[][] dirs={
        {0,1,3,4},
        {0,1,2,4},
        {1,2,3,4},
        {0,2,3,4}
    };
    
    static Point choice(Point r){
        if(arr[r.x][r.y]>0) return r;

        Queue<Point> q = new LinkedList<>();
        List <Point> rest=new ArrayList<>();
        visited=new boolean[n+1][n+1];
        q.offer(r);
        visited[r.x][r.y]=true;
        isRobot[r.x][r.y]=false;

        while(!q.isEmpty()){
            int len = q.size();
            
            for(int L=0; L<len; L++){
                Point p = q.poll();

                for(int i=0; i<4; i++){
                    int nx=p.x+dx[i];
                    int ny=p.y+dy[i];

                    if(nx>0 && ny>0 && nx<=n && ny<=n && arr[nx][ny]!=-1 && !visited[nx][ny] && !isRobot[nx][ny]){
                        if(arr[nx][ny] == 0) {
                            q.offer(new Point(nx, ny));
                            visited[nx][ny]=true;
                        }
                        else {
                            rest.add(new Point(nx, ny));
                        }
                    }
                }
            }

            if(!rest.isEmpty()){
                Collections.sort(rest);
                isRobot[rest.get(0).x][rest.get(0).y]=true;                     

                return rest.get(0);
            }
        }
        isRobot[r.x][r.y]=true;
        return r;
    }

    static void clean(Point p){
        List <Integer> dusts=new ArrayList<>();
        int max=0;
        int idx=0;
        for(int j=0; j<4; j++){
            int dust=0;
            int[] dir=dirs[j];
            for(int d : dir){
                int nx=p.x+dx[d];
                int ny=p.y+dy[d];

                if(nx>0 && ny>0 && nx<=n && ny<=n && arr[nx][ny]!=-1){
                    dust+=Math.min(20,arr[nx][ny]);
                }
            }
            if(max < dust){
                max=dust;
                idx=j;
            }
        }
        
        
        int[] dir=dirs[idx];
        for(int d : dir){
            int nx=p.x+dx[d];
            int ny=p.y+dy[d];

            if(nx>0 && ny>0 && nx<=n && ny<=n && arr[nx][ny]!=-1){
                if(arr[nx][ny]>20){
                    arr[nx][ny]-=20;
                }else{
                    arr[nx][ny]=0;
                }
            }
        }
    }



    static void addDust(){
        int[][] tmp=new int[n+1][n+1];
        for(int i=1; i<=n; i++){
            for(int j=1; j<=n; j++){
                if(arr[i][j]==0 ){
                    for(int dir=0; dir<4; dir++){

                        int nx=i+dx[dir];
                        int ny=j+dy[dir];

                        if(nx>0 && ny>0 && nx<=n && ny<=n && arr[nx][ny]>0){
                            tmp[i][j]+=arr[nx][ny];
                        }
                    }
                }
            }
        }


        for(int i=1; i<=n; i++){
            for(int j=1; j<=n; j++){
                if(tmp[i][j]>0) arr[i][j]=tmp[i][j]/10;
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        k=sc.nextInt();
        l=sc.nextInt();

        arr=new int[n+1][n+1];
        isRobot=new boolean[n+1][n+1];
        for(int i=1; i<=n; i++){
            for(int j=1; j<=n; j++){
                arr[i][j]=sc.nextInt();
            }
        }

        robot=new Point[k];
        for(int i=0; i<k; i++){
            robot[i]=new Point(sc.nextInt(), sc.nextInt());
            isRobot[robot[i].x][robot[i].y]=true;
        }

        for(int t=0; t<l; t++){
            //청소기 이동
            for(int i=0; i<k; i++) {
                robot[i]=choice(robot[i]);
            }
            
            //청소
             for(int i=0; i<k; i++) {
                clean(robot[i]);
            }

            //먼지 축적
            for(int i=1; i<=n; i++){
                for(int j=1; j<=n; j++){
                    if(arr[i][j]>0) arr[i][j]+=5;
                }
            }

            //확산
            addDust();

            int answer=0;
            for(int i=1; i<=n; i++){
                for(int j=1; j<=n; j++){
                    if(arr[i][j]>0) answer+=arr[i][j];
                }
            }
            System.out.println(answer);
        }
    }
}