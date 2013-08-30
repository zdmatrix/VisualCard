package zdmatrix.hed.visualcard.DataTypeTrans;

public class APDUCmd {

	public static final String APDUGet8BytesRandom = "0084000008";
	public static final String APDUSelectFile = "00a4000002";
	public static final String APDUExternIdentify = "0082000008";
	public static final String APDUGetReadyPublicKey = "80bf040280";
	public static final String APDUReadPublicKey = "00c0000010";
	public static final String APDUGetReadyAuthCode = "80bf080000";
	public static final String APDUGetAuthCode = "00c0000006";
	public static final String APDUSendOTPCmdToCard = "80bf050000";
	public static final String APDUWaitCardButtonPushed = "80bf060000";
	public static final String APDUReadSelectFileData = "00b0000004";
	public static final String APDUUpdateSelectFileData = "00d6000004";
	public static final String APDUGenerateRSAKey = "8046111200";
	
	

//  80bf070000:等待按键，按键按下后显示一个随机数
	
}
