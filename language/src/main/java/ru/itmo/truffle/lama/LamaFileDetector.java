package ru.itmo.truffle.lama;

import java.io.IOException;
import java.nio.charset.Charset;

import com.oracle.truffle.api.TruffleFile;
import com.oracle.truffle.api.TruffleFile.FileTypeDetector;

public class LamaFileDetector implements FileTypeDetector {
	@Override
	public String findMimeType(TruffleFile file) throws IOException {
		return file.getName().endsWith(".lama")
				? LamaLanguage.MIME_TYPE
				: null;
	}

	@Override
	public Charset findEncoding(TruffleFile file) throws IOException {
		return null;
	}
}
