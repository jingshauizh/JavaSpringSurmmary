package zlicense.de.schlichtherle.license;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;

import javax.security.auth.x500.X500Principal;
import javax.swing.filechooser.FileFilter;









import zlicense.de.schlichtherle.util.ObfuscatedString;
import zlicense.de.schlichtherle.xml.GenericCertificate;
import zlicense.util.LicenseCheckModel;
import zlicense.util.ListNets;



public class LicenseManager implements LicenseCreator, LicenseVerifier {
	private static final long TIMEOUT = 1800000L;
	private static final String PREFERENCES_KEY;
	public static final String LICENSE_SUFFIX;
	private static final String PARAM = LicenseNotary.PARAM;
	private static final String SUBJECT = new ObfuscatedString(new long[] {
			-6788193907359448604L, -2787711522493615434L }).toString();
	private static final String KEY_STORE_PARAM = new ObfuscatedString(
			new long[] { 4943981370588954830L, 8065447823433585419L,
					-2749528823549501332L }).toString();
	private static final String CIPHER_PARAM = new ObfuscatedString(new long[] {
			-3651048337721043740L, 1928803483347080380L, 1649789960289346230L })
			.toString();
	protected static final String CN = new ObfuscatedString(new long[] {
			7165044359350484836L, -6008675436704023088L }).toString();
	private static final String CN_USER = CN
			+ new ObfuscatedString(new long[] { -883182015789302099L,
					6587252612286394632L }).toString();
	private static final String USER = new ObfuscatedString(new long[] {
			-6950934198262740461L, -10280221617836935L }).toString();
	private static final String SYSTEM = new ObfuscatedString(new long[] {
			-1441033263392531498L, 6113162389128247115L }).toString();
	private static final String EXC_INVALID_SUBJECT = new ObfuscatedString(
			new long[] { -9211605111142713620L, 391714365510707393L,
					-7356761750428556372L, 6379560902598103028L }).toString();
	private static final String EXC_HOLDER_IS_NULL = new ObfuscatedString(
			new long[] { 7150026245468079143L, 6314884536402738366L,
					-1360923923476698800L }).toString();
	private static final String EXC_ISSUER_IS_NULL = new ObfuscatedString(
			new long[] { -3034693013076752554L, -1011266899694033610L,
					6775785917404597234L }).toString();
	private static final String EXC_ISSUED_IS_NULL = new ObfuscatedString(
			new long[] { -6084371209004858580L, 3028840747031697166L,
					-3524637886726219307L }).toString();
	private static final String EXC_LICENSE_IS_NOT_YET_VALID = new ObfuscatedString(
			new long[] { 5434633639502011825L, -3406117476263181371L,
					6903673940810780388L, -6816911225052310716L }).toString();
	private static final String EXC_LICENSE_HAS_EXPIRED = new ObfuscatedString(
			new long[] { 1000558500458715757L, -6998261911041258483L,
					-5490039629745846648L, 3561172928787106880L }).toString();
	private static final String EXC_CONSUMER_TYPE_IS_NULL = new ObfuscatedString(
			new long[] { -3274088377466921882L, -1704115158449736962L,
					-1134622897105293263L, 2875630655915253859L }).toString();
	private static final String EXC_CONSUMER_TYPE_IS_NOT_USER = new ObfuscatedString(
			new long[] { -3559580260061340089L, 8807812719464926891L,
					3255622466169980128L, 3208430498260873670L,
					8772089725159421213L }).toString();
	private static final String EXC_CONSUMER_AMOUNT_IS_NOT_ONE = new ObfuscatedString(
			new long[] { 6854702630454082314L, -1676630527348424687L,
					4853969635229547239L, -7087814313396201500L,
					7133601245775504376L }).toString();
	private static final String EXC_CONSUMER_AMOUNT_IS_NOT_POSITIVE = new ObfuscatedString(
			new long[] { -5670394608177286583L, -3674104453170648872L,
					4159301984262248157L, 7442355638167795990L,
					4780252201915657674L }).toString();
	private static final String FILE_FILTER_DESCRIPTION = new ObfuscatedString(
			new long[] { 3160933239845492228L, -2320904495012387647L,
					-5935185636215549881L, -3418607682842311949L }).toString();
	private static final String FILE_FILTER_SUFFIX = new ObfuscatedString(
			new long[] { -6576160320308571504L, 7010427383913371869L })
			.toString();
	private LicenseParam param;
	private LicenseNotary notary;
	private PrivacyGuard guard;
	private GenericCertificate certificate;
	private long certificateTime;
	private FileFilter fileFilter;
	private Preferences preferences;

