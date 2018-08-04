SUMPROG 	  START 	 000000              
       	 EXTREF 	 XREAD,XWRITE,LPUSH,LPOP
       	    STL 	 RETADR              
       	      J 	 {EXADDR}            
     X 	   RESW 	 1                   
     Y 	   RESW 	 1                   
   SUM 	   RESW 	 1                   
       	  +JSUB 	 XREAD               
       	   WORD 	 2                   
       	   WORD 	 X                   
       	   WORD 	 Y                   
       	    LDA 	 X                   
       	    ADD 	 Y                   
       	    STA 	 SUM                 
       	  +JSUB 	 XWRITE              
       	   WORD 	 1                   
       	   WORD 	 SUM                 
       	    END 	                     
