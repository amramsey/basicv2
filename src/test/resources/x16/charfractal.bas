5 PRINT CHR$(147);
7 TI$="000000"
8 DIMSD,V,U,A,B,N,S,I,R,Z,X,H,Y,D
10 X1=79:Y1=58:NS=48:NE=33:Z=0
20 I1=-1.0:I2=1.0:R1=-2.0:R2=1.0
30 H=(R2-R1)/X1:S2=(I2-I1)/Y1:S=127:D=14848:YH=Y1/2
40 FORY=ZTOYH:I=I1+Y*S2:R=R1
60 FORX=ZTOX1:V=R:U=I:FORN=48TO33STEP-1:A=V*V:B=U*U
110 IFA+B<=4THENU=U*V*2+I:V=A-B+R:NEXT
130 S=S+1:SD=2*S:VPOKE0,SD,N:VPOKE0,SD+D,N:R=R+H:NEXTX:S=S+48:D=D-512:NEXT
10000 PRINT"time: ";TI$;
10010 GETA$:IF A$="" THEN 10010
