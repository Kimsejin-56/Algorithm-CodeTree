import java.util.*;

class Point implements Comparable<Point>{
    int x, y, k;
    int num, score;
    boolean dead;
    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }

    public Point(int num, int x, int y){
        this.num=num;
        this.x=x;
        this.y=y;
    }

    public int compareTo(Point p){
        if(this.x==p.x) return p.y-this.y;
        return p.x-this.x;
    }
}
public class Main {
    static int n, m, p, c, d, turn;
    static int[] dx={-1, 0, 1, 0, -1, -1, 1, 1};
    static int[] dy={0, 1, 0, -1, -1, 1, -1, 1};
    static Point dog;
    static List<Point> peoples=new ArrayList<>();
    static List<Point> history=new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        p=sc.nextInt();
        c=sc.nextInt();
        d=sc.nextInt();
        turn=1;

        dog=new Point(sc.nextInt()-1, sc.nextInt()-1);

        for(int i=1; i<=p; i++){
            Point p=new Point(sc.nextInt(), sc.nextInt()-1, sc.nextInt()-1);
            peoples.add(p);
        }

        peoples.sort((a, b) -> a.num-b.num);

        while(turn<=m){
            moveDog();
            movePeoples();
            for(Point p : peoples){
                if(!p.dead) p.score++;
            }
            if(allDie()) break;
            turn++;
        }

        for(Point p : peoples){
            System.out.print(p.score+" ");
        }
    }

    static boolean allDie(){
        int cnt=0;
        for(Point p : peoples){
            if(p.dead) cnt++;
        }

        if(cnt==peoples.size()) return true;
        return false;
    }

    static void movePeoples() {
        for (int i = 0; i < peoples.size(); i++) {
            Point p = peoples.get(i);
            if(p.dead) continue;
            if(p.k>turn) continue;
            int curDis = (dog.x - p.x) * (dog.x - p.x) + (dog.y - p.y) * (dog.y - p.y);
            int min = curDis;
            int dir=-1;

            for (int j = 0; j < 4; j++) {
                int nx = p.x + dx[j];
                int ny = p.y + dy[j];

                if (nx >= 0 && nx < n && ny >= 0 && ny < n && !hasPeople(nx, ny)) {
                    int dis=(nx-dog.x)*(nx-dog.x)+(ny-dog.y)*(ny-dog.y);
                    if(min>dis){
                        min=dis;
                        dir=j;
                    }
                }
            }

            if(dir!=-1){
                p.x+=dx[dir];
                p.y+=dy[dir];
            }

            if(p.x==dog.x && p.y==dog.y){
                p.score+=d;
                p.k=turn+2;

                if(dir==1) dir=3;
                else if(dir==3) dir=1;
                else if(dir==2) dir=0;
                else if(dir==0) dir=2;

                for(int j=0; j<d; j++){
                    p.x+=dx[dir];
                    p.y+=dy[dir];
                }

                if(p.x<0 || p.x>=n || p.y<0 || p.y>=n){
                    p.dead=true;
                    continue;
                }

                crash(p, dir);
            }
        }


    }

    static boolean hasPeople(int x, int y){
        for(Point cp : peoples){
            if(cp.dead) continue;
            if(cp.x==x && cp.y==y) return true;
        }
        return false;
    }

    public static void moveDog(){
        List<Point> sort=new ArrayList<>();
        int min=Integer.MAX_VALUE;

        for(int i=0; i<peoples.size(); i++){
            Point p=peoples.get(i);
            if(p.dead) continue;
            int dis=(dog.x-p.x)*(dog.x-p.x)+(dog.y-p.y)*(dog.y-p.y);
            if(min>dis){
                min=dis;
                sort.clear();
                sort.add(p);
            }else if(min==dis) sort.add(p);
        }

        Collections.sort(sort);
        Point p=sort.get(0);
        int dir=dash(p);

        if(dog.x==p.x && dog.y==p.y){
            p.score+=c;
            p.k=turn+2;

            for(int j=0; j<c; j++){
                p.x+=dx[dir];
                p.y+=dy[dir];
            }

            if(p.x<0 || p.x>=n || p.y<0 || p.y>=n){
                p.dead=true;
                return;
            }

            crash(p, dir);
        }
    }

    static void crash(Point p, int dir){
        for(int i=0; i<peoples.size(); i++){
            Point cp=peoples.get(i);
            if(p==cp) continue;
            if(p.x==cp.x && p.y==cp.y) {
                cp.x += dx[dir];
                cp.y += dy[dir];

                if (cp.x < 0 || cp.x >= n || cp.y < 0 || cp.y >= n) {
                    cp.dead=true;
                    return;
                }

                crash(cp, dir);
            }
        }
    }

    static int dash(Point p){
        int dir=-1;
        int min=Integer.MAX_VALUE;

        for(int i=0; i<8; i++){
            int nx=dog.x+dx[i];
            int ny=dog.y+dy[i];

            if(nx>=0 && nx<n && ny>=0 && ny<n){
                int dis=(nx-p.x)*(nx-p.x)+(ny-p.y)*(ny-p.y);
                if(min>dis){
                    min=dis;
                    dir=i;
                }
            }
        }

        dog.x+=dx[dir];
        dog.y+=dy[dir];
        return dir;
    }

}