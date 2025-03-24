#include <iostream>
#include <queue>
#include <utility>
#include <string>
#include <cctype>
#include <cstring>
using namespace std;

int T, h, w, ans;
string str;
char board[100][100];
bool visit[100][100];
bool keys[26];
queue<pair<int, int>> gates[27];
queue<char> keyqueue;
const int EMPTY = 26;
int dx[] = { 1, 0, -1, 0 };
int dy[] = { 0, 1, 0, -1 };

bool isBorder(int x, int y) {
	return x == 0 || x == h - 1 || y == 0 || y == w - 1;
}

bool validRange(int x, int y) {
	return x >= 0 && x < h && y >= 0 && y < w;
}

void init() {
	memset(keys, 0, sizeof(bool) * 26);
	for (int i = 0; i < h; i++) {
		memset(visit[i], 0, sizeof(bool) * w);
	}

	for (int i = 0; i < 27; i++) {
		while (!gates[i].empty()) gates[i].pop();
	}

	ans = 0;
}

void bfs(int x, int y) {
	queue<pair<int, int>> q;
	q.emplace(x, y);
	visit[x][y] = true;

	while (!q.empty()) {
		int curx = q.front().first;
		int cury = q.front().second;
		q.pop();

		for (int i = 0; i < 4; i++) {
			int nx = curx + dx[i];
			int ny = cury + dy[i];
			if (!validRange(nx, ny)) continue;
			if (board[nx][ny] == '*' || visit[nx][ny]) continue;
			if (isupper(board[nx][ny])) {
				gates[board[nx][ny] - 'A'].emplace(nx, ny);
				keyqueue.push(tolower(board[nx][ny]));
				continue;
			}
			if (islower(board[nx][ny])) {
				keys[board[nx][ny] - 'a'] = true;
				keyqueue.push(board[nx][ny]);
			}

			if (board[nx][ny] == '$') {
				ans++;
			}
			q.emplace(nx, ny);
			visit[nx][ny] = true;
		}
	}
}


int main() {
	ios::sync_with_stdio(false);
	cin.tie(nullptr);

	cin >> T;
	while (T--) {
		cin >> h >> w;
		init();
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				cin >> board[i][j];
				if (isBorder(i, j) && board[i][j] != '*') {
					if (isupper(board[i][j]))
						gates[board[i][j] - 'A'].emplace(i, j);
					else
						gates[EMPTY].emplace(i, j);
				}
			}
		}

		cin >> str;
		for (char c : str) {
			if (c == '0') break;
			keys[c - 'a'] = true;
			keyqueue.push(c);
		}

		while (!gates[EMPTY].empty()) {
			int x = gates[EMPTY].front().first;
			int y = gates[EMPTY].front().second;
			gates[EMPTY].pop();

			if (visit[x][y]) continue;
			if (board[x][y] == '$') {
				ans++;
			}
			if (islower(board[x][y])) {
				keys[board[x][y] - 'a'] = true;
				keyqueue.push(board[x][y]);
			}
			bfs(x, y);
		}

		while (!keyqueue.empty()) {
			char ch = keyqueue.front();
			keyqueue.pop();

			int idx = ch - 'a';
			if (!keys[idx]) continue;
			while (!gates[idx].empty()) {
				int x = gates[idx].front().first;
				int y = gates[idx].front().second;
				gates[idx].pop();

				if (visit[x][y]) continue;
				bfs(x, y);
			}
		}

		cout << ans << '\n';
	}

	return 0;
}