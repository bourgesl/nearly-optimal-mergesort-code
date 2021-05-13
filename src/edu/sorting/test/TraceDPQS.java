/*******************************************************************************
 * JMMC project ( http://www.jmmc.fr ) - Copyright (C) CNRS.
 ******************************************************************************/
package edu.sorting.test;

import edu.sorting.DualPivotQuickSort2011;
import edu.sorting.DualPivotQuicksort20210424;
import edu.sorting.perf.IntArrayTweaker;
import edu.sorting.perf.ParamIntArrayBuilder;
import edu.sorting.util.WelfordVariance;
import java.util.Arrays;

/**
 *
 * @author bourgesl
 */
/*
Test[SPIRAL | IDENT_____] stats: [1000000: µ=1000000.5 σ=577350.1248525326 (57.73498361776146 %) rms=1577350.6248525325 min=2.0 max=1999999.0 sum=1.0000005E12]
Test[SPIRAL | IDENT_____] statsDelta: [999999: µ=1.0 σ=1154699.9610288378 (1.1546999610288379E8 %) rms=1154700.9610288378 min=-1999995.0 max=1999997.0 sum=999999.0]
Test[SPIRAL | REVERSE___] stats: [1000000: µ=1000000.5 σ=577350.1248518336 (57.73498361769155 %) rms=1577350.6248518336 min=2.0 max=1999999.0 sum=1.0000005E12]
Test[SPIRAL | REVERSE___] statsDelta: [999999: µ=-0.9999999999997493 σ=1154699.961028839 (-1.1546999610291286E8 %) rms=1154698.961028839 min=-1999997.0 max=1999995.0 sum=-999998.9999997494]
Test[SPIRAL | REVERSE_FR] stats: [1000000: µ=1000000.5 σ=577350.124852572 (57.734983617765394 %) rms=1577350.624852572 min=2.0 max=1999999.0 sum=1.0000005E12]
Test[SPIRAL | REVERSE_FR] statsDelta: [999999: µ=0.5000005000007322 σ=1154699.6362695366 (2.3093969631387284E8 %) rms=1154700.1362700365 min=-1999995.0 max=1999997.0 sum=500000.00000023213]
Test[SPIRAL | REVERSE_BA] stats: [1000000: µ=1000000.5 σ=577350.1248517823 (57.734983617686424 %) rms=1577350.6248517823 min=2.0 max=1999999.0 sum=1.0000005E12]
Test[SPIRAL | REVERSE_BA] statsDelta: [999999: µ=-0.5000005000004946 σ=1154699.636269642 (-2.3093969631400368E8 %) rms=1154699.1362691422 min=-1999997.0 max=1999995.0 sum=-499999.9999999946]
Test[SPIRAL | SORT______] stats: [1000000: µ=1000000.5000000038 σ=577350.1248520404 (57.73498361771201 %) rms=1577350.6248520443 min=2.0 max=1999999.0 sum=1.0000005000000038E12]
Test[SPIRAL | SORT______] statsDelta: [999999: µ=1.9999989999989998 σ=0.001000000500000381 (0.05000005000006905 %) rms=2.000999000499 min=1.0 max=2.0 sum=1999996.9999999998]
Test[SPIRAL | DITHER____] stats: [1000000: µ=1000002.499999992 σ=577350.1248535572 (57.73486814818581 %) rms=1577352.624853549 min=5.0 max=2000003.0 sum=1.000002499999992E12]
Test[SPIRAL | DITHER____] statsDelta: [999999: µ=1.0000040000038977 σ=1154699.9610273335 (1.1546953422414638E8 %) rms=1154700.9610313335 min=-1999994.0 max=1999998.0 sum=1000002.9999998977]


Test[STAGGER | IDENT_____] stats: [1000000: µ=499999.00000000675 σ=288675.2789319055 (57.735171256722836 %) rms=788674.2789319123 min=0.0 max=999998.0 sum=4.999990000000068E11]
Test[STAGGER | IDENT_____] statsDelta: [999999: µ=0.9999989999989918 σ=1000.0005000003721 (100000.15000028803 %) rms=1001.0004990003711 min=-999998.0 max=2.0 sum=999997.9999999917]
Test[STAGGER | REVERSE___] stats: [1000000: µ=499999.0000000076 σ=288675.2789319083 (57.7351712567233 %) rms=788674.278931916 min=0.0 max=999998.0 sum=4.999990000000076E11]
Test[STAGGER | REVERSE___] statsDelta: [999999: µ=-0.9999989999989918 σ=1000.0005000003721 (-100000.15000028803 %) rms=999.0005010003731 min=-2.0 max=999998.0 sum=-999997.9999999917]
Test[STAGGER | REVERSE_FR] stats: [1000000: µ=499999.00000000675 σ=288675.2789319055 (57.735171256722836 %) rms=788674.2789319123 min=0.0 max=999998.0 sum=4.999990000000068E11]
Test[STAGGER | REVERSE_FR] statsDelta: [999999: µ=-5.409006711499034E-15 σ=2.0000000000000058 (-3.6975365472337016E16 %) rms=2.0000000000000004 min=-2.0 max=2.0 sum=-5.409001302492323E-9]
Test[STAGGER | REVERSE_BA] stats: [1000000: µ=499999.0000000076 σ=288675.2789319083 (57.7351712567233 %) rms=788674.278931916 min=0.0 max=999998.0 sum=4.999990000000076E11]
Test[STAGGER | REVERSE_BA] statsDelta: [999999: µ=5.409006711499034E-15 σ=2.0000000000000058 (3.6975365472337016E16 %) rms=2.000000000000011 min=-2.0 max=2.0 sum=5.409001302492323E-9]
Test[STAGGER | SORT______] stats: [1000000: µ=499999.0 σ=288675.278931919 (57.73517125672632 %) rms=788674.278931919 min=0.0 max=999998.0 sum=4.99999E11]
Test[STAGGER | SORT______] statsDelta: [999999: µ=0.999998999999 σ=1.000000500000375 (100.0001500002875 %) rms=1.999999499999375 min=0.0 max=2.0 sum=999998.0]
Test[STAGGER | DITHER____] stats: [1000000: µ=500000.9999998022 σ=288675.2789493261 (57.73494032000742 %) rms=788676.2789491282 min=0.0 max=1000002.0 sum=5.000009999998022E11]
Test[STAGGER | DITHER____] statsDelta: [999999: µ=1.0000030000030158 σ=1000.0064999873822 (100000.34999738666 %) rms=1001.0065029873853 min=-1000002.0 max=3.0 sum=1000002.0000000158]

Test[SAWTOTH | IDENT_____] stats: [1000000: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[SAWTOTH | IDENT_____] statsDelta: [999999: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[SAWTOTH | REVERSE___] stats: [1000000: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[SAWTOTH | REVERSE___] statsDelta: [999999: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[SAWTOTH | REVERSE_FR] stats: [1000000: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[SAWTOTH | REVERSE_FR] statsDelta: [999999: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[SAWTOTH | REVERSE_BA] stats: [1000000: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[SAWTOTH | REVERSE_BA] statsDelta: [999999: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[SAWTOTH | SORT______] stats: [1000000: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[SAWTOTH | SORT______] statsDelta: [999999: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[SAWTOTH | DITHER____] stats: [1000000: µ=2.000000000000717 σ=1.414214269479691 (70.7107134739592 %) rms=3.414214269480408 min=0.0 max=4.0 sum=2000000.0000007171]
Test[SAWTOTH | DITHER____] statsDelta: [999999: µ=4.0000040000042105E-6 σ=1.9999979999910051 (4.99998999998225E7 %) rms=2.000001999995005 min=-4.0 max=1.0 sum=4.0000000000002105]

Test[_RANDOM | IDENT_____] stats: [1000000: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[_RANDOM | IDENT_____] statsDelta: [999999: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[_RANDOM | REVERSE___] stats: [1000000: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[_RANDOM | REVERSE___] statsDelta: [999999: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[_RANDOM | REVERSE_FR] stats: [1000000: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[_RANDOM | REVERSE_FR] statsDelta: [999999: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[_RANDOM | REVERSE_BA] stats: [1000000: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[_RANDOM | REVERSE_BA] statsDelta: [999999: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[_RANDOM | SORT______] stats: [1000000: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[_RANDOM | SORT______] statsDelta: [999999: µ=0.0 σ=0.0 (NaN %) rms=0.0 min=0.0 max=0.0 sum=0.0]
Test[_RANDOM | DITHER____] stats: [1000000: µ=2.000000000000717 σ=1.414214269479691 (70.7107134739592 %) rms=3.414214269480408 min=0.0 max=4.0 sum=2000000.0000007171]
Test[_RANDOM | DITHER____] statsDelta: [999999: µ=4.0000040000042105E-6 σ=1.9999979999910051 (4.99998999998225E7 %) rms=2.000001999995005 min=-4.0 max=1.0 sum=4.0000000000002105]

Test[PLATEAU | IDENT_____] stats: [1000000: µ=0.9999990000000116 σ=9.99999999999986E-4 (0.10000010000009743 %) rms=1.0009990000000115 min=0.0 max=1.0 sum=999999.0000000116]
Test[PLATEAU | IDENT_____] statsDelta: [999999: µ=1.0000010000010243E-6 σ=0.001000000500000399 (99999.94999998747 %) rms=0.0010010005010004 min=0.0 max=1.0 sum=1.0000000000000242]
Test[PLATEAU | REVERSE___] stats: [1000000: µ=0.999999 σ=0.001 (0.10000010000010001 %) rms=1.000999 min=0.0 max=1.0 sum=999999.0]
Test[PLATEAU | REVERSE___] statsDelta: [999999: µ=-1.000001000001E-6 σ=0.001000000500000375 (-99999.9499999875 %) rms=9.99000499000374E-4 min=-1.0 max=0.0 sum=-0.9999999999999999]
Test[PLATEAU | REVERSE_FR] stats: [1000000: µ=0.9999990000000053 σ=0.0010000000000000009 (0.10000010000009955 %) rms=1.0009990000000053 min=0.0 max=1.0 sum=999999.0000000054]
Test[PLATEAU | REVERSE_FR] statsDelta: [999999: µ=0.0 σ=0.0014142149765887786 (Infinity %) rms=0.0014142149765887786 min=-1.0 max=1.0 sum=0.0]
Test[PLATEAU | REVERSE_BA] stats: [1000000: µ=0.9999990000000116 σ=9.99999999999986E-4 (0.10000010000009743 %) rms=1.0009990000000115 min=0.0 max=1.0 sum=999999.0000000116]
Test[PLATEAU | REVERSE_BA] statsDelta: [999999: µ=1.0000010000010243E-6 σ=0.001000000500000399 (99999.94999998747 %) rms=0.0010010005010004 min=0.0 max=1.0 sum=1.0000000000000242]
Test[PLATEAU | SORT______] stats: [1000000: µ=0.9999990000000116 σ=9.99999999999986E-4 (0.10000010000009743 %) rms=1.0009990000000115 min=0.0 max=1.0 sum=999999.0000000116]
Test[PLATEAU | SORT______] statsDelta: [999999: µ=1.0000010000010243E-6 σ=0.001000000500000399 (99999.94999998747 %) rms=0.0010010005010004 min=0.0 max=1.0 sum=1.0000000000000242]
Test[PLATEAU | DITHER____] stats: [1000000: µ=2.9999990000000576 σ=1.414216037246111 (47.14055028838622 %) rms=4.414215037246168 min=0.0 max=5.0 sum=2999999.0000000577]
Test[PLATEAU | DITHER____] statsDelta: [999999: µ=5.000005000005028E-6 σ=1.9999987499908596 (3.9999934999841966E7 %) rms=2.0000037499958596 min=-4.0 max=2.0 sum=5.0000000000000275]

Test[SHUFFLE | IDENT_____] stats: [1000000: µ=1000002.0 σ=577350.5578646881 (57.73494031658818 %) rms=1577352.557864688 min=3.0 max=2000001.0 sum=1.000002E12]
Test[SHUFFLE | IDENT_____] statsDelta: [999999: µ=2.0 σ=0.0 (0.0 %) rms=2.0 min=2.0 max=2.0 sum=1999998.0]
Test[SHUFFLE | REVERSE___] stats: [1000000: µ=1000002.0 σ=577350.5578646881 (57.73494031658818 %) rms=1577352.557864688 min=3.0 max=2000001.0 sum=1.000002E12]
Test[SHUFFLE | REVERSE___] statsDelta: [999999: µ=-2.0 σ=0.0 (-0.0 %) rms=-2.0 min=-2.0 max=-2.0 sum=-1999998.0]
Test[SHUFFLE | REVERSE_FR] stats: [1000000: µ=1000002.0 σ=577350.5578646881 (57.73494031658818 %) rms=1577352.557864688 min=3.0 max=2000001.0 sum=1.000002E12]
Test[SHUFFLE | REVERSE_FR] statsDelta: [999999: µ=1.0000010000009925 σ=1000.0024999973747 (100000.14999948822 %) rms=1001.0025009973757 min=-2.0 max=1000000.0 sum=999999.9999999924]
Test[SHUFFLE | REVERSE_BA] stats: [1000000: µ=1000001.9999999874 σ=577350.557864687 (57.7349403165888 %) rms=1577352.5578646744 min=3.0 max=2000001.0 sum=1.0000019999999874E12]
Test[SHUFFLE | REVERSE_BA] statsDelta: [999999: µ=1.0000010000010082 σ=1000.0024999973854 (100000.14999948771 %) rms=1001.0025009973864 min=-2.0 max=1000000.0 sum=1000000.0000000083]
Test[SHUFFLE | SORT______] stats: [1000000: µ=1000002.0 σ=577350.5578646881 (57.73494031658818 %) rms=1577352.557864688 min=3.0 max=2000001.0 sum=1.000002E12]
Test[SHUFFLE | SORT______] statsDelta: [999999: µ=2.0 σ=0.0 (0.0 %) rms=2.0 min=2.0 max=2.0 sum=1999998.0]
Test[SHUFFLE | DITHER____] stats: [1000000: µ=1000003.9999993263 σ=577350.5578738054 (57.73482484812004 %) rms=1577354.5578731317 min=3.0 max=2000005.0 sum=1.0000039999993263E12]
Test[SHUFFLE | DITHER____] statsDelta: [999999: µ=2.00000400000419 σ=1.9999979999910051 (99.99969999994074 %) rms=4.000001999995195 min=-2.0 max=3.0 sum=2000002.00000019]
 */
