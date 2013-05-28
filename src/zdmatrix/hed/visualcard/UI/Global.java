package zdmatrix.hed.visualcard.UI;

import java.io.File;
import java.io.FileOutputStream;
//import java.util.Date;
import java.util.Random;

import zdmatrix.hed.visualcard.Alg.EncryptIn3DES;


import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class Global {
	public static final int LOGIC0FRENQUENCE = 1250;
	public static final int LOGIC1FRENQUENCE = 2500;
	public static final short AMPLITUDEMAX = 32767;
	public static final short AMPLITUDEMIN = -3;
	public static final short DECODETHRESHOLDVAL = 10000;
/*
	public static final int HALFLOGIC1NUMMAX = 12;
	public static final int HALFLOGIC1NUMMIN = 6;
	public static final int HALFLOGIC0NUMMAX = 22;
	public static final int HALFLOGIC0NUMMIN = 14;
*/

	public static final int HALFLOGIC1NUMMAX = 14;
	public static final int HALFLOGIC1NUMMIN = 6;
	public static final int HALFLOGIC0NUMMAX = 24;
	public static final int HALFLOGIC0NUMMIN = 16;
	
	public static final int FILELENGTHONETIME = 16;
	public static final int LEADSIGNALNUM = 0x10;
	public static final int BITMAPLENGTH = 470;
	public static final int GETIMAGEDATACOUNTER = 29;
	public static final int GETIMAGEDATATIMES = 30;
	public static final int RECORDTHRESHOLDVAL = 10000;
	public static final int INITBANLANCECASH = 10000;
	public static final int DEFAULTSRCACCOUNT = 1234567890;
	
	public static final int GENERATERSAKEYPAIROK = 0xc0;
	
	public static final String CMDGETRANDOMDATA = "48050084000008";
	public static final String CMDEXTERNIDENTIFY = "080d0082000008";
	public static final String CMDGETREADYPUBLICKEY = "080580BF040280";
	public static final String CMDGETRESPONSE = "480500C0000010";
	public static final String CMDSELECTFILE = "080700A4000002";
	public static final String CMDCARDBUTTON = "080580BF060000";
	public static final String CMDGETREADYAUTHCODE = "080580BF080000";
/*
	public static final String FILEADDR = "00BF";
	public static final String CMDRSAPUBKEY = "081480E000100E";
	public static final String DATARSAPUBKEY = "0011000001010000000000000000";
	public static final String CMDRSAPRIKEY = "080F80E000110A";
	public static final String DATARSAPRIKEY = "00120000010100000001";
*/
	public static final String CMDRSAKEYPAIR = "08058046111200";
	
	
	
	static final int 	frequency = 44100;
//	static final int 	channelInConfiguration = AudioFormat.CHANNEL_IN_STEREO;
	static final int 	channelInConfiguration = AudioFormat.CHANNEL_IN_MONO;
	static final int 	audioInEncoding = AudioFormat.ENCODING_PCM_16BIT;
	static final int 	channelOutConfiguration = AudioFormat.CHANNEL_OUT_STEREO;
	static final int 	audioOutEncoding = AudioFormat.ENCODING_PCM_16BIT;
	
	
	public static final short logic0buff[] = {   
			AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN,
			AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN,
			AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN,
			AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN,
			AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN,
			AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN,

			AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX,
			AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX,
			AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX,
			AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX,
			AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX,
			AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX,
			
			};

	public static final short logic1buff[] = {  
			AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN,
			AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, 
			AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN,
			AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN, AMPLITUDEMIN,
			

			
			AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX,
			AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, 

			
			AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX,		
			AMPLITUDEMAX, AMPLITUDEMAX, AMPLITUDEMAX,		
			

			};


	public static final int Key[] = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
									0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
	
	private static final int NUM0[]  = { 7, 1, 2, 4, 6, 5, 3, 1 };
	private static final int NUM1[]  = { 2, 2, 6 };
	private static final int NUM2[]  = { 6, 1, 2, 4, 3, 5, 6 };
	private static final int NUM3[]  = { 7, 1, 2, 4, 3, 4, 6, 5 };
	private static final int NUM4[]  = { 6, 1, 3, 4, 2, 4, 6 };
	private static final int NUM5[]  = { 6, 2, 1, 3, 4, 6, 5 };
	private static final int NUM6[]  = { 7, 2, 1, 3, 4, 6, 5, 3 };
	private static final int NUM7[]  = { 3, 1, 2, 6 };
	private static final int NUM8[]  = { 9, 1, 2, 4, 6, 5, 3, 1, 3, 4 };
	private static final int NUM9[]  = { 7, 4, 2, 1, 3, 4, 6, 5 };

	private static final int NUM09[][] = { NUM0, NUM1, NUM2, NUM3, NUM4, NUM5, NUM6, NUM7, NUM8, NUM9 };
	
	
	
	public int nImageData[] = new int[470];
	
	public class ReturnVal{
		boolean bLogic = false;
		int nOffset = 0;
		int nStatusCode = 0;
		String strRetInfo = "";
		String strRetData = "";
		public int nPcbCharacter = 0;
		int nLength = 0;
		int nData[] = new int[30];
	}

	public class FrameCharacter{
		int nPcbCharacter = 0;
		int nLength = 0;
		int nData[] = new int[30];
	}
	
	public class ReturnDataInString{
		public String strSW;
		public String strData;
	}
	
	public static int nBanlanceCash = 0;
	public static int nLogic1NumMax = 0;
	public static int nLogic1NumMin = 0;
	public static int nLogic0NumMax = 0;
	public static int nLogic0NumMin = 0;
	
	
	public static int nHalfLogic1NumMax = 0;
	public static int nHalfLogic1NumMin = 0;
	public static int nHalfLogic0NumMax = 0;
	public static int nHalfLogic0NumMin = 0;
	
	
	AudioTrack 			audioTrack = null;	
	
	
	public ReturnDataInString getRetDataInStr(ReturnVal retcode){
		ReturnDataInString ret = new ReturnDataInString();
		String str[] = new String[retcode.nLength];
		int offset = 0;
		for(int k = 0; k < retcode.nLength; k ++){
			try{
			str[k] = Integer.toString(retcode.nData[k], 16);
			
			if(retcode.nData[k] < 16)
				str[k] = "0" + str[k];
			}catch(Exception e){
				Log.v("zdmatrix", "在getRetDataInStr方法中第 " + k + "次错误,错误原因 " + e);
			}
		}
		
		ret.strData = "";
		ret.strSW = "";
		for(offset = 0; offset < retcode.nLength - 2; offset ++){
			ret.strData += str[offset];
		}
		while(offset < retcode.nLength){
			ret.strSW += str[offset];
			offset ++;
		}
		return ret;
	}
	
	public void txDataToMCU(String apducmd, String data, boolean bDecOrHex){
		
		String strVersion = android.os.Build.VERSION.RELEASE;
		
				
		int nPlayBufSize = AudioTrack.getMinBufferSize(frequency,channelOutConfiguration, 
		    	audioOutEncoding);
		
		short[] sTxDataBuf = encodePhoneSignal(apducmd, data, bDecOrHex);
/*
		try{
			audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
				channelOutConfiguration, audioOutEncoding,
				4 * nPlayBufSize, AudioTrack.MODE_STREAM);
		}catch(Exception e){
			Log.v("zdmatrix", "err message is : " + e);
		}
		audioTrack.play();//开始播放
		
		audioTrack.write(sTxDataBuf, 0, sTxDataBuf.length);
		
		audioTrack.stop();
*/
		try{
			if(audioTrack != null){
				audioTrack.release();
				audioTrack = null;
			}
			if(sTxDataBuf.length < (nPlayBufSize / 2))
			audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
				channelOutConfiguration, audioOutEncoding,
				nPlayBufSize, AudioTrack.MODE_STATIC);
	
			else
				audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
						channelOutConfiguration, audioOutEncoding,
						2 * sTxDataBuf.length, AudioTrack.MODE_STATIC);
/*
			audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
					channelOutConfiguration, audioOutEncoding,
					4 * sTxDataBuf.length, AudioTrack.MODE_STATIC);
*/
		}catch(Exception e){
			Log.v("zdmatrix", "err message is : " + e);
		}