	protected static final Date midnight() {
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.set(11, 0);
		localCalendar.set(12, 0);
		localCalendar.set(13, 0);
		localCalendar.set(14, 0);
		return localCalendar.getTime();
	}

	protected LicenseManager() {
	}

	public LicenseManager(LicenseParam paramLicenseParam)
			throws NullPointerException, IllegalPasswordException {
		setLicenseParam(paramLicenseParam);
	}

	public LicenseParam getLicenseParam() {
		return this.param;
	}

	public synchronized void setLicenseParam(LicenseParam paramLicenseParam)
			throws NullPointerException, IllegalPasswordException {
		if (paramLicenseParam == null) {
			throw new NullPointerException(PARAM);
		}
		if (paramLicenseParam.getSubject() == null) {
			throw new NullPointerException(SUBJECT);
		}
		if (paramLicenseParam.getKeyStoreParam() == null) {
			throw new NullPointerException(KEY_STORE_PARAM);
		}
		CipherParam localCipherParam = paramLicenseParam.getCipherParam();
		if (localCipherParam == null) {
			throw new NullPointerException(CIPHER_PARAM);
		}
		Policy.getCurrent().checkPwd(localCipherParam.getKeyPwd());
		this.param = paramLicenseParam;
		this.notary = null;
		this.certificate = null;
		this.certificateTime = 0L;
		this.fileFilter = null;
		this.preferences = null;
	}

	public final synchronized void store(LicenseContent paramLicenseContent,
			File paramFile) throws Exception {
		store(paramLicenseContent, getLicenseNotary(), paramFile);
	}

	/**
	 * @deprecated
	 */
	protected synchronized void store(LicenseContent paramLicenseContent,
			LicenseNotary paramLicenseNotary, File paramFile) throws Exception {
		storeLicenseKey(create(paramLicenseContent, paramLicenseNotary),
				paramFile);
	}

	public final synchronized byte[] create(LicenseContent paramLicenseContent)
			throws Exception {
		return create(paramLicenseContent, getLicenseNotary());
	}

	/**
	 * @deprecated
	 */
	protected synchronized byte[] create(LicenseContent paramLicenseContent,
			LicenseNotary paramLicenseNotary) throws Exception {
		initialize(paramLicenseContent);
		create_validate(paramLicenseContent);
		GenericCertificate localGenericCertificate = paramLicenseNotary
				.sign(paramLicenseContent);
		byte[] arrayOfByte = getPrivacyGuard()
				.cert2key(localGenericCertificate);
		return arrayOfByte;
	}

	public final synchronized LicenseContent install(File paramFile)
			throws Exception {
		return install(paramFile, getLicenseNotary());
	}

	/**
	 * @deprecated
	 */
	protected synchronized LicenseContent install(File paramFile,
			LicenseNotary paramLicenseNotary) throws Exception {
		return install(loadLicenseKey(paramFile), paramLicenseNotary);
	}

	/**
	 * @deprecated
	 */
	protected synchronized LicenseContent install(byte[] paramArrayOfByte,
			LicenseNotary paramLicenseNotary) throws Exception {
		GenericCertificate localGenericCertificate = getPrivacyGuard()
				.key2cert(paramArrayOfByte);
		paramLicenseNotary.verify(localGenericCertificate);
		LicenseContent localLicenseContent = (LicenseContent) localGenericCertificate
				.getContent();
		validate(localLicenseContent);
		setLicenseKey(paramArrayOfByte);
		setCertificate(localGenericCertificate);
		return localLicenseContent;
	}

	public final synchronized LicenseContent verify() throws Exception {
		return verify(getLicenseNotary());
	}

