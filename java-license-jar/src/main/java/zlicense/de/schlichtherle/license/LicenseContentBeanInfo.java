package zlicense.de.schlichtherle.license;

import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class LicenseContentBeanInfo
  extends SimpleBeanInfo
{
  private static BeanDescriptor beanDescriptor = null;
  private static final int PROPERTY_consumerAmount = 0;
  private static final int PROPERTY_consumerType = 1;
  private static final int PROPERTY_extra = 2;
  private static final int PROPERTY_holder = 3;
  private static final int PROPERTY_info = 4;
  private static final int PROPERTY_issued = 5;
  private static final int PROPERTY_issuer = 6;
  private static final int PROPERTY_notAfter = 7;
  private static final int PROPERTY_notBefore = 8;
  private static final int PROPERTY_subject = 9;
  private static EventSetDescriptor[] eventSets = null;
  private static MethodDescriptor[] methods = null;
  private static final int defaultPropertyIndex = -1;
  private static final int defaultEventIndex = -1;
  
  private static BeanDescriptor getBdescriptor()
  {
    return beanDescriptor;
  }
  
  private static PropertyDescriptor[] getPdescriptor()
  {
    PropertyDescriptor[] arrayOfPropertyDescriptor = new PropertyDescriptor[10];
    try
    {
      arrayOfPropertyDescriptor[0] = new PropertyDescriptor("consumerAmount", LicenseContent.class, "getConsumerAmount", "setConsumerAmount");
      arrayOfPropertyDescriptor[1] = new PropertyDescriptor("consumerType", LicenseContent.class, "getConsumerType", "setConsumerType");
      arrayOfPropertyDescriptor[2] = new PropertyDescriptor("extra", LicenseContent.class, "getExtra", "setExtra");
      arrayOfPropertyDescriptor[3] = new PropertyDescriptor("holder", LicenseContent.class, "getHolder", "setHolder");
      arrayOfPropertyDescriptor[4] = new PropertyDescriptor("info", LicenseContent.class, "getInfo", "setInfo");
      arrayOfPropertyDescriptor[5] = new PropertyDescriptor("issued", LicenseContent.class, "getIssued", "setIssued");
      arrayOfPropertyDescriptor[6] = new PropertyDescriptor("issuer", LicenseContent.class, "getIssuer", "setIssuer");
      arrayOfPropertyDescriptor[7] = new PropertyDescriptor("notAfter", LicenseContent.class, "getNotAfter", "setNotAfter");
      arrayOfPropertyDescriptor[8] = new PropertyDescriptor("notBefore", LicenseContent.class, "getNotBefore", "setNotBefore");
      arrayOfPropertyDescriptor[9] = new PropertyDescriptor("subject", LicenseContent.class, "getSubject", "setSubject");
    }
    catch (IntrospectionException localIntrospectionException)
    {
      localIntrospectionException.printStackTrace();
    }
    return arrayOfPropertyDescriptor;
  }
  
  private static EventSetDescriptor[] getEdescriptor()
  {
    return eventSets;
  }
  
  private static MethodDescriptor[] getMdescriptor()
  {
    return methods;
  }
  
  public BeanDescriptor getBeanDescriptor()
  {
    return getBdescriptor();
  }
  
  public PropertyDescriptor[] getPropertyDescriptors()
  {
    return getPdescriptor();
  }
  
  public EventSetDescriptor[] getEventSetDescriptors()
  {
    return getEdescriptor();
  }
  
  public MethodDescriptor[] getMethodDescriptors()
  {
    return getMdescriptor();
  }
  
  public int getDefaultPropertyIndex()
  {
    return -1;
  }
  
  public int getDefaultEventIndex()
  {
    return -1;
  }
}
