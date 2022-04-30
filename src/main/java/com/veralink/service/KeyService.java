package com.veralink.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import com.upokecenter.cbor.CBORObject;

import COSE.CoseException;
import COSE.KeyKeys;
import COSE.OneKey;
import io.github.cdimascio.dotenv.Dotenv;

public class KeyService {

	private static final int KEYSIZE = 256;
	private final Dotenv dotenv = Dotenv.configure().load();
	private final String keyStoreType = dotenv.get("KEYSTORE_TYPE");
	public final String keyStoreAlias = dotenv.get("KEYSTORE_ALIAS");
	public final String keyStorePath = dotenv.get("KEYSTORE_PATH");
	private final char[] keyStorePass = dotenv.get("KEYSTORE_PASS").toCharArray();

	public static KeyPair generateECKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("EC");
        keyPairGen.initialize(KEYSIZE);
        KeyPair pair = keyPairGen.generateKeyPair();
        return pair;
	}
	
	public static OneKey generateOneKeyPair(ECPublicKey ecPublicKey, ECPrivateKey ecPrivateKey) throws CoseException, NoSuchAlgorithmException {
        CBORObject map = CBORObject.NewMap();
        OneKey oneKey;

        byte[] rgbD = ArrayFromBigNum(((ECPrivateKey) ecPrivateKey).getS(), KEYSIZE);
    
        map.set(KeyKeys.KeyType.AsCBOR(), KeyKeys.KeyType_EC2);
        map.set(KeyKeys.EC2_Curve.AsCBOR(), getEcCurve(ecPublicKey));
        map.set(KeyKeys.EC2_X.AsCBOR(), stripLeadingZero(ecPublicKey.getW().getAffineX()));
        map.set(KeyKeys.EC2_Y.AsCBOR(), stripLeadingZero(ecPublicKey.getW().getAffineY()));
        map.set(KeyKeys.EC2_D.AsCBOR(), CBORObject.FromObject(rgbD));
        oneKey = new OneKey(map);

        return oneKey;
	}
	
	public Certificate getCertificateFromStore(String alias, String filePath) {

        char[] _keyStorePass = this.keyStorePass;
		Certificate cert = null;
		
		try {
			FileInputStream inputStream = new FileInputStream(filePath);
			KeyStore keyStore = KeyStore.getInstance(this.keyStoreType);
			keyStore.load(inputStream, _keyStorePass);
			cert = keyStore.getCertificate(alias);
		} catch (IOException
				| KeyStoreException
				| NoSuchAlgorithmException
				| CertificateException e) {
			e.printStackTrace();
		}
		return cert;
	}
	
	public Key getPrivateKeyFromStore(String filePath) {

        char[] _keyStorePass = this.keyStorePass;
		Key secretKey = null;
		
		try {
			FileInputStream inputStream = new FileInputStream(filePath);
			KeyStore keyStore = KeyStore.getInstance(this.keyStoreType);
			keyStore.load(inputStream, _keyStorePass);
			secretKey = keyStore.getKey(this.keyStoreAlias, _keyStorePass);
		} catch (IOException
				| KeyStoreException
				| NoSuchAlgorithmException
				| CertificateException 
				| UnrecoverableKeyException e) {
			e.printStackTrace();
		}
		return secretKey;
	}
	
	public static void createKeyStoreFile(String filePath) {
		try {
		File storeFile = new File(filePath);
		String filename =  storeFile.getName();
		if (storeFile.createNewFile()) {
	        System.out.println("File created: " + filename);
	      } else {
	        System.out.println("File already exists: " + filename);
	      }
		} catch (IOException e) {
		  e.printStackTrace();
		}
	}
	
	public boolean populateKeyStore(String filePath) {

        char[] _keyStorePass = this.keyStorePass;

		try {
			KeyStore keyStore = KeyStore.getInstance(this.keyStoreType);
			KeyPair keyPair = generateECKeyPair();
			ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();
			ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();

			Certificate cert = generateCertificate(ecPublicKey, ecPrivateKey);
			Certificate[] chain = { generateCertificate(ecPublicKey, ecPrivateKey), cert };

			FileOutputStream fos = new FileOutputStream(filePath);

			keyStore.load(null, _keyStorePass);
			// save alias to .env
			keyStore.setKeyEntry(this.keyStoreAlias, ecPrivateKey, _keyStorePass, chain);

			keyStore.store(fos, _keyStorePass);
			fos.close();
			
			return true;
			
		} catch (KeyStoreException
				| NoSuchAlgorithmException
				| IOException
				| InvalidKeyException
				| IllegalStateException
				| NoSuchProviderException
				| SignatureException
				| OperatorCreationException
				| CertificateException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static Certificate generateCertificate(ECPublicKey ecPublicKey, ECPrivateKey ecPrivateKey)
			throws InvalidKeyException, 
				   IllegalStateException, 
				   NoSuchProviderException, 
				   NoSuchAlgorithmException, 
				   SignatureException, 
				   OperatorCreationException,
				   CertificateException
	{  
		X500Name issuer = new X500Name("CN=veralink");
		X500Name subject = new X500Name("CN=veralink");
		BigInteger serialNumber = BigInteger.valueOf(Math.abs(new Random().nextLong()));
		Date issuedAt = new Date();
		Date expiresAt = new Date(new Date().getTime() + 86400000);
		SubjectPublicKeyInfo subPubKeyInfo = SubjectPublicKeyInfo.getInstance(ecPublicKey.getEncoded());
		
		final X509v3CertificateBuilder builder = new X509v3CertificateBuilder(issuer, serialNumber, issuedAt, expiresAt, subject, subPubKeyInfo);
		
		ContentSigner signer = new JcaContentSignerBuilder("SHA256WITHECDSA").setProvider(new BouncyCastleProvider()).build(ecPrivateKey);
		final X509CertificateHolder holder = builder.build(signer);

		Certificate cert = new JcaX509CertificateConverter().setProvider(new BouncyCastleProvider()).getCertificate(holder);
	    return cert;
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
    
    private static byte[] ArrayFromBigNum(BigInteger n, int curveSize) {
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
