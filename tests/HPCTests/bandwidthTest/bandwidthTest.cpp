
#include <iostream>
#include <string>
#include <mpi.h>
#include <vector>

using namespace std;

const int CYCLE_COUNT = 1000;
vector<double> firstArr(CYCLE_COUNT, 3.3);
vector<double> secondArr(CYCLE_COUNT, 3.3);
vector<double> thirdArr(CYCLE_COUNT, 3.3);
vector<double> indirectAddressArr(1, 0.0);
double a = 3.5;
double b = 7.1;
double c = 4.5;
double nonAddressVar = 0.0;

enum Modes { PROCESSOR, MEMORY };

int main(int argc, char **argv) {
  int retVal;
  int numproces, rank;
  int numAuto = 0;
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
    cout << " Invalid args" << endl;
    MPI_Finalize();
    return -1;
  }
  MPI_Comm_size(MPI_COMM_WORLD, &numproces);
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);

  if (curMode == PROCESSOR) {
    beginTime = MPI_Wtime();
    while (true) {
      numAuto++;
#pragma omp parallel private(nonAddressVar, a, b, c)
      for (int j = 0; j < CYCLE_COUNT; ++j) {
#pragma omp for
        for (int k = 0; k < CYCLE_COUNT; ++k) {
          nonAddressVar = a * b + c;
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
      numAuto++;
#pragma omp parallel private(j)
      for (j = 0; j < CYCLE_COUNT; ++j) {
#pragma omp for
        for (int k = 0; k < CYCLE_COUNT; ++k) {
          indirectAddressArr[0] = firstArr[k] * secondArr[k] + thirdArr[k];
        }
      }
      if (MPI_Wtime() - beginTime > 5.0) {
        break;
      }
    }
  }

  if (numproces == 1) {
    cout << numAuto << endl;
    MPI_Finalize();
    return 0;
  }

  MPI_Status status;
  if (rank != 0) {
    MPI_Send(&numAuto, 1, MPI_INT, 0, rank, MPI_COMM_WORLD);
  }

  if (rank == 0) {
    int buf;
    int res = 0;
    res += numAuto;
    for (int i = 0; i < numproces - 1; ++i) {
      MPI_Recv(&buf, 1, MPI_INT, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD,
               &status);
      res += buf;
    }
    cout << res << endl;
  }

  MPI_Finalize();
  return 0;
}