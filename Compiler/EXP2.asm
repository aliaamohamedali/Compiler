  EEXP 	  START 	 000000              
       	 EXTREF 	 XREAD,XWRITE,LPUSH,LPOP
       	    STL 	 RETADR              
       	      J 	 {EXADDR}            
     a 	   RESW 	 1                   
     b 	   RESW 	 1                   
     c 	   RESW 	 1                   
     f 	   RESW 	 1                   
     h 	   RESW 	 1                   
       	  +JSUB 	 XREAD               
       	   WORD 	 3                   
       	   WORD 	 a                   
       	   WORD 	 b                   
       	   WORD 	 c                   
       	    LDA 	 a                   
       	    ADD 	 b                   
       	    STA 	 f                   
       	    LDA 	 a                   
       	    MUL 	 b                   
       	    STA 	 h                   
       	  +JSUB 	 XWRITE              
       	   WORD 	 2                   
       	   WORD 	 f                   
       	   WORD 	 h                   
       	    END 	                     
