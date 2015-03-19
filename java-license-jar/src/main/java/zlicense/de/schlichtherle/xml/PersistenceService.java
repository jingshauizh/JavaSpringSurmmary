package zlicense.de.schlichtherle.xml;

import java.beans.Encoder;
import java.beans.ExceptionListener;
import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PersistenceService
  implements XMLConstants
{
  private static final HashMap allPDs = new HashMap();
  public static int BUFSIZE = 10240;
  
  private static final ExceptionListener createExceptionListener()
  {
    return new ExceptionListener()
    {
      public void exceptionThrown(Exception paramAnonymousException)
      {
        throw ((paramAnonymousException instanceof UndeclaredThrowableException) ? (UndeclaredThrowableException)paramAnonymousException : new UndeclaredThrowableException(paramAnonymousException));
      }
    };
  }
  
  public static final synchronized void setPersistenceDelegate(Class paramClass, PersistenceDelegate paramPersistenceDelegate)
  {
    allPDs.put(paramClass, paramPersistenceDelegate);
  }
  
  protected static synchronized void installPersistenceDelegates(Encoder paramEncoder)
  {
    Iterator localIterator = allPDs.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      paramEncoder.setPersistenceDelegate((Class)localEntry.getKey(), (PersistenceDelegate)localEntry.getValue());
    }
  }
  
  public static void store(Object paramObject, OutputStream paramOutputStream)
    throws NullPointerException, PersistenceServiceException
  {
    if (paramOutputStream == null) {
      throw new NullPointerException();
    }
    try
    {
      BufferedOutputStream localBufferedOutputStream = null;
      XMLEncoder localXMLEncoder = null;
      try
      {
        localBufferedOutputStream = new BufferedOutputStream(paramOutputStream, BUFSIZE);
        localXMLEncoder = new XMLEncoder(localBufferedOutputStream);
        installPersistenceDelegates(localXMLEncoder);
        localXMLEncoder.setExceptionListener(createExceptionListener());
        if (paramObject != null) {
          synchronized (paramObject)
          {
            localXMLEncoder.writeObject(paramObject);
          }
        }
        localXMLEncoder.writeObject(paramObject);
      }
      finally
      {
        if (localXMLEncoder != null) {
          try
          {
            localXMLEncoder.close();
          }
          catch (Throwable localThrowable2)
          {
            localBufferedOutputStream.close();
            throw localThrowable2;
          }
        } else if (localBufferedOutputStream != null) {
          localBufferedOutputStream.close();
        } else {
          paramOutputStream.close();
        }
      }
    }
    catch (UndeclaredThrowableException localUndeclaredThrowableException)
    {
      throw new PersistenceServiceException(localUndeclaredThrowableException.getCause());
    }
    catch (Throwable localThrowable1)
    {
      throw new PersistenceServiceException(localThrowable1);
    }
  }
  
  public static void store(Object paramObject, File paramFile)
    throws NullPointerException, PersistenceServiceException
  {
    if (paramFile == null) {
      throw new NullPointerException();
    }
    File localFile = null;
    boolean bool = false;
    try
    {
      localFile = getRenamedFile(paramFile);
      bool = paramFile.renameTo(localFile);
      store(paramObject, new FileOutputStream(paramFile));
      if (bool) {
        localFile.delete();
      }
    }
    catch (Throwable localThrowable1)
    {
    	Throwable localObject = null;
      if (bool)
      {
        try
        {
          paramFile.delete();
        }
        catch (Throwable localThrowable2)
        {
          localObject = localThrowable2;
        }
        try
        {
          localFile.renameTo(paramFile);
        }
        catch (Throwable localThrowable3)
        {
          localObject = localThrowable3;
        }
      }
      throw ((localObject instanceof PersistenceServiceException) ? (PersistenceServiceException)localObject : new PersistenceServiceException(localObject));
    }
  }
  
  private static File getRenamedFile(File paramFile)
  {
    String str = paramFile.getPath();
    File localFile;
    do
    {
      str = str + '~';
      localFile = new File(str);
    } while (localFile.exists());
    return localFile;
  }
  
  public static byte[] store2ByteArray(Object paramObject)
    throws PersistenceServiceException
  {
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      store(paramObject, localByteArrayOutputStream);
      return localByteArrayOutputStream.toByteArray();
    }
    catch (PersistenceServiceException localPersistenceServiceException)
    {
      throw localPersistenceServiceException;
    }
    catch (Throwable localThrowable)
    {
      throw new PersistenceServiceException(localThrowable);
    }
  }
  
  public static String store2String(Object paramObject)
    throws PersistenceServiceException
  {
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      store(paramObject, localByteArrayOutputStream);
      return localByteArrayOutputStream.toString("UTF-8");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new AssertionError(localUnsupportedEncodingException);
    }
    catch (PersistenceServiceException localPersistenceServiceException)
    {
      throw localPersistenceServiceException;
    }
    catch (Throwable localThrowable)
    {
      throw new PersistenceServiceException(localThrowable);
    }
  }
  
  public static Object load(InputStream paramInputStream)
    throws NullPointerException, PersistenceServiceException
  {
    if (paramInputStream == null) {
      throw new NullPointerException();
    }
    XMLDecoder localXMLDecoder = null;
    try
    {
      localXMLDecoder = new XMLDecoder(new BufferedInputStream(paramInputStream, BUFSIZE), null, createExceptionListener());
      Object localObject1 = localXMLDecoder.readObject();
      return localObject1;
    }
    catch (UndeclaredThrowableException localUndeclaredThrowableException)
    {
      throw new PersistenceServiceException(localUndeclaredThrowableException.getCause());
    }
    catch (Throwable localThrowable1)
    {
      throw new PersistenceServiceException(localThrowable1);
    }
    finally
    {
      if (localXMLDecoder != null) {
        try
        {
          localXMLDecoder.close();
        }
        catch (Throwable localThrowable2)
        {
          throw new PersistenceServiceException(localThrowable2);
        }
      }
    }
  }
  
  public static Object load(File paramFile)
    throws NullPointerException, PersistenceServiceException
  {
    if (paramFile == null) {
      throw new NullPointerException();
    }
    try
    {
      return load(new FileInputStream(paramFile));
    }
    catch (PersistenceServiceException localPersistenceServiceException)
    {
      throw localPersistenceServiceException;
    }
    catch (Throwable localThrowable)
    {
      throw new PersistenceServiceException(localThrowable);
    }
  }
  
  public static Object load(byte[] paramArrayOfByte)
    throws NullPointerException, PersistenceServiceException
  {
    if (paramArrayOfByte == null) {
      throw new NullPointerException();
    }
    try
    {
      return load(new ByteArrayInputStream(paramArrayOfByte));
    }
    catch (PersistenceServiceException localPersistenceServiceException)
    {
      throw localPersistenceServiceException;
    }
    catch (Throwable localThrowable)
    {
      throw new PersistenceServiceException(localThrowable);
    }
  }
  
  public static Object load(String paramString)
    throws NullPointerException, PersistenceServiceException
  {
    if (paramString == null) {
      throw new NullPointerException();
    }
    try
    {
      return load(paramString.getBytes("UTF-8"));
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new AssertionError(localUnsupportedEncodingException);
    }
    catch (PersistenceServiceException localPersistenceServiceException)
    {
      throw localPersistenceServiceException;
    }
    catch (Throwable localThrowable)
    {
      throw new PersistenceServiceException(localThrowable);
    }
  }
}
