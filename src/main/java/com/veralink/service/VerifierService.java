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

	public static boolean validateCoseBytes(byte[] coseSigned, OneKey key) throws CoseException {
        Sign1Message messageDecoded = (Sign1Message) Sign1Message.DecodeFromBytes(coseSigned);
        return messageDecoded.validate(key);
	}
	
	public static CBORObject getDecodedCBOR(byte[] coseSigned, OneKey key) throws CoseException {
		CBORObject payloadDecoded = null;
		Sign1Message messageDecoded = (Sign1Message) Sign1Message.DecodeFromBytes(coseSigned);

		if(validateCoseBytes(coseSigned, key)) {
	        payloadDecoded = CBORObject.DecodeFromBytes(messageDecoded.GetContent());
		}

		return payloadDecoded;
	}
}
