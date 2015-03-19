package zlicense.verify;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.prefs.Preferences;

import zlicense.de.schlichtherle.license.CipherParam;
import zlicense.de.schlichtherle.license.DefaultCipherParam;
import zlicense.de.schlichtherle.license.DefaultKeyStoreParam;
import zlicense.de.schlichtherle.license.DefaultLicenseParam;
import zlicense.de.schlichtherle.license.KeyStoreParam;
import zlicense.de.schlichtherle.license.LicenseManager;
import zlicense.de.schlichtherle.license.LicenseParam;

/**
 * VerifyLicense
 * @author melina
 */
public class VerifyLicense {
	//common param
	private static String PUBLICALIAS = "";
	private static String STOREPWD = "";
	private static String SUBJECT = "";
	private static String licPath = "";
	private static String pubPath = "";
	

	
	public void setParam(String propertiesPath) throws IOException {
		// 
		Properties prop = new Properties();
		System.out.println("propertiesPath="+propertiesPath);
		//InputStream in = getClass().getResourceAsStream(propertiesPath);		
		InputStream in = new FileInputStream(propertiesPath);		
		prop.load(in);		
		PUBLICALIAS = prop.getProperty("PUBLICALIAS");
		STOREPWD = prop.getProperty("STOREPWD");
		SUBJECT = prop.getProperty("SUBJECT");
		licPath = prop.getProperty("licPath");
		pubPath = prop.getProperty("pubPath");
	}

	public boolean verify() throws Exception  {		

		LicenseManager licenseManager = LicenseManagerHolder
				.getLicenseManager(initLicenseParams());
		// install license file
		try {
			licenseManager.install(new File(licPath));
			System.out.println("License file instal successfully!");				
		} catch (Exception e) {
			//e.printStackTrace();			
			String moreInfo ="License file instal failure";
			System.out.println(moreInfo);
			throw e;
		}
		// verify license file
		try {
			licenseManager.verify();			
			System.out.println("License file verify successfully!");
		} catch (Exception e) {
			//e.printStackTrace();			
			String moreInfo ="License file verify failure";			
			System.out.println(moreInfo); 
			throw e;
		}
		return true;
	}

	//
	private static LicenseParam initLicenseParams() {
		Preferences preference = Preferences
				.userNodeForPackage(VerifyLicense.class);
		CipherParam cipherParam = new DefaultCipherParam(STOREPWD);

		KeyStoreParam privateStoreParam = new DefaultKeyStoreParam(
				VerifyLicense.class, pubPath, PUBLICALIAS, STOREPWD, null);
		LicenseParam licenseParams = new DefaultLicenseParam(SUBJECT,
				preference, privateStoreParam, cipherParam);
		return licenseParams;
	}
}