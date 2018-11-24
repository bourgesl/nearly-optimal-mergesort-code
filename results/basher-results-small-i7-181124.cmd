bourgesl@bourgesl-HP-ZBook-15-G3:~/libs/nearly-optimal-mergesort-code$ ./bentley-basher.sh 
Architecture :                          x86_64
Mode(s) opératoire(s) des processeurs : 32-bit, 64-bit
Boutisme :                              Little Endian
Processeur(s) :                         4
Liste de processeur(s) en ligne :       0-3
Thread(s) par cœur :                    1
Cœur(s) par socket :                    4
Socket(s) :                             1
Nœud(s) NUMA :                          1
Identifiant constructeur :              GenuineIntel
Famille de processeur :                 6
Modèle :                                94
Nom de modèle :                         Intel(R) Core(TM) i7-6820HQ CPU @ 2.70GHz
Révision :                              3
Vitesse du processeur en MHz :          800.021
Vitesse maximale du processeur en MHz : 3600,0000
Vitesse minimale du processeur en MHz : 800,0000
BogoMIPS :                              5424.00
Virtualisation :                        VT-x
Cache L1d :                             32K
Cache L1i :                             32K
Cache L2 :                              256K
Cache L3 :                              8192K
Nœud NUMA 0 de processeur(s) :          0-3
Drapaux :                               fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe syscall nx pdpe1gb rdtscp lm constant_tsc art arch_perfmon pebs bts rep_good nopl xtopology nonstop_tsc cpuid aperfmperf tsc_known_freq pni pclmulqdq dtes64 monitor ds_cpl vmx smx est tm2 ssse3 sdbg fma cx16 xtpr pdcm pcid sse4_1 sse4_2 x2apic movbe popcnt tsc_deadline_timer aes xsave avx f16c rdrand lahf_lm abm 3dnowprefetch cpuid_fault epb invpcid_single pti ssbd ibrs ibpb stibp tpr_shadow vnmi flexpriority ept vpid fsgsbase tsc_adjust bmi1 hle avx2 smep bmi2 erms invpcid rtm mpx rdseed adx smap clflushopt intel_pt xsaveopt xsavec xgetbv1 xsaves dtherm ida arat pln pts hwp hwp_notify hwp_act_window hwp_epp flush_l1d
using sudo to set CPU frequency to 2.7GHz ...
[sudo] Mot de passe de bourgesl : 
Setting cpu: 0
Setting cpu: 1
Setting cpu: 2
Setting cpu: 3
analyzing CPU 0:
  driver: intel_pstate
  CPUs which run at the same hardware frequency: 0
  CPUs which need to have their frequency coordinated by software: 0
  maximum transition latency:  Cannot determine or is not supported.
  hardware limits: 800 MHz - 3.60 GHz
  available cpufreq governors: performance powersave
  current policy: frequency should be within 2.70 GHz and 2.70 GHz.
                  The governor "performance" may decide which speed to use
                  within this range.
  current CPU frequency: Unable to call hardware
  current CPU frequency: 1.12 GHz (asserted by call to kernel)
  boost state support:
    Supported: yes
    Active: yes
analyzing CPU 1:
  driver: intel_pstate
  CPUs which run at the same hardware frequency: 1
  CPUs which need to have their frequency coordinated by software: 1
  maximum transition latency:  Cannot determine or is not supported.
  hardware limits: 800 MHz - 3.60 GHz
  available cpufreq governors: performance powersave
  current policy: frequency should be within 2.70 GHz and 2.70 GHz.
                  The governor "performance" may decide which speed to use
                  within this range.
  current CPU frequency: Unable to call hardware
  current CPU frequency: 1.07 GHz (asserted by call to kernel)
  boost state support:
    Supported: yes
    Active: yes
analyzing CPU 2:
  driver: intel_pstate
  CPUs which run at the same hardware frequency: 2
  CPUs which need to have their frequency coordinated by software: 2
  maximum transition latency:  Cannot determine or is not supported.
  hardware limits: 800 MHz - 3.60 GHz
  available cpufreq governors: performance powersave
  current policy: frequency should be within 2.70 GHz and 2.70 GHz.
                  The governor "performance" may decide which speed to use
                  within this range.
  current CPU frequency: Unable to call hardware
  current CPU frequency: 1.33 GHz (asserted by call to kernel)
  boost state support:
    Supported: yes
    Active: yes
analyzing CPU 3:
  driver: intel_pstate
  CPUs which run at the same hardware frequency: 3
  CPUs which need to have their frequency coordinated by software: 3
  maximum transition latency:  Cannot determine or is not supported.
  hardware limits: 800 MHz - 3.60 GHz
  available cpufreq governors: performance powersave
  current policy: frequency should be within 2.70 GHz and 2.70 GHz.
                  The governor "performance" may decide which speed to use
                  within this range.
  current CPU frequency: Unable to call hardware
  current CPU frequency: 1.03 GHz (asserted by call to kernel)
  boost state support:
    Supported: yes
    Active: yes
