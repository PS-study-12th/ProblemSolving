package backjoon.B_type.study3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class CODETREE_Messenger {
    static int N;
    static ChatRoom[] chatRooms;

    static class ChatRoom {
        int index;
        int authority;
        int parent;
        boolean notification;
        int cnt;
        int[] parentStrength;

        ChatRoom() {
            this.cnt = 0; // 알림받는 채팅방 수
            this.notification = true; // 초기값은 ON
            this.parentStrength = new int[N + 1]; // 부모 거리별 세기 리스트
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public void setParent(int parent) {
            this.parent = parent;
        }

        @Override
        public String toString() {
            return "ChatRoom{" +
                    "index=" + index +
                    ", authority=" + authority +
                    ", parent=" + parent +
                    ", notification=" + notification +
                    ", cnt=" + cnt +
                    ", parentStrength=" + Arrays.toString(parentStrength) +
                    '}';
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(bf.readLine());
        N = Integer.parseInt(st.nextToken());
        chatRooms = new ChatRoom[N + 1];
        int iterationCount = Integer.parseInt(st.nextToken());
        for (int i = 0; i < iterationCount; i++) {
            st = new StringTokenizer(bf.readLine());
            String cmd = st.nextToken();
            doMessenger(cmd, st);
        }
        bf.close();
    }

    private static void doMessenger(String cmd, StringTokenizer st) {
        if (cmd.equals("100")) {
            init(st);
        }
        if (cmd.equals("200")) {
            setNotification(st);
        }
        if (cmd.equals("300")) {
            changeAuthority(st);
        }
        if (cmd.equals("400")) {
            exchangeParent(st);
        }
        if (cmd.equals("500")) {
            checkChattingRoom(st);
            System.out.println(Arrays.toString(chatRooms));
        }
    }

    private static void init(StringTokenizer st) {
        for (int i = 0; i <= N; i++) {
            chatRooms[i] = new ChatRoom();
        }
        chatRooms[0].parent = -1;
        for (int i = 1; i <= N; i++) {
            int parent = Integer.parseInt(st.nextToken());
            chatRooms[i].setParent(parent);
            chatRooms[i].setIndex(i);
        }
        // 여기에 이제 해당 Node 부모까지 올라가면서 subChattingRooms 을 채워줌.
        for (int i = 1; i <= N; i++) {
            updateParentStrength(i, Integer.parseInt(st.nextToken()));
        }
    }

    private static void updateParentStrength(int index, int authority) {
        int current = index;
        int power = authority;
        chatRooms[current].parentStrength[0]++;
        while (current != 0 && power > 0) {
            int parent = chatRooms[current].parent;
            if (parent == 0 || !chatRooms[parent].notification) {
                break;
            }

            chatRooms[parent].parentStrength[power]++;
            if (chatRooms[parent].parentStrength[power] == 1) {
                chatRooms[parent].cnt++;
            }
            current = parent;
            power--;
        }
    }

    private static void setNotification(StringTokenizer st) {
        int chatRoom = Integer.parseInt(st.nextToken());
        // 알림을 끄고 cnt 값을 바꿔주는 최신화 작업 필요.
        ChatRoom currentNode = chatRooms[chatRoom];
        boolean isCurrentlyOn = currentNode.notification;
        currentNode.notification = !isCurrentlyOn;

        int sign = isCurrentlyOn ? -1 : 1;
        int current = chatRoom;

        while (current != 0) {
            int parent = chatRooms[current].parent;
            if (parent == 0 || !chatRooms[parent].notification) {
                break;
            }

            for (int i = 1; i <= currentNode.authority; i++) {
                chatRooms[parent].parentStrength[i] += sign;
                if (chatRooms[parent].parentStrength[i] == 1 && sign == 1) {
                    chatRooms[parent].cnt++;
                } else if (chatRooms[parent].parentStrength[i] == 0 && sign == -1) {
                    chatRooms[parent].cnt--;
                }
            }
            current = parent;
        }
    }

    private static void exchangeParent(StringTokenizer st) {
        int room1 = Integer.parseInt(st.nextToken());
        int room2 = Integer.parseInt(st.nextToken());

        // 둘의 위치를 바꿔주고 다시 최신화 해주는 방법.

        int tempParent = chatRooms[room1].parent;
        chatRooms[room1].parent = chatRooms[room2].parent;
        chatRooms[room2].parent = tempParent;


    }

    // 함수: 권한 세기 변경
    private static void changeAuthority(StringTokenizer st) {
        int index = Integer.parseInt(st.nextToken());
        int power = Integer.parseInt(st.nextToken());
        ChatRoom currentNode = chatRooms[index];
        int oldAuthority = currentNode.authority;
        currentNode.authority = power;

        int current = index;

        if (power > oldAuthority) {
            for (int i = oldAuthority + 1; i <= power; i++) {
                int parent = chatRooms[current].parent;
                if (parent == 0 || !chatRooms[parent].notification) {
                    break;
                }

                chatRooms[parent].parentStrength[i]++;
                if (chatRooms[parent].parentStrength[i] == 1) {
                    chatRooms[parent].cnt++;
                }
                current = parent;
            }
        } else {
            for (int i = power + 1; i <= oldAuthority; i++) {
                int parent = chatRooms[current].parent;
                if (parent == 0 || !chatRooms[parent].notification) {
                    break;
                }

                chatRooms[parent].parentStrength[i]--;
                if (chatRooms[parent].parentStrength[i] == 0) {
                    chatRooms[parent].cnt--;
                }
                current = parent;
            }
        }
    }

    private static void checkChattingRoom(StringTokenizer st) {
        int target = Integer.parseInt(st.nextToken());
        System.out.println(chatRooms[target].cnt);
    }


}
