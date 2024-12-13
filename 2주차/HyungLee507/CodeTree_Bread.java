package backjoon.B_type.study2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;

public class CodeTree_Bread {

    private static class Person {
        int index;
        int curX;
        int curY;
        int targetX;
        int targetY;
        boolean isArrived;

        public Person(int index, int curX, int curY, int targetX, int targetY) {
            this.index = index;
            this.curX = curX;
            this.curY = curY;
            this.targetX = targetX;
            this.targetY = targetY;
            this.isArrived = false;
        }

        void move(int dir) {
            this.curX += dx[dir];
            this.curY += dy[dir];
//            if (curX == targetX && curY == targetY) {
//                this.isArrived = true;
//                banLocations.add(new Location(targetX, targetY));
//            }
        }

        public void setStartLocation(Location location) {
            this.curX = location.x;
            this.curY = location.y;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "index=" + index +
                    ", curX=" + curX +
                    ", curY=" + curY +
                    ", targetX=" + targetX +
                    ", targetY=" + targetY +
                    ", isArrived=" + isArrived +
                    '}';
        }
    }

    private static class Location {
        int x;
        int y;

        public Location(int x, int y) {
            this.y = y;
            this.x = x;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Location location = (Location) o;
            return x == location.x && y == location.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    static final int BASECAMP = 1;
    static final int[] dx = {-1, 0, 0, 1};
    static final int[] dy = {0, -1, 1, 0};
    static int N, M;
    static int[][] map;

    static Set<Location> banLocations;

    static List<Location> baseCamps;
    static List<Location> convenienceStores;
    static List<Person> persons;

    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(bf.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        map = new int[N + 1][N + 1];
        baseCamps = new ArrayList<>();
        for (int i = 1; i <= N; i++) {
            st = new StringTokenizer(bf.readLine());
            for (int j = 1; j <= N; j++) {
                int temp = Integer.parseInt(st.nextToken());
                if (temp == BASECAMP) {
                    baseCamps.add(new Location(i, j));
                }
                map[i][j] = temp;
            }
        }
        convenienceStores = new ArrayList<>();
        persons = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(bf.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            convenienceStores.add(new Location(x, y));
            persons.add(new Person(i + 1, 0, 0, x, y));
        }
        int time = 1;
        banLocations = new HashSet<>();
        while (!isAllReached()) {
            // 사람들 돌려가면서 이동
            for (int i = 0; i < M; i++) {
                Person person = persons.get(i);
                if (time > person.index && !person.isArrived) {
                    move(person);
                }
            }
            for (int i = 0; i < M; i++) {
                Person person = persons.get(i);
                if (person.curX == person.targetX && person.curY == person.targetY) {
                    person.isArrived = true;
                    banLocations.add(new Location(person.curX, person.curY));
                }
            }
            for (int i = 0; i < M; i++) {
                Person person = persons.get(i);
                if (time == person.index) {
                    placeBaseCamp(person);
                }
            }

//            System.out.println(persons);
            time++;
        }
        System.out.println(time - 1);
    }

    private static void placeBaseCamp(Person person) {
        Location location = getNearBaseCamp(person.targetX, person.targetY);
        person.setStartLocation(location);
        banLocations.add(location);
    }

    private static Location getNearBaseCamp(int startX, int startY) {
        boolean[][] visited = new boolean[N + 1][N + 1];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY, 0});
        visited[startX][startY] = true;

        PriorityQueue<int[]> pq = new PriorityQueue<>((s1, s2) -> {
            if (s1[2] != s2[2]) {
                return Integer.compare(s1[2], s2[2]);
            }
            if (s1[0] != s2[0]) {
                return Integer.compare(s1[0], s2[0]);
            }
            return Integer.compare(s1[1], s2[1]);
        });

        while (!queue.isEmpty()) {
            int[] poll = queue.poll();
            if (map[poll[0]][poll[1]] == BASECAMP) {
                pq.add(new int[]{poll[0], poll[1], poll[2]});
                continue;
            }
            for (int i = 0; i < 4; i++) {
                int nx = poll[0] + dx[i];
                int ny = poll[1] + dy[i];
                if (nx >= 1 && nx <= N && ny >= 1 && ny <= N && map[nx][ny] != -1 && !visited[nx][ny] &&
                        !banLocations.contains(new Location(nx, ny))) {
                    visited[nx][ny] = true;
                    queue.add(new int[]{nx, ny, poll[2] + 1});
                }
            }
        }
        if (pq.isEmpty()) {
            return null;
        }
        int[] bestCamp = pq.poll();
        return new Location(bestCamp[0], bestCamp[1]);
    }


    private static void move(Person person) {
        // person 의 편의점부터 시작.
        int dir = getNextDirection(person);
        person.move(dir);
    }

    // todo : 해당 메서드 고치기
    static int getNextDirection(Person person) {
        Queue<int[]> queue = new LinkedList<>(); // BFS 상좌우하 탐색을 통해 최단 거리로 이동하는 방향 찾기
        queue.add(new int[]{person.curX, person.curY, -1});
        boolean[][] visited = new boolean[N + 1][N + 1];
        visited[person.curX][person.curY] = true;

        while (!queue.isEmpty()) {
            int[] now = queue.poll();
            if (now[0] == person.targetX && now[1] == person.targetY) {
                return now[2];
            }
            for (int i = 0; i < 4; i++) {
                int nx = now[0] + dx[i];
                int ny = now[1] + dy[i];
                if (nx >= 1 && nx <= N && ny >= 1 && ny <= N && !visited[nx][ny]
                        && !banLocations.contains(
                        new Location(nx, ny))) {
                    visited[nx][ny] = true;
                    if (now[2] == -1) {
                        queue.add(new int[]{nx, ny, i});
                    } else {
                        queue.add(new int[]{nx, ny, now[2]});
                    }
                }
            }
        }
        return -1;
    }

    private static boolean isAllReached() {
        for (Person person : persons) {
            if (!person.isArrived) {
                return false;
            }
        }
        return true;
    }
}
