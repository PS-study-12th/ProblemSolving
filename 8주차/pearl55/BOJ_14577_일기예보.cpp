#include <iostream>
using namespace std;
using ll = long long;
using pii = pair<int, int>;

// 배열 방식 dynamic segtree
struct Node {
    ll left, right;
    ll prefixSum;

    Node() {
        left = right = -1;
        prefixSum = 0;
    }
};

Node segt[4040404];
int treeSize = 1;
int N, Q;
ll area[100001];

void update(ll node, ll st, ll ed, ll idx, ll val) {
    if(st==ed) {
        segt[node].prefixSum = val;
        return;
    }

    ll mid = (st+ed)>>1;

    if(idx <= mid) {
        if(segt[node].left == -1) segt[node].left = treeSize++;
        update(segt[node].left, st, mid, idx, val);
    }
    else {
        if(segt[node].right == -1) segt[node].right = treeSize++;
        update(segt[node].right, mid+1, ed, idx, val);
    }

    ll leftSum = segt[node].left != -1 ? segt[segt[node].left].prefixSum : 0;
    ll rightSum = segt[node].right != -1? segt[segt[node].right].prefixSum : 0;
    segt[node].prefixSum = leftSum + rightSum;
}

ll rangeSumQuery(ll node, ll st, ll ed, ll l, ll r) {
    if(node == -1) return 0;
    if(r < st || ed < l) return 0;
    if(l <= st && ed <= r) return segt[node].prefixSum;
    ll mid = (st+ed)>>1;

    return rangeSumQuery(segt[node].left, st, mid, l, r)
        + rangeSumQuery(segt[node].right, mid+1, ed, l, r);
}

ll kthQuery(ll node, ll st, ll ed, ll k) {
    if(st==ed) return st;
    ll mid = (st+ed)>>1;
    ll leftVal = segt[segt[node].left].prefixSum;
    if(leftVal >= k) return kthQuery(segt[node].left, st, mid, k);
    else return kthQuery(segt[node].right, mid+1, ed, ll(k-leftVal));
}

int main(void){
    cin.tie(0)->sync_with_stdio(0);
    cin >> N >> Q;
    for(int i=1; i<=N; ++i) {
        cin >> area[i];
        ll cur = rangeSumQuery(0, 0, 1e18, area[i], area[i]);
        update(0, 0, 1e18, area[i], cur+1);
    }

    while(Q--) {
        int comm;
        ll one, two;
        cin >> comm >> one;

        if(comm == 1) {
            cin >> two;
            ll cur = rangeSumQuery(0, 0, 1e18, area[one], area[one]);
            update(0, 0, 1e18, area[one], cur-1);

            area[one] += two;
            cur = rangeSumQuery(0, 0, 1e18, area[one], area[one]);
            update(0, 0, 1e18, area[one], cur+1);

        }
        else if(comm==2){
            cin >> two;
            ll cur = rangeSumQuery(0, 0, 1e18, area[one], area[one]);
            update(0, 0, 1e18, area[one], cur-1);

            area[one] -= two;
            cur = rangeSumQuery(0, 0, 1e18, area[one], area[one]);
            update(0, 0, 1e18, area[one], cur+1);

        }
        else if(comm==3) {
            cin >> two;
            cout << rangeSumQuery(0, 0, 1e18, one, two) << "\n";
        }
        else {
            ll kth = N-one+1;
            cout << kthQuery(0, 0, 1e18, kth) << "\n";
        }
    }
    return 0;
}
