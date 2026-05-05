import java.util.*;



public class Main {
    static int T, n, m, num, first, last;
    static int[] prev, next, pos; 
    static boolean[] exist;
    static PriorityQueue<Point> pq=new PriorityQueue<>();


    static class Point implements Comparable<Point>{
  
        int left, right, diff;

        public Point(int left, int right, int diff){
            this.left=left;
            this.right=right;
            this.diff=diff;
        }

        public int compareTo(Point p){
            if(this.diff==p.diff) return pos[this.left] - pos[p.left];
            return p.diff - this.diff;
        }
    }

    static void init(Scanner sc){
        int size=T+m+2;
        pos=new int[size];
        prev=new int[size];
        next=new int[size];;
        exist=new boolean[size];

        for(int i=1; i<=m; i++){
            pos[i]=sc.nextInt();
        }

        for(int i=1; i<=m; i++){
            prev[i]=i-1;
            next[i]=i+1;
            exist[i]=true;
        }

        prev[1]=0;
        next[m]=0;
        first=1;
        last=m;
        num=m+1;

        for(int i=1; i<m; i++){
            pq.offer(new Point(i, i+1, pos[i+1]-pos[i]));
        }
    }

    static boolean isValid(Point p){
        int l=p.left;
        int r=p.right;
        if(next[l]!=r) return false;
        if(prev[r]!=l) return false;
        if(l==0 || r==0) return false;
        if(!exist[l] || !exist[r]) return false;

        return true;
    }


    static Point getMaxGap(){
        while(!pq.isEmpty()){
            Point p=pq.peek();
            if(isValid(p)) return p;
            pq.poll();
        }

        return null;
    }

    static void addLight(){
        Point max=getMaxGap();
        if(max==null) return;

        pq.poll();
        int r=max.right;
        int l=max.left;

        int newId=num++;
        int newPos=(pos[r]+pos[l]+1)/2;
        pos[newId]=newPos;

        prev[newId]=l;
        prev[r]=newId;
        next[newId]=r;
        next[l]=newId;
        exist[newId]=true;

        pq.offer(new Point(l, newId, pos[newId]-pos[l]));
        pq.offer(new Point(newId, r, pos[r]-pos[newId]));
    } 

    static void deleteLight(int id){
        exist[id]=false;
        int r=next[id];
        int l=prev[id];

        if(l!=0) next[l]=r;
        if(r!=0) prev[r]=l;

        if(id==first) first=r;
        if(id==last) last=l;
        
        if(l!=0 && r!=0){
            pq.offer(new Point(l, r, pos[r]-pos[l]));
        }
    }

    static void printR(){
        Point max=getMaxGap();
        int fdis=2*(pos[first] -1);
        int ldis=2*(n-pos[last]);

        int maxdis=Math.max(max.diff, Math.max(fdis, ldis));
        System.out.println(maxdis);
    }

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        T=sc.nextInt();

        for(int i=1; i<=T; i++){
            int command=sc.nextInt();

            if(command==100){
                n=sc.nextInt();
                m=sc.nextInt();
                init(sc);
            }else if(command == 200){
                addLight();
            }else if(command==300){
                int id =sc.nextInt();
                deleteLight(id);
            } else if(command == 400){
                printR();
            }
        } 
    }
}