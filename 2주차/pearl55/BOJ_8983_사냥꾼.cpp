#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>

using namespace std;
using ll = long long;

int M, N;
ll L;
ll firePos[100001];
int ret = 0;

bool binarySearch(ll st, ll ed, ll ax, ll ay, ll target) {

	while (st <= ed) {
		ll mid = (st + ed) / 2;
		ll len = abs(firePos[mid] - ax) + ay;

		if (len <= target) {
			return true;
		}
		else if (firePos[mid] < ax) {
			st = mid + 1;  // 더 큰 범위 탐색
		}
		else {
			ed = mid - 1;  // 더 작은 범위 탐색
		}
	}
	return false;
}

int main(void) {
	cin.tie(0);
	ios::sync_with_stdio(0);

	cin >> M >> N >> L;

	for (int i = 0; i < M; i++) {
		cin >> firePos[i];
	}

	sort(firePos, firePos + M); // 올바른 크기 사용

	for (int i = 0; i < N; i++) {
		ll ax, ay;
		cin >> ax >> ay;
		if (binarySearch(0, M - 1, ax, ay, L)) ret++;
	}

	cout << ret;
	return 0;
}
