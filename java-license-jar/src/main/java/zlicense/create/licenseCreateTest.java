package zlicense.create;



public class licenseCreateTest {
	public static void main(String[] args){
		CreateLicense cLicense = new CreateLicense();
		
		cLicense.setParam("C:/license/createparam.properties");
		//
		cLicense.create();
	}
}
