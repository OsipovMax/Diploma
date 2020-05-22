#include <iostream>
#include <string>
#include <omp.h>
#include <vector>
#include <thread>
#include <mutex>

using namespace std;

const int CYCLE_COUNT = 1000;
const int WALL_HIGHT = 120;

mutex mtx;

enum Modes { PROCESSOR, MEMORY };

void testFunction(Modes mode, int& resultLength) {
  double a = 5.5;
  double b = 3.5;
  double nonAddressVar;
  vector<double> firstArr(CYCLE_COUNT, 3.7);
  vector<double> secondArr(CYCLE_COUNT, 5.3);
  vector<double> thirdArr(CYCLE_COUNT, 1.9);
  vector<double> indirectAddressArr(CYCLE_COUNT, 17.1);
  int partBrickCount = 0;
  nonAddressVar = 3.3;
  double beginTime;
  if (mode == PROCESSOR) {
    beginTime = omp_get_wtime();
    while (true) {
      partBrickCount++;
      for (int j = 0; j < WALL_HIGHT; ++j) {
        for (int k = 0; k < CYCLE_COUNT; ++k) {
          nonAddressVar += a * 3.5 + b;
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
      partBrickCount++;
      for (int j = 0; j < WALL_HIGHT; ++j) {
        for (int k = 0; k < CYCLE_COUNT; ++k) {
          indirectAddressArr[k] = firstArr[k] * secondArr[k] + thirdArr[k];
          indirectAddressArr[k] = firstArr[k];
          firstArr[k] = secondArr[k];
          secondArr[k] = thirdArr[k];
          thirdArr[k] = indirectAddressArr[k];
        }
      }
      if (omp_get_wtime() - beginTime > 5.0) {
        break;
      }
    }
  }
  lock_guard<mutex> lock(mtx);
  resultLength += partBrickCount;
}

int main(int argc, char** argv) {
  int threadCount = 4;
  int resLen = 0;
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
  for (int i = 0; i < threadCount; ++i) {
    thread thr(testFunction, curMode, ref(resLen));
    threads.emplace_back(move(thr));
  }
  for (auto& thr : threads) {
    thr.join();
  }
  cout << int(resLen * 0.25);
  return 0;
}
