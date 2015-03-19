package zlicense.de.schlichtherle.xml;

public class PersistenceServiceException
  extends Exception
{
  public PersistenceServiceException(Throwable paramThrowable)
  {
    super(paramThrowable.getLocalizedMessage(), paramThrowable);
  }
}
