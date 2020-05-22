
#include <iostream>
#include <string>
#include <mpi.h>
#include <vector>

using namespace std;

enum Modes { PROCESSOR, MEMORY };

const int CYCLE_COUNT = 1000;
const int GROUP_SIZE = 1000;
vector<double> firstArr(CYCLE_COUNT, 3.3);
vector<double> secondArr(CYCLE_COUNT, 3.3);
vector<double> thirdArr(CYCLE_COUNT, 3.3);
vector<double> indirectAddressArr(CYCLE_COUNT, 0.0);

int main(int argc, char **argv) {
  int retVal;
  int numproces, rank;
  Modes curMode;
  double timeBeg, timeEnd, timeMax = 0.0;
  double nonAddressVar = 0.0;
  vector<double> groupArr(GROUP_SIZE, 17.3);

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
  int numPerson = 0;
  int k = 0;
  timeBeg = MPI_Wtime();
  if (curMode == PROCESSOR) {
    for (int i = 0; i < GROUP_SIZE; ++i) {
      numPerson++;
#pragma omp parallel for
      for (int j = 0; j < 100; ++j) {
        for (k = 0; k < CYCLE_COUNT; ++k) {
          indirectAddressArr[0] = firstArr[k] * secondArr[k] + thirdArr[k];
          indirectAddressArr[0] -= 10.3;
        }
      }
      if (MPI_Wtime() - timeBeg > 5.0) {
        break;
      }
    }
  } else {
    for (int i = 0; i < GROUP_SIZE; ++i) {
      numPerson++;
#pragma omp parallel for
      for (int j = i + 1; j < GROUP_SIZE; ++j) {
        for (int l = 0; l < 100; ++l) {
          for (int k = 0; k < CYCLE_COUNT; ++k) {
            indirectAddressArr[0] = firstArr[k] * secondArr[k] + thirdArr[k];
            indirectAddressArr[0] -= 10.3;
          }
        }
      }
      if (MPI_Wtime() - timeBeg > 5.0) {
        break;
      }
    }
  }

  if (numproces == 1) {
    cout << numPerson << endl;
    MPI_Finalize();
    return 0;
  }

  MPI_Status status;
  if (rank != 0) {
    MPI_Send(&numPerson, 1, MPI_INT, 0, rank, MPI_COMM_WORLD);
  }
  if (rank == 0) {
    int buf;
    int res = 0;
    res += numPerson;
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