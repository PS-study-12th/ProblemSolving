import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PriorityQueue<Integer> maxPQ = new PriorityQueue<>(Comparator.reverseOrder());
        PriorityQueue<Integer> minPQ = new PriorityQueue<>();
        StringBuilder sb = new StringBuilder();
        
        int N = Integer.parseInt(br.readLine());
        for(int i = 0; i < N; i++) {
            int x = Integer.parseInt(br.readLine());

            if(i == 0) {
                maxPQ.offer(x);
            } else if(maxPQ.size() == minPQ.size()) {
                maxPQ.offer(x);
                if(maxPQ.peek() > minPQ.peek()) {
                    minPQ.offer(maxPQ.poll());
                    maxPQ.offer(minPQ.poll());
                }
            } else {
                minPQ.offer(x);
                if(maxPQ.peek() > minPQ.peek()) {
                    minPQ.offer(maxPQ.poll());
                    maxPQ.offer(minPQ.poll());
                }
            }

            sb.append(String.valueOf(maxPQ.peek()) + "\n");
        }
        System.out.print(sb);
        br.close();
    }
}
