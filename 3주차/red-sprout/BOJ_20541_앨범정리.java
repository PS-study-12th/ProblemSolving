import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class Main {
	static class Album {
		String s;
		Album parent;
		int cntAlbum, cntPhoto;
		TreeMap<String, Album> album;
		TreeSet<String> photo;
		
		public Album(String s) {
			this.s = s;
			this.parent = null;
			this.cntAlbum = 0;
			this.cntPhoto = 0;
			this.album = new TreeMap<>();
			this.photo = new TreeSet<>();
		}

		@Override
		public String toString() {
			return "Album [s=" + s + ", cntAlbum=" + cntAlbum + ", cntPhoto=" + cntPhoto + "]";
		}
	}
	
	static Album root, now;
	
	static void init() {
		root = new Album("album");
		now = root;
	}
	
	static void updateStatus(Album album, int cntA, int cntP) {
		while(album.parent != null) {
			Album parent = album.parent;
			parent.cntAlbum += cntA;
			parent.cntPhoto += cntP;
			album = parent;
		}
	}
	
	static String mkalb(String query) {
		TreeMap<String, Album> sub = now.album;
		if(sub.containsKey(query)) return "duplicated album name\n";
		Album album = new Album(query);
		sub.put(query, album);
		album.parent = now;
		updateStatus(album, 1, 0);
		return "";
	}
	
	static int[] rmalb(String query) {
		Album album = null;
		Entry<String, Album> entry = null;
		int cntA = 0, cntP = 0;
		TreeMap<String, Album> sub = now.album;
		if(sub.isEmpty()) return new int[] {0, 0};
		switch(query) {
		case "-1":
			entry = sub.pollFirstEntry();
			album = entry.getValue();
			cntA = album.cntAlbum + 1;
			cntP = album.cntPhoto;
			break;
		case "0":
			album = now;
			cntA = album.cntAlbum;
			cntP = album.cntPhoto - now.photo.size();
			sub.clear();
			album.cntAlbum = 0;
			album.cntPhoto = now.photo.size();
			break;
		case "1":
			entry = sub.pollLastEntry();
			album = entry.getValue();
			cntA = album.cntAlbum + 1;
			cntP = album.cntPhoto;
			break;
		default:
			if((album = sub.remove(query)) != null) {
				cntA = album.cntAlbum + 1;
				cntP = album.cntPhoto;
			}
			break;
		}
		if(album != null) updateStatus(album, -cntA, -cntP);
		return new int[] {cntA, cntP};
	}
	
	static String insert(String query) {
		TreeSet<String> sub = now.photo;
		if(sub.contains(query)) return "duplicated photo name\n";
		sub.add(query);
		now.cntPhoto++;
		updateStatus(now, 0, 1);
		return "";
	}
	
	static int delete(String query) {
		int cntP = 0;
		TreeSet<String> sub = now.photo;
		if(sub.isEmpty()) return 0;
		switch(query) {
		case "-1":
			cntP = 1;
			now.cntPhoto--;
			sub.pollFirst();
			break;
		case "0":
			cntP = sub.size();
			now.cntPhoto -= cntP;
			sub.clear();
			break;
		case "1":
			cntP = 1;
			now.cntPhoto--;
			sub.pollLast();
			break;
		default:
			if(sub.remove(query)) {
				cntP = 1;
				now.cntPhoto--;
			}
			break;
		}
		updateStatus(now, 0, -cntP);
		return cntP;
	}
	
	static String ca(String query) {
		switch(query) {
		case "..":
			if(now.parent != null) now = now.parent;
			break;
		case "/":
			now = root;
			break;
		default:
			now = now.album.getOrDefault(query, now);
			break;
		}
		return now.s + '\n';
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		int N = Integer.parseInt(br.readLine());
		init();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			String command = st.nextToken();
			String query = st.nextToken();
			switch(command) {
			case "mkalb" : 
				sb.append(mkalb(query)); 
				break;
			case "rmalb" : 
				int[] result = rmalb(query);
				sb.append(result[0]).append(' ').append(result[1]).append('\n'); 
				break;
			case "insert" : 
				sb.append(insert(query)); 
				break;
			case "delete" : 
				sb.append(delete(query)).append('\n'); 
				break;
			case "ca" : 
				sb.append(ca(query)); 
				break;
			}
		}
		System.out.print(sb);
		br.close();
	}
}
