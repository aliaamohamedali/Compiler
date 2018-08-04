  EEXP 	  START 	 000000              
       	 EXTREF 	 XREAD,XWRITE,LPUSH,LPOP
       	    STL 	 RETADR              
       	      J 	 {EXADDR}            
     a 	   RESW 	 1                   
     b 	   RESW 	 1                   
  left 	   RESW 	 1                   
 right 	   RESW 	 1                   
result 	   RESW 	 1                   
       	  +JSUB 	 XREAD               
       	   WORD 	 2                   
       	   WORD 	 a                   
       	   WORD 	 b                   
       	  +JSUB 	 XWRITE              
       	   WORD 	 2                   
       	   WORD 	 a                   
       	   WORD 	 b                   
       	    LDA 	 a                   
       	    ADD 	 b                   
       	    ADD 	 left                
       	    STA 	 left                
       	    LDA 	 a                   
       	    ADD 	 b                   
       	    ADD 	 right               
       	    STA 	 right               
       	    MUL 	 left                
       	    STA 	 result              
       	  +JSUB 	 XWRITE              
       	   WORD 	 1                   
       	   WORD 	 result              
       	    END 	                     