	/**
	 * @deprecated
	 */
	protected synchronized LicenseContent verify(
			LicenseNotary paramLicenseNotary) throws Exception {
		GenericCertificate localGenericCertificate = getCertificate();
		if (localGenericCertificate != null) {
			return (LicenseContent) localGenericCertificate.getContent();
		}
		byte[] arrayOfByte = getLicenseKey();
		if (arrayOfByte == null) {
			throw new NoLicenseInstalledException(getLicenseParam()
					.getSubject());
		}
		localGenericCertificate = getPrivacyGuard().key2cert(arrayOfByte);
		paramLicenseNotary.verify(localGenericCertificate);
		LicenseContent localLicenseContent = (LicenseContent) localGenericCertificate
				.getContent();
		validate(localLicenseContent);
		setCertificate(localGenericCertificate);
		return localLicenseContent;
	}

	public final synchronized LicenseContent verify(byte[] paramArrayOfByte)
			throws Exception {
		return verify(paramArrayOfByte, getLicenseNotary());
	}

	/**
	 * @deprecated
	 */
	protected synchronized LicenseContent verify(byte[] paramArrayOfByte,
			LicenseNotary paramLicenseNotary) throws Exception {
		GenericCertificate localGenericCertificate = getPrivacyGuard()
				.key2cert(paramArrayOfByte);
		paramLicenseNotary.verify(localGenericCertificate);
		LicenseContent localLicenseContent = (LicenseContent) localGenericCertificate
				.getContent();
		validate(localLicenseContent);
		return localLicenseContent;
	}

	public synchronized void uninstall() throws Exception {
		setLicenseKey(null);
		setCertificate(null);
	}

	protected synchronized void initialize(LicenseContent paramLicenseContent) {
		if (paramLicenseContent.getHolder() == null) {
			paramLicenseContent.setHolder(new X500Principal(CN_USER));
		}
		if (paramLicenseContent.getSubject() == null) {
			paramLicenseContent.setSubject(getLicenseParam().getSubject());
		}
		if (paramLicenseContent.getConsumerType() == null) {
			Preferences localPreferences = getLicenseParam().getPreferences();
			if (localPreferences != null) {
				if (localPreferences.isUserNode()) {
					paramLicenseContent.setConsumerType(USER);
				} else {
					paramLicenseContent.setConsumerType(SYSTEM);
				}
				paramLicenseContent.setConsumerAmount(1);
			}
		}
		if (paramLicenseContent.getIssuer() == null) {
			paramLicenseContent.setIssuer(new X500Principal(CN
					+ getLicenseParam().getSubject()));
		}
		if (paramLicenseContent.getIssued() == null) {
			paramLicenseContent.setIssued(new Date());
		}
		if (paramLicenseContent.getNotBefore() == null) {
			paramLicenseContent.setNotBefore(midnight());
		}
	}
	
	
	protected synchronized void create_validate(LicenseContent paramLicenseContent)
			throws LicenseContentException {
		LicenseParam localLicenseParam = getLicenseParam();
		if (!localLicenseParam.getSubject().equals(
				paramLicenseContent.getSubject())) {
			throw new LicenseContentException(EXC_INVALID_SUBJECT);
		}
		if (paramLicenseContent.getHolder() == null) {
			throw new LicenseContentException(EXC_HOLDER_IS_NULL);
		}
		if (paramLicenseContent.getIssuer() == null) {
			throw new LicenseContentException(EXC_ISSUER_IS_NULL);
		}
		if (paramLicenseContent.getIssued() == null) {
			throw new LicenseContentException(EXC_ISSUED_IS_NULL);
		}
		Date localDate1 = new Date();
		Date localDate2 = paramLicenseContent.getNotBefore();
		if ((localDate2 != null) && (localDate1.before(localDate2))) {
			throw new LicenseContentException(EXC_LICENSE_IS_NOT_YET_VALID);
		}
		Date localDate3 = paramLicenseContent.getNotAfter();
		if ((localDate3 != null) && (localDate1.after(localDate3))) {
			throw new LicenseContentException(EXC_LICENSE_HAS_EXPIRED);
		}	
		
		String str = paramLicenseContent.getConsumerType();
		if (str == null) {
			throw new LicenseContentException(EXC_CONSUMER_TYPE_IS_NULL);
		}
		Preferences localPreferences = localLicenseParam.getPreferences();
		if ((localPreferences != null) && (localPreferences.isUserNode())) {
			if (!USER.equalsIgnoreCase(str)) {
				throw new LicenseContentException(EXC_CONSUMER_TYPE_IS_NOT_USER);
			}
			if (paramLicenseContent.getConsumerAmount() != 1) {
				throw new LicenseContentException(
						EXC_CONSUMER_AMOUNT_IS_NOT_ONE);
			}
		} else if (paramLicenseContent.getConsumerAmount() <= 0) {
			throw new LicenseContentException(
					EXC_CONSUMER_AMOUNT_IS_NOT_POSITIVE);
		}
	}

