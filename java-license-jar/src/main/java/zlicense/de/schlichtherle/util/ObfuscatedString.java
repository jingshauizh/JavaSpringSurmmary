package zlicense.de.schlichtherle.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public final class ObfuscatedString
{
  private static final String UTF8 = new String(new char[] { 'U', 'T', 'F', '8' });
  private final String s;
  
  private static final long toLong(byte[] paramArrayOfByte, int paramInt)
  {
    long l = 0L;
    int i = Math.min(paramArrayOfByte.length, paramInt + 8);
    int j = i;
    for (;;)
    {
      j--;
      if (j < paramInt) {
        break;
      }
      l <<= 8;
      l |= paramArrayOfByte[j] & 0xFF;
    }
    return l;
  }
  
  private static final void toBytes(long paramLong, byte[] paramArrayOfByte, int paramInt)
  {
    int i = Math.min(paramArrayOfByte.length, paramInt + 8);
    for (int j = paramInt; j < i; j++)
    {
      paramArrayOfByte[j] = ((byte)(int)paramLong);
      paramLong >>= 8;
    }
  }
  
  public static String obfuscate(String paramString)
  {
    if (paramString.indexOf(0) != -1) {
      throw new IllegalArgumentException(new ObfuscatedString(new long[] { 2598583114197433456L, -2532951909540716745L, 1850312903926917213L, -7324743161950196342L, 3319654553699491298L }).toString());
    }
    byte[] arrayOfByte;
    try
    {
      arrayOfByte = paramString.getBytes(UTF8);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new AssertionError(localUnsupportedEncodingException);
    }
    Random localRandom = new Random();
    long l1;
    do
    {
      l1 = localRandom.nextLong();
    } while (l1 == 0L);
    localRandom = new Random(l1);
    StringBuffer localStringBuffer = new StringBuffer(new ObfuscatedString(new long[] { -6733388613909857970L, -557652741307719956L, 563088487624542180L, 5623833171491374716L, -2309350771052518321L, 2627844803624578169L }).toString());
    appendHexLiteral(localStringBuffer, l1);
    int i = arrayOfByte.length;
    for (int j = 0; j < i; j += 8)
    {
      long l2 = localRandom.nextLong();
      long l3 = toLong(arrayOfByte, j) ^ l2;
      localStringBuffer.append(", ");
      appendHexLiteral(localStringBuffer, l3);
    }
    localStringBuffer.append(new ObfuscatedString(new long[] { 4756003162039514438L, -7241174029104351587L, 2576762727660584163L, 2432800632635846553L }).toString());
    localStringBuffer.append(paramString.replaceAll("\\\\", new ObfuscatedString(new long[] { 7866777055383403009L, -5101749501440392498L }).toString()).replaceAll("\"", new ObfuscatedString(new long[] { -8797265930671803829L, -5738757606858957305L }).toString()));
    localStringBuffer.append(new ObfuscatedString(new long[] { -4228881123273879289L, 1823585417647083411L }).toString());
    return localStringBuffer.toString();
  }
  
  private static final void appendHexLiteral(StringBuffer paramStringBuffer, long paramLong)
  {
    paramStringBuffer.append("0x");
    paramStringBuffer.append(Long.toHexString(paramLong).toUpperCase());
    paramStringBuffer.append('L');
  }
  
  public ObfuscatedString(long[] paramArrayOfLong)
  {
    int i = paramArrayOfLong.length;
    byte[] arrayOfByte = new byte[8 * (i - 1)];
    long l1 = paramArrayOfLong[0];
    Random localRandom = new Random(l1);
    for (int j = 1; j < i; j++)
    {
      long l2 = localRandom.nextLong();
      toBytes(paramArrayOfLong[j] ^ l2, arrayOfByte, 8 * (j - 1));
    }
    String str;
    try
    {
      str = new String(arrayOfByte, UTF8);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new AssertionError(localUnsupportedEncodingException);
    }
    int k = str.indexOf(0);
    this.s = (k != -1 ? str.substring(0, k) : str);
  }
  
  public String toString()
  {
    return this.s;
  }
}
