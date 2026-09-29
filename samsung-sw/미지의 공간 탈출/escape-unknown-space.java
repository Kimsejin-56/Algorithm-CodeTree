import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

class Point {
    int x, y, time, l;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, int l, int time) {
        this.x = x;
        this.y = y;
        this.time = time;
        this.l = l;
    }
}

class Area {
    int x, y, dir, v;

    Area(int x, int y, int dir, int v) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.v = v;
    }
}

public class Main {
    static int n, m, f, wallX, wallY;
    static int[][] board;
    static int[] dx = { 0, 0, 1, -1 };
    static int[] dy = { 1, -1, 0, 0 };
    static int[][][] wall;
    static List<Area> timeArea;
    static Point start;
    static int[][] dangerTime;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        m = sc.nextInt();
        f = sc.nextInt();
        board = new int[n][n];
        wallX = -1;
        wallY = -1;
        wall = new int[5][m][m];
        timeArea = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = sc.nextInt();
                if (board[i][j] == 3) {
                    if (wallX == -1) {
                        wallX = i;
                        wallY = j;
                    }
                }
            }
        }

        for (int l = 0; l < 5; l++) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < m; j++) {
                    wall[l][i][j] = sc.nextInt();
                    if (wall[l][i][j] == 2) {
                        start = new Point(i, j, l, 0);
                    }
                }
            }
        }

        for (int i = 0; i < f; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            int dir = sc.nextInt();
            int v = sc.nextInt();

            timeArea.add(new Area(x, y, dir, v));
        }

        makeDangerTime();
        Point bs = bfsWall();
        if (bs == null) {
            System.out.println(-1);
            return;
        }

        if (dangerTime[bs.x][bs.y] <= bs.time) {
            System.out.println(-1);
            return;
        }
        System.out.println(bfsBoard(bs));
    }

    public static void makeDangerTime() {
        dangerTime = new int[n][n];

        for (int i = 0; i < n; i++) {
            Arrays.fill(dangerTime[i], Integer.MAX_VALUE);
        }

        for (Area area : timeArea) {
            dangerTime[area.x][area.y] = 0;

            int nx = area.x;
            int ny = area.y;
            int time = 0;

            while (true) {
                nx += dx[area.dir];
                ny += dy[area.dir];

                time += area.v;

                if (nx >= 0 && nx < n && ny >= 0 && ny < n && board[nx][ny] == 0) {
                    dangerTime[nx][ny] = Math.min(dangerTime[nx][ny], time);
                } else
                    break;
            }
        }
    }

    public static int bfsBoard(Point s) {
        Queue<Point> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];
        q.offer(s);
        visited[s.x][s.y] = true;

        while (!q.isEmpty()) {
            Point p = q.poll();

            if (board[p.x][p.y] == 4) return p.time;

            for (int i = 0; i < 4; i++) {
                int nx = p.x + dx[i];
                int ny = p.y + dy[i];
                int nt = p.time + 1;

                if (nx >= 0 && nx < n && ny >= 0 && ny < n && !visited[nx][ny]) {
                    if(board[nx][ny] == 0 || board[nx][ny] == 4) {
                        if(dangerTime[nx][ny] > nt) {
                            visited[nx][ny] = true;
                            q.offer(new Point(nx, ny, 5, nt));
                        }
                    }
                }
            }
        }
        
        return -1;
    }

    public static Point bfsWall() {
        Queue<Point> q = new ArrayDeque<>();
        boolean[][][] visited = new boolean[5][m][m];
        q.offer(start);
        visited[start.l][start.x][start.y] = true;

        while (!q.isEmpty()) {
            Point p = q.poll();

            for (int i = 0; i < 4; i++) {
                int nx = p.x + dx[i];
                int ny = p.y + dy[i];
                int nl = p.l;

                if (nx >= 0 && nx < m && ny >= 0 && ny < m) {
                    if(wall[nl][nx][ny] == 0 && !visited[nl][nx][ny]) {
                        visited[nl][nx][ny] = true;
                        q.offer(new Point(nx, ny, nl, p.time + 1));
                    }
                    continue;
                }

                if (nl != 4 && nx >= m) {
                    Point b = wallToBoard(nl, ny);
                    if (b != null) {
                        if (b.x >= 0 && b.x < n && b.y >= 0 && b.y < n && board[b.x][b.y] == 0) {
                            b.time = p.time + 1;
                            return b;
                        }
                    }
                    continue;
                }

                int[] next = wallDir(nx, ny, nl);

                nx = next[0];
                ny = next[1];
                nl = next[2];

                if (wall[nl][nx][ny] == 0 && !visited[nl][nx][ny]) {
                    visited[nl][nx][ny] = true;
                    q.offer(new Point(nx, ny, nl, p.time + 1));
                }
            }
        }
        return null;
    }

    public static Point wallToBoard(int l, int y) {
        int x;
        int ny;

        if (l == 0) {
            x = wallX + (m - y - 1);
            ny = wallY + m;
        } else if (l == 1) {
            x = wallX + y;
            ny = wallY - 1;
        } else if (l == 2) {
            x = wallX + m;
            ny = wallY + y;
        } else if (l == 3) {
            x = wallX - 1;
            ny = wallY + (m - y - 1);
        } else
            return null;

        return new Point(x, ny);
    }

    public static int[] wallDir(int nx, int ny, int l) {
        int nl = l;
        int[] xy = new int[3];
        if (nl == 4) {
            if (nx < 0) {
                nl = 3;
                nx = 0;
                ny = m - ny - 1;
            } else if (nx >= m) {
                nl = 2;
                nx = 0;
            } else if (ny < 0) {
                nl = 1;
                ny = nx;
                nx = 0;
            } else if (ny >= m) {
                nl = 0;
                ny = m - nx - 1;
                nx = 0;
            }
        } else if (nl == 3) {
            if (nx < 0) {
                nl = 4;
                ny = m - ny - 1;
                nx = 0;
            } else if (ny < 0) {
                nl = 0;
                ny = m - 1;
            } else if (ny >= m) {
                nl = 1;
                ny = 0;
            }
        } else if (nl == 2) {
            if (nx < 0) {
                nl = 4;
                nx = m - 1;
            } else if (ny < 0) {
                nl = 1;
                ny = m - 1;
            } else if (ny >= m) {
                nl = 0;
                ny = 0;
            }
        } else if (nl == 1) {
            if (nx < 0) {
                nl = 4;
                nx = ny;
                ny = 0;
            } else if (ny < 0) {
                nl = 3;
                ny = m - 1;
            } else if (ny >= m) {
                nl = 2;
                ny = 0;
            }
        } else if (nl == 0) {
            if (nx < 0) {
                nl = 4;
                nx = m - ny - 1;
                ny = m - 1;
            } else if (ny < 0) {
                nl = 2;
                ny = m - 1;
            } else if (ny >= m) {
                nl = 3;
                ny = 0;
            }
        }
        xy[0] = nx;
        xy[1] = ny;
        xy[2] = nl;
        return xy;
    }
}