	protected synchronized void validate(LicenseContent paramLicenseContent)
			throws LicenseContentException {
		LicenseParam localLicenseParam = getLicenseParam();
		if (!localLicenseParam.getSubject().equals(
				paramLicenseContent.getSubject())) {
			throw new LicenseContentException(EXC_INVALID_SUBJECT);
		}
		if (paramLicenseContent.getHolder() == null) {
			throw new LicenseContentException(EXC_HOLDER_IS_NULL);
		}
		if (paramLicenseContent.getIssuer() == null) {
			throw new LicenseContentException(EXC_ISSUER_IS_NULL);
		}
		if (paramLicenseContent.getIssued() == null) {
			throw new LicenseContentException(EXC_ISSUED_IS_NULL);
		}
		Date localDate1 = new Date();
		Date localDate2 = paramLicenseContent.getNotBefore();
		if ((localDate2 != null) && (localDate1.before(localDate2))) {
			throw new LicenseContentException(EXC_LICENSE_IS_NOT_YET_VALID);
		}
		Date localDate3 = paramLicenseContent.getNotAfter();
		if ((localDate3 != null) && (localDate1.after(localDate3))) {
			throw new LicenseContentException(EXC_LICENSE_HAS_EXPIRED);
		}
		
		LicenseCheckModel licenseCheckModel = (LicenseCheckModel)paramLicenseContent.getExtra();
		String macAddress = licenseCheckModel.getIpMacAddress();
		
		try {
			if (!ListNets.validateMacAddress(macAddress)) {
				throw new LicenseContentException(EXC_LICENSE_HAS_EXPIRED);
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			throw new LicenseContentException(EXC_LICENSE_HAS_EXPIRED);
		}
		
		
		String str = paramLicenseContent.getConsumerType();
		if (str == null) {
			throw new LicenseContentException(EXC_CONSUMER_TYPE_IS_NULL);
		}
		Preferences localPreferences = localLicenseParam.getPreferences();
		if ((localPreferences != null) && (localPreferences.isUserNode())) {
			if (!USER.equalsIgnoreCase(str)) {
				throw new LicenseContentException(EXC_CONSUMER_TYPE_IS_NOT_USER);
			}
			if (paramLicenseContent.getConsumerAmount() != 1) {
				throw new LicenseContentException(
						EXC_CONSUMER_AMOUNT_IS_NOT_ONE);
			}
		} else if (paramLicenseContent.getConsumerAmount() <= 0) {
			throw new LicenseContentException(
					EXC_CONSUMER_AMOUNT_IS_NOT_POSITIVE);
		}
	}

	/**
	 * @deprecated
	 */
	protected GenericCertificate getCertificate() {
		if ((this.certificate != null)
				&& (System.currentTimeMillis() < this.certificateTime + 1800000L)) {
			return this.certificate;
		}
		return null;
	}

	/**
	 * @deprecated
	 */
	protected synchronized void setCertificate(
			GenericCertificate paramGenericCertificate) {
		this.certificate = paramGenericCertificate;
		this.certificateTime = System.currentTimeMillis();
	}

	/**
	 * @deprecated
	 */
	protected byte[] getLicenseKey() {
		return getLicenseParam().getPreferences().getByteArray(PREFERENCES_KEY,
				null);
	}

	/**
	 * @deprecated
	 */
	protected synchronized void setLicenseKey(byte[] paramArrayOfByte) {
		Preferences localPreferences = getLicenseParam().getPreferences();
		if (paramArrayOfByte != null) {
			localPreferences.putByteArray(PREFERENCES_KEY, paramArrayOfByte);
		} else {
			localPreferences.remove(PREFERENCES_KEY);
		}
	}

	protected static void storeLicenseKey(byte[] paramArrayOfByte,
			File paramFile) throws IOException {
		FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
		try {
			localFileOutputStream.write(paramArrayOfByte);
			return;
		} finally {
			try {
				localFileOutputStream.close();
			} catch (IOException localIOException2) {
			}
		}
	}

	/*
	 * protected static byte[] loadLicenseKey(File paramFile) throws IOException
	 * { int i = Math.min((int)paramFile.length(), 1048576); FileInputStream
	 * localFileInputStream = new FileInputStream(paramFile); arrayOfByte = new
	 * byte[i]; try { localFileInputStream.read(arrayOfByte); return
	 * arrayOfByte; } finally { try { localFileInputStream.close(); } catch
	 * (IOException localIOException2) {} } }
	 */

	protected static byte[] loadLicenseKey(final File keyFile)
			throws IOException {
		// Allow max 1MB size files and let the verifier detect a partial read
		final int size = Math.min((int) keyFile.length(), 1024 * 1024);
		final byte[] b = new byte[size];
		final InputStream in = new FileInputStream(keyFile);
		try {
			// Let the verifier detect a partial read as an error
			in.read(b);
		} finally {
			in.close();
		}
		return b;
	}

	protected synchronized LicenseNotary getLicenseNotary() {
		if (this.notary == null) {
			this.notary = new LicenseNotary(getLicenseParam()
					.getKeyStoreParam());
		}
		return this.notary;
	}

	protected synchronized PrivacyGuard getPrivacyGuard() {
		if (this.guard == null) {
			this.guard = new PrivacyGuard(getLicenseParam().getCipherParam());
		}
		return this.guard;
	}

	/*
	 * public synchronized FileFilter getFileFilter() { if (this.fileFilter !=
	 * null) { return this.fileFilter; } final String str =
	 * Resources.getString(FILE_FILTER_DESCRIPTION,
	 * getLicenseParam().getSubject()); if (File.separatorChar == '\\') {
	 * this.fileFilter = new FileFilter() { private final String
	 * val$description;
	 * 
	 * public boolean accept(File paramAnonymousFile) { return
	 * (paramAnonymousFile.isDirectory()) ||
	 * (paramAnonymousFile.getPath().toLowerCase
	 * ().endsWith(LicenseManager.LICENSE_SUFFIX)); }
	 * 
	 * public String getDescription() { return str +
	 * LicenseManager.FILE_FILTER_SUFFIX; } }; } else { this.fileFilter = new
	 * FileFilter() { private final String val$description;
	 * 
	 * public boolean accept(File paramAnonymousFile) { return
	 * (paramAnonymousFile.isDirectory()) ||
	 * (paramAnonymousFile.getPath().endsWith(LicenseManager.LICENSE_SUFFIX)); }
	 * 
	 * public String getDescription() { return str +
	 * LicenseManager.FILE_FILTER_SUFFIX; } }; } return this.fileFilter; }
	 */

	public synchronized FileFilter getFileFilter() {
		if (fileFilter != null)
			return fileFilter;
		final String description = Resources.getString(FILE_FILTER_DESCRIPTION,
				getLicenseParam().getSubject());
		if (File.separatorChar == '\\') {
			fileFilter = new FileFilter() {
				public boolean accept(File f) {
					return f.isDirectory()
							|| f.getPath().toLowerCase()
									.endsWith(LICENSE_SUFFIX);
				}

				public String getDescription() {
					return description + FILE_FILTER_SUFFIX;
				}
			};
		} else {
			fileFilter = new FileFilter() {
				public boolean accept(File f) {
					return f.isDirectory()
							|| f.getPath().endsWith(LICENSE_SUFFIX);
				}

				public String getDescription() {
					return description + FILE_FILTER_SUFFIX;
				}
			};
		}
		return fileFilter;
	}

	static {
		PREFERENCES_KEY = new ObfuscatedString(new long[] {
				-2999492566024573771L, -1728025856628382701L }).toString();
		LICENSE_SUFFIX = new ObfuscatedString(new long[] {
				-7559156485370438418L, 5084921010819724770L }).toString();
		assert (LICENSE_SUFFIX.equals(LICENSE_SUFFIX.toLowerCase()));
	}
}
