ZapposMailer
============

Zappos challenge

ZapposMailer is process that runs infinitely reading user data from customer.dat. 
My implementation:

Assumption: 
1) The user data is in Zappos' database and directly read from there. Since it is not available
   in the Zappos API, I created my file customer.dat for purpose of this app.
2) If there are multiple styles of a particular product, the user is mailed with the one which has the highest discount.

ZapposMailer runs in an infinite loop. Steps:
1) It reads data from customer.data, processes it i.e. sends mails to users if discount > 20% and keeps 
   records with discout < 20% for future processing. In every cycle the data from customer.dat is erased completely.
   [Some kind of backup can be done to prevent loss in case of system crash which is not implemented in ZapposMailer]
    
2) At any time you can add records to customer.dat for processing. This would automatically send mails based on the 
   discount.

3) In every cycle, records from previous cycles which have to be continuously checked for discount > 20% and the 
   new records which might have been entered in customer.dat are processed.
   
   Note: I should have used the Product API instead of Search API as Product API would let me make 1 API call for 10
   products whereas the Search API makes 1 call for 1 product. 
