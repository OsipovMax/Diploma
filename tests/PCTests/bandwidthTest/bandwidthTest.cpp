
#include <iostream>
#include <omp.h>
#include <vector>
#include <string>
#include <thread>
#include <mutex>

using namespace std;

const int CYCLE_COUNT = 1000;

mutex mtx;

enum Modes { PROCESSOR, MEMORY };

void testFunction(Modes mode, int& result) {
  vector<double> firstArr(CYCLE_COUNT, 3.3);
  vector<double> secondArr(CYCLE_COUNT, 3.3);
  vector<double> thirdArr(CYCLE_COUNT, 3.3);
  vector<double> indirectAddressArr(1, 0.0);
  double a = 3.5;
  double b = 7.1;
  double c = 4.5;
  double nonAddressVar = 0.0;
  int numAuto = 0;
  double beginTime;
  if (mode == PROCESSOR) {
    beginTime = omp_get_wtime();
    while (true) {
      numAuto++;
      for (int j = 0; j < CYCLE_COUNT; ++j) {
        for (int k = 0; k < CYCLE_COUNT; ++k) {
          nonAddressVar = a * b + c;
          nonAddressVar -= nonAddressVar * 0.5;
        }
      }
      if (omp_get_wtime() - beginTime > 5.0) {
        break;
      }
    }
    nonAddressVar += nonAddressVar * 0.32;
  } else {
    beginTime = omp_get_wtime();
    while (true) {
      numAuto++;
      for (int j = 0; j < CYCLE_COUNT; ++j) {
        for (int k = 0; k < CYCLE_COUNT; ++k) {
          indirectAddressArr[0] = firstArr[k] * secondArr[k] + thirdArr[k];
        }
      }
      if (omp_get_wtime() - beginTime > 5.0) {
        break;
      }
    }
  }
  lock_guard<mutex> lock(mtx);
  result += numAuto;
}

int main(int argc, char** argv) {
  int threadCount = 2;
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
