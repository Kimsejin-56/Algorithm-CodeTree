import java.util.*;

class Point{
    int num, x, y, d, s, a, score;
    
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
    
    public Point(int num, int x, int y, int d, int s) {
        this.num=num;
        this.x=x;
        this.y=y;
        this.d=d;
        this.s=s;
    }
}

public class Main {
    static int n, m, k;
    static int[] dx= {-1, 0, 1, 0};
    static int[] dy= {0, 1, 0, -1};
    static List<Point> peoples=new ArrayList<>();
    static List<Point> guns=new ArrayList<>();
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        k=sc.nextInt();
        int turn=1;
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                int num=sc.nextInt();
                if(num>0) {
                    Point g=new Point(i, j);
                    g.a=num;
                    guns.add(g);
                }
            }
        }
        
        for(int i=0; i<m; i++) {
            peoples.add(new Point(i, sc.nextInt()-1, sc.nextInt()-1, sc.nextInt(), sc.nextInt()));
        }
        
        while(turn<=k) {
            move();
            turn++;
        }
        
        for(Point p : peoples) {
            System.out.print(p.score+" ");
        }
    }
    
    static void move() {
        for(Point p : peoples) {
            int nx=p.x+dx[p.d];
            int ny=p.y+dy[p.d];
            
            if(nx<0 || nx>=n || ny<0 || ny>=n) {
                if(p.d==0) p.d=2;
                else if(p.d==2) p.d=0;
                else if(p.d==1) p.d=3;
                else if(p.d==3) p.d=1;
                
                nx=p.x+dx[p.d];
                ny=p.y+dy[p.d];
            }
            
            p.x=nx;
            p.y=ny;
            
            Point p2=getPeople(p);
            if(p2!=null) {
                figth(p, p2);
            }
            
            getGun(p);
        }
    }
    
    static void getGun(Point p) {
        List<Point> gun=getGuns(p);
        if(!gun.isEmpty()) {
            int max=0;
            Point ng=new Point(0,0);
            if(p.a==0) {
                for(Point g : gun) {
                    if(max<g.a) {
                        max=g.a;
                        ng=g;
                    }
                }
                p.a=ng.a;
                guns.remove(ng);
            }else {
                max=p.a;
                for(Point g : gun) {
                    if(max<g.a) {
                        max=g.a;
                        ng=g;
                    }
                }
                if(max>p.a) {
                    int tmp=p.a;
                    p.a=ng.a;
                    ng.a=tmp;
                }
            }
        }
    }
    
    static void figth(Point p, Point o) {
        int pa=p.a+p.s;
        int oa=o.a+o.s;
        
        Point win;
        Point lose;
        
        if(pa>oa) {
            win=p;
            lose=o;
        }else if(pa==oa) {
            if(p.s>o.s) {
                win=p;
                lose=o;
            }else {
                win=o;
                lose=p;
            }
        }else {
            win=o;
            lose=p;
        }
        
        if(lose.a>0) {
            Point gun=new Point(lose.x, lose.y);
            gun.a=lose.a;
            guns.add(gun);
            lose.a=0;
        }
        
        for(int i=0; i<4; i++) {
            int nx=lose.x+dx[lose.d];
            int ny=lose.y+dy[lose.d];
            Point tmp=getPeople(new Point(nx, ny));
            if(tmp!=null || nx<0 || nx>=n || ny<0 || ny>=n) {
                lose.d++;
                if(lose.d>3) lose.d=0;
                continue;
            }
            lose.x=nx;
            lose.y=ny;
            getGun(lose);
            break;
        }
        
        win.score+=Math.abs(pa-oa);
        getGun(win);
    }
    
    static Point getPeople(Point p) {
        for(Point o : peoples) {
            if(o==p) continue;
            if(o.x==p.x && o.y==p.y) return o;
        }
        return null;
    }
    
    static List<Point> getGuns(Point p){
        List<Point> gun=new ArrayList<>();
        for(Point g : guns) {
            if(g.x==p.x && g.y==p.y) gun.add(g);
        }
        
        return gun;
    }
}
        