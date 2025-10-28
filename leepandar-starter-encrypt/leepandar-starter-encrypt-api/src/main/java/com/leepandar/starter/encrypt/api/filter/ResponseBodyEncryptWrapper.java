package com.leepandar.starter.encrypt.api.filter;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.http.HttpHeaders;
import com.leepandar.starter.core.constant.StringConstants;
import com.leepandar.starter.encrypt.util.EncryptUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * 响应体加密包装类
 */
public class ResponseBodyEncryptWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream byteArrayOutputStream;
    private final ServletOutputStream servletOutputStream;
    private final PrintWriter printWriter;

    public ResponseBodyEncryptWrapper(HttpServletResponse response) throws IOException {
        super(response);
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.servletOutputStream = this.getOutputStream();
        this.printWriter = new PrintWriter(new OutputStreamWriter(byteArrayOutputStream));
    }

    @Override
    public PrintWriter getWriter() {
        return printWriter;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (servletOutputStream != null) {
            servletOutputStream.flush();
        }
        if (printWriter != null) {
            printWriter.flush();
        }
    }

    @Override
    public void reset() {
        byteArrayOutputStream.reset();
    }

    public byte[] getResponseData() throws IOException {
        flushBuffer();
        return byteArrayOutputStream.toByteArray();
    }

    public String getContent() throws IOException {
        flushBuffer();
        return byteArrayOutputStream.toString();
    }

    /**
     * 获取加密内容
     *
     * @param response        响应对象
     * @param publicKey       RSA公钥
     * @param secretKeyHeader 密钥头
     * @return 加密内容
     */
    public String getEncryptContent(HttpServletResponse response,
                                    String publicKey,
                                    String secretKeyHeader) throws IOException {
        // 生成 AES 密钥
        String aesSecretKey = RandomUtil.randomString(32);
        // Base64 编码
        String secretKeyByBase64 = EncryptUtils.encodeByBase64(aesSecretKey);
        // RSA 加密
        String secretKeyByRsa = EncryptUtils.encryptByRsa(secretKeyByBase64, publicKey);
        // 设置响应头
        response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, secretKeyHeader);
        response.setHeader(secretKeyHeader, secretKeyByRsa);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, StringConstants.ASTERISK);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, StringConstants.ASTERISK);
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        // 通过 AES 密钥，对原始内容进行加密
        return EncryptUtils.encryptByAes(this.getContent(), aesSecretKey);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }

            @Override
            public void write(int b) throws IOException {
                byteArrayOutputStream.write(b);
            }

            @Override
            public void write(byte[] b) throws IOException {
                byteArrayOutputStream.write(b);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                byteArrayOutputStream.write(b, off, len);
            }
        };
    }

}
