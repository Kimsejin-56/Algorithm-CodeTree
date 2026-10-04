
import java.util.*;

class Point{
    int x, y;
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
}

public class Main {
    static int n, m, k, cnt;
    static int[][] board;
    static int[] dx= {-1, 1, 0, 0};
    static int[] dy= {0, 0, -1, 1};
    static Point exit;
    static List<Point> peoples=new ArrayList<>();
    static List<Point> spinPerson=new ArrayList<>();
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        k=sc.nextInt();
        int turn=1;
        board=new int[n][n];
        cnt=0;
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                board[i][j]=sc.nextInt();;
            }
        }
        
        for(int i=0; i<m; i++) {
            peoples.add(new Point(sc.nextInt()-1, sc.nextInt()-1));
        }
        
        exit=new Point(sc.nextInt()-1, sc.nextInt()-1);
        
        while(turn<=k) {
            move();
            for(int i=0; i<peoples.size(); i++) {
                Point p=peoples.get(i);
                if(p.x==exit.x && p.y==exit.y) {
                    peoples.remove(p);
                    i--;
                }
            }
            simulataion();
            spinPerson.clear();
            turn++;
        }
        
        int x=exit.x+1;
        int y=exit.y+1;
        System.out.println(cnt);
        System.out.println(x+" "+y);
    }
    
    static int[][] spin(int[][] arr) {
        int len=arr.length;
        int[][] temp=new int[len][len];
        
        for(int i=0; i<len; i++) {
            for(int j=0; j<len; j++) {
                if(arr[i][j]>0) arr[i][j]--;
                temp[j][len-i-1]=arr[i][j];
            }
        }
        
        return temp;
    }
    
    static void simulataion() {
        int ms=Integer.MAX_VALUE;
        int mx=Integer.MAX_VALUE;
        int my=Integer.MAX_VALUE;
        
        for(int l=2; l<=n; l++) {
            for(int i=0; i<=n-l; i++) {
                for(int j=0; j<=n-l; j++) {
                    if(isAvaiable(i, j, l)) {
                        getPerson(i, j, l);
                        int[][] cut=new int[l][l];
                        
                        int x=i;
                        int y=j;
                        for(int r=0; r<l; r++) {
                            for(int c=0; c<l; c++) {
                                cut[r][c]=board[x][y++];
                            }
                            x++;
                            y=j;
                        }
                        cut=spin(cut);
                        
                        x=0;
                        y=0;
                        for(Point p : spinPerson) {
                            x=p.x-i;
                            y=p.y-j;
                            p.x=y+i;
                            p.y=l-x-1+j;

                        }
                        
                        x=exit.x-i;
                        y=exit.y-j;
                        exit.x=y+i;
                        exit.y=l-x-1+j;
                        
                        x=i;
                        y=j;
                        for(int r=0; r<l; r++) {
                            for(int c=0; c<l; c++) {
                                board[x][y++]=cut[r][c];
                            }
                            x++;
                            y=j;
                        }
                        
                        return;
                    }
                    
                }
            }
        }
    }
  
    
    static void getPerson(int x, int y, int len) {
        for(int i=x; i<x+len; i++) {
            for(int j=y; j<y+len; j++) {
                for(Point p : peoples) {
                    if(p.x==i && p.y==j) spinPerson.add(p);
                }
            }
        }
    }
    
    static boolean isAvaiable(int x, int y, int len) {
        boolean person=false;
        boolean ex=false;
        
        for(int i=x; i<x+len; i++) {
            for(int j=y; j<y+len; j++) {
                if(hasPerson(i, j)) person=true;
                if(exit.x==i && exit.y==j) ex=true;
            }
        }
        
        return person && ex;
    }
    
    static boolean hasPerson(int x, int y) {
        for(Point p : peoples) {
            if(p.x==x && p.y==y) return true;
        }
        return false;
    }
    
    static void move() {
        for(Point p : peoples) {
            for(int i=0; i<4; i++) {
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];
                
                if(nx>=0 && nx<n && ny>=0 && ny<n && board[nx][ny]==0 &&
                    getDis(p.x, p.y, exit.x, exit.y)>getDis(nx, ny, exit.x, exit.y)) {
                    p.x=nx;
                    p.y=ny;
                    cnt++;
                    break;
                }
            }
        }
    }
    
    static int getDis(int x1, int y1, int x2, int y2) {
        return Math.abs(x1-x2)+Math.abs(y1-y2);
    }
}

