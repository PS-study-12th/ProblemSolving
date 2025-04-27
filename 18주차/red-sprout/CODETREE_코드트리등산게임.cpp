// [CODETREE] 코드트리 등산 게임, 57 MB, 634 ms
#include <iostream>
#include <vector>
#define MAX_H 1'000'000
#define MAX_I 550'000

using namespace std;
typedef long long ll;
typedef pair<int, int> pii;

int n;
ll hlist[MAX_I + 1];
ll dp[MAX_I + 1];
ll tree[4 * MAX_I + 1];
int htree[4 * MAX_H + 1];
vector<pii> st[MAX_H + 1];

void update_tree(int node, int s, int e, int idx, ll val) {
	if (e < idx || idx < s) return;
	if (s == e) {
		tree[node] = val;
		return;
	}
	int m = (s + e) >> 1;
	update_tree(node << 1, s, m, idx, val);
	update_tree(node << 1 | 1, m + 1, e, idx, val);
	tree[node] = max(tree[node << 1], tree[node << 1 | 1]);
}

void update_htree(int node, int s, int e, int idx, int val) {
	if (e < idx || idx < s) return;
	if (s == e) {
		htree[node] = val;
		return;
	}
	int m = (s + e) >> 1;
	update_htree(node << 1, s, m, idx, val);
	update_htree(node << 1 | 1, m + 1, e, idx, val);
	htree[node] = max(htree[node << 1], htree[node << 1 | 1]);
}

int get_htree(int node, int s, int e, int ts, int te) {
	if (e < ts || te < s) return -1;
	if (ts <= s && e <= te) return htree[node];
	int m = (s + e) >> 1;
	return max(get_htree(node << 1, s, m, ts, te), get_htree(node << 1 | 1, m + 1, e, ts, te));
}

void cmd100() {
	cin >> n;
	fill(htree, htree + 4 * MAX_H + 1, -1);
	for (int i = 1; i <= n; ++i) {
		cin >> hlist[i];
	}
	for (int i = 1; i <= n; ++i) {
		int cnt = get_htree(1, 1, MAX_H, 1, hlist[i] - 1);
		ll cand = (ll)MAX_H * (cnt + 1) + hlist[i];
		dp[i] = max(hlist[i], cand);
		update_tree(1, 1, MAX_I, i, dp[i]);
		int level = dp[i] / MAX_H;
		if (st[hlist[i]].empty()) st[hlist[i]].push_back({ level, level });
		else st[hlist[i]].push_back({ level, max(st[hlist[i]].back().second, level) });
		update_htree(1, 1, MAX_H, hlist[i], dp[i] / MAX_H);
	}
}

void cmd200() {
	++n;
	cin >> hlist[n];
	int cnt = get_htree(1, 1, MAX_H, 1, hlist[n] - 1);
	ll cand = (ll)MAX_H * (cnt + 1) + hlist[n];
	dp[n] = max(hlist[n], cand);
	update_tree(1, 1, MAX_I, n, dp[n]);
	int level = dp[n] / MAX_H;
	if (st[hlist[n]].empty()) st[hlist[n]].push_back({ level, level });
	else st[hlist[n]].push_back({ level, max(st[hlist[n]].back().second, level) });
	update_htree(1, 1, MAX_H, hlist[n], st[hlist[n]].back().second);
}

void cmd300() {
	update_tree(1, 1, MAX_I, n, 0);
	int h = hlist[n];
	st[h].pop_back();
	int new_val = st[h].empty() ? -1 : st[h].back().second;
	update_htree(1, 1, MAX_H, h, new_val);
	--n;
}

void cmd400() {
	int m_index; cin >> m_index;
	cout << MAX_H * (dp[m_index] / MAX_H + 1) + tree[1] << '\n';
}

int main() {
	ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
	int q; cin >> q;
	vector<int> hlist;
	while (q-- > 0) {
		int cmd; cin >> cmd;
		if (cmd == 100) {
			cmd100();
		}
		else if (cmd == 200) {
			cmd200();
		}
		else if (cmd == 300) {
			cmd300();
		}
		else {
			cmd400();
		}
	}
	return 0;
}

