BASICS 	  START 	 000000              
       	 EXTREF 	 XREAD,XWRITE        
       	    STL 	 RETADR              
       	      J 	 {EXADDR}            
     X 	   RESW 	 1                   
     Y 	   RESW 	 1                   
     A 	   RESW 	 1                   
     B 	   RESW 	 1                   
     C 	   RESW 	 1                   
     Z 	   RESW 	 1                   
     I 	   RESW 	 1                   
     J 	   RESW 	 1                   
     M 	   RESW 	 1                   
     N 	   RESW 	 1                   
       	  +JSUB 	 XREAD               
       	   WORD 	 4                   
       	   WORD 	 X                   
       	   WORD 	 A                   
       	   WORD 	 Z                   
       	   WORD 	 B                   
       	    LDA 	 X                   
       	    ADD 	 Y                   
       	    RMO 	 A,L                 
       	    LDA 	 A                   
       	    ADD 	 B                   
 LOOP1 	    STA 	 I                   
       	   CMPR 	 A, L                
       	    JLT 	 LOOP2               
       	    LDA 	 X                   
       	    ADD 	 Z                   
       	    STA 	 C                   
       	    MUL 	 B                   
       	    STA 	 C                   
       	    LDA 	 A                   
       	    MUL 	 B                   
       	    STA 	 T1                  
       	    LDA 	 C                   
       	    MUL 	 Y                   
       	    ADD 	 T1                  
       	    STA 	 Z                   
       	    LDA 	 X                   
       	    ADD 	 B                   
       	    STA 	 A                   
       	  +JSUB 	 XWRITE              
       	   WORD 	 3                   
       	   WORD 	 A                   
       	   WORD 	 C                   
       	   WORD 	 Z                   
       	    LDA 	 I                   
       	    ADD 	 #1                  
       	      J 	 LOOP1               
 LOOP2 	    NOP 	                     
       	  +JSUB 	 XREAD               
       	   WORD 	 1                   
       	   WORD 	 X                   
       	   RESW 	 T1                  
       	    END 	                     
