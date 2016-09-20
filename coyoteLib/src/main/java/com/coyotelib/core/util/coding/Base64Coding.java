package com.coyotelib.core.util.coding;

import java.io.UnsupportedEncodingException;

import android.util.Base64;

public class Base64Coding extends AbstractCoding {

	@Override
	public byte[] encode(byte[] input) {
		
		String encodedStr =  Base64.encodeToString(input, Base64.URL_SAFE);//NewBase64.getEncoder().encodeToString(input);
		if (encodedStr == null)
			return null;
		try {
			return encodedStr.getBytes(UTF8_CHARSET);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	@Override
	public byte[] decode(byte[] input) {
		if (input == null)
			return null;
		return Base64.decode(input, Base64.URL_SAFE);//NewBase64.getDecoder().decode(input);//Base64old.decode(new String(input, UTF8_CHARSET));
	}

	@Override
	public String encodeBytesToUTF8(byte[] input) {
		return Base64.encodeToString(input, Base64.URL_SAFE);//NewBase64.getEncoder().encodeToString(input);// Base64old.encode(input);
	}

	@Override
	public byte[] decodeUTF8ToBytes(String encodedStr) {
		return Base64.decode(encodedStr, Base64.URL_SAFE);
				//NewBase64.getDecoder().decode(encodedStr);//Base64old.decode(encodedStr);
	}
}
