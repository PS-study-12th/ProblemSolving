#include <algorithm>
#include <iostream>
#include <stack>
#include <string>
using namespace std;

int main() {
    cin.tie(0) -> sync_with_stdio(0);

    string in;
    cin >> in;

    stack<char> L, R;
    for (char c : in) {
        L.push(c); // 초기 문자열을 st1에 넣음
    }

    int N;
    cin >> N;
    
    while (N--) {
        string comm;
        cin >> comm;

        if (comm == "L") { // 커서 왼쪽으로 이동
            if (!L.empty()) {
                R.push(L.top());
                L.pop();
            }
        } else if (comm == "D") { // 커서 오른쪽으로 이동
            if (!R.empty()) {
                L.push(R.top());
                R.pop();
            }
        } else if (comm == "B") { // 커서 왼쪽 문자 삭제
            if (!L.empty()) {
                L.pop();
            }
        } else if (comm == "P") { // 커서 왼쪽에 문자 추가
            char x;
            cin >> x;
            L.push(x);
        }
    }
    
    string ret;
    while (!L.empty()) {
        ret += L.top();
        L.pop();
    }
    reverse(ret.begin(), ret.end());

    while (!R.empty()) {
        ret += R.top();
        R.pop();
    }

    cout << ret << "\n";
    return 0;
}
