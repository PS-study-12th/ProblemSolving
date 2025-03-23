#include <iostream>
#include <string>
#include <vector>

using namespace std;

struct Node {
  int size = 0;
  Node* next[26];
  bool isEnd = false;
};

void add(Node* root, string str) {
  Node* node = root;
  for(int i = 0; i < str.length(); i++) {
    int idx = str[i] - 'a';
    node->size++;
    if(node->next[idx] == nullptr) node->next[idx] = new Node();
    node = node->next[idx];
  }
  node->size++;
  node->isEnd = true;
}

int count(Node* root, string str) {
  Node* node = root;
  int res = 0, size = 10000001;
  for(int i = 0; i < str.length(); i++) {
    int idx = str[i] - 'a';
    node = node->next[idx];
    if(size != node->size) res++;
    size = node->size;
  }
  return res;
}

void deleteTrie(Node* cur) {
  for(int i = 0; i < 26; i++) {
    if(cur->next[i] != nullptr) deleteTrie(cur->next[i]);
  }
  delete cur;
}

int main() {
  ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
  cout << fixed;
  cout.precision(2);
  int n;
  string str;
  
  while(cin >> n) {
    int sum = 0;
    Node* root = new Node();
    vector<string> arr;
    for(int i = 0; i < n; i++) {
      cin >> str;
      arr.emplace_back(str);
      add(root, str);
    }
    for(int i = 0; i < n; i++) {
      sum += count(root, arr[i]);
    }
    cout << double(sum) / double(n) <<'\n';
    deleteTrie(root);
  }

  return 0;
}
