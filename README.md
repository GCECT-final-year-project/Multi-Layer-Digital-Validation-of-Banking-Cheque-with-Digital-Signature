# Multi-Layer-Digital-Validation-of-Banking-Cheque-with-Digital-Signature

The proposed concept is a unique data security protocol for digital validation of cheque in addition to existing verification system. We are trying to meet all the critical data security issues like authentication, confidentiality, integrity, and non-repudiation of data while authenticating the issued cheque.

The protocol is initiated when the user who gives the cheque to the client, logs in to the bank server with his username and password and OTP received on his phone. The user then uploads the cheque details that he issued against the client namely: Amount, Date, Payee name, Cheque number.

The details of the cheque are embedded in a digital cheque image along with the fingerprint with forms of visible and invisible watermarking techniques. We have added visible watermarking/digital signature to strengthen data integrity and non-repudiation. Client generates a digital signature of the cheque by encrypting the Message Digest of the cheque informations using private key of the client and then embedding the signature into the cheque image.

Now the client uploads the cheque to the bank server.

The cheque details are then extracted with the help of an OCR. In the next step, the bank decrypts the embedded data in the cheque along with the user’s fingerprint using the data of the user already stored in the database in the cheque like Secret Key and OTP. Now firstly, the cheque details extracted from the OCR are matched with that uploaded by the user. Furthermore, the fingerprint that is decrypted is matched with the one on the bank’s database. Additionally, the other data decrypted is matched with all the details stored in the bank’s database. Also extracted digital signature is decrypted and matched with the newly generated Message Digest from cheque informations.

If all the data is found to match, the cheque is legit and the amount can transferred to the payee account. If it does not then the cheque has been tampered and the transfer thus fails. Overall, this idea promotes significant improvements over the existing concepts in terms of both security features and data hiding solutions related to the digital verification of cheques.
