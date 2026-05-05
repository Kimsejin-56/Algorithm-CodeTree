import java.util.*;

class Ship implements Comparable<Ship>{
    int id;
    int p;
    int r;
    int ready;

    public Ship(int id, int p, int r, int ready){
        this.id=id;
        this.p=p;
        this.r=r;
        this.ready=ready;
    }

    public int compareTo(Ship s){
        if(this.p == s.p) return this.id - s.id;
        else return s.p - this.p;
    }

}

public class Main {
    static int T, time;
    static HashMap<Integer, Ship> shipMap=new HashMap<>();
    static PriorityQueue<Ship> readyQ= new PriorityQueue<>();
    static PriorityQueue<Ship> waitQ= new PriorityQueue<>(new Comparator<Ship>(){
        public int compare(Ship a, Ship b){
            return a.ready - b.ready;
        }
    });

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        T=sc.nextInt();
        time=0;

        for(int t=0; t<T; t++){
            time++;
            change();
            int command=sc.nextInt();

            if(command==100){
                int n = sc.nextInt();
                for(int i=0; i<n; i++){
                    int id=sc.nextInt();
                    int p=sc.nextInt();
                    int r=sc.nextInt();
                    Ship s=new Ship(id, p, r,0);
                    readyQ.offer(s);
                    shipMap.put(id, s);
                }
            } else if(command==200){
                int id=sc.nextInt();
                int p=sc.nextInt();
                int r=sc.nextInt();
                Ship s=new Ship(id, p, r,0);
                readyQ.offer(s);
                shipMap.put(id, s);
            } else if(command==300){
                int id=sc.nextInt();
                int pw=sc.nextInt();
                Ship old=shipMap.get(id);
                Ship newShip=new Ship(id, pw, old.r, old.ready);
                shipMap.put(id, newShip);
                
                if(newShip.ready <= time) readyQ.offer(newShip);
                else waitQ.offer(newShip);

            } else if(command==400){
                attack();
            }
        }
        

    }

    static void change(){
        while(!waitQ.isEmpty() && waitQ.peek().ready <= time){
            Ship s=waitQ.poll();
            if(shipMap.get(s.id)!=s) continue;

            readyQ.offer(s);
        }
    }

    static void attack(){
        int sum=0;
        int person=0;
        List<Integer> ids=new ArrayList<>();
        
        if(readyQ.size()>5){
            for(int i=1; i<=5; i++){
                Ship tmp=readyQ.poll();
                if(shipMap.get(tmp.id)!=tmp) {
                    i--;
                    continue;
                }
                sum+=tmp.p;
                person=i;
                ids.add(tmp.id);
                tmp.ready=time+tmp.r;
                waitQ.offer(tmp);
            }
        }else{
            int len = readyQ.size();
            for(int i=1; i<=len; i++){
                Ship tmp=readyQ.poll();
                if(shipMap.get(tmp.id)!=tmp) {
                    i--;
                    len--;
                    continue;
                }
                sum+=tmp.p;
                person=i;
                ids.add(tmp.id);
                tmp.ready=time+tmp.r;
                waitQ.offer(tmp);
            }
        }
        

        System.out.print(sum+" "+person+" ");
        for(int id : ids) System.out.print(id+" ");
        System.out.println();

    }
}