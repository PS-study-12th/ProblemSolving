#include <bits/stdc++.h>
using namespace std;
using ll = long long;
using pii = pair<int, int>;
const int INF = 50*50*2 + 1;

// 상 우 하 좌 우선 순위대로 4방향 이동 -> 이후 대각선 이동 (이동 배열 하나로 처리)
int dx[] = {-1,0,1,0,1,1,-1,-1};
int dy[] = {0,1,0,-1,1,-1,1,-1};
int N,M,P,C,D;

vector<pii> santa;
vector<int> death(30); // -1: 죽음 / 0: 생존 / 다른 숫자 : stun 상태
vector<int> score(30); // 산타의 점수

int dist(pii a, pii b) {
    return pow(a.first-b.first,2) + pow(a.second-b.second,2);
}

bool rangeCheck(pii s) {
    return (s.first >= 1 && s.first <= N && s.second >= 1 && s.second <= N);
}

bool cmp(vector<int>& a, vector<int>& b) { // 산타 정렬
    if(a[0] == b[0]) {
        if(a[1] == b[1]) return a[2] > b[2];
        return a[1] > b[1];
    }
    return a[0] < b[0];
}

pii findSanta(pii a) { // 루돌프가 산타 target 찾기
    vector<vector<int>> sList; // (dist, r, c) 순으로 저장

    for(int i=0; i<P; i++) {
        if(death[i] == -1) continue;
        pii b = santa[i];
        sList.push_back({dist(a,b),b.first,b.second});
    }
    sort(sList.begin(),sList.end(),cmp);

    return make_pair(sList[0][1], sList[0][2]);
}

int checkSantaColli(pii pos, int me) { // 해당 좌표에 산타가 있는지 확인
    for(int i=0; i<=P; i++) {
        if(i == me) continue;

        if(santa[i].first == pos.first && santa[i].second == pos.second)
            return i;
    }
    return -1;
}

int findRudolph(pii b, pii a) {
    vector<pii> rList;
    int curDist = dist(b,a);

    for(int i=0; i < 4; i++) {
        int nx = b.first + dx[i];
        int ny = b.second + dy[i];

        if(rangeCheck({nx,ny}) && checkSantaColli({nx,ny},-1) == -1) {
            int d = dist({nx,ny},a);
            if(curDist > d) rList.push_back({d,i});
        }
    }
    if(rList.empty()) return -1;
    sort(rList.begin(),rList.end());

    return rList[0].second;
}

void rudolphMove(pii& a) {
    pii s = findSanta(a);
    vector<pii> crash;

    for(int i=0; i < 8; i++) { // 루돌프는 8방향 탐색
        int nx = a.first + dx[i];
        int ny = a.second + dy[i];

        int d = dist({nx,ny},s);
        crash.push_back({d,i});
    }

    // 정렬 후 첫번째와 충돌하도록
    sort(crash.begin(),crash.end());
    a.first += dx[crash[0].second];
    a.second += dy[crash[0].second];
    int r = checkSantaColli(a,-1); // 산타가 없다면 -1

    if(r != -1) { // 충돌 있는 경우 (루돌프 to 산타)
        score[r] += C;
        santa[r].first += C * dx[crash[0].second];
        santa[r].second += C * dy[crash[0].second];
        death[r] = 2; // 스턴 상태

        if(rangeCheck(santa[r])) {
            int target = checkSantaColli(santa[r],r);
            while(target != -1) {
                santa[target].first += dx[crash[0].second];
                santa[target].second += dy[crash[0].second];

                if(!rangeCheck(santa[target])) death[target] = -1;
                target = checkSantaColli(santa[target],target);
            }
        }
        else death[r] = -1; // 범위 벗어난 경우 death
    }
}

void santaMove(pii R) {
    for(int i=0; i < P; i++) {
        if(death[i]) continue;
        int dir = findRudolph(santa[i],R);
        if(dir == -1) continue;

        santa[i].first+=dx[dir];
        santa[i].second+=dy[dir];

        if(R.first == santa[i].first && R.second == santa[i].second) {
            score[i] += D;
            int nxtd = dir+2 > 3 ? dir-2 : dir+2;
            death[i] = 2; // 스턴 상태
            santa[i].first += D * dx[nxtd];
            santa[i].second += D * dy[nxtd];

            if(rangeCheck(santa[i])) {
                int target = checkSantaColli(santa[i],i);

                while(target != -1) {
                    santa[target].first += dx[nxtd];
                    santa[target].second += dy[nxtd];

                    if(!rangeCheck(santa[target])) death[target]=-1;
                    target = checkSantaColli(santa[target],target);
                }
            }
            else death[i] = -1;
        }
    }
}

void stunCheck() {
    for(int i=0; i < P; i++) {
        if(death[i] > 0) death[i] -= 1;
    }
}

void endScoreCheck() { // 턴 끝날 때 score 추가
    for(int i=0; i<P; i++) {
        if(death[i] != -1) score[i]++;
    }
}

bool endGameCheck() {
    for(int i=0; i<P; i++) {
        if(death[i]!=-1) return false;
    }
    return true;
}

int main() {
    cin.tie(0) ->sync_with_stdio(0);
    cin >> N >> M >> P >> C >> D;
    pii R;
    cin >> R.first >> R.second;

    santa = vector<pii>(P);
    for(int i=0; i < P; i++) {
        int pn, sr, sc;
        cin >> pn >> sr >> sc;

        santa[pn-1] = {sr, sc};
    }

    for(int i=0 ; i < M; i++) {
        if(endGameCheck()) break;

        stunCheck();
        rudolphMove(R);
        santaMove(R);
        endScoreCheck();
    }

    for (int i=0; i < P ; i++) cout << score[i] << " ";
    return 0;
}
