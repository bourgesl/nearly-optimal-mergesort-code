#!/bin/bash

FREQ="2.0GHz"
echo "using sudo to set CPU frequency to $FREQ ..."

# set fixed frequency with governor 'performance'
sudo cpupower -c all frequency-set -d $FREQ -u $FREQ -g performance 
sudo cpupower -c all frequency-info

# set perf bias
sudo cpupower -c all set -b 0
sudo cpupower -c all info

