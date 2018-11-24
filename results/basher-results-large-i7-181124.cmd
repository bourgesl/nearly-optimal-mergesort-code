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
Vitesse du processeur en MHz :          2653.888
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
  current CPU frequency: 2.70 GHz (asserted by call to kernel)
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
  current CPU frequency: 2.70 GHz (asserted by call to kernel)
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
  current CPU frequency: 2.70 GHz (asserted by call to kernel)
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
  current CPU frequency: 2.70 GHz (asserted by call to kernel)
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
Too many loop adjustments for test [50000 1  STAGGER DITHER____ DPQ_18_11_21] :       3.74 % Confidence:       0.00 σ
Too many loop adjustments for test [50000 1  STAGGER DITHER____ DPQ_18_11_21] :       3.85 % Confidence:       0.00 σ
Too many loop adjustments for test [50000 1  STAGGER DITHER____ DPQ_18_11_21] :       3.48 % Confidence:       0.00 σ
Too many loop adjustments for test [50000 1  STAGGER DITHER____ DPQ_18_11_21] :       3.36 % Confidence:       0.79 σ
Too many loop adjustments for test [50000 1  STAGGER DITHER____ DPQ_18_11_21] :       3.27 % Confidence:       0.79 σ
Too many loop adjustments for test [50000 1  STAGGER DITHER____ DPQ_18_11_21] :       3.77 % Confidence:       0.79 σ
Too many loop adjustments for test [50000 1  STAGGER DITHER____ DPQ_18_11_21] :       3.69 % Confidence:       0.79 σ
Too many loop adjustments for test [50000 1  STAGGER DITHER____ DPQ_18_11_21] :       3.49 % Confidence:       0.79 σ
Too many loop adjustments for test [50000 1  STAGGER DITHER____ DPQ_18_11_21] :       3.41 % Confidence:       0.79 σ
Too many loop adjustments for test [50000 4  SHUFFLE IDENT_____ DPQ_18_11_21] :       2.58 % Confidence:       2.45 σ
Too many loop adjustments for test [50000 4  SHUFFLE IDENT_____ DPQ_18_11_21] :       3.11 % Confidence:       0.88 σ
Too many loop adjustments for test [50000 4  SHUFFLE IDENT_____ DPQ_18_11_21] :       2.87 % Confidence:       2.14 σ
Too many loop adjustments for test [50000 4  SHUFFLE REVERSE___ DPQ_18_11_21] :       3.63 % Confidence:       0.91 σ
Too many loop adjustments for test [50000 4  SHUFFLE REVERSE___ DPQ_18_11_21] :       3.24 % Confidence:       0.52 σ
Too many loop adjustments for test [50000 4  SHUFFLE REVERSE___ DPQ_18_11_21] :       4.67 % Confidence:       0.52 σ
Too many loop adjustments for test [50000 4  SHUFFLE REVERSE___ DPQ_18_11_21] :       2.43 % Confidence:       0.01 σ
Too many loop adjustments for test [50000 4  SHUFFLE REVERSE_FR DPQ_18_11_21] :       4.22 % Confidence:       0.00 σ
Too many loop adjustments for test [50000 4  SHUFFLE REVERSE_FR DPQ_18_11_21] :       3.93 % Confidence:       0.22 σ
Too many loop adjustments for test [50000 4  SHUFFLE REVERSE_FR DPQ_18_11_21] :       4.65 % Confidence:       0.22 σ
Too many loop adjustments for test [50000 4  SHUFFLE REVERSE_FR DPQ_18_11_21] :       3.52 % Confidence:       0.22 σ
Too many loop adjustments for test [50000 4  SHUFFLE REVERSE_FR DPQ_18_11_21] :       2.68 % Confidence:       0.22 σ
Too many loop adjustments for test [50000 4  SHUFFLE REVERSE_FR DPQ_18_11_21] :       4.35 % Confidence:       0.62 σ
Too many loop adjustments for test [50000 4  SHUFFLE REVERSE_FR DPQ_18_11_21] :       3.35 % Confidence:       0.08 σ
Too many loop adjustments for test [50000 4  SHUFFLE REVERSE_BA DPQ_18_11_21] :       2.38 % Confidence:       0.02 σ
Too many loop adjustments for test [50000 4  SHUFFLE DITHER____ DPQ_18_11_21] :       2.55 % Confidence:       0.07 σ
Too many loop adjustments for test [50000 4  SHUFFLE DITHER____ DPQ_18_11_21] :       2.55 % Confidence:       1.88 σ
Too many loop adjustments for test [50000 4  SHUFFLE DITHER____ DPQ_18_11_21] :       2.29 % Confidence:       2.55 σ
Too many loop adjustments for test [50000 16  SHUFFLE IDENT_____ DPQ_18_11_21] :       2.50 % Confidence:       3.00 σ
Too many loop adjustments for test [50000 16  SHUFFLE IDENT_____ DPQ_18_11_21] :       2.32 % Confidence:       1.16 σ
Too many loop adjustments for test [50000 16  SHUFFLE IDENT_____ DPQ_18_11_21] :       2.53 % Confidence:       3.63 σ
Too many loop adjustments for test [50000 16  SHUFFLE REVERSE_FR DPQ_18_11_21] :       7.37 % Confidence:       0.00 σ
Too many loop adjustments for test [50000 16  SHUFFLE REVERSE_FR DPQ_18_11_21] :       3.50 % Confidence:       0.05 σ
Too many loop adjustments for test [50000 16  SHUFFLE REVERSE_FR DPQ_18_11_21] :       3.76 % Confidence:       0.05 σ
Too many loop adjustments for test [50000 16  SHUFFLE REVERSE_FR DPQ_18_11_21] :       4.85 % Confidence:       0.05 σ
Too many loop adjustments for test [50000 16  SHUFFLE REVERSE_FR DPQ_18_11_21] :       3.80 % Confidence:       0.05 σ
Too many loop adjustments for test [50000 16  SHUFFLE REVERSE_FR DPQ_18_11_21] :       6.74 % Confidence:       1.61 σ
Too many loop adjustments for test [50000 16  SHUFFLE DITHER____ DPQ_18_11_21] :       2.44 % Confidence:       2.89 σ
Too many loop adjustments for test [50000 64  SHUFFLE REVERSE___ DPQ_18_11_21] :       2.13 % Confidence:       5.55 σ
Too many loop adjustments for test [50000 64  SHUFFLE REVERSE_FR DPQ_18_11_21] :       6.45 % Confidence:       0.00 σ
Too many loop adjustments for test [50000 64  SHUFFLE REVERSE_FR DPQ_18_11_21] :       2.89 % Confidence:       3.27 σ
Too many loop adjustments for test [50000 64  SHUFFLE REVERSE_FR DPQ_18_11_21] :       3.61 % Confidence:       0.05 σ
Too many loop adjustments for test [50000 64  SHUFFLE DITHER____ DPQ_18_11_21] :       2.91 % Confidence:       0.00 σ
Too many loop adjustments for test [50000 64  SHUFFLE DITHER____ DPQ_18_11_21] :       3.12 % Confidence:       0.00 σ
Too many loop adjustments for test [50000 64  SHUFFLE DITHER____ DPQ_18_11_21] :       2.73 % Confidence:       0.00 σ
Too many loop adjustments for test [50000 64  SHUFFLE DITHER____ DPQ_18_11_21] :       2.79 % Confidence:       0.00 σ
Too many loop adjustments for test [50000 64  SHUFFLE DITHER____ DPQ_18_11_21] :       3.15 % Confidence:       0.00 σ
Too many loop adjustments for test [50000 64  SHUFFLE DITHER____ DPQ_18_11_21] :       2.50 % Confidence:       0.00 σ
Too many loop adjustments for test [50000 64  SHUFFLE DITHER____ DPQ_18_11_21] :       2.07 % Confidence:       0.02 σ
Too many loop adjustments for test [50000 64  SHUFFLE DITHER____ DPQ_18_11_21] :       2.97 % Confidence:       0.02 σ
Too many loop adjustments for test [50000 64  SHUFFLE DITHER____ DPQ_18_11_21] :       3.24 % Confidence:       0.02 σ
Too many loop adjustments for test [50000 256  SHUFFLE REVERSE___ DPQ_18_11_21] :       3.23 % Confidence:       0.12 σ
Too many loop adjustments for test [50000 256  SHUFFLE REVERSE___ DPQ_18_11_21] :       3.83 % Confidence:       0.12 σ
Too many loop adjustments for test [50000 256  SHUFFLE REVERSE___ DPQ_18_11_21] :       2.55 % Confidence:       0.16 σ
Too many loop adjustments for test [50000 256  SHUFFLE REVERSE___ DPQ_18_11_21] :       2.82 % Confidence:       3.91 σ
Too many loop adjustments for test [50000 256  SHUFFLE REVERSE___ DPQ_18_11_21] :       2.70 % Confidence:       3.91 σ
Too many loop adjustments for test [50000 256  SAWTOTH REVERSE_FR MARLIN] :       3.86 % Confidence:       0.47 σ
Too many loop adjustments for test [50000 256  SHUFFLE DITHER____ DPQ_18_11_21] :       3.13 % Confidence:       0.00 σ
Too many loop adjustments for test [50000 256  SHUFFLE DITHER____ DPQ_18_11_21] :       2.29 % Confidence:       0.00 σ
Too many loop adjustments for test [50000 256  SHUFFLE DITHER____ DPQ_18_11_21] :       2.39 % Confidence:       0.02 σ
Too many loop adjustments for test [50000 256  SHUFFLE DITHER____ DPQ_18_11_21] :       3.39 % Confidence:       0.02 σ
Too many loop adjustments for test [50000 256  SHUFFLE DITHER____ DPQ_18_11_21] :       2.40 % Confidence:       0.02 σ
Too many loop adjustments for test [50000 256  SHUFFLE DITHER____ DPQ_18_11_21] :       2.28 % Confidence:       0.02 σ
Too many loop adjustments for test [50000 1024  SHUFFLE REVERSE_BA DPQ_18_11_21] :       3.48 % Confidence:       0.14 σ
Too many loop adjustments for test [50000 1024  SHUFFLE REVERSE_BA DPQ_18_11_21] :       2.80 % Confidence:       0.14 σ
Too many loop adjustments for test [50000 1024  SHUFFLE REVERSE_BA DPQ_18_11_21] :       4.74 % Confidence:       0.26 σ
Too many loop adjustments for test [50000 1024  SHUFFLE REVERSE_BA DPQ_18_11_21] :       6.23 % Confidence:       0.15 σ
Too many loop adjustments for test [50000 1024  SHUFFLE DITHER____ DPQ_18_11_21] :       4.38 % Confidence:       0.00 σ
Too many loop adjustments for test [50000 1024  SHUFFLE DITHER____ DPQ_18_11_21] :       2.38 % Confidence:       0.03 σ
Too many loop adjustments for test [50000 4096  SHUFFLE DITHER____ DPQ_18_11_21] :       2.25 % Confidence:       0.46 σ
Too many loop adjustments for test [100000 4  SHUFFLE REVERSE___ DPQ_18_11_21] :       2.52 % Confidence:       0.05 σ
Too many loop adjustments for test [100000 4  SHUFFLE REVERSE___ DPQ_18_11_21] :       3.15 % Confidence:       0.09 σ
Too many loop adjustments for test [100000 4  SHUFFLE REVERSE___ DPQ_18_11_21] :       4.00 % Confidence:       0.09 σ
Too many loop adjustments for test [100000 4  SHUFFLE REVERSE_FR DPQ_18_11_21] :       3.47 % Confidence:       0.00 σ
Too many loop adjustments for test [100000 4  SHUFFLE REVERSE_FR DPQ_18_11_21] :       2.63 % Confidence:       0.00 σ
Too many loop adjustments for test [100000 4  SHUFFLE REVERSE_FR DPQ_18_11_21] :       4.03 % Confidence:       0.00 σ
Too many loop adjustments for test [100000 4  SHUFFLE REVERSE_FR DPQ_18_11_21] :       2.63 % Confidence:       0.00 σ
Too many loop adjustments for test [100000 4  SHUFFLE REVERSE_BA DPQ_18_11_21] :       4.07 % Confidence:       1.05 σ
Too many loop adjustments for test [100000 4  SAWTOTH DITHER____ DPQ_18_11I] :       7.05 % Confidence:       7.52 σ
Too many loop adjustments for test [100000 16  SHUFFLE IDENT_____ DPQ_18_11_21] :       2.57 % Confidence:       0.11 σ
Too many loop adjustments for test [100000 16  SAWTOTH REVERSE___ DPQ_18_11P] :       2.39 % Confidence:       0.00 σ
Too many loop adjustments for test [100000 16  SAWTOTH REVERSE___ DPQ_18_11P] :       2.30 % Confidence:       0.00 σ
Too many loop adjustments for test [100000 16  SAWTOTH REVERSE___ DPQ_18_11P] :       2.30 % Confidence:       0.00 σ
Too many loop adjustments for test [100000 16  SAWTOTH REVERSE___ DPQ_18_11P] :       2.09 % Confidence:       0.63 σ
Too many loop adjustments for test [100000 16  SAWTOTH REVERSE___ DPQ_18_11P] :       2.25 % Confidence:       0.63 σ
Too many loop adjustments for test [100000 16  SAWTOTH REVERSE___ DPQ_18_11P] :       2.12 % Confidence:       0.63 σ
Too many loop adjustments for test [100000 16  SAWTOTH REVERSE___ DPQ_18_11P] :       2.21 % Confidence:       0.12 σ
Too many loop adjustments for test [100000 16  SAWTOTH REVERSE___ DPQ_18_11P] :       2.30 % Confidence:       0.12 σ
Too many loop adjustments for test [100000 16  SHUFFLE REVERSE___ DPQ_18_11_21] :       3.20 % Confidence:       0.68 σ
Too many loop adjustments for test [100000 16  SHUFFLE REVERSE___ DPQ_18_11_21] :       3.25 % Confidence:       0.00 σ
Too many loop adjustments for test [100000 16  _RANDOM REVERSE_FR DPQ_18_11_21] :       2.71 % Confidence:       0.47 σ
Too many loop adjustments for test [100000 16  SHUFFLE REVERSE_FR DPQ_18_11_21] :       4.72 % Confidence:       0.00 σ
Too many loop adjustments for test [100000 16  SHUFFLE REVERSE_FR DPQ_18_11_21] :       3.47 % Confidence:       2.61 σ
Too many loop adjustments for test [100000 16  SHUFFLE REVERSE_FR DPQ_18_11_21] :       5.06 % Confidence:       2.61 σ
Too many loop adjustments for test [100000 16  SHUFFLE REVERSE_FR DPQ_18_11_21] :       5.95 % Confidence:       0.03 σ
Too many loop adjustments for test [100000 16  SHUFFLE REVERSE_FR DPQ_18_11_21] :       4.78 % Confidence:       2.66 σ
Too many loop adjustments for test [100000 16  SHUFFLE REVERSE_FR DPQ_18_11_21] :       3.24 % Confidence:       3.15 σ
Too many loop adjustments for test [100000 16  SHUFFLE REVERSE_FR MARLIN] :       3.94 % Confidence:       0.38 σ
Too many loop adjustments for test [100000 16  SHUFFLE REVERSE_BA MARLIN] :       3.94 % Confidence:      20.59 σ
Too many loop adjustments for test [100000 16  SHUFFLE DITHER____ DPQ_18_11_21] :       2.55 % Confidence:       0.07 σ
Too many loop adjustments for test [100000 16  SHUFFLE DITHER____ DPQ_18_11_21] :       2.15 % Confidence:       0.04 σ
Too many loop adjustments for test [100000 64  SAWTOTH IDENT_____ MARLIN] :       2.10 % Confidence:       0.00 σ
Too many loop adjustments for test [100000 64  SAWTOTH IDENT_____ MARLIN] :       2.85 % Confidence:       0.98 σ
Too many loop adjustments for test [100000 64  SAWTOTH IDENT_____ MARLIN] :       3.91 % Confidence:       0.08 σ
Too many loop adjustments for test [100000 64  SAWTOTH IDENT_____ MARLIN] :       2.91 % Confidence:       0.08 σ
Too many loop adjustments for test [100000 64  SAWTOTH IDENT_____ MARLIN] :       3.10 % Confidence:       4.01 σ
Too many loop adjustments for test [100000 64  SAWTOTH IDENT_____ MARLIN] :       3.14 % Confidence:       4.01 σ
Too many loop adjustments for test [100000 64  SAWTOTH REVERSE___ MARLIN] :       4.39 % Confidence:       0.24 σ
Too many loop adjustments for test [100000 64  SAWTOTH REVERSE___ MARLIN] :       4.24 % Confidence:      14.05 σ
Too many loop adjustments for test [100000 64  SAWTOTH REVERSE___ MARLIN] :       4.10 % Confidence:      29.46 σ
Too many loop adjustments for test [100000 64  SAWTOTH REVERSE___ MARLIN] :       0.15 % Confidence:      52.29 σ
Too many loop adjustments for test [100000 64  SAWTOTH REVERSE_FR MARLIN] :       5.86 % Confidence:       1.37 σ
Too many loop adjustments for test [100000 64  SAWTOTH REVERSE_FR MARLIN] :       5.35 % Confidence:       1.37 σ
Too many loop adjustments for test [100000 64  SAWTOTH REVERSE_FR MARLIN] :       5.37 % Confidence:       0.68 σ
Too many loop adjustments for test [100000 64  SAWTOTH REVERSE_FR MARLIN] :       4.36 % Confidence:      14.07 σ
Too many loop adjustments for test [100000 64  SAWTOTH REVERSE_FR MARLIN] :       5.73 % Confidence:      10.26 σ
Too many loop adjustments for test [100000 64  SHUFFLE REVERSE_FR DPQ_18_11_21] :       2.92 % Confidence:       0.00 σ
Too many loop adjustments for test [100000 64  SHUFFLE REVERSE_FR MARLIN] :       4.46 % Confidence:       0.07 σ
Too many loop adjustments for test [100000 64  SAWTOTH REVERSE_BA MARLIN] :       0.16 % Confidence:      33.51 σ
Too many loop adjustments for test [100000 64  SAWTOTH REVERSE_BA MARLIN] :       2.11 % Confidence:      16.38 σ
Too many loop adjustments for test [100000 64  SAWTOTH REVERSE_BA MARLIN] :       5.12 % Confidence:      61.34 σ
Too many loop adjustments for test [100000 64  SAWTOTH REVERSE_BA MARLIN] :       5.11 % Confidence:       5.37 σ
Too many loop adjustments for test [100000 64  SHUFFLE REVERSE_BA MARLIN] :       4.80 % Confidence:       4.70 σ
Too many loop adjustments for test [100000 64  SHUFFLE REVERSE_BA MARLIN] :       4.46 % Confidence:       0.06 σ
Too many loop adjustments for test [100000 64  SHUFFLE DITHER____ DPQ_18_11_21] :       2.21 % Confidence:       0.11 σ
Too many loop adjustments for test [100000 64  SHUFFLE DITHER____ DPQ_18_11_21] :       2.63 % Confidence:       0.11 σ
Too many loop adjustments for test [100000 64  SHUFFLE DITHER____ DPQ_18_11_21] :       3.43 % Confidence:       0.11 σ
Too many loop adjustments for test [100000 64  SHUFFLE DITHER____ DPQ_18_11_21] :       2.07 % Confidence:       0.11 σ
Too many loop adjustments for test [100000 64  SHUFFLE DITHER____ DPQ_18_11_21] :       2.45 % Confidence:       0.11 σ
Too many loop adjustments for test [100000 64  SHUFFLE DITHER____ DPQ_18_11_21] :       4.16 % Confidence:       2.80 σ
Too many loop adjustments for test [100000 64  SHUFFLE DITHER____ DPQ_18_11_21] :       2.63 % Confidence:       2.80 σ
Too many loop adjustments for test [100000 64  SHUFFLE DITHER____ MARLIN] :       2.31 % Confidence:       0.00 σ
Too many loop adjustments for test [100000 256  SHUFFLE REVERSE___ DPQ_18_11_21] :       2.67 % Confidence:       3.00 σ
Too many loop adjustments for test [100000 256  SHUFFLE REVERSE___ DPQ_18_11_21] :       3.46 % Confidence:       3.00 σ
Too many loop adjustments for test [100000 256  SHUFFLE REVERSE___ DPQ_18_11_21] :       2.66 % Confidence:       0.07 σ
Too many loop adjustments for test [100000 256  SHUFFLE REVERSE_FR DPQ_18_11_21] :       4.05 % Confidence:       0.20 σ
Too many loop adjustments for test [100000 256  SHUFFLE REVERSE_BA MARLIN] :       4.78 % Confidence:       0.03 σ
Too many loop adjustments for test [100000 256  SHUFFLE DITHER____ DPQ_18_11_21] :       2.63 % Confidence:       0.30 σ
Too many loop adjustments for test [100000 256  SHUFFLE DITHER____ DPQ_18_11_21] :       2.52 % Confidence:       0.01 σ
Too many loop adjustments for test [100000 256  SHUFFLE DITHER____ MARLIN] :       3.47 % Confidence:       0.07 σ
Too many loop adjustments for test [100000 1024  SHUFFLE REVERSE_BA DPQ_18_11_21] :       2.82 % Confidence:       0.00 σ
Too many loop adjustments for test [100000 1024  SHUFFLE REVERSE_BA DPQ_18_11_21] :       6.35 % Confidence:       0.04 σ
Too many loop adjustments for test [100000 1024  SHUFFLE DITHER____ DPQ_18_11_21] :       2.12 % Confidence:       0.19 σ
Too many loop adjustments for test [100000 1024  SHUFFLE DITHER____ DPQ_18_11_21] :       3.50 % Confidence:       0.19 σ
Too many loop adjustments for test [100000 1024  SHUFFLE DITHER____ DPQ_18_11_21] :       2.61 % Confidence:       0.15 σ
Too many loop adjustments for test [100000 1024  SHUFFLE DITHER____ DPQ_18_11_21] :       2.64 % Confidence:       2.39 σ
Too many loop adjustments for test [100000 1024  SHUFFLE DITHER____ DPQ_18_11_21] :       3.51 % Confidence:       1.07 σ
Too many loop adjustments for test [100000 4096  STAGGER REVERSE___ MARLIN] :       2.33 % Confidence:      34.06 σ
Too many loop adjustments for test [100000 65536  SAWTOTH DITHER____ DPQ_18_11_21] :       2.74 % Confidence:       0.05 σ
Too many loop adjustments for test [100000 65536  SAWTOTH DITHER____ DPQ_18_11_21] :       2.39 % Confidence:       0.05 σ
Too many loop adjustments for test [100000 65536  SAWTOTH DITHER____ DPQ_18_11_21] :       2.50 % Confidence:       0.05 σ
Too many loop adjustments for test [100000 65536  SAWTOTH DITHER____ DPQ_18_11_21] :       2.41 % Confidence:       0.05 σ
Too many loop adjustments for test [100000 65536  SAWTOTH DITHER____ DPQ_18_11_21] :       2.12 % Confidence:       0.05 σ
Too many loop adjustments for test [100000 65536  SAWTOTH DITHER____ DPQ_18_11_21] :       2.52 % Confidence:       0.05 σ
Too many loop adjustments for test [100000 65536  SAWTOTH DITHER____ DPQ_18_11_21] :       2.07 % Confidence:       0.05 σ
Too many loop adjustments for test [100000 65536  SAWTOTH DITHER____ DPQ_18_11_21] :       2.17 % Confidence:       0.05 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE___ DPQ_11] :       2.31 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE___ DPQ_11] :       2.33 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE___ DPQ_11] :       2.32 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE___ DPQ_11] :       2.20 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE___ DPQ_11] :       2.47 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE___ DPQ_11] :       2.50 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE___ DPQ_11] :       2.15 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE___ DPQ_11] :       2.44 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE___ DPQ_11] :       2.47 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE___ DPQ_11] :       2.61 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE___ DPQ_11] :       2.45 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE___ DPQ_11] :       2.46 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE___ DPQ_11] :       2.46 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE___ DPQ_11] :       2.20 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE___ DPQ_11] :       2.44 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE___ DPQ_11] :       2.39 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE___ DPQ_11] :       2.33 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE___ DPQ_11] :       2.44 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE___ DPQ_11] :       2.43 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE___ DPQ_11] :       2.64 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_FR DPQ_11] :       2.59 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_FR DPQ_11] :       2.43 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_FR DPQ_11] :       2.49 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_FR DPQ_11] :       2.64 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_FR DPQ_11] :       2.20 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_FR DPQ_11] :       2.30 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_FR DPQ_11] :       2.47 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_FR DPQ_11] :       2.54 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_FR DPQ_11] :       2.46 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_FR DPQ_11] :       2.55 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_FR DPQ_11] :       2.50 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_FR DPQ_11] :       2.48 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_FR DPQ_11] :       2.49 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_FR DPQ_11] :       2.50 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_FR DPQ_11] :       2.29 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_FR DPQ_11] :       2.25 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_FR DPQ_11] :       2.22 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_FR DPQ_11] :       2.55 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_FR DPQ_11] :       2.44 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_FR DPQ_11] :       2.28 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_BA DPQ_11] :       2.54 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_BA DPQ_11] :       2.56 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_BA DPQ_11] :       2.59 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_BA DPQ_11] :       2.50 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_BA DPQ_11] :       2.46 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_BA DPQ_11] :       2.29 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_BA DPQ_11] :       2.21 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_BA DPQ_11] :       2.30 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_BA DPQ_11] :       2.46 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH REVERSE_BA DPQ_11] :       2.47 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_BA DPQ_11] :       2.54 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_BA DPQ_11] :       2.36 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_BA DPQ_11] :       2.25 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_BA DPQ_11] :       2.63 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_BA DPQ_11] :       2.51 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_BA DPQ_11] :       2.38 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_BA DPQ_11] :       2.27 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_BA DPQ_11] :       2.46 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_BA DPQ_11] :       2.52 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM REVERSE_BA DPQ_11] :       2.47 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH SORT______ DPQ_11] :       2.48 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH SORT______ DPQ_11] :       2.43 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH SORT______ DPQ_11] :       2.35 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH SORT______ DPQ_11] :       2.48 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH SORT______ DPQ_11] :       2.60 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH SORT______ DPQ_11] :       2.48 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH SORT______ DPQ_11] :       2.37 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH SORT______ DPQ_11] :       2.33 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH SORT______ DPQ_11] :       2.42 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  SAWTOTH SORT______ DPQ_11] :       2.49 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM SORT______ DPQ_11] :       2.52 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM SORT______ DPQ_11] :       2.52 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM SORT______ DPQ_11] :       2.27 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM SORT______ DPQ_11] :       2.57 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM SORT______ DPQ_11] :       2.50 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM SORT______ DPQ_11] :       2.35 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM SORT______ DPQ_11] :       2.50 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM SORT______ DPQ_11] :       2.15 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM SORT______ DPQ_11] :       2.38 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 1  _RANDOM SORT______ DPQ_11] :       2.41 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 16  SAWTOTH IDENT_____ DPQ_18_11I] :      12.38 % Confidence:       1.16 σ
Too many loop adjustments for test [500000 16  SAWTOTH IDENT_____ DPQ_18_11I] :      16.20 % Confidence:      15.31 σ
Too many loop adjustments for test [500000 16  SAWTOTH IDENT_____ DPQ_18_11I] :      16.22 % Confidence:      56.42 σ
Too many loop adjustments for test [500000 16  SAWTOTH REVERSE___ DPQ_18_11I] :       7.63 % Confidence:      12.00 σ
Too many loop adjustments for test [500000 16  SHUFFLE REVERSE_FR DPQ_18_11_21] :       2.94 % Confidence:       0.14 σ
Too many loop adjustments for test [500000 16  SHUFFLE REVERSE_FR DPQ_18_11_21] :       2.56 % Confidence:       1.18 σ
Too many loop adjustments for test [500000 16  SHUFFLE REVERSE_FR DPQ_18_11_21] :       3.19 % Confidence:       1.18 σ
Too many loop adjustments for test [500000 16  SHUFFLE REVERSE_FR DPQ_18_11_21] :       2.88 % Confidence:       1.94 σ
Too many loop adjustments for test [500000 16  SAWTOTH REVERSE_BA DPQ_18_11I] :      14.19 % Confidence:       0.00 σ
Too many loop adjustments for test [500000 16  SAWTOTH REVERSE_BA DPQ_18_11I] :      13.22 % Confidence:      78.40 σ
Too many loop adjustments for test [500000 16  SAWTOTH REVERSE_BA DPQ_18_11I] :       0.27 % Confidence:      15.15 σ
Too many loop adjustments for test [500000 16  SAWTOTH REVERSE_BA DPQ_18_11I] :      12.51 % Confidence:      35.33 σ
Too many loop adjustments for test [500000 16  SAWTOTH REVERSE_BA DPQ_18_11I] :      14.19 % Confidence:      25.98 σ
Too many loop adjustments for test [500000 16  SHUFFLE REVERSE_BA DPQ_18_11_21] :       3.66 % Confidence:       0.14 σ
^Cdone.

