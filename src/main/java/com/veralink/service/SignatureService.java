package com.veralink.service;

import com.upokecenter.cbor.CBORObject;

import COSE.AlgorithmID;
import COSE.Attribute;
import COSE.CoseException;
import COSE.HeaderKeys;
import COSE.OneKey;
import COSE.Sign1Message;

public class SignatureService {
	
    private static OneKey key;
    
    public SignatureService() {
    	setOneKey();
    }

	public static byte[] signCBORMessage(String inputData, OneKey key) throws CoseException {
		// key & payload setup
		CBORObject payload = CBORObject.FromObject(inputData);
        CBORObject kid = CBORObject.FromObject("kid".getBytes());
        Sign1Message sign1Message = new Sign1Message();

        // message setup
        sign1Message.SetContent(payload.EncodeToBytes());
        sign1Message.addAttribute(HeaderKeys.Algorithm, AlgorithmID.ECDSA_256.AsCBOR(), Attribute.PROTECTED);
        sign1Message.addAttribute(HeaderKeys.KID, kid, Attribute.PROTECTED);
        sign1Message.sign(key);

        return sign1Message.EncodeToBytes();
	}

	public static byte[] signCBORMessage(String inputData) throws CoseException {
		return signCBORMessage(inputData, key);
	}

	public void setOneKey() {
        try {
        	key = OneKey.generateKey(AlgorithmID.ECDSA_256);
		} catch (CoseException e) {
			e.printStackTrace();
		}
	}
}
