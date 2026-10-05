import java.util.*;

class Point{
    int num, x, y, h, w, k, o;
    boolean dead;
    
    public Point(int num, int x, int y, int h, int w, int k) {
        this.num=num;
        this.x=x;
        this.y=y;
        this.h=h;
        this.w=w;
        this.k=k;
    }
}

public class Main {
    static int l, n, q;
    static int[][] board, exist;
    static List<Point> peoples=new ArrayList<>();
    static int[] dx= {-1, 0, 1, 0};
    static int[] dy= {0, 1, 0, -1};
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        l=sc.nextInt();
        n=sc.nextInt();
        q=sc.nextInt();
        int turn=1;
        board=new int[l][l];
        exist=new int[l][l];
        
        for(int i=0; i<l; i++) {
            for(int j=0; j<l; j++) {
                board[i][j]=sc.nextInt();
            }
        }
        
        for(int i=0; i<n; i++) {
            Point p=new Point(i+1, sc.nextInt()-1, sc.nextInt()-1, sc.nextInt(), sc.nextInt(), sc.nextInt());
            p.o=p.k;
            peoples.add(p);
        }
        
        batchPeople();
        
        while(turn<=q) {
            int num=sc.nextInt();
            int dir=sc.nextInt();
            Point p=peoples.get(num-1);
            if(p.dead) {
                turn++;
                continue;
            }
            move(p, dir);
            turn++;
            batchPeople();
        }
        int total=0;
        for(Point p : peoples) {
            if(p.dead) continue;
            total+=p.o-p.k;
        }
        
        System.out.println(total);
    }
    
    static void move(Point p, int dir) {
        Set<Integer> set=new HashSet<>();
        set.add(p.num);
        if(!canMove(p, dir, set)) return;
        
        for(int num : set) {
            Point mp=peoples.get(num-1);
            mp.x+=dx[dir];
            mp.y+=dy[dir];
        }
        
        for(int num : set) {
            Point mp=peoples.get(num-1);
            if(mp.num==p.num) continue;
            getDemage(mp);
            if(mp.k<=0) mp.dead=true;
        }
    }
    
    static void getDemage(Point p) {
        int demage=0;
        for(int i=p.x; i<p.x+p.h; i++) {
            for(int j=p.y; j<p.y+p.w; j++) {
                if(board[i][j]==1) demage++;    
            }
        }
        p.k-=demage;
    }
    
    static boolean canMove(Point p, int dir, Set<Integer> set) {
        for(int i=p.x; i<p.x+p.h; i++) {
            for(int j=p.y; j<p.y+p.w; j++) {
                int nx=i+dx[dir];
                int ny=j+dy[dir];
                
                if(nx<0 || nx>=l || ny<0 || ny>=l || board[nx][ny]==2) return false;
                
                if(exist[nx][ny]>0 && !set.contains(exist[nx][ny])) {
                    set.add(exist[nx][ny]);
                    Point np=peoples.get(exist[nx][ny]-1);
                    if(!canMove(np, dir, set)) return false;
                }
            }
        }
        return true;
    }
    
    static Point batchPeople() {
        exist=new int[l][l];
        for(Point p : peoples) {
            if(p.dead) continue;
            for(int i=p.x; i<p.x+p.h; i++) {
                for(int j=p.y; j<p.y+p.w; j++) {
                    exist[i][j]=p.num;
                }
            }
        }
        return null;
    }
}