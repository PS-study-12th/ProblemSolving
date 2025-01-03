import java.io.*;
import java.util.*;

public class Main {
	static class UserFolder {
		int[] cnt;
		HashSet<String> folders;
		HashSet<Integer> files;
		
		UserFolder() {
			cnt = new int[m];
			folders = new HashSet<>();
			files = new HashSet<>();
		}

		@Override
		public String toString() {
			return "UserFolder [cnt=" + Arrays.toString(cnt) + ", folders=" + folders
					+ ", files=" + files + "]";
		}
	}
	
	static int n, m, k, q;
	static HashMap<String, UserFolder> folderMap;
	static HashMap<String, Integer> fileMap;

	static void insert(String parentKey, String key, boolean isFolder) {
		folderMap.putIfAbsent(parentKey, new UserFolder());
		UserFolder parent = folderMap.get(parentKey);
		if(isFolder) {
			UserFolder folder = folderMap.getOrDefault(key, new UserFolder());
			parent.folders.add(key);
			folderMap.put(key, folder);
		} else {
			fileMap.putIfAbsent(key, fileMap.size());
			parent.files.add(fileMap.get(key));
		}
	}
	
	static void dfs(String curKey) {
		UserFolder cur = folderMap.get(curKey);
		for(int idx : cur.files) {
			cur.cnt[idx]++;
		}
		for(String nxtKey : cur.folders) {
			dfs(nxtKey);
			UserFolder nxt = folderMap.get(nxtKey);
			for(int i = 0; i < m; i++) {
				cur.cnt[i] += nxt.cnt[i];
			}
		}
	}
	
	static String[] parser(String path) {
		String[] folders = path.split("/");
		return folders;
	}
	
	static void move(String[] pathA, String[] pathB) {
		String keyA = pathA[pathA.length - 1];
		String keyB = pathB[pathB.length - 1];
		String keyParent = pathA[pathA.length - 2];
		for(String folder : folderMap.get(keyA).folders) {
			folderMap.get(keyB).folders.add(folder);
		}
		for(int file : folderMap.get(keyA).files) {
			folderMap.get(keyB).files.add(file);
		}
		folderMap.get(keyParent).folders.remove(keyA);
	}
	
	static int[] query(String[] path) {
		String key = path[path.length - 1];
		int ans1 = 0, ans2 = 0;
		for(int c : folderMap.get(key).cnt) {
			if(c > 0) {
				ans1++;
				ans2 += c;
			}
		}
		return new int[] {ans1, ans2};
	}
	
	static void print() {
		for(String key : folderMap.keySet()) {
			System.out.println(key + " -> " + folderMap.get(key));
		}
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = null;
		
		st = new StringTokenizer(br.readLine(), " ");
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		folderMap = new HashMap<>();
		fileMap = new HashMap<>();
		for(int i = 0; i < n + m; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			String parentKey = st.nextToken();
			String key = st.nextToken();
			boolean isFolder = Integer.parseInt(st.nextToken()) == 1 ? true : false;
			insert(parentKey, key, isFolder);
		}
		
		k = Integer.parseInt(br.readLine());
		for(int i = 0; i < k; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			String[] pathA = parser(st.nextToken());
			String[] pathB = parser(st.nextToken());
			move(pathA, pathB);
		}
		
		dfs("main");
		q = Integer.parseInt(br.readLine());
		for(int i = 0; i < q; i++) {
			int[] res = query(parser(br.readLine()));
			sb.append(res[0]).append(' ').append(res[1]).append('\n');
		}
		
		System.out.print(sb);
		br.close();
	}
}
