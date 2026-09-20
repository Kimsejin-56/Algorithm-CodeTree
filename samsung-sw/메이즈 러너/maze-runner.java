import java.util.*;

class Point implements Comparable<Point> {
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int compareTo(Point p) {
        if (this.x == p.x)
            return this.y - p.y;
        return this.x - p.x;
    }
}

public class Main {
    static int n, m, k;
    static int[][] board;
    static int[] dx = { -1, 1, 0, 0 };
    static int[] dy = { 0, 0, -1, 1 };
    static List<Point> peoples;
    static Point exit;
    static int total;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        m = sc.nextInt();
        k = sc.nextInt();
        peoples = new ArrayList<>();
        board = new int[n][n];
        int turn = 0;
        total = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = sc.nextInt();
            }
        }

        for (int i = 0; i < m; i++) {
            Point p = new Point(sc.nextInt() - 1, sc.nextInt() - 1);
            peoples.add(p);
        }

        exit = new Point(sc.nextInt() - 1, sc.nextInt() - 1);

        while (turn != k) {
            for (int i = 0; i < peoples.size(); i++) {
                Point p = peoples.get(i);
                if (move(p)) {
                    peoples.remove(p);
                    i--;
                }
            }

            if (peoples.isEmpty())
                break;
            select();
            turn++;
        }

        System.out.println(total);
        int x = exit.x + 1;
        int y = exit.y + 1;
        System.out.println(x + " " + y);
    }

    public static void select() {
        List<Point> list = new ArrayList<>();
        int len = 0;

        for (int i = 2; i <= n; i++) {
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    if (r + i > n || c + i > n)
                        continue;

                    if (isSquare(r, c, i)) {
                        list.add(new Point(r, c));
                    }
                }
            }
            if (!list.isEmpty()) {
                len = i;
                break;
            }
        }

        if (!list.isEmpty()) {
            Collections.sort(list);
            Point square = list.get(0);
            int[][] tmp = new int[len][len];
            int r = 0;

            for (int i = square.x; i < square.x + len; i++) {
                int c = 0;
                for (int j = square.y; j < square.y + len; j++) {
                    tmp[r][c++] = board[i][j];
                }
                r++;
            }

            spin(tmp, square);
        }
    }

    public static void spin(int[][] tmp, Point p) {
        int l = tmp.length;
        int[][] arr = new int[l][l];

        for (int i = 0; i < l; i++) {
            for (int j = 0; j < l; j++) {
                arr[j][l - i - 1] = tmp[i][j];
                if (arr[j][l - i - 1] > 0)
                    arr[j][l - i - 1]--;
            }
        }

        int r = 0;
        for (int i = p.x; i < p.x + l; i++) {
            int c = 0;
            for (int j = p.y; j < p.y + l; j++) {
                board[i][j] = arr[r][c++];
            }
            r++;
        }

        for (Point s : peoples) {
            if (p.x <= s.x && s.x < p.x + l && p.y <= s.y && s.y < p.y + l) {
                int x = s.x-p.x;
                int y = s.y-p.y;
                s.x = p.x + y;
                s.y = p.y + l - x - 1;
            }
        }

        int x = exit.x-p.x;
        int y = exit.y-p.y;
        exit.x = p.x + y;
        exit.y = p.y + l - x - 1;
    }

    public static boolean isSquare(int x, int y, int len) {
        int[][] tmp = new int[len][len];
        boolean passE = false;
        boolean passP = false;

        for (int i = x; i < x + len; i++) {
            for (int j = y; j < y + len; j++) {
                if (i == exit.x && j == exit.y)
                    passE = true;
                if (passP)
                    continue;
                for (Point p : peoples) {
                    if (p.x == i && p.y == j)
                        passP = true;
                }
            }
        }

        return passE && passP;
    }

    public static boolean move(Point p) {
        int disEnd = dis(p, exit);
        for (int i = 0; i < 4; i++) {
            int nx = p.x + dx[i];
            int ny = p.y + dy[i];

            if (nx >= 0 && nx < n && ny >= 0 && ny < n && board[nx][ny] == 0 && dis(nx, ny, exit) < disEnd) {
                p.x = nx;
                p.y = ny;
                total++;
                if (p.x == exit.x && p.y == exit.y)
                    return true;
                return false;
            }
        }
        return false;
    }

    public static int dis(Point s, Point e) {
        return Math.abs(s.x - e.x) + Math.abs(s.y - e.y);
    }

    public static int dis(int nx, int ny, Point e) {
        return Math.abs(nx - e.x) + Math.abs(ny - e.y);
    }
}
