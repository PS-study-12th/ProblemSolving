#include <iostream>
#include <unordered_map>
#include <set>
#include <algorithm>
using namespace std;
using pii = pair<int, int>;
const int MAX = 100001;

int N, M, P, L, G;
set<pii> algoSet[101]; // 알고리즘 분류별 (난이도, 번호)
set<int> levelSet[101]; // 레벨별 문제 번호
unordered_map<int, pii> problemInfo; // 문제 번호 : (난이도, 그룹)

int recommend(int G, int x) {
    if (x == 1) {
        auto pos = algoSet[G].end();
        pos--;
        return pos->second;
    } else {
        auto pos = algoSet[G].begin();
        return pos->second;
    }
}

int recommend2(int x) {
    if (x == 1) {
        for (int i = 100; i > 0; --i) {
            if (levelSet[i].empty()) continue;
            auto pos = levelSet[i].end();
            pos--;
            return *pos;
        }
    } else {
        for (int i = 1; i <= 100; ++i) {
            if (levelSet[i].empty()) continue;
            auto pos = levelSet[i].begin();
            return *pos;
        }
    }
    return -1;
}

int recommend3(int x, int L) {
    if (x == 1) {
        for (int i = L; i <= 100; ++i) {
            if (levelSet[i].empty()) continue;
            auto pos = levelSet[i].begin();
            return *pos;
        }
    } else {
        for (int i = L - 1; i > 0; --i) {
            if (levelSet[i].empty()) continue;
            auto pos = levelSet[i].end();
            pos--;
            return *pos;
        }
    }
    return -1;
}

void add(int P, int L, int G) {
    algoSet[G].insert({L, P});
    levelSet[L].insert(P);
    problemInfo[P] = {L, G};
}

void solved(int P) {
    if (problemInfo.find(P) == problemInfo.end()) return;
    auto [L, G] = problemInfo[P];

    algoSet[G].erase({L, P});
    levelSet[L].erase(P);
    problemInfo.erase(P);
}

int main() {
    cin.tie(0)->sync_with_stdio(0);
    cin >> N;
    for (int i = 0; i < N; ++i) {
        cin >> P >> L >> G;
        add(P, L, G);
    }

    cin >> M;
    string comm;
    int x;
    for (int i = 0; i < M; ++i) {
        cin >> comm;
        if (comm == "recommend") {
            cin >> G >> x;
            cout << recommend(G, x) << "\n";
        } else if (comm == "recommend2") {
            cin >> x;
            cout << recommend2(x) << "\n";
        } else if (comm == "recommend3") {
            cin >> x >> L;
            cout << recommend3(x, L) << "\n";
        } else if (comm == "add") {
            cin >> P >> L >> G;
            add(P, L, G);
        } else if (comm == "solved") {
            cin >> P;
            solved(P);
        }
    }
    return 0;
}
