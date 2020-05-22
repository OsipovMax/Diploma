#include <iostream>
#include <omp.h>
#include <vector>
#include <string>
#include <thread>
#include <mutex>

using namespace std;

const int CYCLE_COUNT = 1000;
const int GROUP_SIZE = 1000;

mutex mtx;

enum Modes { PROCESSOR, MEMORY };

void testFunction(Modes mode, int& result) {
  int numPerson = 0;
  vector<double> firstArr(CYCLE_COUNT, 5.3);
  vector<double> secondArr(CYCLE_COUNT, 7.1);
  vector<double> thirdArr(CYCLE_COUNT, 3.9);
  vector<double> indirectAddressArr(CYCLE_COUNT, 17.1);

  double beginTime = omp_get_wtime();
  if (mode == PROCESSOR) {
    while (true) {
      numPerson++;
      for (int j = 0; j < 100; ++j) {
        for (int k = 0; k < CYCLE_COUNT; ++k) {
          indirectAddressArr[0] = firstArr[k] * secondArr[k] + thirdArr[k];
          indirectAddressArr[0] -= 10.3;
        }
      }
      if (omp_get_wtime() - beginTime > 5.0) {
        break;
      }
    }
  } else {
    for (int i = 0; i < GROUP_SIZE; ++i) {
      numPerson++;
      for (int j = i + 1; j < GROUP_SIZE; ++j) {
        for (int l = 0; l < 100; ++l) {
          for (int k = 0; k < CYCLE_COUNT; ++k) {
            indirectAddressArr[0] = firstArr[k] * secondArr[k] + thirdArr[k];
            indirectAddressArr[0] -= 10.3;
          }
        }
      }
      if (omp_get_wtime() - beginTime > 5.0) {
        break;
      }
    }
  }
  lock_guard<mutex> lock(mtx);
  result += numPerson;
}

int main(int argc, char** argv) {
  int threadCount = 1;
  Modes curMode;
  if (string(argv[1]) == "-p") {
    curMode = PROCESSOR;
  } else if (string(argv[1]) == "-m") {
    curMode = MEMORY;
  } else {
    cout << "Invalid arg" << endl;
    return -1;
  }
  vector<thread> threads;
  int res = 0;
  for (int i = 0; i < threadCount; ++i) {
    thread thr(testFunction, curMode, ref(res));
    threads.emplace_back(move(thr));
  }
  for (auto& thr : threads) {
    thr.join();
  }
  cout << res;
  return 0;
}
