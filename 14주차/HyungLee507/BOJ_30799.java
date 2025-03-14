package backjoon.B_type.study14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BOJ_30799 {
    static final long MOD = 998244353;

    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int s = Integer.parseInt(bf.readLine());
        long[][] dp = new long[8][s + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= s; i++) {
            dp[0][i] = (7 * dp[0][i - 1]) % MOD;
        }
        for (int i = 1; i < 8; i++) {
            for (int j = 1; j <= s; j++) {
                dp[i][j] = (dp[i - 1][j - 1] + dp[i][j - 1] * 6) % MOD;
            }
        }
        System.out.println(dp[7][s]);
        bf.close();
    }
}
