#回答
- サイコロの１〜６の目は等間隔で出るので、均等に分布した擬似乱数であるかどうかが問題となる。
- 均等に分布した擬似乱数を返すnextInt(), nextLong(), nextFloat(), nextDouble()で違いがあるか考察する。サイコロの数２個とし、試行回数を100, 1000, 10000回でそれぞれ実行した。全てのパターンでサイコロの目は中央の値が最も出た回数が多く、中央の値から遠くなる程出た回数が少なくなった。よって均等な分布の擬似乱数であればどれも同じである。

---------------------------
```
trialNum: 100
the case of nextInt()
  2 | 3
  3 | 6
  4 | 12
  5 | 7
  6 | 11
  7 | 14
  8 | 18
  9 | 12
 10 | 4
 11 | 9
 12 | 4

the case of nextLong()
  2 | 1
  3 | 4
  4 | 16
  5 | 7
  6 | 10
  7 | 18
  8 | 16
  9 | 13
 10 | 5
 11 | 6
 12 | 4

the case of nextFloat()
  2 | 2
  3 | 7
  4 | 4
  5 | 14
  6 | 6
  7 | 20
  8 | 17
  9 | 15
 10 | 7
 11 | 6
 12 | 2

the case of nextDuble()
  2 | 2
  3 | 7
  4 | 5
  5 | 17
  6 | 14
  7 | 16
  8 | 19
  9 | 8
 10 | 4
 11 | 7
 12 | 1

trialNum: 1000
the case of nextInt()
  2 | 25
  3 | 69
  4 | 74
  5 | 114
  6 | 145
  7 | 156
  8 | 153
  9 | 94
 10 | 89
 11 | 58
 12 | 23

the case of nextLong()
  2 | 22
  3 | 56
  4 | 90
  5 | 119
  6 | 120
  7 | 172
  8 | 135
  9 | 114
 10 | 79
 11 | 60
 12 | 33

the case of nextFloat()
  2 | 27
  3 | 51
  4 | 93
  5 | 99
  6 | 128
  7 | 175
  8 | 154
  9 | 112
 10 | 87
 11 | 55
 12 | 19

the case of nextDuble()
  2 | 32
  3 | 64
  4 | 76
  5 | 110
  6 | 128
  7 | 156
  8 | 154
  9 | 118
 10 | 74
 11 | 54
 12 | 34

trialNum: 10000
the case of nextInt()
  2 | 259
  3 | 519
  4 | 834
  5 | 1148
  6 | 1411
  7 | 1604
  8 | 1412
  9 | 1111
 10 | 848
 11 | 584
 12 | 270

the case of nextLong()
  2 | 278
  3 | 567
  4 | 827
  5 | 1082
  6 | 1436
  7 | 1685
  8 | 1427
  9 | 1093
 10 | 829
 11 | 535
 12 | 241

the case of nextFloat()
  2 | 277
  3 | 540
  4 | 842
  5 | 1146
  6 | 1374
  7 | 1656
  8 | 1399
  9 | 1084
 10 | 820
 11 | 572
 12 | 290

the case of nextDuble()
  2 | 258
  3 | 545
  4 | 793
  5 | 1154
  6 | 1395
  7 | 1662
  8 | 1406
  9 | 1124
 10 | 802
 11 | 569
 12 | 292
 ```
---------------------------