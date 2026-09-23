import java.util.*;

class Point{
    int x, y, h, w, k, num, o;
    
    public Point(int x, int y, int h, int w, int k, int num) {
        this.x=x;
        this.y=y;
        this.h=h;
        this.w=w;
        this.k=k;
        this.num=num;
    }
    
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
}

public class Main {
    static int l, n, q;
    static int[] dx= {-1, 0, 1, 0};
    static int[] dy= {0, 1, 0, -1};
    static int[][] board, exist;
    static List<Point> peoples, orders;
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        l=sc.nextInt();
        n=sc.nextInt();
        q=sc.nextInt();
        board=new int[l][l];
        exist=new int[l][l];
        peoples=new ArrayList<>();
        orders=new ArrayList<>();
        Set<Integer> set=new HashSet<>();
        int total=0;
        
        for(int i=0; i<l; i++) {
            for(int j=0; j<l; j++) {
                board[i][j]=sc.nextInt();
            }
        }
        
        for(int i=0; i<n; i++) {
            Point p = new Point(sc.nextInt()-1, sc.nextInt()-1, sc.nextInt(), sc.nextInt(), sc.nextInt(), i+1);
            p.o=p.k;
            peoples.add(p);
            for(int r=p.x; r<p.x+p.h; r++) {
                for(int c=p.y; c<p.y+p.w; c++) {
                    exist[r][c]=i+1;
                }
            }
        }
        
        for(int i=0; i<q; i++) {
            orders.add(new Point(sc.nextInt(), sc.nextInt()));
        }
        
        for(Point order : orders) {
            for(Point p : peoples) {
                if(p.num==order.x) {
                    if(isAvailable(p, order.y, set)) {
                        move(p, order.y, set);
                        demage(p, set);
                        break;
                    }
                }
            }
            
            set.clear();
        }
        
        for(Point p : peoples) {
            total+=p.o-p.k;
        }
        System.out.println(total);
    }
    
    public static void demage(Point cur, Set<Integer> set) {
        int t=0;
        for(int i : set) {
            int cnt=0;
            for(int j=0; j<peoples.size(); j++) {
                Point p=peoples.get(j);
                if(i==p.num) {
                    for(int r=p.x; r<p.x+p.h; r++) {
                        for(int c=p.y; c<p.y+p.w; c++) {
                            if(board[r][c]==1) cnt++;
                        }
                    }
                    
                    p.k-=cnt;
                    if(p.k<=0) {
                        for(int r=p.x; r<p.x+p.h; r++) {
                            for(int c=p.y; c<p.y+p.w; c++) {
                                exist[r][c]=0;
                            }
                        }
                        peoples.remove(p);
                        j--;
                    }
                }
            }
        }
    }
    
    public static void move(Point p, int d, Set<Integer> set) {
        exist=new int[l][l];
        int nx=p.x+dx[d];
        int ny=p.y+dy[d];
        p.x=nx;
        p.y=ny;
        
        for(int i : set) {
            for(Point np : peoples) {
                if(np.num==i) {
                    nx=np.x+dx[d];
                    ny=np.y+dy[d];
                    np.x=nx;
                    np.y=ny;
                    break;
                }
            }
        }
        
        for(Point np : peoples) {
            for(int r=np.x; r<np.x+np.h; r++) {
                for(int c=np.y; c<np.y+np.w; c++) {
                    exist[r][c]=np.num;
                }
            }
        }
    }
    
    public static boolean isAvailable(Point p, int d, Set<Integer> set) {
        for(int r=p.x; r<p.x+p.h; r++) {
            for(int c=p.y; c<p.y+p.w; c++) {
                int nx=r+dx[d];
                int ny=c+dy[d];
                
                if(nx>=0 && nx<l && ny>=0 && ny<l && board[nx][ny]!=2) {
                    int num=exist[nx][ny];
                    if(num==p.num)continue;
                    if(num!=0) {
                        for(Point t : peoples) {
                            if(t.num==num) {
                                if(isAvailable(t, d, set)) {
                                    set.add(t.num);
                                    break;
                                }else return false;
                            }
                        }
                    }
                }else return false;
            }
        }
        
        return true;
    }
}
