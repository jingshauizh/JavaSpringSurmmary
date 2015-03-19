package zlicense.de.schlichtherle.license;
/*
 * LicenseManager.java
 *
 * Created on 22. Februar 2005, 13:27
 */
/*
 * Copyright 2005 Schlichtherle IT Services
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.net.URISyntaxException;
import java.util.*;
import java.util.prefs.*;

import javax.swing.filechooser.FileFilter;

import zlicense.de.schlichtherle.license.LicenseContent;
import zlicense.de.schlichtherle.license.LicenseNotary;
import zlicense.de.schlichtherle.license.Resources;
import zlicense.de.schlichtherle.util.ObfuscatedString;
import zlicense.de.schlichtherle.xml.*;




/**
 * This is the top level class which manages all licensing aspects like for
 * instance the creation, installation and verification of license keys.
 * The license manager knows how to install, verify and uninstall full and
 * trial licenses for a given subject and ensures the privacy of the license
 * content in its persistent form (i.e. the <i>license key</i>).
 * For signing, verifying and validating licenses, this class cooperates with
 * a {@link LicenseNotary}.
 * <p>
 * This class is designed to be thread safe.
 *
 * @author Christian Schlichtherle
 */
public class LocalLicenseManager extends zlicense.de.schlichtherle.license.LicenseManager {
	/** The timeout for the license content cache. */
    private static final long TIMEOUT = 30 * 60 * 1000; // half an hour

    /** The key in the preferences used to store the license key. */
    private static final String PREFERENCES_KEY
            = new ObfuscatedString(new long[] {
        0xD65FA96737AE2CB5L, 0xE804D1A38CF9A413L
    }).toString(); /* => "license" */

    /**
     * The suffix for files which hold license certificates.
     */
    public static final String LICENSE_SUFFIX
            = new ObfuscatedString(new long[] {
        0x97187B3A07E79CEEL, 0x469144B7E0D475E2L
    }).toString(); /* => ".lic" - must be lowercase! */
    static {
        assert LICENSE_SUFFIX.equals(LICENSE_SUFFIX.toLowerCase()); // paranoid
    }

    private static final String PARAM = LicenseNotary.PARAM;

    private static final String SUBJECT = new ObfuscatedString(new long[] {
        0xA1CB7D9B4D5E81E4L, 0xD9500F23E58132B6L
    }).toString(); /* => "subject" */

    private static final String KEY_STORE_PARAM = new ObfuscatedString(new long[] {
        0x449C8CDCBA1A80CEL, 0x6FEE3A101634D30BL, 0xD9D7B61A44A2606CL
    }).toString(); /* => "keyStoreParam" */

    private static final String CIPHER_PARAM = new ObfuscatedString(new long[] {
        0xCD54DEE1845B54E4L, 0x1AC47C8C827054BCL, 0x16E53B3A590D62B6L
    }).toString(); /* => "cipherParam" */

    protected static final String CN = new ObfuscatedString(new long[] {
        0x636F59E1FF007F64L, 0xAC9CE58690A43DD0L
    }).toString(); /* => "CN=" */

    private static final String CN_USER = CN + Resources.getString(
            new ObfuscatedString(new long[] {
        0xF3BE4EA2CCDD7EADL, 0x5B6A9F59A1183108L
    }).toString()); /* => "user" */

    private static final String USER = new ObfuscatedString(new long[] {
        0x9F89522C9F6F4A13L, 0xFFDB7A316241AC79L
    }).toString(); /* => "User" */

    private static final String SYSTEM = new ObfuscatedString(new long[] {
        0xEC006BE1C1F75BD6L, 0x54D650CDD244774BL
    }).toString(); /* => "System" */

    private static final String EXC_INVALID_SUBJECT = new ObfuscatedString(new long[] {
        0x8029CDF4E32A76ECL, 0x56FA623D9AEE8C1L, 0x99E7882A708663ACL,
        0x5888C0D72E548FF4L
    }).toString(); /* => "exc.invalidSubject" */

    private static final String EXC_HOLDER_IS_NULL = new ObfuscatedString(new long[] {
        0x6339FEFCDFD84427L, 0x57A2FA0735E47CBEL, 0xED1D06E6EED72950L
    }).toString(); /* => "exc.holderIsNull" */

    private static final String EXC_ISSUER_IS_NULL = new ObfuscatedString(new long[] {
        0xD5E29AC879334756L, 0xF1F7421CD6A06536L, 0x5E086D6468FECBF2L
    }).toString(); /* => "exc.issuerIsNull" */

    private static final String EXC_ISSUED_IS_NULL = new ObfuscatedString(new long[] {
        0xAB8FF89F2DA6C32CL, 0x2A089A9CA80D970EL, 0xCF15F8842FCCD9D5L
    }).toString(); /* => "exc.issuedIsNull" */

    private static final String EXC_LICENSE_IS_NOT_YET_VALID = new ObfuscatedString(new long[] {
        0x4B6BB2804EE7DDB1L, 0xD0BB0A33A41543C5L, 0x5FCEC6DF3725CEE4L,
        0xA165775BBD625344L
    }).toString(); /* => "exc.licenseIsNotYetValid" */

