package backjoon.B_type.study3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

public class BOJ_20541 {

    private static final String ROOT = "album";
    private static final String ERROR_ALBUM = "duplicated album name";
    private static final String ERROR_PHOTO = "duplicated photo name";

    static Album root = new Album(ROOT, null);

    static Album cur;

    // 여기에 전역으로 현재 Node 위치를 나타냄.

    private static class Album implements Comparable<Album> {
        Album parent;
        String title;
        TreeMap<String, Album> child;
        TreeSet<String> photos;

        public Album(String title, Album parent) {
            this.parent = parent;
            this.title = title;
            photos = new TreeSet<>();
            child = new TreeMap<>();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Album album = (Album) o;
            return Objects.equals(title, album.title);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(title);
        }

        @Override
        public int compareTo(Album o) {
            return this.title.compareTo(o.title);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(bf.readLine());
        StringTokenizer st = null;
        // 최상위 노드 생성 및 현재값 초기화.
        cur = root;
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(bf.readLine());
            String cmd = st.nextToken();
            String value = st.nextToken();
            treatCommand(cmd, value);
        }
        bf.close();
    }

    private static void treatCommand(String cmd, String value) {
        if (cmd.equals("mkalb")) {
            mkalb(value);
        }
        if (cmd.equals("rmalb")) {
            rmalb(value);
        }
        if (cmd.equals("insert")) {
            insert(value);
        }
        if (cmd.equals("delete")) {
            delete(value);
        }
        if (cmd.equals("ca")) {
            ca(value);
        }
    }

    private static void ca(String value) {
        if (value.equals("..")) {
            if (cur.parent != null) {
                cur = cur.parent;

            }
        } else if (value.equals("/")) {
            cur = root;
        } else if (cur.child.containsKey(value)) {
            cur = cur.child.get(value);
        }
        System.out.println(cur.title);
    }

    private static void mkalb(String value) {
        if (cur.child.containsKey(value)) {
            System.out.println(ERROR_ALBUM);
        } else {
            cur.child.put(value, new Album(value, cur));
        }
    }

    private static void insert(String value) {
        if (cur.photos.contains(value)) {
            System.out.println(ERROR_PHOTO);
        } else {
            cur.photos.add(value);
        }
    }

    private static void rmalb(String value) {
        if (value.equals("-1")) {
            // 이름이 사전순으로 가장 빠른 앨범 삭제
            if (!cur.child.isEmpty()) {
                Album firstAlbum = cur.child.firstEntry().getValue();
                int[] result = dfsDelete(firstAlbum);
                cur.child.remove(firstAlbum.title);
                System.out.println(result[0] + " " + result[1]);
            } else {
                System.out.println("0 0");
            }
        } else if (value.equals("0")) {
            // 현재 앨범의 모든 자식 앨범 삭제
            if (!cur.child.isEmpty()) {
                int albumCount = 0;
                int photoCount = 0;
                for (Album child : cur.child.values()) {
                    int[] result = dfsDelete(child);
                    albumCount += result[0];
                    photoCount += result[1];
                }
                cur.child.clear();
                System.out.println(albumCount + " " + photoCount);
            } else {
                System.out.println("0 0");
            }
        } else if (value.equals("1")) {
            // 이름이 사전순으로 가장 늦은 앨범 삭제
            if (!cur.child.isEmpty()) {
                Album lastAlbum = cur.child.lastEntry().getValue();
                int[] result = dfsDelete(lastAlbum);
                cur.child.remove(lastAlbum.title);
                System.out.println(result[0] + " " + result[1]);
            } else {
                System.out.println("0 0");
            }
        } else {
            // 특정 이름을 가진 앨범 삭제
            if (cur.child.containsKey(value)) {
                Album targetAlbum = cur.child.get(value);
                int[] result = dfsDelete(targetAlbum);
                cur.child.remove(value);
                System.out.println(result[0] + " " + result[1]);
            } else {
                System.out.println("0 0");
            }
        }
    }

    private static int[] dfsDelete(Album album) {
        // 앨범과 그 하위 항목을 삭제하며 앨범 수와 사진 수를 반환
        int albumCount = 1; // 현재 앨범 포함
        int photoCount = album.photos.size();

        for (Album child : album.child.values()) {
            int[] result = dfsDelete(child);
            albumCount += result[0];
            photoCount += result[1];
        }

        album.child.clear();
        album.photos.clear();
        return new int[]{albumCount, photoCount};
    }

    private static void delete(String value) {
        if (value.equals("-1")) {
            // 사전순으로 가장 빠른 사진 삭제
            if (!cur.photos.isEmpty()) {
                String firstPhoto = cur.photos.first();
                cur.photos.remove(firstPhoto);
                System.out.println(1);
            } else {
                System.out.println(0);
            }
        } else if (value.equals("0")) {
            // 모든 사진 삭제
            int photoCount = cur.photos.size();
            cur.photos.clear();
            System.out.println(photoCount);
        } else if (value.equals("1")) {
            // 사전순으로 가장 늦은 사진 삭제
            if (!cur.photos.isEmpty()) {
                String lastPhoto = cur.photos.last();
                cur.photos.remove(lastPhoto);
                System.out.println(1);
            } else {
                System.out.println(0);
            }
        } else {
            // 특정 이름의 사진 삭제
            if (cur.photos.contains(value)) {
                cur.photos.remove(value);
                System.out.println(1);
            } else {
                System.out.println(0);
            }
        }
    }
}
