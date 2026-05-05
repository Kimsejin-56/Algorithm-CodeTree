import java.util.*;

class Box implements Comparable<Box>{
    int k, h, w, c, x;

    public Box(int k, int h, int w, int c, int x){
        this.k=k;
        this.h=h;
        this.w=w;
        this.c=c;
        this.x=x;
    }

    public int compareTo(Box b){
        return this.k - b.k;
    }
}

public class Main {
    static int n,m;
    static List<Box> answer=new ArrayList<>();
    static List<Box> list=new ArrayList<>();
    static boolean[][] visited; 
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        visited=new boolean[n+1][n+1];
        for(int i=0; i<m; i++){
            int k=sc.nextInt();
            int h=sc.nextInt();
            int w=sc.nextInt();
            int c=sc.nextInt();
            Box b=new Box(k, h, w, c, 1);
            list.add(b);
        }

        init();
        Collections.sort(list);

        while(!list.isEmpty()){
            deleteBoxLeft();
            down();
            deleteBoxRight();
            down();
        }

        for(Box b : answer) System.out.println(b.k);

    }
    static void down(){
        boolean moved=true;
        while(moved){
            moved=false;

            for(Box b : list){
                if(!isDown(b)) continue;
                
                for(int i=0; i<b.h; i++){
                    for(int j=b.c; j<b.c+b.w; j++){
                        visited[b.x+i][j]=false;
                    }
                }

                while(isDown(b)){
                    b.x+=1;
                    moved=true;
                }

                for(int i=0; i<b.h; i++){
                    for(int j=b.c; j<b.c+b.w; j++){
                        visited[b.x+i][j]=true;
                    }
                }

            }
        }
    }

    static void deleteBoxRight(){
        for(int t=0; t<list.size(); t++){
            Box b=list.get(t);
            int tmpC=b.c;
            while((tmpC+b.w)-1 < n){
                if(isRight(b, tmpC)){
                    tmpC+=1;
                    continue;
                } else break;
            }

            if((tmpC+b.w)-1==n){
                for(int i=0; i<b.h; i++){
                    for(int j=b.c; j<b.c+b.w; j++){
                        visited[b.x+i][j]=false;
                    }
                }

                list.remove(b);
                answer.add(b);
                return;
            }
        }
    }

    static void deleteBoxLeft(){
        for(int t=0; t<list.size(); t++){
            Box b=list.get(t);
            int tmpC=b.c;
            while(tmpC > 1){
                if(isLeft(b, tmpC)){
                    tmpC-=1;
                    continue;
                } else break;
            }

            if(tmpC==1){
                for(int i=0; i<b.h; i++){
                    for(int j=b.c; j<b.c+b.w; j++){
                        visited[b.x+i][j]=false;
                    }
                }
                list.remove(b);
                answer.add(b);
                return;
            }
        }
    }

    static void init(){
        for(Box b : list){
            while(true){
                if(isDown(b)) {
                    b.x+=1;
                    continue;
                } else break;
            }

            for(int i=0; i<b.h; i++){
                for(int j=b.c; j<b.c+b.w; j++){
                    visited[b.x+i][j]=true;
                }
            }

        }
    }

    static boolean isLeft(Box b, int bc){
        for(int i=0; i<b.h; i++){
            int ny=bc-1;
            if(ny>=1 && ny<=n && !visited[b.x+i][ny]) continue;
            else return false;
        }
        return true;
    }

    static boolean isRight(Box b, int bc){
        for(int i=0; i<b.h; i++){
            int ny=bc+b.w;
            if(ny>=1 && ny<=n && !visited[b.x+i][ny]) continue;
            else return false;
        }
        return true;
    }

    static boolean isDown(Box b){
        for(int i=b.c; i<b.c+b.w; i++){
            int nx=(b.x+b.h);
            if(nx>=1 && nx<=n && !visited[nx][i]) continue;
            else return false;
        }

        return true;
    }
}
