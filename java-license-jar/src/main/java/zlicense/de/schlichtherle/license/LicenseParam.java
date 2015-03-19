package zlicense.de.schlichtherle.license;

import java.util.prefs.Preferences;

public abstract interface LicenseParam
{
  public abstract String getSubject();
  
  public abstract Preferences getPreferences();
  
  public abstract KeyStoreParam getKeyStoreParam();
  
  public abstract CipherParam getCipherParam();
}
