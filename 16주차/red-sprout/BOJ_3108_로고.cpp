#include <iostream>
#include <vector>

using namespace std;

struct rect {
	int x1, y1, x2, y2;
	rect(int X1, int Y1, int X2, int Y2) {
		x1 = X1; y1 = Y1; x2 = X2; y2 = Y2;
	}
};

int n;
int parent[1001];
vector<rect> list;

bool overlap(rect r1, rect r2) {
	if (r1.x2 < r2.x1 || r2.x2 < r1.x1) return false;
	if (r1.y2 < r2.y1 || r2.y2 < r1.y1) return false;
	if (r1.x1 > r2.x1 && r1.y1 > r2.y1 && r1.x2 < r2.x2 && r1.y2 < r2.y2) return false;
	if (r2.x1 > r1.x1 && r2.y1 > r1.y1 && r2.x2 < r1.x2 && r2.y2 < r1.y2) return false;
	return true;
}

int find_parent(int idx) {
	if (parent[idx] == idx) return idx;
	return parent[idx] = find_parent(parent[idx]);
}

void union_parent(int i1, int i2) {
	i1 = find_parent(i1);
	i2 = find_parent(i2);
	if (i1 == i2) return;
	if (i1 > i2) {
		parent[i1] = i2;
	}
	else {
		parent[i2] = i1;
	}
}

int main() {
	ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
	cin >> n;
	for (int i = 0; i <= n; ++i) {
		parent[i] = i;
	}

	list.emplace_back(rect(0, 0, 0, 0));
	int x1, y1, x2, y2;
	for (int i = 0; i < n; ++i) {
		cin >> x1 >> y1 >> x2 >> y2;
		list.emplace_back(rect(x1, y1, x2, y2));
	}

	for (int i = 0; i < n; ++i) {
		for (int j = i + 1; j <= n; ++j) {
			if (overlap(list[i], list[j])) {
				union_parent(i, j);
			}
		}
	}

	bool v[1001];
	fill(v, v + n + 1, false);
	for (int i = 1; i <= n; ++i) {
		v[find_parent(i)] = true;
	}

	int cnt = 0;
	for (int i = 1; i <= n; ++i) {
		if (v[i]) ++cnt;
	}

	cout << cnt << '\n';
	return 0;
}
