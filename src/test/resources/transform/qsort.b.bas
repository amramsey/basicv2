1 rem  @r=z
10 dim s%(30):sm%=1024:n%=255
20 print "{clr}{light gray}{ct n}{control-q}{control-q}{control-q}{control-q}{control-q}{control-q}{control-q}{control-q}"
30 for i=0 to 255: read a%: poke sm%+i,a%: next 
40 print "{ctrl 9}{211}{208}{193}{195}{197}{ctrl 0} to start"
50 get a$: if a$<>" " then 50
60 print "{sh cursor down}{211}{207}{210}{212}{201}{206}{199}...           "
70 ti$="000000"
80 :
100 rem  quicksort algorithm
110 s%(1)=0:s%(2)=n%:p%=2
120 l%=s%(p%):p%=p%-1:f%=s%(p%):p%=p%-1:i%=f%
130 j%=l%:g%=(f%+l%):g%=g%/2:g%=g%+sm%:d%=peek(g%)
140 if peek(sm%+i%)<d% then i%=i%+1: goto 140
150 if peek(sm%+j%)>d% then j%=j%-1: goto 150
160 if i%>j% then 190
165 x%=sm%+i%:y%=sm%+j%
170 t%=peek(x%): poke x%,peek(y%): poke y%,t%
180 i%=i%+1:j%=j%-1
190 if i%<=j% then 140
200 if f%<j% then p%=p%+1:s%(p%)=f%:p%=p%+1:s%(p%)=j%
210 f%=i%: if f%<l% then 130
220 if p%<>0 then 120
230 :
240 z=ti/60:z=int(z*100)/100
250 print "{sh cursor down}{198}{201}{206}{201}{211}{200}{197}{196}!           "
260 print "{control-q}{212}{201}{205}{197}:";z;"secs."
270 :
500 data  106,20,107,230,80,33,205,88
501 data  36,84,194,165,243,7,1,56
502 data  130,22,214,144,89,45,93,131
503 data  207,16,104,117,147,48,85,163
504 data  210,92,196,69,157,96,30,115
505 data  209,99,119,100,63,77,187,121
506 data  244,103,123,215,42,116,211,250
507 data  87,110,128,32,43,178,162,19
508 data  248,49,135,61,79,76,52,139
509 data  25,27,5,150,91,132,12,225
510 data  155,112,113,167,125,26,177,62
511 data  148,75,189,82,51,95,169,102
512 data  108,246,29,40,4,193,204,54
513 data  98,126,146,58,159,114,228,201
514 data  57,66,72,186,188,240,236,245
515 data  35,143,226,145,74,47,251,120
516 data  129,140,153,237,235,154,255,50
517 data  223,9,229,232,160,10,168,97
518 data  175,21,171,78,233,124,241,253
519 data  138,185,18,105,55,73,71,170
520 data  249,242,28,218,6,219,195,202
521 data  247,231,252,11,83,164,212,64
522 data  137,44,70,8,213,53,0,222
523 data  176,2,174,183,152,59,238,224
524 data  31,197,14,199,13,161,38,166
525 data  217,15,221,158,101,234,65,90
526 data  227,122,37,208,156,23,206,149
527 data  60,94,198,172,182,111,118,192
528 data  41,190,203,136,239,200,141,127
529 data  68,254,180,191,17,133,142,34
530 data  216,151,109,134,81,86,24,220
531 data  67,46,3,39,173,179,181,184