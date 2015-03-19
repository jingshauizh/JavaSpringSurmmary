package zlicense.de.schlichtherle.license;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import zlicense.de.schlichtherle.util.ObfuscatedString;
import zlicense.de.schlichtherle.xml.GenericCertificate;

public class LicenseNotary {
	private static final int BUFSIZE = 5120;
	static final String PARAM = new ObfuscatedString(new long[] {
			668274362144012114L, -2115765889337599212L }).toString();
	private static final String ALIAS = new ObfuscatedString(new long[] {
			1112708769776922148L, 6703392504509681290L }).toString();
	private static final String EXC_NO_KEY_PWD = new ObfuscatedString(
			new long[] { -7210613020960449599L, 222075784786550139L,
					9025728610804768010L }).toString();
	private static final String EXC_NO_KEY_ENTRY = new ObfuscatedString(
			new long[] { -1386002024146642540L, 4133952825992554401L,
					-8020387964636761861L }).toString();
	private static final String EXC_PRIVATE_KEY_OR_PWD_IS_NOT_ALLOWED = new ObfuscatedString(
			new long[] { -2960555953270849419L, 3827258740935670554L,
					-3005417608224527600L, 1939660993088349256L,
					4750831951568910874L }).toString();
	private static final String EXC_NO_CERTIFICATE_ENTRY = new ObfuscatedString(
			new long[] { -3872127676557769698L, -2469202953083814859L,
					6713970776812571709L, -482260351456063412L }).toString();
	private static final String SHA1_WITH_DSA = new ObfuscatedString(
			new long[] { -1509550478491572167L, 1688274905166048601L,
					-4620167493569680976L }).toString();
	private static final String JKS = new ObfuscatedString(new long[] {
			-6234396975553918200L, 2370155821952859770L }).toString();
	private KeyStoreParam param;
	private KeyStore keyStore;
	private PrivateKey privateKey;
	private PublicKey publicKey;

	protected LicenseNotary() {
	}

	public LicenseNotary(KeyStoreParam paramKeyStoreParam)
			throws NullPointerException, IllegalPasswordException {
		setKeyStoreParam(paramKeyStoreParam);
	}

	public KeyStoreParam getKeyStoreParam() {
		return this.param;
	}

	public void setKeyStoreParam(KeyStoreParam paramKeyStoreParam)
			throws NullPointerException, IllegalPasswordException {
		if (paramKeyStoreParam == null) {
			throw new NullPointerException(PARAM);
		}
		if (paramKeyStoreParam.getAlias() == null) {
			throw new NullPointerException(ALIAS);
		}
		Policy localPolicy = Policy.getCurrent();
		String str1 = paramKeyStoreParam.getStorePwd();
		localPolicy.checkPwd(str1);
		String str2 = paramKeyStoreParam.getKeyPwd();
		if (str2 != null) {
			localPolicy.checkPwd(str2);
		}
		this.param = paramKeyStoreParam;
		this.keyStore = null;
		this.privateKey = null;
		this.publicKey = null;
	}

	public GenericCertificate sign(Object paramObject) throws Exception {
		GenericCertificate localGenericCertificate = new GenericCertificate();
		sign(localGenericCertificate, paramObject);
		return localGenericCertificate;
	}

	/**
	 * @deprecated
	 */
	public void sign(GenericCertificate paramGenericCertificate,
			Object paramObject) throws Exception {
		paramGenericCertificate.sign(paramObject, getPrivateKey(),
				getSignatureEngine());
	}

	public void verify(GenericCertificate paramGenericCertificate)
			throws Exception {
		paramGenericCertificate.verify(getPublicKey(), getSignatureEngine());
	}

	/**
	 * @deprecated
	 */
	protected PrivateKey getPrivateKey() throws LicenseNotaryException,
			IOException, CertificateException, NoSuchAlgorithmException,
			UnrecoverableKeyException {
		if (this.privateKey == null) {
			KeyStoreParam localKeyStoreParam = getKeyStoreParam();
			String str1 = localKeyStoreParam.getKeyPwd();
			String str2 = localKeyStoreParam.getAlias();
			if (str1 == null) {
				throw new LicenseNotaryException(EXC_NO_KEY_PWD, str2);
			}
			KeyStore localKeyStore = getKeyStore();
			try {
				this.privateKey = ((PrivateKey) localKeyStore.getKey(str2,
						str1.toCharArray()));
			} catch (KeyStoreException localKeyStoreException) {
				throw new AssertionError(localKeyStoreException);
			}
			if (this.privateKey == null) {
				throw new LicenseNotaryException(EXC_NO_KEY_ENTRY, str2);
			}
		}
		return this.privateKey;
	}

	/**
	 * @deprecated
	 */
	protected PublicKey getPublicKey() throws LicenseNotaryException,
			IOException, CertificateException, NoSuchAlgorithmException {
		if (this.publicKey == null) {
			String str = getKeyStoreParam().getAlias();
			KeyStore localKeyStore = getKeyStore();
			try {
				if ((getKeyStoreParam().getKeyPwd() != null) != localKeyStore
						.isKeyEntry(str)) {
					throw new LicenseNotaryException(
							EXC_PRIVATE_KEY_OR_PWD_IS_NOT_ALLOWED, str);
				}
				Certificate localCertificate = localKeyStore
						.getCertificate(str);
				if (localCertificate == null) {
					throw new LicenseNotaryException(EXC_NO_CERTIFICATE_ENTRY,
							str);
				}
				this.publicKey = localCertificate.getPublicKey();
			} catch (KeyStoreException localKeyStoreException) {
				throw new AssertionError(localKeyStoreException);
			}
		}
		return this.publicKey;
	}

	/**
	 * @deprecated
	 */
	protected Signature getSignatureEngine() {
		try {
			return Signature.getInstance(SHA1_WITH_DSA);
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			throw new AssertionError(localNoSuchAlgorithmException);
		}
	}

	/**
	 * @deprecated
	 */
	protected KeyStore getKeyStore() throws IOException, CertificateException,
			NoSuchAlgorithmException {
		if (this.keyStore != null) {
			return this.keyStore;
		}
		BufferedInputStream localBufferedInputStream = null;
		try {
			this.keyStore = KeyStore.getInstance(JKS);
			localBufferedInputStream = new BufferedInputStream(
					this.param.getStream(), 5120);
			this.keyStore.load(localBufferedInputStream, getKeyStoreParam()
					.getStorePwd().toCharArray());
			return this.keyStore;
		} catch (KeyStoreException localKeyStoreException) {
			throw new AssertionError(localKeyStoreException);
		} finally {
			try {
				localBufferedInputStream.close();
			} catch (Exception localException2) {
			}
		}
	}
}
