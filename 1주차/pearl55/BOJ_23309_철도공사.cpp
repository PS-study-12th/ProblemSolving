#include <iostream>
#include <string>
#include <vector>
using namespace std;
using pii = pair<int, int>;

int N, M;
bool avail[1000001] = {false, };
pii stts[1000001]; // i번째 인덱스의 고유역의 prev 역과 next 역 쌍을 저장

int main(void) {
    cin.tie(0);
    cout.tie(0);
    ios::sync_with_stdio(0);

    cin >> N >> M;
    vector<int> start(N);
    int pos;

    for (int i = 0; i < N; ++i) {
        cin >> start[i];
        avail[start[i]] = true;
    }

    if(N == 1) { //앞 뒤 모두 자신과 연결된 원형 리스트
        pos = start[0];
        stts[pos].first = start[0];
        stts[pos].second = start[0];
    }
    else {
        for (int i=0; i<N; ++i) {
            pos = start[i];

            if(i == 0) {
                stts[pos].first = start[N-1];
                stts[pos].second = start[i+1];
            }
            else if(i == N-1) {
                stts[pos].first = start[i-1];
                stts[pos].second = start[0];
            }
            else {
                stts[pos].first = start[i-1];
                stts[pos].second = start[i+1];
            }
        }
    }

    string info;
    for (int i = 0; i < M; ++i) {
        cin >> info;

        if (info == "BN") {
            int a, b;
            cin >> a >> b;
            int next = stts[a].second;

            cout << next << "\n";

            if(!avail[b]) {
                avail[b] = true;
                stts[b].first = a;
                stts[b].second = stts[a].second;

                stts[a].second = b;
                stts[next].first = b;
            }
        }
        else if (info == "BP") {
            int a, b;
            cin >> a >> b;

            int prev = stts[a].first;
            cout << prev << "\n";

            if(!avail[b]) {
                avail[b] = true;
                stts[b].first = stts[a].first;
                stts[b].second = a;

                stts[a].first = b;
                stts[prev].second = b;
            }
        }
        else if (info == "CN") {
            int a;
            cin >> a;
            int next = stts[a].second;
            cout << next << "\n";

            stts[a].second = stts[next].second;
            stts[stts[next].second].first = a;
            avail[next] = false;
        }
        else { //CP
            int a;
            cin >> a;
            int prev = stts[a].first;
            cout << prev << "\n";

            stts[a].first = stts[prev].first;
            stts[stts[prev].first].second = a;
            avail[prev] = false;
        }
    }
}
