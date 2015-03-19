package zlicense.de.schlichtherle.license;

import zlicense.de.schlichtherle.util.ObfuscatedString;

public class IllegalPasswordException
  extends IllegalArgumentException
{
  public String getLocalizedMessage()
  {
    return Resources.getString(new ObfuscatedString(new long[] { -6087108248892165543L, 4668112285741627657L, -1028382439244694792L, -1679939343705678708L }).toString());
  }
}
