package com.project_b.se2.mauerhuepfer;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.logging.Logger;

/**
 * Created by rohrbe on 14.05.2016.
 */
public class ObjectSerializer {
    private final static Logger LOGGER = Logger.getLogger("ObjectSerializerClass");

    private ObjectSerializer() {

    }

    public static byte[] Serialize(Serializable s) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;

        byte[] bytes = new byte[0];
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(s);
            bytes = bos.toByteArray();
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LOGGER.info(e.getMessage());
            }
            try {
                bos.close();
            } catch (IOException e) {
                LOGGER.info(e.getMessage());
            }
        }
        return bytes;
    }

    public static Serializable DeSerialize(byte[] b) {
        ByteArrayInputStream bis = new ByteArrayInputStream(b);
        ObjectInput in = null;
        Object o = null;
        try {
            in = new ObjectInputStream(bis);
            o = in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.info(e.getMessage());
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                LOGGER.info(e.getMessage());
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                LOGGER.info(e.getMessage());
            }
        }
        return (Serializable) o;
    }
}
