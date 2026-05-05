import java.util.*;

class Point implements Comparable<Point>{
    int x,y;

    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int compareTo(Point p){
        if(this.x==p.x) return this.y - p.y;
        return this.x - p.x;
    }
}

class Dis{
    int dis;
    Point obj;
    public Dis(int dis, Point obj){
        this.dis=dis;
        this.obj=obj;
    } 
}

public class Main {
    static int N, M, K, answer;
    static int[][] board;
    static int[] dx={-1, 1, 0, 0};
    static int[] dy={0, 0, -1, 1};
    static Point exit;
    static List<Point> players=new ArrayList<>();


    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);

        N=sc.nextInt();
        M=sc.nextInt();
        K=sc.nextInt();

        board=new int[N+1][N+1];
        for(int i=1; i<=N; i++){
            for(int j=1; j<=N; j++){
                board[i][j]=sc.nextInt();
            }
        }

        for(int i=0; i<M; i++){
            players.add(new Point(sc.nextInt(), sc.nextInt()));
        }

        exit=new Point(sc.nextInt(), sc.nextInt());

        while(K > 0){
            for(int i=0; i<players.size(); i++) {
                move(players.get(i));
            }
            exitPerson();
            if(players.size()==0) break;
            spin();
            K--;
        }

        System.out.println(answer);
        System.out.println(exit.x+" "+exit.y);
    }

    
    static void exitPerson(){
        int len=players.size();
        List<Point> tmp=new ArrayList<>();
        for(int i=0; i<len; i++){
            Point p = players.get(i);
            if(p.x!=exit.x || p.y!=exit.y){
                tmp.add(p);
            }
        }
        players=tmp;
    }

    static void spin(){
        Dis square=choice();
        Point start=square.obj;
        int dis=square.dis;
        int size=dis+1;
        int[][] tmp=new int[size][size];

        int sx=start.x;
        int sy=start.y;

        for(int i=0; i< size; i++){
            for(int j=0; j<size; j++){
                int val = board[sx+i][sy+j];
                if(val>0) val--;
                tmp[j][size-i-1]=val;
            }
        }

        for(int i=0; i< size; i++){
            for(int j=0; j<size; j++){
                board[sx+i][sy+j]=tmp[i][j];
            }
        }

        int ex=exit.x-sx;
        int ey=exit.y-sy;
        if(ex>=0 && ex<size && ey>=0 && ey<size){
            int rx=ey;
            int ry=size-ex-1;
            exit.x=rx+sx;
            exit.y=ry+sy;
        }

        for(Point p :players){
            int px=p.x-sx;
            int py=p.y-sy;
            if(px>=0 && px<size && py>=0 && py<size){
                int rx=py;
                int ry=size-px-1;
                p.x=rx+sx;
                p.y=ry+sy;
            }
        }

                
    }

    static Dis choice(){
        List<Point> squares=new ArrayList<>();
        int min=Integer.MAX_VALUE;
        for(Point p : players) {
            int dis=Math.max(Math.abs(p.x-exit.x), Math.abs(p.y-exit.y));
            min=Math.min(min, dis);
        }

        for(Point p : players){
            int dist=Math.max(Math.abs(p.x-exit.x), Math.abs(p.y-exit.y));
            if(min < dist) continue;

            for(int sx=1; sx+dist<=N; sx++){
                for(int sy=1; sy+dist<=N; sy++){
                    boolean isPerson=(sx<=p.x && p.x<=sx+dist && sy<=p.y && p.y<=sy+dist);
                    boolean isExit = (sx <= exit.x && exit.x <= sx + dist && sy <= exit.y && exit.y <= sy + dist);

                    if(!isPerson || !isExit) continue;

                    squares.add(new Point(sx, sy));

                }
            }
        }

        Collections.sort(squares);
        Point sq=squares.get(0);
        return new Dis(min, sq);
    }


    static void move(Point p){
        Queue<Point> q=new LinkedList<>();
        q.offer(p);
        List<Dis> dists=new ArrayList<Dis>();
        int original=Math.abs(p.x-exit.x)+Math.abs(p.y-exit.y);

        while(!q.isEmpty()){
            int len =q.size();

            for(int t=0; t<len; t++){
                Point player=q.poll();
                for(int i=0; i<4; i++){
                    int nx=player.x+dx[i];
                    int ny=player.y+dy[i];

                    if(nx>0 && ny>0 && nx<=N && ny<=N && board[nx][ny]==0){
                        Point tmp=new Point(nx, ny);
                        int dist=Math.abs(tmp.x-exit.x)+Math.abs(tmp.y-exit.y);
                        if(original > dist){
                           dists.add(new Dis(dist, tmp));
                        }
                    }
                }
            }

            Point result=getMin(dists);
            
            if(result!=null){
                answer++;
                p.x=result.x;
                p.y=result.y;
            } 
        }
    }

    static Point getMin(List<Dis> dists){
        int min=Integer.MAX_VALUE;
        Point p=null;
        for(Dis dist : dists){
            if(min > dist.dis){
                min=dist.dis;
                p=dist.obj;
            }
        }
        
        return p;
    }
}