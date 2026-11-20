import java.util.*;

public class Main {
    static int count;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        List<List<Integer>> list=new ArrayList<>();
        boolean[] visited=new boolean[n+1];
        count=0;

        for (int i = 0; i <= n; i++) {
            list.add(new ArrayList());
        }

        for (int i = 0; i < m; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();

            list.get(a).add(b);
            list.get(b).add(a);
        }

        dfs(1, visited, list);
        System.out.println(count-1);
    }

    public static void dfs(int num, boolean[] visited,  List<List<Integer>> list){
        if(visited[num]) return;

        count++;
        visited[num]=true;
        
        for(int i : list.get(num)){
            dfs(i, visited, list);
        }
    }
}