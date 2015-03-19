package zlicense.de.schlichtherle.xml;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import org.apache.commons.codec.binary.Base64;

public final class GenericCertificate
  implements Serializable, XMLConstants
{
  private static final String BASE64_CHARSET = "US-ASCII";
  private static final String SIGNATURE_ENCODING = "US-ASCII/Base64";
  private transient boolean locked;
  private String encoded;
  private String signature;
  private String signatureAlgorithm;
  private String signatureEncoding;
  private transient PropertyChangeSupport propertyChangeSupport;
  private transient VetoableChangeSupport vetoableChangeSupport;
  
  public GenericCertificate() {}
  
  public GenericCertificate(GenericCertificate paramGenericCertificate)
  {
    try
    {
      setEncoded(paramGenericCertificate.getEncoded());
      setSignature(paramGenericCertificate.getSignature());
      setSignatureAlgorithm(paramGenericCertificate.getSignatureAlgorithm());
      setSignatureEncoding(paramGenericCertificate.getSignatureEncoding());
    }
    catch (PropertyVetoException localPropertyVetoException)
    {
      throw new AssertionError(localPropertyVetoException);
    }
  }
  
  public final synchronized void sign(Object paramObject, PrivateKey paramPrivateKey, Signature paramSignature)
    throws NullPointerException, GenericCertificateIsLockedException, PropertyVetoException, PersistenceServiceException, InvalidKeyException
  {
    if (paramPrivateKey == null) {
      throw new NullPointerException("signingKey");
    }
    if (paramSignature == null) {
      throw new NullPointerException("signingEngine");
    }
    PropertyChangeEvent localPropertyChangeEvent = new PropertyChangeEvent(this, "locked", Boolean.valueOf(isLocked()), Boolean.TRUE);
    if (isLocked()) {
      throw new GenericCertificateIsLockedException(localPropertyChangeEvent);
    }
    fireVetoableChange(localPropertyChangeEvent);
    try
    {
      byte[] arrayOfByte1 = PersistenceService.store2ByteArray(paramObject);
      paramSignature.initSign(paramPrivateKey);
      paramSignature.update(arrayOfByte1);
      byte[] arrayOfByte2 = Base64.encodeBase64(paramSignature.sign());
      String str = new String(arrayOfByte2, 0, arrayOfByte2.length, "US-ASCII");
      setEncoded(new String(arrayOfByte1, "UTF-8"));
      setSignature(str);
      setSignatureAlgorithm(paramSignature.getAlgorithm());
      setSignatureEncoding("US-ASCII/Base64");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new AssertionError(localUnsupportedEncodingException);
    }
    catch (SignatureException localSignatureException)
    {
      throw new AssertionError(localSignatureException);
    }
    this.locked = true;
    firePropertyChange(localPropertyChangeEvent);
  }
  
  public final synchronized void verify(PublicKey paramPublicKey, Signature paramSignature)
    throws NullPointerException, GenericCertificateIsLockedException, PropertyVetoException, InvalidKeyException, SignatureException, GenericCertificateIntegrityException
  {
    if (paramPublicKey == null) {
      throw new NullPointerException("verificationKey");
    }
    if (paramSignature == null) {
      throw new NullPointerException("verificationEngine");
    }
    PropertyChangeEvent localPropertyChangeEvent = new PropertyChangeEvent(this, "locked", Boolean.valueOf(isLocked()), Boolean.TRUE);
    if (isLocked()) {
      throw new GenericCertificateIsLockedException(localPropertyChangeEvent);
    }
    fireVetoableChange(localPropertyChangeEvent);
    try
    {
      byte[] arrayOfByte1 = getEncoded().getBytes("UTF-8");
      paramSignature.initVerify(paramPublicKey);
      paramSignature.update(arrayOfByte1);
      byte[] arrayOfByte2 = Base64.decodeBase64(getSignature().getBytes("US-ASCII"));
      if (!paramSignature.verify(arrayOfByte2)) {
        throw new GenericCertificateIntegrityException();
      }
      setSignatureAlgorithm(paramSignature.getAlgorithm());
      setSignatureEncoding("US-ASCII/Base64");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new AssertionError(localUnsupportedEncodingException);
    }
    this.locked = true;
    firePropertyChange(localPropertyChangeEvent);
  }
  
  public final boolean isLocked()
  {
    return this.locked;
  }
  
  public Object getContent()
    throws GenericCertificateNotLockedException, PersistenceServiceException
  {
    if (!isLocked()) {
      throw new GenericCertificateNotLockedException();
    }
    return PersistenceService.load(getEncoded());
  }
  
  public final String getEncoded()
  {
    return this.encoded;
  }
  
  public synchronized void setEncoded(String paramString)
    throws GenericCertificateIsLockedException
  {
    if (paramString == null)
    {
      if (this.encoded != null) {}
    }
    else if (paramString.equals(this.encoded)) {
      return;
    }
    PropertyChangeEvent localPropertyChangeEvent = new PropertyChangeEvent(this, "encoded", getEncoded(), paramString);
    if (isLocked()) {
      throw new GenericCertificateIsLockedException(localPropertyChangeEvent);
    }
    this.encoded = paramString;
    firePropertyChange(localPropertyChangeEvent);
  }
  
  public final String getSignature()
  {
    return this.signature;
  }
  
  public synchronized void setSignature(String paramString)
    throws GenericCertificateIsLockedException
  {
    if (paramString == null)
    {
      if (this.signature != null) {}
    }
    else if (paramString.equals(this.signature)) {
      return;
    }
    PropertyChangeEvent localPropertyChangeEvent = new PropertyChangeEvent(this, "signature", getSignature(), paramString);
    if (isLocked()) {
      throw new GenericCertificateIsLockedException(localPropertyChangeEvent);
    }
    this.signature = paramString;
    firePropertyChange(localPropertyChangeEvent);
  }
  
  public final String getSignatureAlgorithm()
  {
    return this.signatureAlgorithm;
  }
  
  public synchronized void setSignatureAlgorithm(String paramString)
    throws GenericCertificateIsLockedException
  {
    if (paramString == null)
    {
      if (this.signatureAlgorithm != null) {}
    }
    else if (paramString.equals(this.signatureAlgorithm)) {
      return;
    }
    PropertyChangeEvent localPropertyChangeEvent = new PropertyChangeEvent(this, "signatureAlgorithm", getSignatureAlgorithm(), paramString);
    if (isLocked()) {
      throw new GenericCertificateIsLockedException(localPropertyChangeEvent);
    }
    this.signatureAlgorithm = paramString;
    firePropertyChange(localPropertyChangeEvent);
  }
  
  public final String getSignatureEncoding()
  {
    return this.signatureEncoding;
  }
  
  /**
   * @deprecated
   */
  public synchronized void setSignatureEncoding(String paramString)
    throws GenericCertificateIsLockedException
  {
    if (paramString == null)
    {
      if (this.signatureEncoding != null) {}
    }
    else if (paramString.equals(this.signatureEncoding)) {
      return;
    }
    PropertyChangeEvent localPropertyChangeEvent = new PropertyChangeEvent(this, "signatureEncoding", getSignatureEncoding(), paramString);
    if (isLocked()) {
      throw new GenericCertificateIsLockedException(localPropertyChangeEvent);
    }
    this.signatureEncoding = paramString;
    firePropertyChange(localPropertyChangeEvent);
  }
  
  public final synchronized void addVetoableChangeListener(VetoableChangeListener paramVetoableChangeListener)
  {
    if (this.vetoableChangeSupport == null) {
      this.vetoableChangeSupport = new VetoableChangeSupport(this);
    }
    this.vetoableChangeSupport.addVetoableChangeListener(paramVetoableChangeListener);
  }
  
  public final void removeVetoableChangeListener(VetoableChangeListener paramVetoableChangeListener)
  {
    if (this.vetoableChangeSupport == null) {
      return;
    }
    this.vetoableChangeSupport.removeVetoableChangeListener(paramVetoableChangeListener);
  }
  
  protected final void fireVetoableChange(PropertyChangeEvent paramPropertyChangeEvent)
    throws PropertyVetoException
  {
    if (this.vetoableChangeSupport == null) {
      return;
    }
    this.vetoableChangeSupport.fireVetoableChange(paramPropertyChangeEvent);
  }
  
  public final synchronized void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
  {
    if (this.propertyChangeSupport == null) {
      this.propertyChangeSupport = new PropertyChangeSupport(this);
    }
    this.propertyChangeSupport.addPropertyChangeListener(paramPropertyChangeListener);
  }
  
  public final void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
  {
    if (this.propertyChangeSupport == null) {
      return;
    }
    this.propertyChangeSupport.removePropertyChangeListener(paramPropertyChangeListener);
  }
  
  protected final void firePropertyChange(PropertyChangeEvent paramPropertyChangeEvent)
  {
    if (this.propertyChangeSupport == null) {
      return;
    }
    this.propertyChangeSupport.firePropertyChange(paramPropertyChangeEvent);
  }
}
