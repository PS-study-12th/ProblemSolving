#include <iostream>
#include <vector>
using namespace std;
using pii = pair<int, int>;
const int MAX = 100001;

int N, M, Q;
int beltCnt[MAX];
int head[MAX];
int tail[MAX];
pii giftLink[MAX]; // first -> prev, second -> next

// 100
void cmd100() {
    cin >> N >> M;
    
    for(int id = 1; id <= M; ++id) {
        int bNum;
        cin >> bNum;
        beltCnt[bNum]++;
        
        if(head[bNum] == 0) {
            head[bNum] = tail[bNum] = id;
        } else {
            int oldTail = tail[bNum];
            giftLink[oldTail].second = id;
            giftLink[id].first = oldTail;
            tail[bNum] = id;
        }
    }
}

// 200
int cmd200(int src, int dst) {
    if(beltCnt[src] == 0) return beltCnt[dst];

    beltCnt[dst] += beltCnt[src];
    beltCnt[src] = 0;

    int src_head = head[src];
    int src_tail = tail[src];
    int dst_head = head[dst];

    head[dst] = src_head;
    if(tail[dst] == 0) tail[dst] = src_tail;
    else {
        giftLink[src_tail].second = dst_head;
        giftLink[dst_head].first = src_tail;
    }

    head[src] = tail[src] = 0;
    return beltCnt[dst];
}

// 300
int cmd300(int src, int dst) {
    int src_head = head[src];
    int dst_head = head[dst];

    if(src_head == 0 && dst_head == 0) return beltCnt[dst];

    if(src_head == 0) {
        beltCnt[src]++;
        beltCnt[dst]--;
        
        head[src] = tail[src] = dst_head;
        if(giftLink[dst_head].second != 0) head[dst] = giftLink[dst_head].second;
        else head[dst] = tail[dst] = 0;

        giftLink[head[dst]].first = 0;
        giftLink[dst_head].second = 0;
    }
    else if(dst_head == 0) {
        beltCnt[src]--;
        beltCnt[dst]++;       

        head[dst] = tail[dst] = src_head;
        if(giftLink[src_head].second != 0) head[src] = giftLink[src_head].second;
        else head[src] = tail[src] = 0;

        giftLink[head[src]].first = 0;
        giftLink[src_head].second = 0;
    }
    else {
        int src_next = giftLink[src_head].second;
        int dst_next = giftLink[dst_head].second;

        giftLink[src_head].second = dst_next;
        giftLink[dst_next].first = src_head;

        giftLink[dst_head].second = src_next;
        giftLink[src_next].first = dst_head;

        if(head[src] == tail[src]) head[src] = tail[src] = dst_head;
        else head[src] = dst_head;

        if(head[dst] == tail[dst]) head[dst] = tail[dst] = src_head;
        else head[dst] = src_head;
    }

    return beltCnt[dst];
}

// 400
int cmd400(int src, int dst) {
    if(beltCnt[src] <= 1) return beltCnt[dst];

    int movedCnt = beltCnt[src] / 2;
    beltCnt[src] -= movedCnt;
    beltCnt[dst] += movedCnt;

    int src_head = head[src];
    int dst_head = head[dst];
    int lastGift = head[src];

    while(--movedCnt) lastGift = giftLink[lastGift].second;
    int new_head = giftLink[lastGift].second;

    head[src] = new_head;
    giftLink[new_head].first = 0;

    if(dst_head == 0) {
        head[dst] = src_head;
        tail[dst] = lastGift;
        giftLink[src_head].first = 0;
        giftLink[lastGift].second = 0;
    }
    else {
        giftLink[dst_head].first = lastGift;
        giftLink[lastGift].second = dst_head;
        head[dst] = src_head;
    }

    return beltCnt[dst];
}

// 500
int cmd500(int p_num) {
    int a = giftLink[p_num].first == 0 ? -1 : giftLink[p_num].first;
    int b = giftLink[p_num].second == 0 ? -1 : giftLink[p_num].second;
    return (a + 2 * b);
}

// 600
int cmd600(int b_num) {
    int a = head[b_num] == 0 ? -1 : head[b_num];
    int b = tail[b_num] == 0 ? -1 : tail[b_num];
    int c = beltCnt[b_num];
    return (a + 2 * b + 3 * c);
}

int main() {
    cin.tie(0)->sync_with_stdio(0);
    cin >> Q;

    int cmd, p_num, b_num;
    int src = 0, dst = 0;

    while (Q--) {
        cin >> cmd;
        if(cmd == 100) cmd100();
        else if(cmd == 200) {
            cin >> src >> dst;
            cout << cmd200(src, dst) << "\n";
        }
        else if(cmd == 300) {
            cin >> src >> dst;
            cout << cmd300(src, dst) << "\n";
        }
        else if(cmd == 400) {
            cin >> src >> dst;
            cout << cmd400(src, dst) << "\n";
        }
        else if(cmd == 500) {
            cin >> p_num;
            cout << cmd500(p_num) << "\n";
        }
        else if(cmd == 600) {
            cin >> b_num;
            cout << cmd600(b_num) << "\n";
        }
    }
    return 0;
}