analyzing CPU 0:
perf-bias: 0
analyzing CPU 1:
perf-bias: 0
analyzing CPU 2:
perf-bias: 0
analyzing CPU 3:
perf-bias: 0
JAVA:
java version "1.8.0_192"
Java(TM) SE Runtime Environment (build 1.8.0_192-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.192-b12, mixed mode)
JAVA_OPTS: -Xms1g -Xmx1g -XX:-TieredCompilation -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=300000
Running Bentley basher
Too many loop adjustments for test [50 4  PLATEAU REVERSE___ DPQ_18_11_21] :       3.02 % Confidence:       0.13 σ
Too many loop adjustments for test [50 16  SHUFFLE IDENT_____ DPQ_18_11_21] :       2.87 % Confidence:       2.89 σ
Too many loop adjustments for test [50 16  SHUFFLE DITHER____ DPQ_18_11_21] :       3.93 % Confidence:       0.14 σ
Too many loop adjustments for test [50 16  SHUFFLE DITHER____ DPQ_18_11_21] :       2.92 % Confidence:       0.01 σ
Too many loop adjustments for test [50 64  SHUFFLE IDENT_____ DPQ_18_11_21] :       4.19 % Confidence:       0.21 σ
Too many loop adjustments for test [100 4  _RANDOM IDENT_____ DPQ_18_11_21] :       2.49 % Confidence:       0.00 σ
Too many loop adjustments for test [100 4  _RANDOM REVERSE_FR DPQ_11] :       3.22 % Confidence:       0.00 σ
Too many loop adjustments for test [200 1  SAWTOTH IDENT_____ DPQ_11] :       2.85 % Confidence:       0.08 σ
Too many loop adjustments for test [200 1  _RANDOM IDENT_____ DPQ_11] :       2.90 % Confidence:       0.22 σ
Too many loop adjustments for test [200 1  SAWTOTH REVERSE___ DPQ_11] :       2.61 % Confidence:       0.04 σ
Too many loop adjustments for test [200 1  _RANDOM REVERSE___ DPQ_11] :       3.20 % Confidence:       0.17 σ
Too many loop adjustments for test [200 1  PLATEAU REVERSE___ DPQ_11] :       6.38 % Confidence:       0.14 σ
Too many loop adjustments for test [200 1  SAWTOTH REVERSE_FR DPQ_11] :       2.94 % Confidence:       0.10 σ
Too many loop adjustments for test [200 1  _RANDOM REVERSE_FR DPQ_11] :       2.96 % Confidence:       0.21 σ
Too many loop adjustments for test [200 1  SAWTOTH REVERSE_BA DPQ_11] :       3.23 % Confidence:       0.08 σ
Too many loop adjustments for test [200 1  _RANDOM REVERSE_BA DPQ_11] :       2.82 % Confidence:       0.03 σ
Too many loop adjustments for test [200 1  SAWTOTH SORT______ DPQ_11] :       3.13 % Confidence:       0.04 σ
Too many loop adjustments for test [200 1  _RANDOM SORT______ DPQ_11] :       2.62 % Confidence:       0.13 σ
Too many loop adjustments for test [200 4  _RANDOM REVERSE___ DPQ_11] :       3.05 % Confidence:       1.51 σ
Too many loop adjustments for test [200 4  PLATEAU REVERSE___ DPQ_11] :       3.44 % Confidence:       0.07 σ
Too many loop adjustments for test [200 4  STAGGER SORT______ RADIX] :       3.22 % Confidence:       0.95 σ
Too many loop adjustments for test [200 16  PLATEAU REVERSE___ RADIX] :       3.27 % Confidence:       0.69 σ
Too many loop adjustments for test [200 16  SAWTOTH SORT______ RADIX] :       4.12 % Confidence:       2.21 σ
Too many loop adjustments for test [200 64  PLATEAU REVERSE___ RADIX] :       2.61 % Confidence:       0.93 σ
Too many loop adjustments for test [200 64  STAGGER SORT______ RADIX] :       3.42 % Confidence:       1.51 σ
Too many loop adjustments for test [200 64  _RANDOM SORT______ RADIX] :       5.12 % Confidence:       1.94 σ
Too many loop adjustments for test [200 256  _RANDOM SORT______ RADIX] :       4.60 % Confidence:       0.08 σ
Too many loop adjustments for test [500 4  SAWTOTH REVERSE_FR DPQ_18_11I] :       2.29 % Confidence:       0.00 σ
Too many loop adjustments for test [500 4  SAWTOTH REVERSE_FR DPQ_18_11I] :       2.06 % Confidence:       0.00 σ
Too many loop adjustments for test [500 4  SAWTOTH REVERSE_FR DPQ_18_11I] :       2.32 % Confidence:       0.00 σ
Too many loop adjustments for test [500 4  SAWTOTH REVERSE_FR DPQ_18_11I] :       2.18 % Confidence:       0.00 σ
Too many loop adjustments for test [500 4  SAWTOTH REVERSE_FR DPQ_18_11I] :       2.16 % Confidence:       0.00 σ
Too many loop adjustments for test [500 4  SAWTOTH REVERSE_FR DPQ_18_11I] :       2.22 % Confidence:       0.00 σ
Too many loop adjustments for test [500 4  SAWTOTH REVERSE_FR DPQ_18_11I] :       2.26 % Confidence:       0.00 σ
Too many loop adjustments for test [500 4  SAWTOTH REVERSE_FR DPQ_18_11I] :       2.18 % Confidence:       0.28 σ
Too many loop adjustments for test [500 4  SAWTOTH REVERSE_FR DPQ_18_11I] :       2.32 % Confidence:       0.28 σ
Too many loop adjustments for test [500 16  SHUFFLE REVERSE___ DPQ_18_11I] :       2.53 % Confidence:       2.33 σ
Too many loop adjustments for test [500 16  SHUFFLE REVERSE___ DPQ_18_11I] :       2.39 % Confidence:       2.88 σ
Too many loop adjustments for test [500 64  SHUFFLE REVERSE___ DPQ_18_11I] :       2.44 % Confidence:       1.93 σ
Too many loop adjustments for test [500 64  SHUFFLE REVERSE___ DPQ_18_11I] :       2.51 % Confidence:       0.05 σ
Too many loop adjustments for test [500 256  PLATEAU DITHER____ DPQ_18_11I] :       3.84 % Confidence:       0.00 σ
Too many loop adjustments for test [500 256  PLATEAU DITHER____ DPQ_18_11I] :       3.83 % Confidence:       0.00 σ
Too many loop adjustments for test [500 256  PLATEAU DITHER____ DPQ_18_11I] :       3.48 % Confidence:       0.00 σ
Too many loop adjustments for test [500 256  PLATEAU DITHER____ DPQ_18_11I] :       3.80 % Confidence:       0.00 σ
Too many loop adjustments for test [500 256  PLATEAU DITHER____ DPQ_18_11I] :       3.28 % Confidence:       0.00 σ
Too many loop adjustments for test [500 256  PLATEAU DITHER____ DPQ_18_11I] :       3.23 % Confidence:       0.00 σ
Too many loop adjustments for test [500 256  PLATEAU DITHER____ DPQ_18_11I] :       3.21 % Confidence:       0.00 σ
Too many loop adjustments for test [500 256  PLATEAU DITHER____ DPQ_18_11I] :       3.90 % Confidence:       0.00 σ
Too many loop adjustments for test [500 256  PLATEAU DITHER____ DPQ_18_11I] :       2.50 % Confidence:       0.00 σ
Too many loop adjustments for test [500 256  PLATEAU DITHER____ DPQ_18_11I] :       3.27 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 4  SAWTOTH REVERSE_FR DPQ_18_11_21] :       2.17 % Confidence:       4.20 σ
Too many loop adjustments for test [1000 4  SAWTOTH REVERSE_FR DPQ_18_11P] :      16.06 % Confidence:      51.30 σ
Too many loop adjustments for test [1000 4  STAGGER SORT______ DPQ_18_11I] :       2.41 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 4  STAGGER SORT______ DPQ_18_11I] :       2.30 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 4  STAGGER SORT______ DPQ_18_11I] :       2.39 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 4  STAGGER SORT______ DPQ_18_11I] :       2.29 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 4  STAGGER SORT______ DPQ_18_11I] :       2.33 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 4  STAGGER SORT______ DPQ_18_11I] :       2.27 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 4  STAGGER SORT______ DPQ_18_11I] :       2.39 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 4  STAGGER SORT______ DPQ_18_11I] :       2.21 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 4  STAGGER SORT______ DPQ_18_11I] :       2.33 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 4  _RANDOM DITHER____ DPQ_18_11P] :      42.46 % Confidence:     182.80 σ
Too many loop adjustments for test [1000 16  _RANDOM IDENT_____ DPQ_18_11P] :       2.25 % Confidence:       0.42 σ
Too many loop adjustments for test [1000 16  _RANDOM IDENT_____ DPQ_18_11P] :       3.53 % Confidence:       1.36 σ
Too many loop adjustments for test [1000 16  _RANDOM REVERSE___ DPQ_18_11P] :       2.30 % Confidence:       0.41 σ
Too many loop adjustments for test [1000 16  _RANDOM REVERSE___ DPQ_18_11P] :       2.49 % Confidence:       0.05 σ
Too many loop adjustments for test [1000 16  SHUFFLE REVERSE___ DPQ_18_11I] :       2.95 % Confidence:       0.28 σ
Too many loop adjustments for test [1000 16  _RANDOM REVERSE_FR DPQ_18_11P] :       3.14 % Confidence:       0.25 σ
Too many loop adjustments for test [1000 16  _RANDOM REVERSE_BA DPQ_18_11P] :       3.75 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 16  _RANDOM REVERSE_BA DPQ_18_11P] :      44.27 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 16  _RANDOM REVERSE_BA DPQ_18_11P] :       2.17 % Confidence:       0.12 σ
Too many loop adjustments for test [1000 16  _RANDOM DITHER____ DPQ_18_11P] :       2.80 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 16  _RANDOM DITHER____ DPQ_18_11P] :       3.00 % Confidence:       0.48 σ
Too many loop adjustments for test [1000 16  _RANDOM DITHER____ DPQ_18_11P] :       2.55 % Confidence:       0.48 σ
Too many loop adjustments for test [1000 16  _RANDOM DITHER____ DPQ_18_11P] :       2.58 % Confidence:       0.07 σ
Too many loop adjustments for test [1000 16  _RANDOM DITHER____ DPQ_18_11P] :       2.76 % Confidence:       0.07 σ
Too many loop adjustments for test [1000 64  _RANDOM IDENT_____ DPQ_18_11_21] :      25.93 % Confidence:       6.45 σ
Too many loop adjustments for test [1000 64  _RANDOM REVERSE___ DPQ_11] :       2.39 % Confidence:       0.15 σ
Too many loop adjustments for test [1000 64  _RANDOM REVERSE___ DPQ_18_11_21] :      23.37 % Confidence:       0.19 σ
Too many loop adjustments for test [1000 64  _RANDOM REVERSE___ DPQ_18_11_21] :       7.01 % Confidence:       0.19 σ
Too many loop adjustments for test [1000 64  _RANDOM REVERSE___ DPQ_18_11_21] :       2.75 % Confidence:       0.19 σ
Too many loop adjustments for test [1000 64  _RANDOM REVERSE___ DPQ_18_11I] :       2.89 % Confidence:       0.27 σ
Too many loop adjustments for test [1000 64  _RANDOM REVERSE___ DPQ_18_11P] :       2.26 % Confidence:       0.14 σ
Too many loop adjustments for test [1000 64  _RANDOM REVERSE_FR DPQ_11] :       2.56 % Confidence:       0.21 σ
Too many loop adjustments for test [1000 64  _RANDOM REVERSE_FR DPQ_18_11_21] :       2.25 % Confidence:       0.01 σ
Too many loop adjustments for test [1000 64  _RANDOM REVERSE_FR DPQ_18_11_21] :      27.24 % Confidence:       0.35 σ
Too many loop adjustments for test [1000 64  _RANDOM REVERSE_FR DPQ_18_11I] :       2.04 % Confidence:       0.57 σ
Too many loop adjustments for test [1000 64  _RANDOM REVERSE_FR DPQ_18_11P] :       2.65 % Confidence:       0.17 σ
Too many loop adjustments for test [1000 64  STAGGER SORT______ DPQ_18_11I] :       2.39 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 64  STAGGER SORT______ DPQ_18_11I] :       2.37 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 64  STAGGER SORT______ DPQ_18_11I] :       2.30 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 64  STAGGER SORT______ DPQ_18_11I] :       2.31 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 64  STAGGER SORT______ DPQ_18_11I] :       2.38 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 64  STAGGER SORT______ DPQ_18_11I] :       2.18 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 64  STAGGER SORT______ DPQ_18_11I] :       2.35 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 64  STAGGER SORT______ DPQ_18_11I] :       2.37 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 64  STAGGER SORT______ DPQ_18_11I] :       2.39 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 64  STAGGER SORT______ DPQ_18_11I] :       2.16 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 64  _RANDOM DITHER____ DPQ_18_11P] :       2.66 % Confidence:       0.52 σ
Too many loop adjustments for test [1000 256  _RANDOM IDENT_____ DPQ_18_11I] :       2.45 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 256  STAGGER REVERSE___ DPQ_11] :       3.75 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 256  STAGGER REVERSE___ DPQ_11] :       3.65 % Confidence:       7.93 σ
Too many loop adjustments for test [1000 256  STAGGER REVERSE___ DPQ_11] :       3.37 % Confidence:       4.12 σ
Too many loop adjustments for test [1000 256  STAGGER REVERSE___ DPQ_11] :       2.66 % Confidence:       8.54 σ
Too many loop adjustments for test [1000 256  STAGGER REVERSE___ DPQ_11] :       3.00 % Confidence:      11.53 σ
Too many loop adjustments for test [1000 256  _RANDOM REVERSE___ DPQ_18_11I] :       2.14 % Confidence:       0.23 σ
Too many loop adjustments for test [1000 256  _RANDOM REVERSE___ DPQ_18_11I] :       2.57 % Confidence:       1.29 σ
Too many loop adjustments for test [1000 256  _RANDOM REVERSE_FR DPQ_18_11I] :       3.36 % Confidence:       0.03 σ
Too many loop adjustments for test [1000 256  _RANDOM REVERSE_FR DPQ_18_11I] :       2.30 % Confidence:       0.03 σ
Too many loop adjustments for test [1000 256  _RANDOM REVERSE_FR DPQ_18_11P] :       2.71 % Confidence:       0.40 σ
Too many loop adjustments for test [1000 256  _RANDOM REVERSE_BA DPQ_18_11_21] :       2.17 % Confidence:       0.09 σ
Too many loop adjustments for test [1000 256  _RANDOM REVERSE_BA DPQ_18_11I] :       2.14 % Confidence:       0.06 σ
Too many loop adjustments for test [1000 256  _RANDOM REVERSE_BA DPQ_18_11I] :       2.70 % Confidence:       0.36 σ
Too many loop adjustments for test [1000 256  _RANDOM SORT______ DPQ_18_11I] :       2.37 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 256  _RANDOM SORT______ DPQ_18_11I] :       2.29 % Confidence:       0.54 σ
Too many loop adjustments for test [1000 256  _RANDOM SORT______ DPQ_18_11I] :       2.32 % Confidence:       0.54 σ
Too many loop adjustments for test [1000 256  _RANDOM SORT______ DPQ_18_11I] :       2.55 % Confidence:       0.36 σ
Too many loop adjustments for test [1000 256  _RANDOM DITHER____ DPQ_18_11I] :       2.19 % Confidence:       4.25 σ
Too many loop adjustments for test [1000 256  _RANDOM DITHER____ DPQ_18_11I] :       2.52 % Confidence:       0.22 σ
Too many loop adjustments for test [1000 256  SHUFFLE DITHER____ MARLIN] :       4.46 % Confidence:      13.17 σ
Too many loop adjustments for test [1000 1024  _RANDOM REVERSE___ DPQ_18_11I] :       2.43 % Confidence:       0.00 σ
Too many loop adjustments for test [1000 1024  _RANDOM REVERSE___ DPQ_18_11I] :       2.62 % Confidence:       3.11 σ
Too many loop adjustments for test [1000 1024  _RANDOM REVERSE_FR DPQ_18_11I] :       2.34 % Confidence:       0.50 σ
Too many loop adjustments for test [1000 1024  _RANDOM REVERSE_FR DPQ_18_11P] :       2.46 % Confidence:       0.49 σ
Too many loop adjustments for test [1000 1024  _RANDOM REVERSE_BA DPQ_18_11I] :       2.41 % Confidence:       0.05 σ
Too many loop adjustments for test [1000 1024  _RANDOM REVERSE_BA DPQ_18_11I] :       2.95 % Confidence:       0.05 σ
Too many loop adjustments for test [1000 1024  _RANDOM REVERSE_BA DPQ_18_11I] :       2.54 % Confidence:       0.05 σ
Too many loop adjustments for test [1000 1024  _RANDOM REVERSE_BA DPQ_18_11I] :       2.67 % Confidence:       0.05 σ
Too many loop adjustments for test [1000 1024  _RANDOM REVERSE_BA DPQ_18_11I] :       3.22 % Confidence:       0.05 σ
Too many loop adjustments for test [1000 1024  _RANDOM SORT______ DPQ_18_11I] :       2.14 % Confidence:       0.42 σ
Too many loop adjustments for test [1000 1024  _RANDOM SORT______ DPQ_18_11I] :       2.43 % Confidence:       1.22 σ
Too many loop adjustments for test [1000 1024  _RANDOM SORT______ DPQ_18_11I] :       2.19 % Confidence:       1.22 σ
Too many loop adjustments for test [1000 1024  _RANDOM DITHER____ DPQ_18_11I] :       2.47 % Confidence:       0.11 σ
Too many loop adjustments for test [1000 1024  _RANDOM DITHER____ DPQ_18_11I] :       2.31 % Confidence:       0.77 σ
Too many loop adjustments for test [1000 1024  SHUFFLE DITHER____ DPQ_18_11I] :       2.21 % Confidence:       0.20 σ
Too many loop adjustments for test [2000 1  STAGGER SORT______ DPQ_18_11I] :       2.36 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1  STAGGER SORT______ DPQ_18_11I] :       2.43 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1  STAGGER SORT______ DPQ_18_11I] :       2.42 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1  STAGGER SORT______ DPQ_18_11I] :       2.43 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1  STAGGER SORT______ DPQ_18_11I] :       2.44 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1  STAGGER SORT______ DPQ_18_11I] :       2.35 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1  STAGGER SORT______ DPQ_18_11I] :       2.30 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1  STAGGER SORT______ DPQ_18_11I] :       2.34 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1  STAGGER SORT______ DPQ_18_11I] :       2.39 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1  STAGGER SORT______ DPQ_18_11I] :       2.40 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       3.82 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       3.68 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       3.10 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       4.65 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       3.67 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       3.56 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       4.91 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       4.08 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       2.87 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       2.94 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  _RANDOM IDENT_____ DPQ_18_11_21] :       3.24 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  _RANDOM IDENT_____ DPQ_18_11_21] :       2.71 % Confidence:       0.67 σ
Too many loop adjustments for test [2000 4  _RANDOM IDENT_____ DPQ_18_11_21] :       3.05 % Confidence:       0.67 σ
Too many loop adjustments for test [2000 4  _RANDOM IDENT_____ DPQ_18_11_21] :       2.98 % Confidence:       0.27 σ
Too many loop adjustments for test [2000 4  _RANDOM IDENT_____ DPQ_18_11I] :       2.99 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  _RANDOM IDENT_____ DPQ_18_11I] :       3.43 % Confidence:       2.56 σ
Too many loop adjustments for test [2000 4  _RANDOM IDENT_____ DPQ_18_11I] :       3.10 % Confidence:       2.56 σ
Too many loop adjustments for test [2000 4  _RANDOM IDENT_____ DPQ_18_11I] :       3.18 % Confidence:       0.19 σ
Too many loop adjustments for test [2000 4  _RANDOM IDENT_____ DPQ_18_11P] :       2.37 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  _RANDOM IDENT_____ DPQ_18_11P] :       5.24 % Confidence:       0.48 σ
Too many loop adjustments for test [2000 4  _RANDOM IDENT_____ DPQ_18_11P] :       3.61 % Confidence:       2.25 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE___ DPQ_18_11_21] :       2.70 % Confidence:       3.18 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE___ DPQ_18_11_21] :       3.09 % Confidence:       0.28 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE___ DPQ_18_11I] :       3.22 % Confidence:       1.01 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE___ DPQ_18_11I] :       2.32 % Confidence:       0.58 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE___ DPQ_18_11P] :       2.50 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE___ DPQ_18_11P] :       3.24 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE___ DPQ_18_11P] :       3.18 % Confidence:       0.11 σ
Too many loop adjustments for test [2000 4  SHUFFLE REVERSE___ DPQ_18_11I] :       2.70 % Confidence:       0.27 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE_FR DPQ_18_11_21] :       3.42 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE_FR DPQ_18_11_21] :       2.75 % Confidence:       0.20 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE_FR DPQ_18_11I] :       3.67 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE_FR DPQ_18_11I] :       3.22 % Confidence:       0.98 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE_FR DPQ_18_11P] :       2.09 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE_FR DPQ_18_11P] :      40.90 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE_FR DPQ_18_11P] :       3.64 % Confidence:       0.01 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE_FR DPQ_18_11P] :       3.41 % Confidence:       0.72 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE_BA DPQ_18_11P] :       3.24 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE_BA DPQ_18_11P] :       3.84 % Confidence:       0.35 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE_BA DPQ_18_11P] :       3.85 % Confidence:       0.35 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE_BA DPQ_18_11P] :       3.22 % Confidence:       0.69 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE_BA DPQ_18_11P] :       3.45 % Confidence:       3.00 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE_BA DPQ_18_11P] :       3.80 % Confidence:       3.00 σ
Too many loop adjustments for test [2000 4  _RANDOM REVERSE_BA DPQ_18_11P] :       3.63 % Confidence:       3.00 σ
Too many loop adjustments for test [2000 4  _RANDOM DITHER____ DPQ_18_11_21] :       2.66 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  _RANDOM DITHER____ DPQ_18_11_21] :       2.43 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  _RANDOM DITHER____ DPQ_18_11_21] :       2.83 % Confidence:       0.32 σ
Too many loop adjustments for test [2000 4  _RANDOM DITHER____ DPQ_18_11_21] :       3.06 % Confidence:       0.32 σ
Too many loop adjustments for test [2000 4  _RANDOM DITHER____ DPQ_18_11_21] :       4.27 % Confidence:       0.32 σ
Too many loop adjustments for test [2000 4  _RANDOM DITHER____ DPQ_18_11_21] :       2.15 % Confidence:       0.32 σ
Too many loop adjustments for test [2000 4  _RANDOM DITHER____ DPQ_18_11_21] :       2.54 % Confidence:       0.32 σ
Too many loop adjustments for test [2000 4  _RANDOM DITHER____ DPQ_18_11_21] :       2.03 % Confidence:       0.16 σ
Too many loop adjustments for test [2000 4  _RANDOM DITHER____ DPQ_18_11I] :       3.80 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  _RANDOM DITHER____ DPQ_18_11I] :       2.94 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 4  _RANDOM DITHER____ DPQ_18_11I] :       2.59 % Confidence:       2.50 σ
Too many loop adjustments for test [2000 4  _RANDOM DITHER____ DPQ_18_11I] :       4.32 % Confidence:       0.63 σ
Too many loop adjustments for test [2000 4  _RANDOM DITHER____ DPQ_18_11I] :       2.53 % Confidence:       0.63 σ
Too many loop adjustments for test [2000 16  _RANDOM IDENT_____ DPQ_11] :       2.94 % Confidence:       0.25 σ
Too many loop adjustments for test [2000 16  _RANDOM IDENT_____ DPQ_18_11I] :       3.05 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM IDENT_____ DPQ_18_11I] :       4.39 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM IDENT_____ DPQ_18_11I] :       2.55 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM IDENT_____ DPQ_18_11I] :       3.67 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM IDENT_____ DPQ_18_11I] :       3.21 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM IDENT_____ DPQ_18_11I] :       4.56 % Confidence:       0.08 σ
Too many loop adjustments for test [2000 16  _RANDOM IDENT_____ DPQ_18_11I] :       2.51 % Confidence:       0.08 σ
Too many loop adjustments for test [2000 16  _RANDOM REVERSE___ DPQ_18_11I] :       2.36 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM REVERSE___ DPQ_18_11I] :       2.63 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM REVERSE___ DPQ_18_11I] :       3.37 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM REVERSE___ DPQ_18_11I] :       2.51 % Confidence:       0.17 σ
Too many loop adjustments for test [2000 16  _RANDOM REVERSE___ DPQ_18_11I] :       3.38 % Confidence:       0.17 σ
Too many loop adjustments for test [2000 16  SHUFFLE REVERSE___ DPQ_18_11I] :       2.21 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM REVERSE_FR DPQ_18_11I] :       3.74 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM REVERSE_FR DPQ_18_11I] :       2.31 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM REVERSE_FR DPQ_18_11I] :       2.97 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM REVERSE_FR DPQ_18_11I] :       2.26 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM REVERSE_FR DPQ_18_11I] :       3.51 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM REVERSE_FR DPQ_18_11I] :       2.26 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM REVERSE_FR DPQ_18_11I] :       3.09 % Confidence:       0.13 σ
Too many loop adjustments for test [2000 16  SHUFFLE REVERSE_FR MARLIN] :       1.26 % Confidence:      10.79 σ
Too many loop adjustments for test [2000 16  _RANDOM REVERSE_BA DPQ_18_11I] :       2.07 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM REVERSE_BA DPQ_18_11I] :       3.59 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM REVERSE_BA DPQ_18_11I] :       4.08 % Confidence:       0.06 σ
Too many loop adjustments for test [2000 16  _RANDOM REVERSE_BA DPQ_18_11I] :       4.43 % Confidence:       0.56 σ
Too many loop adjustments for test [2000 16  _RANDOM REVERSE_BA DPQ_18_11I] :       2.97 % Confidence:       0.56 σ
Too many loop adjustments for test [2000 16  _RANDOM REVERSE_BA DPQ_18_11I] :       4.03 % Confidence:       0.27 σ
Too many loop adjustments for test [2000 16  _RANDOM DITHER____ DPQ_18_11I] :       2.75 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM DITHER____ DPQ_18_11I] :       2.85 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM DITHER____ DPQ_18_11I] :       2.25 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 16  _RANDOM DITHER____ DPQ_18_11I] :       2.17 % Confidence:       0.43 σ
Too many loop adjustments for test [2000 64  _RANDOM REVERSE_BA DPQ_18_11I] :      14.29 % Confidence:      13.07 σ
Too many loop adjustments for test [2000 256  SAWTOTH SORT______ DPQ_18_11I] :       2.57 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 256  SAWTOTH SORT______ DPQ_18_11I] :       2.61 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 256  SAWTOTH SORT______ DPQ_18_11I] :       2.46 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 256  SAWTOTH SORT______ DPQ_18_11I] :       2.56 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 256  SAWTOTH SORT______ DPQ_18_11I] :       2.57 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 256  SAWTOTH SORT______ DPQ_18_11I] :       2.56 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 256  SAWTOTH SORT______ DPQ_18_11I] :       2.64 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 256  SAWTOTH SORT______ DPQ_18_11I] :       2.48 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 256  SAWTOTH SORT______ DPQ_18_11I] :       2.55 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 256  SAWTOTH SORT______ DPQ_18_11I] :       2.54 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 256  _RANDOM SORT______ DPQ_18_11I] :       2.49 % Confidence:       0.28 σ
Too many loop adjustments for test [2000 256  _RANDOM SORT______ DPQ_18_11I] :       2.64 % Confidence:       1.09 σ
Too many loop adjustments for test [2000 256  _RANDOM SORT______ DPQ_18_11I] :       2.55 % Confidence:       1.09 σ
Too many loop adjustments for test [2000 256  _RANDOM SORT______ DPQ_18_11I] :       2.55 % Confidence:       1.09 σ
Too many loop adjustments for test [2000 1024  SAWTOTH REVERSE___ DPQ_18_11I] :       2.42 % Confidence:       0.02 σ
Too many loop adjustments for test [2000 1024  SAWTOTH REVERSE___ DPQ_18_11I] :       2.71 % Confidence:       0.95 σ
Too many loop adjustments for test [2000 1024  SAWTOTH REVERSE___ DPQ_18_11I] :       2.59 % Confidence:       0.95 σ
Too many loop adjustments for test [2000 1024  SAWTOTH SORT______ DPQ_18_11I] :       2.45 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  SAWTOTH SORT______ DPQ_18_11I] :       2.30 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  SAWTOTH SORT______ DPQ_18_11I] :       2.43 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  SAWTOTH SORT______ DPQ_18_11I] :       2.43 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  SAWTOTH SORT______ DPQ_18_11I] :       2.24 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  SAWTOTH SORT______ DPQ_18_11I] :       2.34 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  SAWTOTH SORT______ DPQ_18_11I] :       2.43 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  SAWTOTH SORT______ DPQ_18_11I] :       2.36 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  SAWTOTH SORT______ DPQ_18_11I] :       2.35 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  SAWTOTH SORT______ DPQ_18_11I] :       2.26 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  _RANDOM SORT______ DPQ_18_11I] :       2.51 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  _RANDOM SORT______ DPQ_18_11I] :       2.54 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  _RANDOM SORT______ DPQ_18_11I] :       2.52 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  _RANDOM SORT______ DPQ_18_11I] :       2.59 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  _RANDOM SORT______ DPQ_18_11I] :       2.54 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  _RANDOM SORT______ DPQ_18_11I] :       2.57 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  _RANDOM SORT______ DPQ_18_11I] :       2.36 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  _RANDOM SORT______ DPQ_18_11I] :       2.28 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  _RANDOM SORT______ DPQ_18_11I] :       2.40 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  _RANDOM SORT______ DPQ_18_11I] :       2.09 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  SAWTOTH DITHER____ DPQ_18_11I] :       3.00 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  SAWTOTH DITHER____ DPQ_18_11I] :       3.03 % Confidence:       0.71 σ
Too many loop adjustments for test [2000 1024  SAWTOTH DITHER____ DPQ_18_11I] :       2.98 % Confidence:       0.18 σ
Too many loop adjustments for test [2000 1024  SAWTOTH DITHER____ DPQ_18_11I] :       2.05 % Confidence:       0.18 σ
Too many loop adjustments for test [2000 1024  SAWTOTH DITHER____ DPQ_18_11I] :       3.24 % Confidence:       0.18 σ
Too many loop adjustments for test [2000 1024  SAWTOTH DITHER____ DPQ_18_11I] :       3.03 % Confidence:       0.19 σ
Too many loop adjustments for test [2000 1024  SAWTOTH DITHER____ DPQ_18_11I] :       2.11 % Confidence:       0.19 σ
Too many loop adjustments for test [2000 1024  PLATEAU DITHER____ DPQ_18_11I] :       2.57 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  PLATEAU DITHER____ DPQ_18_11I] :       2.48 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  PLATEAU DITHER____ DPQ_18_11I] :       2.57 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  PLATEAU DITHER____ DPQ_18_11I] :       2.50 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  PLATEAU DITHER____ DPQ_18_11I] :       2.56 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  PLATEAU DITHER____ DPQ_18_11I] :       2.54 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  PLATEAU DITHER____ DPQ_18_11I] :       2.56 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  PLATEAU DITHER____ DPQ_18_11I] :       2.46 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  PLATEAU DITHER____ DPQ_18_11I] :       2.57 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  PLATEAU DITHER____ DPQ_18_11I] :       2.40 % Confidence:       0.00 σ
Too many loop adjustments for test [2000 1024  SHUFFLE DITHER____ DPQ_18_11I] :       2.18 % Confidence:       0.15 σ
Too many loop adjustments for test [2000 1024  SHUFFLE DITHER____ DPQ_18_11I] :       2.55 % Confidence:       0.15 σ
Too many loop adjustments for test [2000 1024  SHUFFLE DITHER____ DPQ_18_11I] :       2.24 % Confidence:       0.38 σ
Too many loop adjustments for test [5000 4  _RANDOM REVERSE_BA DPQ_11] :       2.08 % Confidence:       0.25 σ
Too many loop adjustments for test [5000 4  _RANDOM REVERSE_BA DPQ_18_11I] :       2.23 % Confidence:       0.00 σ
Too many loop adjustments for test [5000 16  SHUFFLE IDENT_____ DPQ_18_11I] :       2.64 % Confidence:       3.92 σ
Too many loop adjustments for test [5000 1024  SHUFFLE DITHER____ DPQ_18_11I] :       2.10 % Confidence:       0.53 σ
Too many loop adjustments for test [5000 1024  SHUFFLE DITHER____ DPQ_18_11I] :       2.59 % Confidence:       0.53 σ
Too many loop adjustments for test [5000 1024  SHUFFLE DITHER____ DPQ_18_11I] :       2.36 % Confidence:       1.42 σ
Too many loop adjustments for test [5000 4096  SHUFFLE DITHER____ DPQ_18_11I] :       2.17 % Confidence:       0.00 σ
Too many loop adjustments for test [5000 4096  SHUFFLE DITHER____ DPQ_18_11I] :       2.44 % Confidence:       6.52 σ
Too many loop adjustments for test [10000 1  SAWTOTH IDENT_____ DPQ_11] :       2.73 % Confidence:       3.11 σ
Too many loop adjustments for test [10000 1  SAWTOTH IDENT_____ DPQ_11] :       3.10 % Confidence:       3.11 σ
Too many loop adjustments for test [10000 1  SAWTOTH IDENT_____ DPQ_11] :       2.57 % Confidence:       0.40 σ
Too many loop adjustments for test [10000 1  SAWTOTH IDENT_____ DPQ_11] :       2.98 % Confidence:       0.40 σ
Too many loop adjustments for test [10000 1  _RANDOM IDENT_____ DPQ_11] :       2.73 % Confidence:       1.46 σ
Too many loop adjustments for test [10000 1  _RANDOM IDENT_____ DPQ_11] :       2.44 % Confidence:       1.46 σ
Too many loop adjustments for test [10000 1  _RANDOM IDENT_____ DPQ_11] :       2.83 % Confidence:       1.67 σ
Too many loop adjustments for test [10000 1  _RANDOM IDENT_____ DPQ_11] :       3.00 % Confidence:       1.67 σ
Too many loop adjustments for test [10000 1  SAWTOTH REVERSE___ DPQ_11] :       2.77 % Confidence:       2.21 σ
Too many loop adjustments for test [10000 1  SAWTOTH REVERSE___ DPQ_11] :       3.13 % Confidence:       2.21 σ
Too many loop adjustments for test [10000 1  SAWTOTH REVERSE___ DPQ_11] :       2.64 % Confidence:       0.47 σ
Too many loop adjustments for test [10000 1  SAWTOTH REVERSE___ DPQ_11] :       3.11 % Confidence:       0.47 σ
Too many loop adjustments for test [10000 1  _RANDOM REVERSE___ DPQ_11] :       2.88 % Confidence:       1.78 σ
Too many loop adjustments for test [10000 1  _RANDOM REVERSE___ DPQ_11] :       3.01 % Confidence:       1.78 σ
Too many loop adjustments for test [10000 1  _RANDOM REVERSE___ DPQ_11] :       2.68 % Confidence:       0.59 σ
Too many loop adjustments for test [10000 1  _RANDOM REVERSE___ DPQ_11] :       2.94 % Confidence:       0.59 σ
Too many loop adjustments for test [10000 1  SAWTOTH REVERSE_FR DPQ_11] :       2.72 % Confidence:       2.20 σ
Too many loop adjustments for test [10000 1  SAWTOTH REVERSE_FR DPQ_11] :       2.70 % Confidence:       2.20 σ
Too many loop adjustments for test [10000 1  SAWTOTH REVERSE_FR DPQ_11] :       2.84 % Confidence:       2.23 σ
Too many loop adjustments for test [10000 1  SAWTOTH REVERSE_FR DPQ_11] :       2.72 % Confidence:       2.23 σ
Too many loop adjustments for test [10000 1  _RANDOM REVERSE_FR DPQ_11] :       2.85 % Confidence:       2.00 σ
Too many loop adjustments for test [10000 1  _RANDOM REVERSE_FR DPQ_11] :       3.10 % Confidence:       2.00 σ
Too many loop adjustments for test [10000 1  _RANDOM REVERSE_FR DPQ_11] :       2.82 % Confidence:       3.53 σ
Too many loop adjustments for test [10000 1  _RANDOM REVERSE_FR DPQ_11] :       2.99 % Confidence:       3.53 σ
Too many loop adjustments for test [10000 1  SAWTOTH REVERSE_BA DPQ_11] :       2.71 % Confidence:       1.68 σ
Too many loop adjustments for test [10000 1  SAWTOTH REVERSE_BA DPQ_11] :       2.97 % Confidence:       1.68 σ
Too many loop adjustments for test [10000 1  SAWTOTH REVERSE_BA DPQ_11] :       2.78 % Confidence:       0.48 σ
Too many loop adjustments for test [10000 1  SAWTOTH REVERSE_BA DPQ_11] :       2.44 % Confidence:       0.48 σ
Too many loop adjustments for test [10000 1  _RANDOM REVERSE_BA DPQ_11] :       2.76 % Confidence:       1.59 σ
Too many loop adjustments for test [10000 1  _RANDOM REVERSE_BA DPQ_11] :       3.13 % Confidence:       1.59 σ
Too many loop adjustments for test [10000 1  _RANDOM REVERSE_BA DPQ_11] :       2.78 % Confidence:       1.42 σ
Too many loop adjustments for test [10000 1  _RANDOM REVERSE_BA DPQ_11] :       3.09 % Confidence:       1.42 σ
Too many loop adjustments for test [10000 1  SAWTOTH SORT______ DPQ_11] :       2.68 % Confidence:       1.91 σ
Too many loop adjustments for test [10000 1  SAWTOTH SORT______ DPQ_11] :       3.20 % Confidence:       1.91 σ
Too many loop adjustments for test [10000 1  SAWTOTH SORT______ DPQ_11] :       2.72 % Confidence:       0.10 σ
Too many loop adjustments for test [10000 1  SAWTOTH SORT______ DPQ_11] :       2.82 % Confidence:       0.10 σ
Too many loop adjustments for test [10000 1  _RANDOM SORT______ DPQ_11] :       2.76 % Confidence:       1.85 σ
Too many loop adjustments for test [10000 1  _RANDOM SORT______ DPQ_11] :       2.90 % Confidence:       1.85 σ
Too many loop adjustments for test [10000 1  _RANDOM SORT______ DPQ_11] :       2.64 % Confidence:       1.37 σ
Too many loop adjustments for test [10000 1  _RANDOM SORT______ DPQ_11] :       2.84 % Confidence:       1.37 σ
Too many loop adjustments for test [10000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       4.60 % Confidence:       0.95 σ
Too many loop adjustments for test [10000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       5.11 % Confidence:       0.95 σ
Too many loop adjustments for test [10000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       5.24 % Confidence:       0.95 σ
Too many loop adjustments for test [10000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       4.89 % Confidence:       0.95 σ
Too many loop adjustments for test [10000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       4.78 % Confidence:       0.95 σ
Too many loop adjustments for test [10000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       4.95 % Confidence:       0.95 σ
Too many loop adjustments for test [10000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       4.97 % Confidence:       0.95 σ
Too many loop adjustments for test [10000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       5.39 % Confidence:       0.95 σ
Too many loop adjustments for test [10000 4  SAWTOTH IDENT_____ DPQ_18_11I] :       4.59 % Confidence:       0.95 σ
Too many loop adjustments for test [10000 16  SHUFFLE REVERSE_FR MARLIN] :       3.76 % Confidence:       1.92 σ
Too many loop adjustments for test [10000 16  SHUFFLE REVERSE_BA MARLIN] :       0.35 % Confidence:      21.23 σ
Too many loop adjustments for test [10000 1024  SHUFFLE DITHER____ DPQ_18_11I] :       2.32 % Confidence:       0.36 σ
Too many loop adjustments for test [10000 4096  SHUFFLE DITHER____ DPQ_18_11I] :       2.66 % Confidence:       0.00 σ
Too many loop adjustments for test [10000 4096  SHUFFLE DITHER____ DPQ_18_11I] :       2.53 % Confidence:       1.05 σ
Too many loop adjustments for test [10000 4096  SHUFFLE DITHER____ DPQ_18_11I] :       2.38 % Confidence:       1.05 σ
Too many loop adjustments for test [10000 4096  SHUFFLE DITHER____ DPQ_18_11I] :       2.08 % Confidence:       1.05 σ
Too many loop adjustments for test [10000 4096  SHUFFLE DITHER____ DPQ_18_11I] :       2.27 % Confidence:       0.46 σ
Too many loop adjustments for test [10000 16384  SAWTOTH DITHER____ DPQ_18_11I] :       2.20 % Confidence:       0.25 σ
Too many loop adjustments for test [10000 16384  PLATEAU DITHER____ DPQ_18_11I] :       2.13 % Confidence:       0.02 σ
Too many loop adjustments for test [10000 16384  PLATEAU DITHER____ DPQ_18_11I] :       2.08 % Confidence:       0.02 σ
Too many loop adjustments for test [10000 16384  SHUFFLE DITHER____ DPQ_18_11I] :       2.60 % Confidence:       0.16 σ
Too many loop adjustments for test [10000 16384  SHUFFLE DITHER____ DPQ_18_11I] :       2.44 % Confidence:       0.16 σ
done.

