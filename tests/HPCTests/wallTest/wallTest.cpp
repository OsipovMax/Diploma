
#include <iostream>
#include <string>
#include <mpi.h>
#include <vector>

using namespace std;

enum Modes { PROCESSOR, MEMORY };

const int CYCLE_COUNT = 1000;
const int WALL_HIGHT = 120;
double a = 5.5;
double b = 3.5;
double c = 5.7;
double nonAddressVar;
vector<double> firstArr(CYCLE_COUNT, 3.7);
vector<double> secondArr(CYCLE_COUNT, 5.3);
vector<double> thirdArr(CYCLE_COUNT, 1.9);
vector<double> indirectAddressArr(CYCLE_COUNT, 17.1);

int main(int argc, char **argv) {
  int retVal;
  int numproces, rank;
  nonAddressVar = 3.3;
  int partBrickCount = 0;
  Modes curMode;
  double beginTime;
  if ((retVal = MPI_Init(&argc, &argv)) != MPI_SUCCESS) {
    cout << " MPI_Init error " << endl;
    MPI_Abort(MPI_COMM_WORLD, retVal);
  }
  if (string(argv[1]) == "-p") {
    curMode = PROCESSOR;
  } else if (string(argv[1]) == "-m") {
    curMode = MEMORY;
  } else {
    cout << " Invalid arg" << endl;
    MPI_Finalize();
    return -1;
  }
  MPI_Comm_size(MPI_COMM_WORLD, &numproces);
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);

  if (curMode == PROCESSOR) {
    beginTime = MPI_Wtime();
    while (true) {
      partBrickCount++;
#pragma omp parallel private(nonAddressVar, a, c)
      for (int j = 0; j < WALL_HIGHT; ++j) {
#pragma omp for
        for (int k = 0; k < CYCLE_COUNT; ++k) {
          nonAddressVar += a * 3.5 + c;
          nonAddressVar -= nonAddressVar * 0.5;
        }
      }
      if (MPI_Wtime() - beginTime > 5.0) {
        break;
      }
    }
    nonAddressVar += nonAddressVar * 0.32;
  } else {
    int j = 0;
    beginTime = MPI_Wtime();
    while (true) {
      partBrickCount++;
#pragma omp parallel private(j)
      for (j = 0; j < WALL_HIGHT; ++j) {
#pragma omp for
        for (int k = 0; k < CYCLE_COUNT; ++k) {
          indirectAddressArr[k] = firstArr[k] * secondArr[k] + thirdArr[k];
          indirectAddressArr[k] = firstArr[k];
          firstArr[k] = secondArr[k];
          secondArr[k] = thirdArr[k];
          thirdArr[k] = indirectAddressArr[k];
        }
      }
      if (MPI_Wtime() - beginTime > 5.0) {
        break;
      }
    }
  }
  if (numproces == 1) {
    cout << int(partBrickCount * 0.25) << endl;
    MPI_Finalize();
    return 0;
  }
  MPI_Status status;
  if (rank != 0) {
    MPI_Send(&partBrickCount, 1, MPI_INT, 0, rank, MPI_COMM_WORLD);
  }
  if (rank == 0) {
    int buf;
    int res = 0;
    res += partBrickCount;
    for (int i = 0; i < numproces - 1; ++i) {
      MPI_Recv(&buf, 1, MPI_INT, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD,
               &status);
      res += buf;
    }
    cout << int(res * 0.25) << endl;
  }
  MPI_Finalize();
  return 0;
}