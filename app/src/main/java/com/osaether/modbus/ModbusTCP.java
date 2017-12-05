package com.osaether.modbus;

import android.support.annotation.NonNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ModbusTCP {
    private String host_;
    private int port_ = 502;
    private byte slaveID_ = 1;
    private int timeout_ = 5000;
    private int trxID_ = 0;
    private int read_coils = 0x01;
    private int read_discrete_inputs = 0x02;
    private int read_holding_registers = 0x03;
    private int read_input_registers = 0x04;
    private int write_single_coil = 0x05;
    private int write_single_register = 0x06;
    private Socket socket_ = new Socket();

    public ModbusTCP(@NonNull String host) {
        host_ = host;
    }

    public ModbusTCP(@NonNull String host, int port) {
        host_ = host;
        port_ = port;
    }

    public ModbusTCP(@NonNull String host, int port, byte slaveID) {
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

        byte[] cmd = createModbusCommand(addr, len, read_input_registers);
        DataOutputStream out = new DataOutputStream(socket_.getOutputStream());
        DataInputStream in = new DataInputStream(socket_.getInputStream());

        out.write(cmd);
        int n = in.read(header, 0, 9);
        if (n != 9)
        {
            throw new IOException("Modbus header not received correctly");
        }
        n = in.read(resp, 0, len*2);
        if (n != len*2)
        {
            throw new IOException("Modbus response not received correctly");
        }
        short[] data = convertBytestoShort(resp, len);

        trxID_ = (trxID_ + 1) & 0xffff;
        return data;
    }

    private byte[] createModbusCommand(int addr, int len, int opcode) {
        byte[] cmd = new byte[12];

        cmd[0] = (byte)((trxID_ >> 8) & 0xff);
        cmd[1] = (byte)(trxID_ & 0xff);
        // Protocol identifier (always 0):
        cmd[2] = 0;
        cmd[3] = 0;
        // Two byte message length
        cmd[4] = 0;
        cmd[5] = 6;
        cmd[6] = slaveID_;
        cmd[7] = (byte)opcode;
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
