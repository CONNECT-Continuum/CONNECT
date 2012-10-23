/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package gov.hhs.fha.nhinc.largefile;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.activation.DataHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.aegis.type.mtom.StreamDataSource;
import org.apache.cxf.attachment.ByteDataSource;

public class LargeFileUtils {
    private static Log log = null;

    private static LargeFileUtils INSTANCE = new LargeFileUtils();
    private static String ATTACHMENT_FILE_PREFIX = "nhin";
    private static String ATTACHMENT_FILE_SUFFIX = ".clf";

    LargeFileUtils() {
        log = createLogger();
    };

    /**
     * Returns the singleton instance of this class.
     * 
     * @return the singleton instance
     */
    public static LargeFileUtils getInstance() {
        return INSTANCE;
    }

    /**
     * Returns true if the gateway is configured to parse document payload as a URI pointing to the data rather than
     * having the actual data itself.
     * 
     * @return true if the gateway should process document payload as a URI.
     */
    public boolean isParsePayloadAsFileLocationEnabled() {
        try {
            return PropertyAccessor.getInstance().getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.PARSE_PAYLOAD_AS_FILE_URI_OUTBOUND);
        } catch (PropertyAccessException pae) {
            log.error("Failed to determine if payload should be parsed as a file location.  Will assume false.", pae);
        }

        return false;
    }

    /**
     * Returns true if the gateway is configured to save incoming payload to the file system.
     * 
     * @return true if the gateway is to save payloads on the file system
     */
    public boolean isSavePayloadToFileEnabled() {
        try {
            return PropertyAccessor.getInstance().getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.SAVE_PAYLOAD_TO_FILE_INBOUND);
        } catch (PropertyAccessException pae) {
            log.error("Failed to determine if payload should be saved to a file location.  Will assume false.", pae);
        }

        return false;
    }

    /**
     * Parse the data source of the data handler as a base 64 encoded URI string.
     * 
     * @param dh - the data handler that contains the data source to parse
     * @return URI representing the data inside the data handler
     * @throws IOException
     * @throws URISyntaxException
     */
    public URI parseBase64DataAsUri(DataHandler dh) throws IOException, URISyntaxException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = dh.getInputStream();
            int numRead = 1024;
            byte[] buf = new byte[numRead];
            if ((numRead = is.read(buf)) >= 0) {
                baos.write(buf, 0, numRead);
            }
        } finally {
            closeStreamWithoutException(is);
            closeStreamWithoutException(baos);
        }

        String uriString = new String(baos.toByteArray());
        return new URI(uriString);
    }

    /**
     * Saves the data handler to a file in the configured payload directory.
     * 
     * @param dh - the data handler to convert to a file
     * @return
     * @throws IOException
     */
    public File saveDataToFile(DataHandler dh) throws IOException {
        File attachmentFile = generateAttachmentFile();
        saveDataToFile(dh, attachmentFile);

        return attachmentFile;
    }

    /**
     * Save the data handler to the given file. The data handler will be empty at the end of this call.
     * 
     * @param dh - the data handler to convert to a file
     * @param file - the file containing the data from the data handler
     * @throws IOException
     */
    public void saveDataToFile(DataHandler dh, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        InputStream is = null;
        try {
            is = dh.getInputStream();
            int numRead = 1024;
            byte[] buf = new byte[numRead];
            while ((numRead = is.read(buf)) >= 0) {
                fos.write(buf, 0, numRead);
            }
        } finally {
            closeStreamWithoutException(is);
            closeStreamWithoutException(fos);
        }
    }

    /**
     * Closes the input stream and silently catches the exception.
     * 
     * @param is - the input stream to close
     */
    public void closeStreamWithoutException(InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (Exception e) {
            log.warn("Failed to close input stream");
        }
    }

    /**
     * Closes the output stream and silently catches the exception.
     * 
     * @param os - the output stream to close
     */
    public void closeStreamWithoutException(OutputStream os) {
        try {
            if (os != null) {
                os.close();
            }
        } catch (Exception e) {
            log.warn("Failed to close output stream");
        }
    }

    /**
     * Converts the given file into a data handler with a StreamDataSource.
     * 
     * @param file - the file to convert
     * @return the data handler representing the file
     * @throws IOException
     */
    public DataHandler convertToDataHandler(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException(
                    "Payload file location points to does not exists.  Please ensure that the file path is base64 encoded. "
                            + file.getAbsolutePath());
        }

        FileInputStream fis = new FileInputStream(file);
        StreamDataSource sds = new StreamDataSource("application/octect-stream", fis);

        return new DataHandler(sds);
    }

    /**
     * Converts the given string into a data handler with a ByteDataSource.
     * 
     * @param data - the data to convert
     * @return the data handler representing the string
     * @throws IOException
     */
    public DataHandler convertToDataHandler(String data) {
        return convertToDataHandler(data.getBytes());
    }

    /**
     * Converts the given byte array into a data handler with a ByteDataSource.
     * 
     * @param data - the data to convert
     * @return the data handler representing the bytes
     * @throws IOException
     */
    public DataHandler convertToDataHandler(byte[] data) {
        ByteDataSource bds = new ByteDataSource(data);
        return new DataHandler(bds);
    }

    /**
     * Saves the data handler as a byte array. The data handler will be empty at the end of this call.
     * 
     * @param dh - the data handler to convert
     * @return a byte array containing the data from the data handler
     * @throws IOException
     */
    public byte[] convertToBytes(DataHandler dh) throws IOException {
        InputStream is = dh.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int read = 0;
        byte[] bytes = new byte[1024];
        while ((read = is.read(bytes)) != -1) {
            baos.write(bytes, 0, read);
        }

        return baos.toByteArray();
    }

    /**
     * Generates a file to be used for saving payload attachments. The file will be created at the configured payload
     * directory or at the java tmp directory if the payload directory does not exists.
     * 
     * @return
     * @throws IOException
     */
    public File generateAttachmentFile() throws IOException {
        String payloadSaveDirectory = getPayloadSaveDirectory();
        return generateAttachmentFile(payloadSaveDirectory);
    }

    /**
     * Generates a file to be used for saving payload attachments. The file will be created at the configured payload
     * directory or at the java tmp directory if the payload directory does not exists.
     * 
     * @param payloadSaveDirectory - directory where the attachment file will be created
     * @return generated file
     * @throws IOException
     */
    public File generateAttachmentFile(String payloadSaveDirectory) throws IOException {
        File parentDir = null;
        if (payloadSaveDirectory != null) {
            parentDir = new File(payloadSaveDirectory);
            if (!parentDir.exists()) {
                log.warn("Payload save directory does not exists.  Defaulting to use java tmp directory.");
                parentDir = null;
            }
        }

        return File.createTempFile(ATTACHMENT_FILE_PREFIX, ATTACHMENT_FILE_SUFFIX, parentDir);
    }

    protected String getPayloadSaveDirectory() {
        try {
            return PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.PAYLOAD_SAVE_DIRECTORY);
        } catch (PropertyAccessException pae) {
            log.error("Failed to determine payload save directory.  Is " + NhincConstants.PAYLOAD_SAVE_DIRECTORY
                    + " set in gateway.properties?", pae);
        }

        return null;
    }

    protected Log createLogger() {
        if (log == null) {
            log = LogFactory.getLog(LargeFileUtils.class);
        }
        return log;
    }
}