//		audioTrack.setStereoVolume(1.0f, 0.0f);
		audioTrack.write(sTxDataBuf, 0, sTxDataBuf.length);
		audioTrack.flush();
		
		if(strVersion.equals("2.3.6")){
			while(AudioTrack.SUCCESS != audioTrack.setStereoVolume(1.0f, 0.0f));
		}else if(strVersion.equals("2.2.2")){
			while(AudioTrack.SUCCESS != audioTrack.setStereoVolume(0.0f, 1.0f));
		}
		
		audioTrack.play();//开始播放
		
		/*
		while(!playstop){
			if(AudioTrack.PLAYSTATE_STOPPED == audioTrack.getPlayState()){
				audioTrack.flush();
				playstop = true;
			}
		}
		 */
		
	}
	
	public ReturnVal rxDataFromMCU(){
		boolean 				stillrx 				= true;
		short[]					sLeadCodeBuf 			= new short[50];
		short[] 				buf 					= new short[16000];
		boolean[]				bLeadCodeBuf			= new boolean[50];
		ReturnVal 				retcode;
		AudioRecord 			audioRecord;

		int						startbitcount			= 0;
		
		
		
		
		int nRecBufSize = AudioRecord.getMinBufferSize(frequency,channelInConfiguration, 
	    		audioInEncoding);

		if(nRecBufSize * 2 < buf.length)
			audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
				channelInConfiguration, audioInEncoding, buf.length);
		else
			audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
					channelInConfiguration, audioInEncoding, nRecBufSize * 2);
//		Log.v("zdmatrix", "创建audiorecord对象成功");
		audioRecord.startRecording();//开始录制
		
		while(stillrx){
			
			try{
//				long now = android.os.SystemClock.uptimeMillis();   
//				Log.v("zdmatrix", "第   " + num1 + "  次开始调用read方法接收引导码     " + now);
			audioRecord.read(sLeadCodeBuf, 0, sLeadCodeBuf.length);
			}catch(Exception e){
				Log.v("zdmatrix", "err message is : " + e);
			}
			for(int num = 0; (num < (sLeadCodeBuf.length - 4)) && stillrx; num ++){
				if(sLeadCodeBuf[num] > RECORDTHRESHOLDVAL){
					bLeadCodeBuf[num] = true;
				}
				else if(sLeadCodeBuf[num] < -RECORDTHRESHOLDVAL){
					bLeadCodeBuf[num] = false;
				}
			
				if(sLeadCodeBuf[num + 1] > RECORDTHRESHOLDVAL){
					bLeadCodeBuf[num + 1] = true;
				}
				else if(sLeadCodeBuf[num + 1] < -RECORDTHRESHOLDVAL){
					bLeadCodeBuf[num + 1] = false;
				}
				
				if((bLeadCodeBuf[num] ^ bLeadCodeBuf[num + 1]) && bLeadCodeBuf[num] && !bLeadCodeBuf[num + 1]){
				
				/*通过相邻的2个采样点和阀值比较然后结果异或的方法找到跃变点，然后判断是否连着4个点符合要求*/
//				if((sLeadCodeBuf[num] > RECORDTHRESHOLDVAL) ^ (sLeadCodeBuf[num + 1] < -RECORDTHRESHOLDVAL)){
					if((sLeadCodeBuf[num + 2] < -RECORDTHRESHOLDVAL) && (sLeadCodeBuf[num + 3] < -RECORDTHRESHOLDVAL)
							&& (sLeadCodeBuf[num + 4] < -RECORDTHRESHOLDVAL)){
						startbitcount ++;			//连续4个采样点符合要求，认为一个有效信号
						num += 4;
					}
					else{
						startbitcount = 0;
						num += 4;
					}
				}
				else if((bLeadCodeBuf[num] ^ bLeadCodeBuf[num + 1]) && !bLeadCodeBuf[num] && bLeadCodeBuf[num + 1]){
//				else if((sLeadCodeBuf[num] < -RECORDTHRESHOLDVAL) ^ (sLeadCodeBuf[num + 1] > RECORDTHRESHOLDVAL)){
					if((sLeadCodeBuf[num + 2] > RECORDTHRESHOLDVAL) && (sLeadCodeBuf[num + 3] > RECORDTHRESHOLDVAL)
							&& (sLeadCodeBuf[num + 4] > RECORDTHRESHOLDVAL)){
						startbitcount ++;
						num += 4;
					}
					else{
						startbitcount = 0;
						num += 4;
					}
				}
				if(3 == startbitcount){			//连续3个有效信号，认为是引导码到来，而非干扰信号。开始采集数据
					audioRecord.read(buf, 0, buf.length);
					stillrx = false;
					startbitcount = 0;
					break;
				}
			}
/*
	
//目前使用的，输入信号是方波时的方法		
			for(int num = 0; num < sLeadCodeBuf.length && stillrx; num ++){
				
				if((sLeadCodeBuf[num] > RECORDTHRESHOLDVAL) || (sLeadCodeBuf[num] < -RECORDTHRESHOLDVAL)){
					j ++;
					if(4 < j){
						start = true;
						startbitcount ++;
					}
					else{
						start = false;
						startbitcount = 0;
					}
				}
				else
					j = 0;
				if(start && (3 == startbitcount)){
					audioRecord.read(buf, 0, buf.length);
					j = 0;
					stillrx = false;	
					break;
				}
				else if(start)
					break;
				
*/
/*
//输入信号非方波时的判断方法
				if(sLeadCodeBuf[num] > RECORDTHRESHOLDVAL){
					if(start){
						audioRecord.read(buf, 0, buf.length);
						j = 0;
						stillrx = false;	
						break;
					}
					else{
						start = false;
						j ++;
						if(4 < j )
							start = true;
					}
					
				}
				else
					j = 0;
*/					
			
		}
		audioRecord.stop();
