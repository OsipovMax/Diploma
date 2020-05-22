main: searchTest.cpp
	mpixlcxx_r -qsmp=omp:noopt searchTest.cpp -o main

run:
	mpisubmit.bg -n 1 -w 00:03:00 -m SMP -e "OMP_NUM_THREADS=4" main -- -p