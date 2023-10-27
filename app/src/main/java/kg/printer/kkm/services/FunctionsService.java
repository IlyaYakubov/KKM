package kg.printer.kkm.services;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FunctionsService {

	static public int isOdd(int num) {
		return num & 0x1;
	}

	static public byte HexToByte(String inHex) {
		return (byte) Integer.parseInt(inHex,16);
	}

	static public String Byte2Hex(Byte inByte) {
		return String.format("%02x", inByte).toUpperCase();
	}

	static public String ByteArrToHex(byte[] inBytArr) {
		StringBuilder strBuilder=new StringBuilder();
		int j=inBytArr.length;
		for (int i = 0; i < j; i++)	{
			strBuilder.append(Byte2Hex(inBytArr[i]));
			strBuilder.append(" ");
		}
		return strBuilder.toString();
	}

	static public byte[] HexToByteArr(String inHex)	{
		int hexlen = inHex.length();
		byte[] result;
		if (isOdd(hexlen)==1) {
			hexlen++;
			result = new byte[(hexlen/2)];
			inHex="0"+inHex;
		}else {
			result = new byte[(hexlen/2)];
		}
		int j=0;
		for (int i = 0; i < hexlen; i+=2) {
			result[j]=HexToByte(inHex.substring(i,i+2));
			j++;
		}
		return result;
	}

    public static class SavingMediaFileService {

        public static final int MEDIA_TYPE_IMAGE = 1;
        public static final int MEDIA_TYPE_VIDEO = 2;

        public static final String DIR_CAPTURE = "Capture";

        private SavingMediaFileService() {
            throw new UnsupportedOperationException("cannot be instantiated");
        }

        public static boolean isExternalStorageWritable() {
            String state = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(state);
        }

        public static Uri getOutputMediaFileUri(Context context, String dirName, int type) {
            return Uri.fromFile(getOutputMediaFile(context, dirName, type));
        }

        public static File getOutputMediaFile(Context context, String dirName, int type) {
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.

            File mediaStorageDir =
                    new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), dirName);
            // a standard location for saving pictures and videos which are
            // associated with your application. If your application is uninstalled,
            // any files saved in this location are removed. Security is not
            // enforced for files in this location and other applications may read,
            // change and delete them.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "IMG_" + timeStamp + ".jpg");
            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "VID_" + timeStamp + ".mp4");
            } else {
                return null;
            }

            return mediaFile;
        }

    }
}
