package com.pponna.util;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The MRDynamicSplit.java.
 * 
 * @author Pratheesh
 */
public class MRDynamicSplit {

    /** The value containing MB_IN_BYTES. */
    private final static long MB_IN_BYTES = 1024 * 1024;

    /** The value containing DFS_BLOCK_SIZE. */
    private final static String DFS_BLOCK_SIZE = "dfs.blocksize";

    /** The value containing CONVERT_TO_BYTES. */
    public static final double CONVERT_TO_BYTES = 1048576;

    /** The value containing ZERO. */
    public static final int ZERO = 0;

    /** Static containing Logger for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MRDynamicSplit.class);

    /**
     * Method to calculate split size
     * 
     * @param conf
     * @param fileSize
     * @param maxSplits
     * @param maxSplitSize
     * @return
     */
    private long calculateSplitSize(Configuration conf, long fileSize, int maxSplits, long maxSplitSize) {

        long blockSize = 128 * 1024 * 1024;
        if (null != conf.get(DFS_BLOCK_SIZE)) {
            blockSize = Long.parseLong(conf.get(DFS_BLOCK_SIZE));
        } else {
            LOGGER.info("Using block size 128 MB");
        }
        double splitSize = 10.5;
        fileSize /= MB_IN_BYTES;
        blockSize /= MB_IN_BYTES;
        maxSplitSize /= MB_IN_BYTES;
        double tempSplitSize = (fileSize <= blockSize) ? splitSize : maxSplitSize;
        int tempSplits = 128;
        if (fileSize <= 0) {
            splitSize = 0.5;
        } else {
            do {
                splitSize = tempSplitSize;
                tempSplitSize = splitSize - 0.5;
                if (tempSplitSize >= 0.5) {
                    tempSplits = (int) (fileSize / tempSplitSize);
                }
            } while (tempSplits < maxSplits && tempSplits >= 0 && tempSplitSize >= 0.5);
        }
        LOGGER.info("fileSize : {} | splitSize : {} | splits : {}", fileSize, splitSize, ((int) (fileSize / splitSize)));
        return convertToBytes(splitSize);
    }

    /**
     * Method to convert size in MB to Bytes
     * 
     * @param splitSizeInMB
     * @return
     */
    private long convertToBytes(final double splitSizeInMB) {

        double valueInBytes = ZERO;
        valueInBytes = CONVERT_TO_BYTES * splitSizeInMB;
        return (long) valueInBytes;
    }

    /**
     * Method to invoke the main class
     * 
     * @param args
     */
    public static void main(String[] args) {

        if (args.length != 3) {
            LOGGER.error("Usage : java -cp <hadoop classpath jars>:<utility jar> com.pponna.util.MRDynamicSplit <fileSizeInBytes> <maxNoOfSplits> <maxSplitSizeInBytes>");
            System.exit(0);
        }
        if (!StringUtils.isBlank(args[0]) && !StringUtils.isBlank(args[1]) && !StringUtils.isBlank(args[2])) {
            int maxSplits = Integer.parseInt(args[1]);
            long maxSplitSize = Long.parseLong(args[2]);
            long fileSiezInBytes = Long.parseLong(args[0]);
            Configuration conf = new Configuration();
            MRDynamicSplit dynamicSplit = new MRDynamicSplit();
            LOGGER.info("Calculated Split Size :: {}",
                    dynamicSplit.calculateSplitSize(conf, fileSiezInBytes, maxSplits, maxSplitSize));
        } else {
            LOGGER.error("Usage : java -cp <hadoop classpath jars>:<utility jar> com.pponna.util.MRDynamicSplit <fileSizeInBytes> <maxNoOfSplits> <maxSplitSizeInBytes>");
        }
    }
}
