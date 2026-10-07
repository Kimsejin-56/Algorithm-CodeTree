import java.util.*;

class Point{
    int num, x, y, score, k, dir;
    boolean dead;
    
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public Point(int num, int x, int y) {
        this.num=num;
        this.x=x;
        this.y=y;
    }
}

public class Main {
    static int n, m, p, c, d, turn;
    static Point dog;
    static List<Point> santas=new ArrayList<>();
    static int[] dx= {-1, 0, 1, 0, -1, -1, 1, 1};
    static int[] dy= {0, 1, 0, -1, -1, 1, -1, 1};
    
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        p=sc.nextInt();
        c=sc.nextInt();
        d=sc.nextInt();
        turn=1;
        
        dog=new Point(sc.nextInt()-1, sc.nextInt()-1);
        for(int i=0; i<p; i++) {
            santas.add(new Point(sc.nextInt(), sc.nextInt()-1, sc.nextInt()-1));
        }
        santas.sort((a,b)->a.num-b.num);
        
        while(turn<=m) {
            moveDog();
            moveSanta();
            if(exit()) break;
            for(Point s : santas) {
                if(s.dead) continue;
                s.score++;
            }
            turn++;
        }
        
        for(Point s : santas) {
            System.out.print(s.score+" ");
        }
    }
    
    static void moveDog() {
        int min=Integer.MAX_VALUE;
        int mx=0;
        int my=0;
        Point select=new Point(0,0);
        
        for(Point p :santas) {
            if(p.dead) continue;
            int dis=getDis(p.x, p.y, dog.x, dog.y);
            if(bestSanta(min, mx, my, dis, p)) {
                min=dis;
                mx=p.x;
                my=p.y;
                select=p;
            }
        }
        
        min=Integer.MAX_VALUE;
        for(int i=0; i<8; i++) {
            int nx=dog.x+dx[i];
            int ny=dog.y+dy[i];
            
            if(nx>=0 && nx<n && ny>=0 && ny<n) {
                int dis=getDis(nx, ny, select.x, select.y);
                if(min>dis) {
                    min=dis;
                    mx=nx;
                    my=ny;
                    dog.dir=i;
                }
            }
        }
        
        dog.x=mx;
        dog.y=my;
        
        if(dog.x==select.x && dog.y==select.y) {
            select.score+=c;
            select.k=turn+2;
            
            for(int i=0; i<c; i++) {
                select.x+=dx[dog.dir];
                select.y+=dy[dog.dir];
            }
            
            if(select.x<0 || select.x>=n || select.y<0 || select.y>=n) {
                select.dead=true;
                return;
            }
            
            //상호작용
            if(hasSanta(select)) {
                chainReaction(select, dog.dir);
            }
        }
    }
    
    static void moveSanta() {
        for(Point p : santas) {
            if(p.dead) continue;
            if(p.k>turn) continue;
            int min=getDis(p.x, p.y , dog.x, dog.y);
            int mx=p.x;
            int my=p.y;
            
            for(int i=0; i<4; i++) {
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];
                
                if(nx>=0 && nx<n && ny>=0 && ny<n && !hasSanta(new Point(nx, ny))) {
                    int dis=getDis(nx, ny, dog.x, dog.y);
                    if(min>dis) {
                        min=dis;
                        mx=nx;
                        my=ny;
                        p.dir=i;
                    }
                }
            }
            
            p.x=mx;
            p.y=my;
            
            if(dog.x==p.x && dog.y==p.y) {
                p.score+=d;
                p.k=turn+2;
                
                if(p.dir==0) p.dir=2;
                else if(p.dir==2) p.dir=0;
                else if(p.dir==1) p.dir=3;
                else if(p.dir==3) p.dir=1;
                
                for(int i=0; i<d; i++) {
                    p.x+=dx[p.dir];
                    p.y+=dy[p.dir];
                }
                
                if(p.x<0 || p.x>=n || p.y<0 || p.y>=n) {
                    p.dead=true;
                    continue;
                }
                
                //상호작용
                if(hasSanta(p)) {
                    chainReaction(p, p.dir);
                }
            }
        }
    }
    
    static void chainReaction(Point p, int dir) {
        Point np=new Point(0,0);
        for(Point s : santas) {
            if(s.dead) continue;
            if(s==p) continue;
            if(s.x==p.x && s.y==p.y) np=s;
        }
        
        np.x+=dx[dir];
        np.y+=dy[dir];
        
        if(np.x<0 || np.x>=n || np.y<0 || np.y>=n) {
            np.dead=true;
            return;
        }
        
        if(hasSanta(np)) {
            chainReaction(np, dir);
        }
    }
    
    static boolean exit() {
        int cnt=0;
        for(Point s : santas) {
            if(s.dead) cnt++;
        }
        
        return cnt==p;
    }
    
    static boolean bestSanta(int mdis, int mx, int my, int dis, Point p) {
        if(mdis!=dis) return mdis>dis;
        if(mx!=p.x) return mx<p.x;
        return my<p.y;
    }
    
    static boolean hasSanta(Point p) {
        for(Point s : santas) {
            if(s.dead) continue;
            if(s==p) continue;
            if(s.x==p.x && s.y==p.y) return true;
        }
        return false;
    }
    
    static int getDis(int x1, int y1, int x2, int y2) {
        return(x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
    }
}
        