import java.io.*;
import java.util.*;

public class Main {
	static class Node {
		int id, auth;
		int[] cnt;
		
		public Node(int id, int auth) {
			this.id = id;
			setAuth(auth);
			cnt = new int[20];
		}
		
		public void setAuth(int auth) {
			this.auth = auth > 20 ? 20 : auth;
		}

		@Override
		public String toString() {
			return "Node [id=" + id + ", auth=" + auth + ", cnt=" + Arrays.toString(cnt) + "]";
		}
	}
	
	static int N, Q;
	static int[] parents, auth, alarm;
	static Node[] node;
	
	static void init() {
		parents[0] = -1;
		node = new Node[N + 1];
		alarm = new int[N + 1];
		Arrays.fill(alarm, 1);
		for(int i = 0; i <= N; i++) {
			node[i] = new Node(i, auth[i]);
		}
		for(int i = 0; i <= N; i++) {
			setValue(i, 1, node[i].auth);
		}
	}
	
	static void setValue(int id, int val, int power) {
		while(true) {
			if(parents[id] == -1 || power-- == 0 || alarm[id] == -1) break;
			node[parents[id]].cnt[power] += val;
			id = parents[id];
		}
	}
	
	static void setSub(int id, int val) {
		setValue(id, val, node[id].auth);
		for(int power = 1; power < 20; power++) {
			setValue(id, val * node[id].cnt[power], power);
		}
	}
	
	static void updateAlarm(int c) {
		if(alarm[c] == -1) {
			alarm[c] = 1; setSub(c, 1);
		} else {
			setSub(c, -1); alarm[c] = -1;
		}
	}
	
	static void updateAuth(int c, int power) {
		setValue(c, -1, node[c].auth);
		node[c].setAuth(power);
		setValue(c, 1, node[c].auth);
	}
	
	static void updateParents(int c1, int c2) {
		setSub(c1, -1); setSub(c2, -1);
		int p1 = parents[c1], p2 = parents[c2];
		parents[c1] = p2; parents[c2] = p1;
		setSub(c1, 1); setSub(c2, 1);
	}
	
	static int get(int c) {
		int ans = 0;
		int[] cnt = node[c].cnt;
		for(int i : cnt) {
			ans += i;
		}
		return ans;
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		st = new StringTokenizer(br.readLine(), " ");
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());
		parents = new int[N + 1];
		auth = new int[N + 1];
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());
			int query = Integer.parseInt(st.nextToken());
			int c, c1, c2, power;
			switch(query) {
			case 100:
				for(int j = 1; j <= N; j++) parents[j] = Integer.parseInt(st.nextToken());
				for(int j = 1; j <= N; j++) auth[j] = Integer.parseInt(st.nextToken());
				init();
				break;
			case 200:
				c = Integer.parseInt(st.nextToken());
				updateAlarm(c);
				break;
			case 300:
				c = Integer.parseInt(st.nextToken());
				power = Integer.parseInt(st.nextToken());
				updateAuth(c, power);
				break;
			case 400:
				c1 = Integer.parseInt(st.nextToken());
				c2 = Integer.parseInt(st.nextToken());
				updateParents(c1, c2);
				break;
			case 500:
				c = Integer.parseInt(st.nextToken());
				sb.append(get(c)).append('\n');
				break;
			}
		}
		System.out.print(sb);
		br.close();
	}
}
