package zlicense.de.schlichtherle.license;

import java.io.InputStream;

import zlicense.de.schlichtherle.license.KeyStoreParam;

public class LocalKeyStoreParam
	implements KeyStoreParam {

	private String alias;
	private String keyPwd;
	private String storePwd;
	private InputStream stream;

	public LocalKeyStoreParam(String localAlias, String localKeyPwd, String localStorePwd, InputStream localStream) {
		setAlias(localAlias);
		setKeyPwd(localKeyPwd);
		setStorePwd(localStorePwd);
		setStream(localStream);
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getKeyPwd() {
		return keyPwd;
	}

	public void setKeyPwd(String keyPwd) {
		this.keyPwd = keyPwd;
	}

	public String getStorePwd() {
		return storePwd;
	}

	public void setStorePwd(String storePwd) {
		this.storePwd = storePwd;
	}

	public InputStream getStream() {
		return stream;
	}

	public void setStream(InputStream stream) {
		this.stream = stream;
	}
}