    private static final String EXC_LICENSE_HAS_EXPIRED = new ObfuscatedString(new long[] {
        0xDE2B2A7ACD6DA6DL, 0x9EE12DDECB3D4C0DL, 0xB3CF760B522E8688L,
        0x316BD3E92C17CC40L
    }).toString(); /* => "exc.licenseHasExpired" */

    private static final String EXC_CONSUMER_TYPE_IS_NULL = new ObfuscatedString(new long[] {
        0xD29019F7B1D95C66L, 0xE859C44ACC3EB2FEL, 0xF041027C9003B031L,
        0x27E84AD8870D6063L
    }).toString(); /* => "exc.consumerTypeIsNull" */

    private static final String EXC_CONSUMER_TYPE_IS_NOT_USER = new ObfuscatedString(new long[] {
        0xCE99D49CE98D1E47L, 0x7A3BA300A7DFCEABL, 0x2D2E4B624AD7C4E0L,
        0x2C86A28A075E71C6L, 0x79BCB920E5FB351DL
    }).toString(); /* => "exc.consumerTypeIsNotUser" */

    private static final String EXC_CONSUMER_AMOUNT_IS_NOT_ONE = new ObfuscatedString(new long[] {
        0x5F20CBB98126BB0AL, 0xE8BB696B25D24011L, 0x435CC3AA7263BAE7L,
        0x9DA3066F501717E4L, 0x62FFA4899FBBA3F8L
    }).toString(); /* => "exc.consumerAmountIsNotOne" */

    private static final String EXC_CONSUMER_AMOUNT_IS_NOT_POSITIVE = new ObfuscatedString(new long[] {
        0xB14EB6259B4D7249L, 0xCD02F577511528D8L, 0x39B8CF1E258756DDL,
        0x67488F05891DF916L, 0x4256DE0CFFF62DCAL
    }).toString(); /* => "exc.consumerAmountIsNotPositive" */

    private static final String FILE_FILTER_DESCRIPTION = new ObfuscatedString(new long[] {
        0x2BDDE408C7B71604L, 0xDFCA7DA8912DE4C1L, 0xADA1FC1C1D5F1047L,
        0xD08EAA6CCDC342F3L
    }).toString(); /* => "fileFilter.description" */

    private static final String FILE_FILTER_SUFFIX = new ObfuscatedString(new long[] {
        0xA4BCC907D9FD1290L, 0x614A0A9015D3D8DDL
    }).toString(); /* => " (*.lic)" */

    /**
     * Returns midnight local time today.
     */

    private LicenseParam param; // initialized by setLicenseParam() - should be accessed via getLicenseParam() only!

    //
    // Data computed and cached from the license configuration parameters.
    //

    private LicenseNotary notary; // lazy initialized

    private PrivacyGuard guard; // lazy initialized

    /** The cached certificate of the current license key. */
    private GenericCertificate certificate; // lazy initialized

    /** The time when the certificate was last set. */
    private long certificateTime; // lazy initialized

    /** A suitable file filter for the subject of this license manager. */
    private FileFilter fileFilter; // lazy initialized

    /** The preferences node used to store the license key. */
    private Preferences preferences; // lazy initialized
	public LocalLicenseManager() throws URISyntaxException {
		super();
		// TODO Auto-generated constructor stub
	}
	public LocalLicenseManager(LicenseParam param)
	    throws  NullPointerException,
	            IllegalPasswordException {
		setLicenseParam(param);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 protected synchronized void validate(final LicenseContent content)
	    throws LicenseContentException {
	        final LicenseParam param = getLicenseParam();
	        if (!param.getSubject().equals(content.getSubject()))
	            throw new LicenseContentException(EXC_INVALID_SUBJECT);
	        if (content.getHolder() == null)
	            throw new LicenseContentException(EXC_HOLDER_IS_NULL);
	        if (content.getIssuer() == null)
	            throw new LicenseContentException(EXC_ISSUER_IS_NULL);
	        if (content.getIssued() == null)
	            throw new LicenseContentException(EXC_ISSUED_IS_NULL);
	        final Date now = new Date();
	        final Date notBefore = content.getNotBefore();
	        if (notBefore != null && now.before(notBefore))
	            throw new LicenseContentException(EXC_LICENSE_IS_NOT_YET_VALID);
	        final Date notAfter = content.getNotAfter();
//	        if (notAfter != null && now.after(notAfter))
//	            throw new LicenseContentException(EXC_LICENSE_HAS_EXPIRED);
	        final String consumerType = content.getConsumerType();
	        if (consumerType == null)
	            throw new LicenseContentException(EXC_CONSUMER_TYPE_IS_NULL);
	        final Preferences prefs = param.getPreferences();
	        if (prefs != null && prefs.isUserNode()) {
	            if (!USER.equalsIgnoreCase(consumerType))
	                throw new LicenseContentException(EXC_CONSUMER_TYPE_IS_NOT_USER);
	            if (content.getConsumerAmount() != 1)
	                throw new LicenseContentException(EXC_CONSUMER_AMOUNT_IS_NOT_ONE);
	        } else {
	            if (content.getConsumerAmount() <= 0)
	                throw new LicenseContentException(EXC_CONSUMER_AMOUNT_IS_NOT_POSITIVE);
	        }
	    }
}
