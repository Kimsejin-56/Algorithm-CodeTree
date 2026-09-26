import java.util.*;

class Point{
    int x, y;
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
}

class Info implements Comparable<Info>{
    int num, size;
    public Info(int num, int size) {
        this.num=num;
        this.size=size;
    }
    
    public int compareTo(Info o) {
        if(this.size==o.size)return this.num-o.num;
        return o.size-this.size;
    }
}
public class Main {
    static int n, q, idx, total;
    static int[][] board;
    static Map<Integer, List<Point>> map;
    static int[] dx= {-1, 0, 1, 0};
    static int[] dy= {0, -1, 0, 1};
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        q=sc.nextInt();
        board=new int[n][n];
        map=new HashMap<>();
        int turn=0;
        idx=1;
        
        Point p1, p2;
        while(turn<q) {
            p1=new Point(sc.nextInt()-1, sc.nextInt()-1);
            p2=new Point(sc.nextInt()-1, sc.nextInt()-1);
            List<Integer> removeList = new ArrayList<>();
            total=0;
            
            putCreature(p1, p2);
            for(int i : map.keySet()) {
                if(map.get(i).isEmpty()) {
                    removeList.add(i);
                    continue;
                }
                Point p=map.get(i).get(0);
                if(!bfs(p, i)) removeList.add(i);
            }
            
            for(int i : removeList) {
                map.remove(i);
            }
            
            moveCreature();
            
            List<Integer> list=new ArrayList<>();
            for(int i : map.keySet()) list.add(i);
            if(list.size()>=2) dfs(0, 0, list, new int[2]);
            System.out.println(total);
            idx++;
            turn++;
        }
        
        
    }
    
    public static void dfs(int start, int depth, List<Integer> list, int[] arr) {
        if(depth==2) {
            List<Point> l1=map.get(arr[0]);
            List<Point> l2=map.get(arr[1]);
            if(available(l1, arr[1])) {
                total+=l1.size()*l2.size();
            }
        }else {
            for(int i=start; i<list.size(); i++) {
                arr[depth]=list.get(i);
                dfs(i+1, depth+1, list, arr);
            }
        }
    }
    
    public static boolean available(List<Point> l1, int num) {
        for(int i=0; i<l1.size(); i++) {
            Point p=l1.get(i);
            
            for(int d=0; d<4; d++) {
                int nx=p.x+dx[d];
                int ny=p.y+dy[d];
                
                if(nx>=0 && nx<n && ny>=0 && ny<n &&  board[nx][ny]==num) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public static void moveCreature() {
        int max=-1;
        int num=0;
        List<Info> list=new ArrayList<>();
        
        for(int i : map.keySet()) {
            list.add(new Info(i, map.get(i).size()));
        }
        Collections.sort(list);
        
        int[][] copy=new int[n][n];
        
        for(int l=0; l<list.size(); l++) {
            num=list.get(l).num;
            List<Point> c=map.get(num);
            int mx=n+1;
            int my=n+1;
            
            for(Point t : c) {
                mx=Math.min(mx, t.x);
                my=Math.min(my, t.y);
            }
            boolean possible=true;
            boolean placed=false;
            for(int y=0; y<n; y++) {
                for(int x=0; x<n; x++) {
                    possible=true;
                    
                    for(Point t : c) {
                        int nx=x+(t.x-mx);
                        int ny=y+(t.y-my);
                        
                        if(nx<0 || nx>=n || ny<0 || ny>=n) {
                            possible=false;
                            break;
                        }
                        
                        if(copy[nx][ny]!=0) {
                            possible=false;
                            break;
                        }
                    }
                    
                    if(possible) {
                        for(Point t : c) {
                            int nx=x+(t.x-mx);
                            int ny=y+(t.y-my);
                            t.x=nx;
                            t.y=ny;
                            copy[nx][ny]=num;
                            placed=true;
                        }
                        break;
                    }
                }
                if(possible) break;
            }
            
            if(!placed) map.remove(num);
        }
        
        board=copy;
        
    }
    
    public static void putCreature(Point p1, Point p2) {
        List<Point> list=new ArrayList<>();
        for(int i=p1.y+1; i<=p2.y; i++) {
            for(int j=p1.x+1; j<=p2.x; j++) {
                if(board[i][j]!=0) {
                    for(int l=0; l<map.get(board[i][j]).size(); l++ ) {
                        Point p=map.get(board[i][j]).get(l);
                        if(p.x==i && p.y==j) map.get(board[i][j]).remove(p);
                    }
                }
                board[i][j]=idx;
                list.add(new Point(i, j));
            }
        }
        
        map.put(idx, list);
    }
    
    public static boolean bfs(Point s, int num) {
        Queue<Point> q=new ArrayDeque<>();
        boolean[][] visited=new boolean[n][n];
        q.offer(s);
        visited[s.x][s.y]=true;
        
        while(!q.isEmpty()) {
            Point p=q.poll();
            
            for(int i=0; i<4; i++) {
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];
                
                if(nx>=0 && nx<n && ny>=0 && ny<n && !visited[nx][ny] && board[nx][ny]==board[p.x][p.y]) {
                    visited[nx][ny]=true;
                    q.offer(new Point(nx, ny));
                }
            }
        }
        
        boolean rm=false;
        for(Point p : map.get(num)) {
            if(!visited[p.x][p.y]) {
                rm=true;
                break;
            }
        }
        
        if(rm) return false;
        return true;
    }
}
