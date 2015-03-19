package zlicense.de.schlichtherle.license;

public class LicenseContentException
  extends Exception
{
  public LicenseContentException(String paramString)
  {
    super(paramString);
  }
  
  public String getLocalizedMessage()
  {
    return Resources.getString(super.getMessage());
  }
}
