SUMPROG 	  START 	 000000              
       	 EXTREF 	 XREAD,XWRITE,LPUSH,LPOP
       	    STL 	 RETADR              
       	      J 	 {EXADDR}            
     N 	   RESW 	 1                   
   SUM 	   RESW 	 1                   
     I 	   RESW 	 1                   
     M 	   RESW 	 1                   
     J 	   RESW 	 1                   
   one 	   RESW 	 1                   
  zero 	   RESW 	 1                   
       	  +JSUB 	 XREAD               
       	   WORD 	 2                   
       	   WORD 	 N                   
       	   WORD 	 M                   
       	    LDA 	 zero                
       	    STA 	 SUM                 
       	    LDA 	 N                   
       	  +JSUB 	 LPUSH               
       	    RMO 	 A,L                 
       	    LDA 	 one                 
 LOOP1 	    STA 	 I                   
       	   CMPR 	 A, L                
       	    JLT 	 LOOP2               
       	    LDA 	 M                   
       	  +JSUB 	 LPUSH               
       	    RMO 	 A,L                 
       	    LDA 	 one                 
 LOOP3 	    STA 	 J                   
       	   CMPR 	 A, L                
       	    JLT 	 LOOP4               
       	    LDA 	 SUM                 
       	    ADD 	 I                   
       	    MUL 	 J                   
       	    STA 	 SUM                 
       	    LDA 	 J                   
       	    ADD 	 #1                  
       	      J 	 LOOP3               
 LOOP4 	  +JSUB 	 LPOP                
       	    LDA 	 I                   
       	    ADD 	 #1                  
       	      J 	 LOOP1               
 LOOP2 	  +JSUB 	 LPOP                
       	  +JSUB 	 XWRITE              
       	   WORD 	 1                   
       	   WORD 	 SUM                 
       	    END 	                     
