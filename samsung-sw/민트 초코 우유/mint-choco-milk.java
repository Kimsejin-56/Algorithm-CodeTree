import java.util.*;

class Point{
    int x, y;

    public Point (int x, int y) {
        this.x=x;
        this.y=y;
    }
}

public class Main {
    static int n, t;
    static String[][] foods;
    static int[][] board, defend;
    static List<Point> represents=new ArrayList<>();
    static int[] dx= {-1, 1, 0, 0};
    static int[] dy= {0, 0, -1, 1};
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        t=sc.nextInt();
        board=new int[n][n];
        defend=new int[n][n];
        foods=new String[n][n];
        int turn=1;
        Map<String, Integer> map=new HashMap<>();
        String[] names= {"TCM", "TC", "TM", "CM", "M", "C", "T"};
        
        sc.nextLine();
        for(int i=0; i<n; i++) {
            String str=sc.nextLine();
            for(int j=0; j<n; j++) {
                foods[i][j]=String.valueOf(str.charAt(j));
            }
        }
  
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                board[i][j]=sc.nextInt();
            }
        }
        
        while(turn<=t) {
            morning();
            lunch();
            evening(turn);
            
            for(int i=0; i<names.length; i++) {
                map.put(names[i], 0);
            }
            
            for(int i=0; i<n; i++) {
                for(int j=0; j<n; j++) {
                    map.put(foods[i][j], map.get(foods[i][j])+board[i][j]);
                }
            }
            for(int i=0; i<names.length; i++) {
                System.out.print(map.get(names[i])+" ");
            }
            System.out.println();
            
            represents.clear();
            map.clear();
            turn++;
        }
    }
    static void evening(int turn) {
        represents.sort((a, b) -> {
            int al=foods[a.x][a.y].length();
            int bl=foods[b.x][b.y].length();
            
            if(al!=bl) return al-bl;
            
            int ab=board[a.x][a.y];
            int bb=board[b.x][b.y];
            
            if(ab!=bb) return bb-ab;
            if(a.x!=b.x) return a.x-b.x;
            return a.y-b.y;
            
        });
        
        for(Point p : represents) {
            if(defend[p.x][p.y]==turn) {
                continue;
            }
            
            int b=board[p.x][p.y];
            int x=b-1;
            int dir=b%4;
            board[p.x][p.y]=1;
            
            int nx=p.x+dx[dir];
            int ny=p.y+dy[dir];
            
            while(nx>=0 && nx<n && ny>=0 && ny<n && x>0) {
                if(foods[p.x][p.y].equals(foods[nx][ny])) {
                    nx+=dx[dir];
                    ny+=dy[dir];
                    continue; 
                }
                
                int y=board[nx][ny];
                
                if(x>y) {
                    foods[nx][ny]=foods[p.x][p.y];
                    x-=(y+1);
                    board[nx][ny]++;
                }else {
                    String str=foods[nx][ny]+foods[p.x][p.y];
                    str=containFood(str);
                    foods[nx][ny]=str;
                    board[nx][ny]+=x;
                    x=0;
                }
                
                defend[nx][ny]=turn;
                
                nx+=dx[dir];
                ny+=dy[dir];
            }
        }
    }
    
    static String containFood(String str) {
        String s="";
        if(str.contains("T")) s+="T";
        if(str.contains("C")) s+="C";
        if(str.contains("M")) s+="M";
        return s;
    }
    
    static void morning() {
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                board[i][j]++;
            }
        }
    }
    
    static void lunch() {
        boolean[][] visited=new boolean[n][n];
        
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(visited[i][j]) continue;
                int mb=Integer.MIN_VALUE;
                int mx=Integer.MAX_VALUE;
                int my=Integer.MAX_VALUE;
                Point represent=new Point(0,0);
                
                List<Point> groups=bfs(new Point(i, j), visited);
                for(Point p : groups) {
                    if(bestRepresent(mb, mx, my, p)) {
                        mb=board[p.x][p.y];
                        mx=p.x;
                        my=p.y;
                        represent=p;
                    }
                }
                
                represents.add(represent);
                board[represent.x][represent.y]+=groups.size();
                
                for(Point p : groups) {
                    board[p.x][p.y]-=1;
                }
            }
        }
    }
    
    static List<Point> bfs(Point s, boolean[][] visited) {
        Queue<Point> q=new ArrayDeque<>();
        visited[s.x][s.y]=true;
        List<Point> groups=new ArrayList<>();
        q.offer(s);
        groups.add(s);
        
        while(!q.isEmpty()) {
            Point p=q.poll();
            
            for(int i=0; i<4; i++) {
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];
                
                if(nx>=0 && nx<n && ny>=0 && ny<n && !visited[nx][ny] && 
                        foods[p.x][p.y].equals(foods[nx][ny])) {
                    visited[nx][ny]=true;
                    Point t=new Point(nx, ny);
                    groups.add(t);
                    q.offer(t);
                }
            }
        }
        
        return groups;
    }
    
    static boolean bestRepresent(int mb, int mx, int my, Point p) {
        if(mb!=board[p.x][p.y]) return mb<board[p.x][p.y];
        if(mx!=p.x) return mx>p.x;
        return my>p.y;
    }
}

