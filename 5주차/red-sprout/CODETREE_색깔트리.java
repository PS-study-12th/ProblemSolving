import java.io.*;
import java.util.*;

public class Main {
	static int globalT = 0;
	static int getT() { return globalT++; }
	
	static class Node {
		int id, parent, color, time, max_depth;
		int[] check;
		ArrayList<Integer> children;
		
		public Node(int m_id, int p_id, int color, int max_depth) {
			this.id = m_id;
			this.parent = p_id;
			this.color = color;
			this.time = getT();
			this.max_depth = max_depth;
			this.check = new int[6];
			this.children = new ArrayList<>();
		}

		@Override
		public String toString() {
			return "Node [id=" + id + ", parent=" + parent + ", color=" + color + ", time=" + time + ", max_depth="
					+ max_depth + ", check=" + Arrays.toString(check) + ", children=" + children + "]";
		}
	}
	
	static HashMap<Integer, Node> map;
	
	static void add(int m_id, int p_id, int color, int max_depth) {
		int id = p_id, depth = 1;
		
		while(id != -1) {
			Node node = map.get(id);
			if(++depth > node.max_depth) return;
			id = node.parent;
		}
		
		map.put(m_id, new Node(m_id, p_id, color, max_depth));
		Node parent = map.get(p_id);
		parent.children.add(m_id);
	}
	
	static void update(int m_id, int color) {
		Node node = map.get(m_id);
		node.color = color;
		node.time = getT();
	}
	
	static int get(int m_id) {
		int id = m_id, time = 0, color = 0;
		
		while(id != -1) {
			Node node = map.get(id);
			if(time < node.time) {
				time = node.time;
				color = node.color;
			}
			id = node.parent;
		}
		
		return color;
	}
	
	static void dfs(int cur, int color, int time) {
		Node node = map.get(cur);
		Arrays.fill(node.check, 0);
		node.check[color]++;
		
		for(int nxt : node.children) {
			Node next = map.get(nxt);
			if(next.time < time) {
				dfs(nxt, color, time);
			} else {
				dfs(nxt, next.color, next.time);
			}
			
			for(int i = 1; i <= 5; i++) {
				node.check[i] += next.check[i];
			}
		}
	}
	
	static int total() {
		dfs(-1, 0, 0);
		int res = 0;
		for(int key : map.keySet()) {
			if(key == -1) continue;
			int score = 0;
			Node node = map.get(key);
			for(int i = 1; i <= 5; i++) {
				if(node.check[i] > 0) score++;
			}
			res += score * score;
		}
		return res;
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = null;
		
		int q = Integer.parseInt(br.readLine());
		map = new HashMap<>();
		map.put(-1, new Node(-1, -1, 0, Integer.MAX_VALUE));
		int cmd, m_id, p_id, color, max_depth;
		while(q-- > 0) {
			st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());
			switch(cmd) {
				case 100:
					m_id = Integer.parseInt(st.nextToken());
					p_id = Integer.parseInt(st.nextToken());
					color = Integer.parseInt(st.nextToken());
					max_depth = Integer.parseInt(st.nextToken());
					add(m_id, p_id, color, max_depth);
					break;
				case 200:
					m_id = Integer.parseInt(st.nextToken());
					color = Integer.parseInt(st.nextToken());
					update(m_id, color);
					break;
				case 300:
					m_id = Integer.parseInt(st.nextToken());
					sb.append(get(m_id)).append('\n');
					break;
				case 400:
					sb.append(total()).append('\n');
					break;
			}
		}
		
		System.out.print(sb);
		br.close();
	}
}
