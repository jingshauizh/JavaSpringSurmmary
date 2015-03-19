package zlicense.de.schlichtherle.license;

public class DefaultKeyStoreParam
  extends AbstractKeyStoreParam
{
  private final String alias;
  private final String storePwd;
  private final String keyPwd;
  
  public DefaultKeyStoreParam(Class paramClass, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    super(paramClass, paramString1);
    this.alias = paramString2;
    this.storePwd = paramString3;
    this.keyPwd = paramString4;
  }
  
  public String getAlias()
  {
    return this.alias;
  }
  
  public String getStorePwd()
  {
    return this.storePwd;
  }
  
  public String getKeyPwd()
  {
    return this.keyPwd;
  }
}
