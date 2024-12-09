#include <iostream>
#include <algorithm>
#define MAX 100001

using namespace std;
typedef pair<int, int> pi;

int M, N, L;
int arr[MAX];
pi p[MAX];

void init() {
    cin >> M >> N >> L;
    for(int i = 0; i < M; i++) {
        cin >> arr[i];
    }
    sort(arr, arr + M);
    for(int i = 0; i < N; i++) {
        cin >> p[i].first >> p[i].second;
    }
}

void solve() {
    int result = 0;
    for(int i = 0; i < N; i++){
        int l = -1, r = M, mid;
        while(l + 1 < r){
            mid = l + (r - l) / 2;
            int cal = abs(arr[mid] - p[i].first) + p[i].second;
            if(cal <= L){
                result++;
                break;
            }
            else if(p[i].first >= arr[mid]) {
              l = mid;
            } else{
              r = mid;
            }
        }
    }
    cout << result;
}

int main() {
  ios_base::sync_with_stdio(0); cin.tie(0); cout.tie(0);
  init();
  solve();
  return 0;
}
