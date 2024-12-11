import java.io.*;
import java.util.*;

public class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		int N = Integer.parseInt(br.readLine());
		st = new StringTokenizer(br.readLine(), " ");
		int[] A = new int[N];
		for(int i = 0; i < N; i++) {
			A[i] = Integer.parseInt(st.nextToken());
		}
		int[] answer = new int[N];
		Arrays.fill(answer, -1);
		Deque<Integer> stack = new ArrayDeque<>();
		for(int i = 0; i < N; i++) {
			if(stack.isEmpty()) {
				stack.push(i);
			}
			while(!stack.isEmpty() && A[stack.peek()] < A[i]) {
				int idx = stack.pop();
				answer[idx] = A[i];
			}
			stack.push(i);
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < N; i++) {
			sb.append(answer[i]).append(' ');
		}
		System.out.println(sb);
		br.close();
	}
}
