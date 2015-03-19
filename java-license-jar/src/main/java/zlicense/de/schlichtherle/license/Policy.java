package zlicense.de.schlichtherle.license;

public class Policy
{
  private static Policy current;
  
  public static void setCurrent(Policy paramPolicy)
  {
    current = paramPolicy;
  }
  
  public static Policy getCurrent()
  {
    if (current == null) {
      current = new Policy();
    }
    return current;
  }
  
  public void checkPwd(String paramString)
    throws IllegalArgumentException
  {
    int i = paramString.length();
    if (paramString == null) {
      throw new IllegalPasswordException();
    }
    if (i < 6) {
      throw new IllegalPasswordException();
    }
    int j = 0;
    int k = 0;
    for (int m = 0; m < i; m++)
    {
      char c = paramString.charAt(m);
      if (Character.isLetter(c)) {
        j = 1;
      } else if (Character.isDigit(c)) {
        k = 1;
      }
    }
    if ((j == 0) || (k == 0)) {
      throw new IllegalPasswordException();
    }
  }
}
