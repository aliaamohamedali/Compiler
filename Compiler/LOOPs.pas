PROGRAM SUMPROG
VAR
  N,SUM,I,M,J,one,zero
BEGIN 
READ(N,M);
SUM:= zero; 
FOR I:= one TO N DO 
   BEGIN
 FOR J:= one TO M DO
   BEGIN
     SUM := (SUM + I)*J
   END
 END;
WRITE(SUM)
END. 
 

