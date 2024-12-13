#include <iostream>
#include <vector>
#include <queue>
#include <set>
#include <tuple>
using namespace std;
using ll = long long;
using pii = pair<int, int>;
using vi = vector<int>;

const int BASECAMP = 1;
const int dx[4] = {-1, 0, 0, 1};
const int dy[4] = {0, -1, 1, 0};
int N, M;
vector<vi> grid;
vector<pii> baseCamps;
vector<pii> stores;
vector<tuple<int, int, int, int, bool>> persons;
set<pii> banPos;

bool checkEnd() {
    for (auto& person : persons) {
        if (!get<4>(person)) {
            return false;
        }
    }
    return true;
}

bool checkRange(int x, int y) {
    return (x >= 1 && x <= N && y >= 1 && y <= N);
}

pii findBestBC(int sx, int sy) {
    vector<vector<bool>> vst(N + 1, vector<bool>(N + 1, false));
    queue<tuple<int, int, int>> q;
    priority_queue<tuple<int, int, int>, vector<tuple<int, int, int>>, greater<>> pq;

    q.emplace(sx, sy, 0);
    vst[sx][sy] = true;

    while (!q.empty()) {
        auto [x, y, dist] = q.front();
        q.pop();

        if (grid[x][y] == BASECAMP) {
            pq.emplace(dist, x, y);
        }

        for (int i = 0; i < 4; ++i) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            
            if (checkRange(nx, ny) && !vst[nx][ny] && grid[nx][ny] != -1 &&
                !banPos.count({nx, ny})) {
                vst[nx][ny] = true;
                q.emplace(nx, ny, dist + 1);
            }
        }
    }

    if (pq.empty()) return {-1, -1};
    return {get<1>(pq.top()), get<2>(pq.top())};
}

int getNextDirection(int cx, int cy, int tx, int ty) {
    vector<vector<bool>> vst(N + 1, vector<bool>(N + 1, false));
    queue<tuple<int, int, int>> q;
    q.emplace(cx, cy, -1);
    vst[cx][cy] = true;

    while (!q.empty()) {
        auto [x, y, dir] = q.front();
        q.pop();

        if (x == tx && y == ty) return dir;

        for (int i = 0; i < 4; ++i) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            
            if (checkRange(nx, ny) && !vst[nx][ny] && !banPos.count({nx, ny})) {
                vst[nx][ny] = true;
                q.emplace(nx, ny, dir == -1 ? i : dir);
            }
        }
    }
    return -1;
}

void movePerson(int& cx, int& cy, int tx, int ty) {
    int dir = getNextDirection(cx, cy, tx, ty);
    if (dir != -1) {
        cx += dx[dir];
        cy += dy[dir];
    }
}

int main() {
    cin >> N >> M;
    grid.assign(N + 1, vector<int>(N + 1));

    for (int i = 1; i <= N; ++i) {
        for (int j = 1; j <= N; ++j) {
            cin >> grid[i][j];
            if (grid[i][j] == BASECAMP) {
                baseCamps.emplace_back(i, j);
            }
        }
    }

    for (int i = 0; i < M; ++i) {
        int x, y;
        cin >> x >> y;
        stores.emplace_back(x, y);
        persons.emplace_back(0, 0, x, y, false);
    }

    int time = 1;
    while (!checkEnd()) { // 전체 이동 할 때까지
        // 사람 이동
        for (int i = 0; i < M; ++i) {
            auto& [cx, cy, tx, ty, isArrived] = persons[i];
            if (time > i + 1 && !isArrived) {
                movePerson(cx, cy, tx, ty);
            }
        }

        // 도착 확인
        for (int i = 0; i < M; ++i) {
            auto& [cx, cy, tx, ty, isArrived] = persons[i];
            if (cx == tx && cy == ty) {
                isArrived = true;
                banPos.insert({cx, cy});
            }
        }

        // BC 할당
        for (int i = 0; i < M; ++i) {
            auto& [cx, cy, tx, ty, isArrived] = persons[i];
            if (time == i + 1) {
                auto [bx, by] = findBestBC(tx, ty);
                cx = bx;
                cy = by;
                banPos.insert({bx, by});
            }
        }
        ++time;
    }
    cout << time-1;
    return 0;
}
