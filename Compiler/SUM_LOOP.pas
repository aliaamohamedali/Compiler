PROGRAM SUMPROG
VAR
  N,SUM,I,one, zero
BEGIN 
READ(N);
SUM:= zero;
FOR I:= one TO N DO 
   BEGIN 
     SUM := SUM + I
   END; 
WRITE(SUM)
END. 
 