public class TraceDPQS {

    private final static int M = 1000000;
    private final static int N = 1;

    public static void main(String[] args) {

//        final int subSize = 2;
//        final int subSize = 256;
        final int subSize = M;

        System.out.println("subSize: " + subSize);
        
        /*
DITHER____:SHUFFLE stats (%): [1: µ=29.021154 σ=NaN rms=NaN min=29.021154 max=29.021154]
        
data[0-512]: [3, 6, 9, 12, 15, 13, 16, 19, 22, 25, 23, 26, 29, 32, 35, 33, 36, 39, 42, 45, 43, 46, 49, 52, 55, 53, 56, 59, 62, 65, 63, 66, 69, 72, 75, 73, 76, 79, 82, 85, 83, 86, 89, 92, 95, 93, 96, 99, 102, 105, 103, 106, 109, 112, 115, 113, 116, 119, 122, 125, 123, 126, 129, 132, 135, 133, 136, 139, 142, 145, 143, 146, 149, 152, 155, 153, 156, 159, 162, 165, 163, 166, 169, 172, 175, 173, 176, 179, 182, 185, 183, 186, 189, 192, 195, 193, 196, 199, 202, 205, 203, 206, 209, 212, 215, 213, 216, 219, 222, 225, 223, 226, 229, 232, 235, 233, 236, 239, 242, 245, 243, 246, 249, 252, 255, 253, 256, 259, 262, 265, 263, 266, 269, 272, 275, 273, 276, 279, 282, 285, 283, 286, 289, 292, 295, 293, 296, 299, 302, 305, 303, 306, 309, 312, 315, 313, 316, 319, 322, 325, 323, 326, 329, 332, 335, 333, 336, 339, 342, 345, 343, 346, 349, 352, 355, 353, 356, 359, 362, 365, 363, 366, 369, 372, 375, 373, 376, 379, 382, 385, 383, 386, 389, 392, 395, 393, 396, 399, 402, 405, 403, 406, 409, 412, 415, 413, 416, 419, 422, 425, 423, 426, 429, 432, 435, 433, 436, 439, 442, 445, 443, 446, 449, 452, 455, 453, 456, 459, 462, 465, 463, 466, 469, 472, 475, 473, 476, 479, 482, 485, 483, 486, 489, 492, 495, 493, 496, 499, 502, 505, 503, 506, 509, 512, 515, 513, 516, 519, 522, 525, 523, 526, 529, 532, 535, 533, 536, 539, 542, 545, 543, 546, 549, 552, 555, 553, 556, 559, 562, 565, 563, 566, 569, 572, 575, 573, 576, 579, 582, 585, 583, 586, 589, 592, 595, 593, 596, 599, 602, 605, 603, 606, 609, 612, 615, 613, 616, 619, 622, 625, 623, 626, 629, 632, 635, 633, 636, 639, 642, 645, 643, 646, 649, 652, 655, 653, 656, 659, 662, 665, 663, 666, 669, 672, 675, 673, 676, 679, 682, 685, 683, 686, 689, 692, 695, 693, 696, 699, 702, 705, 703, 706, 709, 712, 715, 713, 716, 719, 722, 725, 723, 726, 729, 732, 735, 733, 736, 739, 742, 745, 743, 746, 749, 752, 755, 753, 756, 759, 762, 765, 763, 766, 769, 772, 775, 773, 776, 779, 782, 785, 783, 786, 789, 792, 795, 793, 796, 799, 802, 805, 803, 806, 809, 812, 815, 813, 816, 819, 822, 825, 823, 826, 829, 832, 835, 833, 836, 839, 842, 845, 843, 846, 849, 852, 855, 853, 856, 859, 862, 865, 863, 866, 869, 872, 875, 873, 876, 879, 882, 885, 883, 886, 889, 892, 895, 893, 896, 899, 902, 905, 903, 906, 909, 912, 915, 913, 916, 919, 922, 925, 923, 926, 929, 932, 935, 933, 936, 939, 942, 945, 943, 946, 949, 952, 955, 953, 956, 959, 962, 965, 963, 966, 969, 972, 975, 973, 976, 979, 982, 985, 983, 986, 989, 992, 995, 993, 996, 999, 1002, 1005, 1003, 1006, 1009, 1012, 1015, 1013, 1016, 1019, 1022, 1025, 1023, 1026]
data[-100:0]: [1999803, 1999806, 1999809, 1999812, 1999815, 1999813, 1999816, 1999819, 1999822, 1999825, 1999823, 1999826, 1999829, 1999832, 1999835, 1999833, 1999836, 1999839, 1999842, 1999845, 1999843, 1999846, 1999849, 1999852, 1999855, 1999853, 1999856, 1999859, 1999862, 1999865, 1999863, 1999866, 1999869, 1999872, 1999875, 1999873, 1999876, 1999879, 1999882, 1999885, 1999883, 1999886, 1999889, 1999892, 1999895, 1999893, 1999896, 1999899, 1999902, 1999905, 1999903, 1999906, 1999909, 1999912, 1999915, 1999913, 1999916, 1999919, 1999922, 1999925, 1999923, 1999926, 1999929, 1999932, 1999935, 1999933, 1999936, 1999939, 1999942, 1999945, 1999943, 1999946, 1999949, 1999952, 1999955, 1999953, 1999956, 1999959, 1999962, 1999965, 1999963, 1999966, 1999969, 1999972, 1999975, 1999973, 1999976, 1999979, 1999982, 1999985, 1999983, 1999986, 1999989, 1999992, 1999995, 1999993, 1999996, 1999999, 2000002, 2000005]
        
Test[SHUFFLE | DITHER____ | DPQS_24] M=1000000----------------------------------------------------------
DPQSsort[1000000] bits = 0 in [0 - 1000000]
tryMergeRuns[1000000] in [0 - 1000000]
Challenge: 
1: i[375003] => [750012]
2: i[437501] => [875006]
3: i[499999] => [1000005]
4: i[562497] => [1124999]
5: i[624996] => [1249996]
5-1: d = [499984]
1-2: d = [124994]
2-3: d = [124999]
3-4: d = [124994]
4-5: d = [124997]
radixSort[1000000] in [0 - 1000000]

Radix is not best in this situation         !
         */

        if (true) {
            test(ParamIntArrayBuilder.SHUFFLE, IntArrayTweaker.DITHER____, subSize, Impl.DPQS_24);
        }

        for (ParamIntArrayBuilder iab : ParamIntArrayBuilder.values()) {
            for (IntArrayTweaker iat : IntArrayTweaker.values()) {
                test(iab, iat, subSize, Impl.DPQS_24);
            }
        }
    }

