import java.util.*;

class Point implements Comparable<Point>{
    int k, h, w, c, r;

    public Point(int k, int h, int w, int c) {
        this.k=k;
        this.h=h;
        this.w=w;
        this.c=c;
    }
    
    public int compareTo(Point p) {
        return this.k-p.k;
    }
}

public class Main {
    static int n, m;
    static int[][] board;
    static List<Point> box;
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        board=new int[n][n];
        box=new ArrayList<>();
        List<Integer> answer=new ArrayList<>();
        
        for(int i=0; i<m; i++) {
            Point b=new Point(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt()-1);
            b.r=b.h-1;
            box.add(b);
        }
        
        putBox();
        
        Collections.sort(box);
        
        int num=m;
        if(num%2==0) num=m/2;
        else num=m/2+1;
        
        for(int l=0; l<num; l++) {
            answer.add(leftBox());
            down();
            if(box.isEmpty()) break;
            answer.add(rightBox());
            down();
        }
        
        for(int i : answer) System.out.println(i);
        
        
    }
    
    public static void down() {
        for(int l=0; l<box.size(); l++) {
            Point b=box.get(l);
            
            for(int i=b.r; i>b.r-b.h; i--) {
                for(int j=b.c; j<b.c+b.w; j++) {
                    board[i][j]=0; 
                }
            }
            
            while(true) {
                if(canMove(b)) {
                    l=-1;
                    b.r++;
                }else break;
            }
        }
    }
    
    public static int rightBox() {
        int num=0;
        for(int l=0; l<box.size(); l++) {
            Point b=box.get(l);
            int c=b.c;
            int r=b.r;
            
            if(!rightMove(b)) {
                b.c=c;
                b.r=r;
                continue;
            }
            
            //복구
            for(int i=r; i>r-b.h; i--) {
                for(int j=c; j<c+b.w; j++) {
                    board[i][j]=0; 
                }
            }
            
            num=b.k;
            box.remove(b);
            break;
        }
        return num;
    }
    
    public static boolean rightMove(Point b) {
        while(b.c!=n-b.w) {
            for(int i=b.r; i>b.r-b.h; i--) {
                if(board[i][b.c+b.w]!=0) return false;
            }
            
            b.c++;
        }
        
        if(b.c==n-b.w) return true;
        return false;
    }
    
    public static int leftBox() {
        int num=0;
        for(int l=0; l<box.size(); l++) {
            Point b=box.get(l);
            int c=b.c;
            int r=b.r;
            
            if(!leftMove(b)) {
                b.c=c;
                b.r=r;
                continue;
            }
            
            //복구
            for(int i=r; i>r-b.h; i--) {
                for(int j=c; j<c+b.w; j++) {
                    board[i][j]=0; 
                }
            }
            
            num=b.k;
            box.remove(b);
            break;
        }
        return num;
    }
    
    public static boolean leftMove(Point b) {
        while(b.c!=0) {
            for(int i=b.r; i>b.r-b.h; i--) {
                if(board[i][b.c-1]!=0) return false;
            }
            
            b.c--;
        }
        
        if(b.c==0) return true;
        return false;
    }
    
    public static void putBox() {
        for(int i=0; i<m; i++) {
            Point b=box.get(i);
            
            while(true) {
                if(canMove(b)) {
                    b.r++;
                }else break;
            }
        }
    }
    
    public static boolean canMove(Point b) {
        int cnt=0;
        
        if(b.r<n-1) {
            for(int j=b.c; j<b.c+b.w; j++) {
                if(board[b.r+1][j]==0) cnt++; 
            }
        }
        
        if(cnt==b.w) return true;
        
        for(int i=b.r; i>b.r-b.h; i--) {
            for(int j=b.c; j<b.c+b.w; j++) {
                board[i][j]=b.k; 
            }
        }
        
        return false;
    }
}
