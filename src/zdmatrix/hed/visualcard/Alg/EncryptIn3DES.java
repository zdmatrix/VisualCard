package zdmatrix.hed.visualcard.Alg;

public class EncryptIn3DES {
	private final int IPTable[] =
		{
			58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4,
			62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8,
			57, 49, 41, 33, 25, 17,  9, 1, 59, 51, 43, 35, 27, 19, 11, 3,
			61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7
		};

	private final int PC1Table[] =
		{
			57, 49, 41, 33, 25, 17,  9,  1, 58, 50, 42, 34, 26, 18,
			10,  2, 59, 51, 43, 35, 27, 19, 11,  3, 60, 52, 44, 36,
			63, 55, 47, 39, 31, 23, 15,  7, 62, 54, 46, 38, 30, 22,
			14,  6, 61, 53, 45, 37, 29, 21, 13,  5, 28, 20, 12,  4
		};

	private final int loopTable[] = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };

	private final int PC2Table[] =
		{
			14, 17, 11, 24,  1,  5,  3, 28, 15,  6, 21, 10,
			23, 19, 12,  4, 26,  8, 16,  7, 27, 20, 13,  2,
			41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48,
			44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32
		};

	private final int ETable[] =
		{
			32,  1,  2,  3,  4,  5,  4,  5,  6,  7,  8,  9,
			8,  9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17,
			16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25,
			24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32,  1
		};

	private final int SBoxTable[][][] =
		{
			// S1
			{
			{14, 4,	13,	 1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,  7,},
			{0, 15,  7,  4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5,  3,  8,},
			{4,  1, 14,  8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10,  5,  0,},
			{15, 12,  8,  2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0,  6, 13,},
			},

			// S2
			{
			{15,  1,  8, 14,  6, 11,  3,  4,  9,  7,  2, 13, 12,  0,  5, 10,},
			{3, 13,  4,  7, 15,  2,  8, 14, 12,  0,  1, 10,  6,  9, 11,  5,},
			{0, 14,  7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15,},
			{13,  8, 10,  1,  3, 15,  4,  2, 11,  6,  7, 12,  0,  5, 14,  9,},
			},

			// S3
			{
			{10,  0,  9, 14,  6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,  8,},
			{13,  7,  0,  9,  3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15,  1,},
			{13,  6,  4,  9,  8, 15,  3,  0, 11,  1,  2, 12,  5, 10, 14,  7,},
			{1, 10, 13,  0,  6,  9,  8,  7,  4, 15, 14,  3, 11,  5,  2, 12,},
			},
			// S4 
			{
			{7, 13, 14,  3,  0,  6,  9, 10,  1,  2,  8,  5, 11, 12,  4, 15,},
			{13,  8, 11,  5,  6, 15,  0,  3,  4,  7,  2, 12,  1, 10, 14,  9,},
			{10,  6,  9,  0, 12, 11,  7, 13, 15,  1,  3, 14,  5,  2,  8,  4,},
			{3, 15,  0,  6, 10,  1, 13,  8,  9,  4,  5, 11, 12,  7,  2, 14,},
			},
			// S5
			{
			{2, 12,  4,  1,  7, 10, 11,  6,  8,  5,  3, 15, 13,  0, 14,  9,},
			{14, 11,  2, 12,  4,  7, 13,  1,  5,  0, 15, 10,  3,  9,  8,  6,},
			{4,  2,  1, 11, 10, 13,  7,  8, 15,  9, 12,  5,  6,  3,  0, 14,},
			{11,  8, 12,  7,  1, 14,  2, 13,  6, 15,  0,  9, 10,  4,  5,  3,},
			},
			// S6 
			{
			{12,  1, 10, 15,  9,  2,  6,  8,  0, 13,  3,  4, 14,  7,  5, 11,},
			{10, 15,  4,  2,  7, 12,  9,  5,  6,  1, 13, 14,  0, 11,  3,  8,},
			{9, 14, 15,  5,  2,  8, 12,  3,  7,  0,  4, 10,  1, 13, 11,  6,},
			{4,  3,  2, 12,  9,  5, 15, 10, 11, 14,  1,  7,  6,  0,  8, 13,},
			},
			// S7 
			{
			{4, 11,  2, 14, 15,  0,  8, 13,  3, 12,  9,  7,  5, 10,  6,  1,},
			{13,  0, 11,  7,  4,  9,  1, 10, 14,  3,  5, 12,  2, 15,  8,  6,},
			{1,  4, 11, 13, 12,  3,  7, 14, 10, 15,  6,  8,  0,  5,  9,  2,},
			{6, 11, 13,  8,  1,  4, 10,  7,  9,  5,  0, 15, 14,  2,  3, 12,},
			},
			// S8 
			{
			{13,  2,  8,  4,  6, 15, 11,  1, 10,  9,  3, 14,  5,  0, 12,  7,},
			{1, 15, 13,  8, 10,  3,  7,  4, 12,  5,  6, 11,  0, 14,  9,  2,},
			{7, 11,  4,  1,  9, 12, 14,  2,  0,  6, 10, 13, 15,  3,  5,  8,},
			{2,  1, 14,  7,  4, 10,  8, 13, 15, 12,  9,  0,  3,  5,  6, 11},
			},
		};

	private final int PTable[] =
		{
			16, 7, 20, 21, 29, 12, 28, 17, 1,  15, 23, 26, 5,  18, 31, 10,
			2,  8, 24, 14, 32, 27, 3,  9,  19, 13, 30, 6,  22, 11, 4,  25
		};

	private final int IPRTable[] =
		{
			40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31,
			38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29,
			36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27,
			34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41,  9, 49, 17, 57, 25
		};
	
	
	
	int[] Bytes2Bits (int[] input, int length) {
		int[] tmp = new int[input.length];
		System.arraycopy(input, 0, tmp, 0, input.length);
		int bits[] = new int[length];
		for(int i = 0; i < length; i ++){
			bits[i] = (tmp[i / 8] >> ((i & 7) ^ 7)) & 0x01;
		}
		return bits;
	}
	
	int[] TableChange (int[] input, int[] table, int outLen ){
		int[] out = new int[outLen]; 
		for(int i = 0; i < outLen; i++ )
		{
			out[i] = input[table[i] - 1];
		}
		return out;
	}

	int[] ArrayLeftRotate (int[] input, int len, int loop)
	{
		int[] inputtmp = new int[input.length];
		System.arraycopy(input, 0, inputtmp, 0, input.length);
		int	validLoop = loop % len;
		int[] tmp = new int[validLoop];
		int[] out = new int[len];
		
		System.arraycopy(inputtmp, 0, tmp, 0, validLoop);
		System.arraycopy(inputtmp, validLoop, out, 0, len - validLoop);
		System.arraycopy(tmp, 0, out, len - validLoop, validLoop);
		return out;
		
	}

	int[][] SetSubKey (int[] key)
	{
		int[] keyBits = new int[64];
		int[] t = new int[56];
		int	index = 0;
		int[] c = new int[56];
		int[] d = new int[28];
		int[][] DESSubKey = new int[16][];
		for(int i = 0; i < 16; i ++)
			DESSubKey[i] = new int[48];

		keyBits = Bytes2Bits (key, 64 );

		t = TableChange (keyBits, PC1Table, 56);
		System.arraycopy(t, 0, c, 0, t.length);
		System.arraycopy(t, 28, d, 0, t.length - 28);
		for ( index = 0; index != 16; index++ )
		{
			c = ArrayLeftRotate (c, 28, loopTable[index] );
			d = ArrayLeftRotate (d, 28, loopTable[index] );
			DESSubKey[index] = TableChange (t, PC2Table, 48 );
		}
		return DESSubKey;
	}

	int[] XorArray (int[] a, int[] b, int len)
	{
		int[] tmpa = new int[a.length];
		int[] tmpb = new int[b.length];
		int[] out = new int[len];
		System.arraycopy(a, 0, tmpa, 0, a.length);
		System.arraycopy(b, 0, tmpb, 0, b.length);
		for(int i = 0; i < len; i ++)
			out[i] = tmpa[i] ^ tmpb[i];
		return out;
	}

	int[] SBox (int[] in )
	{
		int[] out = new int[36];
		for (int i = 0; i != 8; i++ )
		{
			int r = (in[i * 6] << 1) + in[i * 6 + 5];
			int c = (in[i * 6 + 1] << 3) + (in[i * 6 + 2] << 2) + (in[i * 6 + 3] << 1) + in[i * 6 + 4];
			int tmp = SBoxTable[i][r][c];
			
			out[i * 4]		= ((tmp >> 3) & 1);
			out[i * 4 + 1]	= ((tmp >> 2) & 1);
			out[i * 4 + 2]	= ((tmp >> 1) & 1);
			out[i * 4 + 3]	= (tmp & 1);
		}
		return out;
	}

	int[] F (int[] in, int[] ki)
	{
		int[] tmp = new int[48];
		
		tmp = TableChange (in, ETable, 48 );
		tmp = XorArray (tmp, ki, 48 );
		tmp = SBox (tmp );
		int[] out = TableChange (tmp, PTable, 32 );
		return out;
	}

	int[] Bits2Bytes (int[] in, int bitLen )
	{
		int[] intmp = new int[in.length];
		System.arraycopy(in, 0, intmp, 0, in.length);
		
		int[] out = new int[bitLen / 8];

		for (int i = 0; i != bitLen; i++ )
		{
			out[i >> 3] |= intmp[i] << ((i & 7) ^ 7);
		}
		return out;
	}

	int[] DESGo (int[] in, int[] key, int enorde )
	{
		
		int index = 0;
		int[] inPtr = new int[in.length];
		int[] keyPtr = new int[key.length];
		int[] rTmp = new int[32];
		System.arraycopy(in, 0, inPtr, 0, in.length);
		System.arraycopy(key, 0, keyPtr, 0, key.length);

		int[] bitTmp = Bytes2Bits (in, 64 );

		int[] lr = TableChange (bitTmp, IPTable, 64 );
		int[] l = new int[lr.length];
		System.arraycopy(lr, 0, l, 0, lr.length);
		
		int[] r = new int[32];
		System.arraycopy(lr, 32, r, 0, 32);
		
		int[][] DESSubKey = SetSubKey (keyPtr);
		if ( 0 == enorde )
		{
			for ( index = 0; index != 16; index++ )
			{
				System.arraycopy(r, 0, rTmp, 0, 32);
				r = F (r, DESSubKey[index] );
				r = XorArray (l, r, 32 );
				System.arraycopy(rTmp, 0, l, 0, 32);
			}
		}
		else
		{
			for ( index = 16; index != 0; index-- )
			{
				System.arraycopy(r, 0, rTmp, 0, 32);
				r = F (r, DESSubKey[index - 1] );
				r = XorArray (l, r, 32 );
				System.arraycopy(rTmp, 0, l, 0, 32);
			}
		}
		System.arraycopy(r, 0, bitTmp, 0, 32);
		System.arraycopy(l, 0, bitTmp, 32, 32);
		

		lr = TableChange (bitTmp, IPRTable, 64 );

		int[] out = Bits2Bytes (lr, 64 );
		return out;
	}


	public int[] DES3Go (int[] in, int[] key, int enorde )
	{
		int[] inPtr = new int[in.length];
		int[] keyPtr = new int[key.length];

		int[] keyL = new int[8];
		int[] keyR = new int[8];
		int[] tmp = new int[8];
		
		System.arraycopy(in, 0, inPtr, 0, in.length);
		System.arraycopy(key, 0, keyPtr, 0, keyPtr.length);

		System.arraycopy(keyPtr, 0, keyL, 0, 8);
		System.arraycopy(keyPtr, 8, keyR, 0, 8);
		

		tmp = DESGo (inPtr, keyL, enorde );
		tmp = DESGo (tmp, keyR, 1^enorde );
		int[] outPtr = DESGo (tmp, keyL, enorde );
		return outPtr;
	}

}