//		Log.v("zdmatrix", "record的状态为：" + audioRecord.getRecordingState());
		
		boolean[] DataBuf = preProcessPhoneSignal(buf);			//预处理采集到的数据，转为bool类型
		retcode = decodePhoneSignal(DataBuf, buf);			//开始解码
		return retcode;
	}
	
		
		/*编码输入命令，将输入的命令转为表示逻辑1或0的方波点*/
		short[] encodePhoneSignal(String cmd, String data, boolean flag){
			
			if(null != data){

				
				if(flag){

					int apducmdbody[] = stringHexToIntArray(cmd);
					int apdudata[] = stringHexToIntArray(data);
					int tmp[] = new int[apducmdbody.length + apdudata.length];
					System.arraycopy(apducmdbody, 0, tmp, 0, apducmdbody.length);
					System.arraycopy(apdudata, 0, tmp, apducmdbody.length, apdudata.length);
					short dst[] = arraMerge(tmp);
					return dst;
				}
				else{

					int apducmdbody[] = stringHexToIntArray(cmd);
					int apdudata[] = stringDecToIntArray(data);
					int tmp[] = new int[apducmdbody.length + apdudata.length];
					System.arraycopy(apducmdbody, 0, tmp, 0, apducmdbody.length);
					System.arraycopy(apdudata, 0, tmp, apducmdbody.length, apdudata.length);
					short dst[] = arraMerge(tmp);
					return dst;
				}
			}
			else{

				int apducmdbody[] = stringHexToIntArray(cmd);
				int tmp[] = new int[apducmdbody.length];
				System.arraycopy(apducmdbody, 0, tmp, 0, apducmdbody.length);
				short dst[] = arraMerge(tmp);
				return dst;
			}
		}
		
		/*将int类型的数组转化为表示方波的点*/
		short[] arraMerge(int array[]){
			int dstlength = 0;
//			int leadsignalnum = 16;
			dstlength += (LEADSIGNALNUM * Global.logic1buff.length + Global.logic0buff.length);	//16个逻辑1前导信号+1个逻辑0起始位
			for(int i = 0; i < array.length; i ++){
				for(int j = 7; j >= 0; j --){
					if(1 == (((array[i] >> j) & 0x01))){
						dstlength += Global.logic1buff.length;
					}
					else
						dstlength += Global.logic0buff.length;
						
				}
			}
			
			short tmp[] = new short[dstlength];
			for(int i = 0; i < LEADSIGNALNUM; i ++)
				System.arraycopy(Global.logic1buff, 0, tmp, i * Global.logic1buff.length, Global.logic1buff.length);
			int index = LEADSIGNALNUM * Global.logic1buff.length;
			
			for(int i = 0; i < Global.logic0buff.length; i ++, index ++)
				tmp[index] = Global.logic0buff[i];
			for(int i = 0; i < array.length; i ++){
				for(int j = 7; j >= 0; j --){
					if(1 == (((array[i] >> j) & 0x01))){
						for(int m = 0; m < Global.logic1buff.length; m ++){
							tmp[index] = Global.logic1buff[m];
							index ++;
						}
					}
					else{
						for(int m = 0; m < Global.logic0buff.length; m ++){
							tmp[index] = Global.logic0buff[m];
							index ++;
						}
					}
				}
			}
			
			return tmp;
			
		}
		
		/*由于MCU存在时钟误差，上传的信号并不是标准的2.5KHz和1.25KHz，利用该函数
		 * 获得代表逻辑1的1个周期的方波的计数个数*/
		int getBaseWaveCount(boolean buf[]){
			int index = 0;
			int count = 0;
			boolean start = false;
			boolean	end = false;
			
//直接判断整个周期个数的方法
			while(!start){
				if((buf[index] ^ buf[index + 1]) && !buf[index] && buf[index + 1]){
					start = true;
					index += 1;
				}
				else{
					index ++;
					start = false;
				}
			}
			
			while(start && !end){
				if((buf[index] ^ buf[index + 1]) && !buf[index] && buf[index + 1]){
					if(count > 5){
						end = true;
						count += 1;
					}
					else
						end = false;
				}
				else{
					count ++;
					end = false;
					index ++;
				}
			}
			
			nLogic1NumMax = count + 3;
			nLogic1NumMin = count - 3;
			nLogic0NumMax = 2 * count + 3;
			nLogic0NumMin = 2 * count - 3;
			
/*
 *半周期判断的方法 
 
			for(index = 0; index < buf.length; index ++){
				if(buf[index] ^ buf[index + 1]){
					start = true;
					index += 1;
					break;
				}
				else{
					start = false;
				}
			}
			while(start){
				if(buf[index] ^ buf[index + 1]){
					start = false;
					count ++;		//buf[index+1]是跃变点,所以buf[index]也要算一个计数点
				}
				else{
					count ++;
					start = true;
					index ++;
				}
			}
			
			nHalfLogic1NumMax = count + 4;
			nHalfLogic1NumMin = count - 4;
			nHalfLogic0NumMin = 2 * count - 4;
			nHalfLogic0NumMax = 2 * count + 4;
*/			
			return index;
		}
		
		/*解析一个bit*/
		ReturnVal decodeOneBit(boolean buf[], ReturnVal resault){
			boolean	start = false;
			boolean end = false;
			int count = 0;
			int index = resault.nOffset;
			
			
//直接判断整个周期个数的方法
			while(!start){
				if((buf[index] ^ buf[index + 1]) && !buf[index] && buf[index + 1]){
					start = true;
					index += 1;
				}
				else{
					index ++;
					start = false;
				}
			}
			
			while(start && !end){
				if((buf[index] ^ buf[index + 1]) && !buf[index] && buf[index + 1] || count > 50){
					end = true;
					count += 1;
				}
				else{
					count ++;
					index ++;
					end = false;
				}
			}
			
			if(count <= nLogic0NumMax && count >= nLogic0NumMin){
				resault.bLogic = false;
			}
			else{
				if(count <= nLogic1NumMax && count >= nLogic1NumMin){
					resault.bLogic = true;
				}
			}
			
			resault.nOffset = index;
			
/*
 * 分别判断两段半周期波形个数的方法			
			while(!end){
				if(!(buf[index] ^ buf[index + 1])){
					count ++;
					index ++;
				}
				else{
//					if(count <= HALFLOGIC0NUMMAX && count >= HALFLOGIC0NUMMIN){
					if(count <= nHalfLogic0NumMax && count >= nHalfLogic0NumMin){
					
					logicval = false;
						if(!firsthalf){
							firsthalf = true;
							
						}
						else{
							secondhalf = true;
							
						}
					}
					else{
//						if(count <= HALFLOGIC1NUMMAX && count >= HALFLOGIC1NUMMIN){
						if(count <= nHalfLogic1NumMax && count >= nHalfLogic1NumMin){
							logicval = true;
							if(!firsthalf){
								firsthalf = true;
								
							}
							else{
								secondhalf = true;
								
							}
						}
					}
					count = 0;
					index ++;
				}
				
				if((secondhalf && firsthalf) || (index > buf.length / 2) || (count > 45)){
					end = true;
					secondhalf = false;
					firsthalf = false;
					resault.nOffset = index;
					resault.bLogic = logicval;
				}
				
				
			}
*/
			return resault;
		}

		/*解析起始位*/
		int decodeStartBit(boolean buf[], ReturnVal resault){
			boolean startbit = false;
//			boolean firsthalf = false;
//			boolean secondhalf = false;
			
			
//			int firsttrippoint = 0;
//			int secondtrippoint = 0;
			
			int index = resault.nOffset;

//直接判断整个周期个数的方法
			while(!startbit){
				boolean	start = false;
				boolean	end = false;
				int count = 0;
			while(!start){
				if((buf[index] ^ buf[index + 1]) && !buf[index] && buf[index + 1]){
					start = true;
					index += 1;
				}
				else{
					index ++;
					start = false;
				}
			}
			
			while(start && !end){
				if((buf[index] ^ buf[index + 1]) && !buf[index] && buf[index + 1]){
					end = true;
					count += 1;
				}
				else{
					count ++;
					index ++;
					end = false;
				}
			}
			
			if(count <= nLogic0NumMax && count >= nLogic0NumMin){
				startbit = true;
			}
			else
				startbit = false;
			}
			
/*
 * 利用2段半周期进行判断的方法			
			while(!startbit){
				if(!(buf[index] ^ buf[index + 1])){
					count ++;
					index ++;
				}
				else{
//					if(count <= HALFLOGIC0NUMMAX && count >= HALFLOGIC0NUMMIN){
					if(count <= nHalfLogic0NumMax && count >= nHalfLogic0NumMin){
						if(!firsthalf){
							firsthalf = true;
							firsttrippoint = index;
						}
						else{
							secondhalf = true;
							secondtrippoint = index;
						}
					}
//					if(secondhalf && firsthalf && ((secondtrippoint - firsttrippoint) > HALFLOGIC0NUMMIN)){
				if(secondhalf && firsthalf && ((secondtrippoint - firsttrippoint) > nHalfLogic0NumMin)){
						startbit = true;
						secondhalf = false;
						firsthalf = false;
					}
					count = 0;
					index ++;
				}
			}
*/			

/*			
			while(!startbit){
				if(!(buf[index] ^ buf[index + 1])){
					count ++;
					index ++;
				}
				else{
					if(!firsthalf){
						if(count <= HALFLOGIC0NUMMAX && count >= HALFLOGIC0NUMMIN){
							firsthalf = true;
							firsttrippoint = index;
						}
					}
					else{
						secondtrippoint = index;
						count = secondtrippoint - firsttrippoint;
						if(count <= HALFLOGIC0NUMMAX && count >= HALFLOGIC0NUMMIN){
							secondhalf = true;
						}
					}
						
					
					if(secondhalf && firsthalf){
						startbit = true;
						secondhalf = false;
						firsthalf = false;
					}
					count = 0;
					index ++;
				}
			}
*/

			return index;
		}

		
		
		/*解析整个接收到的数据*/
		ReturnVal decodePhoneSignal(boolean buf[], short buf1[]){
			ReturnVal resault = new ReturnVal();
			
			char tmp[] = new char[8];
			
			resault.nOffset = getBaseWaveCount(buf);
			
			resault.nOffset = decodeStartBit(buf, resault);
//			Log.v("zdmatrix", "startbit offset val is  " + resault.nOffset);
			
			for(int i = 0; i < 8; i ++){
				resault = decodeOneBit(buf, resault);
				if(resault.bLogic)
					tmp[i] = '1';
				else
					tmp[i] = '0';

			}
			
			resault.nPcbCharacter = Integer.parseInt(String.valueOf(tmp), 2);
			
			if(0x80 == resault.nPcbCharacter || 0x88 == resault.nPcbCharacter){
			
			for(int i = 0; i < 8; i ++){
				resault = decodeOneBit(buf, resault);
				if(resault.bLogic)
					tmp[i] = '1';
				else
					tmp[i] = '0';

			}
			resault.nLength = Integer.parseInt(String.valueOf(tmp), 2);

			if(18 < resault.nLength || 0 == resault.nLength){
				Log.v("zdmatrix", "在decodePhoneSignal中接收数据错误，数据保存在sd/test目录下");
				byte[] databuf = shortToByteArray(buf1);
				try {
					saveToSDCard("record.txt", databuf);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

//			Log.v("zdmatrix", "return data's length is  " + resault.nLength);
			
			for(int i = 0; i < resault.nLength; i ++){
				for(int j = 0; j < 8; j ++){
					resault = decodeOneBit(buf, resault);
					if(resault.bLogic)
						tmp[j] = '1';
					else
						tmp[j] = '0';

				}
				try{
				resault.nData[i] = Integer.parseInt(String.valueOf(tmp), 2);
				}catch(Exception e){
					Log.v("zdmatrix", "在decodePhoneSignal中接收错误，第 " + i + "次返回信息" + e);
				}
			}
//			Log.v("zdmatrix", "20000个数据解析完毕，等待下一次接收解析.");
			}
			
			else{
				switch(resault.nPcbCharacter){
					case 0x81:
						resault.strRetInfo = "卡未插入";
						break;
					case 0x82:
						resault.strRetInfo = "ATR数据接收错误";
						break;
					case 0x87:
						resault.strRetInfo = "不支持的操作";
						break;
					case 0x89:
						resault.strRetInfo = "卡无响应";
						break;
					case 0x8A:
						resault.strRetInfo = "不支持的APDU指令";
						break;
					case 0x8B:
						resault.strRetInfo = "7816协议超时";
						break;
					default:
						resault.strRetInfo = "未知错误";
						break;
				}
			}
			return resault;
		}
		
		/*预处理接收到的数据*/
		boolean[] preProcessPhoneSignal(short buf[]){
			boolean tmpbuf[] = new boolean[buf.length];

			for(int i = 0; i < (buf.length / 2); i ++){
				if(buf[i] < RECORDTHRESHOLDVAL)
					tmpbuf[i] = false;
				else
					tmpbuf[i] = true;
			}
			return tmpbuf;
		}			
			

	/*16进制的字符串转化为int型数组*/
	private int[] stringHexToIntArray(String s){
		int i, k;
		int length = s.length() / 2;
		int dst[] = new int[length];
		String str[] = new String[length];
		char ch[] = new char[s.length()];
		ch = s.toCharArray();
		for(i = 0, k = 0; i < s.length(); k ++){
			str[k] = String.valueOf(ch, i, 2);
			dst[k] = Integer.parseInt(str[k], 16);
			i += 2;
		}
		return dst;
	}
	/*10进制的字符串转化为int型数组*/
	private int[] stringDecToIntArray(String s){
		char ch[] = s.toCharArray();
		int length = ch.length;
		int dst[] = new int[length];
		String str[] = new String[length];
		for(int i = 0; i < length; i ++){
			str[i] = String.valueOf(ch, i, 1);
			str[i] = "3" + str[i];
			dst[i] = Integer.parseInt(str[i], 16);
			
		}
		return dst;
	}
	/*int型数组转化为String型*/
	public String intToString(int[] data, int startindex, int endindex){
		String sdata = "";
		int[] tmp = new int[data.length];
		System.arraycopy(data, 0, tmp, 0, data.length);
		for(int i = startindex; i < endindex; i ++){
			try{
			if(tmp[i] < 16)
				sdata += ("0" + Integer.toHexString(tmp[i]));
			else
				sdata += Integer.toHexString(tmp[i]);
			}catch(Exception e){
				Log.v("zdmatrix", "在intToString中接收错误，第 " + i + "次返回信息" + e);
			}
		}
		return sdata;
	}
	/*生成公私钥对*/
	ReturnVal generateRSAKeyPair(){
		EncryptIn3DES encryptin3DES = new EncryptIn3DES();
		String sw = "";
		ReturnVal	retcode = new ReturnVal();
		//取8byte随机数，0084000005
		retcode = get8ByteRandomData();
		sw= intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
		if(sw.equals("9000")){
			int encryptdata[] = encryptin3DES.DES3Go(retcode.nData, Global.Key, 0);
			//外部认证，0082000008 + 8个3DES加密的数据
			retcode = externIdentify(encryptdata);
			sw= intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
			if(sw.equals("9000")){
				//生成公私钥对，8046111200
				txDataToMCU(CMDRSAKEYPAIR, null, true);
				retcode = rxDataFromMCU();
				sw= intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
				if(!sw.equals("9000")){
					retcode.strRetInfo = "生成密钥对错误";
				}

			}
			else{
				retcode.strRetInfo = "外部认证错";
			}
		}
		else{
			retcode.strRetInfo = "取随机数错";
		}
		
		
		if(0x88 == retcode.nPcbCharacter){
			retcode.bLogic = true;	
			retcode.strRetInfo = "生成公钥操作成功";
		}
			
		return retcode;	
	}
	
	/*获取RSA公钥*/
	ReturnVal getRSAPubKey(){
		int resetdatanum = 0x80;
		String staticsw = "61";
		String returnsw = "";
		String sw = "";
		ReturnVal	retcode = new ReturnVal();
		String[] strPublicKey = new String[8];
		int[][] publickey = new int[8][];
		for(int i = 0; i < 8; i ++)
			publickey[i] = new int[16];
			//准备公钥，80bf040280	
			retcode = getReadyPublicKey();
			sw= intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
			if(sw.equals("9000")){
				for(int i = 0; i < 8; i ++){
					//读取公钥，因为MCU的限制，每次只能回传32byte数据，因此对于长数据，要分次读取
					//使用getresponse命令，00c0000010，每次回传16byte数据
					retcode = readPublicKey();
					sw= intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
					returnsw += (staticsw + Integer.toHexString(resetdatanum - (i + 1) * 16));
					//返回sw=“61xx”，其中xx代表剩余数据长度，最后返回9000
					if(7 == i)
						returnsw = "9000";
					if(sw.equals(returnsw)){
						for(int j = 0; j < 16; j ++)
							publickey[i][j] = retcode.nData[j];
						strPublicKey[i] = intToString(publickey[i], 0, 16);
					}
					else{
						retcode.strRetInfo = "读16个公钥错";
						break;
					}
					returnsw = "";
				}
				for(String str:strPublicKey)
					retcode.strRetData += str;
			}
			else{
				retcode.strRetInfo = "发送准备公钥数据指令错";
			}
			
		
//		else
//			retcode.strRetInfo = "生成公私钥对错误";
		return retcode;
	}
	
	ReturnVal get8ByteRandomData(){
		String apducmd = Global.CMDGETRANDOMDATA;
		String data = null;
		txDataToMCU(apducmd, data, true);
		ReturnVal retcode = rxDataFromMCU();
		return retcode;
	}
	
	ReturnVal externIdentify(int[] data){
		String apducmd = Global.CMDEXTERNIDENTIFY;
		String sdata = intToString(data, 0, data.length);
		txDataToMCU(apducmd, sdata, true);
		ReturnVal retcode = rxDataFromMCU();
		return retcode;
	}
	
	ReturnVal readPublicKey(){
		String apducmd = Global.CMDGETRESPONSE;
		String data = null;
		txDataToMCU(apducmd, data, true);
		ReturnVal retcode = rxDataFromMCU();
		return retcode;
	}
	
	ReturnVal getReadyPublicKey(){
		String apducmd = Global.CMDGETREADYPUBLICKEY;
		String data = null;
		txDataToMCU(apducmd, data, true);
		ReturnVal retcode = rxDataFromMCU();
		String sw= intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
		if(sw.equals("9000")){
			retcode.bLogic = true;
		}
		else
			retcode.bLogic = false;
		return retcode;
	}
	
	ReturnVal waitPushCardButton(){
		String apducmd = Global.CMDCARDBUTTON;
		String data = null;
		txDataToMCU(apducmd, data, true);
		ReturnVal retcode = rxDataFromMCU();
		String sw= intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
		if(sw.equals("9000")){
			retcode.bLogic = true;
		}
		else
			retcode.bLogic = false;
		return retcode;
	}
	
	ReturnVal resetCard(){
		String apducmd = "02";
		String data = null;
		txDataToMCU(apducmd, data, true);
		ReturnVal retcode = rxDataFromMCU();
		
		if(0x82 == retcode.nPcbCharacter){
			retcode.bLogic = true;
		}
		else
			retcode.bLogic = false;
		return retcode;
	}
	
	ReturnVal getReadyBitmapFile(int times){
		String offset = "";
		String apducmd = "";
		String sw = "";
		
		if(times * FILELENGTHONETIME < 256){
			if(times * FILELENGTHONETIME < 16)
				offset = "00" + "0" + Integer.toHexString(times * FILELENGTHONETIME);
			else
				offset = "00" + Integer.toHexString(times * FILELENGTHONETIME);
		}
		else 
			offset = "0" + Integer.toHexString(times * FILELENGTHONETIME);
		String offsethigh = offset.substring(0, 2);
		String offsetlow = offset.substring(2, 4);
		if(GETIMAGEDATACOUNTER != times){
			apducmd = "080580bf03" + offsethigh + offsetlow;

		}
		else{
			apducmd = "080580bf03" + offsethigh + offsetlow;

		}

		txDataToMCU(apducmd, null, true);
		ReturnVal retcode = rxDataFromMCU();
		sw= intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
		if(sw.equals("9000")){
			retcode.bLogic = true;
			retcode.strRetInfo = "第" + (times + 1) + "次卡准备图片数据成功";
		}
		else{
			retcode.strRetInfo = "第" + (times + 1) + "次卡准备图片数据失败";
		}
		return retcode;
	}
	
	
	
	ReturnVal readBitmapFile(int times){
		String sw = "";
		
		txDataToMCU(CMDGETRESPONSE, null, true);
		ReturnVal retcode = rxDataFromMCU();
		sw= intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
		if(!sw.equals("9000"))
			retcode.strRetInfo = "第 " + (times + 1) + " 次取16个图片数据错误";
		else{
			retcode.strRetInfo = "第 " + (times + 1) + " 次取16个图片数据成功";
			retcode.bLogic = true;
		}
		return retcode;
	}
	
	ReturnVal generateBitmapFile(String data){
		String cmd;
		
		int length = data.length() + 5;
		if(length < 16){
			cmd = "08" + "0" + Integer.toHexString(length) 
					+ "80bf0200" + "0" + Integer.toHexString(length - 5);
		}
		else if((length - 5) < 16)
			cmd = "08"  + Integer.toHexString(length)
			+ "80bf0200" + "0" + Integer.toHexString(length - 5);
		else
			cmd = "08"  + Integer.toHexString(length)
			+ "80bf0200" + Integer.toHexString(length - 5);
		
		txDataToMCU(cmd, data, false);
		ReturnVal retcode = rxDataFromMCU();
		return retcode;
	}
	
	public void echoTest(String cmd, String data, boolean flag){
		
		boolean 				stillrx 				= true;
		short[]					sLeadCodeBuf 			= new short[200];
		short[] 				buf 					= new short[20000];
		
		AudioRecord 			audioRecord;
		AudioTrack 			audioTrack;
		
		int nPlayBufSize = AudioTrack.getMinBufferSize(frequency,channelOutConfiguration, 
		    	audioOutEncoding);
		
		short[] sTxDataBuf = encodePhoneSignal(cmd, data, flag);
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
				channelOutConfiguration, audioOutEncoding,
				4 * nPlayBufSize, AudioTrack.MODE_STATIC);
	
		audioTrack.write(sTxDataBuf, 0, sTxDataBuf.length);
		audioTrack.flush();
		audioTrack.play();//开始播放
		
		
		int nRecBufSize = AudioRecord.getMinBufferSize(frequency,channelInConfiguration, 
	    		audioInEncoding);
		
		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
				channelInConfiguration, audioInEncoding, nRecBufSize * 10);
		
		audioRecord.startRecording();//开始录制
			
		while(stillrx){
			audioRecord.read(sLeadCodeBuf, 0, sLeadCodeBuf.length);
			for(int num = 0; num < sLeadCodeBuf.length && stillrx; num ++){
				if(sLeadCodeBuf[num] > 15000){
					audioRecord.read(buf, 0, buf.length);
					stillrx = false;	
					break;
				}
			}
		}
		audioRecord.stop();
		
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
				channelOutConfiguration, audioOutEncoding,
				4 * nPlayBufSize, AudioTrack.MODE_STATIC);
	
		audioTrack.write(buf, 0, buf.length);
		audioTrack.flush();
		audioTrack.play();//开始播放
	}
	
	ReturnVal disBanlanceOnCard(int banlance){
		String data = Integer.toString(banlance); 
		String apducmd = "080" + Integer.toHexString((data.length() + 5)) + "80bf01000" + Integer.toHexString(data.length());

		txDataToMCU(apducmd, data, false);

		ReturnVal retcode = rxDataFromMCU();
		
		String sw= intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
        
        if(sw.equals("9000")){
        	retcode.bLogic = true;
        	
        }
        else{
        	retcode.bLogic = false;
        	
        }
        return retcode;
	}
	
	ReturnVal disAuthorCodeOnCard(String authcode){
		
		String data = authcode; 
		String apducmd = "080" + Integer.toHexString((data.length() + 5)) + "80bf01000" + Integer.toHexString(data.length());

		txDataToMCU(apducmd, data, false);

		ReturnVal retcode = rxDataFromMCU();
		
		String sw= intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
        
        if(sw.equals("9000")){
        	retcode.bLogic = true;
        	
        }
        else{
        	retcode.bLogic = false;
        	
        }
        return retcode;
	}

	
	ReturnVal getAuthCode(){
		String apducmd = CMDGETREADYAUTHCODE;
		String data = null;
		String sw = "";
		txDataToMCU(apducmd, data, true);
		ReturnVal retcode = rxDataFromMCU();
		sw= intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
		if(!sw.equals("9000"))
			retcode.strRetInfo = "认证码准备错误，请重新准备";
		else{
			apducmd = "480500c0000006";
			data = null;
			txDataToMCU(apducmd, data, true);
			retcode = rxDataFromMCU();
			sw= intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
			if(sw.equals("9000")){
				retcode.bLogic = true;
				retcode.strRetInfo = "认证码接收完毕";
			}
			else{
				retcode.bLogic = false;
				retcode.strRetInfo = "认证码接收错误";
			}
		}
		return retcode;
	}
	
	
	private final int ORG[][] ={
			{0, 0},
			{6, 0},
			{0, 6},
			{6, 6},
			{0, 12},
			{6, 12},
	};

	/*
	private final short g_c1[] =
		{
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
		};

	private final short g_c2[] =
		{
			0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00,
			0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0x00, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00,
			0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
		};

	private final short g_c3[] =
		{
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0x00, 0x00,
			0x00, 0xFF, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0xFF, 0x00, 0x00, 0x00, 0x00,
			0x00, 0xFF, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0xFF, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0xFF, 0x00, 0xFF, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00,
			0x00, 0xFF, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0xFF, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0xFF, 0x00, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0xFF, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
		};

	private final short g_c4[] =
		{
			0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF,
			0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00
		};

	private final short g_c5[] =
		{
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF,
			0x00, 0xFF, 0xFF, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
		};

	private final short g_c6[] =
		{
			0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF,
			0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF,
			0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF,
			0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF,
			0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF,
			0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF,
			0x00, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0xFF,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
		};

	private final short g_c7[] =
		{
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF,
			0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0xFF, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00,
			0x00, 0xFF, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF,
			0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
		};

	private final short g_c8[] =
		{
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF,
			0x00, 0x00, 0x00, 0xFF, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00,
			0x00, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
		};

	private final short g_c9[] =
		{
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00,
			0x00, 0x00, 0xFF, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00,
			0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00,
			0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x00, 0x00
		};
	
*/	
	
	
	private void drawLine(int x0, int y0, int x1, int y1, int color, int[] buf){
		boolean steep = false;
		int deltax;
		int deltay;
		int error;
		int ystep;
		int x;
		int y;
		steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
		if ( steep )
		{
			int tmp = 0;
			tmp = x0;
			x0 = y0;
			y0 = tmp;
			
			tmp = x1;
			x1 = y1;
			y1 = tmp;
//			swap ( x0, y0 );
//			swap ( x1, y1 );
		}

		if ( x0 > x1 )
		{
			int tmp = x0;
			x0 = x1;
			x1 = tmp;
			
			tmp = y0;
			y0 = y1;
			y1 = tmp;
//			swap ( x0, x1 );
//			swap ( y0, y1 );
		}

		deltax = x1 - x0;

		deltay = Math.abs( y1 - y0 );

		error = deltax / 2;

		y = y0;

		if ( y0 < y1 )
			ystep = 1;
		else
			ystep = -1;

		for ( x = x0; x <= x1; x ++ )
		{

			if ( steep )
				setPixel ( y, x, color, buf);
				
			else{
				
//				if(y < 0)
//					Log.v("zdmatrix", "输入的y像素错误，为：" + y);
				setPixel ( x, y, color, buf);
			}
			error = error - deltay;

			if ( error < 0 )
			{
				y = y + ystep;
				error = error + deltax;
			}
		}
	}

	private void setPixel(int x, int y, int color, int[] buf){
		int gx = 0x3E + ((16 - y) * 24) + (x / 8);
		int gPixelMask = (0x80 >> (x % 8));
		buf[gx] &= ~gPixelMask;
	}
	
	private void display7Seg (int nx, int ny, final int[] staticbuf, int[] buf)
	{
		int		z = 0;
		int[][] p = new int[6][];
		for(int i = 0; i < 6; i ++)
			p[i] = new int[2];
		Random	rand = new Random();
		
		
		
		int x1, y1, x2 = 0, y2 = 0;

		for ( z = 0; z < 6; z++ )
		{
			int random = rand.nextInt(3);
			p[z][0] = ORG[z][0] + random;
			random = rand.nextInt(3);
			p[z][1] = ORG[z][1] + random;
		}
		
		x1 = p[staticbuf[1] - 1][0];
		y1 = p[staticbuf[1] - 1][1];
		for ( z = 2; z <= staticbuf[0]; z++ )
		{
			x2 = p[staticbuf[z] - 1][0];
			y2 = p[staticbuf[z] - 1][1];
//			if((ny + y2) < 0 ){
//				Log.v("zdmatrix", "显示7段错误，输入的n2 = " + ny + "，输入的y2 = " + y2);
//			}
			drawLine (nx + x1, ny + y1, nx + x2, ny + y2, 0, buf);
/*
			// 将边线加宽
			if ( x1 < 7 && x2 < 7 )
				drawLine ( nx + x1 - 1, ny + y1, nx + x2 - 1, ny + y2, 0, buf );
			else if ( x1 >= 7 && x2 >= 7 )
				drawLine ( nx + x1 + 1, ny + y1, nx + x2 + 1, ny + y2, 0, buf );
			else if ( y1 < 7 && y2 < 7 )
				drawLine ( nx + x1, ny + y1 - 1, nx + x2, ny + y2 - 1, 0, buf );
			else
				drawLine ( nx + x1, ny + y1 + 1, nx + x2, ny + y2 + 1, 0, buf );
*/
			// 交换坐标
			x1 = x2;
			y1 = y2;
			x2 = 0;
			y2 = 0;
		}

		
	}

	public int[] getImageData(String sdata){
		int length = sdata.length();
		int ndata[] = stringDecToIntArray(sdata);
		int ndataarray[] = new int[470];
		for(int i = 0; i < 470; i ++)
			ndataarray[i] = 0xff;
		
		// 位图文件的类型，必须为BM
		ndataarray[0x00] = 0x42;		// B
		ndataarray[0x01] = 0x4D;		// M

		// 位图文件的大小，以字节为单位
		ndataarray[0x02] = 0xD6;
		ndataarray[0x03] = 0x01;
		//把4张图片合成在一起，4 × 470
//		ndataarray[0x02] = 0x58;
//		ndataarray[0x03] = 0x07;
		//
		ndataarray[0x04] = 0x00;
		ndataarray[0x05] = 0x00;

		// 位图文件保留字，必须为0
		ndataarray[0x06] = 0x00;
		ndataarray[0x07] = 0x00;
		ndataarray[0x08] = 0x00;
		ndataarray[0x09] = 0x00;

		// 位图数据的起始位置，以相对于位图
		ndataarray[0x0A] = 0x3E;
		ndataarray[0x0B] = 0x00;
		ndataarray[0x0C] = 0x00;
		ndataarray[0x0D] = 0x00;

		// 位图信息头的长度
		ndataarray[0x0E] = 0x28;
		ndataarray[0x0F] = 0x00;
		ndataarray[0x10] = 0x00;
		ndataarray[0x11] = 0x00;

		// 位图的宽度
		ndataarray[0x12] = 0xC0;
		ndataarray[0x13] = 0x00;
		ndataarray[0x14] = 0x00;
		ndataarray[0x15] = 0x00;

		// 位图的高度
		ndataarray[0x16] = 0x11;
//		ndataarray[0x16] = 0x44;		//为了4张图拼接在一起
		ndataarray[0x17] = 0x00;
		ndataarray[0x18] = 0x00;
		ndataarray[0x19] = 0x00;

		// 位图的位面数
		ndataarray[0x1A] = 0x01;
		ndataarray[0x1B] = 0x00;

		// 每个象素的位数
		ndataarray[0x1C] = 0x01;
		ndataarray[0x1D] = 0x00;

		// 压缩说明
		ndataarray[0x1E] = 0x00;
		ndataarray[0x1F] = 0x00;
		ndataarray[0x20] = 0x00;
		ndataarray[0x21] = 0x00;

		// 用字节数表示的位图数据的大小，该数必须是4的倍数
		ndataarray[0x22] = 0x98;
		ndataarray[0x23] = 0x01;
		
//		ndataarray[0x22] = 0x1a;
//		ndataarray[0x23] = 0x07;
		
		ndataarray[0x24] = 0x00;
		ndataarray[0x25] = 0x00;

		// 用象素/米表示的水平分辨率
		ndataarray[0x26] = 0x00;
		ndataarray[0x27] = 0x00;
		ndataarray[0x28] = 0x00;
		ndataarray[0x29] = 0x00;

		// 用象素/米表示的垂直分辨率
		ndataarray[0x2A] = 0x00;
		ndataarray[0x2B] = 0x00;
		ndataarray[0x2C] = 0x00;
		ndataarray[0x2D] = 0x00;

		// 位图使用的颜色数
		ndataarray[0x2E] = 0x00;
		ndataarray[0x2F] = 0x00;
		ndataarray[0x30] = 0x00;
		ndataarray[0x31] = 0x00;

		// 指定重要的颜色数
		ndataarray[0x32] = 0x00;
		ndataarray[0x33] = 0x00;
		ndataarray[0x34] = 0x00;
		ndataarray[0x35] = 0x00;

		// 调色板 (黑色)
		ndataarray[0x36] = 0x00;
		ndataarray[0x37] = 0x00;
		ndataarray[0x38] = 0x00;
		ndataarray[0x39] = 0x00;

		// 调色板 (底色)
		ndataarray[0x3A] = 0xFF;
		ndataarray[0x3B] = 0xFF;
		ndataarray[0x3C] = 0xFF;
		ndataarray[0x3D] = 0x00;
		
		
		int xstart = 0;
		switch(length){
		case 15:
		case 16:
			xstart = 0;
			break;
			
		case 13:
		case 14:
			xstart = 12;
			break;
			
		case 11:
		case 12:
			xstart = 24;
			break;
			
		case 9:
		case 10:
			xstart = 36;
			break;
		
		case 7:
		case 8:
			xstart = 48;
			break;
			
		case 5:
		case 6:
			xstart = 60;
			break;
		
		case 3:
		case 4:
			xstart = 72;
			break;	
			
		case 1:
		case 2:
			xstart = 84;
			break;		
			
		default:
			break;
			
			
		}
		int ystart = 0;
		
		for(int i = 0; i < length; i ++){
			int num = ndata[i] & 0x0f;
			display7Seg(xstart, ystart, NUM09[num], ndataarray);
			xstart += 12;
		}
		
		return ndataarray;
	}
	
	public byte[] getWholeImage(int[] src, byte[] dst, int index){
		byte[] tmp = new byte[470];
		for(int i = 0; i < 470; i ++)
			tmp[i] = (byte)src[i];
		System.arraycopy(tmp, 0, dst, index * 470, 470);
		return dst;
	}
	
	public byte[] shortToByteArray(short s[]) {  
		byte[] targets = new byte[s.length * 2];  
		byte[] tmp = new byte[2];
		for(int j = 0 ; j < s.length; j ++){
			for (int i = 0; i < 2; i++) {  
				int offset = (tmp.length - 1 - i) * 8;  
				short s1 = (short)(s[j] >>> offset);
				tmp[2 - i - 1] = (byte) (s1 & 0xff);  
			}
			System.arraycopy(tmp, 0, targets, j * 2, 2);
		}
		return targets;
	}
	
	public void saveToSDCard(String filename, byte[] buf) throws Exception{
		String dir=Environment.getExternalStorageDirectory()+"/test";
		
		java.io.File a=new java.io.File(dir);

		if (!a.exists()){
			a.mkdir();
		}
		File file=new File(a,filename);
		FileOutputStream outStream=new FileOutputStream(file);
		outStream.write(buf);
		
		outStream.close();
	}
	
	ReturnVal selectFile(String addr){
		String apducmd = Global.CMDSELECTFILE;
		String data = addr;
		txDataToMCU(apducmd, data, true);
		ReturnVal retcode = rxDataFromMCU();
		
		String sw= intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
		if(!sw.equals("9000"))
			retcode.bLogic = false;
		else{
			
			retcode.bLogic = true;
		}
		return retcode;
	}
	
	/*从选中的文件里读取数据*/
	ReturnVal readSelectFileData(int offsetlow, int offsethigh, int length){
		String stroffsetlow = "";
		String stroffsethigh = "";
		String apducmd = "";
		String data = null;
		
		if(offsetlow < 16)
			stroffsetlow = "0" + Integer.toString(offsetlow, 16);
		else
			stroffsetlow = Integer.toString(offsetlow, 16);
		
		if(offsethigh < 16)
			stroffsethigh = "0" + Integer.toString(offsethigh, 16);
		else
			stroffsethigh = Integer.toString(offsethigh, 16);
		
		apducmd = "4805" + "00b0" + stroffsethigh + stroffsetlow + "0" + Integer.toString(length, 16);
		
		txDataToMCU(apducmd, data, true);
		ReturnVal retcode = rxDataFromMCU();
		
		String sw= intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
		if(!sw.equals("9000"))
			retcode.bLogic = false;
		else{
			
			retcode.bLogic = true;
		}
		return retcode;
	}
	
	/*读余额*/
	ReturnVal readBanlance(String fileid, int offsetlow, int offsethigh, int length){
		resetCard();
		ReturnVal retcode = selectFile(fileid);
        if(retcode.bLogic){
        	retcode = readSelectFileData(offsetlow, offsethigh, length);
        	if(retcode.bLogic){
        		nBanlanceCash = 0;
        		for(int i = 0; i < 4; i ++){
        			nBanlanceCash += (retcode.nData[i] << ((3 - i) * 8));
        		}
        		String banlance = Integer.toHexString(nBanlanceCash);
                if(banlance.length() < (length * 2)){
                	int index = 0;
                	int max = 8 - banlance.length();
                	while(index < max){
                		banlance = "0" + banlance;
                		index ++;
                	}
                }
                retcode.strRetData = banlance;
                
        	}
        	else{
        		retcode.strRetInfo = "读余额错";
        		retcode.bLogic = false;
        	}
        }
        else{
        	retcode.strRetInfo = "选文件错";
        	retcode.bLogic = false;
        }
        
        return retcode;
	}
	
	
	/*更新选中文件里的指定数据*/
	ReturnVal updateSelectFileData(int offsetlow, int offsethigh, String data, int length){
		String stroffsetlow = "";
		String stroffsethigh = "";
		String apducmd = "";
		
		int len = data.length();
		int index = 0;
		if(offsetlow < 16)
			stroffsetlow = "0" + Integer.toString(offsetlow, 16);
		else
			stroffsetlow = Integer.toString(offsetlow, 16);
		
		if(offsethigh < 16)
			stroffsethigh = "0" + Integer.toString(offsethigh, 16);
		else
			stroffsethigh = Integer.toString(offsethigh, 16);
		
		apducmd = "080" + Integer.toString(length + 5, 16) + "00d6" + stroffsethigh + stroffsetlow + "0" + Integer.toString(length, 16);
		
		if(len < (length * 2)){
			while(index < (length * 2 - len)){
				data = "0" + data;
				index ++;
			}
		}
		txDataToMCU(apducmd, data, true);
		ReturnVal retcode = rxDataFromMCU();
		
		String sw= intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
		if(!sw.equals("9000"))
			retcode.bLogic = false;
		else{
			
			retcode.bLogic = true;
		}
		return retcode;
	}
	

}
