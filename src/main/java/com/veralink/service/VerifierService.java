package com.veralink.service;

import com.upokecenter.cbor.CBORObject;

import COSE.CoseException;
import COSE.OneKey;
import COSE.Sign1Message;

public class VerifierService {
	
	private OneKey key;
	
	public VerifierService(OneKey verifierKey) {
		this.key = verifierKey;
	}

	private boolean validateMessage(byte[] coseSigned) throws CoseException {
        Sign1Message messageDecoded = (Sign1Message) Sign1Message.DecodeFromBytes(coseSigned);
        return messageDecoded.validate(key);
	}
	
	public CBORObject getDecodedCBOR(byte[] coseSigned) throws CoseException {
		CBORObject payloadDecoded = null;
		if(validateMessage(coseSigned)) {
	        Sign1Message messageDecoded = (Sign1Message) Sign1Message.DecodeFromBytes(coseSigned);
	        payloadDecoded = CBORObject.DecodeFromBytes(messageDecoded.GetContent());
	        return payloadDecoded;
		}
		return payloadDecoded;
	}
}
