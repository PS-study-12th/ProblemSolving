#include <iostream>
#include <cstring>
#include <vector>
#include <queue>
#include <tuple>

using namespace std;
typedef pair<int, int> pii;
typedef tuple<int, int, int> tiii;

int h, w;
char map[100][100];
int keys;
vector<pii> ent;
queue<tiii> q;
int v[100][100];

int dr[] = { -1, 0, 1, 0 };
int dc[] = { 0, -1, 0, 1 };

int bfs(pii s) {
    int res = 0;
    while (!q.empty()) q.pop();
    char* c = &map[s.first][s.second];
    if ((v[s.first][s.second] | keys) == v[s.first][s.second]) return 0;
    if (*c == '$') {
        res++;
        *c = '.';
        v[s.first][s.second] = keys;
        q.emplace(make_tuple(s.first, s.second, keys));
    }
    else if ('a' <= *c && *c <= 'z') {
        keys |= 1 << (*c - 'a');
        v[s.first][s.second] = keys;
        q.emplace(make_tuple(s.first, s.second, keys));
    }
    else if (*c == '.' || (keys & (1 << (*c - 'A'))) != 0) {
        v[s.first][s.second] = keys;
        q.emplace(make_tuple(s.first, s.second, v[s.first][s.second]));
    }
    else {
        return 0;
    }
    while (!q.empty()) {
        tiii cur = q.front(); q.pop();
        int nr, nc, nk;
        for (int d = 0; d < 4; d++) {
            nr = get<0>(cur) + dr[d];
            nc = get<1>(cur) + dc[d];
            nk = get<2>(cur);
            if (nr < 0 || nr >= h || nc < 0 || nc >= w || map[nr][nc] == '*') continue;

            c = &map[nr][nc];
            if ('A' <= *c && *c <= 'Z' && (nk & (1 << (*c - 'A'))) == 0) continue;
            if (*c == '$') {
                res++;
                *c = '.';
            }

            if ('a' <= *c && *c <= 'z') {
                nk |= 1 << (*c - 'a');
                keys |= nk;
            }

            if ((v[nr][nc] | nk) == v[nr][nc]) continue;

            v[nr][nc] |= nk;
            q.emplace(make_tuple(nr, nc, nk));
        }
    }
    return res;
}

int solution() {
    int res = 0;
    int tmp = 0;
    while (true) {
        tmp = 0;
        for (pii s : ent) {
            tmp += bfs(s);
        }
        res += tmp;
        if (tmp == 0) break;
    }
    return res;
}

int main() {
    ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
    int t, i, j;
    cin >> t;
    while (t-- > 0) {
        cin >> h >> w;
        memset(map, '*', sizeof(map));
        memset(v, 0, sizeof(v));
        string row;
        ent.clear();
        for (i = 0; i < h; ++i) {
            cin >> row;
            for (j = 0; j < w; ++j) {
                map[i][j] = row[j];
                if ((i == 0 || j == 0 || i == h - 1 || j == w - 1) && map[i][j] != '*') {
                    ent.emplace_back(pii(i, j));
                }
            }
        }
        cin >> row;
        keys = 1 << 27;
        if (row[0] != '0') {
            for (i = 0; i < row.length(); ++i) {
                keys |= 1 << (row[i] - 'a');
            }
        }
        cout << solution() << '\n';
    }
    return 0;
}
