package zlicense.create;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.prefs.Preferences;

import javax.security.auth.x500.X500Principal;






import zlicense.de.schlichtherle.license.CipherParam;
import zlicense.de.schlichtherle.license.DefaultCipherParam;
import zlicense.de.schlichtherle.license.DefaultKeyStoreParam;
import zlicense.de.schlichtherle.license.DefaultLicenseParam;
import zlicense.de.schlichtherle.license.KeyStoreParam;
import zlicense.de.schlichtherle.license.LicenseContent;
import zlicense.de.schlichtherle.license.LicenseManager;
import zlicense.de.schlichtherle.license.LicenseParam;
import zlicense.util.LicenseCheckModel;

/**
 * CreateLicense
 * @author melina
 */
public class CreateLicense {
	//common param
	private static String PRIVATEALIAS = "";
	private static String KEYPWD = "";
	private static String STOREPWD = "";
	private static String SUBJECT = "";
	private static String licPath = "";
	private static String priPath = "";
	//license content
	private static String issuedTime = "";
	private static String notBefore = "";
	private static String notAfter = "";
	private static String ipAddress = "";
	private static String macAddress = "";
	private static String consumerType = "";
	private static int consumerAmount = 0;
	private static String info = "";
	
	
	private final static X500Principal DEFAULTHOLDERANDISSUER = new X500Principal(
			"CN=Duke、OU=JavaSoft、O=Sun Microsystems、C=US");
	
	public void setParam(String propertiesPath) {
		//
		Properties prop = new Properties();
		//InputStream in = getClass().getResourceAsStream(propertiesPath);
		
		try {
			InputStream in = new FileInputStream(propertiesPath);
			prop.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PRIVATEALIAS = prop.getProperty("PRIVATEALIAS");
		KEYPWD = prop.getProperty("KEYPWD");
		STOREPWD = prop.getProperty("STOREPWD");
		SUBJECT = prop.getProperty("SUBJECT");
		KEYPWD = prop.getProperty("KEYPWD");
		licPath = prop.getProperty("licPath");
		priPath = prop.getProperty("priPath");
		//license content
		issuedTime = prop.getProperty("issuedTime");
		notBefore = prop.getProperty("notBefore");
		notAfter = prop.getProperty("notAfter");
		
		ipAddress = prop.getProperty("ipAddress");
		macAddress = prop.getProperty("macAddress");
		
		consumerType = prop.getProperty("consumerType");
		consumerAmount = Integer.valueOf(prop.getProperty("consumerAmount"));
		info = prop.getProperty("info");
		
	}

	public boolean create() {		
		try {
			
			LicenseManager licenseManager = LicenseManagerHolder
					.getLicenseManager(initLicenseParams0());
			licenseManager.store((createLicenseContent()), new File(licPath));	
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("create license file failure");
			return false;
		}
		System.out.println("create license file successfully");
		return true;
	}

	
	private static LicenseParam initLicenseParams0() {
		Preferences preference = Preferences
				.userNodeForPackage(CreateLicense.class);
		
		CipherParam cipherParam = new DefaultCipherParam(STOREPWD);
		
		KeyStoreParam privateStoreParam = new DefaultKeyStoreParam(
				CreateLicense.class, priPath, PRIVATEALIAS, STOREPWD, KEYPWD);
		LicenseParam licenseParams = new DefaultLicenseParam(SUBJECT,
				preference, privateStoreParam, cipherParam);
		return licenseParams;
	}

	
		public final static LicenseContent createLicenseContent() {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			
			LicenseCheckModel licenseCheckModel = new LicenseCheckModel();
			licenseCheckModel.setIpAddress(ipAddress);
			licenseCheckModel.setIpMacAddress(macAddress);
			
			LicenseContent content = null;
			content = new LicenseContent();
			content.setSubject(SUBJECT);
			content.setHolder(DEFAULTHOLDERANDISSUER);
			content.setIssuer(DEFAULTHOLDERANDISSUER);
			try {
				content.setIssued(format.parse(issuedTime));
				content.setNotBefore(format.parse(notBefore));
				content.setNotAfter(format.parse(notAfter));
				content.setExtra(licenseCheckModel);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			content.setConsumerType(consumerType);
			content.setConsumerAmount(consumerAmount);
			content.setInfo(info);
			
			
			return content;
		}
}