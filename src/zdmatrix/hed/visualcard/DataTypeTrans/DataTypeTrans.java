package zdmatrix.hed.visualcard.DataTypeTrans;


public class DataTypeTrans {
	public static byte[] stringHexToByteArray(String s){
		int i, k;
		int length = s.length() / 2;
		int dst[] = new int[length];
		byte[] ret = new byte[length];
		String str[] = new String[length];
		char ch[] = new char[s.length()];
		ch = s.toCharArray();
		for(i = 0, k = 0; i < s.length(); k ++){
			str[k] = String.valueOf(ch, i, 2);
			dst[k] = Integer.parseInt(str[k], 16);
			ret[k] = (byte)dst[k];
			i += 2;
		}
		return ret;
	}
	
	/*10进制的字符串转化为int型数组*/
	public static byte[] stringDecToByteArray(String s){
		char ch[] = s.toCharArray();
		int length = ch.length;
		int dst[] = new int[length];
		byte[] ret = new byte[length];
		String str[] = new String[length];
		for(int i = 0; i < length; i ++){
			str[i] = String.valueOf(ch, i, 1);
			str[i] = "3" + str[i];
			dst[i] = Integer.parseInt(str[i], 16);
			ret[i] = (byte)dst[i];
		}
		return ret;
	}
	
	/*int型数组转化为String型*/
	public static String intArrayToString(int[] data, int startindex, int endindex){
		String sdata = "";
		for(int i = startindex; i < endindex; i ++){
			try{
			if(data[i] < 16)
				sdata += ("0" + Integer.toHexString(data[i]));
			
			else
				sdata += Integer.toHexString(data[i]);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return sdata;
	}
	
	public static int byteArray2Int(byte[] array, int startindex, int length) {  
        int result = 0;  
        byte loop;  
  
        for (int i = startindex; i < length; i++) {  
            loop = array[i];  
            int offSet = length -i -1;  
            result += (loop & 0xFF) << (8 * offSet);  
  
        }  
        return result;  
    }
	
	public static byte[] intArray2ByteArray(int[] array){
		byte[] ret = new byte[array.length];
		for(int i = 0; i < array.length; i ++){
			ret[i] = (byte)array[i];
		}
		return ret;
	}
	
	public static int[] byteArray2IntArray(byte[] array){
		int[] ret = new int[array.length];
		for(int i = 0; i < array.length; i ++){
			ret[i] = (int)array[i];
			
			ret[i] &= 0xff;
		}
		
		return ret;
	}
	
	//字符序列转换为16进制字符串
	public static String bytesArrayToHexString(byte[] src, int startindex, int length) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		char[] buffer = new char[2];
		for (int i = startindex; i < startindex + length; i++) {
			buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
			buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
			stringBuilder.append(buffer);
		}
		return stringBuilder.toString();
	}
	
	/*10进制的字符串转化为int型数组*/
	public static int[] stringDecToIntArray(String s){
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
}
