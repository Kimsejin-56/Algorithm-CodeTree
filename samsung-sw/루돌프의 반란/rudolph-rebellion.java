import java.util.*;

class Point implements Comparable<Point>{
    int num, x, y, k, score;
    boolean dead;
    
    public Point(int num, int x, int y) {
        this.num=num;
        this.x=x;
        this.y=y;
    }
    
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
    
    public int compareTo(Point p) {
        if(this.x==p.x) return p.y-this.y;
        return p.x-this.x;
    }
}

public class Main {
    static int n,m,p,c,d;
    static int[] dx= {-1, 0, 1, 0, -1, -1, 1, 1};
    static int[] dy= {0, 1, 0, -1, -1, 1, -1, 1};
    static List<Point> santas=new ArrayList<>();
    static Point dog;
    static int turn;
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
            for(Point p : santas) {
                if(p.dead) continue;
                p.score++;
            }
            if(allDie()) break;
            turn++;
        }
        
        for(Point p : santas) {
            System.out.print(p.score+" ");
        }
    }
    
    static boolean allDie() {
        for(Point p : santas) {
            if(!p.dead) return false;
        }
        return true;
    }
    
    static void moveDog() {
        Point p=selectSanta();
        int dir=dash(p);
        if(p.x==dog.x && p.y==dog.y) {
            p.score+=c;
            p.k=turn+2;
            
            for(int i=0; i<c; i++) {
                p.x+=dx[dir];
                p.y+=dy[dir];
            }
            
            if(p.x<0 || p.x>=n || p.y<0 || p.y>=n) {
                p.dead=true;
                return;
            }
            
            crash(p, dir);
        }
    }
    
    static void moveSanta() {
        for(Point p : santas) {
            if(p.dead) continue;
            if(p.k>turn) continue;
            int min=(dog.x-p.x)*(dog.x-p.x)+(dog.y-p.y)*(dog.y-p.y);
            int dir=-1;
            
            for(int i=0; i<4; i++) {
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];
                
                if(nx>=0 && nx<n && ny>=0 && ny<n && !hasSanta(nx, ny)) {
                    int dis=(nx-dog.x)*(nx-dog.x)+(ny-dog.y)*(ny-dog.y);
                    //System.out.println("in"+p.num+" "+nx+" "+ny+"    "+dis);
                    if(min>dis) {
                        min=dis;
                        dir=i;
                    }
                }
            }
            
            if(dir!=-1) {
                p.x+=dx[dir];
                p.y+=dy[dir];
                
                if(p.x==dog.x && p.y==dog.y) {
                    p.score+=d;
                    p.k=turn+2;
                    
                    if(dir==1) dir=3;
                    else if(dir==3) dir=1;
                    else if(dir==0) dir=2;
                    else if(dir==2) dir=0;
                    
                    for(int i=0; i<d; i++) {
                        p.x+=dx[dir];
                        p.y+=dy[dir];
                    }
                    
                    if(p.x<0 || p.x>=n || p.y<0 || p.y>=n) {
                        p.dead=true;
                        continue;
                    }
                    
                    crash(p, dir);
                }
            }
        }
    }
    
    static void crash(Point p, int dir) {
        for(Point cur : santas) {
            if(cur==p) continue;
            if(p.dead) continue;
            if(p.x==cur.x && p.y==cur.y) {
                cur.x+=dx[dir];
                cur.y+=dy[dir];
                
                if(cur.x<0 || cur.x>=n || cur.y<0 || cur.y>=n) {
                    cur.dead=true;
                    return;
                }
                
                crash(cur, dir);
            }
        }
    }
    
    static Point selectSanta() {
        List<Point> sort=new ArrayList<>();
        int min=Integer.MAX_VALUE;
        for(Point p : santas) {
            if(p.dead) continue;
            int dis=(dog.x-p.x)*(dog.x-p.x)+(dog.y-p.y)*(dog.y-p.y);
            if(min>dis) {
                min=dis;
                sort.clear();
                sort.add(p);
            }else if(min==dis) sort.add(p);
        }
        Collections.sort(sort);
        return sort.get(0);
    }
    
    static boolean hasSanta(int x, int y){
        for(Point cur : santas) {
            if(cur.dead) continue;
            if(x==cur.x && y==cur.y)return true;
        }
        return false;
    }
    
    static int dash(Point p) {
        int min=Integer.MAX_VALUE;
        int dir=-1;
        
        for(int i=0; i<8; i++) {
            int nx=dog.x+dx[i];
            int ny=dog.y+dy[i];
            
            if(nx>=0 && nx<n && ny>=0 && ny<n) {
                int dis=(nx-p.x)*(nx-p.x)+(ny-p.y)*(ny-p.y);
                if(min>dis) {
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
