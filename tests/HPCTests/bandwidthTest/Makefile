main: bandwidthTest.cpp
	mpixlcxx_r -qsmp=omp:noopt bandwidthTest.cpp -o main

run:
	mpisubmit.bg -n 10 -w 00:03:00 -m SMP -e "OMP_NUM_THREADS=4" main -- -m