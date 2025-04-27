// [BOJ] 13308 / 주유소, 106572 KB, 344 ms
#include <iostream>
#include <vector>
#include <queue>
#include <tuple>
#define MAX 2501ll * 2501ll * 2501ll

using namespace std;
typedef long long ll;
typedef pair<int, ll> pil;
typedef tuple<int, int, ll> tiil;

int n, m;
ll cost[2501][2501];
bool v[2501][2501];
ll price[2501];
vector<pil> g[2501];

struct comp {
	bool operator()(tiil t1, tiil t2) {
		return get<2>(t1) > get<2>(t2);
	}
};

priority_queue<tiil, vector<tiil>, comp> pq;

ll dijkstra() {
	pq.emplace(make_tuple(1, 1, 0));
	cost[1][1] = 0;

	int a, b;
	ll c;
	while (!pq.empty()) {
		tiil cur = pq.top(); pq.pop();
		a = get<0>(cur); b = get<1>(cur); c = get<2>(cur);

		if (a == n) return c;
		if (v[a][b]) continue;
		v[a][b] = true;

		for (pil nxt : g[a]) {
			int na = nxt.first;
			int nb = b;
			ll d = nxt.second;
			ll nc = c + price[b] * d;
			if (price[b] > price[na]) {
				nb = na;
			}

			if (!v[na][nb] && cost[na][nb] > nc) {
				cost[na][nb] = nc;
				pq.emplace(make_tuple(na, nb, nc));
			}
		}
	}

	return 0;
}

int main() {
	ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
	cin >> n >> m;
	for (int i = 1; i < n + 1; ++i) {
		cin >> price[i];
	}

	int a, b;
	ll c;
	for (int i = 0; i < m; ++i) {
		cin >> a >> b >> c;
		g[a].emplace_back(make_pair(b, c));
		g[b].emplace_back(make_pair(a, c));
	}

	for (int i = 1; i <= n; ++i) {
		for (int j = 1; j <= n; ++j) {
			cost[i][j] = MAX;
			v[i][j] = false;
		}
	}

	cout << dijkstra() << '\n';
	return 0;
}
