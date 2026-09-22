import java.util.*;

class Point implements Comparable<Point>{
    int x, y, num;
    String type;
    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int compareTo(Point p){
        if(this.num==p.num){
            if(this.x==p.x) return this.y-p.y;
            return this.x-p.x;
        }
        return p.num-this.num;
    }
}

public class Main {
    static int n, t;
    static int[][] board;
    static String[][] types;
    static int[] dx={-1, 1, 0, 0};
    static int[] dy={0, 0, -1, 1};
    static List<Point> represent;
    static String[] names={"TCM", "TC", "TM", "CM", "M", "C", "T"};
    static boolean[][] defend;

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        t=sc.nextInt();
        board=new int[n][n];
        types=new String[n][n];
        int turn=0;
        represent=new ArrayList<>();

        sc.nextLine();
        for(int i=0; i<n; i++){
            String str=sc.nextLine();
            for(int j=0; j<n; j++){
                types[i][j]=String.valueOf(str.charAt(j));
            }
        }

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                board[i][j]=sc.nextInt();
            }
        }

        while(turn<t){
            defend=new boolean[n][n];
            morning();
            lunch();
            evening();
            represent.clear();
            clac();
            turn++;
        }
    }

    public static void morning(){
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                board[i][j]++;
            }
        }
    }

    public static void lunch(){
        boolean[][] visited=new boolean[n][n];
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                if(visited[i][j]) continue;
                Point p=new Point(i, j);
                p.type=types[i][j];
                represent.add(bfs(p, visited));
            }
        }
    }

    public static void evening(){
        List<Point> small=new ArrayList<>();
        List<Point> midium=new ArrayList<>();
        List<Point> large=new ArrayList<>();
        List<Point> list=new ArrayList<>();

        for(Point p : represent){
            if(p.type.length()==1) small.add(p);
            else if(p.type.length()==2) midium.add(p);
            else large.add(p);
        }

        Collections.sort(small);
        Collections.sort(midium);
        Collections.sort(large);
        list.addAll(small);
        list.addAll(midium);
        list.addAll(large);

        for(Point p : list) {
            if(defend[p.x][p.y]) continue;
            int x = board[p.x][p.y] - 1;
            int dir=board[p.x][p.y] % 4;
            board[p.x][p.y] = 1;
            while (x != 0) {
                int nx = p.x + dx[dir];
                int ny = p.y + dy[dir];

                if (nx >= 0 && nx < n && ny >= 0 && ny < n) {
                    if(types[nx][ny].equals(p.type)) {
                        p.x=nx;
                        p.y=ny;
                        continue;
                    }

                    int y=board[nx][ny];

                    if(x>y){
                        x-=(y+1);
                        types[nx][ny]=p.type;
                        board[nx][ny]+=1;
                    }else{
                        String type=match(types[nx][ny]+p.type);
                        types[nx][ny]=type;
                        board[nx][ny]+=x;
                        x=0;
                    }
                    defend[nx][ny]=true;
                    p.x=nx;
                    p.y=ny;
                }else break;
            }
        }
    }

    public static void clac(){
        for(int l=0; l<names.length; l++){
            String name=names[l];
            int sum=0;
            for(int i=0; i<n; i++){
                for(int j=0; j<n; j++){
                    if(name.equals(types[i][j])){
                        sum+=board[i][j];
                    }
                }
            }
            System.out.print(sum+" ");
        }
        System.out.println();
    }

    public static Point bfs(Point s, boolean[][] visited){
        Queue<Point> q=new ArrayDeque<>();
        List<Point> list=new ArrayList<>();
        int cnt=1;
        q.offer(s);
        visited[s.x][s.y]=true;
        s.num=board[s.x][s.y]--;
        list.add(s);

        while(!q.isEmpty()){
            Point p=q.poll();
            for(int i=0; i<4; i++){
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];

                if(nx>=0 && nx<n && ny>=0 && ny<n && !visited[nx][ny] && types[nx][ny].equals(p.type)){
                    int num=board[nx][ny]--;
                    Point tmp=new Point(nx, ny);

                    tmp.type=p.type;
                    tmp.num=num;
                    visited[nx][ny]=true;
                    q.offer(tmp);
                    cnt++;
                    list.add(tmp);
                }
            }
        }

        Collections.sort(list);
        Point p=list.get(0);
        board[p.x][p.y]+=cnt;
        p.num=board[p.x][p.y];
        return p;
    }

    public static String match(String type){
        String str="";
        if(type.contains("T")) str+="T";
        if(type.contains("C")) str+="C";
        if(type.contains("M")) str+="M";
        return str;
    }
}