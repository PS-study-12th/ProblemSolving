#include <iostream>
#include <queue>
#include <tuple>
using namespace std;

enum side {
    EAST, WEST, SOUTH, NORTH, TOP, BOT
};

int N, M, F, T, wallR = -1, wallC = -1, ans = -1;
tuple<int, int, int> startpos;
int space[20][20];
int timewall[5][10][10];
int abnomality[10][4];
int visit[20][20];
int visitwall[5][10][10];
int dx[] = {0, 0, 1, -1};
int dy[] = {1, -1, 0, 0};
queue<tuple<int, int, int>> q;
bool isFound;

bool validRange(int x, int y, int side = BOT) {
    if(side == BOT) return x >= 0 && x < N && y >= 0 && y < N;
    else return x >= 0 && x < M && y >= 0 && y < M;
}

void spreadAbnomality() {
    for(int i = 0; i < F; ++i) {
        if(T % abnomality[i][3]) continue;

        int dir = abnomality[i][2];
        int nx = abnomality[i][0] + dx[dir];
        int ny = abnomality[i][1] + dy[dir];
        
        if(!validRange(nx, ny) || space[nx][ny] != 0) continue;
        visit[nx][ny] = 1;
        abnomality[i][0] = nx;
        abnomality[i][1] = ny;
    }
}

tuple<int, int, int> getNxtPos(tuple<int, int, int>& cur, int dir) {
    int nx = get<1>(cur) + dx[dir];
    int ny = get<2>(cur) + dy[dir];
    int curs = get<0>(cur);
    tuple<int, int, int> ret = {curs, nx, ny};

    if(validRange(nx, ny, curs) && curs == BOT && space[nx][ny] == 3) {
        if(dir == EAST) ret = {WEST, M - 1, nx - wallR};
        else if(dir == WEST) ret = {EAST, M - 1, M - 1 - (nx - wallR)};
        else if(dir == SOUTH) ret = {NORTH, M - 1, M - 1 - (ny - wallC)};
        else ret = {SOUTH, M - 1, ny - wallC};
    }
    else if(!validRange(nx, ny, curs)) {
        if(curs == BOT) ret = {-1, -1, -1};
        else if(curs == EAST) {
            if(dir == EAST) ret = {NORTH, nx, 0};
            else if(dir == WEST) ret = {SOUTH, nx, M - 1};
            else if(dir == SOUTH) ret = {BOT, wallR + M - ny - 1, wallC + M};
            else ret = {TOP, M - 1 - ny, M - 1};
        }
        else if(curs == WEST) {
            if(dir == EAST) ret = {SOUTH, nx, 0};
            else if(dir == WEST) ret = {NORTH, nx, M - 1};
            else if(dir == SOUTH) ret = {BOT, wallR + ny, wallC - 1};
            else ret = {TOP, ny, 0};
        }
        else if(curs == SOUTH) {
            if(dir == EAST) ret = {EAST, nx, 0};
            else if(dir == WEST) ret = {WEST, nx, M - 1};
            else if(dir == SOUTH) ret = {BOT, wallR + M, wallC + ny};
            else ret = {TOP, M - 1, ny};
        }
        else if(curs == NORTH) {
            if(dir == EAST) ret = {WEST, nx, 0};
            else if(dir == WEST) ret = {EAST, nx, M - 1};
            else if(dir == SOUTH) ret = {BOT, wallR - 1, wallC + M - ny - 1};
            else ret = {TOP, 0, M - 1 - ny};
        }
        else {
            if(dir == EAST) ret = {EAST, 0, M - 1 - nx};
            else if(dir == WEST) ret = {WEST, 0, nx};
            else if(dir == SOUTH) ret = {SOUTH, 0, ny};
            else ret = {NORTH, 0, M - 1 - ny};
        }
    }

    return ret;
}

void moveMachine() {
    int sz = q.size();

    while(sz--) {
        tuple<int, int, int> cur = q.front();
        q.pop();

        for(int i = 0; i < 4; ++i) {
            int ns, nx, ny;
            tie(ns, nx, ny) = getNxtPos(cur, i);
            if(ns == -1 || nx == -1 || ny == -1) continue;
            if(ns == BOT && (space[nx][ny] == 1 || visit[nx][ny] > 0)) continue;
            else if(ns != BOT && (timewall[ns][nx][ny] == 1 || visitwall[ns][nx][ny] > 0)) {
                continue;
            }

            if(ns == BOT) {
                if(get<0>(cur) == BOT) visit[nx][ny] = visit[get<1>(cur)][get<2>(cur)] + 1;
                else visit[nx][ny] = visitwall[get<0>(cur)][get<1>(cur)][get<2>(cur)] + 1;

                if(space[nx][ny] == 4) {
                    ans = visit[nx][ny] - 1;
                    isFound = true;
                    return;
                }
            }
            else {
                if(get<0>(cur) == BOT) visitwall[ns][nx][ny] = visit[get<1>(cur)][get<2>(cur)] + 1;
                else visitwall[ns][nx][ny] = visitwall[get<0>(cur)][get<1>(cur)][get<2>(cur)] + 1;
            }

            q.emplace(ns, nx, ny);
        }
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> N >> M >> F;
    for(int i = 0; i < N; ++i) {
        for(int j = 0; j < N; ++j) {
            cin >> space[i][j];
            
            // 시간의 벽이 차지하는 공간의 가장 왼쪽 위 칸 저장
            if(space[i][j] == 3 && wallR == -1) wallR = i, wallC = j;
        }
    }

    for(int k = 0; k < 5; ++k) {
        for(int i = 0; i < M; ++i) {
            for(int j = 0; j < M; ++j) {
                cin >> timewall[k][i][j];
                if(timewall[k][i][j] == 2) {
                    timewall[k][i][j] = 0;
                    startpos = {k, i, j};
                    visitwall[k][i][j] = 1;
                }
            }
        }
    }

    for(int i = 0; i < F; ++i) {
        cin >> abnomality[i][0] >> abnomality[i][1] >> abnomality[i][2] >> abnomality[i][3];
        visit[abnomality[i][0]][abnomality[i][1]] = 1;
    }

    q.push(startpos);

    while(!isFound && !q.empty()) {
        ++T;
        spreadAbnomality();
        moveMachine();
    }
    
    cout << ans;
    return 0;
}