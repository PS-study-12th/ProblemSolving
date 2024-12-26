import java.io.*;
import java.util.*;

public class Main {
	static int[] compression(int N, int[] arr) {
		List<int[]> list = new ArrayList<>();
		for(int i = 0; i < N; i++) {
			list.add(new int[] {i, arr[i]});
		}
		
		Collections.sort(list, (o1, o2) -> o1[1] - o2[1]);
		Map<Integer, Integer> idxMap = new HashMap<>();
		int idx = 0;
		for(int i = 0; i < N; i++) {
			int key = list.get(i)[1];
			if(!idxMap.containsKey(key)) idxMap.put(key, idx++);
		}
		
		int[] result = new int[N];
		for(int i = 0; i < N; i++) {
			result[i] = idxMap.get(arr[i]);
		}
		
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = null;
		
		int N = Integer.parseInt(br.readLine());
		int[] arr = new int[N];
		st = new StringTokenizer(br.readLine(), " ");
		for(int i = 0; i < N; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		
		int[] result = compression(N, arr);
		for(int i = 0; i < N; i++) {
			sb.append(result[i]).append(' ');
		}
		
		System.out.println(sb);
		br.close();
	}
}
