#include <iostream>
#include <utility>
#define OFFSET  4
#define EXIT    -1
#define ABS(X)  ((X) >= 0 ? (X) : (X) * (-1))
using namespace std;

int N, M, K, dstX, dstY, ans;
int maze[11][11];
int plate[10][10];
pair<int, int> players[10];
bool escaped[10];
int dx[] = {1, 0, -1, 0};
int dy[] = {0, 1, 0, -1};

bool validRange(int x, int y) {
    return x >= 1 && x <= N && y >= 1 && y <= N;
}

void movePlayers() {
    for(int i = 0; i < M; ++i) {
        if(escaped[i]) continue;

        int& curx = players[i].first;
        int& cury = players[i].second;
        int dist = ABS(curx - dstX) + ABS(cury - dstY);
        int dir = -1;

        for(int j = 0; j < 4; ++j) {
            int nx = curx + dx[j];
            int ny = cury + dy[j];
            if(!validRange(nx, ny) || (maze[nx][ny] >= 1 && maze[nx][ny] <= 9)) continue;

            int ndist = ABS(nx - dstX) + ABS(ny - dstY);
            if(ndist < dist) {
                dist = ndist;
                dir = j;
            }
            else if(ndist == dist && dir % 2) {
                dir = j;
            }
        }

        if(dir != -1) {
            maze[curx][cury] &= (~(1 << (i + OFFSET)));
            curx += dx[dir];
            cury += dy[dir];
            ++ans;

            if(curx == dstX && cury == dstY) {
                escaped[i] = true;
            }
            else {
                maze[curx][cury] |= (1 << (i + OFFSET));
            }
        }
    }
}

bool checkArea(int r, int c, int l) {
    bool isPlayer = false, isExit = false;

    for(int i = r; i < r + l; ++i) {
        for(int j = c; j < c + l; ++j) {
            if(maze[i][j] > 10) isPlayer = true;
            else if(maze[i][j] == EXIT) isExit = true;
        }
    }

    return isPlayer && isExit;
}

void selectArea(int& r, int& c, int& l) {
    for(int k = 2; k <= N; ++k) {
        for(int i = 1; i <= N - k + 1; ++i) {
            for(int j = 1; j <= N - k + 1; ++j) {
                if(checkArea(i, j, k)) {
                    r = i; c = j; l = k;
                    return;
                }
            }
        }
    }
}

void rotateMaze() {
    int r = -1, c = -1, l = -1;
    selectArea(r, c, l);

    for(int i = 0; i < l; ++i) {
        for(int j = 0; j < l; ++j) {
            plate[i][j] = maze[r + i][c + j];
        }
    }

    for(int i = 0; i < l; ++i) {
        for(int j = 0; j < l; ++j) {
            maze[r + i][c + j] = plate[l - j - 1][i];
            if(maze[r + i][c + j] == EXIT) {
                dstX = r + i;
                dstY = c + j;
            }
            else if(maze[r + i][c + j] >= 1 && maze[r + i][c + j] <= 9) --maze[r + i][c + j];
            else if(maze[r + i][c + j] > 10) {
                int mask = maze[r + i][c + j] >> 4;

                for(int k = 0; k < M; ++k) {
                    if(mask & (1 << k)) {
                        players[k] = {r + i, c + j};
                    }
                }
            }
        }
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> N >> M >> K;
    for(int i = 1; i <= N; ++i) {
        for(int j = 1; j <= N; ++j) {
            cin >> maze[i][j];
        }
    }

    for(int i = 0; i < M; ++i) {
        cin >> players[i].first >> players[i].second;
        maze[players[i].first][players[i].second] |= (1 << (i + OFFSET));
    }

    cin >> dstX >> dstY;
    maze[dstX][dstY] = EXIT;

    for(int i = 0; i < K; ++i) {
        movePlayers();
        rotateMaze();
    }

    cout << ans << '\n' << dstX << ' ' << dstY;
    return 0;
}