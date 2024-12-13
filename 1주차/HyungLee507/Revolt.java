package codeTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class Revolt {

    static int[] dx1 = {-1, 0, 1, 0};
    static int[] dy1 = {0, 1, 0, -1};
    static int[] dx2 = {-1, 0, 1, 0, -1, -1, 1, 1};
    static int[] dy2 = {0, 1, 0, -1, 1, -1, 1, -1};
    static int N, M, P, C, D;

    static Rudolph rudolph;
    static List<Santa> santaList;

    static class Rudolph {
        int x;
        int y;
        int dir;

        public Rudolph(int x, int y, int dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        private Santa move() {
            // 여기에 산타가 움직이는 로직을 추가해야
            getNextDir();
            this.x = this.x + dx2[dir];
            this.y = this.y + dy2[dir];
            // for 문 돌아서 충돌하게된 산타를 찾고 반환
            for (Santa santa : santaList) {
                if (santa.x == this.x && santa.y == this.y) {
                    return santa;
                }
            }
            return null;
        }

        // 루돌프가 다음 이동할 방향을 구하는 메서드
        private void getNextDir() {
            // 여기서 이제
            Santa target = santaList.get(0);

//            int dist = getDist(target);
            int dist = Integer.MAX_VALUE;
            int targetR = Integer.MAX_VALUE;
            int targetC = Integer.MAX_VALUE;
            for (int i = 0; i < santaList.size(); i++) {
                Santa santa = santaList.get(i);
                if (santa.isDead) {
                    continue;
                }
                int temp = getDist(santa);
                if (temp < dist) {
                    target = santa;
                    targetR = santa.x;
                    targetC = santa.y;
                    dist = temp;
                } else if (temp == dist) {
                    if (santa.x > targetR) {
                        target = santa;
                        targetR = santa.x;
                        targetC = santa.y;
                    } else if (santa.x == targetR && santa.y > targetC) {
                        target = santa;
                        targetR = santa.x;
                        targetC = santa.y;
                    }
                }
            }
            this.setDir(target);
        }

        private int getDist(Santa target) {
            int temp1 = Math.abs(target.x - this.x);
            int temp2 = Math.abs(target.y - this.y);
            return temp1 * temp1 + temp2 * temp2;
        }

        private void setDir(Santa target) {
            int dirX;
            int dirY;
            if (target.x == this.x) {
                dirX = 0;
            } else if (target.x > this.x) {
                dirX = 1;
            } else {
                dirX = -1;
            }
            if (target.y == this.y) {
                dirY = 0;
            } else if (target.y > this.y) {
                dirY = 1;
            } else {
                dirY = -1;
            }
            for (int i = 0; i < 8; i++) {
                if (dirX == dx2[i] && dirY == dy2[i]) {
                    this.dir = i;
                    return;
                }
            }
        }
    }

    static class Santa {
        int index;
        int x;
        int y;
        int dir;

        @Override
        public String toString() {
            return "Santa{" +
                    "index=" + index +
                    ", x=" + x +
                    ", y=" + y +
                    ", dir=" + dir +
                    ", score=" + score +
                    ", stunTime=" + stunTime +
                    ", isDead=" + isDead +
                    '}';
        }

        int score;
        int stunTime;
        boolean isDead;

        public Santa(int index, int x, int y) {
            this.index = index;
            this.x = x;
            this.y = y;
            this.dir = -1;
            this.score = 0;
            this.stunTime = 0;
            this.isDead = false;
        }

        private void move() {
            setDir();
            if (dir == -1) {
                return;
            }
            this.x += dx1[dir];
            this.y += dy1[dir];

        }

        private void setDir() {
            int dist = rudolph.getDist(this);
            int temp = dist;
            for (int i = 0; i < 4; i++) {
                int nextX = this.x + dx1[i];
                int nextY = this.y + dy1[i];
                int moveDist = getDist(nextX, nextY);
                if (moveDist < dist && canMove(nextX, nextY)) {
                    this.dir = i;
                    dist = moveDist;
                }
                // 이동 불가하다면

            }
            if (temp == dist) {
                this.dir = -1;
            }
        }

        private boolean canMove(int nextX, int nextY) {
            for (Santa santa : santaList) {
                if (santa.x == nextX && santa.y == nextY) {
                    return false;
                }
            }
            return true;
        }

        private int getDist(int x, int y) {
            int temp1 = Math.abs(rudolph.x - x);
            int temp2 = Math.abs(rudolph.y - y);
            return temp1 * temp1 + temp2 * temp2;
        }

        private void setState() {
            if (x < 1 || x > N || y < 1 || y > N) {
                this.isDead = true;
            }
        }
    }


    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(bf.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        P = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        D = Integer.parseInt(st.nextToken());
        santaList = new ArrayList<>();
        st = new StringTokenizer(bf.readLine());
        rudolph = new Rudolph(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), -1);
        for (int i = 0; i < P; i++) {
            st = new StringTokenizer(bf.readLine());
            int index = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            santaList.add(new Santa(index, x, y));
        }
        Collections.sort(santaList, (s1, s2) -> Integer.compare(s1.index, s2.index));
        // 턴제 게임 시작.
        for (int time = 1; time <= M; time++) {
            // 종료 체크
            boolean stopFlag = true;
            for (Santa santa : santaList) {
                if (!santa.isDead) {
                    stopFlag = false;
                }
            }
            if (stopFlag) {
                break;
            }
            // 루돌프 이동
            Santa attackedSanta = rudolph.move();
            if (attackedSanta != null) {
                // todo: 여기에서 santa 와 방향을 인자로 받는 재귀 콜 로직.
                attackedSanta.score += C;
                // 상호작용 로직
                int moveDir = rudolph.dir;
                attackedSanta.x += (dx2[moveDir] * C);
                attackedSanta.y += (dy2[moveDir] * C);
//                if (attackedSanta.stunTime < time) {
                attackedSanta.stunTime = time + 1;
//                }
                attackedSanta.setState();
                if (!attackedSanta.isDead) {
                    // todo: 여기서 재귀 콜 타고 들어가면서 이동.-> 해당 산타의 위치에서부터 시작
                    interAction1(attackedSanta, moveDir);
                }
            }
            // 산타들 이동
            for (int j = 0; j < santaList.size(); j++) {
                Santa santa = santaList.get(j);
                if (santa.isDead || santa.stunTime >= time) {
                    continue;
                }
                santa.move();
                if (santa.x == rudolph.x && santa.y == rudolph.y) {
                    santa.score += D;
                    int moveDir = (santa.dir + 2) % 4;
                    santa.x += (dx1[moveDir] * D);
                    santa.y += (dy1[moveDir] * D);
//                    if (santa.stunTime < time) {
                    santa.stunTime = time + 1;
//                    }
                    santa.setState();
                    if (!santa.isDead) {
                        // todo: 여기서 재귀 콜 타고 들어가면서 이동.-> 해당 산타의 위치에서부터 시작
                        interAction2(santa, moveDir);
                    }
                }
            }
            // 살아있는 산타에게 점수 부여
            for (Santa santa : santaList) {
                if (!santa.isDead) {
                    santa.score++;
                }
            }
            System.out.println("현재 턴은 " + time);
            System.out.println("루돌프의 위치는 " + rudolph.x + " " + rudolph.y);
            System.out.println(santaList);

        }
        StringBuilder sb = new StringBuilder();
        for (Santa santa : santaList) {
            sb.append(santa.score).append(" ");
        }

        System.out.println(sb);
    }

    private static void interAction1(Santa santa, int moveDir) {
        for (int i = 0; i < santaList.size(); i++) {
            Santa temp = santaList.get(i);
            if (temp.index != santa.index && santa.x == temp.x && santa.y == temp.y) {
                temp.x += dx2[moveDir];
                temp.y += dy2[moveDir];
                temp.setState();
                if (!temp.isDead) {
                    interAction1(temp, moveDir);
                }
                return;
            }
        }
    }

    private static void interAction2(Santa santa, int moveDir) {
        for (int i = 0; i < santaList.size(); i++) {
            Santa temp = santaList.get(i);
            if (temp.index != santa.index && santa.x == temp.x && santa.y == temp.y) {
                temp.x += dx1[moveDir];
                temp.y += dy1[moveDir];
                temp.setState();
                if (!temp.isDead) {
                    interAction2(temp, moveDir);
                }
                return;
            }
        }
    }
}
