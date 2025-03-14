#include <iostream>
#define MOD 998244353
using namespace std;

int s;
long long dp[50300][8];    // i번째 위치까지 j번째 색상 간식을 먹는 경우의 수

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> s;
    dp[0][0] = 1;

    for (int i = 1; i <= s; ++i) {
        if (s - i >= 7) dp[i][0] = dp[i - 1][0] * 6 % MOD;

        for (int j = 1; j <= min(i, 7); ++j) {
            if (s - i < 7 - j) continue;
            if (j == 7) {
                dp[i][j] = (dp[i - 1][j - 1] + dp[i - 1][j] * 7) % MOD;
            }
            else {
				dp[i][j] = (dp[i - 1][j - 1] + dp[i - 1][j] * 6) % MOD;
            }
        }
    }

    cout << dp[s][7];
    return 0;
}