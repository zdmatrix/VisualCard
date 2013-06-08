package zdmatrix.hed.visualcard.FunctionMode;

import android.nfc.tech.IsoDep;
import zdmatrix.hed.visualcard.Alg.EncryptIn3DES;
import zdmatrix.hed.visualcard.DataTypeTrans.APDUCmd;
import zdmatrix.hed.visualcard.DataTypeTrans.DataTypeTrans;


public class FunctionMode {
	
	static byte[] randomdata = new byte[8];
	public static final int Key[] = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
	
	public static void updateSelectFileData(String data, IsoDep isodep){
		
		int len = data.length();
		int index = 0;
		
		if(len < 8){
			while(index < (8 - len)){
				data = "0" + data;
				index ++;
			}
		}
		
		String str = APDUCmd.APDUUpdateSelectFileData + data;
		byte[]	apdu = DataTypeTrans.stringHexToByteArray(str);
		try{
			byte[] sw = isodep.transceive(apdu);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static boolean get8ByteRandom(byte[] randomdata, IsoDep isodep){
		boolean bret = false;
		byte[] apdu = DataTypeTrans.stringHexToByteArray(APDUCmd.APDUGet8BytesRandom);
		try{
			
			byte[] ret = isodep.transceive(apdu);
			for(int i = 0; i < 8; i ++){
				randomdata[i] = ret[i];
			}
			String sw = DataTypeTrans.bytesArrayToHexString(ret, ret.length - 2, 2);
			if(sw.equals("9000")){
				bret = true;
			}
			return bret;
		}catch(Exception e){
			e.printStackTrace();
			bret = false;
			return bret;
		}
		
	}
	
	public static boolean disNumOnCard(String decnum, IsoDep isodep){
		boolean bret = false;
		String length = decnum.length() < 16 ? ("0" + Integer.toString(decnum.length(), 16)) : Integer.toString(decnum.length(), 16);
		String apducmd = "80bf0100" + length;
		byte[] apdu = new byte[5 + decnum.length()];
		byte[] apdu1 = DataTypeTrans.stringHexToByteArray(apducmd);
		byte[] apdu2 = DataTypeTrans.stringDecToByteArray(decnum);
		System.arraycopy(apdu1, 0, apdu, 0, apdu1.length);
		System.arraycopy(apdu2, 0, apdu, apdu1.length, apdu2.length);
		try{
			if(!isodep.isConnected()){
				isodep.connect();
			}

			byte[] ret = isodep.transceive(apdu);
			String sw = DataTypeTrans.bytesArrayToHexString(ret, ret.length - 2, 2);
			
			if(sw.equals("9000")){
				bret = true;
			}
			return bret;
		}catch(Exception e){
			e.printStackTrace();
			bret = false;
			return bret;
		}
	}
	
//	public static boolean readSelectFileData(int offsetlow, int offsethigh, int length, IsoDep isodep){
	public static int readSelectFileData(IsoDep isodep){
		int banlance = 0;
		byte[] apdu = DataTypeTrans.stringHexToByteArray(APDUCmd.APDUReadSelectFileData);
		try{
			byte[] ret = isodep.transceive(apdu);
			String sw = DataTypeTrans.bytesArrayToHexString(ret, ret.length - 2, 2);
			
			if(sw.equals("9000")){
				banlance = DataTypeTrans.byteArray2Int(ret, 0, 4);
			}
			return banlance;
		}catch(Exception e){
			banlance = -1;
			e.printStackTrace();
			return banlance;
		}
	}
	
	public static boolean selectFile(String addr, IsoDep isodep){
		boolean bret = false;
		byte[] apdu = DataTypeTrans.stringHexToByteArray(APDUCmd.APDUSelectFile + addr);
		try{
			byte[] ret = isodep.transceive(apdu);
			String sw = DataTypeTrans.bytesArrayToHexString(ret, ret.length - 2, 2);
			if(sw.equals("9000")){
				bret = true;
			}
			return bret;
		}catch(Exception e){
			e.printStackTrace();
			bret = false;
			return bret;
		}
		
	}
	
	
	
	public static boolean generateRSAKeyPair(IsoDep isodep){
		EncryptIn3DES encryptin3DES = new EncryptIn3DES();
		
		boolean bret = false;
		//取8byte随机数，0084000008
		if(get8ByteRandomData(isodep)){
			int[] encryped = DataTypeTrans.byteArray2IntArray(randomdata);
			int encryptdata[] = encryptin3DES.DES3Go(encryped, Key, 0);
			byte[] apdu = DataTypeTrans.intArray2ByteArray(encryptdata);
			if(externIdentify(apdu, isodep)){
/*
				apdu = DataTypeTrans.stringHexToByteArray(APDUCmd.APDUGenerateRSAKey);
				try{
					isodep.setTimeout(10000);
					byte[] ret = isodep.transceive(apdu);
					String sw = DataTypeTrans.bytesArrayToHexString(ret, ret.length - 2, 2);
					if(sw.equals("9000")){
						bret = true;
					}
				}catch(Exception e){
					e.printStackTrace();
					bret = false;					
				}			
*/
				bret = true;
			}else{
				bret = false;
			}
		}
		return bret;
	}
	
	public static boolean get8ByteRandomData(IsoDep isodep){
		boolean bret = false;
		byte[] apdu = DataTypeTrans.stringHexToByteArray(APDUCmd.APDUGet8BytesRandom);
		try{
			byte[] ret = isodep.transceive(apdu);
			String sw = DataTypeTrans.bytesArrayToHexString(ret, ret.length - 2, 2);
			if(sw.equals("9000")){
				for(int i = 0; i < 8; i ++){
					randomdata[i] = ret[i];
				}
				bret = true;
			}
			return bret;
		}catch(Exception e){
			e.printStackTrace();
			bret = false;
			return bret;
		}
	}
	
	public static boolean externIdentify(byte[] data, IsoDep isodep){
		boolean bret = false;
		byte[] apdu = new byte[13];
		byte[] apducmd = DataTypeTrans.stringHexToByteArray(APDUCmd.APDUExternIdentify);
		System.arraycopy(apducmd, 0, apdu, 0, apducmd.length);
		System.arraycopy(data, 0, apdu, apducmd.length, data.length);
		try{
			
			byte[] ret = isodep.transceive(apdu);
			String sw = DataTypeTrans.bytesArrayToHexString(ret, ret.length - 2, 2);
			
			if(sw.equals("9000")){
				bret = true;
				
			}
			return bret;
		}catch(Exception e){
			e.printStackTrace();
			bret = false;
			return bret;
		}
		
	}
	
	public static boolean getReadyPublicKey(IsoDep isodep){
		boolean bret = false;
		byte[] apdu = DataTypeTrans.stringHexToByteArray(APDUCmd.APDUGetReadyPublicKey);
		try{
			byte[] ret = isodep.transceive(apdu);
			String sw = DataTypeTrans.bytesArrayToHexString(ret, ret.length - 2, 2);
		
			if(sw.equals("9000")){
				bret = true;
			}
			return bret;
		}catch(Exception e){
			e.printStackTrace();
			bret = false;
			return bret;
		}
	}
	
	public static boolean readPublicKey(int count, IsoDep isodep, byte[] rsa){
		String readkeysw = "";
		boolean bret = false;
		byte[] apdu = DataTypeTrans.stringHexToByteArray(APDUCmd.APDUReadPublicKey);
		try{
			byte[] ret = isodep.transceive(apdu);
			String sw = DataTypeTrans.bytesArrayToHexString(ret, ret.length - 2, 2);
			if(7 == count){
				readkeysw = "9000";
			}
			else
				readkeysw = "61" + Integer.toHexString(128 - (count + 1) * 16);
			if(sw.equals(readkeysw)){
				for(int i = 0; i < 16; i ++){
					rsa[i] = ret[i];
				}
//				retcode.strRetInfo = "第" + (count + 1) + " 次取公钥成功";
				
//			else{
//				retcode.strRetInfo = "第" + (count + 1) + " 次取公钥失败";
//				errinfo[index] = retcode.strRetInfo;
//				index ++;
//				break;
			
				bret = true;
			}
			else{
				bret = false;
			}
			return bret;
		}catch(Exception e){
			e.printStackTrace();
			bret = false;
			return bret;
		}
	}
	

	


	public static boolean getAuthCode(IsoDep isodep, byte[] authcode){
		boolean bret = false;
		byte[] apdu = DataTypeTrans.stringHexToByteArray(APDUCmd.APDUGetReadyAuthCode);
		try{

			byte[] ret = isodep.transceive(apdu);
			String sw = DataTypeTrans.bytesArrayToHexString(ret, ret.length - 2, 2);
			if(sw.equals("9000")){
				apdu = DataTypeTrans.stringHexToByteArray(APDUCmd.APDUGetAuthCode);
				try{
					ret = isodep.transceive(apdu);
					sw = DataTypeTrans.bytesArrayToHexString(ret, ret.length - 2, 2);
				
					if(sw.equals("9000")){
						for(int i = 0; i < 6; i ++){
							authcode[i] = ret[i];
						}
						bret = true;
					}
				}catch(Exception e){
					e.printStackTrace();
					bret = false;
				}
				bret = true;
			}
			return bret;
		}catch(Exception e){
			e.printStackTrace();
			bret = false;
			return bret;
		}
	}
	
	public static void sendOTPCmdToCard(IsoDep isodep){
		byte[] apdu = DataTypeTrans.stringHexToByteArray(APDUCmd.APDUSendOTPCmdToCard);
		try{
			if(!isodep.isConnected()){
				isodep.connect();
			}
			isodep.setTimeout(30000);
			byte[] ret = isodep.transceive(apdu);
			String sw = DataTypeTrans.bytesArrayToHexString(ret, ret.length - 2, 2);
			if(sw.equals("9000")){
				
			}
		}catch(Exception e){
			e.printStackTrace();
			
		}
	}
	
	public boolean waitCardButtonPushed(IsoDep isodep){
		boolean bret = false;
		byte[] apdu = DataTypeTrans.stringHexToByteArray(APDUCmd.APDUWaitCardButtonPushed);
		try{
			if(!isodep.isConnected()){
				isodep.connect();
			}
			byte[] ret = isodep.transceive(apdu);
			String sw = DataTypeTrans.bytesArrayToHexString(ret, ret.length - 2, 2);
			if(sw.equals("9000")){
				bret = true;
			}
			return bret;
		}catch(Exception e){
			e.printStackTrace();
			bret = false;
			return bret;
		}
	}

	
}
