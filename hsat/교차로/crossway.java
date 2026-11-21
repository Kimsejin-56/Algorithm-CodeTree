import java.util.*;

class Car{
    int t, idx, out, num;
    String w;
    boolean exit;

    public Car(int t, String w){
        this.t=t;
        this.w=w;
        this.exit=false;
        this.out=-1;

        if(this.w.equals("A")) idx=0;
        else if(this.w.equals("B")) idx=1;
        else if(this.w.equals("C")) idx=2;
        else if(this.w.equals("D")) idx=3;
    }

    public int getRight(){
        if(idx==0) return 3;

        return idx-1;
    }
}

public class Main {
    static int time, N, idx, exit;
    static String[] arr={"A", "B", "C", "D"};

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        List<Car> cars=new ArrayList<>();
        Map<String, Deque<Car>> cur=new HashMap<>();
        time=0;
        idx=0;
        exit=0;

        for (int i = 0; i < N; i++) {
            int t = sc.nextInt();
            String w = sc.next();
            Car car=new Car(t, w);
            
            car.num=i+1;
            cars.add(car);
        }

        for(int i=0; i<4; i++){
            cur.put(arr[i], new ArrayDeque<>());
        }

        while(exit!=cars.size()){
            if(isEmpty(cur) && idx<N){
                time=Math.max(time, cars.get(idx).t);
            }

            putCar(cars, cur);
            move(cur);
            remove(cur);
            if(deadLock(cur)) break;
            time++;
        }

        for(Car car : cars){
            System.out.println(car.out);
        }
    }

    static void move(Map<String, Deque<Car>> cur){
        for(String s : arr){
            Deque<Car> list=cur.get(s);

            if(list.isEmpty()) continue;
            
            Car car=list.getFirst();
            if(!cur.get(arr[car.getRight()]).isEmpty()) continue;

            car.exit=true;
            car.out=time;
        }
    }

    static void putCar(List<Car> cars, Map<String, Deque<Car>> cur){
        while(idx<N && cars.get(idx).t==time){
            Car car=cars.get(idx);
            cur.get(car.w).add(car);
            idx++;
        }
    }

    static void remove(Map<String, Deque<Car>> cur){
        for(int i=0; i<4; i++){
            Deque<Car> list=cur.get(arr[i]);

            if(list.isEmpty()) continue;

            if(list.getFirst().exit) {
                exit++;
                list.removeFirst();
            }
        }
    }

    static boolean deadLock(Map<String, Deque<Car>> cur){
        int cnt=0;

        for(int i=0; i<4; i++){
            if(!cur.get(arr[i]).isEmpty()) cnt++;
        }

        return cnt==4;
    }

    static boolean isEmpty(Map<String, Deque<Car>> cur){
        for(int i=0; i<4; i++){
            if(!cur.get(arr[i]).isEmpty()) return false;
        }

        return true;
    }
}