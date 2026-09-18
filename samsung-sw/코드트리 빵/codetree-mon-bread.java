import java.util.*;

class Point implements Comparable<Point>{
    int x, y, num, dir;
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
    static int n,m, visitId;
    static int[][] board, visited;
    static List<Point> stores, peoples, camps;
    static int[] dx={-1, 0, 0, 1};
    static int[] dy={0, -1, 1, 0};
    static boolean[][] check;
    static Queue<Point> q;


    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        board=new int[n][n];
        check=new boolean[n][n];
        stores=new ArrayList<>();
        peoples=new ArrayList<>();
        camps=new ArrayList<>();
        q=new ArrayDeque<>();
        visited=new int[n][n];
        ArrayList<Point>list=new ArrayList<>();
        int t=1;

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                board[i][j]=sc.nextInt();
                if(board[i][j]==1) camps.add(new Point(i, j));
            }
        }

        for(int i=0; i<m; i++){
            Point p=new Point(sc.nextInt()-1, sc.nextInt()-1);
            stores.add(p);
        }

        int cnt=0;
        while(cnt!=m){
            for(Point p : peoples){
                move(p);
            }

            for(int i=0; i<peoples.size(); i++){
                Point p=peoples.get(i);
                Point store=stores.get(p.num);
                if(p.x==store.x && p.y==store.y){
                    check[store.x][store.y]=true;
                    cnt++;
                    peoples.remove(p);
                    i--;
                    if(cnt==m){
                        System.out.println(t);
                        return;
                    }
                }
            }
           

            if(t<=m){
                int min=Integer.MAX_VALUE;
                Point s=stores.get(t-1);
                int[][] dis=bfs(s);
                for(Point c : camps){
                    if(check[c.x][c.y]) continue;
                    int num=dis[c.x][c.y];
                    if(num==-1) continue;
                    if(min>num){
                        min=num;
                        list.clear();
                        list.add(c);
                    } else if(min==num){
                        list.add(c);
                    }
                }

                Collections.sort(list);
                Point select=list.get(0);
                check[select.x][select.y]=true;
                Point people=new Point(select.x, select.y);
                people.num=t-1;
                peoples.add(people);
            }

            t++;
        }
    }

    public static void move(Point s){
        q.clear();
        visitId++;
        q.offer(s);
        visited[s.x][s.y]=visitId;
        Point arrive=stores.get(s.num);
        s.dir=-1;

        while(!q.isEmpty()){
            Point p=q.poll();

            if(arrive.x==p.x && arrive.y==p.y){
                s.x+=dx[p.dir];
                s.y+=dy[p.dir];
                return;
            }

            for(int i=0; i<4; i++){
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];

                if(nx>=0 && nx<n && ny>=0 && ny<n && visited[nx][ny]!=visitId && !check[nx][ny]){
                    Point next=new Point(nx, ny);
                    if(p.dir==-1) next.dir=i;
                    else next.dir=p.dir;
                    q.offer(next);
                    visited[nx][ny]=visitId;
                }
            }
        }
    }

    public static int[][] bfs(Point s){
        q.clear();
        int[][] dis=new int[n][n];
        for(int i=0; i<n; i++){
            Arrays.fill(dis[i], -1);
        }
        dis[s.x][s.y]=0;
        q.offer(s);

        while(!q.isEmpty()){
            Point p=q.poll();

            for(int i=0; i<4; i++){
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];

                if(nx>=0 && nx<n && ny>=0 && ny<n && dis[nx][ny]==-1 && !check[nx][ny]){
                    q.offer(new Point(nx, ny));
                    dis[nx][ny]=dis[p.x][p.y]+1;
                }
            }
        }
        return dis;
    }
}


