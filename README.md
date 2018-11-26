# Sorting Algorithms & Fair comparison testbed

In Nov 2018, I needed to figure out what is the best sort algorithm on small datasets (almost sorted for graphics rendering) so I started my quest gathering good Java Sorting implementations ... reading papers & started experimenting my own benchmarks.

As I found this gem (https://github.com/sebawild/nearly-optimal-mergesort-code) the source code of Sebastian Wild 2018 paper, I started hacking the code to make its benchmark for fair (more reproductible) tests and I fixed several Sorter implementations to use pre-allocation (no GC overhead).

Thanks to Vladimir Yaroslavskiy, I added its DualPivotQuickSort 2011, 2018 & his BentleyBasher implementation, from Tageer Valev his adaptive RadixSort ...

I made lots of improvements on the BentleyBasher 

All code is free & open source, under MIT or GPL2 license.

TODO: complete history & motivations

# Objective

- Use a robust test methodology to test Java Sorting Implementations (working, tested) with all known distributions (random, sawtooth, dithered, ...) as recent Sorting implementations use polymorphism / adaptive strategy to better sort some types of data sets (sorted & reversed runs, random ...)
- Include best Sort implementations (based on int[] arrays only, sorry) and also using 2 int[] arrays (data + indices)
- Provide a complete Test suite (basher, stats, & analysis) to reproduce reliable experiments and allow optimisation (tuning every details of a particular instance) in a faire manner (reduce OS & JVM biases) to obtain a fair comparison

# Data results

See in the results folder to last data & comparison stats

Nice Plots will be coming next...



## Fork of the Sebastian Wild's 'Nearly optimal Natural Mergesort &mdash; Code'

I derived my work from this fabulous repository on github, that provides source code (MIT license) of several sorting algorithms (merge sorts, like TD/BU, TimSort & variants, Peek & Power Sort).


Here is the original 'README' of the master repository:

### Nearly optimal Natural Mergesort &mdash; Code
Code for experiments with nearly optimally adaptive mergesort variants 
peeksort and powersort.

#### Reproducing the results from the paper

To reproduce the running time study from the paper,
execute 

    ant package
    ./paper-experiments.sh

The build requires a recent JDK 8, Oracle's version is recommended.

Make sure to use the paper release:  
[![DOI](https://zenodo.org/badge/132030229.svg)](https://zenodo.org/badge/latestdoi/132030229)

This produces several files in the current directory.

 * The *.out files show the progress made in the individual runs 
and contain debug output from JVM's just-in-time compiler. 
It can be used to check that no massive deoptimization steps happened during the timed
experiments. (Endless output during the warmup phase and occasional printed lines 
during timed runs are normal.)
 * The *.csv files contain one line per executed sort and report the individual
running time. These files were used in the paper to compute average and standard deviations
of running times.

#### Unit Tests

To run harness tests for correctness of the sorting methods, run

    ant test

