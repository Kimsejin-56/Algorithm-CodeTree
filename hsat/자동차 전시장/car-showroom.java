import java.util.*;

public class Main {
    static List<List<Integer>> node;
    static int n, m, k;
    static int[] startPoints;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        m = sc.nextInt();
        k = sc.nextInt();
        node=new ArrayList<>();
        int answer=Integer.MAX_VALUE;
        
        for(int i=0; i<=n; i++){
            node.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            node.get(x).add(y);
        }

        startPoints = new int[k];
        for (int i = 0; i < k; i++) {
            startPoints[i] = sc.nextInt();
        }

        int[][] dis=new int[k][n+1];
        for(int i=0; i<startPoints.length; i++){
            dis[i]=bfs(startPoints[i]);
        }

        for(int i=1; i<=n; i++){
            int max=-1;
            boolean pass=false;

            for(int j=0; j<k; j++){
                if(dis[j][i]==-1){
                    pass=true;
                    break;
                }

                max=Math.max(max, dis[j][i]);
            }

            if(pass) continue;
            
            answer=Math.min(answer, max);
        }

        if(answer==Integer.MAX_VALUE) System.out.println(-1);
        else System.out.println(answer);
    }

    public static int[] bfs(int start){
        Queue<Integer> q=new ArrayDeque<>();
        int level=1;
        int[] dist=new int[n+1];
        
        Arrays.fill(dist, -1);
        dist[start]=0;
        q.offer(start);
        
        while(!q.isEmpty()){
            int len=q.size();
            for(int i=0; i<len; i++){
                int cur=q.poll();

                for(int next : node.get(cur)){
                    if(dist[next]!=-1) continue;

                    dist[next]=level;
                    q.offer(next);
                }
            }

            level++;
        }

        return dist;
    }
}