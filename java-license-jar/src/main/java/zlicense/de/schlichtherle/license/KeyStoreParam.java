package zlicense.de.schlichtherle.license;

import java.io.IOException;
import java.io.InputStream;

public abstract interface KeyStoreParam
{
  public abstract InputStream getStream()
    throws IOException;
  
  public abstract String getAlias();
  
  public abstract String getStorePwd();
  
  public abstract String getKeyPwd();
}
