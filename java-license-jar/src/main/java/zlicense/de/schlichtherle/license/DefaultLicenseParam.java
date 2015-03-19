package zlicense.de.schlichtherle.license;

import java.util.prefs.Preferences;

public class DefaultLicenseParam
  implements LicenseParam
{
  private final String subject;
  private final Preferences preferences;
  private final KeyStoreParam keyStoreParam;
  private final CipherParam cipherParam;
  
  public DefaultLicenseParam(String paramString, Preferences paramPreferences, KeyStoreParam paramKeyStoreParam, CipherParam paramCipherParam)
  {
    this.subject = paramString;
    this.preferences = paramPreferences;
    this.keyStoreParam = paramKeyStoreParam;
    this.cipherParam = paramCipherParam;
  }
  
  public String getSubject()
  {
    return this.subject;
  }
  
  public Preferences getPreferences()
  {
    return this.preferences;
  }
  
  public KeyStoreParam getKeyStoreParam()
  {
    return this.keyStoreParam;
  }
  
  public CipherParam getCipherParam()
  {
    return this.cipherParam;
  }
}
