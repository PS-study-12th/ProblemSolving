#include <iostream>
#include <unordered_map>
#include <string>

using namespace std;

struct Node {
  string var;
  int start, end;
  Node *prev, *next;
  Node(string var, int start, int end) : var(var), start(start), end(end) {prev = nullptr; next = nullptr;}
};

int N;
Node *start_node = new Node("start", 0, 0);
Node *end_node = new Node("end", 100001, 100001);
unordered_map<string, Node*> map;

void init() {
  map.insert({"start", start_node});
  map.insert({"end", end_node});
  start_node->next = end_node;
  end_node->prev = start_node;
}

void malloc(string var, int size) {
  for(Node *node = start_node; node != end_node; node = node->next) {
    int s = node->end + 1;
    int e = node->next->start - 1;
    int length = e - s + 1;
    if(size <= length) {
      Node *new_node = new Node(var, s, s + size - 1);
      Node *next = node->next;
      node->next = new_node;
      new_node->prev = node;
      next->prev = new_node;
      new_node->next = next;
      map[var] = new_node;
      return;
    }
  }
  map[var] = start_node;
  return;
}

void free(string var) {
  Node *node = map.count(var) ? map[var] : start_node;
  if(node->prev && node->next) {
    Node *prev = node->prev;
    Node *next = node->next;
    prev->next = next;
    next->prev = prev;
    node->prev = nullptr;
    node->prev = nullptr;
    node->start = 0;
    node->end = 0;
  }
}

void print(string var) {
  Node *node = map.count(var) ? map[var] : start_node;
  cout << node->start << '\n';
}

int main() {
  cin.tie(0)->sync_with_stdio(0);
  cin >> N;
  init();
  string query;
  for(int i = 0; i < N; i++) {
    cin >> query;
    if (query.find("malloc") != string::npos) {
      string var = query.substr(0, 4);
      int size = stoi(query.substr(12, query.length() - 13));
      malloc(var, size);
    } else if (query.find("print") != string::npos) {
      string var = query.substr(6, 4);
      print(var);
    } else {
      string var = query.substr(5, 4);
      free(var);
    }
  }
  return 0;
}
