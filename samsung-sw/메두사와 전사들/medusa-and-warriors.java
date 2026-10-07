import java.util.*;

class Point{
    int x, y, dir, k;
    boolean dead, stun;

    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
}

public class Main {
    static int n, m, move, rock, attack;
    static Point monster;
    static Point home;
    static int[][] board;
    static List<Point> peoples=new ArrayList<>();
    static int[] dx= {-1, 1, 0, 0};
    static int[] dy= {0, 0, -1, 1};
    static int[] dsx= {0, 0, -1, 1};
    static int[] dsy= {-1, 1, 0, 0};
    static int[] sx= {0, 0, 1, 1};
    static int[] sy= {1, 1, 0, 0};
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        board=new int[n][n];
        monster=new Point(sc.nextInt(), sc.nextInt());
        home=new Point(sc.nextInt(), sc.nextInt());

        for(int i=0; i<m; i++) {
            peoples.add(new Point(sc.nextInt(), sc.nextInt()));
        }

        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                board[i][j]=sc.nextInt();
            }
        }
        int[][] dis=bfs(home);
        if(dis[monster.x][monster.y]==Integer.MAX_VALUE) {
            System.out.println(-1);
            return;
        }

        while(monster.x!=home.x || monster.y!=home.y) {
            move=0;
            rock=0;
            attack=0;

            moveMonster(dis);
            if(monster.x==home.x && monster.y==home.y) {
                System.out.println(0);
                return;
            }
            int[][] eye=eyeDir();
            move(eye);
            for(Point p : peoples) {
                if(p.dead) continue;
                if(p.stun) p.stun=false;
            }

            System.out.println(move+" "+rock+" "+attack);
        }
    }

    static void move(int[][] eye) {
        for(Point p : peoples) {
            if(p.dead) continue;
            if(p.stun) continue;
            for(int i=0; i<4; i++) {
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];

                if(nx>=0 && nx<n && ny>=0 && ny<n && eye[nx][ny]==0 &&
                        getDis(p.x, p.y, monster.x, monster.y)>getDis(nx, ny, monster.x, monster.y)) {
                    p.x=nx;
                    p.y=ny;
                    move++;
                    break;
                }
            }

            for(int i=0; i<4; i++) {
                int nx=p.x+dsx[i];
                int ny=p.y+dsy[i];

                if(nx>=0 && nx<n && ny>=0 && ny<n && eye[nx][ny]==0 &&
                        getDis(p.x, p.y, monster.x, monster.y)>getDis(nx, ny, monster.x, monster.y)) {
                    p.x=nx;
                    p.y=ny;
                    move++;
                    break;
                }
            }

            if(p.x==monster.x && p.y==monster.y) {
                attack++;
                p.dead=true;
            }
        }

    }

    static int[][] eyeDir() {
        int[][] next=new int[n][n];
        int max=-1;
        int dir=0;
        for(int i=0; i<4; i++) {
            int [][] tmp=makeArr(i);
            tmp=removeEye(tmp, i);
            int cnt=countPeople(tmp);
            if(max<cnt) {
                dir=i;
                next=tmp;
                max=cnt;
            }
        }

        for(Point p : peoples) {
            if(p.dead) continue;
            if(next[p.x][p.y]==1) {
                rock++;
                p.stun=true;
            }
        }

        return next;
    }

    static int countPeople(int[][] arr) {
        int cnt=0;
        for(Point p : peoples) {
            if(p.dead) continue;
            if(arr[p.x][p.y]==1) cnt++;
        }

        return cnt;
    }

    static int[][] removeEye(int[][] tmp, int dir){
        for(Point p : peoples) {
            if(p.dead) continue;
            int rx=p.x-monster.x;
            int ry=p.y-monster.y;

            int pd=rx*dx[dir]+ry*dy[dir];
            int ps=rx*sx[dir]+ry*sy[dir];

            if(pd<=0) continue;
            if(Math.abs(ps)>pd) continue;
            if(tmp[p.x][p.y]==0) continue;

            for(int d=pd+1; d<n; d++) {
                int gap=d-pd;

                int lt;
                int rt;

                if(ps==0) {
                    lt=0;
                    rt=0;
                }else if(ps<0) {
                    lt=ps-gap;
                    rt=ps;
                }else {
                    lt=ps;
                    rt=ps+gap;
                }

                for(int s=lt; s<=rt; s++) {
                    int cx=monster.x+dx[dir]*d;
                    int cy=monster.y+dy[dir]*d;

                    int nx=cx+sx[dir]*s;
                    int ny=cy+sy[dir]*s;

                    if(nx<0 || nx>=n || ny<0 || ny>=n) continue;

                    tmp[nx][ny]=0;
                }
            }
        }

        return tmp;
    }


    static int[][] makeArr(int dir){
        int [][] tmp=new int[n][n];
        for(int d=1; d<n; d++) {
            int cx=monster.x+dx[dir]*d;
            int cy=monster.y+dy[dir]*d;

            if(cx<0 || cx>=n || cy<0 || cy>=n) break;

            for(int s=-d; s<=d; s++) {
                int nx=cx+sx[dir]*s;
                int ny=cy+sy[dir]*s;

                if(nx<0 || nx>=n || ny<0 || ny>=n) continue;

                tmp[nx][ny]=1;
            }
        }

        return tmp;
    }

    static void moveMonster(int[][] dis) {
        for(int i=0; i<4; i++) {
            int nx=monster.x+dx[i];
            int ny=monster.y+dy[i];

            if(nx>=0 && nx<n && ny>=0 && ny<n &&
                    dis[monster.x][monster.y]-1==dis[nx][ny]) {
                monster.x=nx;
                monster.y=ny;
                break;
            }
        }

        for(Point p : peoples) {
            if(p.dead) continue;
            if(p.x==monster.x && p.y==monster.y) p.dead=true;
        }
    }

    static int[][] bfs(Point s) {
        Queue<Point> q=new ArrayDeque<>();
        boolean[][] visited=new boolean[n][n];
        int[][] dis=new int[n][n];
        q.offer(s);
        visited[s.x][s.y]=true;
        for(int i=0; i<n; i++) {
            Arrays.fill(dis[i], Integer.MAX_VALUE);
        }
        dis[s.x][s.y]=0;

        while(!q.isEmpty()) {
            Point p=q.poll();

            for(int i=0; i<4; i++) {
                int nx=p.x+dx[i];
                int ny=p.y+dy[i];

                if(nx>=0 && nx<n && ny>=0 && ny<n && board[nx][ny]==0 && !visited[nx][ny]) {
                    visited[nx][ny]=true;
                    q.offer(new Point(nx, ny));
                    dis[nx][ny]=dis[p.x][p.y]+1;
                }
            }
        }
        return dis;
    }

    static int getDis(int x1, int y1, int x2, int y2) {
        return Math.abs(x2-x1)+Math.abs(y2-y1);
    }
}

