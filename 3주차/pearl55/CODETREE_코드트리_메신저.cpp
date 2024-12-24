#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;
const int MAX = 100001;
const int MAX_POWER = 20;

int N, Q;
int parents[MAX];
vector<vector<int>> children(MAX);
int auth[MAX];
vector<vector<int>> authDP(MAX, vector<int>(21, 0));
vector<bool> alarms(MAX, true); // 전체를 true로 초기화

void init() {
    for (int i = 1; i <= N; ++i) {
        cin >> parents[i];
    }
    for (int i = 1; i <= N; ++i) {
        cin >> auth[i];
        auth[i] = min(MAX_POWER, auth[i]);
    }

    // 알림 설정
    for (int i = 1; i <= N; ++i) {
        children[parents[i]].push_back(i);
        int power = auth[i], idx = i;
        while (power >= 0) {
            authDP[idx][power] += 1;
            power--;

            if (idx == parents[idx]) break;
            idx = parents[idx];
        }
    }
}

void update(int idx) {
    fill(authDP[idx].begin(), authDP[idx].end(), 0);
    authDP[idx][auth[idx]] += 1;

    // 자식 노드들 range 합산
    for (int child : children[idx]) {
        if (!alarms[child]) continue;
        for (int i = 0; i < MAX_POWER; ++i) {
            authDP[idx][i] += authDP[child][i+1];
        }
    }

    // 부모로 값 전파
    if (idx != parents[idx]) {
        update(parents[idx]);
    }
}

void changeAlarm(int c) {
    alarms[c] = !alarms[c];
    update(parents[c]);
}

void changePower(int c, int power) {
    auth[c] = min(MAX_POWER, power);
    update(c);
}

void changeParents(int c1, int c2) {
    int p1 = parents[c1], p2 = parents[c2];
    if (p1 == p2) return;

    // 부모 변경 및 자식 관계 업데이트
    parents[c1] = p2;
    parents[c2] = p1;

    // 자식 관계 update
    auto it1 = find(children[p1].begin(), children[p1].end(), c1);
    if (it1 != children[p1].end()) children[p1].erase(it1);
    children[p2].push_back(c1);

    auto it2 = find(children[p2].begin(), children[p2].end(), c2);
    if (it2 != children[p2].end()) children[p2].erase(it2);
    children[p1].push_back(c2);

    update(p1);
    update(p2);
}

void printRet(int cur) {
    int ret = 0;
    for (int i = 0; i < 21; ++i) {
        ret += authDP[cur][i];
    }
    cout << ret-1 << "\n";
}

int main() {
    cin.tie(0)->sync_with_stdio(0);
    cin >> N >> Q;

    int comm, c1, c2;
    for (int i = 0; i<Q; ++i) {
        cin >> comm;
        if(comm==100) init();
        else if (comm == 200) {
            cin >> c1;
            changeAlarm(c1);
        } else if (comm == 300) {
            cin >> c1 >> c2;
            changePower(c1, c2);
        } else if (comm == 400) {
            cin >> c1 >> c2;
            changeParents(c1, c2);
        } else if (comm == 500) {
            cin >> c1;
            printRet(c1);
        }
    }

    return 0;
}
