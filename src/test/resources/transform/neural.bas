10 dim  l1(16)
20 dim  l2(16)
30 input  "enter a number:"; a%
40 ti$="000000":a% = int((a% / 0.024574 - 128)+0.5)
50 acc = ((117 + 0) * (a% + 128)) + 6
60 if  acc>0 then  acc=(acc*1169513172 + 2^30) / 2^31
70 if  acc<=0 then  acc=(acc*1169513172 + (1-2^30)) / 2^31
80 acc = (int((acc)+0.5) / 2^6) + -128
90 acc = int((acc)+0.5)
100 if  acc < -128 then  acc = -128
110 if  acc > 127 then  acc = 127
120 l1(1) = acc
130 acc = ((28 + 0) * (a% + 128)) + 2935
140 if  acc>0 then  acc=(acc*1169513172 + 2^30) / 2^31
150 if  acc<=0 then  acc=(acc*1169513172 + (1-2^30)) / 2^31
160 acc = (int((acc)+0.5) / 2^6) + -128
170 acc = int((acc)+0.5)
180 if  acc < -128 then  acc = -128
190 if  acc > 127 then  acc = 127
200 l1(2) = acc
210 acc = ((17 + 0) * (a% + 128)) + -2477
220 if  acc>0 then  acc=(acc*1169513172 + 2^30) / 2^31
230 if  acc<=0 then  acc=(acc*1169513172 + (1-2^30)) / 2^31
240 acc = (int((acc)+0.5) / 2^6) + -128
250 acc = int((acc)+0.5)
260 if  acc < -128 then  acc = -128
270 if  acc > 127 then  acc = 127
280 l1(3) = acc
290 acc = ((-31 + 0) * (a% + 128)) + 0
300 if  acc>0 then  acc=(acc*1169513172 + 2^30) / 2^31
310 if  acc<=0 then  acc=(acc*1169513172 + (1-2^30)) / 2^31
320 acc = (int((acc)+0.5) / 2^6) + -128
330 acc = int((acc)+0.5)
340 if  acc < -128 then  acc = -128
350 if  acc > 127 then  acc = 127
360 l1(4) = acc
370 acc = ((12 + 0) * (a% + 128)) + 3191
380 if  acc>0 then  acc=(acc*1169513172 + 2^30) / 2^31
390 if  acc<=0 then  acc=(acc*1169513172 + (1-2^30)) / 2^31
400 acc = (int((acc)+0.5) / 2^6) + -128
410 acc = int((acc)+0.5)
420 if  acc < -128 then  acc = -128
430 if  acc > 127 then  acc = 127
440 l1(5) = acc
450 acc = ((-127 + 0) * (a% + 128)) + 0
460 if  acc>0 then  acc=(acc*1169513172 + 2^30) / 2^31
470 if  acc<=0 then  acc=(acc*1169513172 + (1-2^30)) / 2^31
480 acc = (int((acc)+0.5) / 2^6) + -128
490 acc = int((acc)+0.5)
500 if  acc < -128 then  acc = -128
510 if  acc > 127 then  acc = 127
520 l1(6) = acc
530 acc = ((-91 + 0) * (a% + 128)) + 0
540 if  acc>0 then  acc=(acc*1169513172 + 2^30) / 2^31
550 if  acc<=0 then  acc=(acc*1169513172 + (1-2^30)) / 2^31
560 acc = (int((acc)+0.5) / 2^6) + -128
570 acc = int((acc)+0.5)
580 if  acc < -128 then  acc = -128
590 if  acc > 127 then  acc = 127
600 l1(7) = acc
610 acc = ((66 + 0) * (a% + 128)) + 1747
620 if  acc>0 then  acc=(acc*1169513172 + 2^30) / 2^31
630 if  acc<=0 then  acc=(acc*1169513172 + (1-2^30)) / 2^31
640 acc = (int((acc)+0.5) / 2^6) + -128
650 acc = int((acc)+0.5)
660 if  acc < -128 then  acc = -128
670 if  acc > 127 then  acc = 127
680 l1(8) = acc
690 acc = ((-2 + 0) * (a% + 128)) + 0
700 if  acc>0 then  acc=(acc*1169513172 + 2^30) / 2^31
710 if  acc<=0 then  acc=(acc*1169513172 + (1-2^30)) / 2^31
720 acc = (int((acc)+0.5) / 2^6) + -128
730 acc = int((acc)+0.5)
740 if  acc < -128 then  acc = -128
750 if  acc > 127 then  acc = 127
760 l1(9) = acc
770 acc = ((-43 + 0) * (a% + 128)) + 0
780 if  acc>0 then  acc=(acc*1169513172 + 2^30) / 2^31
790 if  acc<=0 then  acc=(acc*1169513172 + (1-2^30)) / 2^31
800 acc = (int((acc)+0.5) / 2^6) + -128
810 acc = int((acc)+0.5)
820 if  acc < -128 then  acc = -128
830 if  acc > 127 then  acc = 127
840 l1(10) = acc
850 acc = ((-44 + 0) * (a% + 128)) + 8562
860 if  acc>0 then  acc=(acc*1169513172 + 2^30) / 2^31
870 if  acc<=0 then  acc=(acc*1169513172 + (1-2^30)) / 2^31
880 acc = (int((acc)+0.5) / 2^6) + -128
890 acc = int((acc)+0.5)
900 if  acc < -128 then  acc = -128
910 if  acc > 127 then  acc = 127
920 l1(11) = acc
930 acc = ((-78 + 0) * (a% + 128)) + 0
940 if  acc>0 then  acc=(acc*1169513172 + 2^30) / 2^31
950 if  acc<=0 then  acc=(acc*1169513172 + (1-2^30)) / 2^31
960 acc = (int((acc)+0.5) / 2^6) + -128
970 acc = int((acc)+0.5)
980 if  acc < -128 then  acc = -128
990 if  acc > 127 then  acc = 127
1000 l1(12) = acc
1010 acc = ((97 + 0) * (a% + 128)) + 1839
1020 if  acc>0 then  acc=(acc*1169513172 + 2^30) / 2^31
1030 if  acc<=0 then  acc=(acc*1169513172 + (1-2^30)) / 2^31
1040 acc = (int((acc)+0.5) / 2^6) + -128
1050 acc = int((acc)+0.5)
1060 if  acc < -128 then  acc = -128
1070 if  acc > 127 then  acc = 127
1080 l1(13) = acc
1090 acc = ((120 + 0) * (a% + 128)) + -2713
1100 if  acc>0 then  acc=(acc*1169513172 + 2^30) / 2^31
1110 if  acc<=0 then  acc=(acc*1169513172 + (1-2^30)) / 2^31
1120 acc = (int((acc)+0.5) / 2^6) + -128
1130 acc = int((acc)+0.5)
1140 if  acc < -128 then  acc = -128
1150 if  acc > 127 then  acc = 127
1160 l1(14) = acc
1170 acc = ((25 + 0) * (a% + 128)) + -4044
1180 if  acc>0 then  acc=(acc*1169513172 + 2^30) / 2^31
1190 if  acc<=0 then  acc=(acc*1169513172 + (1-2^30)) / 2^31
1200 acc = (int((acc)+0.5) / 2^6) + -128
1210 acc = int((acc)+0.5)
1220 if  acc < -128 then  acc = -128
1230 if  acc > 127 then  acc = 127
1240 l1(15) = acc
1250 acc = ((-33 + 0) * (a% + 128)) + 0
1260 if  acc>0 then  acc=(acc*1169513172 + 2^30) / 2^31
1270 if  acc<=0 then  acc=(acc*1169513172 + (1-2^30)) / 2^31
1280 acc = (int((acc)+0.5) / 2^6) + -128
1290 acc = int((acc)+0.5)
1300 if  acc < -128 then  acc = -128
1310 if  acc > 127 then  acc = 127
1320 l1(16) = acc
1330 acc = 0
1340 acc = acc + ((-18 + 0) * (l1(1) + 128))
1350 acc = acc + ((-4 + 0) * (l1(2) + 128))
1360 acc = acc + ((0 + 0) * (l1(3) + 128))
1370 acc = acc + ((-20 + 0) * (l1(4) + 128))
1380 acc = acc + ((5 + 0) * (l1(5) + 128))
1390 acc = acc + ((23 + 0) * (l1(6) + 128))
1400 acc = acc + ((-17 + 0) * (l1(7) + 128))
1410 acc = acc + ((-20 + 0) * (l1(8) + 128))
1420 acc = acc + ((-26 + 0) * (l1(9) + 128))
1430 acc = acc + ((-8 + 0) * (l1(10) + 128))
1440 acc = acc + ((3 + 0) * (l1(11) + 128))
1450 acc = acc + ((1 + 0) * (l1(12) + 128))
1460 acc = acc + ((0 + 0) * (l1(13) + 128))
1470 acc = acc + ((-6 + 0) * (l1(14) + 128))
1480 acc = acc + ((-8 + 0) * (l1(15) + 128))
1490 acc = acc + ((-11 + 0) * (l1(16) + 128))
1500 acc = acc + 0
1510 if  acc>0 then  acc=(acc*1799998806 + 2^30) / 2^31
1520 if  acc<=0 then  acc=(acc*1799998806 + (1-2^30)) / 2^31
1530 acc = (int((acc)+0.5) / 2^5) + -128
1540 acc = int((acc)+0.5)
1550 if  acc < -128 then  acc = -128
1560 if  acc > 127 then  acc = 127
1570 l2(1) = acc
1580 acc = 0
1590 acc = acc + ((-36 + 0) * (l1(1) + 128))
1600 acc = acc + ((-21 + 0) * (l1(2) + 128))
1610 acc = acc + ((39 + 0) * (l1(3) + 128))
1620 acc = acc + ((20 + 0) * (l1(4) + 128))
1630 acc = acc + ((-15 + 0) * (l1(5) + 128))
1640 acc = acc + ((-34 + 0) * (l1(6) + 128))
1650 acc = acc + ((-30 + 0) * (l1(7) + 128))
1660 acc = acc + ((-37 + 0) * (l1(8) + 128))
1670 acc = acc + ((-16 + 0) * (l1(9) + 128))
1680 acc = acc + ((-34 + 0) * (l1(10) + 128))
1690 acc = acc + ((49 + 0) * (l1(11) + 128))
1700 acc = acc + ((6 + 0) * (l1(12) + 128))
1710 acc = acc + ((2 + 0) * (l1(13) + 128))
1720 acc = acc + ((-26 + 0) * (l1(14) + 128))
1730 acc = acc + ((-18 + 0) * (l1(15) + 128))
1740 acc = acc + ((-7 + 0) * (l1(16) + 128))
1750 acc = acc + 1205
1760 if  acc>0 then  acc=(acc*1799998806 + 2^30) / 2^31
1770 if  acc<=0 then  acc=(acc*1799998806 + (1-2^30)) / 2^31
1780 acc = (int((acc)+0.5) / 2^5) + -128
1790 acc = int((acc)+0.5)
1800 if  acc < -128 then  acc = -128
1810 if  acc > 127 then  acc = 127
1820 l2(2) = acc
1830 acc = 0
1840 acc = acc + ((0 + 0) * (l1(1) + 128))
1850 acc = acc + ((22 + 0) * (l1(2) + 128))
1860 acc = acc + ((7 + 0) * (l1(3) + 128))
1870 acc = acc + ((-32 + 0) * (l1(4) + 128))
1880 acc = acc + ((-2 + 0) * (l1(5) + 128))
1890 acc = acc + ((-1 + 0) * (l1(6) + 128))
1900 acc = acc + ((-23 + 0) * (l1(7) + 128))
1910 acc = acc + ((6 + 0) * (l1(8) + 128))
1920 acc = acc + ((-25 + 0) * (l1(9) + 128))
1930 acc = acc + ((-17 + 0) * (l1(10) + 128))
1940 acc = acc + ((-127 + 0) * (l1(11) + 128))
1950 acc = acc + ((27 + 0) * (l1(12) + 128))
1960 acc = acc + ((24 + 0) * (l1(13) + 128))
1970 acc = acc + ((-22 + 0) * (l1(14) + 128))
1980 acc = acc + ((-55 + 0) * (l1(15) + 128))
1990 acc = acc + ((1 + 0) * (l1(16) + 128))
2000 acc = acc + 2680
2010 if  acc>0 then  acc=(acc*1799998806 + 2^30) / 2^31
2020 if  acc<=0 then  acc=(acc*1799998806 + (1-2^30)) / 2^31
2030 acc = (int((acc)+0.5) / 2^5) + -128
2040 acc = int((acc)+0.5)
2050 if  acc < -128 then  acc = -128
2060 if  acc > 127 then  acc = 127
2070 l2(3) = acc
2080 acc = 0
2090 acc = acc + ((15 + 0) * (l1(1) + 128))
2100 acc = acc + ((0 + 0) * (l1(2) + 128))
2110 acc = acc + ((-38 + 0) * (l1(3) + 128))
2120 acc = acc + ((-9 + 0) * (l1(4) + 128))
2130 acc = acc + ((14 + 0) * (l1(5) + 128))
2140 acc = acc + ((-20 + 0) * (l1(6) + 128))
2150 acc = acc + ((19 + 0) * (l1(7) + 128))
2160 acc = acc + ((31 + 0) * (l1(8) + 128))
2170 acc = acc + ((4 + 0) * (l1(9) + 128))
2180 acc = acc + ((19 + 0) * (l1(10) + 128))
2190 acc = acc + ((-76 + 0) * (l1(11) + 128))
2200 acc = acc + ((-26 + 0) * (l1(12) + 128))
2210 acc = acc + ((-3 + 0) * (l1(13) + 128))
2220 acc = acc + ((6 + 0) * (l1(14) + 128))
2230 acc = acc + ((-71 + 0) * (l1(15) + 128))
2240 acc = acc + ((-32 + 0) * (l1(16) + 128))
2250 acc = acc + 1581
2260 if  acc>0 then  acc=(acc*1799998806 + 2^30) / 2^31
2270 if  acc<=0 then  acc=(acc*1799998806 + (1-2^30)) / 2^31
2280 acc = (int((acc)+0.5) / 2^5) + -128
2290 acc = int((acc)+0.5)
2300 if  acc < -128 then  acc = -128
2310 if  acc > 127 then  acc = 127
2320 l2(4) = acc
2330 acc = 0
2340 acc = acc + ((13 + 0) * (l1(1) + 128))
2350 acc = acc + ((-20 + 0) * (l1(2) + 128))
2360 acc = acc + ((-16 + 0) * (l1(3) + 128))
2370 acc = acc + ((-34 + 0) * (l1(4) + 128))
2380 acc = acc + ((-21 + 0) * (l1(5) + 128))
2390 acc = acc + ((-9 + 0) * (l1(6) + 128))
2400 acc = acc + ((5 + 0) * (l1(7) + 128))
2410 acc = acc + ((38 + 0) * (l1(8) + 128))
2420 acc = acc + ((26 + 0) * (l1(9) + 128))
2430 acc = acc + ((-28 + 0) * (l1(10) + 128))
2440 acc = acc + ((111 + 0) * (l1(11) + 128))
2450 acc = acc + ((26 + 0) * (l1(12) + 128))
2460 acc = acc + ((-22 + 0) * (l1(13) + 128))
2470 acc = acc + ((30 + 0) * (l1(14) + 128))
2480 acc = acc + ((53 + 0) * (l1(15) + 128))
2490 acc = acc + ((-33 + 0) * (l1(16) + 128))
2500 acc = acc + -1935
2510 if  acc>0 then  acc=(acc*1799998806 + 2^30) / 2^31
2520 if  acc<=0 then  acc=(acc*1799998806 + (1-2^30)) / 2^31
2530 acc = (int((acc)+0.5) / 2^5) + -128
2540 acc = int((acc)+0.5)
2550 if  acc < -128 then  acc = -128
2560 if  acc > 127 then  acc = 127
2570 l2(5) = acc
2580 acc = 0
2590 acc = acc + ((26 + 0) * (l1(1) + 128))
2600 acc = acc + ((-13 + 0) * (l1(2) + 128))
2610 acc = acc + ((-15 + 0) * (l1(3) + 128))
2620 acc = acc + ((25 + 0) * (l1(4) + 128))
2630 acc = acc + ((15 + 0) * (l1(5) + 128))
2640 acc = acc + ((3 + 0) * (l1(6) + 128))
2650 acc = acc + ((27 + 0) * (l1(7) + 128))
2660 acc = acc + ((-31 + 0) * (l1(8) + 128))
2670 acc = acc + ((-34 + 0) * (l1(9) + 128))
2680 acc = acc + ((19 + 0) * (l1(10) + 128))
2690 acc = acc + ((-10 + 0) * (l1(11) + 128))
2700 acc = acc + ((25 + 0) * (l1(12) + 128))
2710 acc = acc + ((-1 + 0) * (l1(13) + 128))
2720 acc = acc + ((-10 + 0) * (l1(14) + 128))
2730 acc = acc + ((27 + 0) * (l1(15) + 128))
2740 acc = acc + ((24 + 0) * (l1(16) + 128))
2750 acc = acc + 0
2760 if  acc>0 then  acc=(acc*1799998806 + 2^30) / 2^31
2770 if  acc<=0 then  acc=(acc*1799998806 + (1-2^30)) / 2^31
2780 acc = (int((acc)+0.5) / 2^5) + -128
2790 acc = int((acc)+0.5)
2800 if  acc < -128 then  acc = -128
2810 if  acc > 127 then  acc = 127
2820 l2(6) = acc
2830 acc = 0
2840 acc = acc + ((-16 + 0) * (l1(1) + 128))
2850 acc = acc + ((28 + 0) * (l1(2) + 128))
2860 acc = acc + ((-38 + 0) * (l1(3) + 128))
2870 acc = acc + ((27 + 0) * (l1(4) + 128))
2880 acc = acc + ((27 + 0) * (l1(5) + 128))
2890 acc = acc + ((32 + 0) * (l1(6) + 128))
2900 acc = acc + ((-27 + 0) * (l1(7) + 128))
2910 acc = acc + ((26 + 0) * (l1(8) + 128))
2920 acc = acc + ((-11 + 0) * (l1(9) + 128))
2930 acc = acc + ((-1 + 0) * (l1(10) + 128))
2940 acc = acc + ((-106 + 0) * (l1(11) + 128))
2950 acc = acc + ((11 + 0) * (l1(12) + 128))
2960 acc = acc + ((0 + 0) * (l1(13) + 128))
2970 acc = acc + ((1 + 0) * (l1(14) + 128))
2980 acc = acc + ((-51 + 0) * (l1(15) + 128))
2990 acc = acc + ((-34 + 0) * (l1(16) + 128))
3000 acc = acc + 2713
3010 if  acc>0 then  acc=(acc*1799998806 + 2^30) / 2^31
3020 if  acc<=0 then  acc=(acc*1799998806 + (1-2^30)) / 2^31
3030 acc = (int((acc)+0.5) / 2^5) + -128
3040 acc = int((acc)+0.5)
3050 if  acc < -128 then  acc = -128
3060 if  acc > 127 then  acc = 127
3070 l2(7) = acc
3080 acc = 0
3090 acc = acc + ((13 + 0) * (l1(1) + 128))
3100 acc = acc + ((-10 + 0) * (l1(2) + 128))
3110 acc = acc + ((22 + 0) * (l1(3) + 128))
3120 acc = acc + ((-29 + 0) * (l1(4) + 128))
3130 acc = acc + ((-19 + 0) * (l1(5) + 128))
3140 acc = acc + ((-4 + 0) * (l1(6) + 128))
3150 acc = acc + ((14 + 0) * (l1(7) + 128))
3160 acc = acc + ((-23 + 0) * (l1(8) + 128))
3170 acc = acc + ((-6 + 0) * (l1(9) + 128))
3180 acc = acc + ((-21 + 0) * (l1(10) + 128))
3190 acc = acc + ((92 + 0) * (l1(11) + 128))
3200 acc = acc + ((-4 + 0) * (l1(12) + 128))
3210 acc = acc + ((29 + 0) * (l1(13) + 128))
3220 acc = acc + ((2 + 0) * (l1(14) + 128))
3230 acc = acc + ((91 + 0) * (l1(15) + 128))
3240 acc = acc + ((-30 + 0) * (l1(16) + 128))
3250 acc = acc + -2050
3260 if  acc>0 then  acc=(acc*1799998806 + 2^30) / 2^31
3270 if  acc<=0 then  acc=(acc*1799998806 + (1-2^30)) / 2^31
3280 acc = (int((acc)+0.5) / 2^5) + -128
3290 acc = int((acc)+0.5)
3300 if  acc < -128 then  acc = -128
3310 if  acc > 127 then  acc = 127
3320 l2(8) = acc
3330 acc = 0
3340 acc = acc + ((-31 + 0) * (l1(1) + 128))
3350 acc = acc + ((-11 + 0) * (l1(2) + 128))
3360 acc = acc + ((21 + 0) * (l1(3) + 128))
3370 acc = acc + ((-20 + 0) * (l1(4) + 128))
3380 acc = acc + ((-12 + 0) * (l1(5) + 128))
3390 acc = acc + ((0 + 0) * (l1(6) + 128))
3400 acc = acc + ((19 + 0) * (l1(7) + 128))
3410 acc = acc + ((5 + 0) * (l1(8) + 128))
3420 acc = acc + ((-20 + 0) * (l1(9) + 128))
3430 acc = acc + ((12 + 0) * (l1(10) + 128))
3440 acc = acc + ((29 + 0) * (l1(11) + 128))
3450 acc = acc + ((20 + 0) * (l1(12) + 128))
3460 acc = acc + ((14 + 0) * (l1(13) + 128))
3470 acc = acc + ((-25 + 0) * (l1(14) + 128))
3480 acc = acc + ((11 + 0) * (l1(15) + 128))
3490 acc = acc + ((-12 + 0) * (l1(16) + 128))
3500 acc = acc + 1295
3510 if  acc>0 then  acc=(acc*1799998806 + 2^30) / 2^31
3520 if  acc<=0 then  acc=(acc*1799998806 + (1-2^30)) / 2^31
3530 acc = (int((acc)+0.5) / 2^5) + -128
3540 acc = int((acc)+0.5)
3550 if  acc < -128 then  acc = -128
3560 if  acc > 127 then  acc = 127
3570 l2(9) = acc
3580 acc = 0
3590 acc = acc + ((25 + 0) * (l1(1) + 128))
3600 acc = acc + ((0 + 0) * (l1(2) + 128))
3610 acc = acc + ((-41 + 0) * (l1(3) + 128))
3620 acc = acc + ((5 + 0) * (l1(4) + 128))
3630 acc = acc + ((39 + 0) * (l1(5) + 128))
3640 acc = acc + ((2 + 0) * (l1(6) + 128))
3650 acc = acc + ((21 + 0) * (l1(7) + 128))
3660 acc = acc + ((-22 + 0) * (l1(8) + 128))
3670 acc = acc + ((-22 + 0) * (l1(9) + 128))
3680 acc = acc + ((2 + 0) * (l1(10) + 128))
3690 acc = acc + ((-101 + 0) * (l1(11) + 128))
3700 acc = acc + ((0 + 0) * (l1(12) + 128))
3710 acc = acc + ((12 + 0) * (l1(13) + 128))
3720 acc = acc + ((-6 + 0) * (l1(14) + 128))
3730 acc = acc + ((-24 + 0) * (l1(15) + 128))
3740 acc = acc + ((-22 + 0) * (l1(16) + 128))
3750 acc = acc + 2516
3760 if  acc>0 then  acc=(acc*1799998806 + 2^30) / 2^31
3770 if  acc<=0 then  acc=(acc*1799998806 + (1-2^30)) / 2^31
3780 acc = (int((acc)+0.5) / 2^5) + -128
3790 acc = int((acc)+0.5)
3800 if  acc < -128 then  acc = -128
3810 if  acc > 127 then  acc = 127
3820 l2(10) = acc
3830 acc = 0
3840 acc = acc + ((-3 + 0) * (l1(1) + 128))
3850 acc = acc + ((0 + 0) * (l1(2) + 128))
3860 acc = acc + ((20 + 0) * (l1(3) + 128))
3870 acc = acc + ((-3 + 0) * (l1(4) + 128))
3880 acc = acc + ((11 + 0) * (l1(5) + 128))
3890 acc = acc + ((2 + 0) * (l1(6) + 128))
3900 acc = acc + ((-17 + 0) * (l1(7) + 128))
3910 acc = acc + ((-18 + 0) * (l1(8) + 128))
3920 acc = acc + ((6 + 0) * (l1(9) + 128))
3930 acc = acc + ((-18 + 0) * (l1(10) + 128))
3940 acc = acc + ((1 + 0) * (l1(11) + 128))
3950 acc = acc + ((13 + 0) * (l1(12) + 128))
3960 acc = acc + ((6 + 0) * (l1(13) + 128))
3970 acc = acc + ((-26 + 0) * (l1(14) + 128))
3980 acc = acc + ((-9 + 0) * (l1(15) + 128))
3990 acc = acc + ((17 + 0) * (l1(16) + 128))
4000 acc = acc + -441
4010 if  acc>0 then  acc=(acc*1799998806 + 2^30) / 2^31
4020 if  acc<=0 then  acc=(acc*1799998806 + (1-2^30)) / 2^31
4030 acc = (int((acc)+0.5) / 2^5) + -128
4040 acc = int((acc)+0.5)
4050 if  acc < -128 then  acc = -128
4060 if  acc > 127 then  acc = 127
4070 l2(11) = acc
4080 acc = 0
4090 acc = acc + ((-9 + 0) * (l1(1) + 128))
4100 acc = acc + ((9 + 0) * (l1(2) + 128))
4110 acc = acc + ((-8 + 0) * (l1(3) + 128))
4120 acc = acc + ((-15 + 0) * (l1(4) + 128))
4130 acc = acc + ((33 + 0) * (l1(5) + 128))
4140 acc = acc + ((-1 + 0) * (l1(6) + 128))
4150 acc = acc + ((14 + 0) * (l1(7) + 128))
4160 acc = acc + ((-13 + 0) * (l1(8) + 128))
4170 acc = acc + ((-20 + 0) * (l1(9) + 128))
4180 acc = acc + ((18 + 0) * (l1(10) + 128))
4190 acc = acc + ((38 + 0) * (l1(11) + 128))
4200 acc = acc + ((29 + 0) * (l1(12) + 128))
4210 acc = acc + ((-14 + 0) * (l1(13) + 128))
4220 acc = acc + ((-23 + 0) * (l1(14) + 128))
4230 acc = acc + ((40 + 0) * (l1(15) + 128))
4240 acc = acc + ((24 + 0) * (l1(16) + 128))
4250 acc = acc + 1206
4260 if  acc>0 then  acc=(acc*1799998806 + 2^30) / 2^31
4270 if  acc<=0 then  acc=(acc*1799998806 + (1-2^30)) / 2^31
4280 acc = (int((acc)+0.5) / 2^5) + -128
4290 acc = int((acc)+0.5)
4300 if  acc < -128 then  acc = -128
4310 if  acc > 127 then  acc = 127
4320 l2(12) = acc
4330 acc = 0
4340 acc = acc + ((-32 + 0) * (l1(1) + 128))
4350 acc = acc + ((-5 + 0) * (l1(2) + 128))
4360 acc = acc + ((-13 + 0) * (l1(3) + 128))
4370 acc = acc + ((-12 + 0) * (l1(4) + 128))
4380 acc = acc + ((5 + 0) * (l1(5) + 128))
4390 acc = acc + ((29 + 0) * (l1(6) + 128))
4400 acc = acc + ((29 + 0) * (l1(7) + 128))
4410 acc = acc + ((-5 + 0) * (l1(8) + 128))
4420 acc = acc + ((-3 + 0) * (l1(9) + 128))
4430 acc = acc + ((30 + 0) * (l1(10) + 128))
4440 acc = acc + ((-4 + 0) * (l1(11) + 128))
4450 acc = acc + ((17 + 0) * (l1(12) + 128))
4460 acc = acc + ((-24 + 0) * (l1(13) + 128))
4470 acc = acc + ((7 + 0) * (l1(14) + 128))
4480 acc = acc + ((9 + 0) * (l1(15) + 128))
4490 acc = acc + ((3 + 0) * (l1(16) + 128))
4500 acc = acc + 0
4510 if  acc>0 then  acc=(acc*1799998806 + 2^30) / 2^31
4520 if  acc<=0 then  acc=(acc*1799998806 + (1-2^30)) / 2^31
4530 acc = (int((acc)+0.5) / 2^5) + -128
4540 acc = int((acc)+0.5)
4550 if  acc < -128 then  acc = -128
4560 if  acc > 127 then  acc = 127
4570 l2(13) = acc
4580 acc = 0
4590 acc = acc + ((18 + 0) * (l1(1) + 128))
4600 acc = acc + ((-14 + 0) * (l1(2) + 128))
4610 acc = acc + ((54 + 0) * (l1(3) + 128))
4620 acc = acc + ((-5 + 0) * (l1(4) + 128))
4630 acc = acc + ((-36 + 0) * (l1(5) + 128))
4640 acc = acc + ((28 + 0) * (l1(6) + 128))
4650 acc = acc + ((-7 + 0) * (l1(7) + 128))
4660 acc = acc + ((-17 + 0) * (l1(8) + 128))
4670 acc = acc + ((-13 + 0) * (l1(9) + 128))
4680 acc = acc + ((-25 + 0) * (l1(10) + 128))
4690 acc = acc + ((111 + 0) * (l1(11) + 128))
4700 acc = acc + ((12 + 0) * (l1(12) + 128))
4710 acc = acc + ((29 + 0) * (l1(13) + 128))
4720 acc = acc + ((0 + 0) * (l1(14) + 128))
4730 acc = acc + ((69 + 0) * (l1(15) + 128))
4740 acc = acc + ((-3 + 0) * (l1(16) + 128))
4750 acc = acc + -2132
4760 if  acc>0 then  acc=(acc*1799998806 + 2^30) / 2^31
4770 if  acc<=0 then  acc=(acc*1799998806 + (1-2^30)) / 2^31
4780 acc = (int((acc)+0.5) / 2^5) + -128
4790 acc = int((acc)+0.5)
4800 if  acc < -128 then  acc = -128
4810 if  acc > 127 then  acc = 127
4820 l2(14) = acc
4830 acc = 0
4840 acc = acc + ((14 + 0) * (l1(1) + 128))
4850 acc = acc + ((-16 + 0) * (l1(2) + 128))
4860 acc = acc + ((11 + 0) * (l1(3) + 128))
4870 acc = acc + ((25 + 0) * (l1(4) + 128))
4880 acc = acc + ((26 + 0) * (l1(5) + 128))
4890 acc = acc + ((-6 + 0) * (l1(6) + 128))
4900 acc = acc + ((-32 + 0) * (l1(7) + 128))
4910 acc = acc + ((25 + 0) * (l1(8) + 128))
4920 acc = acc + ((31 + 0) * (l1(9) + 128))
4930 acc = acc + ((19 + 0) * (l1(10) + 128))
4940 acc = acc + ((54 + 0) * (l1(11) + 128))
4950 acc = acc + ((28 + 0) * (l1(12) + 128))
4960 acc = acc + ((18 + 0) * (l1(13) + 128))
4970 acc = acc + ((-21 + 0) * (l1(14) + 128))
4980 acc = acc + ((59 + 0) * (l1(15) + 128))
4990 acc = acc + ((12 + 0) * (l1(16) + 128))
5000 acc = acc + -1717
5010 if  acc>0 then  acc=(acc*1799998806 + 2^30) / 2^31
5020 if  acc<=0 then  acc=(acc*1799998806 + (1-2^30)) / 2^31
5030 acc = (int((acc)+0.5) / 2^5) + -128
5040 acc = int((acc)+0.5)
5050 if  acc < -128 then  acc = -128
5060 if  acc > 127 then  acc = 127
5070 l2(15) = acc
5080 acc = 0
5090 acc = acc + ((-76 + 0) * (l1(1) + 128))
5100 acc = acc + ((-53 + 0) * (l1(2) + 128))
5110 acc = acc + ((-26 + 0) * (l1(3) + 128))
5120 acc = acc + ((19 + 0) * (l1(4) + 128))
5130 acc = acc + ((-6 + 0) * (l1(5) + 128))
5140 acc = acc + ((-21 + 0) * (l1(6) + 128))
5150 acc = acc + ((-15 + 0) * (l1(7) + 128))
5160 acc = acc + ((6 + 0) * (l1(8) + 128))
5170 acc = acc + ((28 + 0) * (l1(9) + 128))
5180 acc = acc + ((-6 + 0) * (l1(10) + 128))
5190 acc = acc + ((24 + 0) * (l1(11) + 128))
5200 acc = acc + ((-27 + 0) * (l1(12) + 128))
5210 acc = acc + ((-21 + 0) * (l1(13) + 128))
5220 acc = acc + ((-53 + 0) * (l1(14) + 128))
5230 acc = acc + ((12 + 0) * (l1(15) + 128))
5240 acc = acc + ((-12 + 0) * (l1(16) + 128))
5250 acc = acc + 1354
5260 if  acc>0 then  acc=(acc*1799998806 + 2^30) / 2^31
5270 if  acc<=0 then  acc=(acc*1799998806 + (1-2^30)) / 2^31
5280 acc = (int((acc)+0.5) / 2^5) + -128
5290 acc = int((acc)+0.5)
5300 if  acc < -128 then  acc = -128
5310 if  acc > 127 then  acc = 127
5320 l2(16) = acc
5330 acc = 0
5340 acc = acc + ((33 + 0) * (l2(1) + 128))
5350 acc = acc + ((-91 + 0) * (l2(2) + 128))
5360 acc = acc + ((-117 + 0) * (l2(3) + 128))
5370 acc = acc + ((-54 + 0) * (l2(4) + 128))
5380 acc = acc + ((94 + 0) * (l2(5) + 128))
5390 acc = acc + ((29 + 0) * (l2(6) + 128))
5400 acc = acc + ((-50 + 0) * (l2(7) + 128))
5410 acc = acc + ((66 + 0) * (l2(8) + 128))
5420 acc = acc + ((-99 + 0) * (l2(9) + 128))
5430 acc = acc + ((-50 + 0) * (l2(10) + 128))
5440 acc = acc + ((31 + 0) * (l2(11) + 128))
5450 acc = acc + ((-80 + 0) * (l2(12) + 128))
5460 acc = acc + ((-33 + 0) * (l2(13) + 128))
5470 acc = acc + ((84 + 0) * (l2(14) + 128))
5480 acc = acc + ((47 + 0) * (l2(15) + 128))
5490 acc = acc + ((-127 + 0) * (l2(16) + 128))
5500 acc = acc + -4212
5510 if  acc>0 then  acc=(acc*1623303118 + 2^30) / 2^31
5520 if  acc<=0 then  acc=(acc*1623303118 + (1-2^30)) / 2^31
5530 acc = (int((acc)+0.5) / 2^7) + 4
5540 acc = int((acc)+0.5)
5550 if  acc < -128 then  acc = -128
5560 if  acc > 127 then  acc = 127
5570 result = (acc - 4) * 0.008472
5580 print  result,ti