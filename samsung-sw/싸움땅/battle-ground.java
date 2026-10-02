import java.util.*;

class Point{
    int num, x, y, s, dir, a, score;
    
    public Point(int num, int x, int y, int dir, int s) {
        this.x=x;
        this.y=y;
        this.num=num;
        this.dir=dir;
        this.s=s;
    }
    
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
}

public class Main {
    static int n, m, k;
    static int[] dx= {-1, 0, 1, 0};
    static int[] dy= {0, 1, 0, -1};
    static int[][] board;
    static List<Point> peoples=new ArrayList<>();
    static List<Point> guns=new ArrayList<>();
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        k=sc.nextInt();
        board=new int[n][n];
        int turn=1;
        
        int attack=0;
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                attack=sc.nextInt();
                if(attack>0) {
                    Point gun=new Point(i, j);
                    gun.a=attack;
                    guns.add(gun);
                }
            }
        }
        
        for(int i=0; i<m; i++) {
            peoples.add(new Point(i+1, sc.nextInt()-1, sc.nextInt()-1, sc.nextInt(), sc.nextInt()));
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
            int nx=p.x+dx[p.dir];
            int ny=p.y+dy[p.dir];
            
            if(nx<0 || nx>=n || ny<0 || ny>=n) {
                if(p.dir==0) p.dir=2;
                else if(p.dir==2) p.dir=0;
                else if(p.dir==1) p.dir=3;
                else if(p.dir==3) p.dir=1;
                nx=p.x+dx[p.dir];
                ny=p.y+dy[p.dir];
            }
            
            p.x=nx;
            p.y=ny;
            
            if(hasPeople(p)) {
                fight(p);
            }else {
                getGun(p);
            }
        }
    }
    
    static void fight(Point p) {
        int pa=p.a+p.s;
        Point lose=new Point(0,0);
        Point win=new Point(0,0);
        for(Point np : peoples) {
            if(p==np) continue;
            if(p.x==np.x && p.y==np.y) {
                int npa=np.a+np.s;
                
                if(npa<pa) {
                    p.score+=pa-npa;
                    win=p;
                    lose=np;
                }else if(npa==pa) {
                    if(p.s>np.s) {
                        win=p;
                        lose=np;
                    }else if(p.s<np.s) {
                        win=np;
                        lose=p;
                    }
                }else {
                    np.score+=npa-pa;
                    win=np;
                    lose=p;
                }
                break;
            }
        }
        
        if(lose.a>0) {
            Point gun=new Point(lose.x, lose.y);
            gun.a=lose.a;
            guns.add(gun);
            lose.a=0;
        }
        
        int dir=lose.dir;
        int nx=lose.x+dx[dir];
        int ny=lose.y+dy[dir];
        
        while(true) {
            if(nx<0 || nx>=n || ny<0 || ny>=n || hasPeople(new Point(nx, ny))) {
                dir=changeDir(dir);
                nx=lose.x+dx[dir];
                ny=lose.y+dy[dir];
            }else {
                lose.dir=dir;
                break;
            }
        }
        
        lose.x=nx;
        lose.y=ny;
        getGun(lose);
        getGun(win);
    }
    
    static void getGun(Point p) {
        List<Point> curGun=hasGun(p);
        if(!curGun.isEmpty()) {
            for(int i=0; i<curGun.size(); i++) {
                Point g=curGun.get(i);
                if(p.a<g.a) {
                    if(p.a==0) {
                        p.a=g.a;
                        guns.remove(g);
                    }else {
                        int tmp=g.a;
                        g.a=p.a;
                        p.a=tmp;
                    }
                }
            }
        }
    }
    
    public static int changeDir(int d) {
        int dir=d+1;
        if(dir>3) {
            dir=0;
        }
        return dir;
    }
    
    static boolean hasPeople(Point p) {
        for(Point np : peoples) {
            if(p==np) continue;
            if(p.x==np.x && p.y==np.y) return true;
        }
        return false;
    }
    
    static List<Point> hasGun(Point p) {
        List<Point> list=new ArrayList<>();
        for(Point ng : guns) {
            if(p.x==ng.x && p.y==ng.y) {
                list.add(ng);
            }
        }
        return list;
    }
}
