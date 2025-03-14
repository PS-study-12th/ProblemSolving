import java.io.*;
import java.util.*;

public class Main {
    static int s;
    static long[][] dp;
    static final int MOD = 998_244_353;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        s = Integer.parseInt(br.readLine());
        dp = new long[s + 1][8];
        dp[0][0] = 1;
        for(int i = 1; i <= s; i++) {
            dp[i][0] = 7 * dp[i - 1][0] % MOD;
        }

        for(int i = 1; i <= s; i++) {
            for(int j = 1; j < 8; j++) {
                dp[i][j] = (dp[i - 1][j - 1] + 6 * dp[i - 1][j] % MOD) % MOD;
            }
        }

        System.out.println(dp[s][7]);
        br.close();
    }
}