    public static void test(final ParamIntArrayBuilder iab, final IntArrayTweaker iat, final int subSize, Impl impl) {
        System.out.println("Test[" + iab + " | " + iat + " | " + impl + "] M=" + M + "----------------------------------------------------------");
        final int[] input = new int[M];
        final int[] proto = new int[M];

        // Get new distribution sample:
        iab.build(input, subSize);

        // Reset tweaker to have sample initial conditions (seed):
        ParamIntArrayBuilder.reset();

        // tweak sample:
        iat.tweak(input, proto);

        final int[] data = proto;
        // copy
        final int[] copy = Arrays.copyOf(data, M);

        if (true) {
            System.out.println("data[0 : 100]: " + Arrays.toString(Arrays.copyOfRange(data, 0, 100)));
            System.out.println("data[-100 : 0]: " + Arrays.toString(Arrays.copyOfRange(data, M - 100, M)));

            // Stats:
            final WelfordVariance stats = new WelfordVariance();
            final WelfordVariance statsDelta = new WelfordVariance();
            final WelfordVariance statsAbsDelta = new WelfordVariance();

            final long start = System.nanoTime();

            for (int j = 0; j < data.length; j++) {
                stats.add(data[j]);
            }
            for (int j = 1, d; j < data.length; j++) {
                d = data[j] - data[j - 1]; // delta = V(i) - V(i-1)
                statsDelta.add(d);
                if (d < 0) {
                    d = -d;
                }
                statsAbsDelta.add(d);
            }
            final long elapsed = (System.nanoTime() - start);
            System.out.println("Test[" + iab + " | " + iat + "] stats        : " + stats.toString());
            System.out.println("Test[" + iab + " | " + iat + "] statsDelta   : " + statsDelta.toString());
            System.out.println("Test[" + iab + " | " + iat + "] statsAbsDelta: " + statsAbsDelta.toString());
            System.out.println("Test[" + iab + " | " + iat + "] elapsed: " + (1e-6 * elapsed) + " ms");
        }

        System.out.println("Test[" + iab + " | " + iat + " | " + impl + "] M=" + M + "----------------------------------------------------------");
        System.arraycopy(data, 0, copy, 0, copy.length);
        impl.sort(data);
        Arrays.sort(copy);

        if (!Arrays.equals(data, copy)) {
            for (int j = 0; j < M; j++) {
                if (data[j] != copy[j]) {
                    System.out.println("Mismatch[" + j + "]: " + data[j] + " != " + copy[j]);
                }
            }
            System.out.flush();
            throw new IllegalStateException("Bad sort : " + impl);
        }
    }

    enum Impl {

        DPQS_24 {
            @Override
            void sort(int[] data) {
                DualPivotQuicksort20210424.INSTANCE.sort(data);
            }
        },
        DPQS_11 {
            @Override
            void sort(int[] data) {
                DualPivotQuickSort2011.INSTANCE.sort(data);
            }
        };

        abstract void sort(int[] data);
    }
}
