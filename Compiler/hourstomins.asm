HMINSEC 	  START 	 000000              
       	 EXTREF 	 XREAD,XWRITE,LPUSH,LPOP
       	    STL 	 RETADR              
       	      J 	 {EXADDR}            
 hours 	   RESW 	 1                   
convert_to 	   RESW 	 1                   
  mins 	   RESW 	 1                   
  secs 	   RESW 	 1                   
sixity 	   RESW 	 1                   
       	  +JSUB 	 XREAD               
       	   WORD 	 1                   
       	   WORD 	 hours               
       	  +JSUB 	 XREAD               
       	   WORD 	 1                   
       	   WORD 	 convert_to          
       	    LDA 	 hours               
       	    MUL 	 sixity              
       	    STA 	 mins                
       	    MUL 	 sixity              
       	    STA 	 secs                
       	  +JSUB 	 XWRITE              
       	   WORD 	 2                   
       	   WORD 	 mins                
       	   WORD 	 secs                
       	    END 	                     
