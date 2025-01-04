import java.io.*;
import java.util.*;

public class Main {
	static class Task implements Comparable<Task> {
		int t, p, id;
	  String d;
		
		public Task(int t, int p, String d, int id) {
			this.t = t;
			this.p = p;
			this.d = d;
			this.id = id;
		}

		@Override
		public String toString() {
			return "[t=" + t + ", p=" + p + ", url=" + d + "/" + id + "]";
		}

		@Override
		public int compareTo(Task t) {
			return this.p == t.p ? this.t - t.t : this.p - t.p;
		}
	}
	
	static class Pair {
		int s, g;
		
		public Pair() {
			this.s = 0;
			this.g = 0;
		}

		public Pair(int s, int g) {
			this.s = s;
			this.g = g;
		}

		@Override
		public String toString() {
			return "[" + s + ", " + g + "]";
		}
	}
	
	static PriorityQueue<Integer> judger;
	
	static HashSet<String> waitingUrl;
	static HashMap<String, PriorityQueue<Task>> waitingQueue;
	
	static HashMap<String, Task> judgeDomain;
	static HashMap<Integer, Task> judgeJid;
	
	static HashMap<String, Pair> schedule;
	
	static Task empty;
	static int n, MAX;
	
	static String[] parser(String u) {
		return u.split("/");
	}
	
	static String buildUrl(String domain, int id) {
		return domain + "/" + id;
	}
	
	static void init(int N, String u) {
		judger = new PriorityQueue<>();
		
		waitingUrl = new HashSet<>();
		waitingQueue = new HashMap<>();
		
		judgeDomain = new HashMap<>();
		judgeJid = new HashMap<>();
		
		schedule = new HashMap<>();
		
		empty = new Task(Integer.MAX_VALUE, Integer.MAX_VALUE, "EMPTY", -1);
		n = N;
		MAX = 1_000_001;
		
		for(int i = 1; i <= N; i++) {
			judger.offer(i);
		}
		
		// 대기 큐에 초기 문제 넣기
		request(0, 1, u);
	}
	
	static void request(int t, int p, String u) {
		if(waitingUrl.contains(u)) return;
		
		String[] info = parser(u);
		String domain = info[0];
		int id = Integer.parseInt(info[1]);
		
		Task task = new Task(t, p, domain, id);
		
		// 대기 큐에 넣기
		waitingUrl.add(u);
		waitingQueue.putIfAbsent(domain, new PriorityQueue<>());
		waitingQueue.get(domain).offer(task);
		
		// schedule - 값 없을 때만 new Pair
		schedule.putIfAbsent(domain, new Pair());
	}
	
	static void judge(int t) {
		// 채점기 있나 살펴보기 - 없으면 return;
		if(judger.isEmpty()) return;
		
		// waitingQueue에서 judgeDomain.keySet()에 없는 domain의 Task에 대해서 peek() 하기
		Task task = empty;
		for(String domain : waitingQueue.keySet()) {
			if(judgeDomain.keySet().contains(domain)) continue;
			Pair pair = schedule.get(domain);
			
			// schedule 에 t < s + 3 * g 인 것 무시
			if(t < pair.s + pair.g * 3) continue;
			Task curTask = waitingQueue.get(domain).peek();
			
			// 우선순위 큐가 비어있을 수 있으므로 조심
			if(curTask == null) continue;
			if(task.p > curTask.p) {
				task = curTask;
			} else if(task.p == curTask.p && task.t > curTask.t) {
				task = curTask;
			}
		}
		
		// id == -1 이면 return; 아니면 그 문제 poll() 하기
		if(task.id == -1) return;
		
		waitingUrl.remove(buildUrl(task.d, task.id));
		waitingQueue.get(task.d).poll();
		
		// schedule update 하기 - s = t, g = MAX
		Pair pair = schedule.get(task.d);
		pair.s = t;
		pair.g = MAX;
		
		// 채점하기
		judgeDomain.put(task.d, task);
		judgeJid.put(judger.poll(), task);
	}
	
	static void terminate(int t, int j_id) {
		// judgeJid 에서 Task 가져오기 - null 이면 return
		Task task = judgeJid.get(j_id);
		if(task == null) return;
		
		// judgeJid 에서 삭제, Task의 d 활용 judgeDomain 에서 삭제
		judgeDomain.remove(task.d);
		judgeJid.remove(j_id);
		
		// judger 에 넣기
		judger.offer(j_id);
		
		// schedule update 하기 - g = t - s
		Pair pair = schedule.get(task.d);
		pair.g = t - pair.s;
	}
	
	static int count(int t) {
		int res = 0;
		for(String domain : waitingQueue.keySet()) {
			res += waitingQueue.get(domain).size();
		}
		return res;
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = null;
		
		int Q = Integer.parseInt(br.readLine());
		int cmd, N, t, p, j_id;
		String u;
		
		while(Q-- > 0) {
			st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());
			switch(cmd) {
			case 100:
				N = Integer.parseInt(st.nextToken());
				u = st.nextToken();
				init(N, u);
				break;
			case 200:
				t = Integer.parseInt(st.nextToken());
				p = Integer.parseInt(st.nextToken());
				u = st.nextToken();
				request(t, p, u);
				break;
			case 300:
				t = Integer.parseInt(st.nextToken());
				judge(t);
				break;
			case 400:
				t = Integer.parseInt(st.nextToken());
				j_id = Integer.parseInt(st.nextToken());
				terminate(t, j_id);
				break;
			case 500:
				t = Integer.parseInt(st.nextToken());
				sb.append(count(t)).append('\n');
				break;
			}
		}
		
		System.out.print(sb);
		br.close();
	}
}
