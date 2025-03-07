package backjoon.B_type.study13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CODETREE_PRESENTFACTORY {

    static Present[] beltHead, beltTail;
    static int[] beltSize;
    static Present[] presents; // HashMap 대신 배열 사용

    static class Present {
        int idx;
        Present prev;
        Present next;

        public Present(int idx) {
            this.idx = idx;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int q = Integer.parseInt(bf.readLine());
        for (int i = 0; i < q; i++) {
            String[] split = bf.readLine().split(" ");
            String command = split[0];
            if (command.equals("100")) {
                init(split);
            } else if (command.equals("200")) {
                int m_src = Integer.parseInt(split[1]);
                int m_dst = Integer.parseInt(split[2]);
                System.out.println(exchangeAll(m_src, m_dst));
            } else if (command.equals("300")) {
                int m_src = Integer.parseInt(split[1]);
                int m_dst = Integer.parseInt(split[2]);
                System.out.println(exchangeFirst(m_src, m_dst));
            } else if (command.equals("400")) {
                int m_src = Integer.parseInt(split[1]);
                int m_dst = Integer.parseInt(split[2]);
                System.out.println(exchangeHalf(m_src, m_dst));
            } else if (command.equals("500")) {
                int p_num = Integer.parseInt(split[1]);
                System.out.println(getPresentInfo(p_num));
            } else if (command.equals("600")) {
                int b_num = Integer.parseInt(split[1]);
                System.out.println(getBeltInfo(b_num));
            }
        }
        bf.close();
    }

    // 100번: 초기화. n: 벨트 개수, m: 선물 개수, 이후 m개의 숫자는 각 선물이 어느 벨트에 들어갈지
    private static void init(String[] split) {
        int n = Integer.parseInt(split[1]);
        int m = Integer.parseInt(split[2]);
        beltHead = new Present[n + 1];
        beltTail = new Present[n + 1];
        beltSize = new int[n + 1];
        presents = new Present[m + 1];
        for (int i = 1; i <= m; i++) {
            int b = Integer.parseInt(split[i + 2]);
            Present p = new Present(i);
            presents[i] = p;
            if (beltSize[b] == 0) {
                beltHead[b] = p;
                beltTail[b] = p;
            } else {
                beltTail[b].next = p;
                p.prev = beltTail[b];
                beltTail[b] = p;
            }
            beltSize[b]++;
        }
    }

    // 200번: exchangeAll(mSrc, mDst)
    private static int exchangeAll(int mSrc, int mDst) {
        if (beltSize[mSrc] == 0) {
            return beltSize[mDst];
        }

        if (beltSize[mDst] == 0) {
            beltHead[mDst] = beltHead[mSrc];
            beltTail[mDst] = beltTail[mSrc];
        } else {
            beltTail[mSrc].next = beltHead[mDst];
            beltHead[mDst].prev = beltTail[mSrc];
            beltHead[mDst] = beltHead[mSrc];
        }
        beltSize[mDst] += beltSize[mSrc];
        beltHead[mSrc] = null;
        beltTail[mSrc] = null;
        beltSize[mSrc] = 0;
        return beltSize[mDst];
    }

    // 300번: exchangeFirst(mSrc, mDst)
    private static int exchangeFirst(int mSrc, int mDst) {
        // 둘 다 비어있으면 바로 반환
        if (beltSize[mSrc] == 0 && beltSize[mDst] == 0) {
            return 0;
        }

        if (beltSize[mSrc] == 0) {
            Present p = beltHead[mDst];
            beltHead[mDst] = p.next;
            if (beltHead[mDst] != null) {
                beltHead[mDst].prev = null;
            } else {
                beltTail[mDst] = null;
            }
            beltSize[mDst]--;

            p.next = beltHead[mSrc]; // mSrc는 null
            beltHead[mSrc] = p;
            if (beltSize[mSrc] == 0) {
                beltTail[mSrc] = p;
            }
            beltSize[mSrc]++;
        } else if (beltSize[mDst] == 0) {
            Present p = beltHead[mSrc];
            beltHead[mSrc] = p.next;
            if (beltHead[mSrc] != null) {
                beltHead[mSrc].prev = null;
            } else {
                beltTail[mSrc] = null;
            }
            beltSize[mSrc]--;

            p.next = beltHead[mDst]; // null
            beltHead[mDst] = p;
            if (beltSize[mDst] == 0) {
                beltTail[mDst] = p;
            }
            beltSize[mDst]++;
        } else {
            Present pSrc = beltHead[mSrc];
            Present pDst = beltHead[mDst];

            // mSrc에서 head 제거
            beltHead[mSrc] = pSrc.next;
            if (beltHead[mSrc] != null) {
                beltHead[mSrc].prev = null;
            } else {
                beltTail[mSrc] = null;
            }

            // mDst에서 head 제거
            beltHead[mDst] = pDst.next;
            if (beltHead[mDst] != null) {
                beltHead[mDst].prev = null;
            } else {
                beltTail[mDst] = null;
            }

            // pSrc를 mDst의 head에 삽입
            pSrc.next = beltHead[mDst];
            if (beltHead[mDst] != null) {
                beltHead[mDst].prev = pSrc;
            } else {
                beltTail[mDst] = pSrc;
            }
            beltHead[mDst] = pSrc;

            // pDst를 mSrc의 head에 삽입
            pDst.next = beltHead[mSrc];
            if (beltHead[mSrc] != null) {
                beltHead[mSrc].prev = pDst;
            } else {
                beltTail[mSrc] = pDst;
            }
            beltHead[mSrc] = pDst;
        }
        return beltSize[mDst];
    }

    // 400번: exchangeHalf(mSrc, mDst)
    private static int exchangeHalf(int mSrc, int mDst) {
        int k = beltSize[mSrc] / 2;
        if (k == 0) {
            return beltSize[mDst];
        }

        Present movedHead = beltHead[mSrc];
        Present movedTail = movedHead;
        for (int i = 1; i < k; i++) {
            movedTail = movedTail.next;
        }
        Present remaining = movedTail.next;
        if (remaining != null) {
            remaining.prev = null;
        }
        beltHead[mSrc] = remaining;
        beltSize[mSrc] -= k;

        if (beltSize[mDst] == 0) {
            beltHead[mDst] = movedHead;
            beltTail[mDst] = movedTail;
            movedTail.next = null;
        } else {
            movedTail.next = beltHead[mDst];
            beltHead[mDst].prev = movedTail;
            beltHead[mDst] = movedHead;
        }
        beltSize[mDst] += k;
        return beltSize[mDst];
    }

    // 500번: getPresentInfo(pNum)
    private static int getPresentInfo(int pNum) {
        Present p = presents[pNum];
        int a = (p.prev == null ? -1 : p.prev.idx);
        int b = (p.next == null ? -1 : p.next.idx);
        return a + 2 * b;
    }

    // 600번: getBeltInfo(bNum)
    private static int getBeltInfo(int bNum) {
        if (beltSize[bNum] == 0) {
            return -3;
        }
        int a = beltHead[bNum].idx;
        int b = beltTail[bNum].idx;
        int c = beltSize[bNum];
        return a + 2 * b + 3 * c;
    }
}
