package com.osaether.modbus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by Ole on 30.03.2017.
 */

public class ModbusTCP {
    private String host_;
    private int port_ = 502;
    private byte slaveID_ = 1;
    private int timeout_ = 5000;
    private int trxID_ = 0;
    private Socket socket_ = new Socket();

    public ModbusTCP(String host) {
        host_ = host;
    }

    public ModbusTCP(String host, int port) {
        host_ = host;
        port_ = port;
    }

    public ModbusTCP(String host, int port, byte slaveID) {
        host_ = host;
        port_ = port;
        slaveID_ = slaveID;
    }

    public void setPort(int port) {
        port_ = port;
    }

    public void setSlaveID(byte slaveID) {
        slaveID_ = slaveID;
    }

    public void connect() throws java.io.IOException {
        socket_.connect(new InetSocketAddress(host_, port_), timeout_);
        socket_.setSoTimeout(timeout_);
    }

    public void close() throws java.io.IOException {
        socket_.close();
    }

    public short[] readInputRegisters(int addr, int len) throws java.io.IOException {
        byte[] header = new byte[9];
        byte[] resp = new byte[len*2];

        byte[] cmd = createModbusCommand(addr, len);
        DataOutputStream out = new DataOutputStream(socket_.getOutputStream());
        DataInputStream in = new DataInputStream(socket_.getInputStream());

        out.write(cmd);
        int n = in.read(header, 0, 9);
        n = in.read(resp, 0, len*2);
        short[] tsdata = convertBytestoShort(resp, len);

        trxID_ = (trxID_ + 1) & 0xffff;
        return tsdata;
    }

    private byte[] createModbusCommand(int addr, int len) {
        byte[] cmd = new byte[12];

        cmd[0] = (byte)((trxID_ >> 8) & 0xff);
        cmd[1] = (byte)(trxID_ & 0xff);
        cmd[2] = 0;
        cmd[3] = 0;
        cmd[4] = 0;
        cmd[5] = 6; // Message length that follow
        cmd[6] = slaveID_;
        cmd[7] = 4; // Read input register
        cmd[8] = (byte)((addr >> 8) & 0xff);
        cmd[9] = (byte)(addr & 0xff);
        cmd[10] = (byte)((len >> 8) & 0xff);
        cmd[11] = (byte)(len & 0xff);

        return cmd;
    }

    private short[] convertBytestoShort(byte[] inp, int len) {
        short[] outp = new short[len];

        ByteBuffer buf = ByteBuffer.wrap(inp);
        for (int i=0;i<len;i++)
        {
            outp[i] = buf.getShort(2*i);
        }
        return outp;
    }
}
