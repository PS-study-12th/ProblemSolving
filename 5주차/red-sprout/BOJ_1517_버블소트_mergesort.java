import java.io.*;
import java.util.*;

public class Main {
	static int n;
	static long ans;
	static int[] arr, result;
		
	static void mergesort(int start, int end) {
		if(start < end) {
			int mid = (start + end) / 2;
			mergesort(start, mid);
			mergesort(mid + 1, end);
			merge(start, mid, end);
		}
	}
	
	static void merge(int start, int mid, int end) {
		int left = start;
		int right = mid + 1;
		int idx = start;
		
		while(left <= mid && right <= end) {
			if(arr[left] <= arr[right]) {
				result[idx++] = arr[left++];
			} else {
				result[idx++] = arr[right++];
				ans += mid - left + 1;
			}
		}
		
		if(left > mid) {
			for(int i = right; i <= end; i++) {
				result[idx++] = arr[i];
			}
		} else {
			for(int i = left; i <= mid; i++) {
				result[idx++] = arr[i];
			}
		}
		
		for(int i = start; i <= end; i++) {
			arr[i] = result[i];
		}
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		n = Integer.parseInt(br.readLine());
		arr = new int[n];
		result = new int[n];
		
		st = new StringTokenizer(br.readLine());
		for(int i = 0; i < n; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		
		mergesort(0, n - 1);
		
		System.out.println(ans);
		br.close();
	}
}
