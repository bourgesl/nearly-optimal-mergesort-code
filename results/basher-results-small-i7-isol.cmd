./bentley-basher.sh 
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
Vitesse du processeur en MHz :          800.030
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
  current CPU frequency: 2.37 GHz (asserted by call to kernel)
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
  current CPU frequency: 2.51 GHz (asserted by call to kernel)
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
  current CPU frequency: 2.42 GHz (asserted by call to kernel)
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
  current CPU frequency: 2.23 GHz (asserted by call to kernel)
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
JAVA_OPTS: -Xms1g -Xmx1g
Running Bentley basher
Too long loop:   12104.07 ms for test [1000 256  _RANDOM IDENT_____ DPQ_18_11_21] :      14.64 %
Too long loop:    8955.51 ms for test [1000 256  _RANDOM REVERSE_BA DPQ_18_11_21] :      28.25 %
Too long loop:    7940.76 ms for test [1000 256  _RANDOM REVERSE_BA DPQ_18_11_21] :       4.93 %
Too long loop:   13014.06 ms for test [1000 1024  _RANDOM IDENT_____ DPQ_18_11_21] :       4.32 %
Too long loop:    9021.41 ms for test [2000 1  STAGGER DITHER____ DPQ_18_11_21] :       4.95 %
Too long loop:    7175.10 ms for test [2000 4  STAGGER REVERSE_BA DPQ_18_11_21] :      12.96 %
Too long loop:    7892.05 ms for test [2000 4  STAGGER REVERSE_BA DPQ_18_11_21] :      13.32 %
Too long loop:   12594.17 ms for test [2000 64  _RANDOM REVERSE___ DPQ_18_11_21] :      11.04 %
Too long loop:   11114.71 ms for test [2000 256  _RANDOM IDENT_____ DPQ_18_11_21] :       6.04 %
Too long loop:    8570.81 ms for test [2000 256  SAWTOTH REVERSE___ DPQ_18_11_21] :       8.76 %
done.

