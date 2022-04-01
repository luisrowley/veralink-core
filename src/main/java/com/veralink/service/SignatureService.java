package com.veralink.service;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Arrays;

import com.upokecenter.cbor.CBORObject;

import COSE.AlgorithmID;
import COSE.Attribute;
import COSE.CoseException;
import COSE.HeaderKeys;
import COSE.KeyKeys;
import COSE.OneKey;
import COSE.Sign1Message;

public class SignatureService {

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
	
	public static boolean validateCoseBytes(byte[] coseSigned, OneKey key) throws CoseException {
        Sign1Message messageDecoded = (Sign1Message) Sign1Message.DecodeFromBytes(coseSigned);
        return messageDecoded.validate(key);
	}
	
	public static OneKey generateOneKeyForSigning() throws CoseException {
		return OneKey.generateKey(AlgorithmID.ECDSA_256);
	}

	public static OneKey generateOneKeyPair(ECPublicKey ecPublicKey, ECPrivateKey ecPrivateKey) throws CoseException, NoSuchAlgorithmException {
        CBORObject map = CBORObject.NewMap();
        OneKey oneKey;

        byte[] rgbD = ArrayFromBigNum(((ECPrivateKey) ecPrivateKey).getS(), 256);
    
        map.set(KeyKeys.KeyType.AsCBOR(), KeyKeys.KeyType_EC2);
        map.set(KeyKeys.EC2_Curve.AsCBOR(), getEcCurve(ecPublicKey));
        map.set(KeyKeys.EC2_X.AsCBOR(), stripLeadingZero(ecPublicKey.getW().getAffineX()));
        map.set(KeyKeys.EC2_Y.AsCBOR(), stripLeadingZero(ecPublicKey.getW().getAffineY()));
        map.set(KeyKeys.EC2_D.AsCBOR(), CBORObject.FromObject(rgbD));
        oneKey = new OneKey(map);

        return oneKey;
	}

	public static KeyPair generateECKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("EC");
        keyPairGen.initialize(256);
        KeyPair pair = keyPairGen.generateKeyPair();

        //ECPublicKey ecPublicKey = (ECPublicKey) pair.getPublic();
        return pair;
	}

    private static CBORObject getEcCurve(ECPublicKey publicKey) {
        CBORObject keyKeys;
        switch (publicKey.getParams().getOrder().bitLength()) {
            case 384:
                keyKeys = KeyKeys.EC2_P384;
                break;
            case 256:
                keyKeys = KeyKeys.EC2_P256;
                break;
            default:
                throw new IllegalArgumentException("unsupported EC curveSize");
        }
        return keyKeys;
    }
    
    private static CBORObject stripLeadingZero(BigInteger input) {
        byte[] bytes = input.toByteArray();
        byte[] stripped;

        if (bytes.length % 8 != 0 && bytes[0] == 0x00) {
            stripped = Arrays.copyOfRange(bytes, 1, bytes.length);
        } else {
            stripped = bytes;
        }
        return CBORObject.FromObject(stripped);
    }
    
    static private byte[] ArrayFromBigNum(BigInteger n, int curveSize) {
        byte[] rgb = new byte[(curveSize+7)/8];
        byte[] rgb2 = n.toByteArray();
        if (rgb.length == rgb2.length) return rgb2;
        if (rgb2.length > rgb.length) {
            System.arraycopy(rgb2, rgb2.length-rgb.length, rgb, 0, rgb.length);
        }
        else {
            System.arraycopy(rgb2, 0, rgb, rgb.length-rgb2.length, rgb2.length);
        }
        return rgb;
    }
}
