package zlicense.de.schlichtherle.license;

import java.rmi.Remote;

public abstract interface LicenseVerifier
  extends Remote
{
  public abstract LicenseContent verify(byte[] paramArrayOfByte)
    throws Exception;
}
