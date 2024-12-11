#include <iostream>
#include <algorithm>
#include <queue>
#include <unordered_set>
#include <unordered_map>
using namespace std;
using ll = long long;
using pii = pair<int, int>;
using pipii = pair<int, pair<int, int>>;

int Q, P, N, M, K, S;

// P마리 토끼, N*M 격자에서 경주, K번의 turn
unordered_map<int, int> pidToIdx;
ll idxDist[2001];
pii rabbits[2001];
unordered_set<int> rabbitPids;
ll totalScore = 0;
ll maxScore = 0;
ll idxToScore[2001] = {0, }; // 점수 관리

int dr[4] = {-1, 1, 0, 0};
int dc[4] = {0, 0, -1, 1};

// 좌표 보정 함수
pii calPos(int nr, int nc) {
    while (nr < 1 || nr > N || nc < 1 || nc > M) {
        if (nr < 1) nr = (abs(nr) % ((2 * N) - 2)) + 2;
        if (nr > N) nr = nr % ((2 * N) - 2);
        if (nr > N) nr = (2 * N) - nr;
        if (nc < 1) nc = (abs(nc) % ((2 * M) - 2)) + 2;
        if (nc > M) nc = nc % ((2 * M) - 2);
        if (nc > M) nc = (2 * M) - nc;
    }
    return {nr, nc};
}

// 우선순위 비교 함수
struct cmpMin {
    bool operator()(const vector<int>& a, const vector<int>& b) {
        for (size_t i = 0; i < a.size(); i++) {
            if (a[i] != b[i]) return a[i] > b[i]; // 최소 힙
        }
        return false;
    }
};

priority_queue<vector<int>, vector<vector<int>>, cmpMin> pq;

bool cmpMaxPipii(const pipii& a, const pipii& b) {
    return tie(a.first, a.second.first, a.second.second) > tie(b.first, b.second.first, b.second.second);
}

// 각 턴마다 경주 진행
void raceEachTurn() {
    vector<int> nowR = pq.top(); // {jump, row+col, row, col, pid} 순서
    pq.pop();

    int nowIdx = pidToIdx[nowR[4]];
    int nowD = idxDist[nowIdx];

    // 4방향으로 이동
    vector<pipii> dirs;
    for (int d = 0; d < 4; ++d) {
        int nr = nowR[2] + (dr[d] * nowD);
        int nc = nowR[3] + (dc[d] * nowD);
        pii tmp = calPos(nr, nc);
        dirs.emplace_back(tmp.first + tmp.second, tmp);
    }

    sort(dirs.begin(), dirs.end(), cmpMaxPipii); // <row+col, <row, col>> 순서

    int nxtJump = nowR[0] + 1;
    int nxtRCSum = dirs[0].first;
    int nxtRow = dirs[0].second.first;
    int nxtCol = dirs[0].second.second;

    rabbitPids.insert(nowR[4]);
    rabbits[nowIdx] = {nxtRow, nxtCol};

    totalScore += nxtRCSum;
    idxToScore[nowIdx] -= nxtRCSum;

    pq.push({nxtJump, nxtRCSum, nxtRow, nxtCol, nowR[4]});
}

bool cmpMax(const vector<int>& a, const vector<int>& b) {
    for (size_t i = 0; i < a.size(); i++) {
        if (a[i] != b[i]) return a[i] > b[i];
    }
    return false;
}

// 마지막 토끼 선정
void findLastRabbit(int score) {
    vector<vector<int>> tmp;
    for (const auto& rPid : rabbitPids) {
        int RCSum = rabbits[pidToIdx[rPid]].first + rabbits[pidToIdx[rPid]].second;
        tmp.push_back({RCSum, rabbits[pidToIdx[rPid]].first, rabbits[pidToIdx[rPid]].second, rPid});
    }
    sort(tmp.begin(), tmp.end(), cmpMax);
    int lastPid = tmp[0][3];
    int lastRIdx = pidToIdx[lastPid];
    idxToScore[lastRIdx] += score;
}

int main() {
    cin.tie(0)->sync_with_stdio(0);
    cin >> Q;

    for (int q = 0; q < Q; ++q) {
        int num;
        cin >> num;

        if (num == 100) {
            cin >> N >> M >> P;
            for (int i = 1; i <= P; ++i) {
                int pid, d;
                cin >> pid >> d;
                pidToIdx[pid] = i;
                idxDist[i] = d;
                pq.push({0, 2, 1, 1, pid});
            }
        }
        else if (num == 200) {
            cin >> K >> S;
            rabbitPids.clear();

            for (int k = 0; k < K; ++k) {
                raceEachTurn();
            }
            findLastRabbit(S);
        }
        else if (num == 300) {
            int pid, L;
            cin >> pid >> L;

            int nowIdx = pidToIdx[pid];
            idxDist[nowIdx] *= L;
        }
        else {
            for (int i=1; i<=P; ++i){
                maxScore = max(maxScore, totalScore + idxToScore[i]);
            }
            cout << maxScore;
        }
    }
    return 0;
}
